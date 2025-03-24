package se.liu.ida.sas.pelab.storm.run

import hu.bme.mit.inf.measurement.utilities.Config
import java.util.regex.Pattern
import java.util.HashMap
import java.util.Scanner
import java.io.File
import java.io.FileWriter
import hu.bme.mit.inf.measurement.utilities.configuration.BaseConfiguration
import java.io.BufferedReader
import java.util.Map
import org.eclipse.xtext.xbase.lib.Functions.Function0
import java.util.List

class StormEvaluation {
	static val Pattern pattern = Pattern.compile("^System failure probability at timebound 1 is ([01]\\.\\d*)$")

	static def evalueate(BaseConfiguration cfg, Function0<Pair<String,List<String>>> generator) {
		var results = newHashMap
		var cumTransformationTime = 0.0
		var cumAnalysisTime = 0.0

		/**
		 * Transform base model
		 * (measured as transformation)
		 */
		val start = System.nanoTime
		val value = generator.apply()
		val end = System.nanoTime
		val basemodel = value.key
		val tops = value.value
		cumTransformationTime += (end - start) / 1000.0 / 1000.0 // convert nano seconds to ms
		
		for (top : tops) {
			//`println('''TOP: «top»''')
			var remainingtime_ms = (cfg.timeoutS * 1000) - (cumAnalysisTime + cumTransformationTime)
			if (remainingtime_ms > 0) {
				val time = Math.round(Math.ceil(remainingtime_ms / 1000.0))

				/**
				 * Setup analysis model
				 * (measured as transformation)
				 */
				val trafoStart = System.nanoTime
				val file = new File(cfg.stormFile)
				println(file.absolutePath)
				file.createNewFile
				val writer = new FileWriter(file)
				writer.write('''
				«top»
				«basemodel»
				''')
				writer.flush
				writer.close()
				val trafoEnd = System.nanoTime
				/**
				 * Run analysis
				 * (measured as analysis)
				 */
				val analysisStart = System.nanoTime
				val builder = new ProcessBuilder("storm-dft", "-dft", cfg.stormFile, 
					"--timebound", "1", 
					"--bdd",
					"--precision", "1e-09", 
					"--timeout", time.toString);
				val process = builder.start
				/**
				 * Process results
				 */
				val io = new Scanner(process.inputStream)
				while (io.hasNextLine) {
					val line = io.nextLine
					println('''DEBUG: «line»''')
					val match = pattern.matcher(line)
					if (match.find) {
						results.put(top, Double.parseDouble(match.group(1)))
					}
				}
				val analysisEnd = System.nanoTime
				/**
				 * Update configuration
				 */
				cumAnalysisTime += (analysisEnd - analysisStart) / 1000.0 / 1000.0 // convert nano seconds to ms
				cumTransformationTime += (trafoEnd - trafoStart) / 1000.0 / 1000.0 // convert nano seconds to ms
			}
		}
		var remainingtime_ms = (cfg.timeoutS * 1000) - (cumAnalysisTime + cumTransformationTime)
		val timeout = remainingtime_ms < 0
		return new StormRunInfo(cumTransformationTime, cumAnalysisTime, results, timeout)
	}
}

class StormRunInfo {
	public val double transformation_ms
	public val double run_ms
	public val Map<String, Double> results
	public val boolean timeout
	
	new(double trafo, double run, Map<String, Double> results, boolean timeout) {
		this.transformation_ms = trafo
		this.run_ms = run
		this.results = results
		this.timeout = timeout
	}
}
