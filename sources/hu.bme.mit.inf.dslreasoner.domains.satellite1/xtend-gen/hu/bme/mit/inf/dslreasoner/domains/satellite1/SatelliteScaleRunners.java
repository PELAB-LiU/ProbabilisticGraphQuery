package hu.bme.mit.inf.dslreasoner.domains.satellite1;

import hu.bme.mit.inf.measurement.utilities.CSVLog;
import hu.bme.mit.inf.measurement.utilities.Config;
import hu.bme.mit.inf.measurement.utilities.configuration.SatelliteConfiguration;
import hu.bme.mit.inf.measurement.utilities.viatra.ViatraScaleRunner;
import java.util.Timer;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import problog.ProblogSatelliteUtil;
import reliability.intreface.CancellationException;
import reliability.intreface.Configuration;
import reliability.intreface.ExecutionTime;
import satellite1.SatellitePackage;
import se.liu.ida.sas.pelab.satellite1.storm.StormSatelliteUtil;

@SuppressWarnings("all")
public class SatelliteScaleRunners extends ViatraScaleRunner<SatelliteConfiguration> implements StormSatelliteUtil, ProblogSatelliteUtil {
  private static final Logger LOG4J = LoggerFactory.getLogger(SatelliteScaleRunners.class);

  private final SatelliteModelGenerator modelgen = new SatelliteModelGenerator();

  private SatelliteModelWrapper instance;

  public SatelliteScaleRunners(final SatelliteConfiguration cfg) {
    super(cfg, SatellitePackage.eINSTANCE);
  }

  @Override
  public boolean preRun(final int seed) {
    boolean _xblockexpression = false;
    {
      this.instance = this.modelgen.make(this.cfg.getSize(), seed);
      _xblockexpression = this.engine.getModel().getContents().add(this.instance.mission);
    }
    return _xblockexpression;
  }

  @Override
  public void initViatra() {
    super.initViatra();
    this.initializePatterns(this.engine, "coverage");
  }

  @Override
  public boolean runViatra(final CSVLog log) {
    Configuration.enable();
    try {
      ExecutionTime.reset();
      final Procedure0 _function = () -> {
        Configuration.cancel();
        SatelliteScaleRunners.LOG4J.info("Viatra Timeout");
      };
      final Timer timeout = Config.timeout(this.cfg.getTimeoutS(), _function);
      boolean _isTainted = this.engine.getEngine().isTainted();
      boolean _not = (!_isTainted);
      log.log("incremental.healthy", Boolean.valueOf(_not));
      final long it0start = System.nanoTime();
      this.engine.enable();
      final long it0sync = this.engine.getMdd().unaryForAll(this.engine.getEngine());
      final double coverage = this.checkMatches("coverage", this.engine);
      final long it0end = System.nanoTime();
      timeout.cancel();
      final long it0prop = ExecutionTime.time();
      log.log("incremental.total[ms]", Double.valueOf((((it0end - it0start) / 1000.0) / 1000)));
      log.log("incremental.sync[ms]", Double.valueOf(((it0sync / 1000.0) / 1000)));
      log.log("incremental.prop[ms]", Double.valueOf(((it0prop / 1000.0) / 1000)));
      log.log("incremental.result", Double.valueOf(coverage));
      SatelliteScaleRunners.LOG4J.info("Viatra completed in {}ms with result {}", Double.valueOf((((it0end - it0start) / 1000.0) / 1000)), Double.valueOf(coverage));
      return Configuration.isCancelled();
    } catch (final Throwable _t) {
      if (_t instanceof CancellationException) {
        final CancellationException e = (CancellationException)_t;
        SatelliteScaleRunners.LOG4J.warn("Exception logged: {}", e.getMessage());
        SatelliteScaleRunners.LOG4J.debug("Exception logged: {}, exception: {}", e.getMessage(), e);
        return Configuration.isCancelled();
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    } finally {
      log.log("incremental.timeout", Boolean.valueOf(Configuration.isCancelled()));
      InputOutput.<String>println("Finally block executed.");
    }
  }

  @Override
  public boolean runProblog(final CSVLog log) {
    return this.runProblog(this.cfg, this.instance, log);
  }

  @Override
  public boolean runStorm(final CSVLog log) {
    return this.runStorm(this.cfg, this.instance, log);
  }
}
