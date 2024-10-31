package hu.bme.mit.inf.measurement.utilities.configuration;

import java.util.regex.Pattern;

@SuppressWarnings("all")
public class SmarthomeConfiguration extends BaseConfiguration {
  /* @Parameter(, )
   */private int homes = 10;

  public int getHomes() {
    return this.homes;
  }

  /* @Parameter(, )
   */private int persons = 1;

  public int getPersons() {
    return this.persons;
  }

  /* @Parameter(, , )
   */private Pattern pattern = Pattern.compile("callprobability\\((\\d+),([01]\\.\\d*)\\)");

  public Pattern getProbLogPattern() {
    return this.pattern;
  }

  public static SmarthomeConfiguration parse(final String... args) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field JCommander is undefined"
      + "\nnewBuilder cannot be resolved"
      + "\naddObject cannot be resolved"
      + "\nbuild cannot be resolved"
      + "\nparse cannot be resolved");
  }
}
