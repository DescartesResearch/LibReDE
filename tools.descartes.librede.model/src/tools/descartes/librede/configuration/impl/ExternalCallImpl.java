/**
 */
package tools.descartes.librede.configuration.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import tools.descartes.librede.configuration.ConfigurationPackage;
import tools.descartes.librede.configuration.ExternalCall;
import tools.descartes.librede.configuration.Service;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>External Call</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link tools.descartes.librede.configuration.impl.ExternalCallImpl#getCalledService <em>Called Service</em>}</li>
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
	public void setCalledService(Service newCalledService) {
		Service oldCalledService = calledService;
		calledService = newCalledService;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.EXTERNAL_CALL__CALLED_SERVICE, oldCalledService, calledService));
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
			case ConfigurationPackage.EXTERNAL_CALL__CALLED_SERVICE:
				setCalledService((Service)newValue);
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
		}
		return super.eIsSet(featureID);
	}

} //ExternalCallImpl
