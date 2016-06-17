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

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import tools.descartes.librede.configuration.ConfigurationPackage;
import tools.descartes.librede.configuration.ExternalCall;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>External Call</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link tools.descartes.librede.configuration.impl.ExternalCallImpl#getCalledService <em>Called Service</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.ExternalCallImpl#getDelay <em>Delay</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ExternalCallImpl extends TaskImpl implements ExternalCall {
	/**
	 * The cached value of the '{@link #getCalledService() <em>Called Service</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCalledService()
	 * @generated
	 * @ordered
	 */
	protected Service calledService;

	/**
	 * The cached value of the '{@link #getDelay() <em>Delay</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDelay()
	 * @generated
	 * @ordered
	 */
	protected Quantity<Time> delay;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ExternalCallImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConfigurationPackage.Literals.EXTERNAL_CALL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Service getCalledService() {
		if (calledService != null && calledService.eIsProxy()) {
			InternalEObject oldCalledService = (InternalEObject)calledService;
			calledService = (Service)eResolveProxy(oldCalledService);
			if (calledService != oldCalledService) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ConfigurationPackage.EXTERNAL_CALL__CALLED_SERVICE, oldCalledService, calledService));
			}
		}
		return calledService;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Service basicGetCalledService() {
		return calledService;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCalledService(Service newCalledService, NotificationChain msgs) {
		Service oldCalledService = calledService;
		calledService = newCalledService;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ConfigurationPackage.EXTERNAL_CALL__CALLED_SERVICE, oldCalledService, newCalledService);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCalledService(Service newCalledService) {
		if (newCalledService != calledService) {
			NotificationChain msgs = null;
			if (calledService != null)
				msgs = ((InternalEObject)calledService).eInverseRemove(this, ConfigurationPackage.SERVICE__INCOMING_CALLS, Service.class, msgs);
			if (newCalledService != null)
				msgs = ((InternalEObject)newCalledService).eInverseAdd(this, ConfigurationPackage.SERVICE__INCOMING_CALLS, Service.class, msgs);
			msgs = basicSetCalledService(newCalledService, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.EXTERNAL_CALL__CALLED_SERVICE, newCalledService, newCalledService));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Quantity<Time> getDelay() {
		return delay;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDelay(Quantity<Time> newDelay, NotificationChain msgs) {
		Quantity<Time> oldDelay = delay;
		delay = newDelay;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ConfigurationPackage.EXTERNAL_CALL__DELAY, oldDelay, newDelay);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDelay(Quantity<Time> newDelay) {
		if (newDelay != delay) {
			NotificationChain msgs = null;
			if (delay != null)
				msgs = ((InternalEObject)delay).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ConfigurationPackage.EXTERNAL_CALL__DELAY, null, msgs);
			if (newDelay != null)
				msgs = ((InternalEObject)newDelay).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ConfigurationPackage.EXTERNAL_CALL__DELAY, null, msgs);
			msgs = basicSetDelay(newDelay, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.EXTERNAL_CALL__DELAY, newDelay, newDelay));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ConfigurationPackage.EXTERNAL_CALL__CALLED_SERVICE:
				if (calledService != null)
					msgs = ((InternalEObject)calledService).eInverseRemove(this, ConfigurationPackage.SERVICE__INCOMING_CALLS, Service.class, msgs);
				return basicSetCalledService((Service)otherEnd, msgs);
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
			case ConfigurationPackage.EXTERNAL_CALL__CALLED_SERVICE:
				return basicSetCalledService(null, msgs);
			case ConfigurationPackage.EXTERNAL_CALL__DELAY:
				return basicSetDelay(null, msgs);
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
			case ConfigurationPackage.EXTERNAL_CALL__CALLED_SERVICE:
				if (resolve) return getCalledService();
				return basicGetCalledService();
			case ConfigurationPackage.EXTERNAL_CALL__DELAY:
				return getDelay();
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
			case ConfigurationPackage.EXTERNAL_CALL__CALLED_SERVICE:
				setCalledService((Service)newValue);
				return;
			case ConfigurationPackage.EXTERNAL_CALL__DELAY:
				setDelay((Quantity<Time>)newValue);
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
			case ConfigurationPackage.EXTERNAL_CALL__CALLED_SERVICE:
				setCalledService((Service)null);
				return;
			case ConfigurationPackage.EXTERNAL_CALL__DELAY:
				setDelay((Quantity<Time>)null);
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
			case ConfigurationPackage.EXTERNAL_CALL__CALLED_SERVICE:
				return calledService != null;
			case ConfigurationPackage.EXTERNAL_CALL__DELAY:
				return delay != null;
		}
		return super.eIsSet(featureID);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getName() + "(" + getService().getName() + ")";
	}

} //ExternalCallImpl
