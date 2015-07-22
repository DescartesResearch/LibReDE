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

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see tools.descartes.librede.configuration.ConfigurationPackage
 * @generated
 */
public interface ConfigurationFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ConfigurationFactory eINSTANCE = tools.descartes.librede.configuration.impl.ConfigurationFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Librede Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Librede Configuration</em>'.
	 * @generated
	 */
	LibredeConfiguration createLibredeConfiguration();

	/**
	 * Returns a new object of class '<em>Data Source Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Data Source Configuration</em>'.
	 * @generated
	 */
	DataSourceConfiguration createDataSourceConfiguration();

	/**
	 * Returns a new object of class '<em>Workload Description</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Workload Description</em>'.
	 * @generated
	 */
	WorkloadDescription createWorkloadDescription();

	/**
	 * Returns a new object of class '<em>Input Specification</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Input Specification</em>'.
	 * @generated
	 */
	InputSpecification createInputSpecification();

	/**
	 * Returns a new object of class '<em>Estimation Approach Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Estimation Approach Configuration</em>'.
	 * @generated
	 */
	EstimationApproachConfiguration createEstimationApproachConfiguration();

	/**
	 * Returns a new object of class '<em>Output Specification</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Output Specification</em>'.
	 * @generated
	 */
	OutputSpecification createOutputSpecification();

	/**
	 * Returns a new object of class '<em>Resource</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Resource</em>'.
	 * @generated
	 */
	Resource createResource();

	/**
	 * Returns a new object of class '<em>Service</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Service</em>'.
	 * @generated
	 */
	Service createService();

	/**
	 * Returns a new object of class '<em>Trace Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Trace Configuration</em>'.
	 * @generated
	 */
	TraceConfiguration createTraceConfiguration();

	/**
	 * Returns a new object of class '<em>Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Parameter</em>'.
	 * @generated
	 */
	Parameter createParameter();

	/**
	 * Returns a new object of class '<em>Estimation Specification</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Estimation Specification</em>'.
	 * @generated
	 */
	EstimationSpecification createEstimationSpecification();

	/**
	 * Returns a new object of class '<em>Validation Specification</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Validation Specification</em>'.
	 * @generated
	 */
	ValidationSpecification createValidationSpecification();

	/**
	 * Returns a new object of class '<em>Validator Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Validator Configuration</em>'.
	 * @generated
	 */
	ValidatorConfiguration createValidatorConfiguration();

	/**
	 * Returns a new object of class '<em>Exporter Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Exporter Configuration</em>'.
	 * @generated
	 */
	ExporterConfiguration createExporterConfiguration();

	/**
	 * Returns a new object of class '<em>File Trace Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>File Trace Configuration</em>'.
	 * @generated
	 */
	FileTraceConfiguration createFileTraceConfiguration();

	/**
	 * Returns a new object of class '<em>Trace To Entity Mapping</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Trace To Entity Mapping</em>'.
	 * @generated
	 */
	TraceToEntityMapping createTraceToEntityMapping();

	/**
	 * Returns a new object of class '<em>Model Entity</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model Entity</em>'.
	 * @generated
	 */
	ModelEntity createModelEntity();

	/**
	 * Returns a new object of class '<em>Estimation Algorithm Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Estimation Algorithm Configuration</em>'.
	 * @generated
	 */
	EstimationAlgorithmConfiguration createEstimationAlgorithmConfiguration();

	/**
	 * Returns a new object of class '<em>Trace Filter</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Trace Filter</em>'.
	 * @generated
	 */
	TraceFilter createTraceFilter();

	/**
	 * Returns a new object of class '<em>Resource Demand</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Resource Demand</em>'.
	 * @generated
	 */
	ResourceDemand createResourceDemand();

	/**
	 * Returns a new object of class '<em>External Call</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>External Call</em>'.
	 * @generated
	 */
	ExternalCall createExternalCall();

	/**
	 * Returns a new object of class '<em>Composite Service</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Composite Service</em>'.
	 * @generated
	 */
	CompositeService createCompositeService();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ConfigurationPackage getConfigurationPackage();

} //ConfigurationFactory
