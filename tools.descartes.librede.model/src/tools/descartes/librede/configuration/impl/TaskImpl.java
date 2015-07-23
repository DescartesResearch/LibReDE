/**
 */
package tools.descartes.librede.configuration.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import tools.descartes.librede.configuration.ConfigurationPackage;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.Task;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link tools.descartes.librede.configuration.impl.TaskImpl#getService <em>Service</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class TaskImpl extends ModelEntityImpl implements Task {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TaskImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConfigurationPackage.Literals.TASK;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Service getService() {
		Service service = basicGetService();
		return service != null && service.eIsProxy() ? (Service)eResolveProxy((InternalEObject)service) : service;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Service basicGetService() {
		EObject container = this.eContainer();
		if (container instanceof Task) {
			return ((Task)container).getService();
		} else if (container instanceof Service) {
			return (Service)container;
		}
		throw new IllegalStateException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ConfigurationPackage.TASK__SERVICE:
				if (resolve) return getService();
				return basicGetService();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ConfigurationPackage.TASK__SERVICE:
				return basicGetService() != null;
		}
		return super.eIsSet(featureID);
	}

} //TaskImpl
