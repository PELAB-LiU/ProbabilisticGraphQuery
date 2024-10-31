/**
 */
package surveillance;

import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.Coordinate;

import org.eclipse.emf.ecore.EObject;

import uncertaindatatypes.UReal;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Moving Object</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link surveillance.MovingObject#getSpeed <em>Speed</em>}</li>
 *   <li>{@link surveillance.MovingObject#getPosition <em>Position</em>}</li>
 *   <li>{@link surveillance.MovingObject#getAngle <em>Angle</em>}</li>
 * </ul>
 *
 * @see surveillance.SurveillancePackage#getMovingObject()
 * @model abstract="true"
 * @generated
 */
public interface MovingObject extends EObject {
	/**
	 * Returns the value of the '<em><b>Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Speed</em>' attribute.
	 * @see #setSpeed(UReal)
	 * @see surveillance.SurveillancePackage#getMovingObject_Speed()
	 * @model dataType="surveillance.UReal" required="true"
	 * @generated
	 */
	UReal getSpeed();

	/**
	 * Sets the value of the '{@link surveillance.MovingObject#getSpeed <em>Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Speed</em>' attribute.
	 * @see #getSpeed()
	 * @generated
	 */
	void setSpeed(UReal value);

	/**
	 * Returns the value of the '<em><b>Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Position</em>' attribute.
	 * @see #setPosition(Coordinate)
	 * @see surveillance.SurveillancePackage#getMovingObject_Position()
	 * @model dataType="surveillance.Coordinate" required="true"
	 * @generated
	 */
	Coordinate getPosition();

	/**
	 * Sets the value of the '{@link surveillance.MovingObject#getPosition <em>Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Position</em>' attribute.
	 * @see #getPosition()
	 * @generated
	 */
	void setPosition(Coordinate value);

	/**
	 * Returns the value of the '<em><b>Angle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Angle</em>' attribute.
	 * @see #setAngle(UReal)
	 * @see surveillance.SurveillancePackage#getMovingObject_Angle()
	 * @model dataType="surveillance.UReal" required="true"
	 * @generated
	 */
	UReal getAngle();

	/**
	 * Sets the value of the '{@link surveillance.MovingObject#getAngle <em>Angle</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Angle</em>' attribute.
	 * @see #getAngle()
	 * @generated
	 */
	void setAngle(UReal value);

} // MovingObject
