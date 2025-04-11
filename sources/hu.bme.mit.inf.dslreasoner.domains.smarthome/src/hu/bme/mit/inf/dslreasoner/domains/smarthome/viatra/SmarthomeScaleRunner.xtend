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
	
	override boolean runViatra(CSVLog log) {
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
			])
			log.log("incremental.healthy", !engine.engine.tainted)
			
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
			log.log("incremental.result", coverage)
			return Configuration.isCancelled
		} catch(Exception e){
			println("Cancellation caught.")
			return Configuration.isCancelled
		} finally{
			log.log("incremental.timeout", Configuration.isCancelled)
			println("Finally block executed.")
		}
	}
	
	override boolean runProblog(CSVLog log){
		return runProblog(cfg, instance, log)
	}
	
	override boolean runStorm(CSVLog log) {
		return runStorm(cfg, instance, log)
	}
}