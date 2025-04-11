package se.liu.ida.sas.pelab.smarthome.storm

import hu.bme.mit.inf.measurement.utilities.CSVLog
import se.liu.ida.sas.pelab.storm.run.StormEvaluation
import hu.bme.mit.inf.measurement.utilities.configuration.SmarthomeConfiguration
import hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities.SmarthomeModel
import java.util.Map
import org.slf4j.LoggerFactory
import org.slf4j.Logger

package class LogHelper{
	public static val Logger LOG4J = LoggerFactory.getLogger(StormSmarthomeUtil);
} 

interface StormSmarthomeUtil {
	def runStorm(SmarthomeConfiguration cfg, SmarthomeModel instance, CSVLog log) {
		val result = StormEvaluation.evalueate(cfg,
			[
				(new StormSmarthomeGenerator).generateFrom(instance.model)
			]
		)
		log.log("storm.total[ms]", result.transformation_ms + result.run_ms)
		log.log("storm.trafo[ms]", result.transformation_ms)
		log.log("storm.evaluation[ms]", result.run_ms)
		log.log("storm.result", stormToJSON(instance, result.results, result.timeout))
		log.log("storm.timeout", result.timeout)
		LogHelper.LOG4J.info("ProbLog completed in {}ms with result #{} (timeout: {})", 
			result.transformation_ms + result.run_ms, 
			result.results.size,
			result.timeout
		)
		return result.timeout
	}
	
	def String stormToJSON(SmarthomeModel instance, Map<String,Double> data, boolean timeout){
		val results = data.entrySet.map[ entry |
			val key = entry.key
				.replace("toplevel \"callevent_MeasurementImpl", "")
				.replace("\";", "")
			Integer.parseInt(key) -> entry.value
		].toMap([e|e.key],[e|e.value])
		return '''
		{
			"valid" : «!timeout»,
			"matches" : [
				«FOR entry : results.entrySet SEPARATOR ","»
					{
						"measurement" : "«instance.idmap.get(instance.ofHashCode(entry.key))»",
						"probability" : «entry.value»
					}
				«ENDFOR»
			]
		}
		'''
	}
}