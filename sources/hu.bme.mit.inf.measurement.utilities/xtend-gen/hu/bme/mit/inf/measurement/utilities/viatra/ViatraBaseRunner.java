package hu.bme.mit.inf.measurement.utilities.viatra;

import hu.bme.mit.inf.measurement.utilities.CSVLog;
import hu.bme.mit.inf.measurement.utilities.configuration.BaseConfiguration;
import hu.bme.mit.inf.querytransformation.query.StochasticPatternGenerator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.viatra.query.patternlanguage.emf.EMFPatternLanguageStandaloneSetup;
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParserBuilder;
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParsingResults;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngineOptions;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.viatra.query.runtime.localsearch.matcher.integration.LocalSearchEMFBackendFactory;
import org.eclipse.viatra.query.runtime.rete.matcher.ReteBackendFactory;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IntegerRange;

@SuppressWarnings("all")
public abstract class ViatraBaseRunner<Config extends BaseConfiguration> {
  protected final Config cfg;

  protected final StochasticPatternGenerator generator;

  protected final String transformed;

  protected EngineConfig batch;

  protected EngineConfig incremental;

  protected Resource model;

  public void initBatch() {
    EngineConfig _engineConfig = new EngineConfig(this.transformed, "standalone");
    this.batch = _engineConfig;
  }

  public void initIncremental() {
    EngineConfig _engineConfig = new EngineConfig(this.transformed, "incremental");
    this.incremental = _engineConfig;
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
  }

  public void gc() {
    try {
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
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("[WARMUP ");
        _builder.append(i);
        _builder.append(" of ");
        int _size = this.cfg.getWarmups().size();
        _builder.append(_size);
        _builder.append("]===============================================================");
        InputOutput.<String>println(_builder.toString());
        this.initIncremental();
        this.setupInitialModel((i).intValue());
        int _iterations = this.cfg.getIterations();
        IntegerRange _upTo = new IntegerRange(0, _iterations);
        for (final Integer iter : _upTo) {
          {
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
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("[MEASURE ");
        _builder.append(seed);
        _builder.append(" of ");
        int _size = this.cfg.seeds().size();
        _builder.append(_size);
        _builder.append("]===============================================================");
        InputOutput.<String>println(_builder.toString());
        this.initIncremental();
        this.setupInitialModel((seed).intValue());
        int _iterations = this.cfg.getIterations();
        IntegerRange _upTo = new IntegerRange(0, _iterations);
        for (final Integer iter : _upTo) {
          {
            this.gc();
            this.initBatch();
            this.batch.acquire();
            this.runBatch(log);
            this.batch.dispose();
            this.gc();
            this.incremental.acquire();
            this.runIncremental(log);
            this.incremental.suspend();
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

  public PatternParsingResults parseQueries(final String vql) {
    final PatternParsingResults result = PatternParserBuilder.instance().parse(vql);
    final boolean fault = result.hasError();
    final Consumer<Issue> _function = (Issue issue) -> {
      Severity _severity = issue.getSeverity();
      boolean _tripleEquals = (_severity == Severity.ERROR);
      if (_tripleEquals) {
        InputOutput.<String>println(("Error: " + issue));
      }
    };
    result.getAllDiagnostics().forEach(_function);
    if (fault) {
      System.err.println(vql);
    }
    return result;
  }

  public int countBasicEvents(final EngineConfig engine) {
    final int[] cnt = new int[2];
    final Consumer<IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>> _function = (IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> specification) -> {
      final ViatraQueryMatcher<? extends IPatternMatch> matcher = engine.getEngine().getMatcher(specification);
      cnt[0] = matcher.countMatches();
    };
    engine.getParsed().getQuerySpecification("BERequiredName1").ifPresent(_function);
    final Consumer<IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>> _function_1 = (IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> specification) -> {
      final ViatraQueryMatcher<? extends IPatternMatch> matcher = engine.getEngine().getMatcher(specification);
      cnt[1] = matcher.countMatches();
    };
    engine.getParsed().getQuerySpecification("BERequiredName2").ifPresent(_function_1);
    int _get = cnt[0];
    int _get_1 = cnt[1];
    return (_get + _get_1);
  }

  public int countStochasticPatterns(final EngineConfig engine) {
    final int[] cnt = new int[1];
    final Consumer<IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>> _function = (IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> specification) -> {
      final ViatraQueryMatcher<? extends IPatternMatch> matcher = engine.getEngine().getMatcher(specification);
      final Consumer<IPatternMatch> _function_1 = (IPatternMatch match) -> {
        Object _get = match.get(0);
        cnt[0] = (((Integer) _get)).intValue();
      };
      matcher.getOneArbitraryMatch().ifPresent(_function_1);
    };
    engine.getParsed().getQuerySpecification("stochasticCount").ifPresent(_function);
    return cnt[0];
  }

  public double checkMatches(final String name, final EngineConfig engine) {
    final double[] cnt = new double[1];
    final Consumer<IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>> _function = (IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> specification) -> {
      final ViatraQueryMatcher<? extends IPatternMatch> matcher = engine.getEngine().getMatcher(specification);
      String _simpleName = specification.getSimpleName();
      String _plus = ("Specification found: " + _simpleName);
      InputOutput.<String>println(_plus);
      final Consumer<IPatternMatch> _function_1 = (IPatternMatch match) -> {
        String _prettyPrint = match.prettyPrint();
        String _plus_1 = ("\t" + _prettyPrint);
        InputOutput.<String>println(_plus_1);
      };
      matcher.forEachMatch(_function_1);
      final Consumer<IPatternMatch> _function_2 = (IPatternMatch match) -> {
        Object _get = match.get(0);
        cnt[0] = (((Double) _get)).doubleValue();
      };
      matcher.getOneArbitraryMatch().ifPresent(_function_2);
    };
    engine.getParsed().getQuerySpecification(name).ifPresent(_function);
    return cnt[0];
  }

  public void printMatches(final String name, final EngineConfig engine) {
    final Consumer<IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>> _function = (IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> specification) -> {
      final ViatraQueryMatcher<? extends IPatternMatch> matcher = engine.getEngine().getMatcher(specification);
      String _simpleName = specification.getSimpleName();
      String _plus = ("Specification found: " + _simpleName);
      InputOutput.<String>println(_plus);
      final Consumer<IPatternMatch> _function_1 = (IPatternMatch match) -> {
        String _prettyPrint = match.prettyPrint();
        String _plus_1 = ("\t" + _prettyPrint);
        InputOutput.<String>println(_plus_1);
      };
      matcher.forEachMatch(_function_1);
    };
    engine.getParsed().getQuerySpecification(name).ifPresent(_function);
  }

  public void initializePatterns(final EngineConfig engine, final String... queries) {
    final Consumer<String> _function = (String name) -> {
      final Consumer<IQuerySpecification<? extends ViatraQueryMatcher>> _function_1 = (IQuerySpecification<? extends ViatraQueryMatcher> specification) -> {
        final int cnt = engine.getEngine().getMatcher(specification).countMatches();
      };
      engine.getParsed().getQuerySpecification(name).ifPresent(_function_1);
    };
    ((List<String>)Conversions.doWrapArray(queries)).forEach(_function);
  }
}
