/**
 */
package tracemodel;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Trace2</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link tracemodel.Trace2#getArg1 <em>Arg1</em>}</li>
 *   <li>{@link tracemodel.Trace2#getArg2 <em>Arg2</em>}</li>
 * </ul>
 *
 * @see tracemodel.TracemodelPackage#getTrace2()
 * @model
 * @generated
 */
public interface Trace2 extends Trace {
	/**
	 * Returns the value of the '<em><b>Arg1</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Arg1</em>' reference.
	 * @see #setArg1(EObject)
	 * @see tracemodel.TracemodelPackage#getTrace2_Arg1()
	 * @model
	 * @generated
	 */
	EObject getArg1();

	/**
	 * Sets the value of the '{@link tracemodel.Trace2#getArg1 <em>Arg1</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Arg1</em>' reference.
	 * @see #getArg1()
	 * @generated
	 */
	void setArg1(EObject value);

	/**
	 * Returns the value of the '<em><b>Arg2</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Arg2</em>' reference.
	 * @see #setArg2(EObject)
	 * @see tracemodel.TracemodelPackage#getTrace2_Arg2()
	 * @model
	 * @generated
	 */
	EObject getArg2();

	/**
	 * Sets the value of the '{@link tracemodel.Trace2#getArg2 <em>Arg2</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Arg2</em>' reference.
	 * @see #getArg2()
	 * @generated
	 */
	void setArg2(EObject value);

} // Trace2
