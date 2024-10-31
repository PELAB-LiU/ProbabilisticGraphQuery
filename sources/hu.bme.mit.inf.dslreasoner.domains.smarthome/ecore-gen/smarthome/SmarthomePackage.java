/**
 */
package smarthome;

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
 * @see smarthome.SmarthomeFactory
 * @model kind="package"
 * @generated
 */
public interface SmarthomePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "smarthome";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.example.org/smarthome";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "smarthome";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SmarthomePackage eINSTANCE = smarthome.impl.SmarthomePackageImpl.init();

	/**
	 * The meta object id for the '{@link smarthome.impl.SmarthomeImpl <em>Smarthome</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see smarthome.impl.SmarthomeImpl
	 * @see smarthome.impl.SmarthomePackageImpl#getSmarthome()
	 * @generated
	 */
	int SMARTHOME = 0;

	/**
	 * The feature id for the '<em><b>Persons</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMARTHOME__PERSONS = 0;

	/**
	 * The feature id for the '<em><b>Homes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMARTHOME__HOMES = 1;

	/**
	 * The number of structural features of the '<em>Smarthome</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMARTHOME_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Smarthome</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMARTHOME_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link smarthome.impl.PersonImpl <em>Person</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see smarthome.impl.PersonImpl
	 * @see smarthome.impl.SmarthomePackageImpl#getPerson()
	 * @generated
	 */
	int PERSON = 1;

	/**
	 * The feature id for the '<em><b>Confidence</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERSON__CONFIDENCE = 0;

	/**
	 * The number of structural features of the '<em>Person</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERSON_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Person</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERSON_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link smarthome.impl.HomeImpl <em>Home</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see smarthome.impl.HomeImpl
	 * @see smarthome.impl.SmarthomePackageImpl#getHome()
	 * @generated
	 */
	int HOME = 2;

	/**
	 * The feature id for the '<em><b>Measurements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOME__MEASUREMENTS = 0;

	/**
	 * The feature id for the '<em><b>Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOME__LOCATION = 1;

	/**
	 * The number of structural features of the '<em>Home</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOME_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Home</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOME_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link smarthome.impl.MeasurementImpl <em>Measurement</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see smarthome.impl.MeasurementImpl
	 * @see smarthome.impl.SmarthomePackageImpl#getMeasurement()
	 * @generated
	 */
	int MEASUREMENT = 3;

	/**
	 * The feature id for the '<em><b>Temp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEASUREMENT__TEMP = 0;

	/**
	 * The feature id for the '<em><b>Co</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEASUREMENT__CO = 1;

	/**
	 * The feature id for the '<em><b>Dopen</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEASUREMENT__DOPEN = 2;

	/**
	 * The feature id for the '<em><b>Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEASUREMENT__TIME = 3;

	/**
	 * The feature id for the '<em><b>Athome</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEASUREMENT__ATHOME = 4;

	/**
	 * The number of structural features of the '<em>Measurement</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEASUREMENT_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Measurement</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEASUREMENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '<em>Location</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities.Location
	 * @see smarthome.impl.SmarthomePackageImpl#getLocation()
	 * @generated
	 */
	int LOCATION = 4;

	/**
	 * The meta object id for the '<em>UReal</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see uncertaindatatypes.UReal
	 * @see smarthome.impl.SmarthomePackageImpl#getUReal()
	 * @generated
	 */
	int UREAL = 5;

	/**
	 * The meta object id for the '<em>UBoolean</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see uncertaindatatypes.UBoolean
	 * @see smarthome.impl.SmarthomePackageImpl#getUBoolean()
	 * @generated
	 */
	int UBOOLEAN = 6;


	/**
	 * Returns the meta object for class '{@link smarthome.Smarthome <em>Smarthome</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Smarthome</em>'.
	 * @see smarthome.Smarthome
	 * @generated
	 */
	EClass getSmarthome();

	/**
	 * Returns the meta object for the containment reference list '{@link smarthome.Smarthome#getPersons <em>Persons</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Persons</em>'.
	 * @see smarthome.Smarthome#getPersons()
	 * @see #getSmarthome()
	 * @generated
	 */
	EReference getSmarthome_Persons();

	/**
	 * Returns the meta object for the containment reference list '{@link smarthome.Smarthome#getHomes <em>Homes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Homes</em>'.
	 * @see smarthome.Smarthome#getHomes()
	 * @see #getSmarthome()
	 * @generated
	 */
	EReference getSmarthome_Homes();

	/**
	 * Returns the meta object for class '{@link smarthome.Person <em>Person</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Person</em>'.
	 * @see smarthome.Person
	 * @generated
	 */
	EClass getPerson();

	/**
	 * Returns the meta object for the attribute '{@link smarthome.Person#getConfidence <em>Confidence</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Confidence</em>'.
	 * @see smarthome.Person#getConfidence()
	 * @see #getPerson()
	 * @generated
	 */
	EAttribute getPerson_Confidence();

	/**
	 * Returns the meta object for class '{@link smarthome.Home <em>Home</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Home</em>'.
	 * @see smarthome.Home
	 * @generated
	 */
	EClass getHome();

	/**
	 * Returns the meta object for the containment reference list '{@link smarthome.Home#getMeasurements <em>Measurements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Measurements</em>'.
	 * @see smarthome.Home#getMeasurements()
	 * @see #getHome()
	 * @generated
	 */
	EReference getHome_Measurements();

	/**
	 * Returns the meta object for the attribute '{@link smarthome.Home#getLocation <em>Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Location</em>'.
	 * @see smarthome.Home#getLocation()
	 * @see #getHome()
	 * @generated
	 */
	EAttribute getHome_Location();

	/**
	 * Returns the meta object for class '{@link smarthome.Measurement <em>Measurement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Measurement</em>'.
	 * @see smarthome.Measurement
	 * @generated
	 */
	EClass getMeasurement();

	/**
	 * Returns the meta object for the attribute '{@link smarthome.Measurement#getTemp <em>Temp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Temp</em>'.
	 * @see smarthome.Measurement#getTemp()
	 * @see #getMeasurement()
	 * @generated
	 */
	EAttribute getMeasurement_Temp();

	/**
	 * Returns the meta object for the attribute '{@link smarthome.Measurement#getCo <em>Co</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Co</em>'.
	 * @see smarthome.Measurement#getCo()
	 * @see #getMeasurement()
	 * @generated
	 */
	EAttribute getMeasurement_Co();

	/**
	 * Returns the meta object for the attribute '{@link smarthome.Measurement#getDopen <em>Dopen</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Dopen</em>'.
	 * @see smarthome.Measurement#getDopen()
	 * @see #getMeasurement()
	 * @generated
	 */
	EAttribute getMeasurement_Dopen();

	/**
	 * Returns the meta object for the attribute '{@link smarthome.Measurement#getTime <em>Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Time</em>'.
	 * @see smarthome.Measurement#getTime()
	 * @see #getMeasurement()
	 * @generated
	 */
	EAttribute getMeasurement_Time();

	/**
	 * Returns the meta object for the reference list '{@link smarthome.Measurement#getAthome <em>Athome</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Athome</em>'.
	 * @see smarthome.Measurement#getAthome()
	 * @see #getMeasurement()
	 * @generated
	 */
	EReference getMeasurement_Athome();

	/**
	 * Returns the meta object for data type '{@link hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities.Location <em>Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Location</em>'.
	 * @see hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities.Location
	 * @model instanceClass="hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities.Location"
	 * @generated
	 */
	EDataType getLocation();

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
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	SmarthomeFactory getSmarthomeFactory();

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
		 * The meta object literal for the '{@link smarthome.impl.SmarthomeImpl <em>Smarthome</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see smarthome.impl.SmarthomeImpl
		 * @see smarthome.impl.SmarthomePackageImpl#getSmarthome()
		 * @generated
		 */
		EClass SMARTHOME = eINSTANCE.getSmarthome();

		/**
		 * The meta object literal for the '<em><b>Persons</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SMARTHOME__PERSONS = eINSTANCE.getSmarthome_Persons();

		/**
		 * The meta object literal for the '<em><b>Homes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SMARTHOME__HOMES = eINSTANCE.getSmarthome_Homes();

		/**
		 * The meta object literal for the '{@link smarthome.impl.PersonImpl <em>Person</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see smarthome.impl.PersonImpl
		 * @see smarthome.impl.SmarthomePackageImpl#getPerson()
		 * @generated
		 */
		EClass PERSON = eINSTANCE.getPerson();

		/**
		 * The meta object literal for the '<em><b>Confidence</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PERSON__CONFIDENCE = eINSTANCE.getPerson_Confidence();

		/**
		 * The meta object literal for the '{@link smarthome.impl.HomeImpl <em>Home</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see smarthome.impl.HomeImpl
		 * @see smarthome.impl.SmarthomePackageImpl#getHome()
		 * @generated
		 */
		EClass HOME = eINSTANCE.getHome();

		/**
		 * The meta object literal for the '<em><b>Measurements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference HOME__MEASUREMENTS = eINSTANCE.getHome_Measurements();

		/**
		 * The meta object literal for the '<em><b>Location</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HOME__LOCATION = eINSTANCE.getHome_Location();

		/**
		 * The meta object literal for the '{@link smarthome.impl.MeasurementImpl <em>Measurement</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see smarthome.impl.MeasurementImpl
		 * @see smarthome.impl.SmarthomePackageImpl#getMeasurement()
		 * @generated
		 */
		EClass MEASUREMENT = eINSTANCE.getMeasurement();

		/**
		 * The meta object literal for the '<em><b>Temp</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MEASUREMENT__TEMP = eINSTANCE.getMeasurement_Temp();

		/**
		 * The meta object literal for the '<em><b>Co</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MEASUREMENT__CO = eINSTANCE.getMeasurement_Co();

		/**
		 * The meta object literal for the '<em><b>Dopen</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MEASUREMENT__DOPEN = eINSTANCE.getMeasurement_Dopen();

		/**
		 * The meta object literal for the '<em><b>Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MEASUREMENT__TIME = eINSTANCE.getMeasurement_Time();

		/**
		 * The meta object literal for the '<em><b>Athome</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MEASUREMENT__ATHOME = eINSTANCE.getMeasurement_Athome();

		/**
		 * The meta object literal for the '<em>Location</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities.Location
		 * @see smarthome.impl.SmarthomePackageImpl#getLocation()
		 * @generated
		 */
		EDataType LOCATION = eINSTANCE.getLocation();

		/**
		 * The meta object literal for the '<em>UReal</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see uncertaindatatypes.UReal
		 * @see smarthome.impl.SmarthomePackageImpl#getUReal()
		 * @generated
		 */
		EDataType UREAL = eINSTANCE.getUReal();

		/**
		 * The meta object literal for the '<em>UBoolean</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see uncertaindatatypes.UBoolean
		 * @see smarthome.impl.SmarthomePackageImpl#getUBoolean()
		 * @generated
		 */
		EDataType UBOOLEAN = eINSTANCE.getUBoolean();

	}

} //SmarthomePackage
