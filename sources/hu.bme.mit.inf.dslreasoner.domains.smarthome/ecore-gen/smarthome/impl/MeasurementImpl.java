/**
 */
package smarthome.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import smarthome.Measurement;
import smarthome.Person;
import smarthome.SmarthomePackage;

import uncertaindatatypes.UBoolean;
import uncertaindatatypes.UReal;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Measurement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link smarthome.impl.MeasurementImpl#getTemp <em>Temp</em>}</li>
 *   <li>{@link smarthome.impl.MeasurementImpl#getCo <em>Co</em>}</li>
 *   <li>{@link smarthome.impl.MeasurementImpl#getDopen <em>Dopen</em>}</li>
 *   <li>{@link smarthome.impl.MeasurementImpl#getTime <em>Time</em>}</li>
 *   <li>{@link smarthome.impl.MeasurementImpl#getAthome <em>Athome</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MeasurementImpl extends MinimalEObjectImpl.Container implements Measurement {
	/**
	 * The default value of the '{@link #getTemp() <em>Temp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTemp()
	 * @generated
	 * @ordered
	 */
	protected static final UReal TEMP_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTemp() <em>Temp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTemp()
	 * @generated
	 * @ordered
	 */
	protected UReal temp = TEMP_EDEFAULT;

	/**
	 * The default value of the '{@link #getCo() <em>Co</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCo()
	 * @generated
	 * @ordered
	 */
	protected static final UReal CO_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCo() <em>Co</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCo()
	 * @generated
	 * @ordered
	 */
	protected UReal co = CO_EDEFAULT;

	/**
	 * The default value of the '{@link #getDopen() <em>Dopen</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDopen()
	 * @generated
	 * @ordered
	 */
	protected static final UBoolean DOPEN_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDopen() <em>Dopen</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDopen()
	 * @generated
	 * @ordered
	 */
	protected UBoolean dopen = DOPEN_EDEFAULT;

	/**
	 * The default value of the '{@link #getTime() <em>Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTime()
	 * @generated
	 * @ordered
	 */
	protected static final UReal TIME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTime() <em>Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTime()
	 * @generated
	 * @ordered
	 */
	protected UReal time = TIME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getAthome() <em>Athome</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAthome()
	 * @generated
	 * @ordered
	 */
	protected EList<Person> athome;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MeasurementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SmarthomePackage.Literals.MEASUREMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UReal getTemp() {
		return temp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTemp(UReal newTemp) {
		UReal oldTemp = temp;
		temp = newTemp;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SmarthomePackage.MEASUREMENT__TEMP, oldTemp, temp));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UReal getCo() {
		return co;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCo(UReal newCo) {
		UReal oldCo = co;
		co = newCo;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SmarthomePackage.MEASUREMENT__CO, oldCo, co));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UBoolean getDopen() {
		return dopen;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDopen(UBoolean newDopen) {
		UBoolean oldDopen = dopen;
		dopen = newDopen;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SmarthomePackage.MEASUREMENT__DOPEN, oldDopen, dopen));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UReal getTime() {
		return time;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTime(UReal newTime) {
		UReal oldTime = time;
		time = newTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SmarthomePackage.MEASUREMENT__TIME, oldTime, time));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Person> getAthome() {
		if (athome == null) {
			athome = new EObjectResolvingEList<Person>(Person.class, this, SmarthomePackage.MEASUREMENT__ATHOME);
		}
		return athome;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SmarthomePackage.MEASUREMENT__TEMP:
				return getTemp();
			case SmarthomePackage.MEASUREMENT__CO:
				return getCo();
			case SmarthomePackage.MEASUREMENT__DOPEN:
				return getDopen();
			case SmarthomePackage.MEASUREMENT__TIME:
				return getTime();
			case SmarthomePackage.MEASUREMENT__ATHOME:
				return getAthome();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case SmarthomePackage.MEASUREMENT__TEMP:
				setTemp((UReal)newValue);
				return;
			case SmarthomePackage.MEASUREMENT__CO:
				setCo((UReal)newValue);
				return;
			case SmarthomePackage.MEASUREMENT__DOPEN:
				setDopen((UBoolean)newValue);
				return;
			case SmarthomePackage.MEASUREMENT__TIME:
				setTime((UReal)newValue);
				return;
			case SmarthomePackage.MEASUREMENT__ATHOME:
				getAthome().clear();
				getAthome().addAll((Collection<? extends Person>)newValue);
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
			case SmarthomePackage.MEASUREMENT__TEMP:
				setTemp(TEMP_EDEFAULT);
				return;
			case SmarthomePackage.MEASUREMENT__CO:
				setCo(CO_EDEFAULT);
				return;
			case SmarthomePackage.MEASUREMENT__DOPEN:
				setDopen(DOPEN_EDEFAULT);
				return;
			case SmarthomePackage.MEASUREMENT__TIME:
				setTime(TIME_EDEFAULT);
				return;
			case SmarthomePackage.MEASUREMENT__ATHOME:
				getAthome().clear();
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
			case SmarthomePackage.MEASUREMENT__TEMP:
				return TEMP_EDEFAULT == null ? temp != null : !TEMP_EDEFAULT.equals(temp);
			case SmarthomePackage.MEASUREMENT__CO:
				return CO_EDEFAULT == null ? co != null : !CO_EDEFAULT.equals(co);
			case SmarthomePackage.MEASUREMENT__DOPEN:
				return DOPEN_EDEFAULT == null ? dopen != null : !DOPEN_EDEFAULT.equals(dopen);
			case SmarthomePackage.MEASUREMENT__TIME:
				return TIME_EDEFAULT == null ? time != null : !TIME_EDEFAULT.equals(time);
			case SmarthomePackage.MEASUREMENT__ATHOME:
				return athome != null && !athome.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (temp: ");
		result.append(temp);
		result.append(", co: ");
		result.append(co);
		result.append(", dopen: ");
		result.append(dopen);
		result.append(", time: ");
		result.append(time);
		result.append(')');
		return result.toString();
	}

} //MeasurementImpl
