package hu.bme.mit.inf.measurement.utilities.configuration;

import com.beust.jcommander.JCommander;
import java.util.regex.Pattern;

@SuppressWarnings("all")
public class SmarthomeConfiguration extends BaseConfiguration {
  public SmarthomeConfiguration() {
    this.pattern = Pattern.compile("callprobability\\((\\d+),([01]\\.\\d*)\\)");
  }

  public static SmarthomeConfiguration parse(final String... args) {
    final SmarthomeConfiguration config = new SmarthomeConfiguration();
    JCommander.newBuilder().addObject(config).build().parse(args);
    return config;
  }
}
