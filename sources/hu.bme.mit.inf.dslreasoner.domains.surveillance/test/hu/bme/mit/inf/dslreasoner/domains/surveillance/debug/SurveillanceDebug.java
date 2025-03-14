package hu.bme.mit.inf.dslreasoner.domains.surveillance.debug;

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.viatra.query.patternlanguage.emf.EMFPatternLanguageStandaloneSetup;
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParserBuilder;
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParsingResults;
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngineOptions;
import org.eclipse.viatra.query.runtime.emf.EMFScope;
import org.eclipse.viatra.query.runtime.localsearch.matcher.integration.LocalSearchEMFBackendFactory;
import org.eclipse.viatra.query.runtime.rete.matcher.ReteBackendFactory;
import org.eclipse.xtext.diagnostics.Severity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.ecore.EObject;

import hu.bme.mit.inf.dslreasoner.domains.surveillance.viatra.SurveillanceModelGenerator;
import hu.bme.mit.inf.dslreasoner.domains.surveillance.viatra.SurveillanceRunner;
import hu.bme.mit.inf.dslreasoner.domains.surveillance.viatra.SurveillanceWrapper;
import hu.bme.mit.inf.measurement.utilities.configuration.SurveillanceConfiguration;
import hu.bme.mit.inf.measurement.utilities.viatra.SuspendedQueryEngine;
import hu.bme.mit.inf.querytransformation.query.StochasticPatternGenerator;
import reliability.mdd.MddModel;
import surveillance.SurveillancePackage;

public class SurveillanceDebug /*extends SurveillanceRunner*/{
	/*private static PatternParsingResults parsed;
	private static SurveillanceModelGenerator modelgen;
	public static SurveillanceConfiguration generateConfig() {
		var config = new SurveillanceConfiguration();
		
		return SurveillanceConfiguration.parse(new String[] {
				"--case","SRV",
				"--vql","TODO",
				"--size","200",
				"--seed","3",
				"--vql","TODO",
				"--vql","TODO",
				
		});
		
	}
	public SurveillanceDebug(SurveillanceConfiguration cfg) {
		super(generateConfig());
		// TODO Auto-generated constructor stub
	}
	private StochasticPatternGenerator generator;
	private PatternParsingResults parsed;
	
	//protected var SuspendedQueryEngine incremental
	private static MddModel incrementalMDD;
	private static ResourceSet incrementalResourceSet;
	private static Resource incrementalDomainResource;
	
	@Test
	public void debug() {
		// iteration 0
		var instance = modelgen.make(200, 3);
		incrementalDomainResource.contents.add(instance.model)
		
		// iteration 1
		modelgen.iterate(instance, 0.1)
	}
	
	@BeforeEach
	public  void setup() {

	}
	public void batch(SurveillanceWrapper instance) {
		MddModel.changeTo("standalone");
		var standaloneMDD = MddModel.getInstanceOf("standalone");
		standaloneMDD.resetModel();
		
		var standaloneResourceSet = new ResourceSetImpl();
		
		parsed.getQuerySpecifications().forEach(specification -> {
			standaloneMDD.registerSpecificationIfNeeded(specification);
		});
		
		var traceRes = standaloneResourceSet.createResource(URI.createFileURI("trace-tmp-std.xmi"));
		traceRes.getContents().add(standaloneMDD.getTraceModel());
		
		standalone = 
			SuspendedQueryEngine.create(new EMFScope(standaloneResourceSet));
			
		standaloneMDD.initializePatterns(standalone);
		standalone.enableAndPropagate();
		standalone.suspend();
		
		
		var resource = standaloneResourceSet.createResource(URI.createFileURI("tmp-domain-standalone.xmi"));
		Copier copier = new Copier();
		var result = copier.copy(instance.model);
		copier.copyReferences();
		resource.getContents().add(result);
		Map<EObject,Integer> index = new HashMap<EObject,Integer>();
		copier.entrySet().forEach(it -> index.put(it.getValue(), instance.ordering.get(it.getKey())));
		
		standalone.enableAndPropagate
					
	}
	@BeforeAll
	public static void setupGlobal() {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().putIfAbsent("xmi", new XMIResourceFactoryImpl());
		StochasticPatternGenerator.doSetup();
		
		var generator = new StochasticPatternGenerator();
		
		EMFPatternLanguageStandaloneSetup.doSetup();
		ViatraQueryEngineOptions.setSystemDefaultBackends(ReteBackendFactory.INSTANCE, ReteBackendFactory.INSTANCE,
			LocalSearchEMFBackendFactory.INSTANCE);
		
		EPackage.Registry.INSTANCE.put(SurveillancePackage.eINSTANCE.getNsURI(), SurveillancePackage.eINSTANCE);

		var transformed = generator.transformPatternFile(new File("src/hu/bme/mit/inf/dslreasoner/domains/surveillance/queries/surveillance.vql"));
		parsed = PatternParserBuilder.instance().parse(transformed);

		if(parsed.hasError()){
			parsed.getErrors().forEach(System.out::println);
		}
		assertFalse(parsed.hasError());
		
		modelgen = new SurveillanceModelGenerator();
	}
	public String getMatches(String query, SuspendedQueryEngine engine, Map<EObject,Integer> index) {
		var builder = new StringBuilder();
		builder.append("{").append(System.lineSeparator());
		builder.append("\"\"").append(System.lineSeparator());
	}
	def String getMatchesJSON(PatternParsingResults parsed, SuspendedQueryEngine engine, Map<EObject,Integer> index){
		val opt = parsed.getQuerySpecification("elimination")
		if(opt.isPresent){
			val matcher = opt.get
			'''
			{
				"valid" : true,
				"matches" : [
				«FOR match : engine.getMatcher(matcher).allMatches SEPARATOR ","»
					{
						"object" : "object«index.get(match.get(0))»",
						"probability" : «match.get(1)»
					}
				«ENDFOR»
				]
			}
			'''
		} else {
			return '''{"valid" : false, "matches" : []}'''
		}
	}*/
}
