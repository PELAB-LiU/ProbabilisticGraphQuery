/**
 */
package surveillance.impl;

import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.Coordinate;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import surveillance.*;

import uncertaindatatypes.UBoolean;
import uncertaindatatypes.UInteger;
import uncertaindatatypes.UReal;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SurveillanceFactoryImpl extends EFactoryImpl implements SurveillanceFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SurveillanceFactory init() {
		try {
			SurveillanceFactory theSurveillanceFactory = (SurveillanceFactory)EPackage.Registry.INSTANCE.getEFactory(SurveillancePackage.eNS_URI);
			if (theSurveillanceFactory != null) {
				return theSurveillanceFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new SurveillanceFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SurveillanceFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case SurveillancePackage.DRONE: return createDrone();
			case SurveillancePackage.UNIDENTIFIED_OBJECT: return createUnidentifiedObject();
			case SurveillancePackage.SURVEILLANCE_MODEL: return createSurveillanceModel();
			case SurveillancePackage.SHOT: return createShot();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case SurveillancePackage.UREAL:
				return createURealFromString(eDataType, initialValue);
			case SurveillancePackage.UINTEGER:
				return createUIntegerFromString(eDataType, initialValue);
			case SurveillancePackage.UBOOLEAN:
				return createUBooleanFromString(eDataType, initialValue);
			case SurveillancePackage.COORDINATE:
				return createCoordinateFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case SurveillancePackage.UREAL:
				return convertURealToString(eDataType, instanceValue);
			case SurveillancePackage.UINTEGER:
				return convertUIntegerToString(eDataType, instanceValue);
			case SurveillancePackage.UBOOLEAN:
				return convertUBooleanToString(eDataType, instanceValue);
			case SurveillancePackage.COORDINATE:
				return convertCoordinateToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Drone createDrone() {
		DroneImpl drone = new DroneImpl();
		return drone;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UnidentifiedObject createUnidentifiedObject() {
		UnidentifiedObjectImpl unidentifiedObject = new UnidentifiedObjectImpl();
		return unidentifiedObject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SurveillanceModel createSurveillanceModel() {
		SurveillanceModelImpl surveillanceModel = new SurveillanceModelImpl();
		return surveillanceModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Shot createShot() {
		ShotImpl shot = new ShotImpl();
		return shot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UReal createURealFromString(EDataType eDataType, String initialValue) {
		return (UReal)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertURealToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UInteger createUIntegerFromString(EDataType eDataType, String initialValue) {
		return (UInteger)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertUIntegerToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UBoolean createUBooleanFromString(EDataType eDataType, String initialValue) {
		return (UBoolean)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertUBooleanToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Coordinate createCoordinateFromString(EDataType eDataType, String initialValue) {
		return (Coordinate)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertCoordinateToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SurveillancePackage getSurveillancePackage() {
		return (SurveillancePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static SurveillancePackage getPackage() {
		return SurveillancePackage.eINSTANCE;
	}

} //SurveillanceFactoryImpl
