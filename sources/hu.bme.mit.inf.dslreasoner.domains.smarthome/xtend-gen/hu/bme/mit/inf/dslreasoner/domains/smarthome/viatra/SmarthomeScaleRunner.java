package hu.bme.mit.inf.dslreasoner.domains.smarthome.viatra;

import hu.bme.mit.inf.dslreasoner.domains.smarthome.problog.ProblogSmarthomeUtil;
import hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities.SmarthomeModel;
import hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities.SmarthomeModelGenerator;
import hu.bme.mit.inf.measurement.utilities.CSVLog;
import hu.bme.mit.inf.measurement.utilities.Config;
import hu.bme.mit.inf.measurement.utilities.configuration.SmarthomeConfiguration;
import hu.bme.mit.inf.measurement.utilities.viatra.EngineConfig;
import hu.bme.mit.inf.measurement.utilities.viatra.ViatraScaleRunner;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;
import reliability.intreface.Configuration;
import reliability.intreface.ExecutionTime;
import se.liu.ida.sas.pelab.smarthome.storm.StormSmarthomeUtil;
import smarthome.SmarthomePackage;

@SuppressWarnings("all")
public class SmarthomeScaleRunner extends ViatraScaleRunner<SmarthomeConfiguration> implements StormSmarthomeUtil, ProblogSmarthomeUtil {
  private final SmarthomeModelGenerator modelgen = new SmarthomeModelGenerator();

  private SmarthomeModel instance;

  public SmarthomeScaleRunner(final SmarthomeConfiguration cfg) {
    super(cfg, SmarthomePackage.eINSTANCE);
  }

  @Override
  public void preRun(final int seed) {
    this.instance = this.modelgen.make(this.cfg.getHomes(), this.cfg.getPersons(), this.cfg.getSize());
    this.engine.getModel().getContents().add(this.instance.model);
  }

  @Override
  public void initIncremental() {
    super.initIncremental();
    this.initializePatterns(this.engine, "callProbability");
  }

  @Override
  public void runViatra(final CSVLog log) {
    Configuration.enable();
    try {
      ExecutionTime.reset();
      final Procedure0 _function = () -> {
        InputOutput.<String>println("Run cancelled with timeout.");
        Configuration.cancel();
        this.engine.dispose();
        log.log("timeout", Boolean.valueOf(true));
      };
      final Timer timeout = Config.timeout(this.cfg.getTimeoutS(), _function);
      final long it0start = System.nanoTime();
      this.engine.enable();
      final long it0sync = this.engine.getMdd().unaryForAll(this.engine.getEngine());
      final String coverage = this.getMatchesJSON(this.engine, this.instance.idmap);
      final long it0end = System.nanoTime();
      timeout.cancel();
      final long it0prop = ExecutionTime.time();
      log.log("incremental.total[ms]", Double.valueOf((((it0end - it0start) / 1000.0) / 1000)));
      log.log("incremental.sync[ms]", Double.valueOf(((it0sync / 1000.0) / 1000)));
      log.log("incremental.prop[ms]", Double.valueOf(((it0prop / 1000.0) / 1000)));
      boolean _isTainted = this.engine.getEngine().isTainted();
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
    this.runProblog(this.cfg, this.instance, log);
  }

  @Override
  public void runStorm(final CSVLog log) {
    this.runStorm(this.cfg, this.instance, log);
  }

  /**
   * def String problogToJSON(Map<String,Object> data, boolean timeout){
   * return '''
   * {
   * "valid" : «!timeout»,
   * "matches" : [
   * «FOR entry : data.entrySet SEPARATOR ","»
   * {
   * "measurement" : "«instance.idmap.get(instance.ofHashCode(Integer.parseInt(entry.key)))»",
   * "probability" : «entry.value»
   * }
   * «ENDFOR»
   * ]
   * }
   * '''
   * }
   */
  public String getMatchesJSON(final EngineConfig engine, final Map<EObject, String> index) {
    String _xblockexpression = null;
    {
      final Optional<IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>> opt = engine.getParsed().getQuerySpecification("callProbability");
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
            Collection<? extends IPatternMatch> _allMatches = engine.getEngine().getMatcher(matcher).getAllMatches();
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
              _builder.append("\"measurement\" : \"");
              String _get = index.get(match.get(1));
              _builder.append(_get, "\t\t");
              _builder.append("\",");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\"probability\" : ");
              Object _get_1 = match.get(2);
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
