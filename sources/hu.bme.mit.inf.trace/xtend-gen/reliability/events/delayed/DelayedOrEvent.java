package reliability.events.delayed;

import com.google.common.collect.HashMultiset;
import flight.WrapperEqualsEvent;
import hu.bme.mit.delta.mdd.MddHandle;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import reliability.events.Event;
import reliability.events.ModifiableEvent;
import reliability.mdd.MddModel;

@SuppressWarnings("all")
public class DelayedOrEvent extends AbstractDelayedEvent implements ModifiableEvent {
  private final HashMultiset<Event> events;

  public DelayedOrEvent(final HashMultiset<Event> template) {
    this.events = HashMultiset.<Event>create(template);
  }

  public DelayedOrEvent(final DelayedOrEvent template) {
    super(template);
    this.events = HashMultiset.<Event>create(template.events);
  }

  @Override
  public MddHandle calculateEvent() {
    final Function2<MddHandle, Event, MddHandle> _function = (MddHandle acc, Event current) -> {
      return acc.union(current.getHandle());
    };
    MddHandle temp = IterableExtensions.<Event, MddHandle>fold(this.events.elementSet(), 
      MddModel.INSTANCE.getHandleOf(false), _function);
    return temp;
  }

  @Override
  public void add(final Event event) {
    boolean _contains = this.events.contains(event);
    boolean _not = (!_contains);
    if (_not) {
      this.invalidateHandle();
    }
    this.events.add(event);
  }

  @Override
  public void remove(final Event event) {
    this.events.remove(event);
    boolean _contains = this.events.contains(event);
    boolean _not = (!_contains);
    if (_not) {
      this.invalidateHandle();
    }
  }

  @Override
  public boolean equals(final Object other) {
    final WrapperEqualsEvent event = new WrapperEqualsEvent();
    event.setType(this.getClass().getSimpleName());
    event.setOther(other.getClass().getSimpleName());
    event.begin();
    boolean res = false;
    if ((other instanceof DelayedOrEvent)) {
      if ((this.isValid() && ((DelayedOrEvent)other).isValid())) {
        res = this.getHandle().equals(((DelayedOrEvent)other).getHandle());
      } else {
        res = this.events.elementSet().equals(((DelayedOrEvent)other).events.elementSet());
      }
    } else {
      res = super.equals(other);
    }
    event.commit();
    return res;
  }

  @Override
  public int hashCode() {
    return this.events.hashCode();
  }
}
