package hu.bme.mit.inf.dslreasoner.domains.surveillance.viatra;

import hu.bme.mit.inf.dslreasoner.domains.surveillance.problog.ProblogSurveillanceGenerator;
import hu.bme.mit.inf.measurement.utilities.CSVLog;
import hu.bme.mit.inf.measurement.utilities.Config;
import hu.bme.mit.inf.measurement.utilities.configuration.SurveillanceConfiguration;
import hu.bme.mit.inf.measurement.utilities.viatra.SuspendedQueryEngine;
import hu.bme.mit.inf.measurement.utilities.viatra.ViatraScaleRunner;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParsingResults;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import reliability.intreface.Configuration;
import reliability.intreface.ExecutionTime;
import surveillance.SurveillancePackage;

@SuppressWarnings("all")
public class SurveillanceScaleRunner extends ViatraScaleRunner<SurveillanceConfiguration> {
  private final SurveillanceModelGenerator modelgen = new SurveillanceModelGenerator();

  private SurveillanceWrapper instance;

  public SurveillanceScaleRunner(final SurveillanceConfiguration cfg) {
    super(cfg, SurveillancePackage.eINSTANCE);
  }

  @Override
  public void preRun(final int seed) {
    this.instance = this.modelgen.make(this.cfg.getSize(), seed);
    this.domainResource.getContents().add(this.instance.model);
  }

  @Override
  public void initIncremental() {
    super.initIncremental();
    this.initializePatterns(this.engine, "elimination");
  }

  @Override
  public void runIncremental(final CSVLog log) {
    Configuration.enable();
    try {
      ExecutionTime.reset();
      final Procedure0 _function = () -> {
        InputOutput.<String>println("Run cancelled with timeout.");
        Configuration.cancel();
        boolean _isDisposed = this.engine.isDisposed();
        boolean _not = (!_isDisposed);
        if (_not) {
          this.engine.dispose();
        }
        log.log("timeout", Boolean.valueOf(true));
      };
      final Timer timeout = Config.timeout(this.cfg.getTimeoutS(), _function);
      final long it0start = System.nanoTime();
      this.engine.enableAndPropagate();
      final long it0sync = this.MDD.unaryForAll(this.engine);
      final String coverage = this.getMatchesJSON(this.parsed, this.engine, this.instance.ordering);
      final long it0end = System.nanoTime();
      timeout.cancel();
      final long it0prop = ExecutionTime.time();
      log.log("incremental.total[ms]", Double.valueOf((((it0end - it0start) / 1000.0) / 1000)));
      log.log("incremental.sync[ms]", Double.valueOf(((it0sync / 1000.0) / 1000)));
      log.log("incremental.prop[ms]", Double.valueOf(((it0prop / 1000.0) / 1000)));
      boolean _isTainted = this.engine.isTainted();
      boolean _not = (!_isTainted);
      log.log("incremental.healthy", Boolean.valueOf(_not));
      log.log("incremental.result", coverage);
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        InputOutput.<String>println("Cancellation caught.");
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    } finally {
      log.log("incremental.timeout", Boolean.valueOf(Configuration.isCancelled()));
      InputOutput.<String>println("Finally block executed.");
    }
  }

  @Override
  public void runProblog(final CSVLog log) {
    try {
      String _probLogFile = this.cfg.getProbLogFile();
      final File file = new File(_probLogFile);
      file.createNewFile();
      final FileWriter writer = new FileWriter(file);
      String _probLogFile_1 = this.cfg.getProbLogFile();
      final ProcessBuilder builder = new ProcessBuilder("problog", _probLogFile_1);
      Process process = null;
      final long start = System.nanoTime();
      final String plmodel = new ProblogSurveillanceGenerator().generateFrom(this.instance.model).toString();
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
        process2.destroy();
      };
      final Timer timeout = Config.timeout(this.cfg.getTimeoutS(), _function);
      final HashMap<String, Object> output = new HashMap<String, Object>();
      InputStream _inputStream = process.getInputStream();
      final Scanner io = new Scanner(_inputStream);
      final Procedure1<String> _function_1 = (String line) -> {
        InputOutput.<String>println(("Debug: " + line));
        final Matcher match = this.cfg.getProbLogPattern().matcher(line);
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
      log.log("problog.result", this.problogToJSON(output, timeoutFlag.get()));
      log.log("problog.timeout", Boolean.valueOf(timeoutFlag.get()));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  public String problogToJSON(final Map<String, Object> data, final boolean timeout) {
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
        Integer _get = this.instance.ordering.get(this.instance.ofHashCode(Integer.parseInt(entry.getKey())));
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

  public String getMatchesJSON(final PatternParsingResults parsed, final SuspendedQueryEngine engine, final Map<EObject, Integer> index) {
    String _xblockexpression = null;
    {
      final Optional<IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>> opt = parsed.getQuerySpecification("elimination");
      String _xifexpression = null;
      boolean _isPresent = opt.isPresent();
      if (_isPresent) {
        String _xblockexpression_1 = null;
        {
          final IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> matcher = opt.get();
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("{");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\"valid\" : true,");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\"matches\" : [");
          _builder.newLine();
          {
            Collection<? extends IPatternMatch> _allMatches = engine.getMatcher(matcher).getAllMatches();
            boolean _hasElements = false;
            for(final IPatternMatch match : _allMatches) {
              if (!_hasElements) {
                _hasElements = true;
              } else {
                _builder.appendImmediate(",", "\t");
              }
              _builder.append("\t");
              _builder.append("{");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\"object\" : \"object");
              Integer _get = index.get(match.get(0));
              _builder.append(_get, "\t\t");
              _builder.append("\",");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\"probability\" : ");
              Object _get_1 = match.get(1);
              _builder.append(_get_1, "\t\t");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("}");
              _builder.newLine();
            }
          }
          _builder.append("\t");
          _builder.append("]");
          _builder.newLine();
          _builder.append("}");
          _builder.newLine();
          _xblockexpression_1 = _builder.toString();
        }
        _xifexpression = _xblockexpression_1;
      } else {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("{\"valid\" : false, \"matches\" : []}");
        return _builder.toString();
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
}
