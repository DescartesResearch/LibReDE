/**
 */
package net.descartesresearch.librede.configuration;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Estimation Approach</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.descartesresearch.librede.configuration.EstimationApproach#getClass_ <em>Class</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.EstimationApproach#getConfiguration <em>Configuration</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getEstimationApproach()
 * @model
 * @generated
 */
public interface EstimationApproach extends EObject {
	/**
	 * Returns the value of the '<em><b>Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Class</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Class</em>' attribute.
	 * @see #setClass(Class)
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getEstimationApproach_Class()
	 * @model required="true"
	 * @generated
	 */
	Class<?> getClass_();

	/**
	 * Sets the value of the '{@link net.descartesresearch.librede.configuration.EstimationApproach#getClass_ <em>Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Class</em>' attribute.
	 * @see #getClass_()
	 * @generated
	 */
	void setClass(Class<?> value);

	/**
	 * Returns the value of the '<em><b>Configuration</b></em>' containment reference list.
	 * The list contents are of type {@link net.descartesresearch.librede.configuration.Parameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Configuration</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Configuration</em>' containment reference list.
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getEstimationApproach_Configuration()
	 * @model containment="true"
	 * @generated
	 */
	EList<Parameter> getConfiguration();

} // EstimationApproach
