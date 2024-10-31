package reliability.intreface;

@SuppressWarnings("all")
public class Configuration {
  private static volatile boolean cancelled = false;

  public static void abortIfCancelled() {
    if (Configuration.cancelled) {
      throw new CancellationException();
    }
  }

  public static boolean isCancelled() {
    return Configuration.cancelled;
  }

  public static void cancel() {
    Configuration.cancelled = true;
  }

  public static void enable() {
    Configuration.cancelled = false;
  }
}
