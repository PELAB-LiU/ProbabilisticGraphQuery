package hu.bme.mit.inf.measurement.utilities;

@SuppressWarnings("all")
public class Benchmark {
  private static long time = 0;

  private static long start;

  private static int depth;

  public static int start() {
    int _xblockexpression = (int) 0;
    {
      if ((Benchmark.depth == 0)) {
        Benchmark.start = System.nanoTime();
      }
      _xblockexpression = Benchmark.depth++;
    }
    return _xblockexpression;
  }

  public static long stop() {
    long _xblockexpression = (long) 0;
    {
      Benchmark.depth--;
      long _xifexpression = (long) 0;
      if ((Benchmark.depth == 0)) {
        long _xblockexpression_1 = (long) 0;
        {
          final long end = System.nanoTime();
          long _time = Benchmark.time;
          _xblockexpression_1 = Benchmark.time = (_time + (end - Benchmark.start));
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }

  public static long getTime() {
    return Benchmark.time;
  }
}
