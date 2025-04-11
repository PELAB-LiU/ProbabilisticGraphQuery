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
import org.slf4j.LoggerFactory
import org.slf4j.Logger

class SatelliteScaleRunners extends ViatraScaleRunner<SatelliteConfiguration> implements StormSatelliteUtil, ProblogSatelliteUtil{
	static val Logger LOG4J = LoggerFactory.getLogger(SatelliteScaleRunners);
	
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
	
	override boolean runViatra(CSVLog log) {
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
				Configuration.cancel
				LOG4J.info("Viatra Timeout")
			])
			log.log("incremental.healthy", !engine.engine.tainted)
			
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
			log.log("incremental.result", coverage)
			LOG4J.info("Viatra completed in {}ms with result {}", ((it0end-it0start)/1000.0/1000), coverage)
			return Configuration.isCancelled
		} catch (CancellationException e) {
			println("Cancellation caught.")
			return Configuration.isCancelled
		} finally {
			log.log("incremental.timeout", Configuration.isCancelled)
			println("Finally block executed.")
		}
	}
	
	override boolean runProblog(CSVLog log) {
		return runProblog(cfg, instance, log)
	}
	
	override boolean runStorm(CSVLog log) {
		return runStorm(cfg, instance, log)
	}
}
