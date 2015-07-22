/**
 */
package tools.descartes.librede.configuration.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import tools.descartes.librede.configuration.CompositeService;
import tools.descartes.librede.configuration.ConfigurationPackage;
import tools.descartes.librede.configuration.Service;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Composite Service</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link tools.descartes.librede.configuration.impl.CompositeServiceImpl#getSubServices <em>Sub Services</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CompositeServiceImpl extends ServiceImpl implements CompositeService {
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CompositeServiceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConfigurationPackage.Literals.COMPOSITE_SERVICE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Service> getSubServices() {
		if (subServices == null) {
			subServices = new EObjectContainmentEList<Service>(Service.class, this, ConfigurationPackage.COMPOSITE_SERVICE__SUB_SERVICES);
		}
		return subServices;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ConfigurationPackage.COMPOSITE_SERVICE__SUB_SERVICES:
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
			case ConfigurationPackage.COMPOSITE_SERVICE__SUB_SERVICES:
				return getSubServices();
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
			case ConfigurationPackage.COMPOSITE_SERVICE__SUB_SERVICES:
				getSubServices().clear();
				getSubServices().addAll((Collection<? extends Service>)newValue);
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
			case ConfigurationPackage.COMPOSITE_SERVICE__SUB_SERVICES:
				getSubServices().clear();
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
			case ConfigurationPackage.COMPOSITE_SERVICE__SUB_SERVICES:
				return subServices != null && !subServices.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //CompositeServiceImpl
