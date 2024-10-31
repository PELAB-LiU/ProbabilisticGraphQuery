package reliability.cache;

import hu.bme.mit.delta.mdd.MddHandle;
import hu.bme.mit.delta.mdd.MddVariable;

@SuppressWarnings("all")
public class NoCacheManager implements ReliabilityCacheManager {
  public static class NullReliabilityNode implements ReliabilityCacheEntry {
    @Override
    public double getProbability() {
      return Double.NaN;
    }

    @Override
    public boolean isValid() {
      return false;
    }
  }

  public static final NoCacheManager INSTANCE = new NoCacheManager();

  private final NoCacheManager.NullReliabilityNode invariant = new NoCacheManager.NullReliabilityNode();

  @Override
  public ReliabilityCacheEntry createNode(final MddHandle node, final int childrens) {
    return this.invariant;
  }

  @Override
  public ReliabilityCacheEntry getOrCreateNode(final MddHandle node, final int childrens) {
    return this.invariant;
  }

  @Override
  public void updateNode(final MddHandle node, final double probability) {
  }

  @Override
  public void invalidateNode(final MddHandle node) {
  }

  @Override
  public void removeNode(final MddHandle node) {
  }

  @Override
  public void updateVariable(final MddVariable variable) {
  }
}
