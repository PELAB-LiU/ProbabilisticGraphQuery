package hu.bme.mit.inf.measurement.utilities.viatra;

import java.util.List;
import java.util.function.Consumer;
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParserBuilder;
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParsingResults;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public interface ViatraRunnerUtil {
  default PatternParsingResults parseQueries(final String vql) {
    final PatternParsingResults result = PatternParserBuilder.instance().parse(vql);
    final boolean fault = result.hasError();
    final Consumer<Issue> _function = (Issue issue) -> {
      Severity _severity = issue.getSeverity();
      boolean _tripleEquals = (_severity == Severity.ERROR);
      if (_tripleEquals) {
        InputOutput.<String>println(("Error: " + issue));
      }
    };
    result.getAllDiagnostics().forEach(_function);
    if (fault) {
      System.err.println(vql);
    }
    return result;
  }

  default int countBasicEvents(final EngineConfig engine) {
    final int[] cnt = new int[2];
    final Consumer<IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>> _function = (IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> specification) -> {
      final ViatraQueryMatcher<? extends IPatternMatch> matcher = engine.getEngine().getMatcher(specification);
      cnt[0] = matcher.countMatches();
    };
    engine.getParsed().getQuerySpecification("BERequiredName1").ifPresent(_function);
    final Consumer<IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>> _function_1 = (IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> specification) -> {
      final ViatraQueryMatcher<? extends IPatternMatch> matcher = engine.getEngine().getMatcher(specification);
      cnt[1] = matcher.countMatches();
    };
    engine.getParsed().getQuerySpecification("BERequiredName2").ifPresent(_function_1);
    int _get = cnt[0];
    int _get_1 = cnt[1];
    return (_get + _get_1);
  }

  default int countStochasticPatterns(final EngineConfig engine) {
    final int[] cnt = new int[1];
    final Consumer<IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>> _function = (IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> specification) -> {
      final ViatraQueryMatcher<? extends IPatternMatch> matcher = engine.getEngine().getMatcher(specification);
      final Consumer<IPatternMatch> _function_1 = (IPatternMatch match) -> {
        Object _get = match.get(0);
        cnt[0] = (((Integer) _get)).intValue();
      };
      matcher.getOneArbitraryMatch().ifPresent(_function_1);
    };
    engine.getParsed().getQuerySpecification("stochasticCount").ifPresent(_function);
    return cnt[0];
  }

  default double checkMatches(final String name, final EngineConfig engine) {
    final double[] cnt = new double[1];
    final Consumer<IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>> _function = (IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> specification) -> {
      final ViatraQueryMatcher<? extends IPatternMatch> matcher = engine.getEngine().getMatcher(specification);
      String _simpleName = specification.getSimpleName();
      String _plus = ("Specification found: " + _simpleName);
      InputOutput.<String>println(_plus);
      final Consumer<IPatternMatch> _function_1 = (IPatternMatch match) -> {
        String _prettyPrint = match.prettyPrint();
        String _plus_1 = ("\t" + _prettyPrint);
        InputOutput.<String>println(_plus_1);
      };
      matcher.forEachMatch(_function_1);
      final Consumer<IPatternMatch> _function_2 = (IPatternMatch match) -> {
        Object _get = match.get(0);
        cnt[0] = (((Double) _get)).doubleValue();
      };
      matcher.getOneArbitraryMatch().ifPresent(_function_2);
    };
    engine.getParsed().getQuerySpecification(name).ifPresent(_function);
    return cnt[0];
  }

  default void printMatches(final String name, final EngineConfig engine) {
    final Consumer<IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>> _function = (IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> specification) -> {
      final ViatraQueryMatcher<? extends IPatternMatch> matcher = engine.getEngine().getMatcher(specification);
      String _simpleName = specification.getSimpleName();
      String _plus = ("Specification found: " + _simpleName);
      InputOutput.<String>println(_plus);
      final Consumer<IPatternMatch> _function_1 = (IPatternMatch match) -> {
        String _prettyPrint = match.prettyPrint();
        String _plus_1 = ("\t" + _prettyPrint);
        InputOutput.<String>println(_plus_1);
      };
      matcher.forEachMatch(_function_1);
    };
    engine.getParsed().getQuerySpecification(name).ifPresent(_function);
  }

  default void initializePatterns(final EngineConfig engine, final String... queries) {
    final Consumer<String> _function = (String name) -> {
      final Consumer<IQuerySpecification<? extends ViatraQueryMatcher>> _function_1 = (IQuerySpecification<? extends ViatraQueryMatcher> specification) -> {
        final int cnt = engine.getEngine().getMatcher(specification).countMatches();
      };
      engine.getParsed().getQuerySpecification(name).ifPresent(_function_1);
    };
    ((List<String>)Conversions.doWrapArray(queries)).forEach(_function);
  }
}
