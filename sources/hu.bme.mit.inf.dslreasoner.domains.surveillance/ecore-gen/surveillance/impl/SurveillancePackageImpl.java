/**
 */
package surveillance.impl;

import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.Coordinate;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import surveillance.Drone;
import surveillance.MovingObject;
import surveillance.Shot;
import surveillance.SurveillanceFactory;
import surveillance.SurveillanceModel;
import surveillance.SurveillancePackage;
import surveillance.UnidentifiedObject;

import uncertaindatatypes.UBoolean;
import uncertaindatatypes.UInteger;
import uncertaindatatypes.UReal;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SurveillancePackageImpl extends EPackageImpl implements SurveillancePackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass movingObjectEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass droneEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass unidentifiedObjectEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass surveillanceModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass shotEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType uRealEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType uIntegerEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType uBooleanEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType coordinateEDataType = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see surveillance.SurveillancePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private SurveillancePackageImpl() {
		super(eNS_URI, SurveillanceFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>This method is used to initialize {@link SurveillancePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static SurveillancePackage init() {
		if (isInited) return (SurveillancePackage)EPackage.Registry.INSTANCE.getEPackage(SurveillancePackage.eNS_URI);

		// Obtain or create and register package
		Object registeredSurveillancePackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		SurveillancePackageImpl theSurveillancePackage = registeredSurveillancePackage instanceof SurveillancePackageImpl ? (SurveillancePackageImpl)registeredSurveillancePackage : new SurveillancePackageImpl();

		isInited = true;

		// Create package meta-data objects
		theSurveillancePackage.createPackageContents();

		// Initialize created meta-data
		theSurveillancePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theSurveillancePackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(SurveillancePackage.eNS_URI, theSurveillancePackage);
		return theSurveillancePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMovingObject() {
		return movingObjectEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMovingObject_Speed() {
		return (EAttribute)movingObjectEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMovingObject_Position() {
		return (EAttribute)movingObjectEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMovingObject_Angle() {
		return (EAttribute)movingObjectEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDrone() {
		return droneEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDrone_Shots() {
		return (EReference)droneEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUnidentifiedObject() {
		return unidentifiedObjectEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnidentifiedObject_Confidence() {
		return (EAttribute)unidentifiedObjectEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSurveillanceModel() {
		return surveillanceModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSurveillanceModel_Objects() {
		return (EReference)surveillanceModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getShot() {
		return shotEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getShot_At() {
		return (EReference)shotEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getShot_Probability() {
		return (EAttribute)shotEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getUReal() {
		return uRealEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getUInteger() {
		return uIntegerEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getUBoolean() {
		return uBooleanEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getCoordinate() {
		return coordinateEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SurveillanceFactory getSurveillanceFactory() {
		return (SurveillanceFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		movingObjectEClass = createEClass(MOVING_OBJECT);
		createEAttribute(movingObjectEClass, MOVING_OBJECT__SPEED);
		createEAttribute(movingObjectEClass, MOVING_OBJECT__POSITION);
		createEAttribute(movingObjectEClass, MOVING_OBJECT__ANGLE);

		droneEClass = createEClass(DRONE);
		createEReference(droneEClass, DRONE__SHOTS);

		unidentifiedObjectEClass = createEClass(UNIDENTIFIED_OBJECT);
		createEAttribute(unidentifiedObjectEClass, UNIDENTIFIED_OBJECT__CONFIDENCE);

		surveillanceModelEClass = createEClass(SURVEILLANCE_MODEL);
		createEReference(surveillanceModelEClass, SURVEILLANCE_MODEL__OBJECTS);

		shotEClass = createEClass(SHOT);
		createEReference(shotEClass, SHOT__AT);
		createEAttribute(shotEClass, SHOT__PROBABILITY);

		// Create data types
		uRealEDataType = createEDataType(UREAL);
		uIntegerEDataType = createEDataType(UINTEGER);
		uBooleanEDataType = createEDataType(UBOOLEAN);
		coordinateEDataType = createEDataType(COORDINATE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		droneEClass.getESuperTypes().add(this.getMovingObject());
		unidentifiedObjectEClass.getESuperTypes().add(this.getMovingObject());

		// Initialize classes, features, and operations; add parameters
		initEClass(movingObjectEClass, MovingObject.class, "MovingObject", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMovingObject_Speed(), this.getUReal(), "speed", null, 1, 1, MovingObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMovingObject_Position(), this.getCoordinate(), "position", null, 1, 1, MovingObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMovingObject_Angle(), this.getUReal(), "angle", null, 1, 1, MovingObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(droneEClass, Drone.class, "Drone", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDrone_Shots(), this.getShot(), null, "shots", null, 0, -1, Drone.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(unidentifiedObjectEClass, UnidentifiedObject.class, "UnidentifiedObject", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getUnidentifiedObject_Confidence(), ecorePackage.getEDouble(), "confidence", null, 1, 1, UnidentifiedObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(surveillanceModelEClass, SurveillanceModel.class, "SurveillanceModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSurveillanceModel_Objects(), this.getMovingObject(), null, "objects", null, 0, -1, SurveillanceModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(shotEClass, Shot.class, "Shot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getShot_At(), this.getUnidentifiedObject(), null, "at", null, 1, 1, Shot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getShot_Probability(), ecorePackage.getEDouble(), "probability", null, 1, 1, Shot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize data types
		initEDataType(uRealEDataType, UReal.class, "UReal", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(uIntegerEDataType, UInteger.class, "UInteger", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(uBooleanEDataType, UBoolean.class, "UBoolean", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(coordinateEDataType, Coordinate.class, "Coordinate", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //SurveillancePackageImpl
