/**
 */
package smarthome;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Smarthome</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link smarthome.Smarthome#getPersons <em>Persons</em>}</li>
 *   <li>{@link smarthome.Smarthome#getHomes <em>Homes</em>}</li>
 * </ul>
 *
 * @see smarthome.SmarthomePackage#getSmarthome()
 * @model
 * @generated
 */
public interface Smarthome extends EObject {
	/**
	 * Returns the value of the '<em><b>Persons</b></em>' containment reference list.
	 * The list contents are of type {@link smarthome.Person}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Persons</em>' containment reference list.
	 * @see smarthome.SmarthomePackage#getSmarthome_Persons()
	 * @model containment="true"
	 * @generated
	 */
	EList<Person> getPersons();

	/**
	 * Returns the value of the '<em><b>Homes</b></em>' containment reference list.
	 * The list contents are of type {@link smarthome.Home}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Homes</em>' containment reference list.
	 * @see smarthome.SmarthomePackage#getSmarthome_Homes()
	 * @model containment="true"
	 * @generated
	 */
	EList<Home> getHomes();

} // Smarthome
