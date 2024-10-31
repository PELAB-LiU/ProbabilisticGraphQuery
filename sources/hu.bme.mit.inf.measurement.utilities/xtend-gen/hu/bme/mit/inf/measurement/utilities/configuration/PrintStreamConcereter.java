package hu.bme.mit.inf.measurement.utilities.configuration;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class PrintStreamConcereter /* implements IStringConverter<OutputStream>  */{
  @Override
  public PrintStream convert(final String arg) {
    try {
      if (arg != null) {
        switch (arg) {
          case "System.out":
            return System.out;
          case "System.err":
            return System.err;
          default:
            FileOutputStream _fileOutputStream = new FileOutputStream(arg, true);
            return new PrintStream(_fileOutputStream);
        }
      } else {
        FileOutputStream _fileOutputStream = new FileOutputStream(arg, true);
        return new PrintStream(_fileOutputStream);
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
