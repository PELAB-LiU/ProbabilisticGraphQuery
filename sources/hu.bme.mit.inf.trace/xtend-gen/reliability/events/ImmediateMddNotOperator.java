package reliability.events;

import com.google.common.collect.HashMultiset;
import flight.NotGetAggregateEvent;
import flight.NotUpdateEvent;
import hu.bme.mit.delta.mdd.MddHandle;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import reliability.intreface.Configuration;
import reliability.intreface.ExecutionTime;
import reliability.mdd.MddModel;

@SuppressWarnings("all")
public class ImmediateMddNotOperator extends MddNotOperator<CachedBuffer<HashMultiset<Event>, Event>> {
  @Override
  public CachedBuffer<HashMultiset<Event>, Event> createNeutral() {
    HashMultiset<Event> _create = HashMultiset.<Event>create();
    return new CachedBuffer<HashMultiset<Event>, Event>(_create);
  }

  /**
   * Example:
   * e(a,0):A,e(a,1):B,e(a,2):C
   * -------------NEG-------------
   * !e(a,0):!A,!e(a,1):!B,!e(a,2):!C
   * -------------AGG-------------
   * e(a,_):A|B|C
   * !e(a,_):!(A|B|C)
   */
  @Override
  public Event getAggregate(final CachedBuffer<HashMultiset<Event>, Event> buffer) {
    boolean ignored = false;
    final NotGetAggregateEvent event = new NotGetAggregateEvent();
    event.begin();
    try {
      ignored = ExecutionTime.start();
      boolean _isCancelled = Configuration.isCancelled();
      if (_isCancelled) {
        return null;
      }
      final Function1<HashMultiset<Event>, Event> _function = (HashMultiset<Event> data) -> {
        SimpleEvent _xblockexpression = null;
        {
          final Function2<MddHandle, Event, MddHandle> _function_1 = (MddHandle accu, Event entry) -> {
            return accu.union(entry.getHandle());
          };
          final MddHandle handle = IterableExtensions.<Event, MddHandle>fold(data.elementSet(), MddModel.INSTANCE.getHandleOf(false), _function_1);
          MddHandle _minus = MddModel.INSTANCE.getHandleOf(true).minus(handle);
          _xblockexpression = new SimpleEvent(_minus);
        }
        return _xblockexpression;
      };
      final Event result = buffer.cacheOrCompute(_function);
      return result;
    } finally {
      ExecutionTime.stop(ignored);
      event.commit();
    }
  }

  @Override
  public boolean isNeutral(final CachedBuffer<HashMultiset<Event>, Event> buffer) {
    return buffer.getBuffer().isEmpty();
  }

  @Override
  public CachedBuffer<HashMultiset<Event>, Event> update(final CachedBuffer<HashMultiset<Event>, Event> buffer, final Event updateValue, final boolean isInsertion) {
    boolean ignored = false;
    final NotUpdateEvent event = new NotUpdateEvent();
    event.begin();
    try {
      ignored = ExecutionTime.start();
      if (isInsertion) {
        buffer.getBuffer().add(updateValue);
      } else {
        buffer.getBuffer().remove(updateValue);
      }
      buffer.invalidate();
      return buffer;
    } finally {
      ExecutionTime.stop(ignored);
      event.commit();
    }
  }
}
