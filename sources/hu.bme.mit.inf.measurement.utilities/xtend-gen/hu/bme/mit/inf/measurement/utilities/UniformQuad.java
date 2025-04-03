package hu.bme.mit.inf.measurement.utilities;

import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class UniformQuad<T1 extends Object> extends Quad<T1, T1, T1, T1> {
  public UniformQuad(final T1 fisrt, final T1 second, final T1 third, final T1 forth) {
    super(fisrt, second, third, forth);
  }

  @Override
  @Pure
  public int hashCode() {
    return 1;
  }

  @Override
  @Pure
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    return true;
  }

  @Override
  @Pure
  public String toString() {
    return new ToStringBuilder(this)
    	.addAllFields()
    	.toString();
  }
}
