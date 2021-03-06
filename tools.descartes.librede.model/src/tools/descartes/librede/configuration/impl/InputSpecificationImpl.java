/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2018, by Simon Spinner, Johannes Grohmann
 *  and Contributors.
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

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import tools.descartes.librede.configuration.ConfigurationPackage;
import tools.descartes.librede.configuration.ConstantDataPoint;
import tools.descartes.librede.configuration.DataSourceConfiguration;
import tools.descartes.librede.configuration.InputSpecification;
import tools.descartes.librede.configuration.TraceConfiguration;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Input Specification</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link tools.descartes.librede.configuration.impl.InputSpecificationImpl#getDataSources <em>Data Sources</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.InputSpecificationImpl#getObservations <em>Observations</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.InputSpecificationImpl#getConstantDataPoints <em>Constant Data Points</em>}</li>
 * </ul>
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
	protected EList<DataSourceConfiguration> dataSources;

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
	 * The cached value of the '{@link #getConstantDataPoints() <em>Constant Data Points</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstantDataPoints()
	 * @generated
	 * @ordered
	 */
	protected EList<ConstantDataPoint> constantDataPoints;

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
	public EList<DataSourceConfiguration> getDataSources() {
		if (dataSources == null) {
			dataSources = new EObjectContainmentEList<DataSourceConfiguration>(DataSourceConfiguration.class, this, ConfigurationPackage.INPUT_SPECIFICATION__DATA_SOURCES);
		}
		return dataSources;
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
	public EList<ConstantDataPoint> getConstantDataPoints() {
		if (constantDataPoints == null) {
			constantDataPoints = new EObjectContainmentEList<ConstantDataPoint>(ConstantDataPoint.class, this, ConfigurationPackage.INPUT_SPECIFICATION__CONSTANT_DATA_POINTS);
		}
		return constantDataPoints;
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
			case ConfigurationPackage.INPUT_SPECIFICATION__CONSTANT_DATA_POINTS:
				return ((InternalEList<?>)getConstantDataPoints()).basicRemove(otherEnd, msgs);
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
			case ConfigurationPackage.INPUT_SPECIFICATION__CONSTANT_DATA_POINTS:
				return getConstantDataPoints();
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
				getDataSources().addAll((Collection<? extends DataSourceConfiguration>)newValue);
				return;
			case ConfigurationPackage.INPUT_SPECIFICATION__OBSERVATIONS:
				getObservations().clear();
				getObservations().addAll((Collection<? extends TraceConfiguration>)newValue);
				return;
			case ConfigurationPackage.INPUT_SPECIFICATION__CONSTANT_DATA_POINTS:
				getConstantDataPoints().clear();
				getConstantDataPoints().addAll((Collection<? extends ConstantDataPoint>)newValue);
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
			case ConfigurationPackage.INPUT_SPECIFICATION__CONSTANT_DATA_POINTS:
				getConstantDataPoints().clear();
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
			case ConfigurationPackage.INPUT_SPECIFICATION__CONSTANT_DATA_POINTS:
				return constantDataPoints != null && !constantDataPoints.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //InputSpecificationImpl
