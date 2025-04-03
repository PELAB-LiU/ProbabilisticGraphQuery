package se.liu.ida.sas.pelab.problog.run

import hu.bme.mit.inf.measurement.utilities.Config
import java.util.HashMap
import java.util.regex.Pattern
import java.util.Scanner
import org.slf4j.LoggerFactory
import org.slf4j.Logger

class ProbLogEvaluator {
	static val Logger LOG4J = LoggerFactory.getLogger(ProbLogEvaluator);
	def evaluate(Config cfg){
		val rx_expected = Pattern.compile(cfg.asString("pattern"))
		
		val builder = new ProcessBuilder("problog", cfg.asString("file"));
		val process = builder.start
		val timer = killOnTimeout(cfg,"timeout",process)
		
		val output = new HashMap<String,Object>
		val io = new Scanner(process.inputStream)
		io.forEach[line |
			LOG4J.debug("Line {}", line)
			val match = rx_expected.matcher(line)
			if(match.find){
				output.put(match.group(1),Double.parseDouble(match.group(2)))
			}
		]
		timer.cancel()
		return output
	}

	private def killOnTimeout(Config cfg, String timeoutname, Process process){
		if(cfg.isDefined(timeoutname)){
			val timer = Config.timeout(cfg.asInt(timeoutname),[|
				process.destroy
				process.inputStream.close
			])
			return timer
		}
		return null
	}
}