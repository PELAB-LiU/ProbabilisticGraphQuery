/**
 */
package tracemodel.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import reliability.events.Event;

import tracemodel.Trace;
import tracemodel.TracemodelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Trace</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link tracemodel.impl.TraceImpl#getEvent <em>Event</em>}</li>
 *   <li>{@link tracemodel.impl.TraceImpl#getProbability <em>Probability</em>}</li>
 *   <li>{@link tracemodel.impl.TraceImpl#getGenerator <em>Generator</em>}</li>
 *   <li>{@link tracemodel.impl.TraceImpl#getIndex <em>Index</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class TraceImpl extends EObjectImpl implements Trace
{
	/**
	 * The default value of the '{@link #getEvent() <em>Event</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEvent()
	 * @generated
	 * @ordered
	 */
	protected static final Event EVENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEvent() <em>Event</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEvent()
	 * @generated
	 * @ordered
	 */
	protected Event event = EVENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getProbability() <em>Probability</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProbability()
	 * @generated
	 * @ordered
	 */
	protected static final double PROBABILITY_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getProbability() <em>Probability</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProbability()
	 * @generated
	 * @ordered
	 */
	protected double probability = PROBABILITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getGenerator() <em>Generator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGenerator()
	 * @generated
	 * @ordered
	 */
	protected static final String GENERATOR_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getGenerator() <em>Generator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGenerator()
	 * @generated
	 * @ordered
	 */
	protected String generator = GENERATOR_EDEFAULT;

	/**
	 * The default value of the '{@link #getIndex() <em>Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndex()
	 * @generated
	 * @ordered
	 */
	protected static final int INDEX_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getIndex() <em>Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndex()
	 * @generated
	 * @ordered
	 */
	protected int index = INDEX_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TraceImpl()
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
		return TracemodelPackage.Literals.TRACE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Event getEvent()
	{
		return event;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEvent(Event newEvent)
	{
		Event oldEvent = event;
		event = newEvent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracemodelPackage.TRACE__EVENT, oldEvent, event));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getProbability()
	{
		return probability;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setProbability(double newProbability)
	{
		double oldProbability = probability;
		probability = newProbability;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracemodelPackage.TRACE__PROBABILITY, oldProbability, probability));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getGenerator()
	{
		return generator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setGenerator(String newGenerator)
	{
		String oldGenerator = generator;
		generator = newGenerator;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracemodelPackage.TRACE__GENERATOR, oldGenerator, generator));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getIndex()
	{
		return index;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIndex(int newIndex)
	{
		int oldIndex = index;
		index = newIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracemodelPackage.TRACE__INDEX, oldIndex, index));
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
			case TracemodelPackage.TRACE__EVENT:
				return getEvent();
			case TracemodelPackage.TRACE__PROBABILITY:
				return getProbability();
			case TracemodelPackage.TRACE__GENERATOR:
				return getGenerator();
			case TracemodelPackage.TRACE__INDEX:
				return getIndex();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue)
	{
		switch (featureID)
		{
			case TracemodelPackage.TRACE__EVENT:
				setEvent((Event)newValue);
				return;
			case TracemodelPackage.TRACE__PROBABILITY:
				setProbability((Double)newValue);
				return;
			case TracemodelPackage.TRACE__GENERATOR:
				setGenerator((String)newValue);
				return;
			case TracemodelPackage.TRACE__INDEX:
				setIndex((Integer)newValue);
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
			case TracemodelPackage.TRACE__EVENT:
				setEvent(EVENT_EDEFAULT);
				return;
			case TracemodelPackage.TRACE__PROBABILITY:
				setProbability(PROBABILITY_EDEFAULT);
				return;
			case TracemodelPackage.TRACE__GENERATOR:
				setGenerator(GENERATOR_EDEFAULT);
				return;
			case TracemodelPackage.TRACE__INDEX:
				setIndex(INDEX_EDEFAULT);
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
			case TracemodelPackage.TRACE__EVENT:
				return EVENT_EDEFAULT == null ? event != null : !EVENT_EDEFAULT.equals(event);
			case TracemodelPackage.TRACE__PROBABILITY:
				return probability != PROBABILITY_EDEFAULT;
			case TracemodelPackage.TRACE__GENERATOR:
				return GENERATOR_EDEFAULT == null ? generator != null : !GENERATOR_EDEFAULT.equals(generator);
			case TracemodelPackage.TRACE__INDEX:
				return index != INDEX_EDEFAULT;
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
		result.append(" (event: ");
		result.append(event);
		result.append(", probability: ");
		result.append(probability);
		result.append(", generator: ");
		result.append(generator);
		result.append(", index: ");
		result.append(index);
		result.append(')');
		return result.toString();
	}

} //TraceImpl
