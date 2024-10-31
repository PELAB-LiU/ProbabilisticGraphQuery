package reliability.mdd;

import com.google.common.collect.Multiset;
import hu.bme.mit.delta.mdd.MddHandle;

/**
 * Interface to hide implementation of data source for KOf operation
 */
@SuppressWarnings("all")
public interface MddHandleCollection {
  Multiset<MddHandle> getMultiset();
}
