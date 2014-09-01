/**
 */
package net.descartesresearch.librede.configuration.impl;

import java.util.Collection;

import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.DataProviderConfiguration;
import net.descartesresearch.librede.configuration.DataSourceConfiguration;
import net.descartesresearch.librede.configuration.Parameter;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Data Source Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.descartesresearch.librede.configuration.impl.DataSourceConfigurationImpl#getDataProvider <em>Data Provider</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.impl.DataSourceConfigurationImpl#getType <em>Type</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.impl.DataSourceConfigurationImpl#getParameters <em>Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DataSourceConfigurationImpl extends MinimalEObjectImpl.Container implements DataSourceConfiguration {
	/**
	 * The cached value of the '{@link #getDataProvider() <em>Data Provider</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataProvider()
	 * @generated
	 * @ordered
	 */
	protected DataProviderConfiguration dataProvider;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected Class<?> type;

	/**
	 * The cached value of the '{@link #getParameters() <em>Parameters</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameters()
	 * @generated
	 * @ordered
	 */
	protected EList<Parameter> parameters;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DataSourceConfigurationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConfigurationPackage.Literals.DATA_SOURCE_CONFIGURATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataProviderConfiguration getDataProvider() {
		if (dataProvider != null && dataProvider.eIsProxy()) {
			InternalEObject oldDataProvider = (InternalEObject)dataProvider;
			dataProvider = (DataProviderConfiguration)eResolveProxy(oldDataProvider);
			if (dataProvider != oldDataProvider) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ConfigurationPackage.DATA_SOURCE_CONFIGURATION__DATA_PROVIDER, oldDataProvider, dataProvider));
			}
		}
		return dataProvider;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataProviderConfiguration basicGetDataProvider() {
		return dataProvider;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDataProvider(DataProviderConfiguration newDataProvider) {
		DataProviderConfiguration oldDataProvider = dataProvider;
		dataProvider = newDataProvider;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.DATA_SOURCE_CONFIGURATION__DATA_PROVIDER, oldDataProvider, dataProvider));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Parameter> getParameters() {
		if (parameters == null) {
			parameters = new EObjectContainmentEList<Parameter>(Parameter.class, this, ConfigurationPackage.DATA_SOURCE_CONFIGURATION__PARAMETERS);
		}
		return parameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Class<?> getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(Class<?> newType) {
		Class<?> oldType = type;
		type = newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.DATA_SOURCE_CONFIGURATION__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ConfigurationPackage.DATA_SOURCE_CONFIGURATION__PARAMETERS:
				return ((InternalEList<?>)getParameters()).basicRemove(otherEnd, msgs);
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
			case ConfigurationPackage.DATA_SOURCE_CONFIGURATION__DATA_PROVIDER:
				if (resolve) return getDataProvider();
				return basicGetDataProvider();
			case ConfigurationPackage.DATA_SOURCE_CONFIGURATION__TYPE:
				return getType();
			case ConfigurationPackage.DATA_SOURCE_CONFIGURATION__PARAMETERS:
				return getParameters();
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
			case ConfigurationPackage.DATA_SOURCE_CONFIGURATION__DATA_PROVIDER:
				setDataProvider((DataProviderConfiguration)newValue);
				return;
			case ConfigurationPackage.DATA_SOURCE_CONFIGURATION__TYPE:
				setType((Class<?>)newValue);
				return;
			case ConfigurationPackage.DATA_SOURCE_CONFIGURATION__PARAMETERS:
				getParameters().clear();
				getParameters().addAll((Collection<? extends Parameter>)newValue);
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
			case ConfigurationPackage.DATA_SOURCE_CONFIGURATION__DATA_PROVIDER:
				setDataProvider((DataProviderConfiguration)null);
				return;
			case ConfigurationPackage.DATA_SOURCE_CONFIGURATION__TYPE:
				setType((Class<?>)null);
				return;
			case ConfigurationPackage.DATA_SOURCE_CONFIGURATION__PARAMETERS:
				getParameters().clear();
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
			case ConfigurationPackage.DATA_SOURCE_CONFIGURATION__DATA_PROVIDER:
				return dataProvider != null;
			case ConfigurationPackage.DATA_SOURCE_CONFIGURATION__TYPE:
				return type != null;
			case ConfigurationPackage.DATA_SOURCE_CONFIGURATION__PARAMETERS:
				return parameters != null && !parameters.isEmpty();
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

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (type: ");
		result.append(type);
		result.append(')');
		return result.toString();
	}

} //DataSourceConfigurationImpl
