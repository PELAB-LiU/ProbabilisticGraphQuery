package se.liu.ida.sas.pelab.smarthome.storm;

import com.google.common.collect.Iterables;
import hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities.SmarthomeHelper;
import hu.bme.mit.inf.measurement.utilities.Quad;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import se.liu.ida.sas.pelab.storm.transformation.StormGeneration;
import smarthome.Home;
import smarthome.Measurement;
import smarthome.Person;
import smarthome.Smarthome;

@SuppressWarnings("all")
public class StormSmarthomeGenerator {
  private static final String warningbase = "warningbase";

  private static final String incrementbase = "incrementbase";

  private static final String inc2 = "increment_2";

  private static final String tempInc1 = "tempincrement";

  private static final String warning4 = "warning4";

  private static final String cobe = "cobe";

  private static final String fireevent = "fire";

  private static final String certain = "certain";

  private static final String personevent = "person";

  private static final String nobodyhome = "nbhome";

  private static final String callevent = "callevent";

  public Pair<String, List<String>> generateFrom(final Smarthome model) {
    final Pair<ArrayList<Pair<Measurement, Measurement>>, String> tempBE = this.tempIncrBE(model);
    final Pair<Set<Measurement>, String> temp = this.temp(tempBE.getKey());
    final Pair<Set<Measurement>, String> warning4 = this.fourWarinings(model, temp.getKey());
    final Pair<ArrayList<Measurement>, String> co = this.highCO(model);
    final Pair<Set<Measurement>, String> fire = this.fire(co.getKey(), warning4.getKey());
    final Pair<EList<Person>, String> person = this.personBE(model);
    final Pair<ArrayList<Measurement>, String> nbhome = this.nobodyHome(model);
    final Pair<Set<Measurement>, String> calls = this.call(fire.getKey());
    StringConcatenation _builder = new StringConcatenation();
    String _value = tempBE.getValue();
    _builder.append(_value);
    _builder.newLineIfNotEmpty();
    String _value_1 = temp.getValue();
    _builder.append(_value_1);
    _builder.newLineIfNotEmpty();
    String _value_2 = warning4.getValue();
    _builder.append(_value_2);
    _builder.newLineIfNotEmpty();
    String _value_3 = co.getValue();
    _builder.append(_value_3);
    _builder.newLineIfNotEmpty();
    String _value_4 = fire.getValue();
    _builder.append(_value_4);
    _builder.newLineIfNotEmpty();
    String _value_5 = person.getValue();
    _builder.append(_value_5);
    _builder.newLineIfNotEmpty();
    String _value_6 = nbhome.getValue();
    _builder.append(_value_6);
    _builder.newLineIfNotEmpty();
    String _value_7 = calls.getValue();
    _builder.append(_value_7);
    _builder.newLineIfNotEmpty();
    String _basicEvent = StormGeneration.basicEvent(StormSmarthomeGenerator.warningbase, 0.925);
    _builder.append(_basicEvent);
    _builder.newLineIfNotEmpty();
    String _basicEvent_1 = StormGeneration.basicEvent(StormSmarthomeGenerator.incrementbase, 0.925);
    _builder.append(_basicEvent_1);
    _builder.newLineIfNotEmpty();
    String _basicEvent_2 = StormGeneration.basicEvent(StormSmarthomeGenerator.certain, 1);
    _builder.append(_basicEvent_2);
    _builder.newLineIfNotEmpty();
    final String stormmodel = _builder.toString();
    final Function1<Measurement, String> _function = (Measurement it) -> {
      return StormGeneration.topEvent(this.key(StormSmarthomeGenerator.callevent, it));
    };
    final List<String> tops = IterableExtensions.<String>toList(IterableExtensions.<Measurement, String>map(calls.getKey(), _function));
    return Pair.<String, List<String>>of(stormmodel, tops);
  }

  public Pair<Set<Measurement>, String> call(final Set<Measurement> fires) {
    StringConcatenation _builder = new StringConcatenation();
    {
      for(final Measurement fire : fires) {
        String _andGate = StormGeneration.andGate(this.key(StormSmarthomeGenerator.callevent, fire), 
          this.key(StormSmarthomeGenerator.fireevent, fire), 
          this.key(StormSmarthomeGenerator.nobodyhome, fire));
        _builder.append(_andGate);
        _builder.newLineIfNotEmpty();
      }
    }
    return Pair.<Set<Measurement>, String>of(fires, _builder.toString());
  }

  public Pair<ArrayList<Measurement>, String> nobodyHome(final Smarthome model) {
    final ArrayList<Measurement> nbhome = CollectionLiterals.<Measurement>newArrayList();
    model.getPersons();
    EList<Home> _homes = model.getHomes();
    for (final Home home : _homes) {
      nbhome.addAll(home.getMeasurements());
    }
    StringConcatenation _builder = new StringConcatenation();
    {
      for(final Measurement m : nbhome) {
        {
          boolean _isEmpty = m.getAthome().isEmpty();
          if (_isEmpty) {
            String _andGate = StormGeneration.andGate(this.key(StormSmarthomeGenerator.nobodyhome, m), 
              StormSmarthomeGenerator.certain);
            _builder.append(_andGate);
            _builder.newLineIfNotEmpty();
          } else {
            final Function1<Person, String> _function = (Person p) -> {
              return this.key(StormSmarthomeGenerator.personevent, p);
            };
            String _andGate_1 = StormGeneration.andGate(this.key(StormSmarthomeGenerator.nobodyhome, m), 
              ((String[])Conversions.unwrapArray(ListExtensions.<Person, String>map(m.getAthome(), _function), String.class)));
            _builder.append(_andGate_1);
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return Pair.<ArrayList<Measurement>, String>of(nbhome, _builder.toString());
  }

  public Pair<EList<Person>, String> personBE(final Smarthome model) {
    EList<Person> _persons = model.getPersons();
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<Person> _persons_1 = model.getPersons();
      for(final Person person : _persons_1) {
        String _key = this.key(StormSmarthomeGenerator.personevent, person);
        double _confidence = person.getConfidence();
        double _minus = (1 - _confidence);
        String _basicEvent = StormGeneration.basicEvent(_key, _minus);
        _builder.append(_basicEvent);
        _builder.newLineIfNotEmpty();
      }
    }
    return Pair.<EList<Person>, String>of(_persons, _builder.toString());
  }

  public Pair<Set<Measurement>, String> fire(final List<Measurement> cos, final Set<Measurement> warnings) {
    final ArrayList<Pair<Measurement, Measurement>> fires = CollectionLiterals.<Pair<Measurement, Measurement>>newArrayList();
    for (final Measurement co : cos) {
      {
        final Function1<Measurement, Boolean> _function = (Measurement w) -> {
          EObject _eContainer = w.eContainer();
          EObject _eContainer_1 = co.eContainer();
          return Boolean.valueOf((_eContainer == _eContainer_1));
        };
        final Function1<Measurement, Boolean> _function_1 = (Measurement w) -> {
          return Boolean.valueOf(SmarthomeHelper.within5s(co.getTime(), w.getTime()));
        };
        final Iterable<Measurement> warn = IterableExtensions.<Measurement>filter(IterableExtensions.<Measurement>filter(warnings, _function), _function_1);
        final Consumer<Measurement> _function_2 = (Measurement w) -> {
          Pair<Measurement, Measurement> _mappedTo = Pair.<Measurement, Measurement>of(co, w);
          fires.add(_mappedTo);
        };
        warn.forEach(_function_2);
      }
    }
    final String helperName = (StormSmarthomeGenerator.fireevent + "helper");
    final Function1<Pair<Measurement, Measurement>, Measurement> _function = (Pair<Measurement, Measurement> it) -> {
      return it.getKey();
    };
    Set<Measurement> _set = IterableExtensions.<Measurement>toSet(ListExtensions.<Pair<Measurement, Measurement>, Measurement>map(fires, _function));
    StringConcatenation _builder = new StringConcatenation();
    {
      final Function1<Pair<Measurement, Measurement>, Measurement> _function_1 = (Pair<Measurement, Measurement> it) -> {
        return it.getKey();
      };
      final Function1<Measurement, Pair<Measurement, Iterable<Measurement>>> _function_2 = (Measurement co_1) -> {
        final Function1<Pair<Measurement, Measurement>, Boolean> _function_3 = (Pair<Measurement, Measurement> it) -> {
          Measurement _key = it.getKey();
          return Boolean.valueOf((_key == co_1));
        };
        final Function1<Pair<Measurement, Measurement>, Measurement> _function_4 = (Pair<Measurement, Measurement> it) -> {
          return it.getValue();
        };
        Iterable<Measurement> _map = IterableExtensions.<Pair<Measurement, Measurement>, Measurement>map(IterableExtensions.<Pair<Measurement, Measurement>>filter(fires, _function_3), _function_4);
        return Pair.<Measurement, Iterable<Measurement>>of(co_1, _map);
      };
      Iterable<Pair<Measurement, Iterable<Measurement>>> _map = IterableExtensions.<Measurement, Pair<Measurement, Iterable<Measurement>>>map(IterableExtensions.<Measurement>toSet(ListExtensions.<Pair<Measurement, Measurement>, Measurement>map(fires, _function_1)), _function_2);
      for(final Pair<Measurement, Iterable<Measurement>> fire : _map) {
        final Function1<Measurement, String> _function_3 = (Measurement it) -> {
          return this.key(StormSmarthomeGenerator.warning4, it);
        };
        String _orGate = StormGeneration.orGate(this.key(helperName, fire.getKey()), 
          ((String[])Conversions.unwrapArray(IterableExtensions.<Measurement, String>map(fire.getValue(), _function_3), String.class)));
        _builder.append(_orGate);
        _builder.newLineIfNotEmpty();
        String _andGate = StormGeneration.andGate(this.key(StormSmarthomeGenerator.fireevent, fire.getKey()), 
          this.key(StormSmarthomeGenerator.cobe, fire.getKey()), 
          this.key(helperName, fire.getKey()));
        _builder.append(_andGate);
        _builder.newLineIfNotEmpty();
      }
    }
    return Pair.<Set<Measurement>, String>of(_set, _builder.toString());
  }

  public Pair<ArrayList<Measurement>, String> highCO(final Smarthome model) {
    final ArrayList<Measurement> highCO = CollectionLiterals.<Measurement>newArrayList();
    EList<Home> _homes = model.getHomes();
    for (final Home home : _homes) {
      final Function1<Measurement, Boolean> _function = (Measurement m) -> {
        return Boolean.valueOf(SmarthomeHelper.gt5000(m.getCo()));
      };
      Iterables.<Measurement>addAll(highCO, IterableExtensions.<Measurement>filter(home.getMeasurements(), _function));
    }
    StringConcatenation _builder = new StringConcatenation();
    {
      for(final Measurement m : highCO) {
        String _basicEvent = StormGeneration.basicEvent(this.key(StormSmarthomeGenerator.cobe, m), SmarthomeHelper.gt5000Confidence(m.getCo()));
        _builder.append(_basicEvent);
        _builder.newLineIfNotEmpty();
      }
    }
    return Pair.<ArrayList<Measurement>, String>of(highCO, _builder.toString());
  }

  public Pair<Set<Measurement>, String> fourWarinings(final Smarthome model, final Set<Measurement> increments) {
    final ArrayList<Quad<Measurement, Measurement, Measurement, Measurement>> warnings4 = CollectionLiterals.<Quad<Measurement, Measurement, Measurement, Measurement>>newArrayList();
    EList<Home> _homes = model.getHomes();
    for (final Home home : _homes) {
      {
        final Function1<Measurement, Boolean> _function = (Measurement it) -> {
          return Boolean.valueOf(increments.contains(it));
        };
        final Iterable<Measurement> measurements1 = IterableExtensions.<Measurement>filter(home.getMeasurements(), _function);
        for (final Measurement m1 : measurements1) {
          {
            final Function1<Measurement, Boolean> _function_1 = (Measurement m2) -> {
              return Boolean.valueOf(((SmarthomeHelper.after(m1.getTime(), m2.getTime()) && 
                SmarthomeHelper.within5m(m1.getTime(), m2.getTime())) && 
                (m1 != m2)));
            };
            final Iterable<Measurement> measurements2 = IterableExtensions.<Measurement>filter(measurements1, _function_1);
            for (final Measurement m2 : measurements2) {
              {
                final Function1<Measurement, Boolean> _function_2 = (Measurement m3) -> {
                  return Boolean.valueOf(((SmarthomeHelper.after(m2.getTime(), m3.getTime()) && 
                    SmarthomeHelper.within5m(m1.getTime(), m3.getTime())) && 
                    (m2 != m3)));
                };
                final Iterable<Measurement> measurements3 = IterableExtensions.<Measurement>filter(measurements2, _function_2);
                for (final Measurement m3 : measurements3) {
                  {
                    final Function1<Measurement, Boolean> _function_3 = (Measurement m4) -> {
                      return Boolean.valueOf(((SmarthomeHelper.after(m3.getTime(), m4.getTime()) && 
                        SmarthomeHelper.within5m(m1.getTime(), m4.getTime())) && 
                        (m3 != m4)));
                    };
                    final Iterable<Measurement> measurements4 = IterableExtensions.<Measurement>filter(measurements3, _function_3);
                    for (final Measurement m4 : measurements4) {
                      Quad<Measurement, Measurement, Measurement, Measurement> _quad = new Quad<Measurement, Measurement, Measurement, Measurement>(m1, m2, m3, m4);
                      warnings4.add(_quad);
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    final String helperName = (StormSmarthomeGenerator.warning4 + "helper");
    final Function1<Quad<Measurement, Measurement, Measurement, Measurement>, Measurement> _function = (Quad<Measurement, Measurement, Measurement, Measurement> it) -> {
      return it.getForth();
    };
    Set<Measurement> _set = IterableExtensions.<Measurement>toSet(ListExtensions.<Quad<Measurement, Measurement, Measurement, Measurement>, Measurement>map(warnings4, _function));
    StringConcatenation _builder = new StringConcatenation();
    {
      final Function1<Quad<Measurement, Measurement, Measurement, Measurement>, Measurement> _function_1 = (Quad<Measurement, Measurement, Measurement, Measurement> it) -> {
        return it.getForth();
      };
      Set<Measurement> _set_1 = IterableExtensions.<Measurement>toSet(ListExtensions.<Quad<Measurement, Measurement, Measurement, Measurement>, Measurement>map(warnings4, _function_1));
      for(final Measurement m4 : _set_1) {
        CharSequence _xblockexpression = null;
        {
          final Function1<Quad<Measurement, Measurement, Measurement, Measurement>, Boolean> _function_2 = (Quad<Measurement, Measurement, Measurement, Measurement> it) -> {
            Measurement _forth = it.getForth();
            return Boolean.valueOf((_forth == m4));
          };
          final Iterable<Quad<Measurement, Measurement, Measurement, Measurement>> tuples = IterableExtensions.<Quad<Measurement, Measurement, Measurement, Measurement>>filter(warnings4, _function_2);
          StringConcatenation _builder_1 = new StringConcatenation();
          {
            for(final Quad<Measurement, Measurement, Measurement, Measurement> tuple : tuples) {
              String _andGate = StormGeneration.andGate(this.key(helperName, tuple.getFisrt(), tuple.getSecond(), tuple.getThird(), tuple.getForth()), 
                StormSmarthomeGenerator.warningbase, 
                this.key(StormSmarthomeGenerator.tempInc1, tuple.getFisrt()), 
                this.key(StormSmarthomeGenerator.tempInc1, tuple.getSecond()), 
                this.key(StormSmarthomeGenerator.tempInc1, tuple.getThird()), 
                this.key(StormSmarthomeGenerator.tempInc1, tuple.getForth()));
              _builder_1.append(_andGate);
              _builder_1.newLineIfNotEmpty();
            }
          }
          final Function1<Quad<Measurement, Measurement, Measurement, Measurement>, String> _function_3 = (Quad<Measurement, Measurement, Measurement, Measurement> quad) -> {
            return this.key(helperName, quad.getFisrt(), quad.getSecond(), quad.getThird(), quad.getForth());
          };
          String _orGate = StormGeneration.orGate(this.key(StormSmarthomeGenerator.warning4, m4), 
            ((String[])Conversions.unwrapArray(IterableExtensions.<Quad<Measurement, Measurement, Measurement, Measurement>, String>map(tuples, _function_3), String.class)));
          _builder_1.append(_orGate);
          _builder_1.newLineIfNotEmpty();
          _xblockexpression = _builder_1;
        }
        _builder.append(_xblockexpression);
        _builder.newLineIfNotEmpty();
      }
    }
    return Pair.<Set<Measurement>, String>of(_set, _builder.toString());
  }

  public Pair<Set<Measurement>, String> temp(final List<Pair<Measurement, Measurement>> increments) {
    final Function1<Pair<Measurement, Measurement>, Measurement> _function = (Pair<Measurement, Measurement> pair) -> {
      return pair.getValue();
    };
    final Set<Measurement> tempInc = IterableExtensions.<Measurement>toSet(ListExtensions.<Pair<Measurement, Measurement>, Measurement>map(increments, _function));
    final String helperName = (StormSmarthomeGenerator.tempInc1 + "Helper");
    StringConcatenation _builder = new StringConcatenation();
    {
      for(final Measurement inc : tempInc) {
        CharSequence _xblockexpression = null;
        {
          final Function1<Pair<Measurement, Measurement>, Boolean> _function_1 = (Pair<Measurement, Measurement> entry) -> {
            Measurement _value = entry.getValue();
            return Boolean.valueOf((_value == inc));
          };
          final Function1<Pair<Measurement, Measurement>, String> _function_2 = (Pair<Measurement, Measurement> increment) -> {
            return this.key(StormSmarthomeGenerator.inc2, increment.getKey(), increment.getValue());
          };
          final Iterable<String> incrementEvents = IterableExtensions.<Pair<Measurement, Measurement>, String>map(IterableExtensions.<Pair<Measurement, Measurement>>filter(increments, _function_1), _function_2);
          StringConcatenation _builder_1 = new StringConcatenation();
          String _andGate = StormGeneration.andGate(this.key(StormSmarthomeGenerator.tempInc1, inc), 
            StormSmarthomeGenerator.incrementbase, 
            this.key(helperName, inc));
          _builder_1.append(_andGate);
          _builder_1.newLineIfNotEmpty();
          String _orGate = StormGeneration.orGate(this.key(helperName, inc), ((String[])Conversions.unwrapArray(incrementEvents, String.class)));
          _builder_1.append(_orGate);
          _builder_1.newLineIfNotEmpty();
          _xblockexpression = _builder_1;
        }
        _builder.append(_xblockexpression);
        _builder.newLineIfNotEmpty();
      }
    }
    return Pair.<Set<Measurement>, String>of(tempInc, _builder.toString());
  }

  public Pair<ArrayList<Pair<Measurement, Measurement>>, String> tempIncrBE(final Smarthome model) {
    final ArrayList<Pair<Measurement, Measurement>> increments = CollectionLiterals.<Pair<Measurement, Measurement>>newArrayList();
    EList<Home> _homes = model.getHomes();
    for (final Home home : _homes) {
      EList<Measurement> _measurements = home.getMeasurements();
      for (final Measurement measurement1 : _measurements) {
        EList<Measurement> _measurements_1 = home.getMeasurements();
        for (final Measurement measurement2 : _measurements_1) {
          Boolean _incrementable = this.incrementable(measurement1, measurement2);
          if ((_incrementable).booleanValue()) {
            Pair<Measurement, Measurement> _mappedTo = Pair.<Measurement, Measurement>of(measurement1, measurement2);
            increments.add(_mappedTo);
          }
        }
      }
    }
    StringConcatenation _builder = new StringConcatenation();
    {
      for(final Pair<Measurement, Measurement> increment : increments) {
        String _basicEvent = StormGeneration.basicEvent(
          this.key(StormSmarthomeGenerator.inc2, increment.getKey(), increment.getValue()), 
          (this.incrementProbability(increment.getKey(), increment.getValue())).doubleValue());
        _builder.append(_basicEvent);
        _builder.newLineIfNotEmpty();
      }
    }
    return Pair.<ArrayList<Pair<Measurement, Measurement>>, String>of(increments, _builder.toString());
  }

  public Double incrementProbability(final Measurement m1, final Measurement m2) {
    return Double.valueOf(SmarthomeHelper.incrementConfidence(m1.getTemp(), m1.getTime(), m2.getTemp(), m2.getTime()));
  }

  public Boolean incrementable(final Measurement m1, final Measurement m2) {
    return Boolean.valueOf(((m1 != m2) && SmarthomeHelper.incrementable(m1.getTemp(), m1.getTime(), m2.getTemp(), m2.getTime())));
  }

  public String key(final String name, final EObject... args) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(name);
    _builder.append("_");
    {
      boolean _hasElements = false;
      for(final EObject arg : args) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate("_", "");
        }
        String _name = this.getName(arg);
        _builder.append(_name);
      }
    }
    return _builder.toString();
  }

  public String getName(final EObject object) {
    StringConcatenation _builder = new StringConcatenation();
    String _simpleName = object.getClass().getSimpleName();
    _builder.append(_simpleName);
    int _hashCode = object.hashCode();
    _builder.append(_hashCode);
    return _builder.toString();
  }
}
