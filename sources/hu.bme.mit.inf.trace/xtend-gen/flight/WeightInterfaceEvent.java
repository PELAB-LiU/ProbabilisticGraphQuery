package flight;

import jdk.jfr.Event;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class WeightInterfaceEvent extends Event {
  @Accessors
  private int size = 0;

  @Pure
  public int getSize() {
    return this.size;
  }

  public void setSize(final int size) {
    this.size = size;
  }
}
