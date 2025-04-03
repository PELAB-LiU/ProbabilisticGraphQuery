package hu.bme.mit.inf.dslreasoner.domains.surveillance.problog

import java.util.Map
import hu.bme.mit.inf.dslreasoner.domains.surveillance.viatra.SurveillanceWrapper
import hu.bme.mit.inf.measurement.utilities.configuration.SurveillanceConfiguration
import hu.bme.mit.inf.measurement.utilities.CSVLog
import java.io.File
import java.io.FileWriter
import java.util.concurrent.atomic.AtomicBoolean
import hu.bme.mit.inf.measurement.utilities.Config
import java.util.HashMap
import java.util.Scanner

interface ProblogSurveillanceUtil {
	def runProblog(SurveillanceConfiguration cfg, SurveillanceWrapper instance, CSVLog log) {
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
		log.log("problog.result", problogToJSON(instance, output, timeoutFlag.get))
		log.log("problog.timeout", timeoutFlag.get)
	}
	
	def String problogToJSON(SurveillanceWrapper instance, Map<String,Object> data, boolean timeout){
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
}