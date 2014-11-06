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
package tools.descartes.librede.configuration;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Librede Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link tools.descartes.librede.configuration.LibredeConfiguration#getWorkloadDescription <em>Workload Description</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.LibredeConfiguration#getInput <em>Input</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.LibredeConfiguration#getEstimation <em>Estimation</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.LibredeConfiguration#getOutput <em>Output</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.LibredeConfiguration#getValidation <em>Validation</em>}</li>
 * </ul>
 * </p>
 *
 * @see tools.descartes.librede.configuration.ConfigurationPackage#getLibredeConfiguration()
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
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getLibredeConfiguration_WorkloadDescription()
	 * @model containment="true" required="true"
	 * @generated
	 */
	WorkloadDescription getWorkloadDescription();

	/**
	 * Sets the value of the '{@link tools.descartes.librede.configuration.LibredeConfiguration#getWorkloadDescription <em>Workload Description</em>}' containment reference.
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
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getLibredeConfiguration_Input()
	 * @model containment="true" required="true"
	 * @generated
	 */
	InputSpecification getInput();

	/**
	 * Sets the value of the '{@link tools.descartes.librede.configuration.LibredeConfiguration#getInput <em>Input</em>}' containment reference.
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
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getLibredeConfiguration_Estimation()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EstimationSpecification getEstimation();

	/**
	 * Sets the value of the '{@link tools.descartes.librede.configuration.LibredeConfiguration#getEstimation <em>Estimation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Estimation</em>' containment reference.
	 * @see #getEstimation()
	 * @generated
	 */
	void setEstimation(EstimationSpecification value);

	/**
	 * Returns the value of the '<em><b>Output</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Output</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Output</em>' containment reference.
	 * @see #setOutput(OutputSpecification)
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getLibredeConfiguration_Output()
	 * @model containment="true" required="true"
	 * @generated
	 */
	OutputSpecification getOutput();

	/**
	 * Sets the value of the '{@link tools.descartes.librede.configuration.LibredeConfiguration#getOutput <em>Output</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Output</em>' containment reference.
	 * @see #getOutput()
	 * @generated
	 */
	void setOutput(OutputSpecification value);

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
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getLibredeConfiguration_Validation()
	 * @model containment="true" required="true"
	 * @generated
	 */
	ValidationSpecification getValidation();

	/**
	 * Sets the value of the '{@link tools.descartes.librede.configuration.LibredeConfiguration#getValidation <em>Validation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Validation</em>' containment reference.
	 * @see #getValidation()
	 * @generated
	 */
	void setValidation(ValidationSpecification value);

} // LibredeConfiguration
