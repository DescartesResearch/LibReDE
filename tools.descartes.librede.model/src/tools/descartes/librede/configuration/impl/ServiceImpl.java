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
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;
import tools.descartes.librede.configuration.ConfigurationPackage;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Service</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link tools.descartes.librede.configuration.impl.ServiceImpl#isBackgroundService <em>Background Service</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.ServiceImpl#getResources <em>Resources</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.ServiceImpl#getSubServices <em>Sub Services</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.ServiceImpl#getCalledServices <em>Called Services</em>}</li>
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
	 * The cached value of the '{@link #getResources() <em>Resources</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResources()
	 * @generated
	 * @ordered
	 */
	protected EList<Resource> resources;

	/**
	 * The cached value of the '{@link #getSubServices() <em>Sub Services</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubServices()
	 * @generated
	 * @ordered
	 */
	protected EList<Service> subServices;

	/**
	 * The cached value of the '{@link #getCalledServices() <em>Called Services</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCalledServices()
	 * @generated
	 * @ordered
	 */
	protected EList<Service> calledServices;

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
	public EList<Resource> getResources() {
		if (resources == null) {
			resources = new EObjectWithInverseResolvingEList.ManyInverse<Resource>(Resource.class, this, ConfigurationPackage.SERVICE__RESOURCES, ConfigurationPackage.RESOURCE__SERVICES);
		}
		return resources;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Service> getSubServices() {
		if (subServices == null) {
			subServices = new EObjectContainmentEList<Service>(Service.class, this, ConfigurationPackage.SERVICE__SUB_SERVICES);
		}
		return subServices;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Service> getCalledServices() {
		if (calledServices == null) {
			calledServices = new EObjectResolvingEList<Service>(Service.class, this, ConfigurationPackage.SERVICE__CALLED_SERVICES);
		}
		return calledServices;
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
			case ConfigurationPackage.SERVICE__RESOURCES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getResources()).basicAdd(otherEnd, msgs);
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
			case ConfigurationPackage.SERVICE__RESOURCES:
				return ((InternalEList<?>)getResources()).basicRemove(otherEnd, msgs);
			case ConfigurationPackage.SERVICE__SUB_SERVICES:
				return ((InternalEList<?>)getSubServices()).basicRemove(otherEnd, msgs);
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
			case ConfigurationPackage.SERVICE__RESOURCES:
				return getResources();
			case ConfigurationPackage.SERVICE__SUB_SERVICES:
				return getSubServices();
			case ConfigurationPackage.SERVICE__CALLED_SERVICES:
				return getCalledServices();
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
			case ConfigurationPackage.SERVICE__RESOURCES:
				getResources().clear();
				getResources().addAll((Collection<? extends Resource>)newValue);
				return;
			case ConfigurationPackage.SERVICE__SUB_SERVICES:
				getSubServices().clear();
				getSubServices().addAll((Collection<? extends Service>)newValue);
				return;
			case ConfigurationPackage.SERVICE__CALLED_SERVICES:
				getCalledServices().clear();
				getCalledServices().addAll((Collection<? extends Service>)newValue);
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
			case ConfigurationPackage.SERVICE__RESOURCES:
				getResources().clear();
				return;
			case ConfigurationPackage.SERVICE__SUB_SERVICES:
				getSubServices().clear();
				return;
			case ConfigurationPackage.SERVICE__CALLED_SERVICES:
				getCalledServices().clear();
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
			case ConfigurationPackage.SERVICE__RESOURCES:
				return resources != null && !resources.isEmpty();
			case ConfigurationPackage.SERVICE__SUB_SERVICES:
				return subServices != null && !subServices.isEmpty();
			case ConfigurationPackage.SERVICE__CALLED_SERVICES:
				return calledServices != null && !calledServices.isEmpty();
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
		result.append(" (backgroundService: ");
		result.append(backgroundService);
		result.append(')');
		return result.toString();
	}

} //ServiceImpl
