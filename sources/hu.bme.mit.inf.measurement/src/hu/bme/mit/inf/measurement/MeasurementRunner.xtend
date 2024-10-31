package hu.bme.mit.inf.measurement

import hu.bme.mit.inf.dslreasoner.domains.satellite1.SatelliteSingleGraph
import hu.bme.mit.inf.measurement.utilities.configuration.BaseConfiguration
import hu.bme.mit.inf.measurement.utilities.configuration.RunConfiguration
import hu.bme.mit.inf.measurement.utilities.configuration.SatelliteConfiguration
import hu.bme.mit.inf.measurement.utilities.configuration.SmarthomeConfiguration
import hu.bme.mit.inf.dslreasoner.domains.smarthome.viatra.SmarthomeRunner
import hu.bme.mit.inf.measurement.utilities.configuration.SurveillanceConfiguration
import hu.bme.mit.inf.dslreasoner.domains.surveillance.viatra.SurveillanceRunner
import hu.bme.mit.inf.dslreasoner.domains.satellite1.SatelliteScaleRunners
import hu.bme.mit.inf.dslreasoner.domains.smarthome.viatra.SmarthomeScaleRunner
import hu.bme.mit.inf.dslreasoner.domains.surveillance.viatra.SurveillanceScaleRunner

class MeasurementRunner {
	def static void main(String[] args) {
		val bcfg = BaseConfiguration.parse(args)
		if(bcfg === null){
			return
		}
		switch (bcfg.^case) {
			case RunConfiguration.SAT :{ 
				val cfg = SatelliteConfiguration.parse(args)
				if(cfg.iterations===0){
					val runner = new SatelliteScaleRunners(cfg)
					runner.run();
				} else {
					val runner = new SatelliteSingleGraph(cfg)
					runner.run();
				}
				
			}
			case RunConfiguration.SH :{
				val cfg = SmarthomeConfiguration.parse(args)
				if(cfg.iterations===0){
					val runner = new SmarthomeScaleRunner(cfg)
					runner.run();
				} else {
					val runner = new SmarthomeRunner(cfg)
					runner.run();
				}
			}
			case RunConfiguration.SRV :{
				val cfg = SurveillanceConfiguration.parse(args)
				if(cfg.iterations===0){
					val runner = new SurveillanceScaleRunner(cfg)
					runner.run();
				} else {
					val runner = new SurveillanceRunner(cfg)
					runner.run();
				}
			}
			default: {
				throw new IllegalArgumentException("No configuration is defined for "+bcfg.^case)
			}
		}
	}
}