package hu.bme.mit.inf.measurement.utilities.configuration;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import java.util.regex.Pattern;

@SuppressWarnings("all")
public class SmarthomeConfiguration extends BaseConfiguration {
  @Parameter(names = "--homes", description = "Number of homes to generate.")
  private int homes = 10;

  public int getHomes() {
    return this.homes;
  }

  @Parameter(names = "--persons", description = "Number of persons to generate.")
  private int persons = 1;

  public int getPersons() {
    return this.persons;
  }

  @Parameter(names = "--plpattern", description = "Result extraction regex for ProbLog", converter = RegexPatternConverter.class)
  private Pattern pattern = Pattern.compile("callprobability\\((\\d+),([01]\\.\\d*)\\)");

  public Pattern getProbLogPattern() {
    return this.pattern;
  }

  public static SmarthomeConfiguration parse(final String... args) {
    final SmarthomeConfiguration config = new SmarthomeConfiguration();
    JCommander.newBuilder().addObject(config).build().parse(args);
    return config;
  }
}
