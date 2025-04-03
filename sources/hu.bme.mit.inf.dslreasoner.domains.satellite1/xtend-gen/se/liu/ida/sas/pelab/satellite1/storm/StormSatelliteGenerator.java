package se.liu.ida.sas.pelab.satellite1.storm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
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
import se.liu.ida.sas.pelab.storm.transformation.StormGeneration;

@SuppressWarnings("all")
public class StormSatelliteGenerator {
  private static final String operational = "op";

  private static final String ready = "rdy";

  private static final String online = "on";

  private static final String available = "av";

  private static final String coverage = "cov";

  public Pair<String, List<String>> generateFrom(final InterferometryMission model) {
    StringConcatenation _builder = new StringConcatenation();
    String _operational = this.operational(model);
    _builder.append(_operational);
    _builder.newLineIfNotEmpty();
    String _ready = this.ready(model);
    _builder.append(_ready);
    _builder.newLineIfNotEmpty();
    CharSequence _online = this.online(model);
    _builder.append(_online);
    _builder.newLineIfNotEmpty();
    String _payload = this.payload(model);
    _builder.append(_payload);
    _builder.newLineIfNotEmpty();
    String _coverage = this.coverage(model);
    _builder.append(_coverage);
    _builder.newLineIfNotEmpty();
    final String stormmodel = _builder.toString();
    final Function1<Spacecraft, Payload> _function = (Spacecraft it) -> {
      return it.getPayload();
    };
    final Function1<Payload, Boolean> _function_1 = (Payload it) -> {
      return Boolean.valueOf((it != null));
    };
    int _size = IterableExtensions.size(IterableExtensions.<Payload>filter(ListExtensions.<Spacecraft, Payload>map(model.getSpacecraft(), _function), _function_1));
    final Function1<Integer, String> _function_2 = (Integer it) -> {
      return StormGeneration.topEvent(this.key((StormSatelliteGenerator.coverage + it)));
    };
    final List<String> tops = IterableExtensions.<String>toList(IterableExtensions.<Integer, String>map(new IntegerRange(2, _size), _function_2));
    return Pair.<String, List<String>>of(stormmodel, tops);
  }

  public String coverage(final InterferometryMission mission) {
    final Function1<Spacecraft, Payload> _function = (Spacecraft it) -> {
      return it.getPayload();
    };
    final Function1<Payload, Boolean> _function_1 = (Payload it) -> {
      return Boolean.valueOf((it != null));
    };
    final Iterable<Payload> payloads = IterableExtensions.<Payload>filter(ListExtensions.<Spacecraft, Payload>map(mission.getSpacecraft(), _function), _function_1);
    int _size = IterableExtensions.size(payloads);
    IntegerRange _upTo = new IntegerRange(2, _size);
    for (final Integer count : _upTo) {
    }
    StringConcatenation _builder = new StringConcatenation();
    {
      int _size_1 = IterableExtensions.size(payloads);
      IntegerRange _upTo_1 = new IntegerRange(2, _size_1);
      for(final Integer cnt : _upTo_1) {
        final Function1<Payload, String> _function_2 = (Payload it) -> {
          return this.key(StormSatelliteGenerator.available, it);
        };
        String _kof = StormGeneration.kof(cnt, this.key((StormSatelliteGenerator.coverage + cnt)), 
          ((String[])Conversions.unwrapArray(IterableExtensions.<Payload, String>map(payloads, _function_2), String.class)));
        _builder.append(_kof);
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }

  public String payload(final InterferometryMission mission) {
    final Function1<Spacecraft, Payload> _function = (Spacecraft it) -> {
      return it.getPayload();
    };
    final Function1<Payload, Boolean> _function_1 = (Payload it) -> {
      return Boolean.valueOf((it != null));
    };
    final Iterable<Payload> payloads = IterableExtensions.<Payload>filter(ListExtensions.<Spacecraft, Payload>map(mission.getSpacecraft(), _function), _function_1);
    StringConcatenation _builder = new StringConcatenation();
    {
      for(final Payload pld : payloads) {
        String _andGate = StormGeneration.andGate(this.key(StormSatelliteGenerator.available, pld), 
          this.key(StormSatelliteGenerator.online, pld.eContainer()));
        _builder.append(_andGate);
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }

  public CharSequence online(final InterferometryMission mission) {
    CharSequence _xblockexpression = null;
    {
      final String onlineHelper = (StormSatelliteGenerator.online + "LinkHelper");
      final String transmittingHelper = (StormSatelliteGenerator.online + "TransmitHelper");
      final String linkHelper = (StormSatelliteGenerator.online + "LinkHelper");
      StringConcatenation _builder = new StringConcatenation();
      {
        EList<Spacecraft> _spacecraft = mission.getSpacecraft();
        for(final Spacecraft sat : _spacecraft) {
          CharSequence _xblockexpression_1 = null;
          {
            final Function1<CommSubsystem, Boolean> _function = (CommSubsystem it) -> {
              return Boolean.valueOf(((it.getTarget() != null) || (it.getFallback() != null)));
            };
            final Iterable<CommSubsystem> transmittingList = IterableExtensions.<CommSubsystem>filter(sat.getCommSubsystem(), _function);
            StringConcatenation _builder_1 = new StringConcatenation();
            {
              for(final CommSubsystem transmitting : transmittingList) {
                String _orGate = StormGeneration.orGate(this.key(linkHelper, transmitting), 
                  this.key(StormSatelliteGenerator.ready, transmitting.getTarget()), 
                  this.key(StormSatelliteGenerator.ready, transmitting.getFallback()));
                _builder_1.append(_orGate);
                _builder_1.newLineIfNotEmpty();
                String _andGate = StormGeneration.andGate(this.key(transmittingHelper, transmitting), 
                  this.key(StormSatelliteGenerator.operational, transmitting), 
                  this.key(linkHelper, transmitting));
                _builder_1.append(_andGate);
                _builder_1.newLineIfNotEmpty();
              }
            }
            final Function1<CommSubsystem, String> _function_1 = (CommSubsystem it) -> {
              return this.key(transmittingHelper, it);
            };
            String _orGate_1 = StormGeneration.orGate(this.key(onlineHelper, sat), 
              ((String[])Conversions.unwrapArray(IterableExtensions.<CommSubsystem, String>map(transmittingList, _function_1), String.class)));
            _builder_1.append(_orGate_1);
            _builder_1.newLineIfNotEmpty();
            _xblockexpression_1 = _builder_1;
          }
          _builder.append(_xblockexpression_1);
          _builder.newLineIfNotEmpty();
          String _andGate = StormGeneration.andGate(this.key(StormSatelliteGenerator.online, sat), 
            this.key(StormSatelliteGenerator.operational, sat), 
            this.key(onlineHelper, sat));
          _builder.append(_andGate);
          _builder.newLineIfNotEmpty();
        }
      }
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }

  public String ready(final InterferometryMission mission) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<CommSubsystem> _commSubsystem = mission.getGroundStationNetwork().getCommSubsystem();
      for(final CommSubsystem gcomm : _commSubsystem) {
        String _basicEvent = StormGeneration.basicEvent(this.key(StormSatelliteGenerator.ready, gcomm), 1.0);
        _builder.append(_basicEvent);
        _builder.newLineIfNotEmpty();
      }
    }
    {
      EList<Spacecraft> _spacecraft = mission.getSpacecraft();
      for(final Spacecraft sat : _spacecraft) {
        {
          EList<CommSubsystem> _commSubsystem_1 = sat.getCommSubsystem();
          for(final CommSubsystem comm : _commSubsystem_1) {
            String _andGate = StormGeneration.andGate(this.key(StormSatelliteGenerator.ready, comm), 
              this.key(StormSatelliteGenerator.online, sat), 
              this.key(StormSatelliteGenerator.operational, comm));
            _builder.append(_andGate);
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder.toString();
  }

  public String operational(final InterferometryMission mission) {
    final ArrayList<EObject> components = CollectionLiterals.<EObject>newArrayList();
    final Consumer<Spacecraft> _function = (Spacecraft satellite) -> {
      components.add(satellite);
      components.addAll(satellite.getCommSubsystem());
    };
    mission.getSpacecraft().forEach(_function);
    StringConcatenation _builder = new StringConcatenation();
    {
      for(final EObject comp : components) {
        String _basicEvent = StormGeneration.basicEvent(this.key(StormSatelliteGenerator.operational, comp), this.probability(comp));
        _builder.append(_basicEvent);
        _builder.newLineIfNotEmpty();
      }
    }
    final String events = _builder.toString();
    components.addAll(mission.getGroundStationNetwork().getCommSubsystem());
    return events;
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
