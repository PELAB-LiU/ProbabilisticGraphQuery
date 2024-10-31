package hu.bme.mit.inf.querytransformation.query;

import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

@Data
@SuppressWarnings("all")
public class TransformationInfo {
  private final CharSequence _body;

  private final int _eventcount;

  public TransformationInfo(final CharSequence body, final int eventcount) {
    super();
    this._body = body;
    this._eventcount = eventcount;
  }

  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this._body== null) ? 0 : this._body.hashCode());
    return prime * result + this._eventcount;
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
    TransformationInfo other = (TransformationInfo) obj;
    if (this._body == null) {
      if (other._body != null)
        return false;
    } else if (!this._body.equals(other._body))
      return false;
    if (other._eventcount != this._eventcount)
      return false;
    return true;
  }

  @Override
  @Pure
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }

  @Pure
  public CharSequence getBody() {
    return this._body;
  }

  @Pure
  public int getEventcount() {
    return this._eventcount;
  }
}
