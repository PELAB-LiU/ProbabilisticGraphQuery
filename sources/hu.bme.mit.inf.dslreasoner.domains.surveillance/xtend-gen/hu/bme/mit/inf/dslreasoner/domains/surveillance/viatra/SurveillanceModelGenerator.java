package hu.bme.mit.inf.dslreasoner.domains.surveillance.viatra;

import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.Coordinate;
import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.SurveillanceHelper;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import surveillance.Drone;
import surveillance.MovingObject;
import surveillance.SurveillanceFactory;
import surveillance.UnidentifiedObject;
import uncertaindatatypes.UReal;

@SuppressWarnings("all")
public class SurveillanceModelGenerator {
  public void makeRadomDrone(final int size, final Random rnd) {
    final Drone drone = SurveillanceFactory.eINSTANCE.createDrone();
    double _nextDouble = rnd.nextDouble((2 * Math.PI), 0.1);
    UReal _uReal = new UReal(_nextDouble);
    drone.setAngle(_uReal);
    UReal _uReal_1 = new UReal(20, 0.1);
    drone.setSpeed(_uReal_1);
    double _nextDouble_1 = rnd.nextDouble((1001 * (size / 5.0)));
    UReal _uReal_2 = new UReal(_nextDouble_1, 0.1);
    double _nextDouble_2 = rnd.nextDouble((1001 * (size / 5.0)));
    UReal _uReal_3 = new UReal(_nextDouble_2, 0.1);
    Coordinate _coordinate = new Coordinate(_uReal_2, _uReal_3);
    drone.setPosition(_coordinate);
  }

  public void makeRadomTarget(final int size, final Random rnd) {
    final Drone drone = SurveillanceFactory.eINSTANCE.createDrone();
    double _nextDouble = rnd.nextDouble((2 * Math.PI), 0.1);
    UReal _uReal = new UReal(_nextDouble);
    drone.setAngle(_uReal);
    double _nextDouble_1 = rnd.nextDouble(10);
    double _plus = (50 + _nextDouble_1);
    UReal _uReal_1 = new UReal(_plus, 0.2);
    drone.setSpeed(_uReal_1);
    double _nextDouble_2 = rnd.nextDouble((1001 * (size / 5.0)));
    UReal _uReal_2 = new UReal(_nextDouble_2, 0.1);
    double _nextDouble_3 = rnd.nextDouble((1001 * (size / 5.0)));
    UReal _uReal_3 = new UReal(_nextDouble_3, 0.1);
    Coordinate _coordinate = new Coordinate(_uReal_2, _uReal_3);
    drone.setPosition(_coordinate);
  }

  public SurveillanceWrapper make(final int size, final int seed) {
    final SurveillanceWrapper wrapper = new SurveillanceWrapper(seed);
    final SurveillanceFactory factory = wrapper.factory;
    IntegerRange _upTo = new IntegerRange(1, (size / 5));
    for (final int i : _upTo) {
      {
        final Drone d1 = factory.createDrone();
        UReal _uReal = new UReal((0 + (1001 * i)), 0.1);
        UReal _uReal_1 = new UReal((0 + (1001 * i)), 0.1);
        Coordinate _coordinate = new Coordinate(_uReal, _uReal_1);
        d1.setPosition(_coordinate);
        UReal _uReal_2 = new UReal(20, 0.1);
        d1.setSpeed(_uReal_2);
        UReal _uReal_3 = new UReal(0.78, 0.02);
        d1.setAngle(_uReal_3);
        wrapper.model.getObjects().add(d1);
        wrapper.ordering.put(d1, Integer.valueOf(wrapper.newId()));
        final Drone d2 = factory.createDrone();
        UReal _uReal_4 = new UReal((500 + (1001 * i)), 0.1);
        UReal _uReal_5 = new UReal((700 + (1001 * i)), 0.1);
        Coordinate _coordinate_1 = new Coordinate(_uReal_4, _uReal_5);
        d2.setPosition(_coordinate_1);
        UReal _uReal_6 = new UReal(20, 0.1);
        d2.setSpeed(_uReal_6);
        UReal _uReal_7 = new UReal(1.5, 0.02);
        d2.setAngle(_uReal_7);
        wrapper.model.getObjects().add(d2);
        wrapper.ordering.put(d2, Integer.valueOf(wrapper.newId()));
        final UnidentifiedObject o1 = factory.createUnidentifiedObject();
        UReal _uReal_8 = new UReal(50, 0.2);
        o1.setSpeed(_uReal_8);
        UReal _uReal_9 = new UReal((700 + (1001 * i)), 0.1);
        UReal _uReal_10 = new UReal((700 + (1001 * i)), 0.1);
        Coordinate _coordinate_2 = new Coordinate(_uReal_9, _uReal_10);
        o1.setPosition(_coordinate_2);
        o1.setConfidence(0.98);
        UReal _uReal_11 = new UReal(3.92, 0.07);
        o1.setAngle(_uReal_11);
        wrapper.model.getObjects().add(o1);
        wrapper.ordering.put(o1, Integer.valueOf(wrapper.newId()));
        final UnidentifiedObject o2 = factory.createUnidentifiedObject();
        UReal _uReal_12 = new UReal(60, 0.2);
        o2.setSpeed(_uReal_12);
        UReal _uReal_13 = new UReal((1000 + (1001 * i)), 0.1);
        UReal _uReal_14 = new UReal((900 + (1001 * i)), 0.1);
        Coordinate _coordinate_3 = new Coordinate(_uReal_13, _uReal_14);
        o2.setPosition(_coordinate_3);
        o2.setConfidence(0.98);
        UReal _uReal_15 = new UReal(3.14, 0.07);
        o2.setAngle(_uReal_15);
        wrapper.model.getObjects().add(o2);
        wrapper.ordering.put(o2, Integer.valueOf(wrapper.newId()));
        final UnidentifiedObject o3 = factory.createUnidentifiedObject();
        UReal _uReal_16 = new UReal(60, 0.2);
        o3.setSpeed(_uReal_16);
        UReal _uReal_17 = new UReal((1000 + (1001 * i)), 0.1);
        UReal _uReal_18 = new UReal((900 + (1001 * i)), 0.1);
        Coordinate _coordinate_4 = new Coordinate(_uReal_17, _uReal_18);
        o3.setPosition(_coordinate_4);
        o3.setConfidence(0.98);
        UReal _uReal_19 = new UReal(3.14, 0.07);
        o3.setAngle(_uReal_19);
        wrapper.model.getObjects().add(o3);
        wrapper.ordering.put(o3, Integer.valueOf(wrapper.newId()));
      }
    }
    return wrapper;
  }

  public void iterate(final SurveillanceWrapper wrapper, final double threshold) {
    final Predicate<MovingObject> _function = (MovingObject obj) -> {
      double _nextDouble = wrapper.rnd.nextDouble();
      return (_nextDouble < threshold);
    };
    wrapper.model.getObjects().removeIf(_function);
    final Consumer<MovingObject> _function_1 = (MovingObject obj) -> {
      obj.setPosition(SurveillanceHelper.move(obj.getPosition(), obj.getSpeed(), obj.getAngle(), 1));
    };
    wrapper.model.getObjects().forEach(_function_1);
  }
}
