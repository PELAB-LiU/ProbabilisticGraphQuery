package flight;

import jdk.jfr.Event;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class WrapperEqualsEvent extends Event {
  @Accessors
  private String type;

  @Accessors
  private String other;

  @Pure
  public String getType() {
    return this.type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  @Pure
  public String getOther() {
    return this.other;
  }

  public void setOther(final String other) {
    this.other = other;
  }
}
