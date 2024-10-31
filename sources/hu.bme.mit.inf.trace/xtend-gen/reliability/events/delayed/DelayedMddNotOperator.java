package reliability.events.delayed;

import com.google.common.collect.HashMultiset;
import flight.NotGetAggregateEvent;
import flight.NotUpdateEvent;
import reliability.events.Event;
import reliability.events.MddNotOperator;
import reliability.intreface.Configuration;
import reliability.intreface.ExecutionTime;

@SuppressWarnings("all")
public class DelayedMddNotOperator extends MddNotOperator<DelayedNotEvent> {
  private static final DelayedNotEvent neutral = new DelayedNotEvent(HashMultiset.<Event>create());

  @Override
  public DelayedNotEvent createNeutral() {
    return new DelayedNotEvent(DelayedMddNotOperator.neutral);
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
  public Event getAggregate(final DelayedNotEvent result) {
    boolean ignored = false;
    final NotGetAggregateEvent event = new NotGetAggregateEvent();
    event.begin();
    try {
      ignored = ExecutionTime.start();
      boolean _isCancelled = Configuration.isCancelled();
      if (_isCancelled) {
        return null;
      }
      final DelayedNotEvent res = new DelayedNotEvent(result);
      return res;
    } finally {
      ExecutionTime.stop(ignored);
      event.commit();
    }
  }

  @Override
  public boolean isNeutral(final DelayedNotEvent result) {
    return result.equals(DelayedMddNotOperator.neutral);
  }

  @Override
  public DelayedNotEvent update(final DelayedNotEvent oldResult, final Event updateValue, final boolean isInsertion) {
    boolean ignored = false;
    final NotUpdateEvent event = new NotUpdateEvent();
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
