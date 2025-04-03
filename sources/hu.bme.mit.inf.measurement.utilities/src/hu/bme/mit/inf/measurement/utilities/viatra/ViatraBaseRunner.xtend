package hu.bme.mit.inf.measurement.utilities.viatra

import hu.bme.mit.inf.measurement.utilities.Config
import hu.bme.mit.inf.measurement.utilities.CSVLog
import org.eclipse.emf.ecore.resource.Resource
import reliability.mdd.MddModel
import org.eclipse.viatra.query.runtime.api.AdvancedViatraQueryEngine
import java.util.concurrent.Callable
import java.util.function.Consumer
import hu.bme.mit.inf.querytransformation.query.StochasticPatternGenerator
import org.eclipse.viatra.query.patternlanguage.emf.EMFPatternLanguageStandaloneSetup
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngineOptions
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.common.util.URI
import org.eclipse.viatra.query.runtime.localsearch.matcher.integration.LocalSearchEMFBackendFactory
import org.eclipse.viatra.query.runtime.rete.matcher.ReteBackendFactory
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParserBuilder
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParsingResults
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngine
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.viatra.query.runtime.emf.EMFScope
import org.eclipse.xtext.diagnostics.Severity
import hu.bme.mit.inf.measurement.utilities.configuration.BaseConfiguration
import org.eclipse.viatra.query.runtime.api.IQuerySpecification
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher
import hu.bme.mit.inf.measurement.utilities.configuration.SatelliteConfiguration
import java.io.File
import org.eclipse.emf.ecore.EPackage
import reliability.intreface.CacheMode
import java.util.List
import org.eclipse.xtend.lib.annotations.Accessors
import org.slf4j.Logger
import org.slf4j.LoggerFactory

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

abstract class ViatraBaseRunner<Config extends BaseConfiguration> { 
	static val Logger LOG4J = LoggerFactory.getLogger(ViatraBaseRunner);
	
	protected val Config cfg
	
	protected val StochasticPatternGenerator generator
	protected val String transformed
	
	protected var EngineConfig batch
	protected var EngineConfig incremental
	
	protected var Resource model
	
	def void initBatch(){
		batch = new EngineConfig(transformed, "standalone")
		LOG4J.debug("Init Batch {}", batch.engine.hashCode)
	}
	def void initIncremental(){
		incremental = new EngineConfig(transformed, "incremental")
		LOG4J.debug("Init Incremetnal {}", incremental.engine.hashCode)
	}
	
	/**
	 * Project specific todo: Register EPackages
	 */
	new(Config cfg, EPackage domain){
		this.cfg = cfg

		Resource.Factory.Registry.INSTANCE.extensionToFactoryMap.putIfAbsent("xmi", new XMIResourceFactoryImpl())
		StochasticPatternGenerator.doSetup
		generator = new StochasticPatternGenerator
		
		EMFPatternLanguageStandaloneSetup.doSetup
		ViatraQueryEngineOptions.setSystemDefaultBackends(ReteBackendFactory.INSTANCE, ReteBackendFactory.INSTANCE,
			LocalSearchEMFBackendFactory.INSTANCE)
		
		EPackage.Registry.INSTANCE.put(domain.nsURI, domain)

		transformed = generator.transformPatternFile(cfg.vql)
		LOG4J.info("Queries {}", transformed)
	}
	
	
	def gc(){
		LOG4J.debug("GC {}ms", cfg.GCTime)
		System.gc()
		Thread.sleep(cfg.GCTime)
		System.gc()
		Thread.sleep(cfg.GCTime)
	}
	def void run(){
		val warmlog = new CSVLog(cfg.CSVcolumns,cfg.delimiter)
		try{
			warmup(warmlog)
		} finally {
			cfg.outWarmup.print(warmlog)
		}
		val measlog = new CSVLog(cfg.CSVcolumns,cfg.delimiter)
		try {
			measure(measlog)
		} finally {
			cfg.out.print(measlog)
		}
	}
	def void warmup(CSVLog log){
		for(i : cfg.warmups){
			LOG4J.info("[WARMUP {} ({} of {}) ]===============================================================", i, cfg.warmups.indexOf(i)+1, cfg.warmups.size)
			//println('''[WARMUP «i» of «cfg.warmups.size»]===============================================================''')
			initIncremental()
			setupInitialModel(i)
			/**
			 * Incremental iterations
			 */
			for(iter : 0..cfg.iterations){
				LOG4J.info("[ITERATION {} of {} WARMUP {} ({} of {}) ]===============================================================", iter, cfg.iterations, i, cfg.warmups.indexOf(i)+1, cfg.warmups.size)
				gc()
				incremental.acquire
				runIncremental(log)
				incremental.suspend
				
				gc()
				initBatch
				batch.acquire
				runBatch(log)
				batch.dispose
				
				/**
				 * ProbLog and Storm is not affected by JVM warmup, thus it is safe to skip
				 * Some runs are included to test if it works
				 */
				if(i<2){
					gc()
					runProblog(log)
					
					gc()
					runStorm(log)
				}
				
				log.log("iteration", iter)
				log.log("run",i)
				log.log("prefix", cfg.prefix)
				log.log("size", cfg.size)
				log.commit
				
				applyIncrement
			}
			incremental.dispose
		}
	}
	def void measure(CSVLog log){
		for(seed : cfg.seeds){
			LOG4J.info("[MEASURE {} ({} of {}) ]===============================================================", seed, cfg.seeds.indexOf(seed)+1, cfg.seeds.size)
			//println('''[MEASURE «seed» of «cfg.seeds.size»]===============================================================''')
			initIncremental()
			setupInitialModel(seed)
				
			/**
			 * Incremental iterations
			 */
			for(iter : 0..cfg.iterations){
				LOG4J.info("[ITERATION {} of {} MEASURE {} ({} of {}) ]===============================================================", iter, cfg.iterations, seed, cfg.seeds.indexOf(seed)+1, cfg.seeds.size)
				gc()
				initBatch
				batch.acquire
				runBatch(log)
				batch.dispose
				
				gc()
				incremental.acquire
				runIncremental(log)
				incremental.suspend
				
				gc()
				runProblog(log)
				
				gc()
				runStorm(log)
				
				log.log("iteration", iter)
				log.log("prefix", cfg.prefix)
				log.log("run",seed)
				log.log("size", cfg.size)
				log.commit
				
				applyIncrement
			}
			incremental.dispose
		}
	}
	
	def abstract void setupInitialModel(int seed)
	def abstract void runIncremental(CSVLog log)
	def abstract void runBatch(CSVLog log)
	def abstract void applyIncrement()
	def abstract void runProblog(CSVLog log)
	def abstract void runStorm(CSVLog log)
	
	def parseQueries(String vql){
		val result = PatternParserBuilder.instance.parse(vql)
		val fault = result.hasError
		result.allDiagnostics.forEach[issue | 
			if(issue.severity === Severity.ERROR){
				println("Error: "+issue)
			}
		]
		if(fault){
			System.err.println(vql)
		}
		return result
	}
	
	def int countBasicEvents(EngineConfig engine){
		val cnt = newIntArrayOfSize(2)
		engine.parsed.getQuerySpecification("BERequiredName1").ifPresent([specification |
			val matcher = engine.engine.getMatcher(specification)
			cnt.set(0,matcher.countMatches)
		])
		engine.parsed.getQuerySpecification("BERequiredName2").ifPresent([specification |
			val matcher = engine.engine.getMatcher(specification)
			cnt.set(1,matcher.countMatches)
		])
		return cnt.get(0)+cnt.get(1)
	}
	def countStochasticPatterns(EngineConfig engine){
		val cnt = newIntArrayOfSize(1)
		engine.parsed.getQuerySpecification("stochasticCount").ifPresent([specification |
			val matcher = engine.engine.getMatcher(specification)
			matcher.oneArbitraryMatch.ifPresent([match |
				cnt.set(0, match.get(0) as Integer)
			])
		])
		return cnt.get(0)
	}
	def checkMatches(String name, EngineConfig engine){
		val cnt = newDoubleArrayOfSize(1)
		engine.parsed.getQuerySpecification(name).ifPresent([specification |
			val matcher = engine.engine.getMatcher(specification)	
			println("Specification found: "+specification.simpleName)
			matcher.forEachMatch([match |
				println("\t"+match.prettyPrint)
			])
			matcher.oneArbitraryMatch.ifPresent([match |
				cnt.set(0, match.get(0) as Double)
			])
		])
		return cnt.get(0)
	}
	def printMatches(String name, EngineConfig engine){
		engine.parsed.getQuerySpecification(name).ifPresent([specification |
			val matcher = engine.engine.getMatcher(specification)	
			println("Specification found: "+specification.simpleName)
			matcher.forEachMatch([match |
				println("\t"+match.prettyPrint)
			])
		])
	}
	def initializePatterns(EngineConfig engine, String... queries){
		queries.forEach([name | 
			engine.parsed.getQuerySpecification(name).ifPresent([IQuerySpecification<? extends ViatraQueryMatcher> specification |
				val cnt = engine.engine.getMatcher(specification).countMatches
			])
		])
	}
}