/**
 */
package net.descartesresearch.librede.configuration;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Librede Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.descartesresearch.librede.configuration.LibredeConfiguration#getWorkloadDescription <em>Workload Description</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.LibredeConfiguration#getInput <em>Input</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.LibredeConfiguration#getEstimation <em>Estimation</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.LibredeConfiguration#getOuptut <em>Ouptut</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.LibredeConfiguration#getValidation <em>Validation</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getLibredeConfiguration()
 * @model
 * @generated
 */
public interface LibredeConfiguration extends EObject {
	/**
	 * Returns the value of the '<em><b>Workload Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Workload Description</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Workload Description</em>' containment reference.
	 * @see #setWorkloadDescription(WorkloadDescription)
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getLibredeConfiguration_WorkloadDescription()
	 * @model containment="true" required="true"
	 * @generated
	 */
	WorkloadDescription getWorkloadDescription();

	/**
	 * Sets the value of the '{@link net.descartesresearch.librede.configuration.LibredeConfiguration#getWorkloadDescription <em>Workload Description</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Workload Description</em>' containment reference.
	 * @see #getWorkloadDescription()
	 * @generated
	 */
	void setWorkloadDescription(WorkloadDescription value);

	/**
	 * Returns the value of the '<em><b>Input</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Input</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Input</em>' containment reference.
	 * @see #setInput(InputSpecification)
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getLibredeConfiguration_Input()
	 * @model containment="true" required="true"
	 * @generated
	 */
	InputSpecification getInput();

	/**
	 * Sets the value of the '{@link net.descartesresearch.librede.configuration.LibredeConfiguration#getInput <em>Input</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Input</em>' containment reference.
	 * @see #getInput()
	 * @generated
	 */
	void setInput(InputSpecification value);

	/**
	 * Returns the value of the '<em><b>Estimation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Estimation</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Estimation</em>' containment reference.
	 * @see #setEstimation(EstimationSpecification)
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getLibredeConfiguration_Estimation()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EstimationSpecification getEstimation();

	/**
	 * Sets the value of the '{@link net.descartesresearch.librede.configuration.LibredeConfiguration#getEstimation <em>Estimation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Estimation</em>' containment reference.
	 * @see #getEstimation()
	 * @generated
	 */
	void setEstimation(EstimationSpecification value);

	/**
	 * Returns the value of the '<em><b>Ouptut</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ouptut</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ouptut</em>' containment reference.
	 * @see #setOuptut(OutputSpecification)
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getLibredeConfiguration_Ouptut()
	 * @model containment="true" required="true"
	 * @generated
	 */
	OutputSpecification getOuptut();

	/**
	 * Sets the value of the '{@link net.descartesresearch.librede.configuration.LibredeConfiguration#getOuptut <em>Ouptut</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ouptut</em>' containment reference.
	 * @see #getOuptut()
	 * @generated
	 */
	void setOuptut(OutputSpecification value);

	/**
	 * Returns the value of the '<em><b>Validation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Validation</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Validation</em>' containment reference.
	 * @see #setValidation(ValidationSpecification)
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getLibredeConfiguration_Validation()
	 * @model containment="true"
	 * @generated
	 */
	ValidationSpecification getValidation();

	/**
	 * Sets the value of the '{@link net.descartesresearch.librede.configuration.LibredeConfiguration#getValidation <em>Validation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Validation</em>' containment reference.
	 * @see #getValidation()
	 * @generated
	 */
	void setValidation(ValidationSpecification value);

} // LibredeConfiguration
