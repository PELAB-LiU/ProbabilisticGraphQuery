package hu.bme.mit.inf.measurement.utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.ArrayExtensions;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
public class CSVLog {
  private static final Logger LOG4J = LoggerFactory.getLogger(CSVLog.class);

  private final String[] columns;

  private final String separator;

  private final Queue<Map<String, String>> logs;

  private final Map<String, Object> current;

  public CSVLog(final String[] columns, final String separator) {
    this.columns = columns;
    this.separator = separator;
    this.logs = CollectionLiterals.<Map<String, String>>newLinkedList();
    this.current = CollectionLiterals.<String, Object>newHashMap();
  }

  public Object log(final String key, final Object object) {
    CSVLog.LOG4J.info("CSVSET {} --> {}", key, object);
    boolean _contains = ArrayExtensions.contains(this.columns, key);
    boolean _not = (!_contains);
    if (_not) {
      CSVLog.LOG4J.warn("Unlogged value for key. {}", key);
    }
    return this.current.put(key, object);
  }

  public void commit() {
    final HashMap<String, String> entry = new HashMap<String, String>();
    for (final String key : this.columns) {
      {
        final Object value = this.current.get(key);
        if ((value != null)) {
          CSVLog.LOG4J.warn("Missing entry for key. {}", key);
          entry.put(key, value.toString());
        }
      }
    }
    this.logs.add(entry);
    this.current.clear();
  }

  @Override
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _hasElements = false;
      for(final String key : this.columns) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(this.separator, "");
        }
        _builder.append(key);
      }
    }
    _builder.newLineIfNotEmpty();
    {
      for(final Map<String, String> line : this.logs) {
        {
          boolean _hasElements_1 = false;
          for(final String key_1 : this.columns) {
            if (!_hasElements_1) {
              _hasElements_1 = true;
            } else {
              _builder.appendImmediate(this.separator, "");
            }
            String _stringify = this.stringify(line.get(key_1));
            _builder.append(_stringify);
          }
        }
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }

  public String stringify(final String source) {
    if ((source == null)) {
      return "";
    }
    if ((source.contains("\"") || source.contains(this.separator))) {
      String _replaceAll = source.replaceAll("\"", "\"\"");
      String _plus = ("\"" + _replaceAll);
      return (_plus + "\"");
    }
    return source;
  }
}
