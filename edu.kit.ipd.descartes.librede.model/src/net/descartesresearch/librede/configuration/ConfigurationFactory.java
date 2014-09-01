/**
 */
package net.descartesresearch.librede.configuration;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see net.descartesresearch.librede.configuration.ConfigurationPackage
 * @generated
 */
public interface ConfigurationFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ConfigurationFactory eINSTANCE = net.descartesresearch.librede.configuration.impl.ConfigurationFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Librede Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Librede Configuration</em>'.
	 * @generated
	 */
	LibredeConfiguration createLibredeConfiguration();

	/**
	 * Returns a new object of class '<em>Data Provider Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Data Provider Configuration</em>'.
	 * @generated
	 */
	DataProviderConfiguration createDataProviderConfiguration();

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
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ConfigurationPackage getConfigurationPackage();

} //ConfigurationFactory
