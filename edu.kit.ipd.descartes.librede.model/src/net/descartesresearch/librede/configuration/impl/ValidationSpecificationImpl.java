/**
 */
package net.descartesresearch.librede.configuration.impl;

import java.util.Collection;
import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.ValidationSpecification;

import net.descartesresearch.librede.configuration.ValidatorConfiguration;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Validation Specification</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.descartesresearch.librede.configuration.impl.ValidationSpecificationImpl#getValidators <em>Validators</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.impl.ValidationSpecificationImpl#getValidationFolds <em>Validation Folds</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.impl.ValidationSpecificationImpl#isValidateEstimates <em>Validate Estimates</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ValidationSpecificationImpl extends MinimalEObjectImpl.Container implements ValidationSpecification {
	/**
	 * The cached value of the '{@link #getValidators() <em>Validators</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValidators()
	 * @generated
	 * @ordered
	 */
	protected EList<ValidatorConfiguration> validators;

	/**
	 * The default value of the '{@link #getValidationFolds() <em>Validation Folds</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValidationFolds()
	 * @generated
	 * @ordered
	 */
	protected static final int VALIDATION_FOLDS_EDEFAULT = 1;
	/**
	 * The cached value of the '{@link #getValidationFolds() <em>Validation Folds</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValidationFolds()
	 * @generated
	 * @ordered
	 */
	protected int validationFolds = VALIDATION_FOLDS_EDEFAULT;
	/**
	 * The default value of the '{@link #isValidateEstimates() <em>Validate Estimates</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isValidateEstimates()
	 * @generated
	 * @ordered
	 */
	protected static final boolean VALIDATE_ESTIMATES_EDEFAULT = false;
	/**
	 * The cached value of the '{@link #isValidateEstimates() <em>Validate Estimates</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isValidateEstimates()
	 * @generated
	 * @ordered
	 */
	protected boolean validateEstimates = VALIDATE_ESTIMATES_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ValidationSpecificationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConfigurationPackage.Literals.VALIDATION_SPECIFICATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ValidatorConfiguration> getValidators() {
		if (validators == null) {
			validators = new EObjectContainmentEList<ValidatorConfiguration>(ValidatorConfiguration.class, this, ConfigurationPackage.VALIDATION_SPECIFICATION__VALIDATORS);
		}
		return validators;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getValidationFolds() {
		return validationFolds;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setValidationFolds(int newValidationFolds) {
		int oldValidationFolds = validationFolds;
		validationFolds = newValidationFolds;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.VALIDATION_SPECIFICATION__VALIDATION_FOLDS, oldValidationFolds, validationFolds));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isValidateEstimates() {
		return validateEstimates;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setValidateEstimates(boolean newValidateEstimates) {
		boolean oldValidateEstimates = validateEstimates;
		validateEstimates = newValidateEstimates;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.VALIDATION_SPECIFICATION__VALIDATE_ESTIMATES, oldValidateEstimates, validateEstimates));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ConfigurationPackage.VALIDATION_SPECIFICATION__VALIDATORS:
				return ((InternalEList<?>)getValidators()).basicRemove(otherEnd, msgs);
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
			case ConfigurationPackage.VALIDATION_SPECIFICATION__VALIDATORS:
				return getValidators();
			case ConfigurationPackage.VALIDATION_SPECIFICATION__VALIDATION_FOLDS:
				return getValidationFolds();
			case ConfigurationPackage.VALIDATION_SPECIFICATION__VALIDATE_ESTIMATES:
				return isValidateEstimates();
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
			case ConfigurationPackage.VALIDATION_SPECIFICATION__VALIDATORS:
				getValidators().clear();
				getValidators().addAll((Collection<? extends ValidatorConfiguration>)newValue);
				return;
			case ConfigurationPackage.VALIDATION_SPECIFICATION__VALIDATION_FOLDS:
				setValidationFolds((Integer)newValue);
				return;
			case ConfigurationPackage.VALIDATION_SPECIFICATION__VALIDATE_ESTIMATES:
				setValidateEstimates((Boolean)newValue);
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
			case ConfigurationPackage.VALIDATION_SPECIFICATION__VALIDATORS:
				getValidators().clear();
				return;
			case ConfigurationPackage.VALIDATION_SPECIFICATION__VALIDATION_FOLDS:
				setValidationFolds(VALIDATION_FOLDS_EDEFAULT);
				return;
			case ConfigurationPackage.VALIDATION_SPECIFICATION__VALIDATE_ESTIMATES:
				setValidateEstimates(VALIDATE_ESTIMATES_EDEFAULT);
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
			case ConfigurationPackage.VALIDATION_SPECIFICATION__VALIDATORS:
				return validators != null && !validators.isEmpty();
			case ConfigurationPackage.VALIDATION_SPECIFICATION__VALIDATION_FOLDS:
				return validationFolds != VALIDATION_FOLDS_EDEFAULT;
			case ConfigurationPackage.VALIDATION_SPECIFICATION__VALIDATE_ESTIMATES:
				return validateEstimates != VALIDATE_ESTIMATES_EDEFAULT;
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
		result.append(" (validationFolds: ");
		result.append(validationFolds);
		result.append(", validateEstimates: ");
		result.append(validateEstimates);
		result.append(')');
		return result.toString();
	}

} //ValidationSpecificationImpl
