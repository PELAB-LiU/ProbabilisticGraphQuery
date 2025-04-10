package hu.bme.mit.inf.dslreasoner.domains.surveillance.viatra;

import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.Coordinate;
import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.SurveillanceHelper;
import hu.bme.mit.inf.querytransformation.query.StochasticPatternGenerator;
import java.util.Map;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.viatra.query.patternlanguage.emf.EMFPatternLanguageStandaloneSetup;
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParserBuilder;
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParsingResults;
import org.eclipse.viatra.query.runtime.api.AdvancedViatraQueryEngine;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngineOptions;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.viatra.query.runtime.emf.EMFScope;
import org.eclipse.viatra.query.runtime.localsearch.matcher.integration.LocalSearchEMFBackendFactory;
import org.eclipse.viatra.query.runtime.rete.matcher.ReteBackendFactory;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import surveillance.MovingObject;
import surveillance.SurveillanceModel;
import surveillance.SurveillancePackage;

@SuppressWarnings("all")
public class SurveillanceRunner {
  private final StochasticPatternGenerator generator;

  private final AdvancedViatraQueryEngine engine1;

  private final Resource resource1;

  private final AdvancedViatraQueryEngine engine2;

  private final Resource resource2;

  public SurveillanceRunner() {
    Map<String, Object> _extensionToFactoryMap = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();
    XMIResourceFactoryImpl _xMIResourceFactoryImpl = new XMIResourceFactoryImpl();
    _extensionToFactoryMap.putIfAbsent("xmi", _xMIResourceFactoryImpl);
    StochasticPatternGenerator.doSetup();
    StochasticPatternGenerator _stochasticPatternGenerator = new StochasticPatternGenerator();
    this.generator = _stochasticPatternGenerator;
    EMFPatternLanguageStandaloneSetup.doSetup();
    ViatraQueryEngineOptions.setSystemDefaultBackends(ReteBackendFactory.INSTANCE, ReteBackendFactory.INSTANCE, 
      LocalSearchEMFBackendFactory.INSTANCE);
    EPackage.Registry.INSTANCE.put(SurveillancePackage.eINSTANCE.getNsURI(), SurveillancePackage.eINSTANCE);
    final Pair<AdvancedViatraQueryEngine, Resource> s1 = this.makeEngine(1, SurveillanceRunner.queries1);
    this.engine1 = s1.getKey();
    this.resource1 = s1.getValue();
    final Pair<AdvancedViatraQueryEngine, Resource> s2 = this.makeEngine(2, SurveillanceRunner.queries1);
    this.engine2 = s2.getKey();
    this.resource2 = s2.getValue();
  }

  public Pair<AdvancedViatraQueryEngine, Resource> makeEngine(final int i, final String queries) {
    final ResourceSetImpl resourceSet = new ResourceSetImpl();
    final Resource model = resourceSet.createResource(URI.createFileURI((("model-tmp-" + Integer.valueOf(i)) + ".xmi")));
    final PatternParsingResults parsed = PatternParserBuilder.instance().parse(queries);
    EMFScope _eMFScope = new EMFScope(resourceSet);
    final AdvancedViatraQueryEngine engine = AdvancedViatraQueryEngine.createUnmanagedEngine(_eMFScope);
    final Consumer<IQuerySpecification<? extends ViatraQueryMatcher>> _function = (IQuerySpecification<? extends ViatraQueryMatcher> spec) -> {
      engine.getMatcher(spec);
    };
    parsed.getQuerySpecifications().forEach(_function);
    return Pair.<AdvancedViatraQueryEngine, Resource>of(engine, model);
  }

  public Integer getMatches(final AdvancedViatraQueryEngine engine) {
    Integer _xblockexpression = null;
    {
      final Function1<IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>, Boolean> _function = (IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> it) -> {
        return Boolean.valueOf("elimination".equals(it.getSimpleName()));
      };
      final IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> query = IterableExtensions.<IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>>findFirst(engine.getRegisteredQuerySpecifications(), _function);
      Integer _xifexpression = null;
      if ((query != null)) {
        _xifexpression = InputOutput.<Integer>println(Integer.valueOf(engine.getMatcher(query).countMatches()));
      } else {
        throw new RuntimeException("This state should not be reachable!");
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }

  private final SurveillanceModelGenerator modelgen = new SurveillanceModelGenerator();

  public void iterate(final SurveillanceWrapper wrapper, final double threshold) {
    final Consumer<MovingObject> _function = (MovingObject obj) -> {
      final Coordinate old = obj.getPosition();
      final Coordinate neww = SurveillanceHelper.move(old, obj.getSpeed(), obj.getAngle(), 1);
      obj.setPosition(neww);
      if (((this.engine1 != null) && this.engine1.isTainted())) {
        throw new RuntimeException("Tainted engine 1.");
      }
      if (((this.engine2 != null) && this.engine2.isTainted())) {
        throw new RuntimeException("Tainted engine 2.");
      }
    };
    wrapper.model.getObjects().forEach(_function);
  }

  public void run() {
    SurveillanceWrapper wrapper = this.modelgen.make(200, 0);
    this.getMatches(this.engine1);
    this.resource1.getContents().add(wrapper.model);
    this.getMatches(this.engine1);
    final SurveillanceModel duplicate = EcoreUtil.<SurveillanceModel>copy(wrapper.model);
    IntegerRange _upTo = new IntegerRange(0, 5);
    for (final Integer i : _upTo) {
      {
        this.iterate(wrapper, 0.1);
        this.getMatches(this.engine1);
      }
    }
  }

  private static final String queries1 = new Function0<String>() {
    @Override
    public String apply() {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("package hu.bme.mit.inf.dslreasoner.domains.surveillance.queries;");
      _builder.newLine();
      _builder.newLine();
      _builder.append("import \"http://www.example.org/surveillance\"");
      _builder.newLine();
      _builder.append("import \"http://www.eclipse.org/emf/2002/Ecore\"");
      _builder.newLine();
      _builder.newLine();
      _builder.append("import java hu.bme.mit.inf.querytransformation.query.KGate");
      _builder.newLine();
      _builder.append("import java hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.Coordinate");
      _builder.newLine();
      _builder.append("import java hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.SurveillanceHelper");
      _builder.newLine();
      _builder.newLine();
      _builder.append("pattern targetToShoot(trg: UnidentifiedObject, probability: EDouble){");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("find targettableObject(trg, probability);");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _builder.append("pattern targettableObject(trg: UnidentifiedObject, confidence: EDouble){");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("UnidentifiedObject.confidence(trg,confidence);");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("UnidentifiedObject.speed(trg,speed);");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("check(confidence > 0.65);");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("check(SurveillanceHelper.spd30(speed));");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _builder.append("pattern gunshot(from: Drone, to: UnidentifiedObject, probability: java Double){");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("neg find killed(to);");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("Drone.position(from, dp);");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("find targettableObject(to,_);");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("UnidentifiedObject.position(to,tp);");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("check(SurveillanceHelper.dst1000(dp,tp));");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("UnidentifiedObject.speed(to,speed);");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("UnidentifiedObject.confidence(to, confidence);");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("probability == eval(SurveillanceHelper.shotProbability(dp,tp,speed,confidence));");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _builder.append("pattern killed(object: UnidentifiedObject){");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("Shot.probability(s,p);");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("Shot.at(s,object);");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("check(p>0.95);");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _builder.append("/**");
      _builder.newLine();
      _builder.append(" ");
      _builder.append("* Assess probability of elimination");
      _builder.newLine();
      _builder.append(" ");
      _builder.append("* + add shot for next iteration");
      _builder.newLine();
      _builder.append(" ");
      _builder.append("*/");
      _builder.newLine();
      _builder.append("pattern attempt(from: Drone, to: UnidentifiedObject){");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("find gunshot(from, to, _);");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("find targetToShoot(to,_);");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("pattern elimination(target: UnidentifiedObject, probability: java Integer){");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("probability == count find attempt(_, target);");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      return _builder.toString();
    }
  }.apply();
}
