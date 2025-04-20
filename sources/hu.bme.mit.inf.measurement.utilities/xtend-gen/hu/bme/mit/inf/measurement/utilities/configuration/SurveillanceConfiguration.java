package hu.bme.mit.inf.measurement.utilities.configuration;

import com.beust.jcommander.JCommander;
import java.util.regex.Pattern;

@SuppressWarnings("all")
public class SurveillanceConfiguration extends BaseConfiguration {
  public SurveillanceConfiguration() {
    this.pattern = Pattern.compile("result\\((\\d+),([01]\\.\\d*)\\)");
  }

  public static SurveillanceConfiguration parse(final String... args) {
    final SurveillanceConfiguration config = new SurveillanceConfiguration();
    JCommander.newBuilder().addObject(config).build().parse(args);
    return config;
  }
}
