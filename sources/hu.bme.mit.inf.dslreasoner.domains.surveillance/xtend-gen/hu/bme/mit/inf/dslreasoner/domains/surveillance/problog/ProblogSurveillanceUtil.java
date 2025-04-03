package hu.bme.mit.inf.dslreasoner.domains.surveillance.problog;

import hu.bme.mit.inf.dslreasoner.domains.surveillance.viatra.SurveillanceWrapper;
import hu.bme.mit.inf.measurement.utilities.CSVLog;
import hu.bme.mit.inf.measurement.utilities.Config;
import hu.bme.mit.inf.measurement.utilities.configuration.SurveillanceConfiguration;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public interface ProblogSurveillanceUtil {
  default Object runProblog(final SurveillanceConfiguration cfg, final SurveillanceWrapper instance, final CSVLog log) {
    try {
      Object _xblockexpression = null;
      {
        String _probLogFile = cfg.getProbLogFile();
        final File file = new File(_probLogFile);
        file.createNewFile();
        final FileWriter writer = new FileWriter(file);
        String _probLogFile_1 = cfg.getProbLogFile();
        final ProcessBuilder builder = new ProcessBuilder("problog", _probLogFile_1);
        Process process = null;
        final long start = System.nanoTime();
        final String plmodel = new ProblogSurveillanceGenerator().generateFrom(instance.model).toString();
        writer.write(plmodel);
        writer.flush();
        writer.close();
        final long trafo = System.nanoTime();
        process = builder.start();
        final Process process2 = process;
        final AtomicBoolean timeoutFlag = new AtomicBoolean();
        final Procedure0 _function = () -> {
          InputOutput.<String>println("Run cancelled with timeout.");
          timeoutFlag.set(true);
          process2.destroyForcibly();
        };
        final Timer timeout = Config.timeout(cfg.getTimeoutS(), _function);
        final HashMap<String, Object> output = new HashMap<String, Object>();
        InputStream _inputStream = process.getInputStream();
        final Scanner io = new Scanner(_inputStream);
        final Procedure1<String> _function_1 = (String line) -> {
          InputOutput.<String>println(("Debug: " + line));
          final Matcher match = cfg.getProbLogPattern().matcher(line);
          boolean _find = match.find();
          if (_find) {
            output.put(match.group(1), Double.valueOf(Double.parseDouble(match.group(2))));
          }
        };
        IteratorExtensions.<String>forEach(io, _function_1);
        final long end = System.nanoTime();
        timeout.cancel();
        InputStream _inputStream_1 = process.getInputStream();
        final Procedure1<String> _function_2 = (String line) -> {
          InputOutput.<String>println(("Debug problog.err: " + line));
        };
        IteratorExtensions.<String>forEach(new Scanner(_inputStream_1), _function_2);
        log.log("problog.total[ms]", Double.valueOf((((end - start) / 1000.0) / 1000)));
        log.log("problog.trafo[ms]", Double.valueOf((((trafo - start) / 1000.0) / 1000)));
        log.log("problog.evaluation[ms]", Double.valueOf((((end - trafo) / 1000.0) / 1000)));
        log.log("problog.result", this.problogToJSON(instance, output, timeoutFlag.get()));
        _xblockexpression = log.log("problog.timeout", Boolean.valueOf(timeoutFlag.get()));
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  default String problogToJSON(final SurveillanceWrapper instance, final Map<String, Object> data, final boolean timeout) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("\"valid\" : ");
    _builder.append((!timeout), "\t");
    _builder.append(",");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("\"matches\" : [");
    _builder.newLine();
    {
      Set<Map.Entry<String, Object>> _entrySet = data.entrySet();
      boolean _hasElements = false;
      for(final Map.Entry<String, Object> entry : _entrySet) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "\t\t");
        }
        _builder.append("\t\t");
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("\t");
        _builder.append("\"object\" : \"object");
        Integer _get = instance.ordering.get(instance.ofHashCode(Integer.parseInt(entry.getKey())));
        _builder.append(_get, "\t\t\t");
        _builder.append("\",");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("\t");
        _builder.append("\"probability\" : ");
        Object _value = entry.getValue();
        _builder.append(_value, "\t\t\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("]");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
}
