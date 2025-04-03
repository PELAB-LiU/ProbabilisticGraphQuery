package hu.bme.mit.inf.dslreasoner.domains.smarthome.viatra

import hu.bme.mit.inf.measurement.utilities.Config
import hu.bme.mit.inf.measurement.utilities.CSVLog
import smarthome.SmarthomePackage
import reliability.intreface.Configuration
import hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities.SmarthomeModelGenerator
import reliability.intreface.ExecutionTime
import hu.bme.mit.inf.measurement.utilities.configuration.SmarthomeConfiguration
import hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities.SmarthomeModel
import hu.bme.mit.inf.measurement.utilities.viatra.ViatraScaleRunner
import se.liu.ida.sas.pelab.smarthome.storm.StormSmarthomeUtil
import hu.bme.mit.inf.dslreasoner.domains.smarthome.problog.ProblogSmarthomeUtil

class SmarthomeScaleRunner extends ViatraScaleRunner<SmarthomeConfiguration> implements ViatraSmarthomeUtil, StormSmarthomeUtil, ProblogSmarthomeUtil{
	val modelgen = new SmarthomeModelGenerator
	var SmarthomeModel instance
	
	new(SmarthomeConfiguration cfg) {
		super(cfg, SmarthomePackage.eINSTANCE)
	}
	
	override preRun(int seed){
		instance = modelgen.make(cfg.homes, cfg.persons, cfg.size)
		engine.model.contents.add(instance.model)
	}
	
	
	override initViatra(){
		super.initViatra
		initializePatterns(engine, "callProbability")
	}
	
	override runViatra(CSVLog log) {
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
				engine.dispose
				log.log("timeout", true)
			])
			val it0start = System.nanoTime
			engine.enable
			val it0sync = engine.mdd.unaryForAll(engine.engine)
			val coverage = getMatchesJSON(engine, instance.idmap)
			val it0end = System.nanoTime
			timeout.cancel
			val it0prop = ExecutionTime.time
			
			/**
			 * Make logs
			 */
			log.log("incremental.total[ms]", ((it0end-it0start)/1000.0/1000))
			log.log("incremental.sync[ms]", it0sync/1000.0/1000)
			log.log("incremental.prop[ms]", it0prop/1000.0/1000)
			log.log("incremental.healthy", !engine.engine.tainted)
			log.log("incremental.result", coverage)
		} catch(Exception e){
			println("Cancellation caught.")
		} finally{
			log.log("incremental.timeout", Configuration.isCancelled)
			println("Finally block executed.")
		}
	}
	
	override runProblog(CSVLog log){
		runProblog(cfg, instance, log)
	}
	
	override runStorm(CSVLog log) {
		runStorm(cfg, instance, log)
	}
}