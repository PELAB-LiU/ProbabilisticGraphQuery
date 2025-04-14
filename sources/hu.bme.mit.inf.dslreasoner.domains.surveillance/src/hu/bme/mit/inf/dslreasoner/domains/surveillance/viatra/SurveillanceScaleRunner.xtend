package hu.bme.mit.inf.dslreasoner.domains.surveillance.viatra

import hu.bme.mit.inf.measurement.utilities.CSVLog
import surveillance.SurveillancePackage
import reliability.intreface.Configuration
import reliability.intreface.ExecutionTime
import hu.bme.mit.inf.measurement.utilities.Config
import hu.bme.mit.inf.measurement.utilities.configuration.SurveillanceConfiguration
import hu.bme.mit.inf.measurement.utilities.viatra.ViatraScaleRunner
import se.liu.ida.sas.pelab.surveillance.storm.StormSurveillanceUtil
import hu.bme.mit.inf.dslreasoner.domains.surveillance.problog.ProblogSurveillanceUtil
import org.slf4j.LoggerFactory
import org.slf4j.Logger

class SurveillanceScaleRunner extends ViatraScaleRunner<SurveillanceConfiguration> implements ViatraSurveillanceUtil, StormSurveillanceUtil, ProblogSurveillanceUtil{
	static val Logger LOG4J = LoggerFactory.getLogger(SurveillanceScaleRunner);
	
	val modelgen = new SurveillanceModelGenerator
	var SurveillanceWrapper instance
	
	new(SurveillanceConfiguration cfg) {
		super(cfg, SurveillancePackage.eINSTANCE)
	}
	
	override preRun(int seed){
		instance = modelgen.make(cfg.size, seed)
		engine.model.contents.add(instance.model)
	}
	
	override initViatra(){
		super.initViatra
		initializePatterns(engine, "elimination")
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
				LOG4J.info("Viatra Timeout")
				Configuration.cancel
				engine.dispose
			])
			log.log("incremental.healthy", !engine.engine.tainted)
			
			val it0start = System.nanoTime
			engine.enable
			val it0sync = engine.mdd.unaryForAll(engine.engine)
			val coverage = getMatchesJSON(engine, instance.ordering)
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
			LOG4J.info("Viatra completed in {}ms", ((it0end-it0start)/1000.0/1000))
			return Configuration.isCancelled
		} catch(Exception e){
			LOG4J.warn("Exception logged: {}", e.message)
			LOG4J.debug("Exception logged: {}, exception: {}", e.message, e)
			return Configuration.isCancelled
		} finally{
			log.log("incremental.timeout", Configuration.isCancelled)
		}
	}
	
	override boolean runProblog(CSVLog log) {
		return runProblog(cfg, instance, log)
	}
	
	override boolean runStorm(CSVLog log) {
		return runStorm(cfg, instance, log)
	}
}



