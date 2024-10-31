/**
 */
package surveillance;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Unidentified Object</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link surveillance.UnidentifiedObject#getConfidence <em>Confidence</em>}</li>
 * </ul>
 *
 * @see surveillance.SurveillancePackage#getUnidentifiedObject()
 * @model
 * @generated
 */
public interface UnidentifiedObject extends MovingObject {
	/**
	 * Returns the value of the '<em><b>Confidence</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Confidence</em>' attribute.
	 * @see #setConfidence(double)
	 * @see surveillance.SurveillancePackage#getUnidentifiedObject_Confidence()
	 * @model required="true"
	 * @generated
	 */
	double getConfidence();

	/**
	 * Sets the value of the '{@link surveillance.UnidentifiedObject#getConfidence <em>Confidence</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Confidence</em>' attribute.
	 * @see #getConfidence()
	 * @generated
	 */
	void setConfidence(double value);

} // UnidentifiedObject
