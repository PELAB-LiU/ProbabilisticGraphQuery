package hu.bme.mit.inf.dslreasoner.domains.surveillance.viatra

import surveillance.SurveillancePackage

import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.SurveillanceHelper
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import org.eclipse.viatra.query.patternlanguage.emf.EMFPatternLanguageStandaloneSetup
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngineOptions
import org.eclipse.viatra.query.runtime.localsearch.matcher.integration.LocalSearchEMFBackendFactory
import org.eclipse.viatra.query.runtime.rete.matcher.ReteBackendFactory
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.common.util.URI
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParserBuilder
import org.eclipse.viatra.query.runtime.api.AdvancedViatraQueryEngine
import org.eclipse.viatra.query.runtime.emf.EMFScope
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.viatra.query.runtime.api.IQuerySpecification
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher
import hu.bme.mit.inf.querytransformation.query.StochasticPatternGenerator

class SurveillanceRunner {
	val StochasticPatternGenerator generator
	
	val AdvancedViatraQueryEngine engine1
	val Resource resource1
	val AdvancedViatraQueryEngine engine2
	val Resource resource2
	
	new(){
		Resource.Factory.Registry.INSTANCE.extensionToFactoryMap.putIfAbsent("xmi", new XMIResourceFactoryImpl())
		StochasticPatternGenerator.doSetup
		generator = new StochasticPatternGenerator
		
		EMFPatternLanguageStandaloneSetup.doSetup
		ViatraQueryEngineOptions.setSystemDefaultBackends(ReteBackendFactory.INSTANCE, ReteBackendFactory.INSTANCE,
			LocalSearchEMFBackendFactory.INSTANCE)
		
		EPackage.Registry.INSTANCE.put(SurveillancePackage.eINSTANCE.nsURI, SurveillancePackage.eINSTANCE)

		//val transformed = ""//generator.transformPatternFile(cfg.vql)
		
		
		
		
		
		val s1 = makeEngine(1, queries1)
		engine1 = s1.key
		resource1 = s1.value
		
		val s2 = makeEngine(2, queries1)
		engine2 = s2.key
		resource2 = s2.value
		
	}
	
	def makeEngine(int i, String queries){
		val resourceSet = new ResourceSetImpl
		val model = resourceSet.createResource(URI.createFileURI("model-tmp-"+i+".xmi"))
		
		val parsed = PatternParserBuilder.instance.parse(queries)
		
		val engine = 
			AdvancedViatraQueryEngine.createUnmanagedEngine(new EMFScope(resourceSet))
		parsed.querySpecifications.forEach[IQuerySpecification<? extends ViatraQueryMatcher> spec | engine.getMatcher(spec)]
		return engine -> model
	}
	
	def getMatches(AdvancedViatraQueryEngine engine){
		val query = engine.registeredQuerySpecifications.findFirst["elimination".equals(it.simpleName)]
		if(query!==null){
			println(engine.getMatcher(query).countMatches)/* forEach[match |
				//println(match.prettyPrint)
			]*/
		} else {
			throw new RuntimeException("This state should not be reachable!")
		}
	}
	
	
	val modelgen = new SurveillanceModelGenerator
	
	def iterate(SurveillanceWrapper wrapper, double threshold){
		wrapper.model.objects.forEach[obj |
			val old = obj.position
			val neww = SurveillanceHelper.move(old, obj.speed, obj.angle, 1)
			obj.position = neww 
			
			if(engine1!==null && engine1.tainted){
				throw new RuntimeException("Tainted engine 1.")
			}
			if(engine2!==null && engine2.tainted){
				throw new RuntimeException("Tainted engine 2.")
			}
		]
	}
	
	def run(){
		var wrapper = modelgen.make(200,0)

		getMatches(engine1)
		resource1.contents.add(wrapper.model)
		
		getMatches(engine1)
		
		val duplicate = EcoreUtil.copy(wrapper.model)
		for(i : 0..5){
			iterate(wrapper, 0.1)
		
			getMatches(engine1)	
		}
	}
	
	static val queries1 = '''
	package hu.bme.mit.inf.dslreasoner.domains.surveillance.queries;
	
	import "http://www.example.org/surveillance"
	import "http://www.eclipse.org/emf/2002/Ecore"
	
	import java hu.bme.mit.inf.querytransformation.query.KGate
	import java hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.Coordinate
	import java hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.SurveillanceHelper
	
	pattern targetToShoot(trg: UnidentifiedObject, probability: EDouble){
		find targettableObject(trg, probability);
	}
	pattern targettableObject(trg: UnidentifiedObject, confidence: EDouble){
		UnidentifiedObject.confidence(trg,confidence);
		UnidentifiedObject.speed(trg,speed);
		check(confidence > 0.65);
		check(SurveillanceHelper.spd30(speed));
	}
	pattern gunshot(from: Drone, to: UnidentifiedObject, probability: java Double){
		neg find killed(to);
		Drone.position(from, dp);
		find targettableObject(to,_);
		UnidentifiedObject.position(to,tp);
		check(SurveillanceHelper.dst1000(dp,tp));
		UnidentifiedObject.speed(to,speed);
		UnidentifiedObject.confidence(to, confidence);
		probability == eval(SurveillanceHelper.shotProbability(dp,tp,speed,confidence));
	}
	pattern killed(object: UnidentifiedObject){
		Shot.probability(s,p);
		Shot.at(s,object);
		check(p>0.95);
	}
	/**
	 * Assess probability of elimination
	 * + add shot for next iteration
	 */
	pattern attempt(from: Drone, to: UnidentifiedObject){
		find gunshot(from, to, _);
		find targetToShoot(to,_);
	}
	
	pattern elimination(target: UnidentifiedObject, probability: java Integer){
		probability == count find attempt(_, target);
	}
	
	'''
}