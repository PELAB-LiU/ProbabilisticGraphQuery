package hu.bme.mit.inf.dslreasoner.domains.surveillance.viatra;

import java.util.Map;
import java.util.Random;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import surveillance.MovingObject;
import surveillance.SurveillanceFactory;
import surveillance.SurveillanceModel;

@SuppressWarnings("all")
public class SurveillanceWrapper {
  public final SurveillanceFactory factory;

  public final SurveillanceModel model;

  public final Map<EObject, Integer> ordering = CollectionLiterals.<EObject, Integer>newHashMap();

  public final Random rnd;

  private int id = 1;

  public int newId() {
    return this.id++;
  }

  public SurveillanceWrapper(final int seed) {
    this.factory = SurveillanceFactory.eINSTANCE;
    this.model = this.factory.createSurveillanceModel();
    Random _random = new Random(seed);
    this.rnd = _random;
  }

  public SurveillanceComparator getComparator() {
    return new SurveillanceComparator(this.ordering);
  }

  public int size() {
    return this.model.getObjects().size();
  }

  public MovingObject ofHashCode(final int hashcode) {
    final Function1<MovingObject, Boolean> _function = (MovingObject it) -> {
      int _hashCode = it.hashCode();
      return Boolean.valueOf((_hashCode == hashcode));
    };
    return IterableExtensions.<MovingObject>findFirst(this.model.getObjects(), _function);
  }
}
