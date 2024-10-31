package reliability.events;

import hu.bme.mit.delta.mdd.MddHandle;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.viatra.query.runtime.matchers.psystem.aggregations.IMultisetAggregationOperator;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import reliability.intreface.ExecutionTime;
import reliability.mdd.MddModel;

@SuppressWarnings("all")
public abstract class MddOrOperator<Buffer extends Object> implements IMultisetAggregationOperator<Event, Buffer, Event> {
  @Override
  public Event aggregateStream(final Stream<Event> stream) {
    boolean ignored = false;
    try {
      ignored = ExecutionTime.start();
      final Set<Event> setdata = stream.collect(Collectors.<Event>toSet());
      final Function2<MddHandle, Event, MddHandle> _function = (MddHandle accu, Event entry) -> {
        return accu.union(entry.getHandle());
      };
      final MddHandle result = IterableExtensions.<Event, MddHandle>fold(setdata, MddModel.INSTANCE.getHandleOf(false), _function);
      SimpleEvent _simpleEvent = new SimpleEvent(result);
      return ((Event) _simpleEvent);
    } finally {
      ExecutionTime.stop(ignored);
    }
  }

  @Override
  public String getName() {
    return "or";
  }

  @Override
  public String getShortDescription() {
    return "Joins MddHandles with or relation.";
  }
}
