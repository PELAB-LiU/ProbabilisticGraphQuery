package hu.bme.mit.inf.dslreasoner.domains.surveillance.debug;

import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.Coordinate;
import hu.bme.mit.inf.measurement.utilities.viatra.EngineConfig;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.xtend2.lib.StringConcatenation;
import uncertaindatatypes.UReal;

@SuppressWarnings("all")
public interface DebugUtil {
  default String ureal(final UReal ureal) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("\"x\":");
    double _x = ureal.getX();
    _builder.append(_x, "\t");
    _builder.append(",");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("\"u\":");
    double _u = ureal.getU();
    _builder.append(_u, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }

  default String coordinate(final Coordinate c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("\"x\": ");
    String _ureal = this.ureal(c.x);
    _builder.append(_ureal, "\t");
    _builder.append(",");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("\"y\": ");
    String _ureal_1 = this.ureal(c.y);
    _builder.append(_ureal_1, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }

  default String comaprtison(final Coordinate a, final Coordinate b, final boolean result) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("\"a\": ");
    String _coordinate = this.coordinate(a);
    _builder.append(_coordinate, "\t");
    _builder.append(",");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("\"b\": ");
    String _coordinate_1 = this.coordinate(b);
    _builder.append(_coordinate_1, "\t");
    _builder.append(",");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("\"r\":");
    _builder.append(result, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }

  default String probability(final Coordinate from, final Coordinate to, final UReal speed, final double confidence, final double result) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("\"from\": ");
    String _coordinate = this.coordinate(from);
    _builder.append(_coordinate, "\t");
    _builder.append(",");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("\"to\": ");
    String _coordinate_1 = this.coordinate(to);
    _builder.append(_coordinate_1, "\t");
    _builder.append(",");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("\"speed\": ");
    String _ureal = this.ureal(speed);
    _builder.append(_ureal, "\t");
    _builder.append(",");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("\"confidence\": ");
    _builder.append(confidence, "\t");
    _builder.append(",");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("\"r\":");
    _builder.append(result, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }

  default String getMatchesJSON(final EngineConfig engine, final Map<EObject, Integer> index) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{");
    _builder.newLine();
    {
      Iterable<IQuerySpecification<?>> _querySpecifications = engine.getParsed().getQuerySpecifications();
      boolean _hasElements = false;
      for(final IQuerySpecification spec : _querySpecifications) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "\t");
        }
        _builder.append("\t");
        _builder.append("\"");
        String _fullyQualifiedName = spec.getFullyQualifiedName();
        _builder.append(_fullyQualifiedName, "\t");
        _builder.append("\" : {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("\"count\": ");
        int _countMatches = engine.getEngine().<ViatraQueryMatcher<? extends IPatternMatch>>getMatcher(spec).countMatches();
        _builder.append(_countMatches, "\t\t");
        _builder.append(",");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("\"matches\": [");
        _builder.newLine();
        {
          Collection<? extends IPatternMatch> _allMatches = engine.getEngine().<ViatraQueryMatcher<? extends IPatternMatch>>getMatcher(spec).getAllMatches();
          boolean _hasElements_1 = false;
          for(final IPatternMatch match : _allMatches) {
            if (!_hasElements_1) {
              _hasElements_1 = true;
            } else {
              _builder.appendImmediate(",", "\t\t\t");
            }
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("{");
            _builder.newLine();
            {
              List _parameterNames = spec.getParameterNames();
              boolean _hasElements_2 = false;
              for(final Object arg : _parameterNames) {
                if (!_hasElements_2) {
                  _hasElements_2 = true;
                } else {
                  _builder.appendImmediate(",", "\t\t\t\t");
                }
                {
                  Object _get = match.get(arg.toString());
                  if ((_get instanceof EObject)) {
                    _builder.append("\t");
                    _builder.append("\t\t");
                    _builder.append("\t");
                    _builder.append("\"");
                    _builder.append(arg, "\t\t\t\t");
                    _builder.append("\": ");
                    Object _elvis = null;
                    Integer _get_1 = index.get(match.get(arg.toString()));
                    if (_get_1 != null) {
                      _elvis = _get_1;
                    } else {
                      StringConcatenation _builder_1 = new StringConcatenation();
                      _builder_1.append("\"");
                      Object _get_2 = match.get(arg.toString());
                      _builder_1.append(_get_2);
                      _builder_1.append("\"");
                      _elvis = _builder_1.toString();
                    }
                    _builder.append(((Object)_elvis), "\t\t\t\t");
                    _builder.newLineIfNotEmpty();
                  } else {
                    Object _get_3 = match.get(arg.toString());
                    if ((_get_3 instanceof Number)) {
                      _builder.append("\t");
                      _builder.append("\t\t");
                      _builder.append("\t");
                      _builder.append("\"");
                      _builder.append(arg, "\t\t\t\t");
                      _builder.append("\": ");
                      Object _get_4 = match.get(arg.toString());
                      _builder.append(_get_4, "\t\t\t\t");
                      _builder.newLineIfNotEmpty();
                    } else {
                      _builder.append("\t");
                      _builder.append("\t\t");
                      _builder.append("\t");
                      _builder.append("\"");
                      _builder.append(arg, "\t\t\t\t");
                      _builder.append("\": \"");
                      Object _get_5 = match.get(arg.toString());
                      _builder.append(_get_5, "\t\t\t\t");
                      _builder.append("\"");
                      _builder.newLineIfNotEmpty();
                    }
                  }
                }
              }
            }
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("}");
            _builder.newLine();
          }
        }
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("]");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
}
