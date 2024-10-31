package hu.bme.mit.inf.measurement.utilities.configuration;

import java.util.regex.Pattern;

@SuppressWarnings("all")
public class SatelliteConfiguration extends BaseConfiguration {
  /* @Parameter(, , )
   */private Pattern pattern = Pattern.compile("(expected)\\((\\d\\.\\d+)\\):");

  public Pattern getProbLogPattern() {
    return this.pattern;
  }

  public static SatelliteConfiguration parse(final String... args) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field JCommander is undefined"
      + "\nnewBuilder cannot be resolved"
      + "\naddObject cannot be resolved"
      + "\nbuild cannot be resolved"
      + "\nparse cannot be resolved");
  }
}
