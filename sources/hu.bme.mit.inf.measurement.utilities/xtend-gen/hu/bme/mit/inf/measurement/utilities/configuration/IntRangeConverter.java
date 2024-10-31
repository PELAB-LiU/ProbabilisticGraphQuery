package hu.bme.mit.inf.measurement.utilities.configuration;

import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.IntegerRange;

@SuppressWarnings("all")
public class IntRangeConverter /* implements IStringConverter<List<Integer>>  */{
  @Override
  public List<Integer> convert(final String arg) {
    try {
      final String[] bounds = arg.split("\\.\\.");
      final ArrayList<Integer> list = CollectionLiterals.<Integer>newArrayList();
      int _parseInt = Integer.parseInt(bounds[0]);
      int _parseInt_1 = Integer.parseInt(bounds[1]);
      IntegerRange _upTo = new IntegerRange(_parseInt, _parseInt_1);
      Iterables.<Integer>addAll(list, _upTo);
      return list;
    } catch (final Throwable _t) {
      if (_t instanceof IndexOutOfBoundsException || _t instanceof NumberFormatException) {
        return CollectionLiterals.<Integer>emptyList();
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
}
