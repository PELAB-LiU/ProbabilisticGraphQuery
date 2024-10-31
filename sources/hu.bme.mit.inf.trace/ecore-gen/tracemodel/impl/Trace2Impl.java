/**
 */
package tracemodel.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import tracemodel.Trace2;
import tracemodel.TracemodelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Trace2</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link tracemodel.impl.Trace2Impl#getArg1 <em>Arg1</em>}</li>
 *   <li>{@link tracemodel.impl.Trace2Impl#getArg2 <em>Arg2</em>}</li>
 * </ul>
 *
 * @generated
 */
public class Trace2Impl extends TraceImpl implements Trace2 {
	/**
	 * The cached value of the '{@link #getArg1() <em>Arg1</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArg1()
	 * @generated
	 * @ordered
	 */
	protected EObject arg1;

	/**
	 * The cached value of the '{@link #getArg2() <em>Arg2</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArg2()
	 * @generated
	 * @ordered
	 */
	protected EObject arg2;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Trace2Impl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TracemodelPackage.Literals.TRACE2;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getArg1() {
		if (arg1 != null && arg1.eIsProxy()) {
			InternalEObject oldArg1 = (InternalEObject)arg1;
			arg1 = eResolveProxy(oldArg1);
			if (arg1 != oldArg1) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, TracemodelPackage.TRACE2__ARG1, oldArg1, arg1));
			}
		}
		return arg1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetArg1() {
		return arg1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setArg1(EObject newArg1) {
		EObject oldArg1 = arg1;
		arg1 = newArg1;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracemodelPackage.TRACE2__ARG1, oldArg1, arg1));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getArg2() {
		if (arg2 != null && arg2.eIsProxy()) {
			InternalEObject oldArg2 = (InternalEObject)arg2;
			arg2 = eResolveProxy(oldArg2);
			if (arg2 != oldArg2) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, TracemodelPackage.TRACE2__ARG2, oldArg2, arg2));
			}
		}
		return arg2;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetArg2() {
		return arg2;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setArg2(EObject newArg2) {
		EObject oldArg2 = arg2;
		arg2 = newArg2;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracemodelPackage.TRACE2__ARG2, oldArg2, arg2));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TracemodelPackage.TRACE2__ARG1:
				if (resolve) return getArg1();
				return basicGetArg1();
			case TracemodelPackage.TRACE2__ARG2:
				if (resolve) return getArg2();
				return basicGetArg2();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case TracemodelPackage.TRACE2__ARG1:
				setArg1((EObject)newValue);
				return;
			case TracemodelPackage.TRACE2__ARG2:
				setArg2((EObject)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case TracemodelPackage.TRACE2__ARG1:
				setArg1((EObject)null);
				return;
			case TracemodelPackage.TRACE2__ARG2:
				setArg2((EObject)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case TracemodelPackage.TRACE2__ARG1:
				return arg1 != null;
			case TracemodelPackage.TRACE2__ARG2:
				return arg2 != null;
		}
		return super.eIsSet(featureID);
	}

} //Trace2Impl
