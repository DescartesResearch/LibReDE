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
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.emf.ecore.util.InternalEList;

import tools.descartes.librede.configuration.ConfigurationPackage;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.configuration.SchedulingStrategy;
import tools.descartes.librede.configuration.Service;

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
 *   <li>{@link tools.descartes.librede.configuration.impl.ResourceImpl#getChildResources <em>Child Resources</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.ResourceImpl#getDemands <em>Demands</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.ResourceImpl#getAccessingServices <em>Accessing Services</em>}</li>
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
	 * The cached value of the '{@link #getChildResources() <em>Child Resources</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildResources()
	 * @generated
	 * @ordered
	 */
	protected EList<Resource> childResources;

	/**
	 * The cached value of the '{@link #getDemands() <em>Demands</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDemands()
	 * @generated
	 * @ordered
	 */
	protected EList<ResourceDemand> demands;

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
	public EList<Resource> getChildResources() {
		if (childResources == null) {
			childResources = new EObjectResolvingEList<Resource>(Resource.class, this, ConfigurationPackage.RESOURCE__CHILD_RESOURCES);
		}
		return childResources;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ResourceDemand> getDemands() {
		if (demands == null) {
			demands = new EObjectWithInverseResolvingEList<ResourceDemand>(ResourceDemand.class, this, ConfigurationPackage.RESOURCE__DEMANDS, ConfigurationPackage.RESOURCE_DEMAND__RESOURCE);
		}
		return demands;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<Service> getAccessingServices() {
		List<Service> services = new LinkedList<Service>();
		for (ResourceDemand d : getDemands()) {
			 services.add(d.getService());
		}
		return new EcoreEList.UnmodifiableEList<Service>(this, ConfigurationPackage.Literals.RESOURCE__ACCESSING_SERVICES, services.size(), services.toArray());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ConfigurationPackage.RESOURCE__DEMANDS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getDemands()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ConfigurationPackage.RESOURCE__DEMANDS:
				return ((InternalEList<?>)getDemands()).basicRemove(otherEnd, msgs);
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
			case ConfigurationPackage.RESOURCE__NUMBER_OF_SERVERS:
				return getNumberOfServers();
			case ConfigurationPackage.RESOURCE__SCHEDULING_STRATEGY:
				return getSchedulingStrategy();
			case ConfigurationPackage.RESOURCE__CHILD_RESOURCES:
				return getChildResources();
			case ConfigurationPackage.RESOURCE__DEMANDS:
				return getDemands();
			case ConfigurationPackage.RESOURCE__ACCESSING_SERVICES:
				return getAccessingServices();
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
			case ConfigurationPackage.RESOURCE__NUMBER_OF_SERVERS:
				setNumberOfServers((Integer)newValue);
				return;
			case ConfigurationPackage.RESOURCE__SCHEDULING_STRATEGY:
				setSchedulingStrategy((SchedulingStrategy)newValue);
				return;
			case ConfigurationPackage.RESOURCE__CHILD_RESOURCES:
				getChildResources().clear();
				getChildResources().addAll((Collection<? extends Resource>)newValue);
				return;
			case ConfigurationPackage.RESOURCE__DEMANDS:
				getDemands().clear();
				getDemands().addAll((Collection<? extends ResourceDemand>)newValue);
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
			case ConfigurationPackage.RESOURCE__CHILD_RESOURCES:
				getChildResources().clear();
				return;
			case ConfigurationPackage.RESOURCE__DEMANDS:
				getDemands().clear();
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
			case ConfigurationPackage.RESOURCE__CHILD_RESOURCES:
				return childResources != null && !childResources.isEmpty();
			case ConfigurationPackage.RESOURCE__DEMANDS:
				return demands != null && !demands.isEmpty();
			case ConfigurationPackage.RESOURCE__ACCESSING_SERVICES:
				return !getAccessingServices().isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer();
		result.append("Resource ");
		result.append(getName());
		return result.toString();
	}

} //ResourceImpl
