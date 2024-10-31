package flight;

import jdk.jfr.Event;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Events for reliability.cache.ReliabilityCacheManager
 */
@SuppressWarnings("all")
public class AccessSessionCacheEvent extends Event {
  @Accessors
  private boolean cached = true;

  @Pure
  public boolean isCached() {
    return this.cached;
  }

  public void setCached(final boolean cached) {
    this.cached = cached;
  }
}
