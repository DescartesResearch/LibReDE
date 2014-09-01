/**
 */
package net.descartesresearch.librede.configuration.impl;

import java.util.Collection;
import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.DataSourceConfiguration;
import net.descartesresearch.librede.configuration.Resource;
import net.descartesresearch.librede.configuration.Service;
import net.descartesresearch.librede.configuration.TraceConfiguration;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Trace Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.descartesresearch.librede.configuration.impl.TraceConfigurationImpl#getMetric <em>Metric</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.impl.TraceConfigurationImpl#getUnit <em>Unit</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.impl.TraceConfigurationImpl#getResources <em>Resources</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.impl.TraceConfigurationImpl#getServices <em>Services</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.impl.TraceConfigurationImpl#getInterval <em>Interval</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.impl.TraceConfigurationImpl#getData <em>Data</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TraceConfigurationImpl extends MinimalEObjectImpl.Container implements TraceConfiguration {
	/**
	 * The default value of the '{@link #getMetric() <em>Metric</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMetric()
	 * @generated
	 * @ordered
	 */
	protected static final String METRIC_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMetric() <em>Metric</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMetric()
	 * @generated
	 * @ordered
	 */
	protected String metric = METRIC_EDEFAULT;

	/**
	 * The default value of the '{@link #getUnit() <em>Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnit()
	 * @generated
	 * @ordered
	 */
	protected static final String UNIT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUnit() <em>Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnit()
	 * @generated
	 * @ordered
	 */
	protected String unit = UNIT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getResources() <em>Resources</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResources()
	 * @generated
	 * @ordered
	 */
	protected EList<Resource> resources;

	/**
	 * The cached value of the '{@link #getServices() <em>Services</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getServices()
	 * @generated
	 * @ordered
	 */
	protected EList<Service> services;

	/**
	 * The default value of the '{@link #getInterval() <em>Interval</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInterval()
	 * @generated
	 * @ordered
	 */
	protected static final long INTERVAL_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getInterval() <em>Interval</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInterval()
	 * @generated
	 * @ordered
	 */
	protected long interval = INTERVAL_EDEFAULT;

	/**
	 * The cached value of the '{@link #getData() <em>Data</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getData()
	 * @generated
	 * @ordered
	 */
	protected DataSourceConfiguration data;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TraceConfigurationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConfigurationPackage.Literals.TRACE_CONFIGURATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getMetric() {
		return metric;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMetric(String newMetric) {
		String oldMetric = metric;
		metric = newMetric;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.TRACE_CONFIGURATION__METRIC, oldMetric, metric));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUnit(String newUnit) {
		String oldUnit = unit;
		unit = newUnit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.TRACE_CONFIGURATION__UNIT, oldUnit, unit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Resource> getResources() {
		if (resources == null) {
			resources = new EObjectResolvingEList<Resource>(Resource.class, this, ConfigurationPackage.TRACE_CONFIGURATION__RESOURCES);
		}
		return resources;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Service> getServices() {
		if (services == null) {
			services = new EObjectResolvingEList<Service>(Service.class, this, ConfigurationPackage.TRACE_CONFIGURATION__SERVICES);
		}
		return services;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getInterval() {
		return interval;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInterval(long newInterval) {
		long oldInterval = interval;
		interval = newInterval;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.TRACE_CONFIGURATION__INTERVAL, oldInterval, interval));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataSourceConfiguration getData() {
		return data;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetData(DataSourceConfiguration newData, NotificationChain msgs) {
		DataSourceConfiguration oldData = data;
		data = newData;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ConfigurationPackage.TRACE_CONFIGURATION__DATA, oldData, newData);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setData(DataSourceConfiguration newData) {
		if (newData != data) {
			NotificationChain msgs = null;
			if (data != null)
				msgs = ((InternalEObject)data).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ConfigurationPackage.TRACE_CONFIGURATION__DATA, null, msgs);
			if (newData != null)
				msgs = ((InternalEObject)newData).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ConfigurationPackage.TRACE_CONFIGURATION__DATA, null, msgs);
			msgs = basicSetData(newData, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.TRACE_CONFIGURATION__DATA, newData, newData));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ConfigurationPackage.TRACE_CONFIGURATION__DATA:
				return basicSetData(null, msgs);
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
			case ConfigurationPackage.TRACE_CONFIGURATION__METRIC:
				return getMetric();
			case ConfigurationPackage.TRACE_CONFIGURATION__UNIT:
				return getUnit();
			case ConfigurationPackage.TRACE_CONFIGURATION__RESOURCES:
				return getResources();
			case ConfigurationPackage.TRACE_CONFIGURATION__SERVICES:
				return getServices();
			case ConfigurationPackage.TRACE_CONFIGURATION__INTERVAL:
				return getInterval();
			case ConfigurationPackage.TRACE_CONFIGURATION__DATA:
				return getData();
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
			case ConfigurationPackage.TRACE_CONFIGURATION__METRIC:
				setMetric((String)newValue);
				return;
			case ConfigurationPackage.TRACE_CONFIGURATION__UNIT:
				setUnit((String)newValue);
				return;
			case ConfigurationPackage.TRACE_CONFIGURATION__RESOURCES:
				getResources().clear();
				getResources().addAll((Collection<? extends Resource>)newValue);
				return;
			case ConfigurationPackage.TRACE_CONFIGURATION__SERVICES:
				getServices().clear();
				getServices().addAll((Collection<? extends Service>)newValue);
				return;
			case ConfigurationPackage.TRACE_CONFIGURATION__INTERVAL:
				setInterval((Long)newValue);
				return;
			case ConfigurationPackage.TRACE_CONFIGURATION__DATA:
				setData((DataSourceConfiguration)newValue);
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
			case ConfigurationPackage.TRACE_CONFIGURATION__METRIC:
				setMetric(METRIC_EDEFAULT);
				return;
			case ConfigurationPackage.TRACE_CONFIGURATION__UNIT:
				setUnit(UNIT_EDEFAULT);
				return;
			case ConfigurationPackage.TRACE_CONFIGURATION__RESOURCES:
				getResources().clear();
				return;
			case ConfigurationPackage.TRACE_CONFIGURATION__SERVICES:
				getServices().clear();
				return;
			case ConfigurationPackage.TRACE_CONFIGURATION__INTERVAL:
				setInterval(INTERVAL_EDEFAULT);
				return;
			case ConfigurationPackage.TRACE_CONFIGURATION__DATA:
				setData((DataSourceConfiguration)null);
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
			case ConfigurationPackage.TRACE_CONFIGURATION__METRIC:
				return METRIC_EDEFAULT == null ? metric != null : !METRIC_EDEFAULT.equals(metric);
			case ConfigurationPackage.TRACE_CONFIGURATION__UNIT:
				return UNIT_EDEFAULT == null ? unit != null : !UNIT_EDEFAULT.equals(unit);
			case ConfigurationPackage.TRACE_CONFIGURATION__RESOURCES:
				return resources != null && !resources.isEmpty();
			case ConfigurationPackage.TRACE_CONFIGURATION__SERVICES:
				return services != null && !services.isEmpty();
			case ConfigurationPackage.TRACE_CONFIGURATION__INTERVAL:
				return interval != INTERVAL_EDEFAULT;
			case ConfigurationPackage.TRACE_CONFIGURATION__DATA:
				return data != null;
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
		result.append(" (metric: ");
		result.append(metric);
		result.append(", unit: ");
		result.append(unit);
		result.append(", interval: ");
		result.append(interval);
		result.append(')');
		return result.toString();
	}

} //TraceConfigurationImpl
