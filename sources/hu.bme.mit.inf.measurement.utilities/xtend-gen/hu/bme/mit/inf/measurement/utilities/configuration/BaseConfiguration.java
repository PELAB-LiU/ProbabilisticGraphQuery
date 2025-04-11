package hu.bme.mit.inf.measurement.utilities.configuration;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;
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
  @Parameter(names = "--case", description = "Case study to run.", required = true)
  private RunConfiguration casestudy;

  public RunConfiguration getCase() {
    return this.casestudy;
  }

  @Parameter(names = "--vql", description = "VQL file to transform and evaluate.", required = true, converter = FileConverter.class)
  private File vqlfile;

  public File vql() {
    return this.vqlfile;
  }

  @Parameter(names = "--size", description = "Size of models to generate for evaluation.", required = true)
  private int size_;

  public int getSize() {
    return this.size_;
  }

  @Parameter(names = "--seed", description = "Seeds for the model. Use format \'<from>..<to>\'.", required = true, listConverter = IntRangeConverter.class)
  private List<Integer> seeds = CollectionLiterals.<Integer>emptyList();

  public List<Integer> seeds() {
    return this.seeds;
  }

  @Parameter(names = "--timeout", description = "Timeout for each run in seconds.")
  private long timeout = (60 * 20);

  public long getTimeoutS() {
    return this.timeout;
  }

  @Parameter(names = "--prefix", description = "Prefix in csv.")
  private String prefix = "N/A";

  public String getPrefix() {
    return this.prefix;
  }

  @Parameter(names = "--minimum", description = "Minimum number of measurements before early abort.")
  private int minimum = 20;

  public int getMinimum() {
    return this.minimum;
  }

  @Parameter(names = "--minrate", description = "Minimum success rate of analysis to continue.")
  private double rate = 0.1;

  public double getRate() {
    return this.rate;
  }

  @Parameter(names = "--warmups", description = "Number of warmup runs before measurement. Use format \'<from>..<to>\'.", listConverter = IntRangeConverter.class)
  private List<Integer> warmups = IterableExtensions.<Integer>toList(new IntegerRange(1, 3));

  public List<Integer> getWarmups() {
    return this.warmups;
  }

  @Parameter(names = "--gctime", description = "Garbage collection time between measurements in seconds.")
  private int gctime = 10;

  public int getGCTime() {
    return (this.gctime * 1000);
  }

  @Parameter(names = "--iterations", description = "Number of iterations (incremental changes) in a run.")
  private int iterations = 4;

  public int getIterations() {
    return this.iterations;
  }

  @Parameter(names = "--header", description = "List of CSV columns to include in the output.")
  private List<String> columns = CollectionLiterals.<String>newArrayList("prefix", "size", "run", "iteration", "incremental.total[ms]", "incremental.result", "incremental.sync[ms]", "incremental.prop[ms]", "incremental.timeout", "standalone.total[ms]", "standalone.result", "standalone.sync[ms]", "standalone.prop[ms]", "standalone.timeout", "problog.total[ms]", "problog.result", "problog.trafo[ms]", "problog.evaluation[ms]", "problog.timeout", "storm.total[ms]", "storm.result", "storm.trafo[ms]", "storm.evaluation[ms]", "storm.timeout", "incremental.healthy", "standalone.healthy");

  public List<String> getCSVcolumns() {
    return this.columns;
  }

  @Parameter(names = "--delimiter", description = "Separator in the output CSV file.")
  private String delimiter = ",";

  public String getDelimiter() {
    return this.delimiter;
  }

  @Parameter(names = "--preferabort", description = "Abort execution on erroneous state.")
  private boolean abort = false;

  public boolean isFavourAbort() {
    return this.abort;
  }

  @Parameter(names = "--plmodel", description = "Temporary file for problog model.")
  private String plfile = "tmp-problog.pl";

  public String getProbLogFile() {
    return this.plfile;
  }

  @Parameter(names = "--stormmodel", description = "Temporary file for storm model.")
  private String stormfile = "tmp-storm.dft";

  public String getStormFile() {
    return this.stormfile;
  }

  @Parameter(names = "--logto", description = "Specify where to print the CSV output from measurement runs. System.out and System.err will redirect to standard and error output respectively.", variableArity = true, converter = PrintStreamConcereter.class)
  private List<PrintStream> out = Collections.<PrintStream>unmodifiableList(CollectionLiterals.<PrintStream>newArrayList(System.out));

  public PrintStream out() {
    return new TeePrintStream(((PrintStream[])Conversions.unwrapArray(this.out, PrintStream.class)));
  }

  @Parameter(names = "--warmuplogto", description = "Specify where to print the CSV output from the warmup runs. System.out and System.err will redirect to standard and error output respectively.", variableArity = true, converter = PrintStreamConcereter.class)
  private List<PrintStream> out2 = Collections.<PrintStream>unmodifiableList(CollectionLiterals.<PrintStream>newArrayList(System.out));

  public PrintStream outWarmup() {
    return new TeePrintStream(((PrintStream[])Conversions.unwrapArray(this.out2, PrintStream.class)));
  }

  public static BaseConfiguration parse(final String... args) {
    final BaseConfiguration config = new BaseConfiguration();
    final JCommander parser = JCommander.newBuilder().addObject(config).build();
    if (((args == null) || (args.length == 0))) {
      parser.setProgramName("Base configuration");
      parser.usage();
      return null;
    }
    parser.parse(args);
    return config;
  }
}
