package se.liu.ida.sas.pelab.satellite1.storm

import hu.bme.mit.inf.measurement.utilities.CSVLog
import se.liu.ida.sas.pelab.storm.run.StormEvaluation
import hu.bme.mit.inf.measurement.utilities.configuration.SatelliteConfiguration
import hu.bme.mit.inf.dslreasoner.domains.satellite1.SatelliteModelWrapper
import hu.bme.mit.inf.dslreasoner.domains.satellite1.performability.Performability

interface StormSatelliteUtil {
	def runStorm(SatelliteConfiguration cfg, SatelliteModelWrapper instance, CSVLog log) {
		val result = StormEvaluation.evalueate(cfg,
			[
				(new StormSatelliteGenerator).generateFrom(instance.mission)
			]
		)
		val coverage = result.results.entrySet.map[ entry |
			val count = entry.key
				.replace("toplevel \"cov", "")
				.replace("_\";", "")
			Integer.parseInt(count) -> entry.value
		].fold(0.0, [accu, entry | accu+Performability.calculate(entry.key)*entry.value])
		
		log.log("storm.total[ms]", result.transformation_ms + result.run_ms)
		log.log("storm.trafo[ms]", result.transformation_ms)
		log.log("storm.evaluation[ms]", result.run_ms)
		log.log("storm.result", coverage)
		log.log("storm.timeout", result.timeout)
	}
}