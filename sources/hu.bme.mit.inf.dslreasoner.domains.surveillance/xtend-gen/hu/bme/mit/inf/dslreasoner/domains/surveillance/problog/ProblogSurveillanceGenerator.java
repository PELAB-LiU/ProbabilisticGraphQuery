package hu.bme.mit.inf.dslreasoner.domains.surveillance.problog;

import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.SurveillanceHelper;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import se.liu.ida.sas.pelab.problog.transformation.ProbLogGeneration;
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
    StringConcatenation _builder = new StringConcatenation();
    double _shotProbability = SurveillanceHelper.shotProbability(from.getPosition(), to.getPosition(), to.getSpeed(), Double.valueOf(to.getConfidence()));
    _builder.append(_shotProbability);
    _builder.append("::");
    CharSequence _between = this.between(this.gs, from, to);
    _builder.append(_between);
    _builder.append(".");
    _builder.newLineIfNotEmpty();
    return _builder;
  }

  public CharSequence objectevent(final UnidentifiedObject obj) {
    StringConcatenation _builder = new StringConcatenation();
    double _confidence = obj.getConfidence();
    _builder.append(_confidence);
    _builder.append("::");
    CharSequence _between = this.between(this.trg, obj);
    _builder.append(_between);
    _builder.append(".");
    _builder.newLineIfNotEmpty();
    return _builder;
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

  public CharSequence between(final String type, final EObject... args) {
    final Function1<EObject, String> _function = (EObject arg) -> {
      return this.getName(arg);
    };
    return ProbLogGeneration.dfact(type, ((String[])Conversions.unwrapArray(ListExtensions.<EObject, String>map(((List<EObject>)Conversions.doWrapArray(args)), _function), String.class)));
  }

  public String getName(final EObject object) {
    int _hashCode = object.hashCode();
    return ("" + Integer.valueOf(_hashCode));
  }
}
