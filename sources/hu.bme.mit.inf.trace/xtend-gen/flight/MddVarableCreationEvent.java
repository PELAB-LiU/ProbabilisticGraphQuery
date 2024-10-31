package flight;

import jdk.jfr.Event;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Events for reliability.mdd.MddModel.xtend
 */
@SuppressWarnings("all")
public class MddVarableCreationEvent extends Event {
  @Accessors
  private boolean recycled = false;

  @Pure
  public boolean isRecycled() {
    return this.recycled;
  }

  public void setRecycled(final boolean recycled) {
    this.recycled = recycled;
  }
}
