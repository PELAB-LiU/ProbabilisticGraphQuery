package reliability.events;

import hu.bme.mit.delta.mdd.MddHandle;

@SuppressWarnings("all")
public interface Event {
  MddHandle getHandle();
}
