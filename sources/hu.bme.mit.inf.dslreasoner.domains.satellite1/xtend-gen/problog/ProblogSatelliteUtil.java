package problog;

import hu.bme.mit.inf.dslreasoner.domains.satellite1.SatelliteModelWrapper;
import hu.bme.mit.inf.measurement.utilities.CSVLog;
import hu.bme.mit.inf.measurement.utilities.Config;
import hu.bme.mit.inf.measurement.utilities.configuration.SatelliteConfiguration;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public interface ProblogSatelliteUtil {
  default boolean runProblog(final SatelliteConfiguration cfg, final SatelliteModelWrapper instance, final CSVLog log) {
    try {
      String _probLogFile = cfg.getProbLogFile();
      final File file = new File(_probLogFile);
      file.createNewFile();
      final FileWriter writer = new FileWriter(file);
      String _probLogFile_1 = cfg.getProbLogFile();
      final ProcessBuilder builder = new ProcessBuilder("problog", _probLogFile_1);
      final long start = System.nanoTime();
      final String plmodel = new Generation().generateFrom(instance.mission).toString();
      writer.write(plmodel);
      writer.flush();
      writer.close();
      final long trafo = System.nanoTime();
      final Process process = builder.start();
      final AtomicBoolean timeoutFlag = new AtomicBoolean();
      final Procedure0 _function = () -> {
        try {
          timeoutFlag.set(true);
          String _string = Long.valueOf(process.pid()).toString();
          Runtime.getRuntime().exec(new String[] { "kill", "-9", _string });
        } catch (Throwable _e) {
          throw Exceptions.sneakyThrow(_e);
        }
      };
      final Timer timeout = Config.timeout(cfg.getTimeoutS(), _function);
      final HashMap<String, Object> output = new HashMap<String, Object>();
      InputStream _inputStream = process.getInputStream();
      final Scanner io = new Scanner(_inputStream);
      final Procedure1<String> _function_1 = (String line) -> {
        final Matcher match = cfg.getProbLogPattern().matcher(line);
        boolean _find = match.find();
        if (_find) {
          output.put(match.group(1), Double.valueOf(Double.parseDouble(match.group(2))));
        }
      };
      IteratorExtensions.<String>forEach(io, _function_1);
      final long end = System.nanoTime();
      timeout.cancel();
      log.log("problog.total[ms]", Double.valueOf((((end - start) / 1000.0) / 1000)));
      log.log("problog.trafo[ms]", Double.valueOf((((trafo - start) / 1000.0) / 1000)));
      log.log("problog.evaluation[ms]", Double.valueOf((((end - trafo) / 1000.0) / 1000)));
      Object _xifexpression = null;
      boolean _isEmpty = output.values().isEmpty();
      if (_isEmpty) {
        _xifexpression = Integer.valueOf(0);
      } else {
        _xifexpression = ((Object[])Conversions.unwrapArray(output.values(), Object.class))[0];
      }
      log.log("problog.result", _xifexpression);
      log.log("problog.timeout", Boolean.valueOf(timeoutFlag.get()));
      Object _xifexpression_1 = null;
      boolean _isEmpty_1 = output.values().isEmpty();
      if (_isEmpty_1) {
        _xifexpression_1 = Integer.valueOf(0);
      } else {
        _xifexpression_1 = ((Object[])Conversions.unwrapArray(output.values(), Object.class))[0];
      }
      LogHelper.LOG4J.info("ProbLog completed in {}ms with result {} (timeout: {})", Double.valueOf((((end - start) / 1000.0) / 1000)), _xifexpression_1, 
        Boolean.valueOf(timeoutFlag.get()));
      return timeoutFlag.get();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
