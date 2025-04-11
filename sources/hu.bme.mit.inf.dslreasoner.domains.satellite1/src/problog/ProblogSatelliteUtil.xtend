package problog

import hu.bme.mit.inf.measurement.utilities.CSVLog
import java.io.File
import hu.bme.mit.inf.measurement.utilities.configuration.SatelliteConfiguration
import java.io.FileWriter
import hu.bme.mit.inf.dslreasoner.domains.satellite1.SatelliteModelWrapper
import java.util.concurrent.atomic.AtomicBoolean
import hu.bme.mit.inf.measurement.utilities.Config
import java.util.HashMap
import java.util.Scanner

interface ProblogSatelliteUtil {
	def runProblog(SatelliteConfiguration cfg, SatelliteModelWrapper instance, CSVLog log) {
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
		return timeoutFlag.get
	}
}