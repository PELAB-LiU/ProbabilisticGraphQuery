package se.liu.ida.sas.pelab.problog.run;

import hu.bme.mit.inf.measurement.utilities.Config;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ProbLogEvaluator {
  public HashMap<String, Object> evaluate(final Config cfg) {
    try {
      final Pattern rx_expected = Pattern.compile(cfg.asString("pattern"));
      String _asString = cfg.asString("file");
      final ProcessBuilder builder = new ProcessBuilder("problog", _asString);
      final Process process = builder.start();
      final Timer timer = this.killOnTimeout(cfg, "timeout", process);
      final HashMap<String, Object> output = new HashMap<String, Object>();
      InputStream _inputStream = process.getInputStream();
      final Scanner io = new Scanner(_inputStream);
      final Procedure1<String> _function = (String line) -> {
        InputOutput.<String>println(("Debug: " + line));
        final Matcher match = rx_expected.matcher(line);
        boolean _find = match.find();
        if (_find) {
          output.put(match.group(1), Double.valueOf(Double.parseDouble(match.group(2))));
        }
      };
      IteratorExtensions.<String>forEach(io, _function);
      timer.cancel();
      return output;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  private Timer killOnTimeout(final Config cfg, final String timeoutname, final Process process) {
    boolean _isDefined = cfg.isDefined(timeoutname);
    if (_isDefined) {
      final Procedure0 _function = () -> {
        try {
          process.destroy();
          process.getInputStream().close();
        } catch (Throwable _e) {
          throw Exceptions.sneakyThrow(_e);
        }
      };
      final Timer timer = Config.timeout(cfg.asInt(timeoutname), _function);
      return timer;
    }
    return null;
  }
}
