package reliability.events;

import com.google.common.collect.HashMultiset;
import flight.CollectorGetAggregateEvent;
import flight.CollectorUpdateEvent;
import hu.bme.mit.delta.mdd.MddHandle;
import reliability.intreface.Configuration;
import reliability.intreface.ExecutionTime;
import reliability.mdd.MddHandleMultiset;

@SuppressWarnings("all")
public class ImmediateMddCollectorOperator extends MddCollectorOperator {
  @Override
  public MddHandleMultiset getAggregate(final HashMultiset<MddHandle> result) {
    boolean ignored = false;
    final CollectorGetAggregateEvent event = new CollectorGetAggregateEvent();
    event.begin();
    try {
      ignored = ExecutionTime.start();
      boolean _isCancelled = Configuration.isCancelled();
      if (_isCancelled) {
        return null;
      }
      if (((result == null) || this.isNeutral(result))) {
        return null;
      }
      final MddHandleMultiset res = new MddHandleMultiset(result);
      return res;
    } finally {
      ExecutionTime.stop(ignored);
      event.commit();
    }
  }

  @Override
  public HashMultiset<MddHandle> update(final HashMultiset<MddHandle> oldResult, final Event updateValue, final boolean isInsertion) {
    boolean ignored = false;
    final CollectorUpdateEvent event = new CollectorUpdateEvent();
    event.begin();
    try {
      ignored = ExecutionTime.start();
      if (isInsertion) {
        oldResult.add(updateValue.getHandle());
      } else {
        oldResult.remove(updateValue.getHandle());
      }
      return oldResult;
    } finally {
      ExecutionTime.stop(ignored);
      event.commit();
    }
  }
}
