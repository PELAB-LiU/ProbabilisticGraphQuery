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
import hu.bme.mit.inf.measurement.utilities.viatra.ViatraScaleRunner
import java.util.concurrent.atomic.AtomicBoolean

class SatelliteScaleRunners extends ViatraScaleRunner<SatelliteConfiguration> {
	val modelgen = new SatelliteModelGenerator
	var SatelliteModelWrapper instance
	
	new(SatelliteConfiguration cfg) {
		super(cfg, SatellitePackage.eINSTANCE)
	}
	
	override preRun(int seed){
		instance = modelgen.make(cfg.size, seed)
		domainResource.contents.add(instance.mission)
	}
	
	override initIncremental(){
		super.initIncremental
		initializePatterns(engine, "coverage")
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
			engine.enableAndPropagate
			val it0sync = MDD.unaryForAll(engine)
			val coverage = checkMatches("coverage", parsed, engine)
			val it0end = System.nanoTime
			timeout.cancel
			val it0prop = ExecutionTime.time

			/**
			 * Make logs
			 */
			log.log("incremental.total[ms]", ((it0end-it0start)/1000.0/1000))
			log.log("incremental.sync[ms]", it0sync/1000.0/1000)
			log.log("incremental.prop[ms]", it0prop/1000.0/1000)
			log.log("incremental.healthy", !engine.tainted)
			log.log("incremental.result", coverage)
		} catch (CancellationException e) {
			println("Cancellation caught.")
		} finally {
			log.log("incremental.timeout", Configuration.isCancelled)
			println("Finally block executed.")
		}
	}
	
	override runProblog(CSVLog log) {
		val file = new File(cfg.probLogFile)
		file.createNewFile
		val writer = new FileWriter(file)
		val builder = new ProcessBuilder("problog", cfg.probLogFile);
		var Process process = null

		val start = System.nanoTime
		val plmodel = (new Generation).generateFrom(instance.mission).toString
		writer.write(plmodel)
		writer.flush
		writer.close
		val trafo = System.nanoTime
		process = builder.start

		val process2 = process
		val timeoutFlag = new AtomicBoolean
		val timeout = Config.timeout(cfg.timeoutS, [|
			println("Run cancelled with timeout.")
			timeoutFlag.set(true)
			process2.destroyForcibly
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
