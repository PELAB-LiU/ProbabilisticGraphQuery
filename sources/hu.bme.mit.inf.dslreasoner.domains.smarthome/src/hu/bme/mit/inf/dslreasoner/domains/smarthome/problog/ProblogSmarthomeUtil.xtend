package hu.bme.mit.inf.dslreasoner.domains.smarthome.problog

import java.util.Map
import hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities.SmarthomeModel
import hu.bme.mit.inf.measurement.utilities.CSVLog
import java.io.File
import hu.bme.mit.inf.measurement.utilities.configuration.SmarthomeConfiguration
import java.io.FileWriter
import java.util.concurrent.atomic.AtomicBoolean
import hu.bme.mit.inf.measurement.utilities.Config
import java.util.HashMap
import java.util.Scanner
import org.slf4j.LoggerFactory
import org.slf4j.Logger

package class LogHelper{
	public static val Logger LOG4J = LoggerFactory.getLogger(ProblogSmarthomeUtil);
} 

interface ProblogSmarthomeUtil {
	def runProblog(SmarthomeConfiguration cfg, SmarthomeModel instance, CSVLog log) {
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
			timeoutFlag.set(true)
			process2.destroyForcibly
		])

		val output = new HashMap<String, Object>
		val io = new Scanner(process.inputStream)
		io.forEach [ line |
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
		log.log("problog.result", problogToJSON(instance, output, timeoutFlag.get))
		log.log("problog.timeout", timeoutFlag.get)
		log.log("problog.exitcode", process.waitFor)
		LogHelper.LOG4J.info("ProbLog completed in {}ms with result #{} (timeout: {})", 
			((end - start)/1000.0/1000), 
			output.size,
			timeoutFlag.get
		)
		if(process.exitValue!=0){
			LogHelper.LOG4J.warn("Exit code {} with model: {}", process.exitValue, plmodel)
		}
		return timeoutFlag.get
	}
	
	def String problogToJSON(SmarthomeModel instance, Map<String,Object> data, boolean timeout){
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
}