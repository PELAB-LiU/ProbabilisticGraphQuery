/**
 */
package satellite1;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Constellation Mission</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link satellite1.ConstellationMission#getGroundStationNetwork <em>Ground Station Network</em>}</li>
 *   <li>{@link satellite1.ConstellationMission#getSpacecraft <em>Spacecraft</em>}</li>
 * </ul>
 *
 * @see satellite1.SatellitePackage#getConstellationMission()
 * @model
 * @generated
 */
public interface ConstellationMission extends EObject {
	/**
	 * Returns the value of the '<em><b>Ground Station Network</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ground Station Network</em>' containment reference.
	 * @see #setGroundStationNetwork(GroundStationNetwork)
	 * @see satellite1.SatellitePackage#getConstellationMission_GroundStationNetwork()
	 * @model containment="true" required="true"
	 * @generated
	 */
	GroundStationNetwork getGroundStationNetwork();

	/**
	 * Sets the value of the '{@link satellite1.ConstellationMission#getGroundStationNetwork <em>Ground Station Network</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ground Station Network</em>' containment reference.
	 * @see #getGroundStationNetwork()
	 * @generated
	 */
	void setGroundStationNetwork(GroundStationNetwork value);

	/**
	 * Returns the value of the '<em><b>Spacecraft</b></em>' containment reference list.
	 * The list contents are of type {@link satellite1.Spacecraft}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Spacecraft</em>' containment reference list.
	 * @see satellite1.SatellitePackage#getConstellationMission_Spacecraft()
	 * @model containment="true" lower="2"
	 * @generated
	 */
	EList<Spacecraft> getSpacecraft();

} // ConstellationMission
