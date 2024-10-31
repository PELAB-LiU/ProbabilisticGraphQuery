package reliability.events;

import org.eclipse.xtext.xbase.lib.Functions.Function1;

@SuppressWarnings("all")
public class CachedBuffer<B extends Object, C extends Object> {
  private final B buffer;

  private C cache;

  private boolean valid;

  public CachedBuffer(final B buffer) {
    this.buffer = buffer;
    this.valid = false;
  }

  public B getBuffer() {
    return this.buffer;
  }

  public C getLastCache() {
    return this.cache;
  }

  public C cacheOrCompute(final Function1<B, C> update) {
    if ((!this.valid)) {
      this.cache = update.apply(this.buffer);
      this.valid = true;
    }
    return this.cache;
  }

  public boolean invalidate() {
    return this.valid = false;
  }
}
