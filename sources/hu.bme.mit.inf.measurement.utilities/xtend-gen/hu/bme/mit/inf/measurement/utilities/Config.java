package hu.bme.mit.inf.measurement.utilities;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;

@SuppressWarnings("all")
public class Config extends HashMap<String, Param> {
  public String asString(final String key) {
    String _xblockexpression = null;
    {
      final Param value = this.get(key);
      if ((value == null)) {
        throw new IllegalArgumentException((("hu.bme.mit.inf.measurement.utilities.Config.get(" + key) + ") is null. Unable to cast to string."));
      }
      _xblockexpression = value.asString();
    }
    return _xblockexpression;
  }

  public int asInt(final String key) {
    int _xblockexpression = (int) 0;
    {
      final Param value = this.get(key);
      if ((value == null)) {
        throw new IllegalArgumentException((("hu.bme.mit.inf.measurement.utilities.Config.get(" + key) + ") is null. Unable to cast to int."));
      }
      _xblockexpression = value.asInt();
    }
    return _xblockexpression;
  }

  public double asDouble(final String key) {
    double _xblockexpression = (double) 0;
    {
      final Param value = this.get(key);
      if ((value == null)) {
        throw new IllegalArgumentException((("hu.bme.mit.inf.measurement.utilities.Config.get(" + key) + ") is null. Unable to cast to double."));
      }
      _xblockexpression = value.asDouble();
    }
    return _xblockexpression;
  }

  public boolean asBoolean(final String key) {
    boolean _xblockexpression = false;
    {
      final Param value = this.get(key);
      if ((value == null)) {
        throw new IllegalArgumentException((("hu.bme.mit.inf.measurement.utilities.Config.get(" + key) + ") is null. Unable to cast to boolean."));
      }
      _xblockexpression = value.asBoolean();
    }
    return _xblockexpression;
  }

  public boolean isDefined(final String key) {
    return this.containsKey(key);
  }

  public Config(final String[] args) {
    for (final String arg : args) {
      boolean _startsWith = arg.startsWith("#");
      boolean _not = (!_startsWith);
      if (_not) {
        final String[] values = arg.split("=", 2);
        int _size = ((List<String>)Conversions.doWrapArray(values)).size();
        boolean _tripleEquals = (_size == 2);
        if (_tripleEquals) {
          String _get = values[0];
          String _get_1 = values[1];
          final Param param = new Param(_get, _get_1);
          this.put(param.getKey(), param);
        } else {
          String _get_2 = values[0];
          final Param param_1 = new Param(_get_2, "true");
          this.put(param_1.getKey(), param_1);
        }
      }
    }
  }

  public static Timer kill(final long sec) {
    final Timer timer = new Timer(true);
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        System.exit(0);
      }
    }, 
      (sec * 1000));
    return timer;
  }

  public static Timer timeout(final long sec, final Procedure0 then) {
    final Timer timer = new Timer(true);
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        then.apply();
      }
    }, 
      (sec * 1000));
    return timer;
  }

  public static void gc(final long delay) {
    try {
      System.gc();
      Thread.sleep(delay);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
