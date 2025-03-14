package hu.bme.mit.inf.measurement.utilities.configuration;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import java.util.regex.Pattern;

@SuppressWarnings("all")
public class SurveillanceConfiguration extends BaseConfiguration {
  @Parameter(names = "--threshold", description = "Probability of removing an object in an iteration.")
  private double threshold = 0.1;

  public double getThreshold() {
    return this.threshold;
  }

  @Parameter(names = "--plpattern", description = "Result extraction regex for ProbLog", converter = RegexPatternConverter.class)
  private Pattern pattern = Pattern.compile("result\\((\\d+),([01]\\.\\d*)\\)");

  public Pattern getProbLogPattern() {
    return this.pattern;
  }

  public static SurveillanceConfiguration parse(final String... args) {
    final SurveillanceConfiguration config = new SurveillanceConfiguration();
    JCommander.newBuilder().addObject(config).build().parse(args);
    return config;
  }
}
