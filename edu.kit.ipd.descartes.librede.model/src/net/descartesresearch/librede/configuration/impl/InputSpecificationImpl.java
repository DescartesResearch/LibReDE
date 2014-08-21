/**
 */
package net.descartesresearch.librede.configuration.impl;

import java.util.Collection;

import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.DataSource;
import net.descartesresearch.librede.configuration.InputSpecification;
import net.descartesresearch.librede.configuration.TimeSeries;

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
 *   <li>{@link net.descartesresearch.librede.configuration.impl.InputSpecificationImpl#getDataSources <em>Data Sources</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.impl.InputSpecificationImpl#getObservations <em>Observations</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InputSpecificationImpl extends MinimalEObjectImpl.Container implements InputSpecification {
	/**
	 * The cached value of the '{@link #getDataSources() <em>Data Sources</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataSources()
	 * @generated
	 * @ordered
	 */
	protected EList<DataSource> dataSources;

	/**
	 * The cached value of the '{@link #getObservations() <em>Observations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObservations()
	 * @generated
	 * @ordered
	 */
	protected EList<TimeSeries> observations;

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
	public EList<DataSource> getDataSources() {
		if (dataSources == null) {
			dataSources = new EObjectContainmentEList<DataSource>(DataSource.class, this, ConfigurationPackage.INPUT_SPECIFICATION__DATA_SOURCES);
		}
		return dataSources;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TimeSeries> getObservations() {
		if (observations == null) {
			observations = new EObjectContainmentEList<TimeSeries>(TimeSeries.class, this, ConfigurationPackage.INPUT_SPECIFICATION__OBSERVATIONS);
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
			case ConfigurationPackage.INPUT_SPECIFICATION__DATA_SOURCES:
				return ((InternalEList<?>)getDataSources()).basicRemove(otherEnd, msgs);
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
			case ConfigurationPackage.INPUT_SPECIFICATION__DATA_SOURCES:
				return getDataSources();
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
			case ConfigurationPackage.INPUT_SPECIFICATION__DATA_SOURCES:
				getDataSources().clear();
				getDataSources().addAll((Collection<? extends DataSource>)newValue);
				return;
			case ConfigurationPackage.INPUT_SPECIFICATION__OBSERVATIONS:
				getObservations().clear();
				getObservations().addAll((Collection<? extends TimeSeries>)newValue);
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
			case ConfigurationPackage.INPUT_SPECIFICATION__DATA_SOURCES:
				getDataSources().clear();
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
			case ConfigurationPackage.INPUT_SPECIFICATION__DATA_SOURCES:
				return dataSources != null && !dataSources.isEmpty();
			case ConfigurationPackage.INPUT_SPECIFICATION__OBSERVATIONS:
				return observations != null && !observations.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //InputSpecificationImpl
