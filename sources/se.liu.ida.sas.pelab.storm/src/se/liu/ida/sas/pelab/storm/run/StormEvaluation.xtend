package se.liu.ida.sas.pelab.storm.run

import java.util.regex.Pattern
import java.util.Scanner
import java.io.File
import java.io.FileWriter
import hu.bme.mit.inf.measurement.utilities.configuration.BaseConfiguration
import java.util.Map
import org.eclipse.xtext.xbase.lib.Functions.Function0
import java.util.List
import org.slf4j.LoggerFactory
import org.slf4j.Logger

class StormEvaluation {
	static val Logger LOG4J = LoggerFactory.getLogger(StormEvaluation);
	static val Pattern pattern = Pattern.compile("^System failure probability at timebound 1 is ([01]\\.\\d*)$")

	static def evalueate(BaseConfiguration cfg, Function0<Pair<String,List<String>>> generator) {
		var results = newHashMap
		var cumTransformationTime = 0.0
		var cumAnalysisTime = 0.0
		var healty = true
		
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
			LOG4J.debug("TOP {}", top)
			var remainingtime_ms = (cfg.timeoutS * 1000) - (cumAnalysisTime + cumTransformationTime)
			if (remainingtime_ms > 0) {
				val time = Math.round(Math.ceil(remainingtime_ms / 1000.0))

				/**
				 * Setup analysis model
				 * (measured as transformation)
				 */
				val trafoStart = System.nanoTime
				val file = new File(cfg.stormFile)
				LOG4J.debug("FILE {}", file.absolutePath)
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
				var error = false
				while (io.hasNextLine) {
					val line = io.nextLine
					LOG4J.debug("LINE {}", line)
					val match = pattern.matcher(line)
					if (match.find) {
						results.put(top, Double.parseDouble(match.group(1)))
					}
					if(error || line.startsWith("ERROR")){
						error = true
						LOG4J.warn("Fault Log: {}", line)
					}
				}
				val analysisEnd = System.nanoTime
				
				if(process.waitFor!=0){
					LOG4J.warn("Exit code {} with top event: {} and base model: {}", process.exitValue, top, basemodel)
				}
				healty = healty && process.exitValue==0
				/**
				 * Update configuration
				 */
				cumAnalysisTime += (analysisEnd - analysisStart) / 1000.0 / 1000.0 // convert nano seconds to ms
				cumTransformationTime += (trafoEnd - trafoStart) / 1000.0 / 1000.0 // convert nano seconds to ms
			}
		}
		var remainingtime_ms = (cfg.timeoutS * 1000) - (cumAnalysisTime + cumTransformationTime)
		val timeout = remainingtime_ms < 0
		return new StormRunInfo(cumTransformationTime, cumAnalysisTime, results, timeout, healty)
	}
}

class StormRunInfo {
	public val double transformation_ms
	public val double run_ms
	public val Map<String, Double> results
	public val boolean timeout
	public val boolean healty
	
	new(double trafo, double run, Map<String, Double> results, boolean timeout, boolean healty) {
		this.transformation_ms = trafo
		this.run_ms = run
		this.results = results
		this.timeout = timeout
		this.healty = healty
	}
}
