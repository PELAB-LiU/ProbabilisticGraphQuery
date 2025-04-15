package se.liu.ida.sas.pelab.smarthome.storm;

import hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities.SmarthomeModel;
import hu.bme.mit.inf.measurement.utilities.CSVLog;
import hu.bme.mit.inf.measurement.utilities.configuration.SmarthomeConfiguration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import se.liu.ida.sas.pelab.storm.run.StormEvaluation;
import se.liu.ida.sas.pelab.storm.run.StormRunInfo;

@SuppressWarnings("all")
public interface StormSmarthomeUtil {
  default boolean runStorm(final SmarthomeConfiguration cfg, final SmarthomeModel instance, final CSVLog log) {
    final Function0<Pair<String, List<String>>> _function = () -> {
      return new StormSmarthomeGenerator().generateFrom(instance.model);
    };
    final StormRunInfo result = StormEvaluation.evalueate(cfg, _function);
    log.log("storm.total[ms]", Double.valueOf((result.transformation_ms + result.run_ms)));
    log.log("storm.trafo[ms]", Double.valueOf(result.transformation_ms));
    log.log("storm.evaluation[ms]", Double.valueOf(result.run_ms));
    log.log("storm.result", this.stormToJSON(instance, result.results, result.timeout));
    log.log("storm.timeout", Boolean.valueOf(result.timeout));
    log.log("storm.healthy", Boolean.valueOf(result.healty));
    LogHelper.LOG4J.info("Storm completed in {}ms with result #{} (timeout: {}, healthy: {})", 
      Double.valueOf((result.transformation_ms + result.run_ms)), 
      Integer.valueOf(result.results.size()), 
      Boolean.valueOf(result.timeout), 
      Boolean.valueOf(result.healty));
    return result.timeout;
  }

  default String stormToJSON(final SmarthomeModel instance, final Map<String, Double> data, final boolean timeout) {
    final Function1<Map.Entry<String, Double>, Pair<Integer, Double>> _function = (Map.Entry<String, Double> entry) -> {
      Pair<Integer, Double> _xblockexpression = null;
      {
        final String key = entry.getKey().replace("toplevel \"callevent_MeasurementImpl", "").replace("\";", "");
        int _parseInt = Integer.parseInt(key);
        Double _value = entry.getValue();
        _xblockexpression = Pair.<Integer, Double>of(Integer.valueOf(_parseInt), _value);
      }
      return _xblockexpression;
    };
    final Function1<Pair<Integer, Double>, Integer> _function_1 = (Pair<Integer, Double> e) -> {
      return e.getKey();
    };
    final Function1<Pair<Integer, Double>, Double> _function_2 = (Pair<Integer, Double> e) -> {
      return e.getValue();
    };
    final Map<Integer, Double> results = IterableExtensions.<Pair<Integer, Double>, Integer, Double>toMap(IterableExtensions.<Map.Entry<String, Double>, Pair<Integer, Double>>map(data.entrySet(), _function), _function_1, _function_2);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("\"valid\" : ");
    _builder.append((!timeout), "\t");
    _builder.append(",");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("\"matches\" : [");
    _builder.newLine();
    {
      Set<Map.Entry<Integer, Double>> _entrySet = results.entrySet();
      boolean _hasElements = false;
      for(final Map.Entry<Integer, Double> entry : _entrySet) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "\t\t");
        }
        _builder.append("\t\t");
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("\t");
        _builder.append("\"measurement\" : \"");
        String _get = instance.idmap.get(instance.ofHashCode((entry.getKey()).intValue()));
        _builder.append(_get, "\t\t\t");
        _builder.append("\",");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("\t");
        _builder.append("\"probability\" : ");
        Double _value = entry.getValue();
        _builder.append(_value, "\t\t\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("]");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
}
