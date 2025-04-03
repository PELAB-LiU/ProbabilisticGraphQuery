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

class SmarthomeRunner extends ViatraBaseRunner<SmarthomeConfiguration> implements ViatraSmarthomeUtil, StormSmarthomeUtil, ProblogSmarthomeUtil{
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
				println("Mapping "+it.value+" to "+instance.idmap.get(it.key))
			]
			ExecutionTime.reset;
			
			/**
			 * Run evaluation
			 */
			val timeout = Config.timeout(cfg.timeoutS, [|
				println("Run cancelled with timeout.")
				Configuration.cancel
				batch.dispose
				log.log("timeout", true)
			])
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
			log.log("standalone.healthy", !batch.engine.tainted)
			log.log("standalone.result", coverage)
		} catch (Exception e){
			println("Cancellation caught.")
		} finally{
			log.log("standalone.timeout", Configuration.isCancelled)
			println("Finally block executed.")
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
				println("Run cancelled with timeout.")
				Configuration.cancel
				incremental.dispose
				log.log("timeout", true)
			])
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
			log.log("incremental.healthy", !incremental.engine.tainted)
			log.log("incremental.result", coverage)
		} catch(Exception e){
			println("Cancellation caught.")
		} finally{
			log.log("incremental.timeout", Configuration.isCancelled)
			println("Finally block executed.")
		}
	}
	
	override applyIncrement(){
		modelgen.iterate(instance, 1)//TODO
	}
	override runProblog(CSVLog log) {
		runProblog(cfg, instance, log)
	}
	override runStorm(CSVLog log) {
		runStorm(cfg, instance, log)
	}	
}