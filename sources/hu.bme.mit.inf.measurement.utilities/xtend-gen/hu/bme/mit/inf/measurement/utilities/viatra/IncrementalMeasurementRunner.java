package hu.bme.mit.inf.measurement.utilities.viatra;

import hu.bme.mit.inf.measurement.utilities.CSVLog;
import hu.bme.mit.inf.measurement.utilities.configuration.BaseConfiguration;
import java.util.List;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
public abstract class IncrementalMeasurementRunner<Config extends BaseConfiguration> extends BaseRunner<Config> implements ViatraRunnerUtil {
  private static final Logger LOG4J = LoggerFactory.getLogger(IncrementalMeasurementRunner.class);

  protected EngineConfig batch;

  protected EngineConfig incremental;

  public void initBatch() {
    EngineConfig _engineConfig = new EngineConfig(this.transformed, "standalone");
    this.batch = _engineConfig;
    IncrementalMeasurementRunner.LOG4J.debug("Init Batch {}", Integer.valueOf(this.batch.getEngine().hashCode()));
  }

  public void initIncremental() {
    EngineConfig _engineConfig = new EngineConfig(this.transformed, "incremental");
    this.incremental = _engineConfig;
    IncrementalMeasurementRunner.LOG4J.debug("Init Incremetnal {}", Integer.valueOf(this.incremental.getEngine().hashCode()));
  }

  public IncrementalMeasurementRunner(final Config cfg, final EPackage domain) {
    super(cfg, domain);
  }

  @Override
  public void warmup(final CSVLog log) {
    List<Integer> _warmups = this.cfg.getWarmups();
    for (final Integer i : _warmups) {
      {
        int _indexOf = this.cfg.getWarmups().indexOf(i);
        int _plus = (_indexOf + 1);
        IncrementalMeasurementRunner.LOG4J.info("[WARMUP {} ({} of {}) ]===============================================================", i, Integer.valueOf(_plus), Integer.valueOf(this.cfg.getWarmups().size()));
        this.initIncremental();
        this.setupInitialModel((i).intValue());
        int _iterations = this.cfg.getIterations();
        IntegerRange _upTo = new IntegerRange(0, _iterations);
        for (final Integer iter : _upTo) {
          {
            int _iterations_1 = this.cfg.getIterations();
            int _indexOf_1 = this.cfg.getWarmups().indexOf(i);
            int _plus_1 = (_indexOf_1 + 1);
            IncrementalMeasurementRunner.LOG4J.info("[ITERATION {} of {} WARMUP {} ({} of {}) ]===============================================================", iter, Integer.valueOf(_iterations_1), i, Integer.valueOf(_plus_1), Integer.valueOf(this.cfg.getWarmups().size()));
            this.gc();
            this.incremental.acquire();
            this.runIncremental(log);
            this.incremental.suspend();
            this.gc();
            this.initBatch();
            this.batch.acquire();
            this.runBatch(log);
            this.batch.dispose();
            if (((i).intValue() < 2)) {
              this.gc();
              this.runProblog(log);
              this.gc();
              this.runStorm(log);
            }
            log.log("iteration", iter);
            log.log("run", i);
            log.log("prefix", this.cfg.getPrefix());
            log.log("size", Integer.valueOf(this.cfg.getSize()));
            log.commit();
            this.applyIncrement();
          }
        }
        this.incremental.dispose();
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
        IncrementalMeasurementRunner.LOG4J.info("[MEASURE {} ({} of {}) ]===============================================================", seed, Integer.valueOf(_plus), Integer.valueOf(this.cfg.seeds().size()));
        this.initIncremental();
        this.setupInitialModel((seed).intValue());
        int _iterations = this.cfg.getIterations();
        IntegerRange _upTo = new IntegerRange(0, _iterations);
        for (final Integer iter : _upTo) {
          {
            int _iterations_1 = this.cfg.getIterations();
            int _indexOf_1 = this.cfg.seeds().indexOf(seed);
            int _plus_1 = (_indexOf_1 + 1);
            IncrementalMeasurementRunner.LOG4J.info("[ITERATION {} of {} MEASURE {} ({} of {}) ]===============================================================", iter, Integer.valueOf(_iterations_1), seed, Integer.valueOf(_plus_1), Integer.valueOf(this.cfg.seeds().size()));
            this.gc();
            this.incremental.acquire();
            this.runIncremental(log);
            this.incremental.suspend();
            this.gc();
            this.initBatch();
            this.batch.acquire();
            this.runBatch(log);
            this.batch.dispose();
            this.gc();
            this.runProblog(log);
            this.gc();
            this.runStorm(log);
            log.log("iteration", iter);
            log.log("prefix", this.cfg.getPrefix());
            log.log("run", seed);
            log.log("size", Integer.valueOf(this.cfg.getSize()));
            log.commit();
            this.applyIncrement();
          }
        }
        this.incremental.dispose();
      }
    }
  }

  public abstract void setupInitialModel(final int seed);

  public abstract void runIncremental(final CSVLog log);

  public abstract void runBatch(final CSVLog log);

  public abstract void applyIncrement();

  public abstract void runProblog(final CSVLog log);

  public abstract void runStorm(final CSVLog log);
}
