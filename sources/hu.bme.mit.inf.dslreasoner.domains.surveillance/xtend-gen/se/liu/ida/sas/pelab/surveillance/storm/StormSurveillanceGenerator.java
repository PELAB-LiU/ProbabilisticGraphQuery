package se.liu.ida.sas.pelab.surveillance.storm;

import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.SurveillanceHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import se.liu.ida.sas.pelab.storm.transformation.StormGeneration;
import surveillance.Drone;
import surveillance.MovingObject;
import surveillance.SurveillanceModel;
import surveillance.UnidentifiedObject;

@SuppressWarnings("all")
public class StormSurveillanceGenerator {
  private final String gs = "gunshot";

  private final String trg = "targettable";

  private final String att = "attempt";

  private final String elim = "elimination";

  public Pair<String, List<String>> generateFrom(final SurveillanceModel model) {
    final Function1<UnidentifiedObject, Boolean> _function = (UnidentifiedObject obj) -> {
      return Boolean.valueOf(this.targettable(obj));
    };
    final Iterable<UnidentifiedObject> targettableUFOs = IterableExtensions.<UnidentifiedObject>filter(this.ufos(model), _function);
    final ArrayList<Pair<Drone, UnidentifiedObject>> gunshots = CollectionLiterals.<Pair<Drone, UnidentifiedObject>>newArrayList();
    Iterable<Drone> _drones = this.drones(model);
    for (final Drone drone : _drones) {
      final Function1<UnidentifiedObject, Boolean> _function_1 = (UnidentifiedObject ufo) -> {
        return Boolean.valueOf(this.shootable(drone, ufo));
      };
      Iterable<UnidentifiedObject> _filter = IterableExtensions.<UnidentifiedObject>filter(this.ufos(model), _function_1);
      for (final UnidentifiedObject ufo : _filter) {
        Pair<Drone, UnidentifiedObject> _mappedTo = Pair.<Drone, UnidentifiedObject>of(drone, ufo);
        gunshots.add(_mappedTo);
      }
    }
    final ArrayList<Pair<Drone, UnidentifiedObject>> attempts = CollectionLiterals.<Pair<Drone, UnidentifiedObject>>newArrayList();
    for (final UnidentifiedObject to : targettableUFOs) {
      {
        final Function1<Pair<Drone, UnidentifiedObject>, Boolean> _function_2 = (Pair<Drone, UnidentifiedObject> pair) -> {
          UnidentifiedObject _value = pair.getValue();
          return Boolean.valueOf((to == _value));
        };
        final Function1<Pair<Drone, UnidentifiedObject>, Drone> _function_3 = (Pair<Drone, UnidentifiedObject> pair) -> {
          return pair.getKey();
        };
        final Iterable<Drone> froms = IterableExtensions.<Pair<Drone, UnidentifiedObject>, Drone>map(IterableExtensions.<Pair<Drone, UnidentifiedObject>>filter(gunshots, _function_2), _function_3);
        for (final Drone from : froms) {
          Pair<Drone, UnidentifiedObject> _mappedTo_1 = Pair.<Drone, UnidentifiedObject>of(from, to);
          attempts.add(_mappedTo_1);
        }
      }
    }
    final HashMap<UnidentifiedObject, ArrayList<Drone>> elimmap = CollectionLiterals.<UnidentifiedObject, ArrayList<Drone>>newHashMap();
    for (final Pair<Drone, UnidentifiedObject> attempt : attempts) {
      {
        elimmap.putIfAbsent(attempt.getValue(), CollectionLiterals.<Drone>newArrayList());
        final ArrayList<Drone> list = elimmap.get(attempt.getValue());
        list.add(attempt.getKey());
      }
    }
    final Function1<Map.Entry<UnidentifiedObject, ArrayList<Drone>>, Pair<UnidentifiedObject, List<Drone>>> _function_2 = (Map.Entry<UnidentifiedObject, ArrayList<Drone>> entry) -> {
      UnidentifiedObject _key = entry.getKey();
      ArrayList<Drone> _value = entry.getValue();
      return Pair.<UnidentifiedObject, List<Drone>>of(_key, ((List<Drone>) _value));
    };
    final Iterable<Pair<UnidentifiedObject, List<Drone>>> eliminations = IterableExtensions.<Map.Entry<UnidentifiedObject, ArrayList<Drone>>, Pair<UnidentifiedObject, List<Drone>>>map(elimmap.entrySet(), _function_2);
    StringConcatenation _builder = new StringConcatenation();
    String _complexElimination = this.complexElimination(eliminations);
    _builder.append(_complexElimination);
    _builder.newLineIfNotEmpty();
    String _complexAttempt = this.complexAttempt(attempts);
    _builder.append(_complexAttempt);
    _builder.newLineIfNotEmpty();
    String _basicEventTargettable = this.basicEventTargettable(targettableUFOs);
    _builder.append(_basicEventTargettable);
    _builder.newLineIfNotEmpty();
    String _basicEventGunshots = this.basicEventGunshots(gunshots);
    _builder.append(_basicEventGunshots);
    _builder.newLineIfNotEmpty();
    final String base = _builder.toString();
    final Function1<Pair<UnidentifiedObject, List<Drone>>, String> _function_3 = (Pair<UnidentifiedObject, List<Drone>> entry) -> {
      return StormGeneration.topEvent(this.key(this.elim, entry.getKey()));
    };
    final List<String> tops = IterableExtensions.<String>toList(IterableExtensions.<Pair<UnidentifiedObject, List<Drone>>, String>map(eliminations, _function_3));
    return Pair.<String, List<String>>of(base, tops);
  }

  public String complexElimination(final Iterable<Pair<UnidentifiedObject, List<Drone>>> eliminations) {
    StringConcatenation _builder = new StringConcatenation();
    {
      for(final Pair<UnidentifiedObject, List<Drone>> elimination : eliminations) {
        final Function1<Drone, String> _function = (Drone drone) -> {
          return this.key(this.att, drone, elimination.getKey());
        };
        String _orGate = StormGeneration.orGate(this.key(this.elim, elimination.getKey()), 
          ((String[])Conversions.unwrapArray(ListExtensions.<Drone, String>map(elimination.getValue(), _function), String.class)));
        _builder.append(_orGate);
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }

  public String complexAttempt(final List<Pair<Drone, UnidentifiedObject>> attempts) {
    StringConcatenation _builder = new StringConcatenation();
    {
      for(final Pair<Drone, UnidentifiedObject> attempt : attempts) {
        String _andGate = StormGeneration.andGate(this.key(this.att, attempt.getKey(), attempt.getValue()), 
          this.key(this.gs, attempt.getKey(), attempt.getValue()), 
          this.key(this.trg, attempt.getValue()));
        _builder.append(_andGate);
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }

  public String basicEventGunshots(final List<Pair<Drone, UnidentifiedObject>> shots) {
    StringConcatenation _builder = new StringConcatenation();
    {
      for(final Pair<Drone, UnidentifiedObject> shot : shots) {
        String _basicEvent = StormGeneration.basicEvent(
          this.key(this.gs, shot.getKey(), shot.getValue()), 
          SurveillanceHelper.shotProbability(shot.getKey().getPosition(), shot.getValue().getPosition(), shot.getValue().getSpeed(), Double.valueOf(shot.getValue().getConfidence())));
        _builder.append(_basicEvent);
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }

  public String basicEventTargettable(final Iterable<UnidentifiedObject> ufos) {
    StringConcatenation _builder = new StringConcatenation();
    {
      for(final UnidentifiedObject ufo : ufos) {
        String _basicEvent = StormGeneration.basicEvent(
          this.key(this.trg, ufo), 
          ufo.getConfidence());
        _builder.append(_basicEvent);
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }

  public Iterable<Drone> drones(final SurveillanceModel model) {
    final Function1<MovingObject, Boolean> _function = (MovingObject obj) -> {
      return Boolean.valueOf((obj instanceof Drone));
    };
    final Function1<MovingObject, Drone> _function_1 = (MovingObject obj) -> {
      return ((Drone) obj);
    };
    return IterableExtensions.<MovingObject, Drone>map(IterableExtensions.<MovingObject>filter(model.getObjects(), _function), _function_1);
  }

  public Iterable<UnidentifiedObject> ufos(final SurveillanceModel model) {
    final Function1<MovingObject, Boolean> _function = (MovingObject obj) -> {
      return Boolean.valueOf((obj instanceof UnidentifiedObject));
    };
    final Function1<MovingObject, UnidentifiedObject> _function_1 = (MovingObject obj) -> {
      return ((UnidentifiedObject) obj);
    };
    return IterableExtensions.<MovingObject, UnidentifiedObject>map(IterableExtensions.<MovingObject>filter(model.getObjects(), _function), _function_1);
  }

  public boolean targettable(final UnidentifiedObject trg) {
    return ((trg.getConfidence() > 0.65) && SurveillanceHelper.spd30(trg.getSpeed()));
  }

  public boolean shootable(final Drone from, final UnidentifiedObject to) {
    return SurveillanceHelper.dst1000(from.getPosition(), to.getPosition());
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
