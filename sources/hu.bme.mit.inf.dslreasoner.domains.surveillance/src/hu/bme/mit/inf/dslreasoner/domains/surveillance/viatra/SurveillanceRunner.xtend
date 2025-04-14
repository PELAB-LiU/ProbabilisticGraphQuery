package hu.bme.mit.inf.dslreasoner.domains.surveillance.viatra

import hu.bme.mit.inf.measurement.utilities.viatra.ViatraBaseRunner
import hu.bme.mit.inf.measurement.utilities.CSVLog
import surveillance.SurveillancePackage
import reliability.intreface.Configuration
import reliability.intreface.ExecutionTime
import hu.bme.mit.inf.measurement.utilities.Config
import hu.bme.mit.inf.measurement.utilities.configuration.SurveillanceConfiguration
import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.SurveillanceHelper
import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.SurveillanceCopier
import se.liu.ida.sas.pelab.surveillance.storm.StormSurveillanceUtil
import hu.bme.mit.inf.dslreasoner.domains.surveillance.problog.ProblogSurveillanceUtil
import org.slf4j.LoggerFactory
import org.slf4j.Logger

class SurveillanceRunner extends ViatraBaseRunner<SurveillanceConfiguration> implements ViatraSurveillanceUtil, StormSurveillanceUtil, ProblogSurveillanceUtil{
	static val Logger LOG4J = LoggerFactory.getLogger(SurveillanceRunner);
	
	val modelgen = new SurveillanceModelGenerator
	var SurveillanceWrapper instance
	
	new(SurveillanceConfiguration cfg) {
		/*super((new Function0<SurveillanceConfiguration>{
			
			override apply() {
				cfg.CSVcolumns.addAll(#["standalone.matches","incremental.matches","standalone.dst","standalone.prob","incremental.dst","incremental.prob"])
				return cfg
			}
			
			}).apply(), SurveillancePackage.eINSTANCE)*/
		super(cfg, SurveillancePackage.eINSTANCE)
	}
	
	override setupInitialModel(int seed){
		instance = modelgen.make(cfg.size, seed)
		incremental.model.contents.add(instance.model)
	}
	
	override applyIncrement() {
		modelgen.iterate(instance, 0.1)
	}
	
	override initBatch(){
		super.initBatch
		initializePatterns(batch, "elimination")
	}
	
	override runBatch(CSVLog log){
		SurveillanceHelper.logger = log

		Configuration.enable
		val resource = batch.model
			
		try {
			/**
			 * Setup instance model 
			 */
			
			//resource.contents.add(EcoreUtil.copy(instance.model, copy))
			val copier = new SurveillanceCopier
			val result = copier.copy(instance.model)
			copier.copyReferences
			resource.contents.add(result)
			val index = newHashMap
			copier.entrySet.forEach[index.put(it.value, instance.ordering.get(it.key))]
			ExecutionTime.reset;
			
			/**
			 * Run evaluation
			 */
			val timeout = Config.timeout(cfg.timeoutS, [|
				LOG4J.info("Batch Timeout")
				Configuration.cancel
				batch.dispose
			])
			log.log("standalone.healthy", !batch.engine.tainted)
			
			val it0start = System.nanoTime
			batch.enable
			val it0sync = batch.mdd.unaryForAll(batch.engine)
			val coverage = getMatchesJSON(batch, index)
			val it0end = System.nanoTime
			timeout.cancel
			val it0prop = ExecutionTime.time
			
			/**
			 * Make logs
			 */
			//log.log("standalone.matches", (new DebugUtil{}).getMatchesJSON(batch, index))
			
			log.log("standalone.total[ms]", ((it0end-it0start)/1000.0/1000))
			log.log("standalone.sync[ms]", it0sync/1000.0/1000)
			log.log("standalone.prop[ms]", it0prop/1000.0/1000)
			log.log("standalone.result", coverage)
			LOG4J.info("Batch completed in {}ms", ((it0end-it0start)/1000.0/1000))
		} catch (Exception e) {
			LOG4J.warn("Exception logged: {}", e.message)
			LOG4J.debug("Exception logged: {}, exception: {}", e.message, e)
		} finally {
			resource.contents.clear
			log.log("standalone.timeout", Configuration.isCancelled)
		}
	}
	
	override initIncremental(){
		super.initIncremental
		initializePatterns(incremental, "elimination")
	}
	
	override runIncremental(CSVLog log) {
		Configuration.enable
		
		SurveillanceHelper.logger = log
		try{
			/**
			 * Setup instance model 
			 */
			ExecutionTime.reset;
			
			/**
			 * Run first evaluation
			 */
			val timeout = Config.timeout(cfg.timeoutS, [|
				LOG4J.info("Incremental Timeout")
				Configuration.cancel
				incremental.dispose
			])
			log.log("incremental.healthy", !incremental.engine.tainted)
			
			val it0start = System.nanoTime
			incremental.enable
			val it0sync = incremental.mdd.unaryForAll(incremental.engine)
			val coverage = getMatchesJSON(incremental, instance.ordering)
			val it0end = System.nanoTime
			timeout.cancel
			val it0prop = ExecutionTime.time
			
			/**
			 * Make logs
			 */
			log.log("incremental.total[ms]", ((it0end-it0start)/1000.0/1000))
			log.log("incremental.sync[ms]", it0sync/1000.0/1000)
			log.log("incremental.prop[ms]", it0prop/1000.0/1000)
			log.log("incremental.result", coverage)
			LOG4J.info("Incremental completed in {}ms", ((it0end-it0start)/1000.0/1000))
		} catch(Exception e){
			LOG4J.warn("Exception logged: {}", e.message)
			LOG4J.debug("Exception logged: {}, exception: {}", e.message, e)
		} finally{
			log.log("incremental.timeout", Configuration.isCancelled)
		}
	}
	
	override runProblog(CSVLog log) {
		runProblog(cfg, instance, log)
	}
	
	override runStorm(CSVLog log) {
		runStorm(cfg, instance, log)
	}
	
}



