/**
 */
package smarthome.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import smarthome.Home;
import smarthome.Person;
import smarthome.Smarthome;
import smarthome.SmarthomePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Smarthome</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link smarthome.impl.SmarthomeImpl#getPersons <em>Persons</em>}</li>
 *   <li>{@link smarthome.impl.SmarthomeImpl#getHomes <em>Homes</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SmarthomeImpl extends MinimalEObjectImpl.Container implements Smarthome {
	/**
	 * The cached value of the '{@link #getPersons() <em>Persons</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPersons()
	 * @generated
	 * @ordered
	 */
	protected EList<Person> persons;

	/**
	 * The cached value of the '{@link #getHomes() <em>Homes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHomes()
	 * @generated
	 * @ordered
	 */
	protected EList<Home> homes;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SmarthomeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SmarthomePackage.Literals.SMARTHOME;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Person> getPersons() {
		if (persons == null) {
			persons = new EObjectContainmentEList<Person>(Person.class, this, SmarthomePackage.SMARTHOME__PERSONS);
		}
		return persons;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Home> getHomes() {
		if (homes == null) {
			homes = new EObjectContainmentEList<Home>(Home.class, this, SmarthomePackage.SMARTHOME__HOMES);
		}
		return homes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SmarthomePackage.SMARTHOME__PERSONS:
				return ((InternalEList<?>)getPersons()).basicRemove(otherEnd, msgs);
			case SmarthomePackage.SMARTHOME__HOMES:
				return ((InternalEList<?>)getHomes()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SmarthomePackage.SMARTHOME__PERSONS:
				return getPersons();
			case SmarthomePackage.SMARTHOME__HOMES:
				return getHomes();
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
			case SmarthomePackage.SMARTHOME__PERSONS:
				getPersons().clear();
				getPersons().addAll((Collection<? extends Person>)newValue);
				return;
			case SmarthomePackage.SMARTHOME__HOMES:
				getHomes().clear();
				getHomes().addAll((Collection<? extends Home>)newValue);
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
			case SmarthomePackage.SMARTHOME__PERSONS:
				getPersons().clear();
				return;
			case SmarthomePackage.SMARTHOME__HOMES:
				getHomes().clear();
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
			case SmarthomePackage.SMARTHOME__PERSONS:
				return persons != null && !persons.isEmpty();
			case SmarthomePackage.SMARTHOME__HOMES:
				return homes != null && !homes.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //SmarthomeImpl
