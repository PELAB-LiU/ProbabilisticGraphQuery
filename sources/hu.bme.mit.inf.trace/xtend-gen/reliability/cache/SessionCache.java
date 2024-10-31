package reliability.cache;

import flight.AccessSessionCacheEvent;
import flight.InvalidateSessionCacheEvent;
import hu.bme.mit.delta.mdd.MddHandle;
import hu.bme.mit.delta.mdd.MddVariable;
import java.util.Map;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class SessionCache implements ReliabilityCacheManager {
  private final Map<MddHandle, ReliabilityCacheNode> cachedHandles = CollectionLiterals.<MddHandle, ReliabilityCacheNode>newHashMap();

  @Override
  public ReliabilityCacheEntry createNode(final MddHandle node, final int childrens) {
    boolean _isTerminal = node.isTerminal();
    if (_isTerminal) {
      throw new IllegalArgumentException("Terminal node cannot be cached.");
    }
    boolean _containsKey = this.cachedHandles.containsKey(node);
    if (_containsKey) {
      throw new IllegalArgumentException("Handle is already cached.");
    }
    final ReliabilityCacheNode newNode = new ReliabilityCacheNode();
    this.cachedHandles.put(node, newNode);
    return newNode;
  }

  @Override
  public ReliabilityCacheEntry getOrCreateNode(final MddHandle node, final int childrens) {
    final AccessSessionCacheEvent event = new AccessSessionCacheEvent();
    event.begin();
    final ReliabilityCacheNode cached = this.cachedHandles.get(node);
    if ((cached == null)) {
      event.setCached(false);
      final ReliabilityCacheEntry res = this.createNode(node, childrens);
      event.commit();
      return res;
    }
    event.commit();
    return cached;
  }

  @Override
  public void updateNode(final MddHandle node, final double probability) {
    ReliabilityCacheNode _get = this.cachedHandles.get(node);
    if (_get!=null) {
      _get.update(probability);
    }
  }

  @Override
  public void updateVariable(final MddVariable variable) {
  }

  @Override
  public void invalidateNode(final MddHandle node) {
    final InvalidateSessionCacheEvent event = new InvalidateSessionCacheEvent();
    event.setAll((node == null));
    event.begin();
    if ((node == null)) {
      this.cachedHandles.clear();
    } else {
      this.cachedHandles.remove(node);
    }
    event.commit();
  }

  @Override
  public void removeNode(final MddHandle node) {
  }
}
