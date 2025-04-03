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
import hu.bme.mit.inf.measurement.utilities.viatra.EngineConfig
import se.liu.ida.sas.pelab.storm.run.StormEvaluation
import se.liu.ida.sas.pelab.surveillance.storm.StormSurveillanceGenerator
import se.liu.ida.sas.pelab.surveillance.storm.StormSurveillanceUtil
import hu.bme.mit.inf.dslreasoner.domains.surveillance.problog.ProblogSurveillanceUtil

class SurveillanceScaleRunner extends ViatraScaleRunner<SurveillanceConfiguration> implements StormSurveillanceUtil, ProblogSurveillanceUtil{
	val modelgen = new SurveillanceModelGenerator
	var SurveillanceWrapper instance
	
	new(SurveillanceConfiguration cfg) {
		super(cfg, SurveillancePackage.eINSTANCE)
	}
	
	override preRun(int seed){
		instance = modelgen.make(cfg.size, seed)
		engine.model.contents.add(instance.model)
	}
	
	override initIncremental(){
		super.initIncremental
		initializePatterns(engine, "elimination")
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
			log.log("incremental.healthy", !engine.engine.tainted)
			log.log("incremental.result", coverage)
		} catch(Exception e){
			println("Cancellation caught.")
		} finally{
			log.log("incremental.timeout", Configuration.isCancelled)
			println("Finally block executed.")
		}
	}
	
	override runProblog(CSVLog log) {
		runProblog(cfg, instance, log)
		/*val file = new File(cfg.probLogFile)
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
		log.log("problog.timeout", timeoutFlag.get)*/
	}
	
	override runStorm(CSVLog log) {
		runStorm(cfg, instance, log)
		/*val result = StormEvaluation.evalueate(cfg,
			[
				(new StormSurveillanceGenerator).generateFrom(instance.model)
			]
		)
		log.log("storm.total[ms]", result.transformation_ms + result.run_ms)
		log.log("storm.trafo[ms]", result.transformation_ms)
		log.log("storm.evaluation[ms]", result.run_ms)
		log.log("storm.result", stormToJSON(result.results, result.timeout))
		log.log("storm.timeout", result.timeout)*/
	}
	
	/*def String stormToJSON(Map<String,Double> data, boolean timeout){
		val results = data.entrySet.map[ entry |
			val key = entry.key
				.replace("toplevel \"elimination_UnidentifiedObjectImpl", "")
				.replace("\";", "")
			Integer.parseInt(key) -> entry.value
		].toMap([e|e.key],[e|e.value])
		return '''
		{
			"valid" : «!timeout»,
			"matches" : [
				«FOR entry : results.entrySet SEPARATOR ","»
					{
						"object" : "object«instance.ordering.get(instance.ofHashCode(entry.key))»",
						"probability" : «entry.value»
					}
				«ENDFOR»
			]
		}
		'''
	}*/
	
	/*def String problogToJSON(Map<String,Object> data, boolean timeout){
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
	}*/
	
	def String getMatchesJSON(EngineConfig engine, Map<EObject,Integer> index){
		val opt = engine.parsed.getQuerySpecification("elimination")
		if(opt.isPresent){
			val matcher = opt.get
			'''
			{
				"valid" : true,
				"matches" : [
				«FOR match : engine.engine.getMatcher(matcher).allMatches SEPARATOR ","»
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



