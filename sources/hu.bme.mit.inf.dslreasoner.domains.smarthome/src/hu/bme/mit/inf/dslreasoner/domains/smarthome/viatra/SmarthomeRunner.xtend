package hu.bme.mit.inf.dslreasoner.domains.smarthome.viatra

import hu.bme.mit.inf.measurement.utilities.viatra.ViatraBaseRunner
import hu.bme.mit.inf.measurement.utilities.Config
import hu.bme.mit.inf.measurement.utilities.CSVLog
import smarthome.SmarthomePackage
import reliability.intreface.Configuration
import hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities.SmarthomeModelGenerator
import reliability.intreface.ExecutionTime
import hu.bme.mit.inf.measurement.utilities.configuration.SmarthomeConfiguration
import hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities.SmarthomeModel
import org.eclipse.emf.ecore.util.EcoreUtil.Copier
import se.liu.ida.sas.pelab.smarthome.storm.StormSmarthomeUtil
import hu.bme.mit.inf.dslreasoner.domains.smarthome.problog.ProblogSmarthomeUtil
import org.slf4j.LoggerFactory
import org.slf4j.Logger

class SmarthomeRunner extends ViatraBaseRunner<SmarthomeConfiguration> implements ViatraSmarthomeUtil, StormSmarthomeUtil, ProblogSmarthomeUtil{
	static val Logger LOG4J = LoggerFactory.getLogger(SmarthomeRunner);
	
	val modelgen = new SmarthomeModelGenerator
	var SmarthomeModel instance
	
	new(SmarthomeConfiguration cfg) {
		super(cfg, SmarthomePackage.eINSTANCE)
	}
	
	override initBatch(){
		super.initBatch		
		initializePatterns(batch, "callProbability")
	}
	override runBatch(CSVLog log){
		Configuration.enable
		
		try{
			/**
			 * Setup instance model 
			 */
			val resource = batch.model
			//resource.contents.add(EcoreUtil.copy(instance.model))
			val copier = new Copier
			val result = copier.copy(instance.model)
			copier.copyReferences
			resource.contents.add(result)
			val index = newHashMap
			copier.entrySet.forEach[index.put(it.value, instance.idmap.get(it.key))
				LOG4J.debug("Map object {} to name {}", it.value, instance.idmap.get(it.key))
			]
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
			
			log.log("standalone.total[ms]", ((it0end-it0start)/1000.0/1000))
			log.log("standalone.sync[ms]", it0sync/1000.0/1000)
			log.log("standalone.prop[ms]", it0prop/1000.0/1000)
			log.log("standalone.result", coverage)
			LOG4J.info("Batch completed in {}ms", ((it0end-it0start)/1000.0/1000))
		} catch (Exception e){
			LOG4J.warn("Exception logged: {}", e.message)
			LOG4J.debug("Exception logged: {}, exception: {}", e.message, e)
		} finally{
			log.log("standalone.timeout", Configuration.isCancelled)
		}
	}
	
	override setupInitialModel(int seed){
		instance = modelgen.make(cfg.homes, cfg.persons, cfg.size)
		incremental.model.contents.add(instance.model)
	}
	
	
	override initIncremental(){
		super.initIncremental
		initializePatterns(incremental, "callProbability")
	}
	
	override runIncremental(CSVLog log) {
		Configuration.enable
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
			val coverage = getMatchesJSON(incremental, instance.idmap)
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
	
	override applyIncrement(){
		modelgen.iterate(instance, cfg.getChangeSize())
	}
	override runProblog(CSVLog log) {
		runProblog(cfg, instance, log)
	}
	override runStorm(CSVLog log) {
		runStorm(cfg, instance, log)
	}	
}