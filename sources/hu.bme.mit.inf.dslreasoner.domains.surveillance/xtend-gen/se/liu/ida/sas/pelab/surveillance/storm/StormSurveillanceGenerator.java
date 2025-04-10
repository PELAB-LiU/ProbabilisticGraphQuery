package se.liu.ida.sas.pelab.surveillance.storm;

import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.SurveillanceHelper;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
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
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field StormGeneration is undefined"
      + "\ntopEvent cannot be resolved");
  }

  public String complexElimination(final Iterable<Pair<UnidentifiedObject, List<Drone>>> eliminations) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field StormGeneration is undefined"
      + "\norGate cannot be resolved");
  }

  public String complexAttempt(final List<Pair<Drone, UnidentifiedObject>> attempts) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field StormGeneration is undefined"
      + "\nandGate cannot be resolved");
  }

  public String basicEventGunshots(final List<Pair<Drone, UnidentifiedObject>> shots) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field StormGeneration is undefined"
      + "\nbasicEvent cannot be resolved");
  }

  public String basicEventTargettable(final Iterable<UnidentifiedObject> ufos) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field StormGeneration is undefined"
      + "\nbasicEvent cannot be resolved");
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
