package flight;

import jdk.jfr.Event;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Events for stochastic event wrappers
 */
@SuppressWarnings("all")
public class WrapperCalculationEvent extends Event {
  @Accessors
  private String type;

  @Pure
  public String getType() {
    return this.type;
  }

  public void setType(final String type) {
    this.type = type;
  }
}
