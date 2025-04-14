package hu.bme.mit.inf.dslreasoner.domains.surveillance.viatra;

import hu.bme.mit.inf.dslreasoner.domains.surveillance.problog.ProblogSurveillanceUtil;
import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.SurveillanceCopier;
import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.SurveillanceHelper;
import hu.bme.mit.inf.measurement.utilities.CSVLog;
import hu.bme.mit.inf.measurement.utilities.Config;
import hu.bme.mit.inf.measurement.utilities.configuration.SurveillanceConfiguration;
import hu.bme.mit.inf.measurement.utilities.viatra.ViatraBaseRunner;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.function.Consumer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reliability.intreface.Configuration;
import reliability.intreface.ExecutionTime;
import se.liu.ida.sas.pelab.surveillance.storm.StormSurveillanceUtil;
import surveillance.SurveillancePackage;

@SuppressWarnings("all")
public class SurveillanceRunner extends ViatraBaseRunner<SurveillanceConfiguration> implements ViatraSurveillanceUtil, StormSurveillanceUtil, ProblogSurveillanceUtil {
  private static final Logger LOG4J = LoggerFactory.getLogger(SurveillanceRunner.class);

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
        SurveillanceRunner.LOG4J.info("Batch Timeout");
        Configuration.cancel();
        this.batch.dispose();
      };
      final Timer timeout = Config.timeout(this.cfg.getTimeoutS(), _function_1);
      boolean _isTainted = this.batch.getEngine().isTainted();
      boolean _not = (!_isTainted);
      log.log("standalone.healthy", Boolean.valueOf(_not));
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
      log.log("standalone.result", coverage);
      SurveillanceRunner.LOG4J.info("Batch completed in {}ms", Double.valueOf((((it0end - it0start) / 1000.0) / 1000)));
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception e = (Exception)_t;
        SurveillanceRunner.LOG4J.warn("Exception logged: {}", e.getMessage());
        SurveillanceRunner.LOG4J.debug("Exception logged: {}, exception: {}", e.getMessage(), e);
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    } finally {
      resource.getContents().clear();
      log.log("standalone.timeout", Boolean.valueOf(Configuration.isCancelled()));
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
        SurveillanceRunner.LOG4J.info("Incremental Timeout");
        Configuration.cancel();
        this.incremental.dispose();
      };
      final Timer timeout = Config.timeout(this.cfg.getTimeoutS(), _function);
      boolean _isTainted = this.incremental.getEngine().isTainted();
      boolean _not = (!_isTainted);
      log.log("incremental.healthy", Boolean.valueOf(_not));
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
      log.log("incremental.result", coverage);
      SurveillanceRunner.LOG4J.info("Incremental completed in {}ms", Double.valueOf((((it0end - it0start) / 1000.0) / 1000)));
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception e = (Exception)_t;
        SurveillanceRunner.LOG4J.warn("Exception logged: {}", e.getMessage());
        SurveillanceRunner.LOG4J.debug("Exception logged: {}, exception: {}", e.getMessage(), e);
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    } finally {
      log.log("incremental.timeout", Boolean.valueOf(Configuration.isCancelled()));
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
}
