/**
 */
package net.descartesresearch.librede.configuration;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Estimation Specification</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.descartesresearch.librede.configuration.EstimationSpecification#getApproaches <em>Approaches</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getEstimationSpecification()
 * @model
 * @generated
 */
public interface EstimationSpecification extends EObject {
	/**
	 * Returns the value of the '<em><b>Approaches</b></em>' containment reference list.
	 * The list contents are of type {@link net.descartesresearch.librede.configuration.EstimationApproach}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Approaches</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Approaches</em>' containment reference list.
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getEstimationSpecification_Approaches()
	 * @model containment="true"
	 * @generated
	 */
	EList<EstimationApproach> getApproaches();

} // EstimationSpecification
