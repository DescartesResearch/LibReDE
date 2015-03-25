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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import tools.descartes.librede.configuration.ConfigurationPackage;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.SchedulingStrategy;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Resource</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link tools.descartes.librede.configuration.impl.ResourceImpl#getNumberOfServers <em>Number Of Servers</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.ResourceImpl#getSchedulingStrategy <em>Scheduling Strategy</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ResourceImpl extends ModelEntityImpl implements Resource {
	/**
	 * The default value of the '{@link #getNumberOfServers() <em>Number Of Servers</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumberOfServers()
	 * @generated
	 * @ordered
	 */
	protected static final int NUMBER_OF_SERVERS_EDEFAULT = 1;

	/**
	 * The cached value of the '{@link #getNumberOfServers() <em>Number Of Servers</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumberOfServers()
	 * @generated
	 * @ordered
	 */
	protected int numberOfServers = NUMBER_OF_SERVERS_EDEFAULT;

	/**
	 * The default value of the '{@link #getSchedulingStrategy() <em>Scheduling Strategy</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSchedulingStrategy()
	 * @generated
	 * @ordered
	 */
	protected static final SchedulingStrategy SCHEDULING_STRATEGY_EDEFAULT = SchedulingStrategy.UNKOWN;

	/**
	 * The cached value of the '{@link #getSchedulingStrategy() <em>Scheduling Strategy</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSchedulingStrategy()
	 * @generated
	 * @ordered
	 */
	protected SchedulingStrategy schedulingStrategy = SCHEDULING_STRATEGY_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ResourceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConfigurationPackage.Literals.RESOURCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getNumberOfServers() {
		return numberOfServers;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNumberOfServers(int newNumberOfServers) {
		int oldNumberOfServers = numberOfServers;
		numberOfServers = newNumberOfServers;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.RESOURCE__NUMBER_OF_SERVERS, oldNumberOfServers, numberOfServers));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SchedulingStrategy getSchedulingStrategy() {
		return schedulingStrategy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSchedulingStrategy(SchedulingStrategy newSchedulingStrategy) {
		SchedulingStrategy oldSchedulingStrategy = schedulingStrategy;
		schedulingStrategy = newSchedulingStrategy == null ? SCHEDULING_STRATEGY_EDEFAULT : newSchedulingStrategy;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.RESOURCE__SCHEDULING_STRATEGY, oldSchedulingStrategy, schedulingStrategy));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ConfigurationPackage.RESOURCE__NUMBER_OF_SERVERS:
				return getNumberOfServers();
			case ConfigurationPackage.RESOURCE__SCHEDULING_STRATEGY:
				return getSchedulingStrategy();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ConfigurationPackage.RESOURCE__NUMBER_OF_SERVERS:
				setNumberOfServers((Integer)newValue);
				return;
			case ConfigurationPackage.RESOURCE__SCHEDULING_STRATEGY:
				setSchedulingStrategy((SchedulingStrategy)newValue);
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
			case ConfigurationPackage.RESOURCE__NUMBER_OF_SERVERS:
				setNumberOfServers(NUMBER_OF_SERVERS_EDEFAULT);
				return;
			case ConfigurationPackage.RESOURCE__SCHEDULING_STRATEGY:
				setSchedulingStrategy(SCHEDULING_STRATEGY_EDEFAULT);
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
			case ConfigurationPackage.RESOURCE__NUMBER_OF_SERVERS:
				return numberOfServers != NUMBER_OF_SERVERS_EDEFAULT;
			case ConfigurationPackage.RESOURCE__SCHEDULING_STRATEGY:
				return schedulingStrategy != SCHEDULING_STRATEGY_EDEFAULT;
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
		result.append(" (numberOfServers: ");
		result.append(numberOfServers);
		result.append(", schedulingStrategy: ");
		result.append(schedulingStrategy);
		result.append(')');
		return result.toString();
	}

} //ResourceImpl
