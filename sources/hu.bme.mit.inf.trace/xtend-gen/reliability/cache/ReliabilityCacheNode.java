package reliability.cache;

import java.util.List;
import java.util.function.Consumer;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class ReliabilityCacheNode implements ReliabilityCacheEntry {
  @Accessors(AccessorType.PACKAGE_GETTER)
  private final List<ReliabilityCacheNode> affects = CollectionLiterals.<ReliabilityCacheNode>newArrayList();

  @Accessors(AccessorType.PUBLIC_GETTER)
  private double probability = 0.0;

  @Accessors(AccessorType.PUBLIC_GETTER)
  private boolean valid = false;

  public void update(final double value) {
    this.invalidate();
    this.valid = true;
    this.probability = value;
  }

  public void invalidate() {
    if (this.valid) {
      this.valid = false;
      final Consumer<ReliabilityCacheNode> _function = (ReliabilityCacheNode node) -> {
        node.invalidate();
      };
      this.affects.forEach(_function);
    }
  }

  @Pure
  List<ReliabilityCacheNode> getAffects() {
    return this.affects;
  }

  @Pure
  @Override
  public double getProbability() {
    return this.probability;
  }

  @Pure
  @Override
  public boolean isValid() {
    return this.valid;
  }
}
