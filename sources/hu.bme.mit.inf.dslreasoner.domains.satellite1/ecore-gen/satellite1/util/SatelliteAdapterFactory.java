/**
 */
package satellite1.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import satellite1.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see satellite1.SatellitePackage
 * @generated
 */
public class SatelliteAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static SatellitePackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SatelliteAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = SatellitePackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject) object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SatelliteSwitch<Adapter> modelSwitch = new SatelliteSwitch<Adapter>() {
		@Override
		public Adapter caseConstellationMission(ConstellationMission object) {
			return createConstellationMissionAdapter();
		}

		@Override
		public Adapter caseCommunicatingElement(CommunicatingElement object) {
			return createCommunicatingElementAdapter();
		}

		@Override
		public Adapter caseGroundStationNetwork(GroundStationNetwork object) {
			return createGroundStationNetworkAdapter();
		}

		@Override
		public Adapter caseInterferometryMission(InterferometryMission object) {
			return createInterferometryMissionAdapter();
		}

		@Override
		public Adapter caseSpacecraft(Spacecraft object) {
			return createSpacecraftAdapter();
		}

		@Override
		public Adapter caseCommSubsystem(CommSubsystem object) {
			return createCommSubsystemAdapter();
		}

		@Override
		public Adapter casePayload(Payload object) {
			return createPayloadAdapter();
		}

		@Override
		public Adapter caseInterferometryPayload(InterferometryPayload object) {
			return createInterferometryPayloadAdapter();
		}

		@Override
		public Adapter caseCubeSat3U(CubeSat3U object) {
			return createCubeSat3UAdapter();
		}

		@Override
		public Adapter caseCubeSat6U(CubeSat6U object) {
			return createCubeSat6UAdapter();
		}

		@Override
		public Adapter caseSmallSat(SmallSat object) {
			return createSmallSatAdapter();
		}

		@Override
		public Adapter caseCubeSat(CubeSat object) {
			return createCubeSatAdapter();
		}

		@Override
		public Adapter caseUHFCommSubsystem(UHFCommSubsystem object) {
			return createUHFCommSubsystemAdapter();
		}

		@Override
		public Adapter caseXCommSubsystem(XCommSubsystem object) {
			return createXCommSubsystemAdapter();
		}

		@Override
		public Adapter caseKaCommSubsystem(KaCommSubsystem object) {
			return createKaCommSubsystemAdapter();
		}

		@Override
		public Adapter defaultCase(EObject object) {
			return createEObjectAdapter();
		}
	};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject) target);
	}

	/**
	 * Creates a new adapter for an object of class '{@link satellite1.ConstellationMission <em>Constellation Mission</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see satellite1.ConstellationMission
	 * @generated
	 */
	public Adapter createConstellationMissionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link satellite1.CommunicatingElement <em>Communicating Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see satellite1.CommunicatingElement
	 * @generated
	 */
	public Adapter createCommunicatingElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link satellite1.GroundStationNetwork <em>Ground Station Network</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see satellite1.GroundStationNetwork
	 * @generated
	 */
	public Adapter createGroundStationNetworkAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link satellite1.InterferometryMission <em>Interferometry Mission</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see satellite1.InterferometryMission
	 * @generated
	 */
	public Adapter createInterferometryMissionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link satellite1.Spacecraft <em>Spacecraft</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see satellite1.Spacecraft
	 * @generated
	 */
	public Adapter createSpacecraftAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link satellite1.CommSubsystem <em>Comm Subsystem</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see satellite1.CommSubsystem
	 * @generated
	 */
	public Adapter createCommSubsystemAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link satellite1.Payload <em>Payload</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see satellite1.Payload
	 * @generated
	 */
	public Adapter createPayloadAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link satellite1.InterferometryPayload <em>Interferometry Payload</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see satellite1.InterferometryPayload
	 * @generated
	 */
	public Adapter createInterferometryPayloadAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link satellite1.CubeSat3U <em>Cube Sat3 U</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see satellite1.CubeSat3U
	 * @generated
	 */
	public Adapter createCubeSat3UAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link satellite1.CubeSat6U <em>Cube Sat6 U</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see satellite1.CubeSat6U
	 * @generated
	 */
	public Adapter createCubeSat6UAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link satellite1.SmallSat <em>Small Sat</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see satellite1.SmallSat
	 * @generated
	 */
	public Adapter createSmallSatAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link satellite1.CubeSat <em>Cube Sat</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see satellite1.CubeSat
	 * @generated
	 */
	public Adapter createCubeSatAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link satellite1.UHFCommSubsystem <em>UHF Comm Subsystem</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see satellite1.UHFCommSubsystem
	 * @generated
	 */
	public Adapter createUHFCommSubsystemAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link satellite1.XCommSubsystem <em>XComm Subsystem</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see satellite1.XCommSubsystem
	 * @generated
	 */
	public Adapter createXCommSubsystemAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link satellite1.KaCommSubsystem <em>Ka Comm Subsystem</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see satellite1.KaCommSubsystem
	 * @generated
	 */
	public Adapter createKaCommSubsystemAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //SatelliteAdapterFactory
