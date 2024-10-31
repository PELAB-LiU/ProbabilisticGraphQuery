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
import tracemodel.TraceModel
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

abstract class ViatraScaleRunner<Config extends BaseConfiguration> { 
	protected val Config cfg
	
	protected val StochasticPatternGenerator generator
	protected val PatternParsingResults parsed
	
	protected var SuspendedQueryEngine engine
	protected val MddModel MDD
	protected val ResourceSet resourceSet
	protected val Resource domainResource 
	
	def void initIncremental(){ 
		MddModel.changeTo("incremental")
		MDD.resetModel
		domainResource.contents.clear
		MDD.invalidateCache
		
		parsed.querySpecifications.forEach [ IQuerySpecification<? extends ViatraQueryMatcher> specification |
			MDD.registerSpecificationIfNeeded(specification)
		]
		
		engine = 
			SuspendedQueryEngine.create(new EMFScope(resourceSet))
		MDD.initializePatterns(engine)
		engine.suspend
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

		val transformed = generator.transformPatternFile(cfg.vql)
		parsed = parseQueries(transformed)
		if(parsed.hasError){
			parsed.errors.forEach[println('''Parse error: «it»''')]
		}
		
		/**
		 * Incremental 
		 */
		MDD = MddModel.getInstanceOf("incremental")
		resourceSet = new ResourceSetImpl
		val traceRes = resourceSet.createResource(URI.createFileURI("trace-tmp-inc.xmi"))
		traceRes.contents.add(MDD.traceModel)
		
		domainResource = resourceSet.createResource(URI.createFileURI("tmp-domain-model.xmi"))
	}
	
	
	def gc(){
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
			println('''[WARMUP «i» of «cfg.warmups.size»]===============================================================''')
			initIncremental()
			preRun(i)
				
			/**
			 * Iteration 0
			 */
			
			gc()
			MddModel.changeTo("incremental")
			runIncremental(log)
			engine.dispose
			
			/**
			 * ProbLog is not affected by JVM warmup, thus it is safe to skip
			 * Some runs are included to test if it works
			 */
			if(i<2){
				gc()
				runProblog(log)
			}
			
			log.log("iteration", 0)
			log.log("run",i)
			log.log("prefix", cfg.prefix)
			log.log("size", cfg.size)
			log.commit
		}
	}
	def void measure(CSVLog log){
		for(seed : cfg.seeds){
			println('''[MEASURE «seed» of «cfg.seeds.size»]===============================================================''')
			initIncremental()
			preRun(seed)
			
			/**
			 * Iteration 0
			 */
			
			gc()
			MddModel.changeTo("incremental")
			runIncremental(log)
			engine.dispose
			
			gc()
			runProblog(log)
			
			log.log("iteration", 0)
			log.log("run",seed)
			log.log("prefix", cfg.prefix)
			log.log("size", cfg.size)
			log.commit
		}
	}
	
	def abstract void preRun(int seed)
	def abstract void runIncremental(CSVLog log)
	def abstract void runProblog(CSVLog log)
	
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
	
	def int countBasicEvents(PatternParsingResults parsed, ViatraQueryEngine engine){
		val cnt = newIntArrayOfSize(2)
		parsed.getQuerySpecification("BERequiredName1").ifPresent([specification |
			val matcher = engine.getMatcher(specification)
			cnt.set(0,matcher.countMatches)
		])
		parsed.getQuerySpecification("BERequiredName2").ifPresent([specification |
			val matcher = engine.getMatcher(specification)
			cnt.set(1,matcher.countMatches)
		])
		return cnt.get(0)+cnt.get(1)
	}
	def countStochasticPatterns(PatternParsingResults parsed, ViatraQueryEngine engine){
		val cnt = newIntArrayOfSize(1)
		parsed.getQuerySpecification("stochasticCount").ifPresent([specification |
			val matcher = engine.getMatcher(specification)
			matcher.oneArbitraryMatch.ifPresent([match |
				cnt.set(0, match.get(0) as Integer)
			])
		])
		return cnt.get(0)
	}
	def checkMatches(String name, PatternParsingResults parsed, ViatraQueryEngine engine){
		val cnt = newDoubleArrayOfSize(1)
		parsed.getQuerySpecification(name).ifPresent([specification |
			val matcher = engine.getMatcher(specification)	
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
	def printMatches(String name, PatternParsingResults parsed, ViatraQueryEngine engine){
		parsed.getQuerySpecification(name).ifPresent([specification |
			val matcher = engine.getMatcher(specification)	
			println("Specification found: "+specification.simpleName)
			matcher.forEachMatch([match |
				println("\t"+match.prettyPrint)
			])
		])
	}
	def initializePatterns(ViatraQueryEngine engine, String... queries){
		queries.forEach([name | 
			parsed.getQuerySpecification(name).ifPresent([IQuerySpecification<? extends ViatraQueryMatcher> specification |
				val cnt = engine.getMatcher(specification).countMatches
			])
		])
	}
}