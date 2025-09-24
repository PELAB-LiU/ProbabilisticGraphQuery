/**
 */
package tracemodel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import reliability.events.Event;

import reliability.mdd.ProbabilityMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Trace Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link tracemodel.TraceModel#getTraces <em>Traces</em>}</li>
 *   <li>{@link tracemodel.TraceModel#getMddTrue <em>Mdd True</em>}</li>
 *   <li>{@link tracemodel.TraceModel#getMddFalse <em>Mdd False</em>}</li>
 *   <li>{@link tracemodel.TraceModel#getProbabilities <em>Probabilities</em>}</li>
 * </ul>
 *
 * @see tracemodel.TracemodelPackage#getTraceModel()
 * @model
 * @generated
 */
public interface TraceModel extends EObject
{
	/**
	 * Returns the value of the '<em><b>Traces</b></em>' containment reference list.
	 * The list contents are of type {@link tracemodel.Trace}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Traces</em>' containment reference list.
	 * @see tracemodel.TracemodelPackage#getTraceModel_Traces()
	 * @model containment="true"
	 * @generated
	 */
	EList<Trace> getTraces();

	/**
	 * Returns the value of the '<em><b>Mdd True</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mdd True</em>' attribute.
	 * @see #setMddTrue(Event)
	 * @see tracemodel.TracemodelPackage#getTraceModel_MddTrue()
	 * @model dataType="tracemodel.Handle" required="true"
	 * @generated
	 */
	Event getMddTrue();

	/**
	 * Sets the value of the '{@link tracemodel.TraceModel#getMddTrue <em>Mdd True</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mdd True</em>' attribute.
	 * @see #getMddTrue()
	 * @generated
	 */
	void setMddTrue(Event value);

	/**
	 * Returns the value of the '<em><b>Mdd False</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mdd False</em>' attribute.
	 * @see #setMddFalse(Event)
	 * @see tracemodel.TracemodelPackage#getTraceModel_MddFalse()
	 * @model dataType="tracemodel.Handle" required="true"
	 * @generated
	 */
	Event getMddFalse();

	/**
	 * Sets the value of the '{@link tracemodel.TraceModel#getMddFalse <em>Mdd False</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mdd False</em>' attribute.
	 * @see #getMddFalse()
	 * @generated
	 */
	void setMddFalse(Event value);

	/**
	 * Returns the value of the '<em><b>Probabilities</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Probabilities</em>' attribute.
	 * @see #setProbabilities(ProbabilityMap)
	 * @see tracemodel.TracemodelPackage#getTraceModel_Probabilities()
	 * @model dataType="tracemodel.ProbabilityMap" required="true"
	 * @generated
	 */
	ProbabilityMap getProbabilities();

	/**
	 * Sets the value of the '{@link tracemodel.TraceModel#getProbabilities <em>Probabilities</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Probabilities</em>' attribute.
	 * @see #getProbabilities()
	 * @generated
	 */
	void setProbabilities(ProbabilityMap value);

} // TraceModel
