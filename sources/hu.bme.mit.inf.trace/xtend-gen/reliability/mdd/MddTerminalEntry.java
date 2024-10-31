package reliability.mdd;

import hu.bme.mit.delta.mdd.MddHandle;
import hu.bme.mit.delta.mdd.MddVariable;
import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

@Data
@SuppressWarnings("all")
public class MddTerminalEntry {
  private final MddVariable _variable;

  private final MddHandle _handle;

  public MddTerminalEntry(final MddVariable variable, final MddHandle handle) {
    super();
    this._variable = variable;
    this._handle = handle;
  }

  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this._variable== null) ? 0 : this._variable.hashCode());
    return prime * result + ((this._handle== null) ? 0 : this._handle.hashCode());
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
    MddTerminalEntry other = (MddTerminalEntry) obj;
    if (this._variable == null) {
      if (other._variable != null)
        return false;
    } else if (!this._variable.equals(other._variable))
      return false;
    if (this._handle == null) {
      if (other._handle != null)
        return false;
    } else if (!this._handle.equals(other._handle))
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
  public MddVariable getVariable() {
    return this._variable;
  }

  @Pure
  public MddHandle getHandle() {
    return this._handle;
  }
}
