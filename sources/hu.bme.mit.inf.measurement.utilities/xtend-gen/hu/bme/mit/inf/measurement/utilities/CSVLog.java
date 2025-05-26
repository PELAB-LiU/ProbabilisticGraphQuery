package hu.bme.mit.inf.measurement.utilities;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.ArrayExtensions;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
public class CSVLog {
  private static final Logger LOG4J = LoggerFactory.getLogger(CSVLog.class);

  private final String[] columns;

  private final String separator;

  private final Queue<Map<String, String>> logs;

  private final Map<String, Object> current;

  private final AtomicBoolean unsaved;

  private final Thread backup;

  public CSVLog(final String[] columns, final String separator) {
    this.columns = columns;
    this.separator = separator;
    this.logs = CollectionLiterals.<Map<String, String>>newLinkedList();
    this.current = CollectionLiterals.<String, Object>newHashMap();
    final CSVLog host = this;
    AtomicBoolean _atomicBoolean = new AtomicBoolean(false);
    this.unsaved = _atomicBoolean;
    this.backup = new Thread() {
      @Override
      public void run() {
        try {
          boolean _andSet = host.unsaved.getAndSet(false);
          if (_andSet) {
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd--HH-mm-ss");
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("csvlog-");
            Date _date = new Date();
            String _format = format.format(_date);
            _builder.append(_format);
            _builder.append("-");
            int _hashCode = host.hashCode();
            _builder.append(_hashCode);
            _builder.append(".backup.txt");
            final FileWriter writer = new FileWriter(_builder.toString());
            writer.write(host.toString());
          }
        } catch (Throwable _e) {
          throw Exceptions.sneakyThrow(_e);
        }
      }
    };
  }

  public Object log(final String key, final Object object) {
    CSVLog.LOG4J.debug("CSVSET {} --> {}", key, object);
    boolean _contains = ArrayExtensions.contains(this.columns, key);
    boolean _not = (!_contains);
    if (_not) {
      CSVLog.LOG4J.warn("Unlogged value for key. {}", key);
    }
    return this.current.put(key, object);
  }

  public void commit() {
    this.unsaved.set(true);
    final HashMap<String, String> entry = new HashMap<String, String>();
    for (final String key : this.columns) {
      {
        final Object value = this.current.get(key);
        if ((value != null)) {
          entry.put(key, value.toString());
        } else {
          CSVLog.LOG4J.warn("Missing entry for key. {}", key);
        }
      }
    }
    this.logs.add(entry);
    this.current.clear();
  }

  @Override
  public String toString() {
    this.unsaved.set(false);
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

  @Override
  public void finalize() {
    Runtime.getRuntime().removeShutdownHook(this.backup);
    boolean _get = this.unsaved.get();
    if (_get) {
      this.backup.run();
    }
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
