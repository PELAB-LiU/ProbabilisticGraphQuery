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
import java.util.concurrent.atomic.AtomicBoolean
import java.lang.ProcessBuilder.Redirect
import hu.bme.mit.inf.dslreasoner.domains.surveillance.debug.DebugUtil
import org.eclipse.xtext.xbase.lib.Functions.Function0
import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.SurveillanceHelper
import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.SurveillanceCopier
import hu.bme.mit.inf.measurement.utilities.viatra.EngineConfig
import se.liu.ida.sas.pelab.surveillance.storm.StormSurveillanceGenerator
import se.liu.ida.sas.pelab.storm.run.StormEvaluation

class SurveillanceRunner extends ViatraBaseRunner<SurveillanceConfiguration>{
	val modelgen = new SurveillanceModelGenerator
	var SurveillanceWrapper instance
	
	new(SurveillanceConfiguration cfg) {
		/*super((new Function0<SurveillanceConfiguration>{
			
			override apply() {
				cfg.CSVcolumns.addAll(#["standalone.matches","incremental.matches","standalone.dst","standalone.prob","incremental.dst","incremental.prob"])
				return cfg
			}
			
			}).apply(), SurveillancePackage.eINSTANCE)*/
		super(cfg, SurveillancePackage.eINSTANCE)
	}
	
	override setupInitialModel(int seed){
		instance = modelgen.make(cfg.size, seed)
		incremental.model.contents.add(instance.model)
	}
	
	override applyIncrement() {
		modelgen.iterate(instance, 0.1)
		println("Incremental query engine status after increment (is tainted): "+incremental.engine.tainted)
	}
	
	override initBatch(){
		super.initBatch
		initializePatterns(batch, "elimination")
	}
	
	override runBatch(CSVLog log){
		SurveillanceHelper.logger = log

		Configuration.enable
		val resource = batch.model
			
		try {
			/**
			 * Setup instance model 
			 */
			
			//resource.contents.add(EcoreUtil.copy(instance.model, copy))
			val copier = new SurveillanceCopier
			val result = copier.copy(instance.model)
			copier.copyReferences
			resource.contents.add(result)
			val index = newHashMap
			copier.entrySet.forEach[index.put(it.value, instance.ordering.get(it.key))]
			ExecutionTime.reset;
			
			/**
			 * Run evaluation
			 */
			val timeout = Config.timeout(cfg.timeoutS, [|
				println("Run cancelled with timeout.")
				Configuration.cancel
				batch.dispose
				log.log("timeout", true)
			])
			val it0start = System.nanoTime
			batch.enable
			val it0sync = batch.mdd.unaryForAll(batch.engine)
			val coverage = getMatchesJSON(batch, index)
			val it0end = System.nanoTime
			timeout.cancel
			val it0prop = ExecutionTime.time
			
			/**
			 * Make logs
			 */
			//log.log("standalone.matches", (new DebugUtil{}).getMatchesJSON(batch, index))
			
			log.log("standalone.total[ms]", ((it0end-it0start)/1000.0/1000))
			log.log("standalone.sync[ms]", it0sync/1000.0/1000)
			log.log("standalone.prop[ms]", it0prop/1000.0/1000)
			log.log("standalone.healthy", !batch.engine.tainted)
			log.log("standalone.result", coverage)
		} catch (Exception e) {
			println("Cancellation caught.")
		} finally {
			resource.contents.clear
			log.log("standalone.timeout", Configuration.isCancelled)
			println("Finally block executed.")
		}
	}
	
	override initIncremental(){
		super.initIncremental
		initializePatterns(incremental, "elimination")
	}
	
	override runIncremental(CSVLog log) {
		Configuration.enable
		
		SurveillanceHelper.logger = log
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
				incremental.dispose
				log.log("timeout", true)
			])
			val it0start = System.nanoTime
			incremental.enable
			val it0sync = incremental.mdd.unaryForAll(incremental.engine)
			val coverage = getMatchesJSON(incremental, instance.ordering)
			val it0end = System.nanoTime
			timeout.cancel
			val it0prop = ExecutionTime.time
			
			/**
			 * Make logs
			 */
			//log.log("incremental.matches", (new DebugUtil{}).getMatchesJSON(incremental, instance.ordering))
			
			log.log("incremental.total[ms]", ((it0end-it0start)/1000.0/1000))
			log.log("incremental.sync[ms]", it0sync/1000.0/1000)
			log.log("incremental.prop[ms]", it0prop/1000.0/1000)
			log.log("incremental.healthy", !incremental.engine.tainted)
			log.log("incremental.result", coverage)
		} catch(Exception e){
			e.printStackTrace
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
		timeout.cancel()
		
		(new Scanner(process.inputStream)).forEach [ line |
			println("Debug problog.err: " + line)
		]
		log.log("problog.total[ms]", ((end - start) / 1000.0 / 1000))
		log.log("problog.trafo[ms]", (trafo - start) / 1000.0 / 1000)
		log.log("problog.evaluation[ms]", (end - trafo) / 1000.0 / 1000)
		log.log("problog.result", problogToJSON(output, timeoutFlag.get))
		log.log("problog.timeout", timeoutFlag.get)
	}
	
	override runStorm(CSVLog log) {
		val result = StormEvaluation.evalueate(cfg,
			[
				(new StormSurveillanceGenerator).generateFrom(instance.model)
			]
		)
		log.log("storm.total[ms]", result.transformation_ms + result.run_ms)
		log.log("storm.trafo[ms]", result.transformation_ms)
		log.log("storm.evaluation[ms]", result.run_ms)
		log.log("storm.result", stormToJSON(result.results, result.timeout))
		log.log("storm.timeout", result.timeout)
	}
	
	def String stormToJSON(Map<String,Double> data, boolean timeout){
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



