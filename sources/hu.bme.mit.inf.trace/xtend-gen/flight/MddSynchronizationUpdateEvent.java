package flight;

import jdk.jfr.Event;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class MddSynchronizationUpdateEvent extends Event {
  @Accessors
  private int traces = 0;

  @Pure
  public int getTraces() {
    return this.traces;
  }

  public void setTraces(final int traces) {
    this.traces = traces;
  }
}
