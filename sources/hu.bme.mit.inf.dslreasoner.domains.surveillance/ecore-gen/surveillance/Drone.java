/**
 */
package surveillance;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Drone</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link surveillance.Drone#getShots <em>Shots</em>}</li>
 * </ul>
 *
 * @see surveillance.SurveillancePackage#getDrone()
 * @model
 * @generated
 */
public interface Drone extends MovingObject {

	/**
	 * Returns the value of the '<em><b>Shots</b></em>' containment reference list.
	 * The list contents are of type {@link surveillance.Shot}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shots</em>' containment reference list.
	 * @see surveillance.SurveillancePackage#getDrone_Shots()
	 * @model containment="true"
	 * @generated
	 */
	EList<Shot> getShots();

} // Drone
