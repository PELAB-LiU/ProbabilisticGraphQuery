package reliability.events.delayed;

import com.google.common.collect.HashMultiset;
import flight.OrGetAggregateEvent;
import flight.OrUpdateEvent;
import reliability.events.Event;
import reliability.events.MddOrOperator;
import reliability.intreface.Configuration;
import reliability.intreface.ExecutionTime;

/**
 * Set may need to be changed due to  multiplicity reasons
 */
@SuppressWarnings("all")
public class DelayedMddOrOperator extends MddOrOperator<DelayedOrEvent> {
  private static final DelayedOrEvent neutral = new DelayedOrEvent(HashMultiset.<Event>create());

  @Override
  public DelayedOrEvent createNeutral() {
    return new DelayedOrEvent(DelayedMddOrOperator.neutral);
  }

  @Override
  public Event getAggregate(final DelayedOrEvent result) {
    boolean ignored = false;
    final OrGetAggregateEvent event = new OrGetAggregateEvent();
    event.begin();
    try {
      ignored = ExecutionTime.start();
      boolean _isCancelled = Configuration.isCancelled();
      if (_isCancelled) {
        return null;
      }
      final DelayedOrEvent res = new DelayedOrEvent(result);
      return res;
    } finally {
      ExecutionTime.stop(ignored);
      event.commit();
    }
  }

  @Override
  public boolean isNeutral(final DelayedOrEvent result) {
    return result.equals(DelayedMddOrOperator.neutral);
  }

  @Override
  public DelayedOrEvent update(final DelayedOrEvent oldResult, final Event updateValue, final boolean isInsertion) {
    boolean ignored = false;
    final OrUpdateEvent event = new OrUpdateEvent();
    event.begin();
    try {
      ignored = ExecutionTime.start();
      if (isInsertion) {
        oldResult.add(updateValue);
      } else {
        oldResult.remove(updateValue);
      }
      return oldResult;
    } finally {
      ExecutionTime.stop(ignored);
      event.commit();
    }
  }
}
