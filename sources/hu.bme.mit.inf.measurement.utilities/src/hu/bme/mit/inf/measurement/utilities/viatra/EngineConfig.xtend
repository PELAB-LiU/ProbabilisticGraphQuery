package hu.bme.mit.inf.measurement.utilities.viatra

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.List
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParsingResults
import reliability.mdd.MddModel
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.common.util.URI
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParserBuilder
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher
import org.eclipse.viatra.query.runtime.api.IQuerySpecification
import org.eclipse.viatra.query.runtime.emf.EMFScope

class EngineConfig{
	static val Logger LOG4J = LoggerFactory.getLogger(EngineConfig);
	
	static List<EngineConfig> configs = newLinkedList
	
	val String mddInstanceName
	val ResourceSet resourceSet
	@Accessors(PUBLIC_GETTER) val Resource model
	@Accessors(PUBLIC_GETTER) val SuspendedQueryEngine engine
	@Accessors(PUBLIC_GETTER) val PatternParsingResults parsed
	@Accessors(PUBLIC_GETTER) val MddModel mdd
	
	
	new(String queries, String name){
		mddInstanceName = name
		resourceSet = new ResourceSetImpl
		model = resourceSet.createResource(URI.createFileURI("model-tmp-"+mddInstanceName+this.hashCode+".xmi"))
		mdd = MddModel.getInstanceOf(mddInstanceName)
		MddModel.changeTo(mddInstanceName)
		mdd.resetQueries
		mdd.resetModel
		mdd.invalidateCache
		
		parsed = PatternParserBuilder.instance.parse(queries)
		if(parsed.hasError){
			LOG4J.error("Parsed with errors! {}", parsed.getErrors())
		}
		parsed.querySpecifications.forEach [ IQuerySpecification<? extends ViatraQueryMatcher> specification |
			mdd.registerSpecificationIfNeeded(specification)
		]
		
		val traceRes = resourceSet.createResource(URI.createFileURI("trace-tmp-"+mddInstanceName+this.hashCode+".xmi"))
		traceRes.contents.add(mdd.traceModel)
		
		engine = 
			SuspendedQueryEngine.create(new EMFScope(resourceSet))
		
		mdd.initializePatterns(engine)
		engine.enableAndPropagate
		engine.suspend()
		
		configs.add(this)
	}
	
	def acquire(){
		LOG4J.debug("Acquire {}", engine.hashCode)
		configs.forEach(cfg | cfg.suspend)
		MddModel.changeTo(mddInstanceName)
	}
	def suspend(){
		LOG4J.debug("Suspend {}", engine.hashCode)
		engine.suspend
	}
	def enable(){
		if(engine.tainted){
			val e = new IllegalStateException("Attempting to use tainted query engine.")
			LOG4J.error("Enable tainted engine! {}", engine.hashCode)
			throw e
		}
		LOG4J.debug("Propagate {}", engine.hashCode)
		engine.enableAndPropagate
	}
	def dispose(){
		if(!engine.disposed){
			configs.remove(this)
			//suspend?
			engine.dispose	
		}
	}
}