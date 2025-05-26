package hu.bme.mit.inf.measurement.utilities.viatra;

import hu.bme.mit.inf.measurement.utilities.CSVLog;
import hu.bme.mit.inf.measurement.utilities.configuration.BaseConfiguration;
import hu.bme.mit.inf.measurement.utilities.configuration.CancellationThresholdMode;
import hu.bme.mit.inf.measurement.utilities.configuration.EarlyCancelMonitor;
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
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reliability.mdd.MddModel;

@SuppressWarnings("all")
public abstract class ViatraScaleRunner<Config extends BaseConfiguration> implements ViatraRunnerUtil {
  private static final Logger LOG4J = LoggerFactory.getLogger(ViatraScaleRunner.class);

  protected final Config cfg;

  protected final StochasticPatternGenerator generator;

  protected final String transformed;

  protected EngineConfig engine;

  public void initViatra() {
    boolean _isFavourAbort = this.cfg.isFavourAbort();
    EngineConfig _engineConfig = new EngineConfig(this.transformed, "incremental", _isFavourAbort);
    this.engine = _engineConfig;
    ViatraScaleRunner.LOG4J.debug("Init VIATRA {}", Integer.valueOf(this.engine.getEngine().hashCode()));
  }

  /**
   * Project specific todo: Register EPackages
   */
  public ViatraScaleRunner(final Config cfg, final EPackage domain) {
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
    ViatraScaleRunner.LOG4J.info("Queries {}", this.transformed);
  }

  public void gc() {
    try {
      ViatraScaleRunner.LOG4J.debug("GC {}ms", Integer.valueOf(this.cfg.getGCTime()));
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
        ViatraScaleRunner.LOG4J.info("[WARMUP {} ({} of {}) ]===============================================================", i, Integer.valueOf(_plus), Integer.valueOf(this.cfg.getWarmups().size()));
        this.initViatra();
        this.preRun((i).intValue());
        this.gc();
        MddModel.changeTo("incremental");
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

  public void measure(final CSVLog log) {
    int _minimum = this.cfg.getMinimum();
    double _rate = this.cfg.getRate();
    final EarlyCancelMonitor viatraMonitor = new EarlyCancelMonitor(_minimum, _rate, CancellationThresholdMode.IF_BELOW);
    int _minimum_1 = this.cfg.getMinimum();
    double _rate_1 = this.cfg.getRate();
    final EarlyCancelMonitor problogMonitor = new EarlyCancelMonitor(_minimum_1, _rate_1, CancellationThresholdMode.IF_BELOW);
    int _minimum_2 = this.cfg.getMinimum();
    double _rate_2 = this.cfg.getRate();
    final EarlyCancelMonitor stormMonitor = new EarlyCancelMonitor(_minimum_2, _rate_2, CancellationThresholdMode.IF_BELOW);
    List<Integer> _seeds = this.cfg.seeds();
    for (final Integer seed : _seeds) {
      {
        int _indexOf = this.cfg.seeds().indexOf(seed);
        int _plus = (_indexOf + 1);
        ViatraScaleRunner.LOG4J.info("[MEASURE {} ({} of {}) ]===============================================================", seed, Integer.valueOf(_plus), Integer.valueOf(this.cfg.seeds().size()));
        this.initViatra();
        this.preRun((seed).intValue());
        this.gc();
        MddModel.changeTo("incremental");
        final Function0<Boolean> _function = () -> {
          return Boolean.valueOf(this.runViatra(log));
        };
        viatraMonitor.conditionalRun(_function);
        this.engine.dispose();
        this.gc();
        final Function0<Boolean> _function_1 = () -> {
          return Boolean.valueOf(this.runProblog(log));
        };
        problogMonitor.conditionalRun(_function_1);
        this.gc();
        final Function0<Boolean> _function_2 = () -> {
          return Boolean.valueOf(this.runStorm(log));
        };
        stormMonitor.conditionalRun(_function_2);
        log.log("iteration", Integer.valueOf(0));
        log.log("run", seed);
        log.log("prefix", this.cfg.getPrefix());
        log.log("size", Integer.valueOf(this.cfg.getSize()));
        log.commit();
      }
    }
  }

  public abstract boolean preRun(final int seed);

  public abstract boolean runViatra(final CSVLog log);

  public abstract boolean runProblog(final CSVLog log);

  public abstract boolean runStorm(final CSVLog log);
}
