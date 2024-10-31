/**
 */
package surveillance;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link surveillance.SurveillanceModel#getObjects <em>Objects</em>}</li>
 * </ul>
 *
 * @see surveillance.SurveillancePackage#getSurveillanceModel()
 * @model
 * @generated
 */
public interface SurveillanceModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Objects</b></em>' containment reference list.
	 * The list contents are of type {@link surveillance.MovingObject}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Objects</em>' containment reference list.
	 * @see surveillance.SurveillancePackage#getSurveillanceModel_Objects()
	 * @model containment="true"
	 * @generated
	 */
	EList<MovingObject> getObjects();

} // SurveillanceModel
