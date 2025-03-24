package se.liu.ida.sas.pelab.storm.transformation;

import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class StormGeneration {
  public static String topEvent(final String name) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("toplevel \"");
    _builder.append(name);
    _builder.append("\";");
    return _builder.toString();
  }

  public static String basicEvent(final String name, final double probability) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\"");
    _builder.append(name);
    _builder.append("\" prob=");
    _builder.append(probability);
    _builder.append(" dorm=1;");
    return _builder.toString();
  }

  public static String orGate(final String name, final String... inputs) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\"");
    _builder.append(name);
    _builder.append("\" or ");
    {
      boolean _hasElements = false;
      for(final String arg : inputs) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(" ", "");
        }
        _builder.append("\"");
        _builder.append(arg);
        _builder.append("\"");
      }
    }
    _builder.append(";");
    return _builder.toString();
  }

  public static String andGate(final String name, final String... inputs) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\"");
    _builder.append(name);
    _builder.append("\" and ");
    {
      boolean _hasElements = false;
      for(final String arg : inputs) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(" ", "");
        }
        _builder.append("\"");
        _builder.append(arg);
        _builder.append("\"");
      }
    }
    _builder.append(";");
    return _builder.toString();
  }
}
