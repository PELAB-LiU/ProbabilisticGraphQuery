package reliability.cache;

@SuppressWarnings("all")
public interface ReliabilityCacheEntry {
  double getProbability();

  boolean isValid();
}
