package hu.bme.mit.inf.dslreasoner.domains.satellite1;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import satellite1.CommSubsystem;
import satellite1.CubeSat3U;
import satellite1.CubeSat6U;
import satellite1.KaCommSubsystem;
import satellite1.Payload;
import satellite1.SatelliteFactory;
import satellite1.SmallSat;
import satellite1.Spacecraft;
import satellite1.UHFCommSubsystem;
import satellite1.XCommSubsystem;

@SuppressWarnings("all")
public class SatelliteModelGenerator {
  public SatelliteModelWrapper make(final int component, final long seed) {
    final SatelliteModelWrapper wrapper = new SatelliteModelWrapper();
    wrapper.gsn = wrapper.factory.createGroundStationNetwork();
    wrapper.gsnka = wrapper.factory.createKaCommSubsystem();
    wrapper.gsnx = wrapper.factory.createXCommSubsystem();
    wrapper.gsn.getCommSubsystem().add(wrapper.gsnka);
    wrapper.gsn.getCommSubsystem().add(wrapper.gsnx);
    wrapper.mission.setGroundStationNetwork(wrapper.gsn);
    int count = 0;
    Random _random = new Random();
    wrapper.rnd = _random;
    wrapper.rnd.setSeed(seed);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Model seed is ");
    _builder.append(seed);
    InputOutput.<String>println(_builder.toString());
    return this.addToModel(wrapper, component);
  }

  public SatelliteModelWrapper addToModel(final SatelliteModelWrapper wrapper, final int component) {
    final SatelliteFactory factory = SatelliteFactory.eINSTANCE;
    int count = 0;
    while ((count < component)) {
      {
        final int satcode = wrapper.rnd.nextInt(3);
        Spacecraft sat = null;
        switch (satcode) {
          case 0:
            sat = factory.createCubeSat3U();
            break;
          case 1:
            sat = factory.createCubeSat6U();
            break;
          case 2:
            sat = factory.createSmallSat();
            break;
          default:
            throw new RuntimeException("Satellite typecode is out of bounds.");
        }
        CommSubsystem primary = null;
        if ((sat instanceof CubeSat3U)) {
          long _count = wrapper.getCrossUHF().count();
          boolean _greaterEqualsThan = (_count >= 2);
          if (_greaterEqualsThan) {
            final int commcode = wrapper.rnd.nextInt(2);
            if ((commcode == 0)) {
              primary = factory.createUHFCommSubsystem();
              final List<UHFCommSubsystem> comms = this.<UHFCommSubsystem>select2(wrapper.getCrossUHF(), wrapper.rnd);
              primary.setTarget(comms.get(0));
              primary.setFallback(comms.get(1));
            } else {
              primary = factory.createXCommSubsystem();
              final List<XCommSubsystem> comms_1 = this.<XCommSubsystem>select2(wrapper.getAllX(), wrapper.rnd);
              primary.setTarget(comms_1.get(0));
              primary.setFallback(comms_1.get(1));
            }
          } else {
            primary = factory.createXCommSubsystem();
            final List<XCommSubsystem> comms_2 = this.<XCommSubsystem>select2(wrapper.getAllX(), wrapper.rnd);
            primary.setTarget(comms_2.get(0));
            primary.setFallback(comms_2.get(1));
          }
        }
        if ((sat instanceof CubeSat6U)) {
          final int commcode_1 = wrapper.rnd.nextInt(2);
          if (((wrapper.getCrossUHF().count() >= 2) && (commcode_1 == 1))) {
            primary = factory.createUHFCommSubsystem();
            final List<UHFCommSubsystem> comms_3 = this.<UHFCommSubsystem>select2(wrapper.getCrossUHF(), wrapper.rnd);
            primary.setTarget(comms_3.get(0));
            primary.setFallback(comms_3.get(1));
          } else {
            primary = factory.createXCommSubsystem();
            long _count_1 = wrapper.getCrossX().count();
            boolean _greaterEqualsThan_1 = (_count_1 >= 2);
            if (_greaterEqualsThan_1) {
              final List<XCommSubsystem> comms_4 = this.<XCommSubsystem>select2(wrapper.getAllX(), wrapper.rnd);
              primary.setTarget(comms_4.get(0));
              primary.setFallback(comms_4.get(1));
            } else {
              primary.setTarget(wrapper.gsnx);
              primary.setFallback(wrapper.gsnx);
            }
          }
        }
        if ((sat instanceof SmallSat)) {
          int _xifexpression = (int) 0;
          long _count_2 = wrapper.getCrossUHF().count();
          boolean _greaterEqualsThan_2 = (_count_2 >= 2);
          if (_greaterEqualsThan_2) {
            _xifexpression = 3;
          } else {
            _xifexpression = 2;
          }
          final int commcode_2 = wrapper.rnd.nextInt(_xifexpression);
          switch (commcode_2) {
            case 0:
              primary = factory.createXCommSubsystem();
              final List<XCommSubsystem> comms_5 = this.<XCommSubsystem>select2(wrapper.getAllX(), wrapper.rnd);
              primary.setTarget(comms_5.get(0));
              primary.setFallback(comms_5.get(1));
              break;
            case 1:
              primary = factory.createKaCommSubsystem();
              final List<KaCommSubsystem> comms_6 = this.<KaCommSubsystem>select2(wrapper.getAllKa(), wrapper.rnd);
              primary.setTarget(comms_6.get(0));
              primary.setFallback(comms_6.get(1));
              break;
            case 2:
              primary = factory.createUHFCommSubsystem();
              final List<UHFCommSubsystem> comms_7 = this.<UHFCommSubsystem>select2(wrapper.getCrossUHF(), wrapper.rnd);
              primary.setTarget(comms_7.get(0));
              primary.setFallback(comms_7.get(1));
              break;
            default:
              throw new RuntimeException("CommSystem typecode if out of bounds.");
          }
        }
        CommSubsystem seconary = null;
        final int commcode_3 = wrapper.rnd.nextInt(4);
        if ((commcode_3 < 2)) {
          if ((commcode_3 == 0)) {
            seconary = factory.createXCommSubsystem();
          } else {
            seconary = factory.createUHFCommSubsystem();
          }
        } else {
          if (((commcode_3 == 2) && (sat instanceof SmallSat))) {
            seconary = factory.createKaCommSubsystem();
          }
        }
        int _nextInt = wrapper.rnd.nextInt(3);
        boolean _lessThan = (_nextInt < 2);
        if (_lessThan) {
          sat.setPayload(factory.createInterferometryPayload());
        }
        wrapper.mission.getSpacecraft().add(sat);
        count++;
        sat.getCommSubsystem().add(primary);
        count++;
        if ((seconary != null)) {
          sat.getCommSubsystem().add(seconary);
          count++;
        }
        Payload _payload = sat.getPayload();
        boolean _tripleNotEquals = (_payload != null);
        if (_tripleNotEquals) {
          count++;
        }
      }
    }
    return wrapper;
  }

  public <T extends CommSubsystem> List<T> select2(final Stream<T> stream, final Random random) {
    final List<T> objects = stream.toList();
    int _size = objects.size();
    boolean _equals = (_size == 1);
    if (_equals) {
      T _get = objects.get(0);
      T _get_1 = objects.get(0);
      return Collections.<T>unmodifiableList(CollectionLiterals.<T>newArrayList(_get, _get_1));
    }
    final int c1 = random.nextInt(objects.size());
    int _size_1 = objects.size();
    int _minus = (_size_1 - 1);
    int c2 = random.nextInt(_minus);
    if ((c2 >= c1)) {
      c2++;
    }
    T _get_2 = objects.get(c1);
    T _get_3 = objects.get(c2);
    return Collections.<T>unmodifiableList(CollectionLiterals.<T>newArrayList(_get_2, _get_3));
  }

  public void makeRandomChange(final SatelliteModelWrapper wrapper, final int changes) {
    int count = 0;
    while ((count < changes)) {
      {
        System.out.print("Action ");
        final Function0<Boolean> _function = () -> {
          return Boolean.valueOf(this.addPayload(wrapper));
        };
        final Function0<Boolean> _function_1 = () -> {
          return Boolean.valueOf(this.removePayload(wrapper));
        };
        final Function0<Boolean> _function_2 = () -> {
          return Boolean.valueOf(this.addSatellite(wrapper));
        };
        final Function0<Boolean> _function_3 = () -> {
          return Boolean.valueOf(this.removeSatellite(wrapper));
        };
        final Function0<Boolean> _function_4 = () -> {
          return Boolean.valueOf(this.addExtraComm(wrapper));
        };
        final Function0<Boolean> _function_5 = () -> {
          return Boolean.valueOf(this.removeExtraComm(wrapper));
        };
        final Function0<Boolean> _function_6 = () -> {
          return Boolean.valueOf(this.rerouteLink(wrapper));
        };
        final List<Function0<Boolean>> options = Collections.<Function0<Boolean>>unmodifiableList(CollectionLiterals.<Function0<Boolean>>newArrayList(_function, _function_1, _function_2, _function_3, _function_4, _function_5, _function_6));
        final Function0<Boolean> action = options.get(wrapper.rnd.nextInt(options.size()));
        Boolean _apply = action.apply();
        if ((_apply).booleanValue()) {
          InputOutput.<String>println(((((" succeeded. (" + Integer.valueOf((count + 1))) + " of ") + Integer.valueOf(changes)) + ")"));
          count++;
        } else {
          InputOutput.<String>println(((((" failed. (" + Integer.valueOf((count + 1))) + " of ") + Integer.valueOf(changes)) + ")"));
        }
      }
    }
  }

  public boolean addPayload(final SatelliteModelWrapper wrapper) {
    System.out.print("addPayload");
    int _size = wrapper.size();
    boolean _greaterThan = (_size > 60);
    if (_greaterThan) {
      return false;
    }
    final LinkedList<Spacecraft> sats = CollectionLiterals.<Spacecraft>newLinkedList();
    final Function1<Spacecraft, Boolean> _function = (Spacecraft sat) -> {
      Payload _payload = sat.getPayload();
      return Boolean.valueOf((_payload == null));
    };
    Iterables.<Spacecraft>addAll(sats, IterableExtensions.<Spacecraft>filter(wrapper.mission.getSpacecraft(), _function));
    boolean _isEmpty = sats.isEmpty();
    if (_isEmpty) {
      return false;
    }
    Collections.shuffle(sats, wrapper.rnd);
    Spacecraft _get = sats.get(0);
    _get.setPayload(wrapper.factory.createInterferometryPayload());
    return true;
  }

  public boolean removePayload(final SatelliteModelWrapper wrapper) {
    System.out.print("removePayload");
    final Predicate<Spacecraft> _function = (Spacecraft sat) -> {
      Payload _payload = sat.getPayload();
      return (_payload != null);
    };
    final List<Spacecraft> sats = wrapper.mission.getSpacecraft().stream().filter(_function).toList();
    int _size = sats.size();
    boolean _lessEqualsThan = (_size <= 2);
    if (_lessEqualsThan) {
      return false;
    }
    Spacecraft _get = sats.get(wrapper.rnd.nextInt(sats.size()));
    _get.setPayload(null);
    return true;
  }

  public boolean addSatellite(final SatelliteModelWrapper wrapper) {
    System.out.print("addSatellite");
    int _size = wrapper.size();
    boolean _greaterThan = (_size > (60 - 2));
    if (_greaterThan) {
      return false;
    }
    final Function0<Boolean> _function = () -> {
      long _count = wrapper.getCrossUHF().count();
      boolean _lessThan = (_count < 2);
      if (_lessThan) {
        return Boolean.valueOf(false);
      }
      final CubeSat3U sat = wrapper.factory.createCubeSat3U();
      final UHFCommSubsystem com = wrapper.factory.createUHFCommSubsystem();
      sat.getCommSubsystem().add(com);
      final List<UHFCommSubsystem> selected = this.<UHFCommSubsystem>select2(wrapper.getCrossUHF(), wrapper.rnd);
      com.setTarget(selected.get(0));
      com.setFallback(selected.get(1));
      wrapper.mission.getSpacecraft().add(sat);
      return Boolean.valueOf(true);
    };
    final Function0<Boolean> _function_1 = () -> {
      final CubeSat3U sat = wrapper.factory.createCubeSat3U();
      final XCommSubsystem com = wrapper.factory.createXCommSubsystem();
      sat.getCommSubsystem().add(com);
      final List<XCommSubsystem> selected = this.<XCommSubsystem>select2(wrapper.getAllX(), wrapper.rnd);
      com.setTarget(selected.get(0));
      com.setFallback(selected.get(1));
      wrapper.mission.getSpacecraft().add(sat);
      return Boolean.valueOf(true);
    };
    final Function0<Boolean> _function_2 = () -> {
      long _count = wrapper.getCrossUHF().count();
      boolean _lessThan = (_count < 2);
      if (_lessThan) {
        return Boolean.valueOf(false);
      }
      final CubeSat6U sat = wrapper.factory.createCubeSat6U();
      final UHFCommSubsystem com = wrapper.factory.createUHFCommSubsystem();
      sat.getCommSubsystem().add(com);
      final List<UHFCommSubsystem> selected = this.<UHFCommSubsystem>select2(wrapper.getCrossUHF(), wrapper.rnd);
      com.setTarget(selected.get(0));
      com.setFallback(selected.get(1));
      wrapper.mission.getSpacecraft().add(sat);
      return Boolean.valueOf(true);
    };
    final Function0<Boolean> _function_3 = () -> {
      final CubeSat6U sat = wrapper.factory.createCubeSat6U();
      final XCommSubsystem com = wrapper.factory.createXCommSubsystem();
      sat.getCommSubsystem().add(com);
      final List<XCommSubsystem> selected = this.<XCommSubsystem>select2(wrapper.getAllX(), wrapper.rnd);
      com.setTarget(selected.get(0));
      com.setFallback(selected.get(1));
      wrapper.mission.getSpacecraft().add(sat);
      return Boolean.valueOf(true);
    };
    final Function0<Boolean> _function_4 = () -> {
      final SmallSat sat = wrapper.factory.createSmallSat();
      final UHFCommSubsystem com = wrapper.factory.createUHFCommSubsystem();
      sat.getCommSubsystem().add(com);
      final List<UHFCommSubsystem> selected = this.<UHFCommSubsystem>select2(wrapper.getCrossUHF(), wrapper.rnd);
      com.setTarget(selected.get(0));
      com.setFallback(selected.get(1));
      wrapper.mission.getSpacecraft().add(sat);
      return Boolean.valueOf(true);
    };
    final Function0<Boolean> _function_5 = () -> {
      final SmallSat sat = wrapper.factory.createSmallSat();
      final XCommSubsystem com = wrapper.factory.createXCommSubsystem();
      sat.getCommSubsystem().add(com);
      final List<XCommSubsystem> selected = this.<XCommSubsystem>select2(wrapper.getAllX(), wrapper.rnd);
      com.setTarget(selected.get(0));
      com.setFallback(selected.get(1));
      wrapper.mission.getSpacecraft().add(sat);
      return Boolean.valueOf(true);
    };
    final Function0<Boolean> _function_6 = () -> {
      final SmallSat sat = wrapper.factory.createSmallSat();
      final KaCommSubsystem com = wrapper.factory.createKaCommSubsystem();
      sat.getCommSubsystem().add(com);
      final List<KaCommSubsystem> selected = this.<KaCommSubsystem>select2(wrapper.getAllKa(), wrapper.rnd);
      com.setTarget(selected.get(0));
      com.setFallback(selected.get(1));
      wrapper.mission.getSpacecraft().add(sat);
      return Boolean.valueOf(true);
    };
    final List<Function0<Boolean>> options = Collections.<Function0<Boolean>>unmodifiableList(CollectionLiterals.<Function0<Boolean>>newArrayList(_function, _function_1, _function_2, _function_3, _function_4, _function_5, _function_6));
    return (options.get(wrapper.rnd.nextInt(options.size())).apply()).booleanValue();
  }

  public boolean removeSatellite(final SatelliteModelWrapper wrapper) {
    System.out.print("removeSatellite");
    final Spacecraft sat = wrapper.mission.getSpacecraft().get(wrapper.rnd.nextInt(wrapper.mission.getSpacecraft().size()));
    final Function1<CommSubsystem, Pair<CommSubsystem, List<CommSubsystem>>> _function = (CommSubsystem com) -> {
      List<CommSubsystem> _list = wrapper.<CommSubsystem>linkableTo(com).toList();
      return Pair.<CommSubsystem, List<CommSubsystem>>of(com, _list);
    };
    final List<Pair<CommSubsystem, List<CommSubsystem>>> alternatives = ListExtensions.<CommSubsystem, Pair<CommSubsystem, List<CommSubsystem>>>map(sat.getCommSubsystem(), _function);
    final Function1<CommSubsystem, Pair<CommSubsystem, Stream<CommSubsystem>>> _function_1 = (CommSubsystem com) -> {
      Stream<CommSubsystem> _incomingDirect = wrapper.incomingDirect(com);
      return Pair.<CommSubsystem, Stream<CommSubsystem>>of(com, _incomingDirect);
    };
    final List<Pair<CommSubsystem, Stream<CommSubsystem>>> incomings = ListExtensions.<CommSubsystem, Pair<CommSubsystem, Stream<CommSubsystem>>>map(sat.getCommSubsystem(), _function_1);
    final Function2<Boolean, Pair<CommSubsystem, List<CommSubsystem>>, Boolean> _function_2 = (Boolean value, Pair<CommSubsystem, List<CommSubsystem>> alt) -> {
      return Boolean.valueOf(((value).booleanValue() && (wrapper.incomingDirect(alt.getKey()).toList().isEmpty() || (!alt.getValue().isEmpty()))));
    };
    final Boolean precondition = IterableExtensions.<Pair<CommSubsystem, List<CommSubsystem>>, Boolean>fold(alternatives, Boolean.valueOf(true), _function_2);
    final Function2<Boolean, Pair<CommSubsystem, List<CommSubsystem>>, Boolean> _function_3 = (Boolean value, Pair<CommSubsystem, List<CommSubsystem>> alt) -> {
      int _size = alt.getValue().size();
      return Boolean.valueOf((_size >= 2));
    };
    IterableExtensions.<Pair<CommSubsystem, List<CommSubsystem>>, Boolean>fold(alternatives, precondition, _function_3);
    if ((!(precondition).booleanValue())) {
      return false;
    }
    for (final Pair<CommSubsystem, Stream<CommSubsystem>> incoming : incomings) {
      {
        final CommSubsystem trg = incoming.getKey();
        List<CommSubsystem> _list = incoming.getValue().toList();
        for (final CommSubsystem src : _list) {
          {
            CommSubsystem _target = src.getTarget();
            boolean _equals = Objects.equal(_target, trg);
            if (_equals) {
              final ArrayList<CommSubsystem> alts = CollectionLiterals.<CommSubsystem>newArrayList();
              final Function1<Pair<CommSubsystem, List<CommSubsystem>>, Boolean> _function_4 = (Pair<CommSubsystem, List<CommSubsystem>> it) -> {
                return Boolean.valueOf(it.getKey().equals(trg));
              };
              alts.addAll(IterableExtensions.<Pair<CommSubsystem, List<CommSubsystem>>>findFirst(alternatives, _function_4).getValue());
              boolean _contains = wrapper.gsn.getCommSubsystem().contains(src.getFallback());
              boolean _not = (!_contains);
              if (_not) {
                alts.remove(src.getFallback());
              }
              src.setTarget(alts.get(wrapper.rnd.nextInt(alts.size())));
            }
            CommSubsystem _fallback = src.getFallback();
            boolean _equals_1 = Objects.equal(_fallback, trg);
            if (_equals_1) {
              final ArrayList<CommSubsystem> alts_1 = CollectionLiterals.<CommSubsystem>newArrayList();
              final Function1<Pair<CommSubsystem, List<CommSubsystem>>, Boolean> _function_5 = (Pair<CommSubsystem, List<CommSubsystem>> it) -> {
                return Boolean.valueOf(it.getKey().equals(trg));
              };
              alts_1.addAll(IterableExtensions.<Pair<CommSubsystem, List<CommSubsystem>>>findFirst(alternatives, _function_5).getValue());
              boolean _contains_1 = wrapper.gsn.getCommSubsystem().contains(src.getTarget());
              boolean _not_1 = (!_contains_1);
              if (_not_1) {
                alts_1.remove(src.getTarget());
              }
              src.setTarget(alts_1.get(wrapper.rnd.nextInt(alts_1.size())));
            }
          }
        }
      }
    }
    wrapper.mission.getSpacecraft().remove(sat);
    return true;
  }

  public boolean addExtraComm(final SatelliteModelWrapper wrapper) {
    System.out.print("addComm");
    final Function1<Spacecraft, Boolean> _function = (Spacecraft it) -> {
      int _size = it.getCommSubsystem().size();
      return Boolean.valueOf((_size < 2));
    };
    final Iterable<Spacecraft> nored = IterableExtensions.<Spacecraft>filter(wrapper.mission.getSpacecraft(), _function);
    boolean _isEmpty = IterableExtensions.isEmpty(nored);
    if (_isEmpty) {
      return false;
    }
    final Spacecraft sat = ((Spacecraft[])Conversions.unwrapArray(nored, Spacecraft.class))[wrapper.rnd.nextInt(IterableExtensions.size(nored))];
    boolean _matched = false;
    if ((sat instanceof CubeSat3U)) {
      _matched=true;
      boolean _nextBoolean = wrapper.rnd.nextBoolean();
      if (_nextBoolean) {
        sat.getCommSubsystem().add(wrapper.factory.createUHFCommSubsystem());
      } else {
        sat.getCommSubsystem().add(wrapper.factory.createXCommSubsystem());
      }
    }
    if (!_matched) {
      if ((sat instanceof CubeSat6U)) {
        _matched=true;
        boolean _nextBoolean_1 = wrapper.rnd.nextBoolean();
        if (_nextBoolean_1) {
          sat.getCommSubsystem().add(wrapper.factory.createUHFCommSubsystem());
        } else {
          sat.getCommSubsystem().add(wrapper.factory.createXCommSubsystem());
        }
      }
    }
    if (!_matched) {
      if ((sat instanceof SmallSat)) {
        _matched=true;
        int _nextInt = wrapper.rnd.nextInt(3);
        switch (_nextInt) {
          case 0:
            sat.getCommSubsystem().add(wrapper.factory.createUHFCommSubsystem());
            break;
          case 1:
            sat.getCommSubsystem().add(wrapper.factory.createXCommSubsystem());
            break;
          case 2:
            sat.getCommSubsystem().add(wrapper.factory.createKaCommSubsystem());
            break;
        }
      }
    }
    return true;
  }

  public boolean removeExtraComm(final SatelliteModelWrapper wrapper) {
    System.out.print("removeComm");
    final Function1<Spacecraft, Boolean> _function = (Spacecraft it) -> {
      int _size = it.getCommSubsystem().size();
      return Boolean.valueOf((_size == 2));
    };
    final Iterable<Spacecraft> twocom = IterableExtensions.<Spacecraft>filter(wrapper.mission.getSpacecraft(), _function);
    final ArrayList<Pair<Spacecraft, CommSubsystem>> comms = CollectionLiterals.<Pair<Spacecraft, CommSubsystem>>newArrayList();
    final Consumer<Spacecraft> _function_1 = (Spacecraft sat) -> {
      EList<CommSubsystem> _commSubsystem = sat.getCommSubsystem();
      for (final CommSubsystem com : _commSubsystem) {
        {
          final List<CommSubsystem> in = wrapper.incomingDirect(com).toList();
          final List<CommSubsystem> linkable = wrapper.<CommSubsystem>linkableTo(com).toList();
          if ((in.isEmpty() || (linkable.size() >= 2))) {
            Pair<Spacecraft, CommSubsystem> _mappedTo = Pair.<Spacecraft, CommSubsystem>of(sat, com);
            comms.add(_mappedTo);
          }
        }
      }
    };
    twocom.forEach(_function_1);
    boolean _isEmpty = comms.isEmpty();
    if (_isEmpty) {
      return false;
    }
    final Pair<Spacecraft, CommSubsystem> pair = comms.get(wrapper.rnd.nextInt(IterableExtensions.size(twocom)));
    final List<CommSubsystem> alternative = wrapper.<CommSubsystem>linkableTo(pair.getValue()).toList();
    final List<CommSubsystem> incomings = wrapper.incomingDirect(pair.getValue()).toList();
    final CommSubsystem trg = pair.getValue();
    for (final CommSubsystem src : incomings) {
      {
        CommSubsystem _target = src.getTarget();
        boolean _equals = Objects.equal(_target, trg);
        if (_equals) {
          final ArrayList<CommSubsystem> alts = CollectionLiterals.<CommSubsystem>newArrayList();
          alts.addAll(alternative);
          boolean _contains = wrapper.gsn.getCommSubsystem().contains(src.getFallback());
          boolean _not = (!_contains);
          if (_not) {
            alts.remove(src.getFallback());
          }
          src.setTarget(alts.get(wrapper.rnd.nextInt(alts.size())));
        }
        CommSubsystem _fallback = src.getFallback();
        boolean _equals_1 = Objects.equal(_fallback, trg);
        if (_equals_1) {
          final ArrayList<CommSubsystem> alts_1 = CollectionLiterals.<CommSubsystem>newArrayList();
          alts_1.addAll(alternative);
          boolean _contains_1 = wrapper.gsn.getCommSubsystem().contains(src.getTarget());
          boolean _not_1 = (!_contains_1);
          if (_not_1) {
            alts_1.remove(src.getTarget());
          }
          src.setTarget(alts_1.get(wrapper.rnd.nextInt(alts_1.size())));
        }
      }
    }
    return true;
  }

  public boolean rerouteLink(final SatelliteModelWrapper wrapper) {
    System.out.print("rerouteLink");
    final List<Pair<CommSubsystem, CommSubsystem>> links = wrapper.allLinks().toList();
    boolean _isEmpty = links.isEmpty();
    if (_isEmpty) {
      InputOutput.<String>println("Fail on no links");
      return false;
    }
    final Pair<CommSubsystem, CommSubsystem> link = links.get(wrapper.rnd.nextInt(links.size()));
    final Predicate<CommSubsystem> _function = (CommSubsystem it) -> {
      CommSubsystem _value = link.getValue();
      return (it != _value);
    };
    final List<CommSubsystem> alt = wrapper.<CommSubsystem>linkableTo(link.getKey()).filter(_function).toList();
    boolean _isEmpty_1 = alt.isEmpty();
    if (_isEmpty_1) {
      InputOutput.<String>println("Fail on no alternatives");
      return false;
    }
    final CommSubsystem to = alt.get(wrapper.rnd.nextInt(alt.size()));
    CommSubsystem _target = link.getKey().getTarget();
    CommSubsystem _value = link.getValue();
    boolean _tripleEquals = (_target == _value);
    if (_tripleEquals) {
      CommSubsystem _key = link.getKey();
      _key.setTarget(to);
    }
    CommSubsystem _fallback = link.getKey().getFallback();
    CommSubsystem _value_1 = link.getValue();
    boolean _tripleEquals_1 = (_fallback == _value_1);
    if (_tripleEquals_1) {
      CommSubsystem _key_1 = link.getKey();
      _key_1.setFallback(to);
    }
    return true;
  }
}
