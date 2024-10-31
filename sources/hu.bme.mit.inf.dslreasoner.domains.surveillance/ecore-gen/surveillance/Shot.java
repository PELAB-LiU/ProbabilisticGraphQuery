/**
 */
package surveillance;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Shot</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link surveillance.Shot#getAt <em>At</em>}</li>
 *   <li>{@link surveillance.Shot#getProbability <em>Probability</em>}</li>
 * </ul>
 *
 * @see surveillance.SurveillancePackage#getShot()
 * @model
 * @generated
 */
public interface Shot extends EObject {
	/**
	 * Returns the value of the '<em><b>At</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>At</em>' reference.
	 * @see #setAt(UnidentifiedObject)
	 * @see surveillance.SurveillancePackage#getShot_At()
	 * @model required="true"
	 * @generated
	 */
	UnidentifiedObject getAt();

	/**
	 * Sets the value of the '{@link surveillance.Shot#getAt <em>At</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>At</em>' reference.
	 * @see #getAt()
	 * @generated
	 */
	void setAt(UnidentifiedObject value);

	/**
	 * Returns the value of the '<em><b>Probability</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Probability</em>' attribute.
	 * @see #setProbability(double)
	 * @see surveillance.SurveillancePackage#getShot_Probability()
	 * @model required="true"
	 * @generated
	 */
	double getProbability();

	/**
	 * Sets the value of the '{@link surveillance.Shot#getProbability <em>Probability</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Probability</em>' attribute.
	 * @see #getProbability()
	 * @generated
	 */
	void setProbability(double value);

} // Shot
