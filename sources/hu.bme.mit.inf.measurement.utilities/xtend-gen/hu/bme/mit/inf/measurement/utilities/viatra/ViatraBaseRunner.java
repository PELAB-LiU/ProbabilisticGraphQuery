package hu.bme.mit.inf.measurement.utilities.viatra;

import hu.bme.mit.inf.measurement.utilities.CSVLog;
import hu.bme.mit.inf.measurement.utilities.configuration.BaseConfiguration;
import hu.bme.mit.inf.querytransformation.query.StochasticPatternGenerator;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.viatra.query.patternlanguage.emf.EMFPatternLanguageStandaloneSetup;
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngineOptions;
import org.eclipse.viatra.query.runtime.localsearch.matcher.integration.LocalSearchEMFBackendFactory;
import org.eclipse.viatra.query.runtime.rete.matcher.ReteBackendFactory;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
public abstract class ViatraBaseRunner<Config extends BaseConfiguration> implements ViatraRunnerUtil {
  private static final Logger LOG4J = LoggerFactory.getLogger(ViatraBaseRunner.class);

  protected final Config cfg;

  protected final StochasticPatternGenerator generator;

  protected final String transformed;

  protected EngineConfig batch;

  protected EngineConfig incremental;

  protected Resource model;

  public void initBatch() {
    EngineConfig _engineConfig = new EngineConfig(this.transformed, "standalone");
    this.batch = _engineConfig;
    ViatraBaseRunner.LOG4J.debug("Init Batch {}", Integer.valueOf(this.batch.getEngine().hashCode()));
  }

  public void initIncremental() {
    EngineConfig _engineConfig = new EngineConfig(this.transformed, "incremental");
    this.incremental = _engineConfig;
    ViatraBaseRunner.LOG4J.debug("Init Incremetnal {}", Integer.valueOf(this.incremental.getEngine().hashCode()));
  }

  /**
   * Project specific todo: Register EPackages
   */
  public ViatraBaseRunner(final Config cfg, final EPackage domain) {
    this.cfg = cfg;
    Map<String, Object> _extensionToFactoryMap = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();
    XMIResourceFactoryImpl _xMIResourceFactoryImpl = new XMIResourceFactoryImpl();
    _extensionToFactoryMap.putIfAbsent("xmi", _xMIResourceFactoryImpl);
    StochasticPatternGenerator.doSetup();
    StochasticPatternGenerator _stochasticPatternGenerator = new StochasticPatternGenerator();
    this.generator = _stochasticPatternGenerator;
    EMFPatternLanguageStandaloneSetup.doSetup();
    ViatraQueryEngineOptions.setSystemDefaultBackends(ReteBackendFactory.INSTANCE, ReteBackendFactory.INSTANCE, 
      LocalSearchEMFBackendFactory.INSTANCE);
    EPackage.Registry.INSTANCE.put(domain.getNsURI(), domain);
    this.transformed = this.generator.transformPatternFile(cfg.vql());
    ViatraBaseRunner.LOG4J.info("Queries {}", this.transformed);
  }

  public void gc() {
    try {
      ViatraBaseRunner.LOG4J.debug("GC {}ms", Integer.valueOf(this.cfg.getGCTime()));
      System.gc();
      Thread.sleep(this.cfg.getGCTime());
      System.gc();
      Thread.sleep(this.cfg.getGCTime());
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  public void run() {
    List<String> _cSVcolumns = this.cfg.getCSVcolumns();
    String _delimiter = this.cfg.getDelimiter();
    final CSVLog warmlog = new CSVLog(((String[])Conversions.unwrapArray(_cSVcolumns, String.class)), _delimiter);
    try {
      this.warmup(warmlog);
    } finally {
      this.cfg.outWarmup().print(warmlog);
    }
    List<String> _cSVcolumns_1 = this.cfg.getCSVcolumns();
    String _delimiter_1 = this.cfg.getDelimiter();
    final CSVLog measlog = new CSVLog(((String[])Conversions.unwrapArray(_cSVcolumns_1, String.class)), _delimiter_1);
    try {
      this.measure(measlog);
    } finally {
      this.cfg.out().print(measlog);
    }
  }

  public void warmup(final CSVLog log) {
    List<Integer> _warmups = this.cfg.getWarmups();
    for (final Integer i : _warmups) {
      {
        int _indexOf = this.cfg.getWarmups().indexOf(i);
        int _plus = (_indexOf + 1);
        ViatraBaseRunner.LOG4J.info("[WARMUP {} ({} of {}) ]===============================================================", i, Integer.valueOf(_plus), Integer.valueOf(this.cfg.getWarmups().size()));
        this.initIncremental();
        this.setupInitialModel((i).intValue());
        int _iterations = this.cfg.getIterations();
        IntegerRange _upTo = new IntegerRange(0, _iterations);
        for (final Integer iter : _upTo) {
          {
            int _iterations_1 = this.cfg.getIterations();
            int _indexOf_1 = this.cfg.getWarmups().indexOf(i);
            int _plus_1 = (_indexOf_1 + 1);
            ViatraBaseRunner.LOG4J.info("[ITERATION {} of {} WARMUP {} ({} of {}) ]===============================================================", iter, Integer.valueOf(_iterations_1), i, Integer.valueOf(_plus_1), Integer.valueOf(this.cfg.getWarmups().size()));
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

  public void measure(final CSVLog log) {
    List<Integer> _seeds = this.cfg.seeds();
    for (final Integer seed : _seeds) {
      {
        int _indexOf = this.cfg.seeds().indexOf(seed);
        int _plus = (_indexOf + 1);
        ViatraBaseRunner.LOG4J.info("[MEASURE {} ({} of {}) ]===============================================================", seed, Integer.valueOf(_plus), Integer.valueOf(this.cfg.seeds().size()));
        this.initIncremental();
        this.setupInitialModel((seed).intValue());
        int _iterations = this.cfg.getIterations();
        IntegerRange _upTo = new IntegerRange(0, _iterations);
        for (final Integer iter : _upTo) {
          {
            int _iterations_1 = this.cfg.getIterations();
            int _indexOf_1 = this.cfg.seeds().indexOf(seed);
            int _plus_1 = (_indexOf_1 + 1);
            ViatraBaseRunner.LOG4J.info("[ITERATION {} of {} MEASURE {} ({} of {}) ]===============================================================", iter, Integer.valueOf(_iterations_1), seed, Integer.valueOf(_plus_1), Integer.valueOf(this.cfg.seeds().size()));
            this.gc();
            this.initBatch();
            this.batch.acquire();
            this.runBatch(log);
            this.batch.dispose();
            this.gc();
            this.incremental.acquire();
            this.runIncremental(log);
            this.incremental.suspend();
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
