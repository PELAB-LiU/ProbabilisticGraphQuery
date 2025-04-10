package hu.bme.mit.inf.measurement.utilities.viatra

import hu.bme.mit.inf.measurement.utilities.CSVLog
import hu.bme.mit.inf.measurement.utilities.configuration.BaseConfiguration
import org.eclipse.emf.ecore.EPackage
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class IncrementalMeasurementRunner<Config extends BaseConfiguration> extends BaseRunner<Config> implements ViatraRunnerUtil{ 
	static val Logger LOG4J = LoggerFactory.getLogger(IncrementalMeasurementRunner);
	
	protected var EngineConfig batch
	protected var EngineConfig incremental
	
	def void initBatch(){
		batch = new EngineConfig(transformed, "standalone")
		LOG4J.debug("Init Batch {}", batch.engine.hashCode)
	}
	def void initIncremental(){
		incremental = new EngineConfig(transformed, "incremental")
		LOG4J.debug("Init Incremetnal {}", incremental.engine.hashCode)
	}
	
	new(Config cfg, EPackage domain){
		super(cfg, domain)
	}
	
	override warmup(CSVLog log){
		for(i : cfg.warmups){
			LOG4J.info("[WARMUP {} ({} of {}) ]===============================================================", i, cfg.warmups.indexOf(i)+1, cfg.warmups.size)
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
	override measure(CSVLog log){
		for(seed : cfg.seeds){
			LOG4J.info("[MEASURE {} ({} of {}) ]===============================================================", seed, cfg.seeds.indexOf(seed)+1, cfg.seeds.size)
			initIncremental()
			setupInitialModel(seed)
				
			/**
			 * Incremental iterations
			 */
			for(iter : 0..cfg.iterations){
				LOG4J.info("[ITERATION {} of {} MEASURE {} ({} of {}) ]===============================================================", iter, cfg.iterations, seed, cfg.seeds.indexOf(seed)+1, cfg.seeds.size)
				gc()
				incremental.acquire
				runIncremental(log)
				incremental.suspend
				
				gc()
				initBatch
				batch.acquire
				runBatch(log)
				batch.dispose
				
				
				
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
	
}