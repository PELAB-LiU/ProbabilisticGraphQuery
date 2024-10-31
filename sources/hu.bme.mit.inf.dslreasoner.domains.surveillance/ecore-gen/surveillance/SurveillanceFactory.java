/**
 */
package surveillance;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see surveillance.SurveillancePackage
 * @generated
 */
public interface SurveillanceFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SurveillanceFactory eINSTANCE = surveillance.impl.SurveillanceFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Drone</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Drone</em>'.
	 * @generated
	 */
	Drone createDrone();

	/**
	 * Returns a new object of class '<em>Unidentified Object</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Unidentified Object</em>'.
	 * @generated
	 */
	UnidentifiedObject createUnidentifiedObject();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	SurveillanceModel createSurveillanceModel();

	/**
	 * Returns a new object of class '<em>Shot</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Shot</em>'.
	 * @generated
	 */
	Shot createShot();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	SurveillancePackage getSurveillancePackage();

} //SurveillanceFactory
