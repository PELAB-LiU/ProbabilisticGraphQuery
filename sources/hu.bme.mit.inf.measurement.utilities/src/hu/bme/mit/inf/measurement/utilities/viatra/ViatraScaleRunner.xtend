package hu.bme.mit.inf.measurement.utilities.viatra

import hu.bme.mit.inf.measurement.utilities.CSVLog
import org.eclipse.emf.ecore.resource.Resource
import reliability.mdd.MddModel
import hu.bme.mit.inf.querytransformation.query.StochasticPatternGenerator
import org.eclipse.viatra.query.patternlanguage.emf.EMFPatternLanguageStandaloneSetup
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngineOptions
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import org.eclipse.viatra.query.runtime.localsearch.matcher.integration.LocalSearchEMFBackendFactory
import org.eclipse.viatra.query.runtime.rete.matcher.ReteBackendFactory
import hu.bme.mit.inf.measurement.utilities.configuration.BaseConfiguration
import org.eclipse.emf.ecore.EPackage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import hu.bme.mit.inf.measurement.utilities.configuration.EarlyCancelMonitor
import static hu.bme.mit.inf.measurement.utilities.configuration.CancellationThresholdMode.IF_BELOW

abstract class ViatraScaleRunner<Config extends BaseConfiguration> implements ViatraRunnerUtil { 
	static val Logger LOG4J = LoggerFactory.getLogger(ViatraScaleRunner);
	
	protected val Config cfg
	
	protected val StochasticPatternGenerator generator
	protected val String transformed
	
	protected var EngineConfig engine
	
	def void initViatra(){ 
		engine = new EngineConfig(transformed, "standalone", cfg.isFavourAbort)
		LOG4J.debug("Init VIATRA {}", engine.engine.hashCode)
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
			initViatra()
			preRun(i)
				
			/**
			 * Iteration 0
			 */
			
			gc()
			MddModel.changeTo("incremental")
			runViatra(log)
			engine.dispose
			
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
			
			log.log("iteration", 0)
			log.log("run",i)
			log.log("prefix", cfg.prefix)
			log.log("size", cfg.size)
			log.commit
		}
	}
	def void measure(CSVLog log){
		val viatraMonitor = new EarlyCancelMonitor(cfg.minimum, cfg.rate, IF_BELOW)
		val problogMonitor = new EarlyCancelMonitor(cfg.minimum, cfg.rate, IF_BELOW)
		val stormMonitor = new EarlyCancelMonitor(cfg.minimum, cfg.rate, IF_BELOW)
		
		for(seed : cfg.seeds){
			LOG4J.info("[MEASURE {} ({} of {}) ]===============================================================", seed, cfg.seeds.indexOf(seed)+1, cfg.seeds.size)
			initViatra()
			preRun(seed)
			
			/**
			 * Iteration 0
			 */
			
			gc()
			MddModel.changeTo("incremental")
			viatraMonitor.conditionalRun[|runViatra(log)]
			engine.dispose
			
			gc()
			problogMonitor.conditionalRun[|runProblog(log)]
			
			gc()
			stormMonitor.conditionalRun[|runStorm(log)] 
			
			log.log("iteration", 0)
			log.log("run",seed)
			log.log("prefix", cfg.prefix)
			log.log("size", cfg.size)
			log.commit
		}
	}
	
	def abstract boolean preRun(int seed)
	def abstract boolean runViatra(CSVLog log)
	def abstract boolean runProblog(CSVLog log)
	def abstract boolean runStorm(CSVLog log)
	
}