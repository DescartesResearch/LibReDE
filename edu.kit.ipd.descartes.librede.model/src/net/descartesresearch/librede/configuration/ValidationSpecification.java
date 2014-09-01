/**
 */
package net.descartesresearch.librede.configuration;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Validation Specification</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.descartesresearch.librede.configuration.ValidationSpecification#getValidators <em>Validators</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getValidationSpecification()
 * @model
 * @generated
 */
public interface ValidationSpecification extends EObject {

	/**
	 * Returns the value of the '<em><b>Validators</b></em>' containment reference list.
	 * The list contents are of type {@link net.descartesresearch.librede.configuration.ValidatorConfiguration}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Validators</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Validators</em>' containment reference list.
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getValidationSpecification_Validators()
	 * @model containment="true"
	 * @generated
	 */
	EList<ValidatorConfiguration> getValidators();
} // ValidationSpecification
