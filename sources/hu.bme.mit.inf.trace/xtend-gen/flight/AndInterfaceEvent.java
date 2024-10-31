package flight;

import jdk.jfr.Event;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Events for reliability.interface.D.java
 */
@SuppressWarnings("all")
public class AndInterfaceEvent extends Event {
  @Accessors
  private int argc = 0;

  @Pure
  public int getArgc() {
    return this.argc;
  }

  public void setArgc(final int argc) {
    this.argc = argc;
  }
}
