package hu.bme.mit.inf.dslreasoner.domains.surveillance.viatra;

import hu.bme.mit.inf.dslreasoner.domains.surveillance.problog.ProblogSurveillanceUtil;
import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.SurveillanceCopier;
import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.SurveillanceHelper;
import hu.bme.mit.inf.measurement.utilities.CSVLog;
import hu.bme.mit.inf.measurement.utilities.Config;
import hu.bme.mit.inf.measurement.utilities.configuration.SurveillanceConfiguration;
import hu.bme.mit.inf.measurement.utilities.viatra.EngineConfig;
import hu.bme.mit.inf.measurement.utilities.viatra.ViatraBaseRunner;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.function.Consumer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;
import reliability.intreface.Configuration;
import reliability.intreface.ExecutionTime;
import se.liu.ida.sas.pelab.surveillance.storm.StormSurveillanceUtil;
import surveillance.SurveillancePackage;

@SuppressWarnings("all")
public class SurveillanceRunner extends ViatraBaseRunner<SurveillanceConfiguration> implements StormSurveillanceUtil, ProblogSurveillanceUtil {
  private final SurveillanceModelGenerator modelgen = new SurveillanceModelGenerator();

  private SurveillanceWrapper instance;

  public SurveillanceRunner(final SurveillanceConfiguration cfg) {
    super(cfg, SurveillancePackage.eINSTANCE);
  }

  @Override
  public void setupInitialModel(final int seed) {
    this.instance = this.modelgen.make(this.cfg.getSize(), seed);
    this.incremental.getModel().getContents().add(this.instance.model);
  }

  @Override
  public void applyIncrement() {
    this.modelgen.iterate(this.instance, 0.1);
    boolean _isTainted = this.incremental.getEngine().isTainted();
    String _plus = ("Incremental query engine status after increment (is tainted): " + Boolean.valueOf(_isTainted));
    InputOutput.<String>println(_plus);
  }

  @Override
  public void initBatch() {
    super.initBatch();
    this.initializePatterns(this.batch, "elimination");
  }

  @Override
  public void runBatch(final CSVLog log) {
    SurveillanceHelper.logger = log;
    Configuration.enable();
    final Resource resource = this.batch.getModel();
    try {
      final SurveillanceCopier copier = new SurveillanceCopier();
      final EObject result = copier.copy(this.instance.model);
      copier.copyReferences();
      resource.getContents().add(result);
      final HashMap<EObject, Integer> index = CollectionLiterals.<EObject, Integer>newHashMap();
      final Consumer<Map.Entry<EObject, EObject>> _function = (Map.Entry<EObject, EObject> it) -> {
        index.put(it.getValue(), this.instance.ordering.get(it.getKey()));
      };
      copier.entrySet().forEach(_function);
      ExecutionTime.reset();
      final Procedure0 _function_1 = () -> {
        InputOutput.<String>println("Run cancelled with timeout.");
        Configuration.cancel();
        this.batch.dispose();
        log.log("timeout", Boolean.valueOf(true));
      };
      final Timer timeout = Config.timeout(this.cfg.getTimeoutS(), _function_1);
      final long it0start = System.nanoTime();
      this.batch.enable();
      final long it0sync = this.batch.getMdd().unaryForAll(this.batch.getEngine());
      final String coverage = this.getMatchesJSON(this.batch, index);
      final long it0end = System.nanoTime();
      timeout.cancel();
      final long it0prop = ExecutionTime.time();
      log.log("standalone.total[ms]", Double.valueOf((((it0end - it0start) / 1000.0) / 1000)));
      log.log("standalone.sync[ms]", Double.valueOf(((it0sync / 1000.0) / 1000)));
      log.log("standalone.prop[ms]", Double.valueOf(((it0prop / 1000.0) / 1000)));
      boolean _isTainted = this.batch.getEngine().isTainted();
      boolean _not = (!_isTainted);
      log.log("standalone.healthy", Boolean.valueOf(_not));
      log.log("standalone.result", coverage);
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        InputOutput.<String>println("Cancellation caught.");
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    } finally {
      resource.getContents().clear();
      log.log("standalone.timeout", Boolean.valueOf(Configuration.isCancelled()));
      InputOutput.<String>println("Finally block executed.");
    }
  }

  @Override
  public void initIncremental() {
    super.initIncremental();
    this.initializePatterns(this.incremental, "elimination");
  }

  @Override
  public void runIncremental(final CSVLog log) {
    Configuration.enable();
    SurveillanceHelper.logger = log;
    try {
      ExecutionTime.reset();
      final Procedure0 _function = () -> {
        InputOutput.<String>println("Run cancelled with timeout.");
        Configuration.cancel();
        this.incremental.dispose();
        log.log("timeout", Boolean.valueOf(true));
      };
      final Timer timeout = Config.timeout(this.cfg.getTimeoutS(), _function);
      final long it0start = System.nanoTime();
      this.incremental.enable();
      final long it0sync = this.incremental.getMdd().unaryForAll(this.incremental.getEngine());
      final String coverage = this.getMatchesJSON(this.incremental, this.instance.ordering);
      final long it0end = System.nanoTime();
      timeout.cancel();
      final long it0prop = ExecutionTime.time();
      log.log("incremental.total[ms]", Double.valueOf((((it0end - it0start) / 1000.0) / 1000)));
      log.log("incremental.sync[ms]", Double.valueOf(((it0sync / 1000.0) / 1000)));
      log.log("incremental.prop[ms]", Double.valueOf(((it0prop / 1000.0) / 1000)));
      boolean _isTainted = this.incremental.getEngine().isTainted();
      boolean _not = (!_isTainted);
      log.log("incremental.healthy", Boolean.valueOf(_not));
      log.log("incremental.result", coverage);
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception e = (Exception)_t;
        e.printStackTrace();
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
   * "object" : "object«instance.ordering.get(instance.ofHashCode(Integer.parseInt(entry.key)))»",
   * "probability" : «entry.value»
   * }
   * «ENDFOR»
   * ]
   * }
   * '''
   * }
   */
  public String getMatchesJSON(final EngineConfig engine, final Map<EObject, Integer> index) {
    String _xblockexpression = null;
    {
      final Optional<IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>> opt = engine.getParsed().getQuerySpecification("elimination");
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
