package reliability.events.delayed;

import flight.WrapperCalculationEvent;
import hu.bme.mit.delta.mdd.MddHandle;
import reliability.events.Event;
import reliability.intreface.Configuration;
import reliability.intreface.ExecutionTime;

@SuppressWarnings("all")
public abstract class AbstractDelayedEvent implements Event {
  private MddHandle event;

  public AbstractDelayedEvent() {
  }

  public AbstractDelayedEvent(final AbstractDelayedEvent template) {
    this.event = template.event;
  }

  @Override
  public MddHandle getHandle() {
    boolean ignored = false;
    try {
      ignored = ExecutionTime.start();
      Configuration.abortIfCancelled();
      if ((this.event == null)) {
        final WrapperCalculationEvent event = new WrapperCalculationEvent();
        event.begin();
        this.event = this.calculateEvent();
        event.setType(this.getClass().getSimpleName());
        event.commit();
      }
      return this.event;
    } finally {
      ExecutionTime.stop(ignored);
    }
  }

  public abstract MddHandle calculateEvent();

  public boolean isValid() {
    return (this.event != null);
  }

  public void invalidateHandle() {
    this.event = null;
  }

  @Override
  public int hashCode() {
    return this.getHandle().hashCode();
  }

  @Override
  public boolean equals(final Object other) {
    if ((other instanceof Event)) {
      final boolean res = this.getHandle().equals(((Event)other).getHandle());
      return res;
    }
    return false;
  }
}
