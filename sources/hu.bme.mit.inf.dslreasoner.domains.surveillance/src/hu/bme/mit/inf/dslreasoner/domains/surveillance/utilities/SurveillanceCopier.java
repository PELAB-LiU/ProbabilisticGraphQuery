package hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;

public class SurveillanceCopier extends Copier {
	public SurveillanceCopier() {
		super(true,false);
	}
	@Override
	protected void copyAttributeValue(EAttribute eAttribute, EObject eObject, Object value, EStructuralFeature.Setting setting)
    {
		if(value instanceof Coordinate coordinate) {
			setting.set(new Coordinate(coordinate.x, coordinate.y));
			return;
		}
      super.copyAttributeValue(eAttribute,eObject,value,setting);
    }
}
