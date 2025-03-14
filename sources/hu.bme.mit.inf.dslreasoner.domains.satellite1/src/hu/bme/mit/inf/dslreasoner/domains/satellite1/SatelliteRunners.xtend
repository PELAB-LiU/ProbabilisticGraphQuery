package hu.bme.mit.inf.dslreasoner.domains.satellite1

import hu.bme.mit.inf.measurement.utilities.CSVLog
import hu.bme.mit.inf.measurement.utilities.Config
import hu.bme.mit.inf.measurement.utilities.configuration.SatelliteConfiguration
import hu.bme.mit.inf.measurement.utilities.viatra.ViatraBaseRunner
import hu.bme.mit.inf.querytransformation.query.StochasticPatternGenerator
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import org.eclipse.viatra.query.patternlanguage.emf.EMFPatternLanguageStandaloneSetup
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParserBuilder
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParsingResults
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngineOptions
import org.eclipse.viatra.query.runtime.localsearch.matcher.integration.LocalSearchEMFBackendFactory
import org.eclipse.viatra.query.runtime.rete.matcher.ReteBackendFactory
import org.eclipse.xtext.diagnostics.Severity
import reliability.intreface.CacheMode
import reliability.intreface.CancellationException
import reliability.intreface.Configuration
import reliability.intreface.ExecutionTime
import reliability.mdd.MddModel
import satellite1.SatellitePackage
import tracemodel.TraceModel

import org.eclipse.emf.ecore.util.EcoreUtil
import problog.Generation
import java.io.File
import java.io.FileWriter
import se.liu.ida.sas.pelab.problog.run.ProbLogEvaluator
import java.util.regex.Pattern
import java.util.Scanner
import java.util.HashMap
import java.util.concurrent.atomic.AtomicBoolean

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
class SatelliteSingleGraph extends ViatraBaseRunner<SatelliteConfiguration> {
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
		val file = new File(cfg.probLogFile)
		file.createNewFile
		val writer = new FileWriter(file)
		val builder = new ProcessBuilder("problog", cfg.probLogFile);
		//var Process process = null

		val start = System.nanoTime
		val plmodel = (new Generation).generateFrom(instance.mission).toString
		writer.write(plmodel)
		writer.flush
		writer.close
		val trafo = System.nanoTime
		val process = builder.start

		val timeoutFlag = new AtomicBoolean
		val timeout = Config.timeout(cfg.timeoutS, [|
			println("Run cancelled with timeout.")
			timeoutFlag.set(true)
			//process.destroyForcibly
			Runtime.runtime.exec(#["kill", "-9", process.pid.toString])
		])

		val output = new HashMap<String, Object>
		val io = new Scanner(process.inputStream)
		io.forEach [ line |
			println("Debug: " + line)
			val match = cfg.probLogPattern.matcher(line)
			if (match.find) {
				output.put(match.group(1), Double.parseDouble(match.group(2)))
			}
		]
		val end = System.nanoTime
		timeout.cancel
		
		log.log("problog.total[ms]", ((end - start) / 1000.0 / 1000))
		log.log("problog.trafo[ms]", (trafo - start) / 1000.0 / 1000)
		log.log("problog.evaluation[ms]", (end - trafo) / 1000.0 / 1000)
		log.log("problog.result", if(output.values.empty) 0 else output.values.get(0))
		log.log("problog.timeout", timeoutFlag.get)
	}
}

