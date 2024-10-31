package hu.bme.mit.inf.dslreasoner.domains.satellite1;

import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import satellite1.CommSubsystem;
import satellite1.CommunicatingElement;
import satellite1.GroundStationNetwork;
import satellite1.InterferometryMission;
import satellite1.KaCommSubsystem;
import satellite1.Payload;
import satellite1.SatelliteFactory;
import satellite1.Spacecraft;
import satellite1.UHFCommSubsystem;
import satellite1.XCommSubsystem;

@SuppressWarnings("all")
public class SatelliteModelWrapper {
  public final SatelliteFactory factory;

  public final InterferometryMission mission;

  public GroundStationNetwork gsn;

  public KaCommSubsystem gsnka;

  public XCommSubsystem gsnx;

  public Stream<CommSubsystem> getSatComms() {
    final Function<Spacecraft, Stream<CommSubsystem>> _function = (Spacecraft sat) -> {
      return sat.getCommSubsystem().stream();
    };
    return this.mission.getSpacecraft().stream().<CommSubsystem>flatMap(_function);
  }

  public Stream<KaCommSubsystem> getCrossKa() {
    final Function<Spacecraft, Stream<CommSubsystem>> _function = (Spacecraft sat) -> {
      return sat.getCommSubsystem().stream();
    };
    final Predicate<CommSubsystem> _function_1 = (CommSubsystem it) -> {
      return (it instanceof KaCommSubsystem);
    };
    final Function<CommSubsystem, KaCommSubsystem> _function_2 = (CommSubsystem it) -> {
      return ((KaCommSubsystem) it);
    };
    return this.mission.getSpacecraft().stream().<CommSubsystem>flatMap(_function).filter(_function_1).<KaCommSubsystem>map(_function_2);
  }

  public Stream<XCommSubsystem> getCrossX() {
    final Function<Spacecraft, Stream<CommSubsystem>> _function = (Spacecraft sat) -> {
      return sat.getCommSubsystem().stream();
    };
    final Predicate<CommSubsystem> _function_1 = (CommSubsystem it) -> {
      return (it instanceof XCommSubsystem);
    };
    final Function<CommSubsystem, XCommSubsystem> _function_2 = (CommSubsystem it) -> {
      return ((XCommSubsystem) it);
    };
    return this.mission.getSpacecraft().stream().<CommSubsystem>flatMap(_function).filter(_function_1).<XCommSubsystem>map(_function_2);
  }

  public Stream<UHFCommSubsystem> getCrossUHF() {
    final Function<Spacecraft, Stream<CommSubsystem>> _function = (Spacecraft sat) -> {
      return sat.getCommSubsystem().stream();
    };
    final Predicate<CommSubsystem> _function_1 = (CommSubsystem it) -> {
      return (it instanceof UHFCommSubsystem);
    };
    final Function<CommSubsystem, UHFCommSubsystem> _function_2 = (CommSubsystem it) -> {
      return ((UHFCommSubsystem) it);
    };
    return this.mission.getSpacecraft().stream().<CommSubsystem>flatMap(_function).filter(_function_1).<UHFCommSubsystem>map(_function_2);
  }

  public Stream<KaCommSubsystem> getAllKa() {
    return Stream.<KaCommSubsystem>concat(Stream.<KaCommSubsystem>of(this.gsnka), this.getCrossKa());
  }

  public Stream<XCommSubsystem> getAllX() {
    return Stream.<XCommSubsystem>concat(Stream.<XCommSubsystem>of(this.gsnx), this.getCrossX());
  }

  public Stream<CommSubsystem> incomingDirect(final CommSubsystem com) {
    final Predicate<CommSubsystem> _function = (CommSubsystem src) -> {
      return ((src.getTarget() == com) || (src.getFallback() == com));
    };
    return this.getSatComms().filter(_function);
  }

  public Stream<CommSubsystem> incomingDirectAndRelay(final CommSubsystem com) {
    EObject _eContainer = com.eContainer();
    final CommunicatingElement container = ((CommunicatingElement) _eContainer);
    final Function<CommSubsystem, Stream<CommSubsystem>> _function = (CommSubsystem trg) -> {
      final Predicate<CommSubsystem> _function_1 = (CommSubsystem src) -> {
        return ((src.getTarget() == com) || (src.getFallback() == com));
      };
      return this.getSatComms().filter(_function_1);
    };
    final Stream<CommSubsystem> connected = container.getCommSubsystem().stream().<CommSubsystem>flatMap(_function);
    final Predicate<CommSubsystem> _function_1 = (CommSubsystem it) -> {
      return (it != com);
    };
    return Stream.<CommSubsystem>concat(container.getCommSubsystem().stream().filter(_function_1), connected);
  }

  public <T extends Object> Stream<T> ofType(final Stream<?> source, final T example) {
    final Predicate<Object> _function = (Object it) -> {
      return it.getClass().equals(example.getClass());
    };
    final Function<Object, T> _function_1 = (Object it) -> {
      return ((T) it);
    };
    return source.filter(_function).<T>map(_function_1);
  }

  public Set<CommSubsystem> incomingTransitiveDirectAndRelay(final CommSubsystem trg, final Set<CommSubsystem> buffer) {
    boolean _contains = buffer.contains(trg);
    if (_contains) {
      return buffer;
    }
    buffer.add(trg);
    final Consumer<CommSubsystem> _function = (CommSubsystem src) -> {
      this.incomingTransitiveDirectAndRelay(src, buffer);
    };
    this.incomingDirectAndRelay(trg).forEach(_function);
    return buffer;
  }

  public <T extends CommSubsystem> Stream<T> linkableTo(final T com) {
    final Set<CommSubsystem> incoming = this.incomingTransitiveDirectAndRelay(com, CollectionLiterals.<CommSubsystem>newHashSet());
    final Function<Stream<? extends CommSubsystem>, Stream<? extends CommSubsystem>> _function = (Stream<? extends CommSubsystem> it) -> {
      return it;
    };
    final Predicate<CommSubsystem> _function_1 = (CommSubsystem it) -> {
      return it.getClass().equals(com.getClass());
    };
    final Predicate<CommSubsystem> _function_2 = (CommSubsystem it) -> {
      return (it != com);
    };
    final Function<CommSubsystem, T> _function_3 = (CommSubsystem it) -> {
      return ((T) it);
    };
    final Predicate<T> _function_4 = (T it) -> {
      boolean _contains = incoming.contains(it);
      return (!_contains);
    };
    return Stream.<Stream<? extends CommSubsystem>>of(this.getAllKa(), this.getAllX(), this.getCrossUHF()).<CommSubsystem>flatMap(_function).filter(_function_1).filter(_function_2).<T>map(_function_3).filter(_function_4);
  }

  public Stream<Pair<CommSubsystem, CommSubsystem>> allTarget() {
    final Function<Spacecraft, Stream<CommSubsystem>> _function = (Spacecraft sat) -> {
      return sat.getCommSubsystem().stream();
    };
    final Function<CommSubsystem, Stream<Pair<CommSubsystem, CommSubsystem>>> _function_1 = (CommSubsystem com) -> {
      Stream<Pair<CommSubsystem, CommSubsystem>> _xifexpression = null;
      CommSubsystem _target = com.getTarget();
      boolean _tripleNotEquals = (_target != null);
      if (_tripleNotEquals) {
        CommSubsystem _target_1 = com.getTarget();
        Pair<CommSubsystem, CommSubsystem> _mappedTo = Pair.<CommSubsystem, CommSubsystem>of(com, _target_1);
        _xifexpression = Stream.<Pair<CommSubsystem, CommSubsystem>>of(_mappedTo);
      } else {
        _xifexpression = Stream.<Pair<CommSubsystem, CommSubsystem>>of();
      }
      return _xifexpression;
    };
    return this.mission.getSpacecraft().stream().<CommSubsystem>flatMap(_function).<Pair<CommSubsystem, CommSubsystem>>flatMap(_function_1);
  }

  public Stream<Pair<CommSubsystem, CommSubsystem>> allFallback() {
    final Function<Spacecraft, Stream<CommSubsystem>> _function = (Spacecraft sat) -> {
      return sat.getCommSubsystem().stream();
    };
    final Function<CommSubsystem, Stream<Pair<CommSubsystem, CommSubsystem>>> _function_1 = (CommSubsystem com) -> {
      Stream<Pair<CommSubsystem, CommSubsystem>> _xifexpression = null;
      CommSubsystem _fallback = com.getFallback();
      boolean _tripleNotEquals = (_fallback != null);
      if (_tripleNotEquals) {
        CommSubsystem _fallback_1 = com.getFallback();
        Pair<CommSubsystem, CommSubsystem> _mappedTo = Pair.<CommSubsystem, CommSubsystem>of(com, _fallback_1);
        _xifexpression = Stream.<Pair<CommSubsystem, CommSubsystem>>of(_mappedTo);
      } else {
        _xifexpression = Stream.<Pair<CommSubsystem, CommSubsystem>>of();
      }
      return _xifexpression;
    };
    return this.mission.getSpacecraft().stream().<CommSubsystem>flatMap(_function).<Pair<CommSubsystem, CommSubsystem>>flatMap(_function_1);
  }

  public Stream<Pair<CommSubsystem, CommSubsystem>> allLinks() {
    return Stream.<Pair<CommSubsystem, CommSubsystem>>concat(this.allTarget(), this.allFallback());
  }

  public CommSubsystem transmitting(final Spacecraft sat) {
    final Function1<CommSubsystem, Boolean> _function = (CommSubsystem it) -> {
      return Boolean.valueOf(((it.getTarget() != null) || (it.getFallback() != null)));
    };
    return IterableExtensions.<CommSubsystem>findFirst(sat.getCommSubsystem(), _function);
  }

  public Random rnd;

  public SatelliteModelWrapper() {
    this.factory = SatelliteFactory.eINSTANCE;
    this.mission = this.factory.createInterferometryMission();
  }

  public int size() {
    int size = 0;
    GroundStationNetwork _groundStationNetwork = this.mission.getGroundStationNetwork();
    boolean _tripleNotEquals = (_groundStationNetwork != null);
    if (_tripleNotEquals) {
      size++;
      final Function2<Integer, CommSubsystem, Integer> _function = (Integer old, CommSubsystem __) -> {
        return Integer.valueOf(((old).intValue() + 1));
      };
      size = (IterableExtensions.<CommSubsystem, Integer>fold(this.mission.getGroundStationNetwork().getCommSubsystem(), Integer.valueOf(size), _function)).intValue();
    }
    final Function2<Integer, Spacecraft, Integer> _function_1 = (Integer old, Spacecraft spc) -> {
      int _xblockexpression = (int) 0;
      {
        int value = ((old).intValue() + 1);
        Payload _payload = spc.getPayload();
        boolean _tripleNotEquals_1 = (_payload != null);
        if (_tripleNotEquals_1) {
          value++;
        }
        int _value = value;
        int _size = spc.getCommSubsystem().size();
        value = (_value + _size);
        _xblockexpression = value;
      }
      return Integer.valueOf(_xblockexpression);
    };
    return (int) IterableExtensions.<Spacecraft, Integer>fold(this.mission.getSpacecraft(), Integer.valueOf(size), _function_1);
  }
}
