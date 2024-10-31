package reliability.cache;

import hu.bme.mit.delta.mdd.MddHandle;
import hu.bme.mit.delta.mdd.MddVariable;

@SuppressWarnings("all")
public interface ReliabilityCacheManager {
  ReliabilityCacheEntry createNode(final MddHandle node, final int childrens);

  ReliabilityCacheEntry getOrCreateNode(final MddHandle node, final int childrens);

  void updateNode(final MddHandle node, final double probability);

  void updateVariable(final MddVariable variable);

  void invalidateNode(final MddHandle node);

  void removeNode(final MddHandle node);
}
