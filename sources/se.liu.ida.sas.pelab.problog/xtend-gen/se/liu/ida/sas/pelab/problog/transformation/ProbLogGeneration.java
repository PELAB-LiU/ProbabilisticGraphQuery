package se.liu.ida.sas.pelab.problog.transformation;

import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;

@SuppressWarnings("all")
public class ProbLogGeneration {
  public static CharSequence basicEvent(final double probability, final String type, final String... names) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(probability);
    _builder.append("::");
    _builder.append(type);
    {
      int _size = ((List<String>)Conversions.doWrapArray(names)).size();
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        _builder.append("(");
        {
          boolean _hasElements = false;
          for(final String arg : names) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(",", "");
            }
            _builder.append(arg);
          }
        }
        _builder.append(")");
      }
    }
    _builder.append(".");
    _builder.newLineIfNotEmpty();
    return _builder;
  }

  public static CharSequence dfact(final String type, final String... names) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(type);
    {
      int _size = ((List<String>)Conversions.doWrapArray(names)).size();
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        _builder.append("(");
        {
          boolean _hasElements = false;
          for(final String arg : names) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(",", "");
            }
            _builder.append(arg);
          }
        }
        _builder.append(")");
      }
    }
    return _builder;
  }
}
