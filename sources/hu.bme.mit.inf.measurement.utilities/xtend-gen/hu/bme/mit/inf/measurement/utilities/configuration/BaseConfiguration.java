package hu.bme.mit.inf.measurement.utilities.configuration;

import java.io.File;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class BaseConfiguration {
  /* @Parameter(, , )
   */private RunConfiguration casestudy;

  public RunConfiguration getCase() {
    return this.casestudy;
  }

  /* @Parameter(, , , )
   */private File vqlfile;

  public File vql() {
    return this.vqlfile;
  }

  /* @Parameter(, , )
   */private int size_;

  public int getSize() {
    return this.size_;
  }

  /* @Parameter(, , , )
   */private List<Integer> seeds = CollectionLiterals.<Integer>emptyList();

  public List<Integer> seeds() {
    return this.seeds;
  }

  /* @Parameter(, )
   */private long timeout = (60 * 20);

  public long getTimeoutS() {
    return this.timeout;
  }

  /* @Parameter(, )
   */private String prefix = "N/A";

  public String getPrefix() {
    return this.prefix;
  }

  /* @Parameter(, , )
   */private List<Integer> warmups = IterableExtensions.<Integer>toList(new IntegerRange(1, 3));

  public List<Integer> getWarmups() {
    return this.warmups;
  }

  /* @Parameter(, )
   */private int gctime = 10;

  public int getGCTime() {
    return (this.gctime * 1000);
  }

  /* @Parameter(, )
   */private int iterations = 4;

  public int getIterations() {
    return this.iterations;
  }

  /* @Parameter(, )
   */private List<String> columns = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("prefix", "size", "run", "iteration", "incremental.total[ms]", "incremental.result", "incremental.sync[ms]", "incremental.prop[ms]", "incremental.timeout", "standalone.total[ms]", "standalone.result", "standalone.sync[ms]", "standalone.prop[ms]", "standalone.timeout", "problog.total[ms]", "problog.result", "problog.trafo[ms]", "problog.evaluation[ms]", "problog.timeout"));

  public List<String> getCSVcolumns() {
    return this.columns;
  }

  /* @Parameter(, )
   */private String delimiter = ",";

  public String getDelimiter() {
    return this.delimiter;
  }

  /* @Parameter(, )
   */private String plfile = "tmp-problog.pl";

  public String getProbLogFile() {
    return this.plfile;
  }

  /* @Parameter(, , , )
   */private List<PrintStream> out = Collections.<PrintStream>unmodifiableList(CollectionLiterals.<PrintStream>newArrayList(System.out));

  public PrintStream out() {
    return new TeePrintStream(((PrintStream[])Conversions.unwrapArray(this.out, PrintStream.class)));
  }

  /* @Parameter(, , , )
   */private List<PrintStream> out2 = Collections.<PrintStream>unmodifiableList(CollectionLiterals.<PrintStream>newArrayList(System.out));

  public PrintStream outWarmup() {
    return new TeePrintStream(((PrintStream[])Conversions.unwrapArray(this.out2, PrintStream.class)));
  }

  public static BaseConfiguration parse(final String... args) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field JCommander is undefined"
      + "\nnewBuilder cannot be resolved"
      + "\naddObject cannot be resolved"
      + "\nbuild cannot be resolved"
      + "\nprogramName cannot be resolved"
      + "\nusage cannot be resolved"
      + "\nparse cannot be resolved");
  }
}
