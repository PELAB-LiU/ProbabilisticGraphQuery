package se.liu.ida.sas.pelab.satellite1.storm;

import hu.bme.mit.inf.dslreasoner.domains.satellite1.SatelliteModelWrapper;
import hu.bme.mit.inf.dslreasoner.domains.satellite1.performability.Performability;
import hu.bme.mit.inf.measurement.utilities.CSVLog;
import hu.bme.mit.inf.measurement.utilities.configuration.SatelliteConfiguration;
import java.util.List;
import java.util.Map;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import se.liu.ida.sas.pelab.storm.run.StormEvaluation;
import se.liu.ida.sas.pelab.storm.run.StormRunInfo;

@SuppressWarnings("all")
public interface StormSatelliteUtil {
  default boolean runStorm(final SatelliteConfiguration cfg, final SatelliteModelWrapper instance, final CSVLog log) {
    final Function0<Pair<String, List<String>>> _function = () -> {
      return new StormSatelliteGenerator().generateFrom(instance.mission);
    };
    final StormRunInfo result = StormEvaluation.evalueate(cfg, _function);
    final Function1<Map.Entry<String, Double>, Pair<Integer, Double>> _function_1 = (Map.Entry<String, Double> entry) -> {
      Pair<Integer, Double> _xblockexpression = null;
      {
        final String count = entry.getKey().replace("toplevel \"cov", "").replace("_\";", "");
        int _parseInt = Integer.parseInt(count);
        Double _value = entry.getValue();
        _xblockexpression = Pair.<Integer, Double>of(Integer.valueOf(_parseInt), _value);
      }
      return _xblockexpression;
    };
    final Function2<Double, Pair<Integer, Double>, Double> _function_2 = (Double accu, Pair<Integer, Double> entry) -> {
      double _calculate = Performability.calculate((entry.getKey()).intValue());
      Double _value = entry.getValue();
      double _multiply = (_calculate * (_value).doubleValue());
      return Double.valueOf(((accu).doubleValue() + _multiply));
    };
    final Double coverage = IterableExtensions.<Pair<Integer, Double>, Double>fold(IterableExtensions.<Map.Entry<String, Double>, Pair<Integer, Double>>map(result.results.entrySet(), _function_1), Double.valueOf(0.0), _function_2);
    log.log("storm.total[ms]", Double.valueOf((result.transformation_ms + result.run_ms)));
    log.log("storm.trafo[ms]", Double.valueOf(result.transformation_ms));
    log.log("storm.evaluation[ms]", Double.valueOf(result.run_ms));
    log.log("storm.result", coverage);
    log.log("storm.timeout", Boolean.valueOf(result.timeout));
    LogHelper.LOG4J.info("Storm completed in {}ms with result #{} (timeout: {})", 
      Double.valueOf((result.transformation_ms + result.run_ms)), 
      Integer.valueOf(result.results.size()), 
      Boolean.valueOf(result.timeout));
    return result.timeout;
  }
}
