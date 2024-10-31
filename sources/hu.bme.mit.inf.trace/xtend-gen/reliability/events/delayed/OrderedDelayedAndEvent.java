package reliability.events.delayed;

import flight.WrapperEqualsEvent;
import hu.bme.mit.delta.mdd.MddHandle;
import java.util.Arrays;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import reliability.events.Event;
import reliability.mdd.MddModel;

@SuppressWarnings("all")
public class OrderedDelayedAndEvent extends AbstractDelayedEvent {
  private final Event[] events;

  public OrderedDelayedAndEvent(final Event... events) {
    this.events = events;
  }

  @Override
  public MddHandle calculateEvent() {
    final Function2<MddHandle, Event, MddHandle> _function = (MddHandle acc, Event current) -> {
      return acc.intersection(current.getHandle());
    };
    final MddHandle handle = IterableExtensions.<Event, MddHandle>fold(((Iterable<Event>)Conversions.doWrapArray(this.events)), 
      MddModel.INSTANCE.getHandleOf(true), _function);
    return handle;
  }

  @Override
  public boolean equals(final Object other) {
    final WrapperEqualsEvent event = new WrapperEqualsEvent();
    event.setType(this.getClass().getSimpleName());
    event.setOther(other.getClass().getSimpleName());
    event.begin();
    boolean res = false;
    if ((other instanceof OrderedDelayedAndEvent)) {
      if ((this.isValid() && ((OrderedDelayedAndEvent)other).isValid())) {
        res = this.getHandle().equals(((OrderedDelayedAndEvent)other).getHandle());
      } else {
        res = ((this.events.length == ((OrderedDelayedAndEvent)other).events.length) && Arrays.deepEquals(this.events, ((OrderedDelayedAndEvent)other).events));
      }
    } else {
      res = super.equals(other);
    }
    event.commit();
    return res;
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(this.events);
  }
}
