package reliability.intreface;

@SuppressWarnings("all")
public class CancellationException extends RuntimeException {
  public CancellationException() {
    super("MDD operations are cancelled.");
  }
}
