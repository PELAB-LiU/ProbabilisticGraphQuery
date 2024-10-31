package problog;

import hu.bme.mit.inf.dslreasoner.domains.satellite1.performability.Performability;
import java.util.Arrays;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import satellite1.CommSubsystem;
import satellite1.CubeSat3U;
import satellite1.CubeSat6U;
import satellite1.InterferometryMission;
import satellite1.KaCommSubsystem;
import satellite1.Payload;
import satellite1.SmallSat;
import satellite1.Spacecraft;
import satellite1.UHFCommSubsystem;
import satellite1.XCommSubsystem;
import se.liu.ida.sas.pelab.problog.transformation.ProbLogGeneration;

@SuppressWarnings("all")
public class Generation {
  private final String op = "operational";

  private final String ss = "subsystem";

  private final String lnk = "link";

  private final String pld = "payload";

  private final String rdy = "ready";

  private final String on = "online";

  public CharSequence generateFrom(final InterferometryMission mission) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(":- use_module(library(aggregate)).");
    _builder.newLine();
    _builder.newLine();
    {
      EList<Spacecraft> _spacecraft = mission.getSpacecraft();
      for(final Spacecraft satellite : _spacecraft) {
        CharSequence _basicEvent = ProbLogGeneration.basicEvent(this.probability(satellite), this.op, this.getName(satellite));
        _builder.append(_basicEvent);
        _builder.newLineIfNotEmpty();
        {
          Payload _payload = satellite.getPayload();
          boolean _tripleNotEquals = (_payload != null);
          if (_tripleNotEquals) {
            CharSequence _between = this.between(this.pld, satellite);
            _builder.append(_between);
            _builder.append(" :- ");
            CharSequence _between_1 = this.between(this.on, satellite);
            _builder.append(_between_1);
            _builder.append(".");
          }
        }
        _builder.newLineIfNotEmpty();
        {
          EList<CommSubsystem> _commSubsystem = satellite.getCommSubsystem();
          for(final CommSubsystem subsystem : _commSubsystem) {
            CharSequence _basicEvent_1 = ProbLogGeneration.basicEvent(this.probability(subsystem), this.op, this.getName(subsystem));
            _builder.append(_basicEvent_1);
            _builder.newLineIfNotEmpty();
            CharSequence _between_2 = this.between(this.ss, satellite, subsystem);
            _builder.append(_between_2);
            _builder.append(".");
            _builder.newLineIfNotEmpty();
            {
              CommSubsystem _target = subsystem.getTarget();
              boolean _tripleNotEquals_1 = (_target != null);
              if (_tripleNotEquals_1) {
                CharSequence _between_3 = this.between(this.lnk, subsystem, subsystem.getTarget());
                _builder.append(_between_3);
                _builder.append(".");
              }
            }
            _builder.newLineIfNotEmpty();
            {
              CommSubsystem _fallback = subsystem.getFallback();
              boolean _tripleNotEquals_2 = (_fallback != null);
              if (_tripleNotEquals_2) {
                CharSequence _between_4 = this.between(this.lnk, subsystem, subsystem.getFallback());
                _builder.append(_between_4);
                _builder.append(".");
              }
            }
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.newLine();
    {
      EList<CommSubsystem> _commSubsystem_1 = mission.getGroundStationNetwork().getCommSubsystem();
      for(final CommSubsystem gsc : _commSubsystem_1) {
        CharSequence _between_5 = this.between(this.rdy, gsc);
        _builder.append(_between_5);
        _builder.append(".");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    {
      final Function1<Spacecraft, Boolean> _function = (Spacecraft sat) -> {
        Payload _payload_1 = sat.getPayload();
        return Boolean.valueOf((_payload_1 != null));
      };
      int _size = IterableExtensions.size(IterableExtensions.<Spacecraft>filter(mission.getSpacecraft(), _function));
      IntegerRange _upTo = new IntegerRange(2, _size);
      for(final Integer size : _upTo) {
        _builder.append("utility(");
        _builder.append(size);
        _builder.append(",");
        double _calculate2 = Performability.calculate2((size).intValue());
        _builder.append(_calculate2);
        _builder.append(").");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.append(this.on);
    _builder.append("(Sat) :- ");
    _builder.append(this.ss);
    _builder.append("(Sat,Comm),");
    _builder.append(this.lnk);
    _builder.append("(Comm,Other),");
    _builder.append(this.op);
    _builder.append("(Sat),");
    _builder.append(this.op);
    _builder.append("(Comm),");
    _builder.append(this.rdy);
    _builder.append("(Other).");
    _builder.newLineIfNotEmpty();
    _builder.append(this.rdy);
    _builder.append("(Comm) :- ");
    _builder.append(this.ss);
    _builder.append("(Sat,Comm),");
    _builder.append(this.op);
    _builder.append("(Comm),");
    _builder.append(this.on);
    _builder.append("(Sat).");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("member(X,[X|R]).");
    _builder.newLine();
    _builder.append("member(X,[H|R]) :- X\\=H,member(X,R).");
    _builder.newLine();
    _builder.newLine();
    _builder.append("unique([],0).");
    _builder.newLine();
    _builder.append("unique([X|R],S) :- member(X,R),unique(R,S).");
    _builder.newLine();
    _builder.append("unique([X|R],S) :- \\+ member(X,R),unique(R,S1),S is S1 + 1.");
    _builder.newLine();
    _builder.newLine();
    _builder.append("%available(Size) :- findall(Sat,");
    _builder.append(this.pld);
    _builder.append("(Sat),Payloads),unique(Payloads,Size),Size >= 2.");
    _builder.newLineIfNotEmpty();
    _builder.append("available(length<P>) :- ");
    _builder.append(this.pld);
    _builder.append("(P).");
    _builder.newLineIfNotEmpty();
    _builder.append("coverage(Size,Reward) :- subquery(available(Size),Prob),utility(Size,Utility),Reward is Prob * Utility.");
    _builder.newLine();
    _builder.append("expected(sum<Reward>) :- coverage(_,Reward).");
    _builder.newLine();
    _builder.newLine();
    _builder.append("query(expected(S)).");
    _builder.newLine();
    _builder.newLine();
    return _builder;
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

  protected double _probability(final CubeSat3U satellite) {
    return 0.98400034407713;
  }

  protected double _probability(final CubeSat6U satellite) {
    return 0.98496269152523;
  }

  protected double _probability(final SmallSat satellite) {
    return 0.98581584235241;
  }

  protected double _probability(final KaCommSubsystem subsystem) {
    return 0.90483741803596;
  }

  protected double _probability(final UHFCommSubsystem subsystem) {
    return 0.92004441462932;
  }

  protected double _probability(final XCommSubsystem subsystem) {
    return 0.92596107864232;
  }

  public double probability(final EObject satellite) {
    if (satellite instanceof CubeSat3U) {
      return _probability((CubeSat3U)satellite);
    } else if (satellite instanceof CubeSat6U) {
      return _probability((CubeSat6U)satellite);
    } else if (satellite instanceof SmallSat) {
      return _probability((SmallSat)satellite);
    } else if (satellite instanceof KaCommSubsystem) {
      return _probability((KaCommSubsystem)satellite);
    } else if (satellite instanceof UHFCommSubsystem) {
      return _probability((UHFCommSubsystem)satellite);
    } else if (satellite instanceof XCommSubsystem) {
      return _probability((XCommSubsystem)satellite);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(satellite).toString());
    }
  }
}
