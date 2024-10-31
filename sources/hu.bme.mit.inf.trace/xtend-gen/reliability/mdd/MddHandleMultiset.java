package reliability.mdd;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import hu.bme.mit.delta.mdd.MddHandle;

@SuppressWarnings("all")
public class MddHandleMultiset implements MddHandleCollection {
  private final HashMultiset<MddHandle> data = HashMultiset.<MddHandle>create();

  public MddHandleMultiset() {
  }

  public MddHandleMultiset(final HashMultiset<MddHandle> data) {
    this.data.addAll(data);
  }

  @Override
  public Multiset<MddHandle> getMultiset() {
    return this.data;
  }

  @Override
  public int hashCode() {
    return this.data.hashCode();
  }

  @Override
  public boolean equals(final Object other) {
    if ((other instanceof MddHandleMultiset)) {
      return this.data.equals(((MddHandleMultiset)other).data);
    }
    return false;
  }
}
