package hu.bme.mit.inf.measurement.utilities.configuration;

import com.beust.jcommander.JCommander;
import java.util.regex.Pattern;

@SuppressWarnings("all")
public class SatelliteConfiguration extends BaseConfiguration {
  public SatelliteConfiguration() {
    this.pattern = Pattern.compile("(expected)\\((\\d\\.\\d+)\\):");
  }

  public static SatelliteConfiguration parse(final String... args) {
    final SatelliteConfiguration config = new SatelliteConfiguration();
    JCommander.newBuilder().addObject(config).build().parse(args);
    return config;
  }
}
