package hu.bme.mit.inf.dslreasoner.domains.satellite1;

import hu.bme.mit.inf.measurement.utilities.CSVLog;
import hu.bme.mit.inf.measurement.utilities.Config;
import hu.bme.mit.inf.measurement.utilities.configuration.SatelliteConfiguration;
import hu.bme.mit.inf.measurement.utilities.viatra.ViatraBaseRunner;
import java.util.Timer;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import problog.ProblogSatelliteUtil;
import reliability.intreface.CancellationException;
import reliability.intreface.Configuration;
import reliability.intreface.ExecutionTime;
import satellite1.InterferometryMission;
import satellite1.SatellitePackage;
import se.liu.ida.sas.pelab.satellite1.storm.StormSatelliteUtil;

@SuppressWarnings("all")
public class SatelliteSingleGraph extends ViatraBaseRunner<SatelliteConfiguration> implements StormSatelliteUtil, ProblogSatelliteUtil {
  private static final Logger LOG4J = LoggerFactory.getLogger(SatelliteSingleGraph.class);

  private final SatelliteModelGenerator modelgen = new SatelliteModelGenerator();

  private SatelliteModelWrapper instance;

  public SatelliteSingleGraph(final SatelliteConfiguration cfg) {
    super(cfg, SatellitePackage.eINSTANCE);
  }

  @Override
  public void initBatch() {
    super.initBatch();
    this.initializePatterns(this.batch, "coverage");
  }

  @Override
  public void runBatch(final CSVLog log) {
    Configuration.enable();
    try {
      final Resource resource = this.batch.getModel();
      resource.getContents().add(EcoreUtil.<InterferometryMission>copy(this.instance.mission));
      ExecutionTime.reset();
      final Procedure0 _function = () -> {
        Configuration.cancel();
        SatelliteSingleGraph.LOG4J.info("Batch Timeout");
      };
      final Timer timeout = Config.timeout(this.cfg.getTimeoutS(), _function);
      boolean _isTainted = this.batch.getEngine().isTainted();
      boolean _not = (!_isTainted);
      log.log("standalone.healthy", Boolean.valueOf(_not));
      final long it0start = System.nanoTime();
      this.batch.enable();
      final long it0sync = this.batch.getMdd().unaryForAll(this.batch.getEngine());
      final double coverage = this.checkMatches("coverage", this.batch);
      final long it0end = System.nanoTime();
      timeout.cancel();
      final long it0prop = ExecutionTime.time();
      log.log("standalone.total[ms]", Double.valueOf((((it0end - it0start) / 1000.0) / 1000)));
      log.log("standalone.sync[ms]", Double.valueOf(((it0sync / 1000.0) / 1000)));
      log.log("standalone.prop[ms]", Double.valueOf(((it0prop / 1000.0) / 1000)));
      log.log("standalone.result", Double.valueOf(coverage));
      SatelliteSingleGraph.LOG4J.info("Batch completed in {}ms with result {}", Double.valueOf((((it0end - it0start) / 1000.0) / 1000)), Double.valueOf(coverage));
    } catch (final Throwable _t) {
      if (_t instanceof CancellationException) {
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    } finally {
      log.log("standalone.timeout", Boolean.valueOf(Configuration.isCancelled()));
    }
  }

  @Override
  public void setupInitialModel(final int seed) {
    this.instance = this.modelgen.make(this.cfg.getSize(), seed);
    this.incremental.getModel().getContents().add(this.instance.mission);
  }

  @Override
  public void initIncremental() {
    super.initIncremental();
    this.initializePatterns(this.incremental, "coverage");
  }

  @Override
  public void runIncremental(final CSVLog log) {
    Configuration.enable();
    try {
      ExecutionTime.reset();
      final Procedure0 _function = () -> {
        Configuration.cancel();
        SatelliteSingleGraph.LOG4J.info("Incremental Timeout");
      };
      final Timer timeout = Config.timeout(this.cfg.getTimeoutS(), _function);
      boolean _isTainted = this.incremental.getEngine().isTainted();
      boolean _not = (!_isTainted);
      log.log("incremental.healthy", Boolean.valueOf(_not));
      final long it0start = System.nanoTime();
      this.incremental.enable();
      final long it0sync = this.incremental.getMdd().unaryForAll(this.incremental.getEngine());
      final double coverage = this.checkMatches("coverage", this.incremental);
      final long it0end = System.nanoTime();
      timeout.cancel();
      final long it0prop = ExecutionTime.time();
      log.log("incremental.total[ms]", Double.valueOf((((it0end - it0start) / 1000.0) / 1000)));
      log.log("incremental.sync[ms]", Double.valueOf(((it0sync / 1000.0) / 1000)));
      log.log("incremental.prop[ms]", Double.valueOf(((it0prop / 1000.0) / 1000)));
      log.log("incremental.result", Double.valueOf(coverage));
      SatelliteSingleGraph.LOG4J.info("Incremental completed in {}ms with result {}", Double.valueOf((((it0end - it0start) / 1000.0) / 1000)), Double.valueOf(coverage));
    } catch (final Throwable _t) {
      if (_t instanceof CancellationException) {
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    } finally {
      log.log("incremental.timeout", Boolean.valueOf(Configuration.isCancelled()));
    }
  }

  @Override
  public void applyIncrement() {
    this.modelgen.makeRandomChange(this.instance, 1);
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
