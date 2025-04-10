package hu.bme.mit.inf.measurement.utilities.viatra

import hu.bme.mit.inf.measurement.utilities.CSVLog
import hu.bme.mit.inf.measurement.utilities.configuration.BaseConfiguration
import org.eclipse.emf.ecore.EPackage
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class ScalingMeasurementRunner<Config extends BaseConfiguration> extends BaseRunner<Config> implements ViatraRunnerUtil { 
	static val Logger LOG4J = LoggerFactory.getLogger(ScalingMeasurementRunner);
	
	protected var EngineConfig engine
	
	def void initViatra(){ 
		engine = new EngineConfig(transformed, "standalone")
		LOG4J.debug("Init VIATRA {}", engine.engine.hashCode)
	}
	new(Config cfg, EPackage domain){
		super(cfg, domain)
	}
	
	override warmup(CSVLog log){
		for(i : cfg.warmups){
			LOG4J.info("[WARMUP {} ({} of {}) ]===============================================================", i, cfg.warmups.indexOf(i)+1, cfg.warmups.size)
			initViatra()
			setupInitialModel(i)
				
			/**
			 * Iteration 0
			 */
			
			gc()
			engine.acquire
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
	override measure(CSVLog log){
		for(seed : cfg.seeds){
			LOG4J.info("[MEASURE {} ({} of {}) ]===============================================================", seed, cfg.seeds.indexOf(seed)+1, cfg.seeds.size)
			initViatra()
			setupInitialModel(seed)
			
			/**
			 * Iteration 0
			 */
			
			gc()
			engine.acquire
			runViatra(log)
			engine.dispose
			
			gc()
			runProblog(log)
			
			gc()
			runStorm(log)
			
			log.log("iteration", 0)
			log.log("run",seed)
			log.log("prefix", cfg.prefix)
			log.log("size", cfg.size)
			log.commit
		}
	}
	
	def abstract void setupInitialModel(int seed)
	def abstract void runViatra(CSVLog log)
	def abstract void runProblog(CSVLog log)
	def abstract void runStorm(CSVLog log)
	
}