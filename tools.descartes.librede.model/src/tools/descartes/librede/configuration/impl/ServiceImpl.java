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
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.emf.ecore.util.InternalEList;
import tools.descartes.librede.configuration.ConfigurationPackage;
import tools.descartes.librede.configuration.ExternalCall;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.Task;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Service</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link tools.descartes.librede.configuration.impl.ServiceImpl#isBackgroundService <em>Background Service</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.ServiceImpl#getTasks <em>Tasks</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.ServiceImpl#getAccessedResources <em>Accessed Resources</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.ServiceImpl#getIncomingCalls <em>Incoming Calls</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.ServiceImpl#getOutgoingCalls <em>Outgoing Calls</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.ServiceImpl#getResourceDemands <em>Resource Demands</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ServiceImpl extends ModelEntityImpl implements Service {
	/**
	 * The default value of the '{@link #isBackgroundService() <em>Background Service</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isBackgroundService()
	 * @generated
	 * @ordered
	 */
	protected static final boolean BACKGROUND_SERVICE_EDEFAULT = false;
	/**
	 * The cached value of the '{@link #isBackgroundService() <em>Background Service</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isBackgroundService()
	 * @generated
	 * @ordered
	 */
	protected boolean backgroundService = BACKGROUND_SERVICE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getTasks() <em>Tasks</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTasks()
	 * @generated
	 * @ordered
	 */
	protected EList<Task> tasks;

	/**
	 * The cached value of the '{@link #getIncomingCalls() <em>Incoming Calls</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIncomingCalls()
	 * @generated
	 * @ordered
	 */
	protected EList<ExternalCall> incomingCalls;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ServiceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConfigurationPackage.Literals.SERVICE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isBackgroundService() {
		return backgroundService;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBackgroundService(boolean newBackgroundService) {
		boolean oldBackgroundService = backgroundService;
		backgroundService = newBackgroundService;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.SERVICE__BACKGROUND_SERVICE, oldBackgroundService, backgroundService));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Task> getTasks() {
		if (tasks == null) {
			tasks = new EObjectContainmentEList<Task>(Task.class, this, ConfigurationPackage.SERVICE__TASKS);
		}
		return tasks;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<Resource> getAccessedResources() {
		List<Resource> result = new LinkedList<>();
		for (Task t : getTasks()) {
			if (t instanceof ResourceDemand) {
				result.add(((ResourceDemand)t).getResource());
			}
		}
		return new EcoreEList.UnmodifiableEList<Resource>(this, ConfigurationPackage.Literals.SERVICE__ACCESSED_RESOURCES, result.size(), result.toArray());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ExternalCall> getIncomingCalls() {
		if (incomingCalls == null) {
			incomingCalls = new EObjectWithInverseResolvingEList<ExternalCall>(ExternalCall.class, this, ConfigurationPackage.SERVICE__INCOMING_CALLS, ConfigurationPackage.EXTERNAL_CALL__CALLED_SERVICE);
		}
		return incomingCalls;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<ExternalCall> getOutgoingCalls() {
		List<ExternalCall> calls = new LinkedList<>();
		for (Task t : getTasks()) {
			if (t instanceof ExternalCall) {
				calls.add((ExternalCall)t);
			}
		}
		return new EcoreEList.UnmodifiableEList<ExternalCall>(this, ConfigurationPackage.Literals.SERVICE__OUTGOING_CALLS, calls.size(), calls.toArray());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<ResourceDemand> getResourceDemands() {
		List<ResourceDemand> demands = new LinkedList<>();
		for (Task t : getTasks()) {
			if (t instanceof ResourceDemand) {
				demands.add((ResourceDemand)t);
			}
		}
		return new EcoreEList.UnmodifiableEList<ResourceDemand>(this, ConfigurationPackage.Literals.SERVICE__RESOURCE_DEMANDS, demands.size(), demands.toArray());
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
			case ConfigurationPackage.SERVICE__INCOMING_CALLS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getIncomingCalls()).basicAdd(otherEnd, msgs);
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
			case ConfigurationPackage.SERVICE__TASKS:
				return ((InternalEList<?>)getTasks()).basicRemove(otherEnd, msgs);
			case ConfigurationPackage.SERVICE__INCOMING_CALLS:
				return ((InternalEList<?>)getIncomingCalls()).basicRemove(otherEnd, msgs);
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
			case ConfigurationPackage.SERVICE__BACKGROUND_SERVICE:
				return isBackgroundService();
			case ConfigurationPackage.SERVICE__TASKS:
				return getTasks();
			case ConfigurationPackage.SERVICE__ACCESSED_RESOURCES:
				return getAccessedResources();
			case ConfigurationPackage.SERVICE__INCOMING_CALLS:
				return getIncomingCalls();
			case ConfigurationPackage.SERVICE__OUTGOING_CALLS:
				return getOutgoingCalls();
			case ConfigurationPackage.SERVICE__RESOURCE_DEMANDS:
				return getResourceDemands();
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
			case ConfigurationPackage.SERVICE__BACKGROUND_SERVICE:
				setBackgroundService((Boolean)newValue);
				return;
			case ConfigurationPackage.SERVICE__TASKS:
				getTasks().clear();
				getTasks().addAll((Collection<? extends Task>)newValue);
				return;
			case ConfigurationPackage.SERVICE__INCOMING_CALLS:
				getIncomingCalls().clear();
				getIncomingCalls().addAll((Collection<? extends ExternalCall>)newValue);
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
			case ConfigurationPackage.SERVICE__BACKGROUND_SERVICE:
				setBackgroundService(BACKGROUND_SERVICE_EDEFAULT);
				return;
			case ConfigurationPackage.SERVICE__TASKS:
				getTasks().clear();
				return;
			case ConfigurationPackage.SERVICE__INCOMING_CALLS:
				getIncomingCalls().clear();
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
			case ConfigurationPackage.SERVICE__BACKGROUND_SERVICE:
				return backgroundService != BACKGROUND_SERVICE_EDEFAULT;
			case ConfigurationPackage.SERVICE__TASKS:
				return tasks != null && !tasks.isEmpty();
			case ConfigurationPackage.SERVICE__ACCESSED_RESOURCES:
				return !getAccessedResources().isEmpty();
			case ConfigurationPackage.SERVICE__INCOMING_CALLS:
				return incomingCalls != null && !incomingCalls.isEmpty();
			case ConfigurationPackage.SERVICE__OUTGOING_CALLS:
				return !getOutgoingCalls().isEmpty();
			case ConfigurationPackage.SERVICE__RESOURCE_DEMANDS:
				return !getResourceDemands().isEmpty();
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
		result.append("Service ");
		result.append(getName());
		return result.toString();
	}

} //ServiceImpl
