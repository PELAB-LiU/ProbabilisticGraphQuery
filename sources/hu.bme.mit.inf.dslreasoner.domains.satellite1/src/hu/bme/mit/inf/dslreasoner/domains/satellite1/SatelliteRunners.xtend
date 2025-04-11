package hu.bme.mit.inf.dslreasoner.domains.satellite1

import hu.bme.mit.inf.measurement.utilities.CSVLog
import hu.bme.mit.inf.measurement.utilities.Config
import hu.bme.mit.inf.measurement.utilities.configuration.SatelliteConfiguration
import hu.bme.mit.inf.measurement.utilities.viatra.ViatraBaseRunner
import reliability.intreface.CancellationException
import reliability.intreface.Configuration
import reliability.intreface.ExecutionTime
import satellite1.SatellitePackage

import org.eclipse.emf.ecore.util.EcoreUtil
import se.liu.ida.sas.pelab.satellite1.storm.StormSatelliteUtil
import problog.ProblogSatelliteUtil

//class SatelliteSinglePrism extends SatelliteCommon{
//	def run(Config cfg){
//		Nailgun.init(cfg)
//		Thread.sleep(2000)
//		val modelgen = new SatelliteModelGenerator
//		val instance = modelgen.make(cfg.asInt("size"), cfg.asInt("seed"))
//		SatelliteFitness.evaluate(instance,cfg)
//		Nailgun.terminate
//	}
//}
class SatelliteSingleGraph extends ViatraBaseRunner<SatelliteConfiguration> implements StormSatelliteUtil, ProblogSatelliteUtil{
	val modelgen = new SatelliteModelGenerator
	var SatelliteModelWrapper instance
	
	new(SatelliteConfiguration cfg) {
		super(cfg, SatellitePackage.eINSTANCE)
	}
	
	override initBatch(){
		super.initBatch
		initializePatterns(batch, "coverage")
	}
	override runBatch(CSVLog log){
		Configuration.enable
				
		try {
			/**
			 * Setup instance model 
			 */
			val resource = batch.model
			//resource.contents.addAll(EcoreUtil.copyAll(incrementalDomainResource.contents))
			resource.contents.add(EcoreUtil.copy(instance.mission))
			
			ExecutionTime.reset;
			
			/**
			 * Run evaluation
			 */
			val timeout = Config.timeout(cfg.timeoutS, [|
				println("Run cancelled with timeout.")
				Configuration.cancel
				log.log("timeout", true)
			])
			log.log("standalone.healthy", !batch.engine.tainted)
			
			val it0start = System.nanoTime
			batch.enable
			val it0sync = batch.mdd.unaryForAll(batch.engine)
			val coverage = checkMatches("coverage", batch)
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
		} catch (CancellationException e) {
			println("Cancellation caught.")
		} finally {
			log.log("standalone.timeout", Configuration.isCancelled)
			println("Finally block executed.")
		}
	}
	override setupInitialModel(int seed){
		instance = modelgen.make(cfg.size, seed)
		incremental.model.contents.add(instance.mission)
	}
	
	override initIncremental(){
		super.initIncremental
		initializePatterns(incremental, "coverage")
	}
	
	override runIncremental(CSVLog log) {
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
			log.log("incremental.healthy", !incremental.engine.tainted)
			
			val it0start = System.nanoTime
			incremental.enable
			val it0sync = incremental.mdd.unaryForAll(incremental.engine)
			val coverage = checkMatches("coverage", incremental)
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
		} catch (CancellationException e) {
			println("Cancellation caught.")
		} finally {
			log.log("incremental.timeout", Configuration.isCancelled)
			println("Finally block executed.")
		}
	}
	
	override applyIncrement() {
		//modelgen.addToModel(instance, 5)
		modelgen.makeRandomChange(instance, 1)
	}
	
	override runProblog(CSVLog log) {
		runProblog(cfg, instance, log)
	}
	
	override runStorm(CSVLog log) {
		runStorm(cfg, instance, log)
	}
}

