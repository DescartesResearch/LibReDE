/**
 */
package net.descartesresearch.librede.configuration.impl;

import java.util.Collection;
import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.DataProviderConfiguration;
import net.descartesresearch.librede.configuration.InputSpecification;
import net.descartesresearch.librede.configuration.TraceConfiguration;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Input Specification</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.descartesresearch.librede.configuration.impl.InputSpecificationImpl#getDataProviders <em>Data Providers</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.impl.InputSpecificationImpl#getObservations <em>Observations</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InputSpecificationImpl extends MinimalEObjectImpl.Container implements InputSpecification {
	/**
	 * The cached value of the '{@link #getDataProviders() <em>Data Providers</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataProviders()
	 * @generated
	 * @ordered
	 */
	protected EList<DataProviderConfiguration> dataProviders;

	/**
	 * The cached value of the '{@link #getObservations() <em>Observations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObservations()
	 * @generated
	 * @ordered
	 */
	protected EList<TraceConfiguration> observations;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InputSpecificationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConfigurationPackage.Literals.INPUT_SPECIFICATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<DataProviderConfiguration> getDataProviders() {
		if (dataProviders == null) {
			dataProviders = new EObjectContainmentEList<DataProviderConfiguration>(DataProviderConfiguration.class, this, ConfigurationPackage.INPUT_SPECIFICATION__DATA_PROVIDERS);
		}
		return dataProviders;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TraceConfiguration> getObservations() {
		if (observations == null) {
			observations = new EObjectContainmentEList<TraceConfiguration>(TraceConfiguration.class, this, ConfigurationPackage.INPUT_SPECIFICATION__OBSERVATIONS);
		}
		return observations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ConfigurationPackage.INPUT_SPECIFICATION__DATA_PROVIDERS:
				return ((InternalEList<?>)getDataProviders()).basicRemove(otherEnd, msgs);
			case ConfigurationPackage.INPUT_SPECIFICATION__OBSERVATIONS:
				return ((InternalEList<?>)getObservations()).basicRemove(otherEnd, msgs);
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
			case ConfigurationPackage.INPUT_SPECIFICATION__DATA_PROVIDERS:
				return getDataProviders();
			case ConfigurationPackage.INPUT_SPECIFICATION__OBSERVATIONS:
				return getObservations();
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
			case ConfigurationPackage.INPUT_SPECIFICATION__DATA_PROVIDERS:
				getDataProviders().clear();
				getDataProviders().addAll((Collection<? extends DataProviderConfiguration>)newValue);
				return;
			case ConfigurationPackage.INPUT_SPECIFICATION__OBSERVATIONS:
				getObservations().clear();
				getObservations().addAll((Collection<? extends TraceConfiguration>)newValue);
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
			case ConfigurationPackage.INPUT_SPECIFICATION__DATA_PROVIDERS:
				getDataProviders().clear();
				return;
			case ConfigurationPackage.INPUT_SPECIFICATION__OBSERVATIONS:
				getObservations().clear();
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
			case ConfigurationPackage.INPUT_SPECIFICATION__DATA_PROVIDERS:
				return dataProviders != null && !dataProviders.isEmpty();
			case ConfigurationPackage.INPUT_SPECIFICATION__OBSERVATIONS:
				return observations != null && !observations.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //InputSpecificationImpl
