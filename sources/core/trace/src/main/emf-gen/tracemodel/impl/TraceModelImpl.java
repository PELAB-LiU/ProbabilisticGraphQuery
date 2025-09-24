/**
 */
package tracemodel.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import reliability.events.Event;

import reliability.mdd.ProbabilityMap;

import tracemodel.Trace;
import tracemodel.TraceModel;
import tracemodel.TracemodelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Trace Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link tracemodel.impl.TraceModelImpl#getTraces <em>Traces</em>}</li>
 *   <li>{@link tracemodel.impl.TraceModelImpl#getMddTrue <em>Mdd True</em>}</li>
 *   <li>{@link tracemodel.impl.TraceModelImpl#getMddFalse <em>Mdd False</em>}</li>
 *   <li>{@link tracemodel.impl.TraceModelImpl#getProbabilities <em>Probabilities</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TraceModelImpl extends EObjectImpl implements TraceModel
{
	/**
	 * The cached value of the '{@link #getTraces() <em>Traces</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTraces()
	 * @generated
	 * @ordered
	 */
	protected EList<Trace> traces;

	/**
	 * The default value of the '{@link #getMddTrue() <em>Mdd True</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMddTrue()
	 * @generated
	 * @ordered
	 */
	protected static final Event MDD_TRUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMddTrue() <em>Mdd True</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMddTrue()
	 * @generated
	 * @ordered
	 */
	protected Event mddTrue = MDD_TRUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getMddFalse() <em>Mdd False</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMddFalse()
	 * @generated
	 * @ordered
	 */
	protected static final Event MDD_FALSE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMddFalse() <em>Mdd False</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMddFalse()
	 * @generated
	 * @ordered
	 */
	protected Event mddFalse = MDD_FALSE_EDEFAULT;

	/**
	 * The default value of the '{@link #getProbabilities() <em>Probabilities</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProbabilities()
	 * @generated
	 * @ordered
	 */
	protected static final ProbabilityMap PROBABILITIES_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getProbabilities() <em>Probabilities</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProbabilities()
	 * @generated
	 * @ordered
	 */
	protected ProbabilityMap probabilities = PROBABILITIES_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TraceModelImpl()
	{
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass()
	{
		return TracemodelPackage.Literals.TRACE_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Trace> getTraces()
	{
		if (traces == null)
		{
			traces = new EObjectContainmentEList<Trace>(Trace.class, this, TracemodelPackage.TRACE_MODEL__TRACES);
		}
		return traces;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Event getMddTrue()
	{
		return mddTrue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMddTrue(Event newMddTrue)
	{
		Event oldMddTrue = mddTrue;
		mddTrue = newMddTrue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracemodelPackage.TRACE_MODEL__MDD_TRUE, oldMddTrue, mddTrue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Event getMddFalse()
	{
		return mddFalse;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMddFalse(Event newMddFalse)
	{
		Event oldMddFalse = mddFalse;
		mddFalse = newMddFalse;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracemodelPackage.TRACE_MODEL__MDD_FALSE, oldMddFalse, mddFalse));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ProbabilityMap getProbabilities()
	{
		return probabilities;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setProbabilities(ProbabilityMap newProbabilities)
	{
		ProbabilityMap oldProbabilities = probabilities;
		probabilities = newProbabilities;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracemodelPackage.TRACE_MODEL__PROBABILITIES, oldProbabilities, probabilities));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
	{
		switch (featureID)
		{
			case TracemodelPackage.TRACE_MODEL__TRACES:
				return ((InternalEList<?>)getTraces()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType)
	{
		switch (featureID)
		{
			case TracemodelPackage.TRACE_MODEL__TRACES:
				return getTraces();
			case TracemodelPackage.TRACE_MODEL__MDD_TRUE:
				return getMddTrue();
			case TracemodelPackage.TRACE_MODEL__MDD_FALSE:
				return getMddFalse();
			case TracemodelPackage.TRACE_MODEL__PROBABILITIES:
				return getProbabilities();
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
	public void eSet(int featureID, Object newValue)
	{
		switch (featureID)
		{
			case TracemodelPackage.TRACE_MODEL__TRACES:
				getTraces().clear();
				getTraces().addAll((Collection<? extends Trace>)newValue);
				return;
			case TracemodelPackage.TRACE_MODEL__MDD_TRUE:
				setMddTrue((Event)newValue);
				return;
			case TracemodelPackage.TRACE_MODEL__MDD_FALSE:
				setMddFalse((Event)newValue);
				return;
			case TracemodelPackage.TRACE_MODEL__PROBABILITIES:
				setProbabilities((ProbabilityMap)newValue);
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
	public void eUnset(int featureID)
	{
		switch (featureID)
		{
			case TracemodelPackage.TRACE_MODEL__TRACES:
				getTraces().clear();
				return;
			case TracemodelPackage.TRACE_MODEL__MDD_TRUE:
				setMddTrue(MDD_TRUE_EDEFAULT);
				return;
			case TracemodelPackage.TRACE_MODEL__MDD_FALSE:
				setMddFalse(MDD_FALSE_EDEFAULT);
				return;
			case TracemodelPackage.TRACE_MODEL__PROBABILITIES:
				setProbabilities(PROBABILITIES_EDEFAULT);
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
	public boolean eIsSet(int featureID)
	{
		switch (featureID)
		{
			case TracemodelPackage.TRACE_MODEL__TRACES:
				return traces != null && !traces.isEmpty();
			case TracemodelPackage.TRACE_MODEL__MDD_TRUE:
				return MDD_TRUE_EDEFAULT == null ? mddTrue != null : !MDD_TRUE_EDEFAULT.equals(mddTrue);
			case TracemodelPackage.TRACE_MODEL__MDD_FALSE:
				return MDD_FALSE_EDEFAULT == null ? mddFalse != null : !MDD_FALSE_EDEFAULT.equals(mddFalse);
			case TracemodelPackage.TRACE_MODEL__PROBABILITIES:
				return PROBABILITIES_EDEFAULT == null ? probabilities != null : !PROBABILITIES_EDEFAULT.equals(probabilities);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString()
	{
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (mddTrue: ");
		result.append(mddTrue);
		result.append(", mddFalse: ");
		result.append(mddFalse);
		result.append(", probabilities: ");
		result.append(probabilities);
		result.append(')');
		return result.toString();
	}

} //TraceModelImpl
