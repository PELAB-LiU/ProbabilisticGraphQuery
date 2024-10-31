package hu.bme.mit.inf.measurement.utilities.configuration;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class TeePrintStream extends PrintStream {
  private final List<PrintStream> out;

  public TeePrintStream(final PrintStream... out) {
    super(OutputStream.nullOutputStream());
    this.out = Arrays.<PrintStream>asList(out);
  }

  @Override
  public boolean checkError() {
    final Function2<Boolean, PrintStream, Boolean> _function = (Boolean accu, PrintStream current) -> {
      return Boolean.valueOf(((accu).booleanValue() || current.checkError()));
    };
    return (boolean) IterableExtensions.<PrintStream, Boolean>fold(this.out, Boolean.valueOf(false), _function);
  }

  @Override
  public void write(final int x) {
    final Consumer<PrintStream> _function = (PrintStream it) -> {
      it.write(x);
    };
    this.out.forEach(_function);
  }

  @Override
  public void write(final byte[] x, final int o, final int l) {
    final Consumer<PrintStream> _function = (PrintStream it) -> {
      it.write(x, o, l);
    };
    this.out.forEach(_function);
  }

  @Override
  public void close() {
    final Consumer<PrintStream> _function = (PrintStream it) -> {
      it.close();
    };
    this.out.forEach(_function);
    super.close();
  }

  @Override
  public void flush() {
    final Consumer<PrintStream> _function = (PrintStream it) -> {
      it.flush();
    };
    this.out.forEach(_function);
  }
}
