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
public abstract class MddNotOperator<Buffer extends Object> implements IMultisetAggregationOperator<Event, Buffer, Event> {
  @Override
  public Event aggregateStream(final Stream<Event> stream) {
    boolean ignored = false;
    try {
      ignored = ExecutionTime.start();
      final Set<Event> setdata = stream.collect(Collectors.<Event>toSet());
      final Function2<MddHandle, Event, MddHandle> _function = (MddHandle acc, Event current) -> {
        return acc.union(current.getHandle());
      };
      MddHandle temp = IterableExtensions.<Event, MddHandle>fold(setdata, 
        MddModel.INSTANCE.getHandleOf(false), _function);
      final MddHandle result = MddModel.INSTANCE.getHandleOf(true).minus(temp);
      return new SimpleEvent(result);
    } finally {
      ExecutionTime.stop(ignored);
    }
  }

  @Override
  public String getName() {
    return "not";
  }

  @Override
  public String getShortDescription() {
    return "Negates a stochastic pattern.";
  }
}
