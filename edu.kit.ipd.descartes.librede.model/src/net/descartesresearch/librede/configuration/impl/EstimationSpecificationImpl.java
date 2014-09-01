/**
 */
package net.descartesresearch.librede.configuration.impl;

import java.util.Collection;
import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.EstimationApproachConfiguration;
import net.descartesresearch.librede.configuration.EstimationSpecification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Estimation Specification</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.descartesresearch.librede.configuration.impl.EstimationSpecificationImpl#getApproaches <em>Approaches</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EstimationSpecificationImpl extends MinimalEObjectImpl.Container implements EstimationSpecification {
	/**
	 * The cached value of the '{@link #getApproaches() <em>Approaches</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getApproaches()
	 * @generated
	 * @ordered
	 */
	protected EList<EstimationApproachConfiguration> approaches;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EstimationSpecificationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConfigurationPackage.Literals.ESTIMATION_SPECIFICATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EstimationApproachConfiguration> getApproaches() {
		if (approaches == null) {
			approaches = new EObjectContainmentEList<EstimationApproachConfiguration>(EstimationApproachConfiguration.class, this, ConfigurationPackage.ESTIMATION_SPECIFICATION__APPROACHES);
		}
		return approaches;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__APPROACHES:
				return ((InternalEList<?>)getApproaches()).basicRemove(otherEnd, msgs);
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
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__APPROACHES:
				return getApproaches();
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
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__APPROACHES:
				getApproaches().clear();
				getApproaches().addAll((Collection<? extends EstimationApproachConfiguration>)newValue);
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
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__APPROACHES:
				getApproaches().clear();
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
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__APPROACHES:
				return approaches != null && !approaches.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //EstimationSpecificationImpl
