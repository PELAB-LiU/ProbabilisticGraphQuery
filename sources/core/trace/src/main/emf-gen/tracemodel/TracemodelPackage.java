/**
 */
package tracemodel;

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
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see tracemodel.TracemodelFactory
 * @model kind="package"
 * @generated
 */
public interface TracemodelPackage extends EPackage
{
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "tracemodel";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.example.org/tracemodel";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "tracemodel";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	TracemodelPackage eINSTANCE = tracemodel.impl.TracemodelPackageImpl.init();

	/**
	 * The meta object id for the '{@link tracemodel.impl.TraceModelImpl <em>Trace Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tracemodel.impl.TraceModelImpl
	 * @see tracemodel.impl.TracemodelPackageImpl#getTraceModel()
	 * @generated
	 */
	int TRACE_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Traces</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_MODEL__TRACES = 0;

	/**
	 * The feature id for the '<em><b>Mdd True</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_MODEL__MDD_TRUE = 1;

	/**
	 * The feature id for the '<em><b>Mdd False</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_MODEL__MDD_FALSE = 2;

	/**
	 * The feature id for the '<em><b>Probabilities</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_MODEL__PROBABILITIES = 3;

	/**
	 * The number of structural features of the '<em>Trace Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_MODEL_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link tracemodel.impl.TraceImpl <em>Trace</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tracemodel.impl.TraceImpl
	 * @see tracemodel.impl.TracemodelPackageImpl#getTrace()
	 * @generated
	 */
	int TRACE = 1;

	/**
	 * The feature id for the '<em><b>Event</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE__EVENT = 0;

	/**
	 * The feature id for the '<em><b>Probability</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE__PROBABILITY = 1;

	/**
	 * The feature id for the '<em><b>Generator</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE__GENERATOR = 2;

	/**
	 * The feature id for the '<em><b>Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE__INDEX = 3;

	/**
	 * The number of structural features of the '<em>Trace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link tracemodel.impl.Trace1Impl <em>Trace1</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tracemodel.impl.Trace1Impl
	 * @see tracemodel.impl.TracemodelPackageImpl#getTrace1()
	 * @generated
	 */
	int TRACE1 = 2;

	/**
	 * The feature id for the '<em><b>Event</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE1__EVENT = TRACE__EVENT;

	/**
	 * The feature id for the '<em><b>Probability</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE1__PROBABILITY = TRACE__PROBABILITY;

	/**
	 * The feature id for the '<em><b>Generator</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE1__GENERATOR = TRACE__GENERATOR;

	/**
	 * The feature id for the '<em><b>Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE1__INDEX = TRACE__INDEX;

	/**
	 * The feature id for the '<em><b>Arg1</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE1__ARG1 = TRACE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Trace1</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE1_FEATURE_COUNT = TRACE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link tracemodel.impl.Trace2Impl <em>Trace2</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tracemodel.impl.Trace2Impl
	 * @see tracemodel.impl.TracemodelPackageImpl#getTrace2()
	 * @generated
	 */
	int TRACE2 = 3;

	/**
	 * The feature id for the '<em><b>Event</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE2__EVENT = TRACE__EVENT;

	/**
	 * The feature id for the '<em><b>Probability</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE2__PROBABILITY = TRACE__PROBABILITY;

	/**
	 * The feature id for the '<em><b>Generator</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE2__GENERATOR = TRACE__GENERATOR;

	/**
	 * The feature id for the '<em><b>Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE2__INDEX = TRACE__INDEX;

	/**
	 * The feature id for the '<em><b>Arg1</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE2__ARG1 = TRACE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Arg2</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE2__ARG2 = TRACE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Trace2</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE2_FEATURE_COUNT = TRACE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '<em>Handle</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see reliability.events.Event
	 * @see tracemodel.impl.TracemodelPackageImpl#getHandle()
	 * @generated
	 */
	int HANDLE = 4;

	/**
	 * The meta object id for the '<em>Probability Map</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see reliability.mdd.ProbabilityMap
	 * @see tracemodel.impl.TracemodelPackageImpl#getProbabilityMap()
	 * @generated
	 */
	int PROBABILITY_MAP = 5;


	/**
	 * Returns the meta object for class '{@link tracemodel.TraceModel <em>Trace Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Trace Model</em>'.
	 * @see tracemodel.TraceModel
	 * @generated
	 */
	EClass getTraceModel();

	/**
	 * Returns the meta object for the containment reference list '{@link tracemodel.TraceModel#getTraces <em>Traces</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Traces</em>'.
	 * @see tracemodel.TraceModel#getTraces()
	 * @see #getTraceModel()
	 * @generated
	 */
	EReference getTraceModel_Traces();

	/**
	 * Returns the meta object for the attribute '{@link tracemodel.TraceModel#getMddTrue <em>Mdd True</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mdd True</em>'.
	 * @see tracemodel.TraceModel#getMddTrue()
	 * @see #getTraceModel()
	 * @generated
	 */
	EAttribute getTraceModel_MddTrue();

	/**
	 * Returns the meta object for the attribute '{@link tracemodel.TraceModel#getMddFalse <em>Mdd False</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mdd False</em>'.
	 * @see tracemodel.TraceModel#getMddFalse()
	 * @see #getTraceModel()
	 * @generated
	 */
	EAttribute getTraceModel_MddFalse();

	/**
	 * Returns the meta object for the attribute '{@link tracemodel.TraceModel#getProbabilities <em>Probabilities</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Probabilities</em>'.
	 * @see tracemodel.TraceModel#getProbabilities()
	 * @see #getTraceModel()
	 * @generated
	 */
	EAttribute getTraceModel_Probabilities();

	/**
	 * Returns the meta object for class '{@link tracemodel.Trace <em>Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Trace</em>'.
	 * @see tracemodel.Trace
	 * @generated
	 */
	EClass getTrace();

	/**
	 * Returns the meta object for the attribute '{@link tracemodel.Trace#getEvent <em>Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Event</em>'.
	 * @see tracemodel.Trace#getEvent()
	 * @see #getTrace()
	 * @generated
	 */
	EAttribute getTrace_Event();

	/**
	 * Returns the meta object for the attribute '{@link tracemodel.Trace#getProbability <em>Probability</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Probability</em>'.
	 * @see tracemodel.Trace#getProbability()
	 * @see #getTrace()
	 * @generated
	 */
	EAttribute getTrace_Probability();

	/**
	 * Returns the meta object for the attribute '{@link tracemodel.Trace#getGenerator <em>Generator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Generator</em>'.
	 * @see tracemodel.Trace#getGenerator()
	 * @see #getTrace()
	 * @generated
	 */
	EAttribute getTrace_Generator();

	/**
	 * Returns the meta object for the attribute '{@link tracemodel.Trace#getIndex <em>Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Index</em>'.
	 * @see tracemodel.Trace#getIndex()
	 * @see #getTrace()
	 * @generated
	 */
	EAttribute getTrace_Index();

	/**
	 * Returns the meta object for class '{@link tracemodel.Trace1 <em>Trace1</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Trace1</em>'.
	 * @see tracemodel.Trace1
	 * @generated
	 */
	EClass getTrace1();

	/**
	 * Returns the meta object for the reference '{@link tracemodel.Trace1#getArg1 <em>Arg1</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Arg1</em>'.
	 * @see tracemodel.Trace1#getArg1()
	 * @see #getTrace1()
	 * @generated
	 */
	EReference getTrace1_Arg1();

	/**
	 * Returns the meta object for class '{@link tracemodel.Trace2 <em>Trace2</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Trace2</em>'.
	 * @see tracemodel.Trace2
	 * @generated
	 */
	EClass getTrace2();

	/**
	 * Returns the meta object for the reference '{@link tracemodel.Trace2#getArg1 <em>Arg1</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Arg1</em>'.
	 * @see tracemodel.Trace2#getArg1()
	 * @see #getTrace2()
	 * @generated
	 */
	EReference getTrace2_Arg1();

	/**
	 * Returns the meta object for the reference '{@link tracemodel.Trace2#getArg2 <em>Arg2</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Arg2</em>'.
	 * @see tracemodel.Trace2#getArg2()
	 * @see #getTrace2()
	 * @generated
	 */
	EReference getTrace2_Arg2();

	/**
	 * Returns the meta object for data type '{@link reliability.events.Event <em>Handle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Handle</em>'.
	 * @see reliability.events.Event
	 * @model instanceClass="reliability.events.Event"
	 * @generated
	 */
	EDataType getHandle();

	/**
	 * Returns the meta object for data type '{@link reliability.mdd.ProbabilityMap <em>Probability Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Probability Map</em>'.
	 * @see reliability.mdd.ProbabilityMap
	 * @model instanceClass="reliability.mdd.ProbabilityMap"
	 * @generated
	 */
	EDataType getProbabilityMap();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	TracemodelFactory getTracemodelFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals
	{
		/**
		 * The meta object literal for the '{@link tracemodel.impl.TraceModelImpl <em>Trace Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tracemodel.impl.TraceModelImpl
		 * @see tracemodel.impl.TracemodelPackageImpl#getTraceModel()
		 * @generated
		 */
		EClass TRACE_MODEL = eINSTANCE.getTraceModel();

		/**
		 * The meta object literal for the '<em><b>Traces</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRACE_MODEL__TRACES = eINSTANCE.getTraceModel_Traces();

		/**
		 * The meta object literal for the '<em><b>Mdd True</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRACE_MODEL__MDD_TRUE = eINSTANCE.getTraceModel_MddTrue();

		/**
		 * The meta object literal for the '<em><b>Mdd False</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRACE_MODEL__MDD_FALSE = eINSTANCE.getTraceModel_MddFalse();

		/**
		 * The meta object literal for the '<em><b>Probabilities</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRACE_MODEL__PROBABILITIES = eINSTANCE.getTraceModel_Probabilities();

		/**
		 * The meta object literal for the '{@link tracemodel.impl.TraceImpl <em>Trace</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tracemodel.impl.TraceImpl
		 * @see tracemodel.impl.TracemodelPackageImpl#getTrace()
		 * @generated
		 */
		EClass TRACE = eINSTANCE.getTrace();

		/**
		 * The meta object literal for the '<em><b>Event</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRACE__EVENT = eINSTANCE.getTrace_Event();

		/**
		 * The meta object literal for the '<em><b>Probability</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRACE__PROBABILITY = eINSTANCE.getTrace_Probability();

		/**
		 * The meta object literal for the '<em><b>Generator</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRACE__GENERATOR = eINSTANCE.getTrace_Generator();

		/**
		 * The meta object literal for the '<em><b>Index</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRACE__INDEX = eINSTANCE.getTrace_Index();

		/**
		 * The meta object literal for the '{@link tracemodel.impl.Trace1Impl <em>Trace1</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tracemodel.impl.Trace1Impl
		 * @see tracemodel.impl.TracemodelPackageImpl#getTrace1()
		 * @generated
		 */
		EClass TRACE1 = eINSTANCE.getTrace1();

		/**
		 * The meta object literal for the '<em><b>Arg1</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRACE1__ARG1 = eINSTANCE.getTrace1_Arg1();

		/**
		 * The meta object literal for the '{@link tracemodel.impl.Trace2Impl <em>Trace2</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tracemodel.impl.Trace2Impl
		 * @see tracemodel.impl.TracemodelPackageImpl#getTrace2()
		 * @generated
		 */
		EClass TRACE2 = eINSTANCE.getTrace2();

		/**
		 * The meta object literal for the '<em><b>Arg1</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRACE2__ARG1 = eINSTANCE.getTrace2_Arg1();

		/**
		 * The meta object literal for the '<em><b>Arg2</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRACE2__ARG2 = eINSTANCE.getTrace2_Arg2();

		/**
		 * The meta object literal for the '<em>Handle</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see reliability.events.Event
		 * @see tracemodel.impl.TracemodelPackageImpl#getHandle()
		 * @generated
		 */
		EDataType HANDLE = eINSTANCE.getHandle();

		/**
		 * The meta object literal for the '<em>Probability Map</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see reliability.mdd.ProbabilityMap
		 * @see tracemodel.impl.TracemodelPackageImpl#getProbabilityMap()
		 * @generated
		 */
		EDataType PROBABILITY_MAP = eINSTANCE.getProbabilityMap();

	}

} //TracemodelPackage
