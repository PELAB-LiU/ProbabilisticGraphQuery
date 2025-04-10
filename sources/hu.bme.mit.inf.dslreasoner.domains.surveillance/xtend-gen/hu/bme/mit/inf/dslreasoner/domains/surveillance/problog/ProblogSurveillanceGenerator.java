package hu.bme.mit.inf.dslreasoner.domains.surveillance.problog;

import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.SurveillanceHelper;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import surveillance.Drone;
import surveillance.MovingObject;
import surveillance.SurveillanceModel;
import surveillance.UnidentifiedObject;

@SuppressWarnings("all")
public class ProblogSurveillanceGenerator {
  private final String gs = "gunshot";

  private final String trg = "targettable";

  public CharSequence generateFrom(final SurveillanceModel model) {
    StringConcatenation _builder = new StringConcatenation();
    {
      final Function1<UnidentifiedObject, Boolean> _function = (UnidentifiedObject obj) -> {
        return Boolean.valueOf(this.targettable(obj));
      };
      Iterable<UnidentifiedObject> _filter = IterableExtensions.<UnidentifiedObject>filter(this.ufos(model), _function);
      for(final UnidentifiedObject ufo : _filter) {
        CharSequence _objectevent = this.objectevent(ufo);
        _builder.append(_objectevent);
        _builder.newLineIfNotEmpty();
      }
    }
    {
      Iterable<Drone> _drones = this.drones(model);
      for(final Drone drone : _drones) {
        {
          final Function1<UnidentifiedObject, Boolean> _function_1 = (UnidentifiedObject ufo_1) -> {
            return Boolean.valueOf(this.shootable(drone, ufo_1));
          };
          Iterable<UnidentifiedObject> _filter_1 = IterableExtensions.<UnidentifiedObject>filter(this.ufos(model), _function_1);
          for(final UnidentifiedObject obj : _filter_1) {
            CharSequence _gsevent = this.gsevent(drone, obj);
            _builder.append(_gsevent);
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.newLine();
    _builder.append("attempt(D,O) :- ");
    _builder.append(this.gs);
    _builder.append("(D,O),");
    _builder.append(this.trg);
    _builder.append("(O).");
    _builder.newLineIfNotEmpty();
    _builder.append("elimination(O) :- attempt(_,O).");
    _builder.newLine();
    _builder.newLine();
    _builder.append("result(O,R) :- subquery(elimination(O),R).");
    _builder.newLine();
    _builder.append("query(result(O,R)).");
    _builder.newLine();
    return _builder;
  }

  public CharSequence gsevent(final Drone from, final UnidentifiedObject to) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method between(String, EObject...) from the type ProblogSurveillanceGenerator refers to the missing type Object");
  }

  public CharSequence objectevent(final UnidentifiedObject obj) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method between(String, EObject...) from the type ProblogSurveillanceGenerator refers to the missing type Object");
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

  public Object between(final String type, final EObject... args) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field ProbLogGeneration is undefined"
      + "\ndfact cannot be resolved");
  }

  public String getName(final EObject object) {
    int _hashCode = object.hashCode();
    return ("" + Integer.valueOf(_hashCode));
  }
}
