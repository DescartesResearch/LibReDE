/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2014, by Simon Spinner and Contributors.
 *
 * Project Info:   http://www.descartes-research.net/
 *
 * All rights reserved. This software is made available under the terms of the
 * Eclipse Public License (EPL) v1.0 as published by the Eclipse Foundation
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Eclipse Public License (EPL)
 * for more details.
 *
 * You should have received a copy of the Eclipse Public License (EPL)
 * along with this software; if not visit http://www.eclipse.org or write to
 * Eclipse Foundation, Inc., 308 SW First Avenue, Suite 110, Portland, 97204 USA
 * Email: license (at) eclipse.org
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 */
/**
 */
package tools.descartes.librede.configuration.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import tools.descartes.librede.configuration.ConfigurationPackage;
import tools.descartes.librede.configuration.DataSourceConfiguration;
import tools.descartes.librede.configuration.TraceConfiguration;
import tools.descartes.librede.configuration.TraceToEntityMapping;
import tools.descartes.librede.units.Unit;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Trace Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link tools.descartes.librede.configuration.impl.TraceConfigurationImpl#getMetric <em>Metric</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.TraceConfigurationImpl#getInterval <em>Interval</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.TraceConfigurationImpl#getDataSource <em>Data Source</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.TraceConfigurationImpl#getMappings <em>Mappings</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.TraceConfigurationImpl#getUnit <em>Unit</em>}</li>
 * </ul>
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
	 * The cached value of the '{@link #getDataSource() <em>Data Source</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataSource()
	 * @generated
	 * @ordered
	 */
	protected DataSourceConfiguration dataSource;

	/**
	 * The cached value of the '{@link #getMappings() <em>Mappings</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMappings()
	 * @generated
	 * @ordered
	 */
	protected EList<TraceToEntityMapping> mappings;

	/**
	 * The cached value of the '{@link #getUnit() <em>Unit</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnit()
	 * @generated
	 * @ordered
	 */
	protected Unit<?> unit;

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
	public Unit<?> getUnit() {
		if (unit != null && unit.eIsProxy()) {
			InternalEObject oldUnit = (InternalEObject)unit;
			unit = (Unit<?>)eResolveProxy(oldUnit);
			if (unit != oldUnit) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ConfigurationPackage.TRACE_CONFIGURATION__UNIT, oldUnit, unit));
			}
		}
		return unit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Unit<?> basicGetUnit() {
		return unit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUnit(Unit<?> newUnit) {
		Unit<?> oldUnit = unit;
		unit = newUnit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.TRACE_CONFIGURATION__UNIT, oldUnit, unit));
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
	public DataSourceConfiguration getDataSource() {
		if (dataSource != null && dataSource.eIsProxy()) {
			InternalEObject oldDataSource = (InternalEObject)dataSource;
			dataSource = (DataSourceConfiguration)eResolveProxy(oldDataSource);
			if (dataSource != oldDataSource) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ConfigurationPackage.TRACE_CONFIGURATION__DATA_SOURCE, oldDataSource, dataSource));
			}
		}
		return dataSource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataSourceConfiguration basicGetDataSource() {
		return dataSource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDataSource(DataSourceConfiguration newDataSource) {
		DataSourceConfiguration oldDataSource = dataSource;
		dataSource = newDataSource;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.TRACE_CONFIGURATION__DATA_SOURCE, oldDataSource, dataSource));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TraceToEntityMapping> getMappings() {
		if (mappings == null) {
			mappings = new EObjectContainmentEList<TraceToEntityMapping>(TraceToEntityMapping.class, this, ConfigurationPackage.TRACE_CONFIGURATION__MAPPINGS);
		}
		return mappings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ConfigurationPackage.TRACE_CONFIGURATION__MAPPINGS:
				return ((InternalEList<?>)getMappings()).basicRemove(otherEnd, msgs);
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
			case ConfigurationPackage.TRACE_CONFIGURATION__INTERVAL:
				return getInterval();
			case ConfigurationPackage.TRACE_CONFIGURATION__DATA_SOURCE:
				if (resolve) return getDataSource();
				return basicGetDataSource();
			case ConfigurationPackage.TRACE_CONFIGURATION__MAPPINGS:
				return getMappings();
			case ConfigurationPackage.TRACE_CONFIGURATION__UNIT:
				if (resolve) return getUnit();
				return basicGetUnit();
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
			case ConfigurationPackage.TRACE_CONFIGURATION__INTERVAL:
				setInterval((Long)newValue);
				return;
			case ConfigurationPackage.TRACE_CONFIGURATION__DATA_SOURCE:
				setDataSource((DataSourceConfiguration)newValue);
				return;
			case ConfigurationPackage.TRACE_CONFIGURATION__MAPPINGS:
				getMappings().clear();
				getMappings().addAll((Collection<? extends TraceToEntityMapping>)newValue);
				return;
			case ConfigurationPackage.TRACE_CONFIGURATION__UNIT:
				setUnit((Unit<?>)newValue);
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
			case ConfigurationPackage.TRACE_CONFIGURATION__INTERVAL:
				setInterval(INTERVAL_EDEFAULT);
				return;
			case ConfigurationPackage.TRACE_CONFIGURATION__DATA_SOURCE:
				setDataSource((DataSourceConfiguration)null);
				return;
			case ConfigurationPackage.TRACE_CONFIGURATION__MAPPINGS:
				getMappings().clear();
				return;
			case ConfigurationPackage.TRACE_CONFIGURATION__UNIT:
				setUnit((Unit<?>)null);
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
			case ConfigurationPackage.TRACE_CONFIGURATION__INTERVAL:
				return interval != INTERVAL_EDEFAULT;
			case ConfigurationPackage.TRACE_CONFIGURATION__DATA_SOURCE:
				return dataSource != null;
			case ConfigurationPackage.TRACE_CONFIGURATION__MAPPINGS:
				return mappings != null && !mappings.isEmpty();
			case ConfigurationPackage.TRACE_CONFIGURATION__UNIT:
				return unit != null;
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
		result.append(", interval: ");
		result.append(interval);
		result.append(')');
		return result.toString();
	}

} //TraceConfigurationImpl
