/**
 */
package smarthome;

import hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities.Location;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Home</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link smarthome.Home#getMeasurements <em>Measurements</em>}</li>
 *   <li>{@link smarthome.Home#getLocation <em>Location</em>}</li>
 * </ul>
 *
 * @see smarthome.SmarthomePackage#getHome()
 * @model
 * @generated
 */
public interface Home extends EObject {
	/**
	 * Returns the value of the '<em><b>Measurements</b></em>' containment reference list.
	 * The list contents are of type {@link smarthome.Measurement}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Measurements</em>' containment reference list.
	 * @see smarthome.SmarthomePackage#getHome_Measurements()
	 * @model containment="true"
	 * @generated
	 */
	EList<Measurement> getMeasurements();

	/**
	 * Returns the value of the '<em><b>Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Location</em>' attribute.
	 * @see #setLocation(Location)
	 * @see smarthome.SmarthomePackage#getHome_Location()
	 * @model dataType="smarthome.Location" required="true"
	 * @generated
	 */
	Location getLocation();

	/**
	 * Sets the value of the '{@link smarthome.Home#getLocation <em>Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Location</em>' attribute.
	 * @see #getLocation()
	 * @generated
	 */
	void setLocation(Location value);

} // Home
