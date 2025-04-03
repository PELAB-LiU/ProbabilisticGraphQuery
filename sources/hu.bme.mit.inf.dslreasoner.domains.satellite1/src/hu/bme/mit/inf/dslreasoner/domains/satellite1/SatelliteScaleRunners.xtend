package hu.bme.mit.inf.dslreasoner.domains.satellite1

import hu.bme.mit.inf.measurement.utilities.CSVLog
import hu.bme.mit.inf.measurement.utilities.Config
import hu.bme.mit.inf.measurement.utilities.configuration.SatelliteConfiguration
import reliability.intreface.CancellationException
import reliability.intreface.Configuration
import reliability.intreface.ExecutionTime
import satellite1.SatellitePackage

import hu.bme.mit.inf.measurement.utilities.viatra.ViatraScaleRunner
import se.liu.ida.sas.pelab.satellite1.storm.StormSatelliteUtil
import problog.ProblogSatelliteUtil

class SatelliteScaleRunners extends ViatraScaleRunner<SatelliteConfiguration> implements StormSatelliteUtil, ProblogSatelliteUtil{
	val modelgen = new SatelliteModelGenerator
	var SatelliteModelWrapper instance
	
	new(SatelliteConfiguration cfg) {
		super(cfg, SatellitePackage.eINSTANCE)
	}
	
	override preRun(int seed){
		instance = modelgen.make(cfg.size, seed)
		engine.model.contents.add(instance.mission)
	}
	
	override initViatra(){
		super.initViatra
		initializePatterns(engine, "coverage")
	}
	
	override runViatra(CSVLog log) {
		Configuration.enable
		
		try {
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
				log.log("timeout", true)
			])
			val it0start = System.nanoTime
			engine.enable
			val it0sync = engine.mdd.unaryForAll(engine.engine)
			val coverage = checkMatches("coverage", engine)
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
		} catch (CancellationException e) {
			println("Cancellation caught.")
		} finally {
			log.log("incremental.timeout", Configuration.isCancelled)
			println("Finally block executed.")
		}
	}
	
	override runProblog(CSVLog log) {
		runProblog(cfg, instance, log)
	}
	
	override runStorm(CSVLog log) {
		runStorm(cfg, instance, log)
	}
}
