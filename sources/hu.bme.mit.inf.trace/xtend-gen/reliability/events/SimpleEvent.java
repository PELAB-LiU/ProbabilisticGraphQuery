package reliability.events;

import hu.bme.mit.delta.mdd.MddHandle;

@SuppressWarnings("all")
public class SimpleEvent implements Event {
  private final MddHandle event;

  public SimpleEvent(final MddHandle event) {
    this.event = event;
  }

  @Override
  public MddHandle getHandle() {
    return this.event;
  }

  @Override
  public boolean equals(final Object other) {
    if ((other instanceof Event)) {
      return this.event.equals(((Event)other).getHandle());
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return this.getHandle().hashCode();
  }
}
