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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
public abstract class BaseRunner<Config extends BaseConfiguration> {
  private static final Logger LOG4J = LoggerFactory.getLogger(ScalingMeasurementRunner.class);

  protected final Config cfg;

  protected final StochasticPatternGenerator generator;

  protected final String transformed;

  public BaseRunner(final Config cfg, final EPackage domain) {
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
    BaseRunner.LOG4J.info("Queries {}", this.transformed);
  }

  public void gc() {
    try {
      BaseRunner.LOG4J.debug("GC {}ms", Integer.valueOf(this.cfg.getGCTime()));
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

  public abstract void warmup(final CSVLog log);

  public abstract void measure(final CSVLog log);
}
