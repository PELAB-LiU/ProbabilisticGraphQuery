package hu.bme.mit.inf.dslreasoner.domains.surveillance.viatra;

import java.util.Comparator;
import java.util.Map;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.xtext.xbase.lib.IntegerRange;

@SuppressWarnings("all")
public class SurveillanceComparator implements Comparator<IPatternMatch> {
  private final Map<EObject, Integer> ordering;

  public SurveillanceComparator(final Map<EObject, Integer> ordering) {
    this.ordering = ordering;
  }

  @Override
  public int compare(final IPatternMatch o1, final IPatternMatch o2) {
    final int argc = Math.max(o1.parameterNames().size(), o2.parameterNames().size());
    IntegerRange _upTo = new IntegerRange(0, (argc - 1));
    for (final int i : _upTo) {
      {
        Integer _get = this.ordering.get(o2.get(i));
        Integer _get_1 = this.ordering.get(o1.get(i));
        final int diff = ((_get).intValue() - (_get_1).intValue());
        if ((diff != 0)) {
          return diff;
        }
      }
    }
    return 0;
  }
}
