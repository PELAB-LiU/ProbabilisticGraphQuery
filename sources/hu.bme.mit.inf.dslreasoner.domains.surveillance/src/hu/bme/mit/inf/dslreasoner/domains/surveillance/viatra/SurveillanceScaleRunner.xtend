package hu.bme.mit.inf.dslreasoner.domains.surveillance.viatra

import hu.bme.mit.inf.measurement.utilities.configuration.BaseConfiguration
import hu.bme.mit.inf.measurement.utilities.viatra.ViatraBaseRunner
import hu.bme.mit.inf.measurement.utilities.CSVLog
import org.eclipse.emf.ecore.EPackage
import surveillance.SurveillancePackage
import reliability.intreface.Configuration
import reliability.intreface.ExecutionTime
import hu.bme.mit.inf.measurement.utilities.Config
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParsingResults
import org.eclipse.viatra.query.runtime.api.AdvancedViatraQueryEngine
import reliability.intreface.CancellationException
import java.util.Random
import hu.bme.mit.inf.measurement.utilities.configuration.SurveillanceConfiguration
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.emf.common.util.URI
import java.io.File
import java.io.FileWriter
import hu.bme.mit.inf.dslreasoner.domains.surveillance.problog.ProblogSurveillanceGenerator
import java.util.HashMap
import java.util.Scanner
import hu.bme.mit.inf.measurement.utilities.viatra.SuspendedQueryEngine
import java.util.Map
import org.eclipse.emf.ecore.util.EcoreUtil.Copier
import org.eclipse.emf.ecore.EObject
import hu.bme.mit.inf.measurement.utilities.viatra.ViatraScaleRunner
import java.util.concurrent.atomic.AtomicBoolean
import java.lang.reflect.InvocationTargetException

class SurveillanceScaleRunner extends ViatraScaleRunner<SurveillanceConfiguration>{
	val modelgen = new SurveillanceModelGenerator
	var SurveillanceWrapper instance
	
	new(SurveillanceConfiguration cfg) {
		super(cfg, SurveillancePackage.eINSTANCE)
	}
	
	override preRun(int seed){
		instance = modelgen.make(cfg.size, seed)
		domainResource.contents.add(instance.model)
	}
	
	override initIncremental(){
		super.initIncremental
		initializePatterns(engine, "elimination")
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
				if(!engine.isDisposed){
					engine.dispose
				}
				log.log("timeout", true)
			])
			val it0start = System.nanoTime
			engine.enableAndPropagate
			val it0sync = MDD.unaryForAll(engine)
			val coverage = getMatchesJSON(parsed, engine, instance.ordering)
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
		} catch(Exception e){
			println("Cancellation caught.")
		} finally{
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
		val plmodel = (new ProblogSurveillanceGenerator).generateFrom(instance.model).toString
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
			process2.destroy
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
		log.log("problog.result", problogToJSON(output, timeoutFlag.get))
		log.log("problog.timeout", timeoutFlag.get)
	}
	def String problogToJSON(Map<String,Object> data, boolean timeout){
		return '''
		{
			"valid" : «!timeout»,
			"matches" : [
				«FOR entry : data.entrySet SEPARATOR ","»
					{
						"object" : "object«instance.ordering.get(instance.ofHashCode(Integer.parseInt(entry.key)))»",
						"probability" : «entry.value»
					}
				«ENDFOR»
			]
		}
		'''
	}
	
	def String getMatchesJSON(PatternParsingResults parsed, SuspendedQueryEngine engine, Map<EObject,Integer> index){
		val opt = parsed.getQuerySpecification("elimination")
		if(opt.isPresent){
			val matcher = opt.get
			'''
			{
				"valid" : true,
				"matches" : [
				«FOR match : engine.getMatcher(matcher).allMatches SEPARATOR ","»
					{
						"object" : "object«index.get(match.get(0))»",
						"probability" : «match.get(1)»
					}
				«ENDFOR»
				]
			}
			'''
		} else {
			return '''{"valid" : false, "matches" : []}'''
		}
	}
}



