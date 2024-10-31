package reliability.intreface;

@SuppressWarnings("all")
public class ExecutionTime {
  private static long time = 0;

  private static boolean running = false;

  private static long start = 0;

  public static boolean start() {
    if (ExecutionTime.running) {
      return true;
    } else {
      ExecutionTime.start = System.nanoTime();
      ExecutionTime.running = true;
      return false;
    }
  }

  public static boolean stop(final boolean ignore) {
    boolean _xifexpression = false;
    if ((ExecutionTime.running && (!ignore))) {
      boolean _xblockexpression = false;
      {
        long _time = ExecutionTime.time;
        long _nanoTime = System.nanoTime();
        long _minus = (_nanoTime - ExecutionTime.start);
        ExecutionTime.time = (_time + _minus);
        _xblockexpression = ExecutionTime.running = false;
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }

  public static long time() {
    return ExecutionTime.time;
  }

  public static void reset() {
    ExecutionTime.time = 0;
    ExecutionTime.running = false;
  }
}
