/**
 */
package smarthome;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import uncertaindatatypes.UBoolean;
import uncertaindatatypes.UReal;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Measurement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link smarthome.Measurement#getTemp <em>Temp</em>}</li>
 *   <li>{@link smarthome.Measurement#getCo <em>Co</em>}</li>
 *   <li>{@link smarthome.Measurement#getDopen <em>Dopen</em>}</li>
 *   <li>{@link smarthome.Measurement#getTime <em>Time</em>}</li>
 *   <li>{@link smarthome.Measurement#getAthome <em>Athome</em>}</li>
 * </ul>
 *
 * @see smarthome.SmarthomePackage#getMeasurement()
 * @model
 * @generated
 */
public interface Measurement extends EObject {
	/**
	 * Returns the value of the '<em><b>Temp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Temp</em>' attribute.
	 * @see #setTemp(UReal)
	 * @see smarthome.SmarthomePackage#getMeasurement_Temp()
	 * @model dataType="smarthome.UReal" required="true"
	 * @generated
	 */
	UReal getTemp();

	/**
	 * Sets the value of the '{@link smarthome.Measurement#getTemp <em>Temp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Temp</em>' attribute.
	 * @see #getTemp()
	 * @generated
	 */
	void setTemp(UReal value);

	/**
	 * Returns the value of the '<em><b>Co</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Co</em>' attribute.
	 * @see #setCo(UReal)
	 * @see smarthome.SmarthomePackage#getMeasurement_Co()
	 * @model dataType="smarthome.UReal" required="true"
	 * @generated
	 */
	UReal getCo();

	/**
	 * Sets the value of the '{@link smarthome.Measurement#getCo <em>Co</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Co</em>' attribute.
	 * @see #getCo()
	 * @generated
	 */
	void setCo(UReal value);

	/**
	 * Returns the value of the '<em><b>Dopen</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dopen</em>' attribute.
	 * @see #setDopen(UBoolean)
	 * @see smarthome.SmarthomePackage#getMeasurement_Dopen()
	 * @model dataType="smarthome.UBoolean" required="true"
	 * @generated
	 */
	UBoolean getDopen();

	/**
	 * Sets the value of the '{@link smarthome.Measurement#getDopen <em>Dopen</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dopen</em>' attribute.
	 * @see #getDopen()
	 * @generated
	 */
	void setDopen(UBoolean value);

	/**
	 * Returns the value of the '<em><b>Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Time</em>' attribute.
	 * @see #setTime(UReal)
	 * @see smarthome.SmarthomePackage#getMeasurement_Time()
	 * @model dataType="smarthome.UReal" required="true"
	 * @generated
	 */
	UReal getTime();

	/**
	 * Sets the value of the '{@link smarthome.Measurement#getTime <em>Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Time</em>' attribute.
	 * @see #getTime()
	 * @generated
	 */
	void setTime(UReal value);

	/**
	 * Returns the value of the '<em><b>Athome</b></em>' reference list.
	 * The list contents are of type {@link smarthome.Person}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Athome</em>' reference list.
	 * @see smarthome.SmarthomePackage#getMeasurement_Athome()
	 * @model
	 * @generated
	 */
	EList<Person> getAthome();

} // Measurement
