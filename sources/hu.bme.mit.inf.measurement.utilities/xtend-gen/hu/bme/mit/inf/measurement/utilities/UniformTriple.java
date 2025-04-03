package hu.bme.mit.inf.measurement.utilities;

import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class UniformTriple<T1 extends Object> extends Triple<T1, T1, T1> {
  public UniformTriple(final T1 fisrt, final T1 second, final T1 third) {
    super(fisrt, second, third);
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
