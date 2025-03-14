package hu.bme.mit.inf.measurement.utilities.configuration;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import java.util.regex.Pattern;

@SuppressWarnings("all")
public class SatelliteConfiguration extends BaseConfiguration {
  @Parameter(names = "--plpattern", description = "Result extraction regex for ProbLog", converter = RegexPatternConverter.class)
  private Pattern pattern = Pattern.compile("(expected)\\((\\d\\.\\d+)\\):");

  public Pattern getProbLogPattern() {
    return this.pattern;
  }

  public static SatelliteConfiguration parse(final String... args) {
    final SatelliteConfiguration config = new SatelliteConfiguration();
    JCommander.newBuilder().addObject(config).build().parse(args);
    return config;
  }
}
