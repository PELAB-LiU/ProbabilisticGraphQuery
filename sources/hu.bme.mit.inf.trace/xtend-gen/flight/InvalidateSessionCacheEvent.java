package flight;

import jdk.jfr.Event;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class InvalidateSessionCacheEvent extends Event {
  @Accessors
  private boolean all = false;

  @Pure
  public boolean isAll() {
    return this.all;
  }

  public void setAll(final boolean all) {
    this.all = all;
  }
}
