package reliability.events;

import com.google.common.collect.HashMultiset;
import hu.bme.mit.delta.mdd.MddHandle;
import java.util.stream.Stream;
import org.eclipse.viatra.query.runtime.matchers.psystem.aggregations.IMultisetAggregationOperator;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.eclipse.xtext.xbase.lib.Pure;
import reliability.intreface.ExecutionTime;
import reliability.mdd.MddHandleMultiset;

@SuppressWarnings("all")
public abstract class MddCollectorOperator implements IMultisetAggregationOperator<Event, HashMultiset<MddHandle>, MddHandleMultiset> {
  @Accessors(AccessorType.PUBLIC_GETTER)
  private static double totalcalctime = 0.0;

  @Override
  public MddHandleMultiset aggregateStream(final Stream<Event> stream) {
    boolean ignored = false;
    try {
      ignored = ExecutionTime.start();
      final MddHandleMultiset wrapper = new MddHandleMultiset();
      final Function2<MddHandleMultiset, Event, MddHandleMultiset> _function = (MddHandleMultiset set, Event element) -> {
        MddHandleMultiset _xblockexpression = null;
        {
          set.getMultiset().add(element.getHandle());
          _xblockexpression = set;
        }
        return _xblockexpression;
      };
      IteratorExtensions.<Event, MddHandleMultiset>fold(stream.iterator(), wrapper, _function);
      return wrapper;
    } finally {
      ExecutionTime.stop(ignored);
    }
  }

  @Override
  public HashMultiset<MddHandle> createNeutral() {
    return HashMultiset.<MddHandle>create();
  }

  @Override
  public String getName() {
    return "collect";
  }

  @Override
  public String getShortDescription() {
    return "Aggregates the arguments to a set.";
  }

  @Override
  public boolean isNeutral(final HashMultiset<MddHandle> result) {
    return result.isEmpty();
  }

  @Pure
  public static double getTotalcalctime() {
    return MddCollectorOperator.totalcalctime;
  }
}
