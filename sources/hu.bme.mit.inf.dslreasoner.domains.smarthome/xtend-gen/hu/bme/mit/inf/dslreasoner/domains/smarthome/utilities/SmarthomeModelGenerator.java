package hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities;

import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import smarthome.Home;
import smarthome.Measurement;
import smarthome.Person;
import smarthome.SmarthomeFactory;
import uncertaindatatypes.UBoolean;
import uncertaindatatypes.UReal;

@SuppressWarnings("all")
public class SmarthomeModelGenerator {
  public SmarthomeModel make(final int homes, final int persons, final int measurements) {
    final SmarthomeFactory factory = SmarthomeFactory.eINSTANCE;
    final SmarthomeModel container = new SmarthomeModel();
    container.model = factory.createSmarthome();
    IntegerRange _upTo = new IntegerRange(1, homes);
    for (final Integer i : _upTo) {
      {
        UReal _uReal = new UReal((1000 * container.counter), 10);
        UReal _uReal_1 = new UReal((1000 * container.counter), 10);
        final Location location = new Location(_uReal, _uReal_1);
        container.locations.add(location);
        container.counter++;
      }
    }
    IntegerRange _upTo_1 = new IntegerRange(1, homes);
    for (final Integer i_1 : _upTo_1) {
      {
        final Home home = factory.createHome();
        int _size = container.locations.size();
        int _modulo = ((i_1).intValue() % _size);
        home.setLocation(container.locations.get(_modulo));
        container.model.getHomes().add(home);
        container.idmap.put(home, ("home" + i_1));
      }
    }
    IntegerRange _upTo_2 = new IntegerRange(1, persons);
    for (final Integer i_2 : _upTo_2) {
      {
        final Person person = factory.createPerson();
        person.setConfidence(0.88);
        container.model.getPersons().add(person);
        container.idmap.put(person, ("person" + i_2));
      }
    }
    IntegerRange _upTo_3 = new IntegerRange(1, measurements);
    for (final Integer i_3 : _upTo_3) {
      {
        final Measurement measurement = factory.createMeasurement();
        UReal _uReal = new UReal((30 + (container.counter % 7)), 0.5);
        measurement.setTemp(_uReal);
        UReal _uReal_1 = new UReal((4920 + (container.counter % 100)), 0.5);
        measurement.setCo(_uReal_1);
        UBoolean _uBoolean = new UBoolean(true, 0.8);
        measurement.setDopen(_uBoolean);
        UReal _uReal_2 = new UReal((4 * container.counter), 1);
        measurement.setTime(_uReal_2);
        container.counter++;
        container.counter++;
        container.idmap.put(measurement, ("measurement" + i_3));
        if ((((i_3).intValue() % 7) == 0)) {
          measurement.getAthome().add(container.model.getPersons().get(((i_3).intValue() % persons)));
        }
        container.model.getHomes().get(((i_3).intValue() % homes)).getMeasurements().add(measurement);
        container.measurements.add(measurement);
      }
    }
    return container;
  }

  public void iterate(final SmarthomeModel container, final int change) {
    final SmarthomeFactory factory = SmarthomeFactory.eINSTANCE;
    final int off = container.measurements.size();
    IntegerRange _upTo = new IntegerRange(1, change);
    for (final int i : _upTo) {
      {
        final Measurement rm = container.measurements.remove(0);
        final Consumer<Home> _function = (Home home) -> {
          home.getMeasurements().remove(rm);
        };
        container.model.getHomes().forEach(_function);
        final Measurement measurement = factory.createMeasurement();
        UReal _uReal = new UReal((30 + (container.counter % 7)), 0.5);
        measurement.setTemp(_uReal);
        UReal _uReal_1 = new UReal((4920 + (container.counter % 100)), 0.5);
        measurement.setCo(_uReal_1);
        UBoolean _uBoolean = new UBoolean(true, 0.8);
        measurement.setDopen(_uBoolean);
        UReal _uReal_2 = new UReal((4 * container.counter), 1);
        measurement.setTime(_uReal_2);
        container.counter++;
        container.counter++;
        container.idmap.put(measurement, ("measurement" + Integer.valueOf(i)));
        if (((i % 7) == 0)) {
          EList<Person> _persons = container.model.getPersons();
          int _size = container.model.getPersons().size();
          int _modulo = (i % _size);
          measurement.getAthome().add(_persons.get(_modulo));
        }
        EList<Home> _homes = container.model.getHomes();
        int _size_1 = container.model.getHomes().size();
        int _modulo_1 = (i % _size_1);
        _homes.get(_modulo_1).getMeasurements().add(measurement);
      }
    }
  }
}
