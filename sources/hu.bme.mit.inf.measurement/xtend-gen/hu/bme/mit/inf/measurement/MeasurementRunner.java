package hu.bme.mit.inf.measurement;

import hu.bme.mit.inf.dslreasoner.domains.satellite1.SatelliteScaleRunners;
import hu.bme.mit.inf.dslreasoner.domains.satellite1.SatelliteSingleGraph;
import hu.bme.mit.inf.dslreasoner.domains.smarthome.viatra.SmarthomeRunner;
import hu.bme.mit.inf.dslreasoner.domains.smarthome.viatra.SmarthomeScaleRunner;
import hu.bme.mit.inf.dslreasoner.domains.surveillance.viatra.SurveillanceRunner;
import hu.bme.mit.inf.dslreasoner.domains.surveillance.viatra.SurveillanceScaleRunner;
import hu.bme.mit.inf.measurement.utilities.configuration.BaseConfiguration;
import hu.bme.mit.inf.measurement.utilities.configuration.RunConfiguration;
import hu.bme.mit.inf.measurement.utilities.configuration.SatelliteConfiguration;
import hu.bme.mit.inf.measurement.utilities.configuration.SmarthomeConfiguration;
import hu.bme.mit.inf.measurement.utilities.configuration.SurveillanceConfiguration;

@SuppressWarnings("all")
public class MeasurementRunner {
  public static void main(final String[] args) {
    final BaseConfiguration bcfg = BaseConfiguration.parse(args);
    if ((bcfg == null)) {
      return;
    }
    RunConfiguration _case = bcfg.getCase();
    if (_case != null) {
      switch (_case) {
        case SAT:
          final SatelliteConfiguration cfg = SatelliteConfiguration.parse(args);
          int _iterations = cfg.getIterations();
          boolean _tripleEquals = (_iterations == 0);
          if (_tripleEquals) {
            final SatelliteScaleRunners runner = new SatelliteScaleRunners(cfg);
            runner.run();
          } else {
            final SatelliteSingleGraph runner_1 = new SatelliteSingleGraph(cfg);
            runner_1.run();
          }
          break;
        case SH:
          final SmarthomeConfiguration cfg_1 = SmarthomeConfiguration.parse(args);
          int _iterations_1 = cfg_1.getIterations();
          boolean _tripleEquals_1 = (_iterations_1 == 0);
          if (_tripleEquals_1) {
            final SmarthomeScaleRunner runner_2 = new SmarthomeScaleRunner(cfg_1);
            runner_2.run();
          } else {
            final SmarthomeRunner runner_3 = new SmarthomeRunner(cfg_1);
            runner_3.run();
          }
          break;
        case SRV:
          final SurveillanceConfiguration cfg_2 = SurveillanceConfiguration.parse(args);
          int _iterations_2 = cfg_2.getIterations();
          boolean _tripleEquals_2 = (_iterations_2 == 0);
          if (_tripleEquals_2) {
            final SurveillanceScaleRunner runner_4 = new SurveillanceScaleRunner(cfg_2);
            runner_4.run();
          } else {
            final SurveillanceRunner runner_5 = new SurveillanceRunner(cfg_2);
            runner_5.run();
          }
          break;
        default:
          RunConfiguration _case_1 = bcfg.getCase();
          String _plus = ("No configuration is defined for " + _case_1);
          throw new IllegalArgumentException(_plus);
      }
    } else {
      RunConfiguration _case_1 = bcfg.getCase();
      String _plus = ("No configuration is defined for " + _case_1);
      throw new IllegalArgumentException(_plus);
    }
  }
}
