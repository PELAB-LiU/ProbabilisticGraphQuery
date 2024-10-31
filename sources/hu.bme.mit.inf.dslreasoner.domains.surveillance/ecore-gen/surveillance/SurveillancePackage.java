/**
 */
package surveillance;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see surveillance.SurveillanceFactory
 * @model kind="package"
 * @generated
 */
public interface SurveillancePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "surveillance";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.example.org/surveillance";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "surveillance";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SurveillancePackage eINSTANCE = surveillance.impl.SurveillancePackageImpl.init();

	/**
	 * The meta object id for the '{@link surveillance.impl.MovingObjectImpl <em>Moving Object</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see surveillance.impl.MovingObjectImpl
	 * @see surveillance.impl.SurveillancePackageImpl#getMovingObject()
	 * @generated
	 */
	int MOVING_OBJECT = 0;

	/**
	 * The feature id for the '<em><b>Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVING_OBJECT__SPEED = 0;

	/**
	 * The feature id for the '<em><b>Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVING_OBJECT__POSITION = 1;

	/**
	 * The feature id for the '<em><b>Angle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVING_OBJECT__ANGLE = 2;

	/**
	 * The number of structural features of the '<em>Moving Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVING_OBJECT_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Moving Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVING_OBJECT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link surveillance.impl.DroneImpl <em>Drone</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see surveillance.impl.DroneImpl
	 * @see surveillance.impl.SurveillancePackageImpl#getDrone()
	 * @generated
	 */
	int DRONE = 1;

	/**
	 * The feature id for the '<em><b>Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRONE__SPEED = MOVING_OBJECT__SPEED;

	/**
	 * The feature id for the '<em><b>Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRONE__POSITION = MOVING_OBJECT__POSITION;

	/**
	 * The feature id for the '<em><b>Angle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRONE__ANGLE = MOVING_OBJECT__ANGLE;

	/**
	 * The feature id for the '<em><b>Shots</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRONE__SHOTS = MOVING_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Drone</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRONE_FEATURE_COUNT = MOVING_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Drone</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRONE_OPERATION_COUNT = MOVING_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link surveillance.impl.UnidentifiedObjectImpl <em>Unidentified Object</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see surveillance.impl.UnidentifiedObjectImpl
	 * @see surveillance.impl.SurveillancePackageImpl#getUnidentifiedObject()
	 * @generated
	 */
	int UNIDENTIFIED_OBJECT = 2;

	/**
	 * The feature id for the '<em><b>Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIDENTIFIED_OBJECT__SPEED = MOVING_OBJECT__SPEED;

	/**
	 * The feature id for the '<em><b>Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIDENTIFIED_OBJECT__POSITION = MOVING_OBJECT__POSITION;

	/**
	 * The feature id for the '<em><b>Angle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIDENTIFIED_OBJECT__ANGLE = MOVING_OBJECT__ANGLE;

	/**
	 * The feature id for the '<em><b>Confidence</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIDENTIFIED_OBJECT__CONFIDENCE = MOVING_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Unidentified Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIDENTIFIED_OBJECT_FEATURE_COUNT = MOVING_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Unidentified Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIDENTIFIED_OBJECT_OPERATION_COUNT = MOVING_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link surveillance.impl.SurveillanceModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see surveillance.impl.SurveillanceModelImpl
	 * @see surveillance.impl.SurveillancePackageImpl#getSurveillanceModel()
	 * @generated
	 */
	int SURVEILLANCE_MODEL = 3;

	/**
	 * The feature id for the '<em><b>Objects</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SURVEILLANCE_MODEL__OBJECTS = 0;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SURVEILLANCE_MODEL_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SURVEILLANCE_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link surveillance.impl.ShotImpl <em>Shot</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see surveillance.impl.ShotImpl
	 * @see surveillance.impl.SurveillancePackageImpl#getShot()
	 * @generated
	 */
	int SHOT = 4;

	/**
	 * The feature id for the '<em><b>At</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHOT__AT = 0;

	/**
	 * The feature id for the '<em><b>Probability</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHOT__PROBABILITY = 1;

	/**
	 * The number of structural features of the '<em>Shot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHOT_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Shot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHOT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '<em>UReal</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see uncertaindatatypes.UReal
	 * @see surveillance.impl.SurveillancePackageImpl#getUReal()
	 * @generated
	 */
	int UREAL = 5;

	/**
	 * The meta object id for the '<em>UInteger</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see uncertaindatatypes.UInteger
	 * @see surveillance.impl.SurveillancePackageImpl#getUInteger()
	 * @generated
	 */
	int UINTEGER = 6;

	/**
	 * The meta object id for the '<em>UBoolean</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see uncertaindatatypes.UBoolean
	 * @see surveillance.impl.SurveillancePackageImpl#getUBoolean()
	 * @generated
	 */
	int UBOOLEAN = 7;

	/**
	 * The meta object id for the '<em>Coordinate</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.Coordinate
	 * @see surveillance.impl.SurveillancePackageImpl#getCoordinate()
	 * @generated
	 */
	int COORDINATE = 8;


	/**
	 * Returns the meta object for class '{@link surveillance.MovingObject <em>Moving Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Moving Object</em>'.
	 * @see surveillance.MovingObject
	 * @generated
	 */
	EClass getMovingObject();

	/**
	 * Returns the meta object for the attribute '{@link surveillance.MovingObject#getSpeed <em>Speed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Speed</em>'.
	 * @see surveillance.MovingObject#getSpeed()
	 * @see #getMovingObject()
	 * @generated
	 */
	EAttribute getMovingObject_Speed();

	/**
	 * Returns the meta object for the attribute '{@link surveillance.MovingObject#getPosition <em>Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Position</em>'.
	 * @see surveillance.MovingObject#getPosition()
	 * @see #getMovingObject()
	 * @generated
	 */
	EAttribute getMovingObject_Position();

	/**
	 * Returns the meta object for the attribute '{@link surveillance.MovingObject#getAngle <em>Angle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Angle</em>'.
	 * @see surveillance.MovingObject#getAngle()
	 * @see #getMovingObject()
	 * @generated
	 */
	EAttribute getMovingObject_Angle();

	/**
	 * Returns the meta object for class '{@link surveillance.Drone <em>Drone</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Drone</em>'.
	 * @see surveillance.Drone
	 * @generated
	 */
	EClass getDrone();

	/**
	 * Returns the meta object for the containment reference list '{@link surveillance.Drone#getShots <em>Shots</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Shots</em>'.
	 * @see surveillance.Drone#getShots()
	 * @see #getDrone()
	 * @generated
	 */
	EReference getDrone_Shots();

	/**
	 * Returns the meta object for class '{@link surveillance.UnidentifiedObject <em>Unidentified Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Unidentified Object</em>'.
	 * @see surveillance.UnidentifiedObject
	 * @generated
	 */
	EClass getUnidentifiedObject();

	/**
	 * Returns the meta object for the attribute '{@link surveillance.UnidentifiedObject#getConfidence <em>Confidence</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Confidence</em>'.
	 * @see surveillance.UnidentifiedObject#getConfidence()
	 * @see #getUnidentifiedObject()
	 * @generated
	 */
	EAttribute getUnidentifiedObject_Confidence();

	/**
	 * Returns the meta object for class '{@link surveillance.SurveillanceModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see surveillance.SurveillanceModel
	 * @generated
	 */
	EClass getSurveillanceModel();

	/**
	 * Returns the meta object for the containment reference list '{@link surveillance.SurveillanceModel#getObjects <em>Objects</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Objects</em>'.
	 * @see surveillance.SurveillanceModel#getObjects()
	 * @see #getSurveillanceModel()
	 * @generated
	 */
	EReference getSurveillanceModel_Objects();

	/**
	 * Returns the meta object for class '{@link surveillance.Shot <em>Shot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Shot</em>'.
	 * @see surveillance.Shot
	 * @generated
	 */
	EClass getShot();

	/**
	 * Returns the meta object for the reference '{@link surveillance.Shot#getAt <em>At</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>At</em>'.
	 * @see surveillance.Shot#getAt()
	 * @see #getShot()
	 * @generated
	 */
	EReference getShot_At();

	/**
	 * Returns the meta object for the attribute '{@link surveillance.Shot#getProbability <em>Probability</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Probability</em>'.
	 * @see surveillance.Shot#getProbability()
	 * @see #getShot()
	 * @generated
	 */
	EAttribute getShot_Probability();

	/**
	 * Returns the meta object for data type '{@link uncertaindatatypes.UReal <em>UReal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>UReal</em>'.
	 * @see uncertaindatatypes.UReal
	 * @model instanceClass="uncertaindatatypes.UReal"
	 * @generated
	 */
	EDataType getUReal();

	/**
	 * Returns the meta object for data type '{@link uncertaindatatypes.UInteger <em>UInteger</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>UInteger</em>'.
	 * @see uncertaindatatypes.UInteger
	 * @model instanceClass="uncertaindatatypes.UInteger"
	 * @generated
	 */
	EDataType getUInteger();

	/**
	 * Returns the meta object for data type '{@link uncertaindatatypes.UBoolean <em>UBoolean</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>UBoolean</em>'.
	 * @see uncertaindatatypes.UBoolean
	 * @model instanceClass="uncertaindatatypes.UBoolean"
	 * @generated
	 */
	EDataType getUBoolean();

	/**
	 * Returns the meta object for data type '{@link hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.Coordinate <em>Coordinate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Coordinate</em>'.
	 * @see hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.Coordinate
	 * @model instanceClass="hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.Coordinate"
	 * @generated
	 */
	EDataType getCoordinate();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	SurveillanceFactory getSurveillanceFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link surveillance.impl.MovingObjectImpl <em>Moving Object</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see surveillance.impl.MovingObjectImpl
		 * @see surveillance.impl.SurveillancePackageImpl#getMovingObject()
		 * @generated
		 */
		EClass MOVING_OBJECT = eINSTANCE.getMovingObject();

		/**
		 * The meta object literal for the '<em><b>Speed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MOVING_OBJECT__SPEED = eINSTANCE.getMovingObject_Speed();

		/**
		 * The meta object literal for the '<em><b>Position</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MOVING_OBJECT__POSITION = eINSTANCE.getMovingObject_Position();

		/**
		 * The meta object literal for the '<em><b>Angle</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MOVING_OBJECT__ANGLE = eINSTANCE.getMovingObject_Angle();

		/**
		 * The meta object literal for the '{@link surveillance.impl.DroneImpl <em>Drone</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see surveillance.impl.DroneImpl
		 * @see surveillance.impl.SurveillancePackageImpl#getDrone()
		 * @generated
		 */
		EClass DRONE = eINSTANCE.getDrone();

		/**
		 * The meta object literal for the '<em><b>Shots</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DRONE__SHOTS = eINSTANCE.getDrone_Shots();

		/**
		 * The meta object literal for the '{@link surveillance.impl.UnidentifiedObjectImpl <em>Unidentified Object</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see surveillance.impl.UnidentifiedObjectImpl
		 * @see surveillance.impl.SurveillancePackageImpl#getUnidentifiedObject()
		 * @generated
		 */
		EClass UNIDENTIFIED_OBJECT = eINSTANCE.getUnidentifiedObject();

		/**
		 * The meta object literal for the '<em><b>Confidence</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIDENTIFIED_OBJECT__CONFIDENCE = eINSTANCE.getUnidentifiedObject_Confidence();

		/**
		 * The meta object literal for the '{@link surveillance.impl.SurveillanceModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see surveillance.impl.SurveillanceModelImpl
		 * @see surveillance.impl.SurveillancePackageImpl#getSurveillanceModel()
		 * @generated
		 */
		EClass SURVEILLANCE_MODEL = eINSTANCE.getSurveillanceModel();

		/**
		 * The meta object literal for the '<em><b>Objects</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SURVEILLANCE_MODEL__OBJECTS = eINSTANCE.getSurveillanceModel_Objects();

		/**
		 * The meta object literal for the '{@link surveillance.impl.ShotImpl <em>Shot</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see surveillance.impl.ShotImpl
		 * @see surveillance.impl.SurveillancePackageImpl#getShot()
		 * @generated
		 */
		EClass SHOT = eINSTANCE.getShot();

		/**
		 * The meta object literal for the '<em><b>At</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SHOT__AT = eINSTANCE.getShot_At();

		/**
		 * The meta object literal for the '<em><b>Probability</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SHOT__PROBABILITY = eINSTANCE.getShot_Probability();

		/**
		 * The meta object literal for the '<em>UReal</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see uncertaindatatypes.UReal
		 * @see surveillance.impl.SurveillancePackageImpl#getUReal()
		 * @generated
		 */
		EDataType UREAL = eINSTANCE.getUReal();

		/**
		 * The meta object literal for the '<em>UInteger</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see uncertaindatatypes.UInteger
		 * @see surveillance.impl.SurveillancePackageImpl#getUInteger()
		 * @generated
		 */
		EDataType UINTEGER = eINSTANCE.getUInteger();

		/**
		 * The meta object literal for the '<em>UBoolean</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see uncertaindatatypes.UBoolean
		 * @see surveillance.impl.SurveillancePackageImpl#getUBoolean()
		 * @generated
		 */
		EDataType UBOOLEAN = eINSTANCE.getUBoolean();

		/**
		 * The meta object literal for the '<em>Coordinate</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.Coordinate
		 * @see surveillance.impl.SurveillancePackageImpl#getCoordinate()
		 * @generated
		 */
		EDataType COORDINATE = eINSTANCE.getCoordinate();

	}

} //SurveillancePackage
