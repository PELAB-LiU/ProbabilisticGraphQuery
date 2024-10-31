package hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities;

import java.util.List;
import java.util.Map;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import smarthome.Home;
import smarthome.Measurement;
import smarthome.Smarthome;

@SuppressWarnings("all")
public class SmarthomeModel {
  public Smarthome model;

  public final List<Location> locations = CollectionLiterals.<Location>newArrayList();

  public final List<Measurement> measurements = CollectionLiterals.<Measurement>newArrayList();

  public final Map<EObject, String> idmap = CollectionLiterals.<EObject, String>newHashMap();

  public int counter = 1;

  public int size() {
    EList<Home> _homes = this.model.getHomes();
    int _size = this.model.getPersons().size();
    int _size_1 = this.model.getHomes().size();
    int _plus = (_size + _size_1);
    final Function2<Integer, Home, Integer> _function = (Integer size, Home home) -> {
      int _size_2 = home.getMeasurements().size();
      return Integer.valueOf(((size).intValue() + _size_2));
    };
    return (int) IterableExtensions.<Home, Integer>fold(_homes, Integer.valueOf(_plus), _function);
  }

  public Measurement ofHashCode(final long code) {
    final Function1<Measurement, Boolean> _function = (Measurement it) -> {
      int _hashCode = it.hashCode();
      return Boolean.valueOf((_hashCode == code));
    };
    return IterableExtensions.<Measurement>findFirst(this.measurements, _function);
  }
}
