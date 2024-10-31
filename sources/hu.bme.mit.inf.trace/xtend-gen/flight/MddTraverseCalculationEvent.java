package flight;

import jdk.jfr.Event;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class MddTraverseCalculationEvent extends Event {
  @Accessors
  private boolean cached = false;

  @Pure
  public boolean isCached() {
    return this.cached;
  }

  public void setCached(final boolean cached) {
    this.cached = cached;
  }
}
