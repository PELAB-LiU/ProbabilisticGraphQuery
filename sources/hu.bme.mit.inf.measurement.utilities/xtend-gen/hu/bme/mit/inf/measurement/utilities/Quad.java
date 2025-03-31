package hu.bme.mit.inf.measurement.utilities;

import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class Quad<T1 extends Object, T2 extends Object, T3 extends Object, T4 extends Object> {
  private final T1 fisrt;

  private final T2 second;

  private final T3 third;

  private final T4 forth;

  public Quad(final T1 fisrt, final T2 second, final T3 third, final T4 forth) {
    super();
    this.fisrt = fisrt;
    this.second = second;
    this.third = third;
    this.forth = forth;
  }

  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.fisrt== null) ? 0 : this.fisrt.hashCode());
    result = prime * result + ((this.second== null) ? 0 : this.second.hashCode());
    result = prime * result + ((this.third== null) ? 0 : this.third.hashCode());
    return prime * result + ((this.forth== null) ? 0 : this.forth.hashCode());
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
    Quad<?, ?, ?, ?> other = (Quad<?, ?, ?, ?>) obj;
    if (this.fisrt == null) {
      if (other.fisrt != null)
        return false;
    } else if (!this.fisrt.equals(other.fisrt))
      return false;
    if (this.second == null) {
      if (other.second != null)
        return false;
    } else if (!this.second.equals(other.second))
      return false;
    if (this.third == null) {
      if (other.third != null)
        return false;
    } else if (!this.third.equals(other.third))
      return false;
    if (this.forth == null) {
      if (other.forth != null)
        return false;
    } else if (!this.forth.equals(other.forth))
      return false;
    return true;
  }

  @Override
  @Pure
  public String toString() {
    ToStringBuilder b = new ToStringBuilder(this);
    b.add("fisrt", this.fisrt);
    b.add("second", this.second);
    b.add("third", this.third);
    b.add("forth", this.forth);
    return b.toString();
  }

  @Pure
  public T1 getFisrt() {
    return this.fisrt;
  }

  @Pure
  public T2 getSecond() {
    return this.second;
  }

  @Pure
  public T3 getThird() {
    return this.third;
  }

  @Pure
  public T4 getForth() {
    return this.forth;
  }
}
