package hu.bme.mit.inf.measurement.utilities.configuration;

import java.util.regex.Pattern;

@SuppressWarnings("all")
public class SurveillanceConfiguration extends BaseConfiguration {
  /* @Parameter(, )
   */private double threshold = 0.1;

  public double getThreshold() {
    return this.threshold;
  }

  /* @Parameter(, , )
   */private Pattern pattern = Pattern.compile("result\\((\\d+),([01]\\.\\d*)\\)");

  public Pattern getProbLogPattern() {
    return this.pattern;
  }

  public static SurveillanceConfiguration parse(final String... args) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field JCommander is undefined"
      + "\nnewBuilder cannot be resolved"
      + "\naddObject cannot be resolved"
      + "\nbuild cannot be resolved"
      + "\nparse cannot be resolved");
  }
}
