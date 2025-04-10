package hu.bme.mit.inf.measurement.utilities.viatra;

import hu.bme.mit.inf.measurement.utilities.CSVLog;
import hu.bme.mit.inf.measurement.utilities.configuration.BaseConfiguration;
import java.util.List;
import org.eclipse.emf.ecore.EPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
public abstract class ScalingMeasurementRunner<Config extends BaseConfiguration> extends BaseRunner<Config> implements ViatraRunnerUtil {
  private static final Logger LOG4J = LoggerFactory.getLogger(ScalingMeasurementRunner.class);

  protected EngineConfig engine;

  public void initViatra() {
    EngineConfig _engineConfig = new EngineConfig(this.transformed, "standalone");
    this.engine = _engineConfig;
    ScalingMeasurementRunner.LOG4J.debug("Init VIATRA {}", Integer.valueOf(this.engine.getEngine().hashCode()));
  }

  public ScalingMeasurementRunner(final Config cfg, final EPackage domain) {
    super(cfg, domain);
  }

  @Override
  public void warmup(final CSVLog log) {
    List<Integer> _warmups = this.cfg.getWarmups();
    for (final Integer i : _warmups) {
      {
        int _indexOf = this.cfg.getWarmups().indexOf(i);
        int _plus = (_indexOf + 1);
        ScalingMeasurementRunner.LOG4J.info("[WARMUP {} ({} of {}) ]===============================================================", i, Integer.valueOf(_plus), Integer.valueOf(this.cfg.getWarmups().size()));
        this.initViatra();
        this.setupInitialModel((i).intValue());
        this.gc();
        this.engine.acquire();
        this.runViatra(log);
        this.engine.dispose();
        if (((i).intValue() < 2)) {
          this.gc();
          this.runProblog(log);
          this.gc();
          this.runStorm(log);
        }
        log.log("iteration", Integer.valueOf(0));
        log.log("run", i);
        log.log("prefix", this.cfg.getPrefix());
        log.log("size", Integer.valueOf(this.cfg.getSize()));
        log.commit();
      }
    }
  }

  @Override
  public void measure(final CSVLog log) {
    List<Integer> _seeds = this.cfg.seeds();
    for (final Integer seed : _seeds) {
      {
        int _indexOf = this.cfg.seeds().indexOf(seed);
        int _plus = (_indexOf + 1);
        ScalingMeasurementRunner.LOG4J.info("[MEASURE {} ({} of {}) ]===============================================================", seed, Integer.valueOf(_plus), Integer.valueOf(this.cfg.seeds().size()));
        this.initViatra();
        this.setupInitialModel((seed).intValue());
        this.gc();
        this.engine.acquire();
        this.runViatra(log);
        this.engine.dispose();
        this.gc();
        this.runProblog(log);
        this.gc();
        this.runStorm(log);
        log.log("iteration", Integer.valueOf(0));
        log.log("run", seed);
        log.log("prefix", this.cfg.getPrefix());
        log.log("size", Integer.valueOf(this.cfg.getSize()));
        log.commit();
      }
    }
  }

  public abstract void setupInitialModel(final int seed);

  public abstract void runViatra(final CSVLog log);

  public abstract void runProblog(final CSVLog log);

  public abstract void runStorm(final CSVLog log);
}
