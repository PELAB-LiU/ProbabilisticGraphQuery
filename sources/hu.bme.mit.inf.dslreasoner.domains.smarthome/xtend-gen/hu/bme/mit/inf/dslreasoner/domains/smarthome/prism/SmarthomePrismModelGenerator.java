package hu.bme.mit.inf.dslreasoner.domains.smarthome.prism;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Consumer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParsingResults;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngine;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@Deprecated
@SuppressWarnings("all")
public class SmarthomePrismModelGenerator {
  public String generatePrismModel(final Map<EObject, String> names, final PatternParsingResults parsed, final ViatraQueryEngine engine) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("module variables");
    _builder.newLine();
    {
      ArrayList<IPatternMatch> _matches = this.getMatches("prism_tempIncrBE", parsed, engine);
      for(final IPatternMatch match : _matches) {
        _builder.append("\t");
        String _map = this.map(names, match.get(0));
        _builder.append(_map, "\t");
        _builder.append(" : bool init true;");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("TI : bool init true;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("TW : bool init true;");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    {
      ArrayList<IPatternMatch> _matches_1 = this.getMatches("prism_tempIncrBE", parsed, engine);
      for(final IPatternMatch match_1 : _matches_1) {
        _builder.append("\t");
        _builder.append("[] ");
        String _map_1 = this.map(names, match_1.get(0));
        _builder.append(_map_1, "\t");
        _builder.append(" -> ");
        Object _get = match_1.get(1);
        double _rateOf = this.rateOf((((Double) _get)).doubleValue());
        _builder.append(_rateOf, "\t");
        _builder.append(" : (");
        String _map_2 = this.map(names, match_1.get(0));
        _builder.append(_map_2, "\t");
        _builder.append("\'=false);");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("[] TI -> ");
    double _rateOf_1 = this.rateOf(0.925);
    _builder.append(_rateOf_1, "\t");
    _builder.append(" : (TI\'=false);");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("[] TW -> ");
    double _rateOf_2 = this.rateOf(0.925);
    _builder.append(_rateOf_2, "\t");
    _builder.append(" : (TW\'=false);");
    _builder.newLineIfNotEmpty();
    _builder.append("endmodule");
    _builder.newLine();
    final String variables = _builder.toString();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("//Matches for temperature increments");
    _builder_1.newLine();
    {
      ArrayList<IPatternMatch> _matches_2 = this.getMatches("prism_tempIncrBE", parsed, engine);
      for(final IPatternMatch match_2 : _matches_2) {
        _builder_1.append("formula incr_");
        String _map_3 = this.map(names, match_2.get(0));
        _builder_1.append(_map_3);
        _builder_1.append(" = ");
        String _map_4 = this.map(names, match_2.get(0));
        _builder_1.append(_map_4);
        _builder_1.append(" & TI;");
        _builder_1.newLineIfNotEmpty();
      }
    }
    _builder_1.newLine();
    _builder_1.append("//Matches for temperature warnings with many arg");
    _builder_1.newLine();
    {
      ArrayList<IPatternMatch> _matches_3 = this.getMatches("fourWarning", parsed, engine);
      for(final IPatternMatch match_3 : _matches_3) {
        _builder_1.append("formula warn4_");
        String _map_5 = this.map(names, match_3.get(1));
        _builder_1.append(_map_5);
        _builder_1.append("_");
        String _map_6 = this.map(names, match_3.get(2));
        _builder_1.append(_map_6);
        _builder_1.append("_");
        String _map_7 = this.map(names, match_3.get(3));
        _builder_1.append(_map_7);
        _builder_1.append("_");
        String _map_8 = this.map(names, match_3.get(4));
        _builder_1.append(_map_8);
        _builder_1.append(" = ");
        String _map_9 = this.map(names, match_3.get(1));
        _builder_1.append(_map_9);
        _builder_1.append(" & ");
        String _map_10 = this.map(names, match_3.get(2));
        _builder_1.append(_map_10);
        _builder_1.append(" & ");
        String _map_11 = this.map(names, match_3.get(3));
        _builder_1.append(_map_11);
        _builder_1.append(" & ");
        String _map_12 = this.map(names, match_3.get(4));
        _builder_1.append(_map_12);
        _builder_1.append(" & TW;");
        _builder_1.newLineIfNotEmpty();
      }
    }
    _builder_1.append("//Aggregate temperature warnings");
    _builder_1.newLine();
    {
      HashSet<Object> _unique = this.unique("fourWarning", 4, parsed, engine);
      for(final Object measurement : _unique) {
        _builder_1.append("formula warn_");
        String _map_13 = this.map(names, measurement);
        _builder_1.append(_map_13);
        _builder_1.append(" = TW & ");
        _builder_1.newLineIfNotEmpty();
        {
          ArrayList<IPatternMatch> _partialMatches = this.getPartialMatches("fourWarning", 4, measurement, parsed, engine);
          boolean _hasElements = false;
          for(final IPatternMatch match_4 : _partialMatches) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder_1.appendImmediate(" & ", "");
            }
            _builder_1.append("warn4_");
            String _map_14 = this.map(names, match_4.get(1));
            _builder_1.append(_map_14);
            _builder_1.append("_");
            String _map_15 = this.map(names, match_4.get(2));
            _builder_1.append(_map_15);
            _builder_1.append("_");
            String _map_16 = this.map(names, match_4.get(3));
            _builder_1.append(_map_16);
            _builder_1.append("_");
            String _map_17 = this.map(names, match_4.get(4));
            _builder_1.append(_map_17);
            _builder_1.newLineIfNotEmpty();
          }
        }
        _builder_1.append(";");
        _builder_1.newLine();
      }
    }
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.newLine();
    final String incrementevents = _builder_1.toString();
    StringConcatenation _builder_2 = new StringConcatenation();
    _builder_2.append("ctmc");
    _builder_2.newLine();
    _builder_2.newLine();
    _builder_2.append(variables);
    _builder_2.newLineIfNotEmpty();
    _builder_2.newLine();
    _builder_2.append(incrementevents);
    _builder_2.newLineIfNotEmpty();
    return _builder_2.toString();
  }

  public String map(final Map<EObject, String> map, final Object object) {
    return map.get(object);
  }

  public double rateOf(final double p) {
    double _log = Math.log((1 - p));
    return (-_log);
  }

  public ArrayList<IPatternMatch> getMatches(final String name, final PatternParsingResults parsed, final ViatraQueryEngine engine) {
    final ArrayList<IPatternMatch> matches = CollectionLiterals.<IPatternMatch>newArrayList();
    final Consumer<IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>> _function = (IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> specification) -> {
      final ViatraQueryMatcher<? extends IPatternMatch> matcher = engine.getMatcher(specification);
      matches.addAll(matcher.getAllMatches());
    };
    parsed.getQuerySpecification(name).ifPresent(_function);
    return matches;
  }

  public HashSet<Object> unique(final String name, final int index, final PatternParsingResults parsed, final ViatraQueryEngine engine) {
    final HashSet<Object> matches = CollectionLiterals.<Object>newHashSet();
    final Consumer<IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>> _function = (IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> specification) -> {
      final ViatraQueryMatcher<? extends IPatternMatch> matcher = engine.getMatcher(specification);
      final Consumer<IPatternMatch> _function_1 = (IPatternMatch match) -> {
        matches.add(match.get(index));
      };
      matcher.forEachMatch(_function_1);
    };
    parsed.getQuerySpecification(name).ifPresent(_function);
    return matches;
  }

  public ArrayList<IPatternMatch> getPartialMatches(final String name, final int index, final Object object, final PatternParsingResults parsed, final ViatraQueryEngine engine) {
    final ArrayList<IPatternMatch> matches = CollectionLiterals.<IPatternMatch>newArrayList();
    final Consumer<IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>> _function = (IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> specification) -> {
      final ViatraQueryMatcher<? extends IPatternMatch> matcher = engine.getMatcher(specification);
      final Consumer<IPatternMatch> _function_1 = (IPatternMatch match) -> {
        boolean _equals = match.get(index).equals(object);
        if (_equals) {
          matches.add(match);
        }
      };
      matcher.forEachMatch(_function_1);
    };
    parsed.getQuerySpecification(name).ifPresent(_function);
    return matches;
  }
}
