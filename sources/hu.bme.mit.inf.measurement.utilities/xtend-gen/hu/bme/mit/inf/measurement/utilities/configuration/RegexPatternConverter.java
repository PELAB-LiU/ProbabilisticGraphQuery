package hu.bme.mit.inf.measurement.utilities.configuration;

import com.beust.jcommander.IStringConverter;
import java.util.regex.Pattern;

@SuppressWarnings("all")
public class RegexPatternConverter implements IStringConverter<Pattern> {
  @Override
  public Pattern convert(final String arg) {
    return Pattern.compile(arg);
  }
}
