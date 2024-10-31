package hu.bme.mit.inf.dslreasoner.domains.surveillance.viatra;

import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.Coordinate;
import java.util.Collections;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import surveillance.Drone;
import uncertaindatatypes.UReal;

@SuppressWarnings("all")
public class SurveillanceIncrementalChanges {
  private final SurveillanceWrapper parent;

  public SurveillanceIncrementalChanges(final SurveillanceWrapper wrapper) {
    this.parent = wrapper;
  }

  public Coordinate getNewCoordinate(final int i, final int j) {
    final Integer off_x = Collections.<Integer>unmodifiableList(CollectionLiterals.<Integer>newArrayList(Integer.valueOf(0), Integer.valueOf(500), Integer.valueOf(700), Integer.valueOf(1000), Integer.valueOf(1000))).get((j % 5));
    final Integer off_y = Collections.<Integer>unmodifiableList(CollectionLiterals.<Integer>newArrayList(Integer.valueOf(0), Integer.valueOf(700), Integer.valueOf(700), Integer.valueOf(900), Integer.valueOf(900))).get((j % 5));
    UReal _uReal = new UReal(((off_x).intValue() + (i * 1001)), 0.1);
    UReal _uReal_1 = new UReal(((off_y).intValue() + (i * 1001)), 0.1);
    final Coordinate coord = new Coordinate(_uReal, _uReal_1);
    return coord;
  }

  public void addDrone() {
    final Drone drove = this.parent.factory.createDrone();
  }
}
