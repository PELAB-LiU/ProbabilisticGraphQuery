package hu.bme.mit.inf.dslreasoner.domains.smarthome.viatra

import hu.bme.mit.inf.measurement.utilities.viatra.ViatraBaseRunner
import hu.bme.mit.inf.measurement.utilities.Config
import hu.bme.mit.inf.measurement.utilities.CSVLog
import smarthome.SmarthomePackage
import reliability.intreface.CancellationException
import reliability.intreface.Configuration
import hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities.SmarthomeModelGenerator
import reliability.intreface.ExecutionTime
import hu.bme.mit.inf.measurement.utilities.configuration.SmarthomeConfiguration
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParsingResults
import org.eclipse.viatra.query.runtime.api.AdvancedViatraQueryEngine
import smarthome.Home
import hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities.SmarthomeModel
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.util.EcoreUtil
import java.io.File
import java.io.FileWriter
import hu.bme.mit.inf.dslreasoner.domains.smarthome.problog.SmarthomeProblogGenerator
import java.util.HashMap
import java.util.Scanner
import java.util.Map
import org.eclipse.emf.ecore.util.EcoreUtil.Copier
import org.eclipse.emf.ecore.EObject
import java.util.concurrent.atomic.AtomicBoolean
import hu.bme.mit.inf.measurement.utilities.viatra.EngineConfig

class SmarthomeRunner extends ViatraBaseRunner<SmarthomeConfiguration> {
	val modelgen = new SmarthomeModelGenerator
	var SmarthomeModel instance
	
	new(SmarthomeConfiguration cfg) {
		super(cfg, SmarthomePackage.eINSTANCE)
	}
	
	override initBatch(){
		super.initBatch		
		initializePatterns(batch, "callProbability")
	}
	override runBatch(CSVLog log){
		Configuration.enable
		
		try{
			/**
			 * Setup instance model 
			 */
			val resource = batch.model
			//resource.contents.add(EcoreUtil.copy(instance.model))
			val copier = new Copier
			val result = copier.copy(instance.model)
			copier.copyReferences
			resource.contents.add(result)
			val index = newHashMap
			copier.entrySet.forEach[index.put(it.value, instance.idmap.get(it.key))
				println("Mapping "+it.value+" to "+instance.idmap.get(it.key))
			]
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
			log.log("standalone.total[ms]", ((it0end-it0start)/1000.0/1000))
			log.log("standalone.sync[ms]", it0sync/1000.0/1000)
			log.log("standalone.prop[ms]", it0prop/1000.0/1000)
			log.log("standalone.healthy", !batch.engine.tainted)
			log.log("standalone.result", coverage)
		} catch (Exception e){
			println("Cancellation caught.")
		} finally{
			log.log("standalone.timeout", Configuration.isCancelled)
			println("Finally block executed.")
		}
	}
	
	override setupInitialModel(int seed){
		instance = modelgen.make(cfg.homes, cfg.persons, cfg.size)
		incremental.model.contents.add(instance.model)
	}
	
	
	override initIncremental(){
		super.initIncremental
		initializePatterns(incremental, "callProbability")
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
				incremental.dispose
				log.log("timeout", true)
			])
			val it0start = System.nanoTime
			incremental.enable
			val it0sync = incremental.mdd.unaryForAll(incremental.engine)
			val coverage = getMatchesJSON(incremental, instance.idmap)
			val it0end = System.nanoTime
			timeout.cancel
			val it0prop = ExecutionTime.time
			
			/**
			 * Make logs
			 */
			log.log("incremental.total[ms]", ((it0end-it0start)/1000.0/1000))
			log.log("incremental.sync[ms]", it0sync/1000.0/1000)
			log.log("incremental.prop[ms]", it0prop/1000.0/1000)
			log.log("incremental.healthy", !incremental.engine.tainted)
			log.log("incremental.result", coverage)
		} catch(Exception e){
			println("Cancellation caught.")
		} finally{
			log.log("incremental.timeout", Configuration.isCancelled)
			println("Finally block executed.")
		}
	}
	
	override applyIncrement(){
		modelgen.iterate(instance, 1)//TODO
	}
	override runProblog(CSVLog log) {
		val file = new File(cfg.probLogFile)
		file.createNewFile
		val writer = new FileWriter(file)
		val builder = new ProcessBuilder("problog", cfg.probLogFile);
		var Process process = null

		val start = System.nanoTime
		val plmodel = (new SmarthomeProblogGenerator).generateFrom(instance.model).toString
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
						"measurement" : "«instance.idmap.get(instance.ofHashCode(Integer.parseInt(entry.key)))»",
						"probability" : «entry.value»
					}
				«ENDFOR»
			]
		}
		'''
	}
	def String getMatchesJSON(EngineConfig engine, Map<EObject,String> index){
		val opt = engine.parsed.getQuerySpecification("callProbability")
		if(opt.isPresent){
			val matcher = opt.get
			'''
			{
				"valid" : true,
				"matches" : [
				«FOR match : engine.engine.getMatcher(matcher).allMatches SEPARATOR ","»
					{
						"measurement" : "«index.get(match.get(1))»",
						"probability" : «match.get(2)»
					}
				«ENDFOR»
				]
			}
			'''
		} else {
			return '''{"valid" : false, "matches" : []}'''
		}
	}
	
	override runStorm(CSVLog log) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
}