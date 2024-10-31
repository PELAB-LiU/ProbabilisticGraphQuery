package reliability.events.delayed;

import com.google.common.collect.HashMultiset;
import hu.bme.mit.delta.mdd.MddHandle;
import reliability.events.Event;
import reliability.mdd.MddModel;

@SuppressWarnings("all")
public class DelayedNotEvent extends DelayedOrEvent {
  public DelayedNotEvent(final HashMultiset<Event> template) {
    super(template);
  }

  public DelayedNotEvent(final DelayedNotEvent template) {
    super(template);
  }

  @Override
  public MddHandle calculateEvent() {
    final MddHandle tmp = super.calculateEvent();
    final MddHandle handle = MddModel.INSTANCE.getHandleOf(true).minus(tmp);
    return handle;
  }

  @Override
  public boolean equals(final Object other) {
    return ((other instanceof DelayedNotEvent) && super.equals(other));
  }
}
