package hu.bme.mit.inf.measurement;

import hu.bme.mit.inf.querytransformation.query.StochasticPatternGenerator;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.viatra.query.patternlanguage.emf.EMFPatternLanguageStandaloneSetup;
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParserBuilder;
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParsingResults;
import org.eclipse.viatra.query.runtime.api.AdvancedViatraQueryEngine;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngine;
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngineOptions;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.viatra.query.runtime.emf.EMFScope;
import org.eclipse.viatra.query.runtime.localsearch.matcher.integration.LocalSearchEMFBackendFactory;
import org.eclipse.viatra.query.runtime.rete.matcher.ReteBackendFactory;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.junit.Test;
import problog.Generation;
import reliability.mdd.MddModel;
import satellite1.GroundStationNetwork;
import satellite1.InterferometryMission;
import satellite1.SatelliteFactory;
import satellite1.SatellitePackage;
import satellite1.SmallSat;
import satellite1.XCommSubsystem;

@SuppressWarnings("all")
public class SatelliteTest {
  @Test
  public void init() {
    Map<String, Object> _extensionToFactoryMap = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();
    XMIResourceFactoryImpl _xMIResourceFactoryImpl = new XMIResourceFactoryImpl();
    _extensionToFactoryMap.putIfAbsent("xmi", _xMIResourceFactoryImpl);
    StochasticPatternGenerator.doSetup();
    final StochasticPatternGenerator generator = new StochasticPatternGenerator();
    EMFPatternLanguageStandaloneSetup.doSetup();
    ViatraQueryEngineOptions.setSystemDefaultBackends(ReteBackendFactory.INSTANCE, ReteBackendFactory.INSTANCE, 
      LocalSearchEMFBackendFactory.INSTANCE);
    EPackage.Registry.INSTANCE.put(SatellitePackage.eINSTANCE.getNsURI(), SatellitePackage.eINSTANCE);
    File _file = new File("../hu.bme.mit.inf.dslreasoner.domains.satellite1/src/hu/bme/mit/inf/dslreasoner/domains/satellite1/queries/reliability.vql");
    final String transformed = generator.transformPatternFile(_file);
    final PatternParsingResults parsed = this.parseQueries(transformed);
    boolean _hasError = parsed.hasError();
    if (_hasError) {
      final Consumer<Issue> _function = (Issue it) -> {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("Parse error: ");
        _builder.append(it);
        InputOutput.<String>println(_builder.toString());
      };
      parsed.getErrors().forEach(_function);
    }
    MddModel.changeTo("standalone");
    final MddModel standaloneMDD = MddModel.getInstanceOf("standalone");
    standaloneMDD.resetModel();
    final ResourceSetImpl standaloneResourceSet = new ResourceSetImpl();
    standaloneResourceSet.getResources().clear();
    final Consumer<IQuerySpecification<? extends ViatraQueryMatcher>> _function_1 = (IQuerySpecification<? extends ViatraQueryMatcher> specification) -> {
      standaloneMDD.registerSpecificationIfNeeded(specification);
    };
    parsed.getQuerySpecifications().forEach(_function_1);
    final Resource traceRes = standaloneResourceSet.createResource(URI.createFileURI("trace-tmp-std.xmi"));
    traceRes.getContents().add(standaloneMDD.getTraceModel());
    EMFScope _eMFScope = new EMFScope(standaloneResourceSet);
    final AdvancedViatraQueryEngine standalone = AdvancedViatraQueryEngine.createUnmanagedEngine(_eMFScope);
    standaloneMDD.initializePatterns(standalone);
    final Consumer<String> _function_2 = (String name) -> {
      final Consumer<IQuerySpecification<? extends ViatraQueryMatcher>> _function_3 = (IQuerySpecification<? extends ViatraQueryMatcher> specification) -> {
        final int cnt = standalone.getMatcher(specification).countMatches();
      };
      parsed.getQuerySpecification(name).ifPresent(_function_3);
    };
    Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("coverage", "ua_online")).forEach(_function_2);
    final SatelliteFactory factory = SatelliteFactory.eINSTANCE;
    final InterferometryMission mission = factory.createInterferometryMission();
    final GroundStationNetwork g = factory.createGroundStationNetwork();
    final XCommSubsystem cg = factory.createXCommSubsystem();
    g.getCommSubsystem().add(cg);
    mission.setGroundStationNetwork(g);
    final SmallSat s1 = factory.createSmallSat();
    final XCommSubsystem c1 = factory.createXCommSubsystem();
    s1.getCommSubsystem().add(c1);
    c1.setTarget(cg);
    s1.setPayload(factory.createInterferometryPayload());
    mission.getSpacecraft().add(s1);
    final SmallSat s2 = factory.createSmallSat();
    final XCommSubsystem c2 = factory.createXCommSubsystem();
    s2.getCommSubsystem().add(c2);
    c2.setTarget(c1);
    s2.setPayload(factory.createInterferometryPayload());
    mission.getSpacecraft().add(s2);
    final Resource resource = standaloneResourceSet.createResource(URI.createFileURI("tmp-domain-standalone.xmi"));
    resource.getContents().add(mission);
    standaloneMDD.unaryForAll(standalone);
    this.checkMatches("coverage", parsed, standalone);
    this.runProblog(mission);
    final XCommSubsystem c3 = factory.createXCommSubsystem();
    s1.getCommSubsystem().add(c3);
    c3.setTarget(cg);
    c2.setFallback(c3);
    standaloneMDD.unaryForAll(standalone);
    this.checkMatches("coverage", parsed, standalone);
    this.runProblog(mission);
  }

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

  public void checkMatches(final String name, final PatternParsingResults parsed, final ViatraQueryEngine engine) {
    final double[] cnt = new double[1];
    final Consumer<IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>> _function = (IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> specification) -> {
      final ViatraQueryMatcher<? extends IPatternMatch> matcher = engine.getMatcher(specification);
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
    parsed.getQuerySpecification(name).ifPresent(_function);
  }

  public void runProblog(final InterferometryMission mission) {
    try {
      final File file = new File("foo.pl");
      file.createNewFile();
      final FileWriter writer = new FileWriter(file);
      final ProcessBuilder builder = new ProcessBuilder("problog", "foo.pl");
      final long start = System.nanoTime();
      final String plmodel = new Generation().generateFrom(mission).toString();
      writer.write(plmodel);
      writer.flush();
      writer.close();
      final long trafo = System.nanoTime();
      final Process process = builder.start();
      final HashMap<String, Object> output = new HashMap<String, Object>();
      InputStream _inputStream = process.getInputStream();
      final Scanner io = new Scanner(_inputStream);
      final Procedure1<String> _function = (String line) -> {
        InputOutput.<String>println(("Debug: " + line));
      };
      IteratorExtensions.<String>forEach(io, _function);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
