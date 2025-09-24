/**
 */
package tracemodel.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import reliability.events.Event;

import reliability.mdd.ProbabilityMap;

import tracemodel.Trace;
import tracemodel.Trace1;
import tracemodel.Trace2;
import tracemodel.TraceModel;
import tracemodel.TracemodelFactory;
import tracemodel.TracemodelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TracemodelPackageImpl extends EPackageImpl implements TracemodelPackage
{
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass traceModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass traceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass trace1EClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass trace2EClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType handleEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType probabilityMapEDataType = null;

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
	 * @see tracemodel.TracemodelPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private TracemodelPackageImpl()
	{
		super(eNS_URI, TracemodelFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link TracemodelPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static TracemodelPackage init()
	{
		if (isInited) return (TracemodelPackage)EPackage.Registry.INSTANCE.getEPackage(TracemodelPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredTracemodelPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		TracemodelPackageImpl theTracemodelPackage = registeredTracemodelPackage instanceof TracemodelPackageImpl ? (TracemodelPackageImpl)registeredTracemodelPackage : new TracemodelPackageImpl();

		isInited = true;

		// Create package meta-data objects
		theTracemodelPackage.createPackageContents();

		// Initialize created meta-data
		theTracemodelPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theTracemodelPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(TracemodelPackage.eNS_URI, theTracemodelPackage);
		return theTracemodelPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getTraceModel()
	{
		return traceModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTraceModel_Traces()
	{
		return (EReference)traceModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTraceModel_MddTrue()
	{
		return (EAttribute)traceModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTraceModel_MddFalse()
	{
		return (EAttribute)traceModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTraceModel_Probabilities()
	{
		return (EAttribute)traceModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getTrace()
	{
		return traceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTrace_Event()
	{
		return (EAttribute)traceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTrace_Probability()
	{
		return (EAttribute)traceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTrace_Generator()
	{
		return (EAttribute)traceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTrace_Index()
	{
		return (EAttribute)traceEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getTrace1()
	{
		return trace1EClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTrace1_Arg1()
	{
		return (EReference)trace1EClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getTrace2()
	{
		return trace2EClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTrace2_Arg1()
	{
		return (EReference)trace2EClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTrace2_Arg2()
	{
		return (EReference)trace2EClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getHandle()
	{
		return handleEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getProbabilityMap()
	{
		return probabilityMapEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TracemodelFactory getTracemodelFactory()
	{
		return (TracemodelFactory)getEFactoryInstance();
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
	public void createPackageContents()
	{
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		traceModelEClass = createEClass(TRACE_MODEL);
		createEReference(traceModelEClass, TRACE_MODEL__TRACES);
		createEAttribute(traceModelEClass, TRACE_MODEL__MDD_TRUE);
		createEAttribute(traceModelEClass, TRACE_MODEL__MDD_FALSE);
		createEAttribute(traceModelEClass, TRACE_MODEL__PROBABILITIES);

		traceEClass = createEClass(TRACE);
		createEAttribute(traceEClass, TRACE__EVENT);
		createEAttribute(traceEClass, TRACE__PROBABILITY);
		createEAttribute(traceEClass, TRACE__GENERATOR);
		createEAttribute(traceEClass, TRACE__INDEX);

		trace1EClass = createEClass(TRACE1);
		createEReference(trace1EClass, TRACE1__ARG1);

		trace2EClass = createEClass(TRACE2);
		createEReference(trace2EClass, TRACE2__ARG1);
		createEReference(trace2EClass, TRACE2__ARG2);

		// Create data types
		handleEDataType = createEDataType(HANDLE);
		probabilityMapEDataType = createEDataType(PROBABILITY_MAP);
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
	public void initializePackageContents()
	{
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		trace1EClass.getESuperTypes().add(this.getTrace());
		trace2EClass.getESuperTypes().add(this.getTrace());

		// Initialize classes and features; add operations and parameters
		initEClass(traceModelEClass, TraceModel.class, "TraceModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getTraceModel_Traces(), this.getTrace(), null, "traces", null, 0, -1, TraceModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTraceModel_MddTrue(), this.getHandle(), "mddTrue", null, 1, 1, TraceModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTraceModel_MddFalse(), this.getHandle(), "mddFalse", null, 1, 1, TraceModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTraceModel_Probabilities(), this.getProbabilityMap(), "probabilities", null, 1, 1, TraceModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(traceEClass, Trace.class, "Trace", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTrace_Event(), this.getHandle(), "event", null, 1, 1, Trace.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTrace_Probability(), ecorePackage.getEDouble(), "probability", null, 1, 1, Trace.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTrace_Generator(), ecorePackage.getEString(), "generator", null, 1, 1, Trace.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTrace_Index(), ecorePackage.getEInt(), "index", null, 1, 1, Trace.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(trace1EClass, Trace1.class, "Trace1", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getTrace1_Arg1(), ecorePackage.getEObject(), null, "arg1", null, 0, 1, Trace1.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(trace2EClass, Trace2.class, "Trace2", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getTrace2_Arg1(), ecorePackage.getEObject(), null, "arg1", null, 0, 1, Trace2.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTrace2_Arg2(), ecorePackage.getEObject(), null, "arg2", null, 0, 1, Trace2.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize data types
		initEDataType(handleEDataType, Event.class, "Handle", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(probabilityMapEDataType, ProbabilityMap.class, "ProbabilityMap", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //TracemodelPackageImpl
