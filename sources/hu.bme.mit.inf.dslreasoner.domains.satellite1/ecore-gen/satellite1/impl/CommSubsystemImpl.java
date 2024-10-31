/**
 */
package satellite1.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import satellite1.CommSubsystem;
import satellite1.SatellitePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Comm Subsystem</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link satellite1.impl.CommSubsystemImpl#getTarget <em>Target</em>}</li>
 *   <li>{@link satellite1.impl.CommSubsystemImpl#getFallback <em>Fallback</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class CommSubsystemImpl extends MinimalEObjectImpl.Container implements CommSubsystem {
	/**
	 * The cached value of the '{@link #getTarget() <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTarget()
	 * @generated
	 * @ordered
	 */
	protected CommSubsystem target;

	/**
	 * The cached value of the '{@link #getFallback() <em>Fallback</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFallback()
	 * @generated
	 * @ordered
	 */
	protected CommSubsystem fallback;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CommSubsystemImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SatellitePackage.Literals.COMM_SUBSYSTEM;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CommSubsystem getTarget() {
		if (target != null && target.eIsProxy()) {
			InternalEObject oldTarget = (InternalEObject) target;
			target = (CommSubsystem) eResolveProxy(oldTarget);
			if (target != oldTarget) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SatellitePackage.COMM_SUBSYSTEM__TARGET,
							oldTarget, target));
			}
		}
		return target;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CommSubsystem basicGetTarget() {
		return target;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTarget(CommSubsystem newTarget) {
		CommSubsystem oldTarget = target;
		target = newTarget;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SatellitePackage.COMM_SUBSYSTEM__TARGET, oldTarget,
					target));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CommSubsystem getFallback() {
		if (fallback != null && fallback.eIsProxy()) {
			InternalEObject oldFallback = (InternalEObject) fallback;
			fallback = (CommSubsystem) eResolveProxy(oldFallback);
			if (fallback != oldFallback) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SatellitePackage.COMM_SUBSYSTEM__FALLBACK,
							oldFallback, fallback));
			}
		}
		return fallback;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CommSubsystem basicGetFallback() {
		return fallback;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFallback(CommSubsystem newFallback) {
		CommSubsystem oldFallback = fallback;
		fallback = newFallback;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SatellitePackage.COMM_SUBSYSTEM__FALLBACK,
					oldFallback, fallback));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case SatellitePackage.COMM_SUBSYSTEM__TARGET:
			if (resolve)
				return getTarget();
			return basicGetTarget();
		case SatellitePackage.COMM_SUBSYSTEM__FALLBACK:
			if (resolve)
				return getFallback();
			return basicGetFallback();
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
		case SatellitePackage.COMM_SUBSYSTEM__TARGET:
			setTarget((CommSubsystem) newValue);
			return;
		case SatellitePackage.COMM_SUBSYSTEM__FALLBACK:
			setFallback((CommSubsystem) newValue);
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
		case SatellitePackage.COMM_SUBSYSTEM__TARGET:
			setTarget((CommSubsystem) null);
			return;
		case SatellitePackage.COMM_SUBSYSTEM__FALLBACK:
			setFallback((CommSubsystem) null);
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
		case SatellitePackage.COMM_SUBSYSTEM__TARGET:
			return target != null;
		case SatellitePackage.COMM_SUBSYSTEM__FALLBACK:
			return fallback != null;
		}
		return super.eIsSet(featureID);
	}

} //CommSubsystemImpl
