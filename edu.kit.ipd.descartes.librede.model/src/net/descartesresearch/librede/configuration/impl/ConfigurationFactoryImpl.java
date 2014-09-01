/**
 */
package net.descartesresearch.librede.configuration.impl;

import net.descartesresearch.librede.configuration.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ConfigurationFactoryImpl extends EFactoryImpl implements ConfigurationFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ConfigurationFactory init() {
		try {
			ConfigurationFactory theConfigurationFactory = (ConfigurationFactory)EPackage.Registry.INSTANCE.getEFactory(ConfigurationPackage.eNS_URI);
			if (theConfigurationFactory != null) {
				return theConfigurationFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ConfigurationFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConfigurationFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case ConfigurationPackage.LIBREDE_CONFIGURATION: return createLibredeConfiguration();
			case ConfigurationPackage.DATA_PROVIDER_CONFIGURATION: return createDataProviderConfiguration();
			case ConfigurationPackage.WORKLOAD_DESCRIPTION: return createWorkloadDescription();
			case ConfigurationPackage.INPUT_SPECIFICATION: return createInputSpecification();
			case ConfigurationPackage.ESTIMATION_APPROACH_CONFIGURATION: return createEstimationApproachConfiguration();
			case ConfigurationPackage.OUTPUT_SPECIFICATION: return createOutputSpecification();
			case ConfigurationPackage.RESOURCE: return createResource();
			case ConfigurationPackage.SERVICE: return createService();
			case ConfigurationPackage.TRACE_CONFIGURATION: return createTraceConfiguration();
			case ConfigurationPackage.PARAMETER: return createParameter();
			case ConfigurationPackage.ESTIMATION_SPECIFICATION: return createEstimationSpecification();
			case ConfigurationPackage.VALIDATION_SPECIFICATION: return createValidationSpecification();
			case ConfigurationPackage.DATA_SOURCE_CONFIGURATION: return createDataSourceConfiguration();
			case ConfigurationPackage.VALIDATOR_CONFIGURATION: return createValidatorConfiguration();
			case ConfigurationPackage.EXPORTER_CONFIGURATION: return createExporterConfiguration();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case ConfigurationPackage.SCHEDULING_STRATEGY:
				return createSchedulingStrategyFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case ConfigurationPackage.SCHEDULING_STRATEGY:
				return convertSchedulingStrategyToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LibredeConfiguration createLibredeConfiguration() {
		LibredeConfigurationImpl libredeConfiguration = new LibredeConfigurationImpl();
		return libredeConfiguration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataProviderConfiguration createDataProviderConfiguration() {
		DataProviderConfigurationImpl dataProviderConfiguration = new DataProviderConfigurationImpl();
		return dataProviderConfiguration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataSourceConfiguration createDataSourceConfiguration() {
		DataSourceConfigurationImpl dataSourceConfiguration = new DataSourceConfigurationImpl();
		return dataSourceConfiguration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WorkloadDescription createWorkloadDescription() {
		WorkloadDescriptionImpl workloadDescription = new WorkloadDescriptionImpl();
		return workloadDescription;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InputSpecification createInputSpecification() {
		InputSpecificationImpl inputSpecification = new InputSpecificationImpl();
		return inputSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EstimationApproachConfiguration createEstimationApproachConfiguration() {
		EstimationApproachConfigurationImpl estimationApproachConfiguration = new EstimationApproachConfigurationImpl();
		return estimationApproachConfiguration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OutputSpecification createOutputSpecification() {
		OutputSpecificationImpl outputSpecification = new OutputSpecificationImpl();
		return outputSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Resource createResource() {
		ResourceImpl resource = new ResourceImpl();
		return resource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Service createService() {
		ServiceImpl service = new ServiceImpl();
		return service;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TraceConfiguration createTraceConfiguration() {
		TraceConfigurationImpl traceConfiguration = new TraceConfigurationImpl();
		return traceConfiguration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Parameter createParameter() {
		ParameterImpl parameter = new ParameterImpl();
		return parameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EstimationSpecification createEstimationSpecification() {
		EstimationSpecificationImpl estimationSpecification = new EstimationSpecificationImpl();
		return estimationSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ValidationSpecification createValidationSpecification() {
		ValidationSpecificationImpl validationSpecification = new ValidationSpecificationImpl();
		return validationSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ValidatorConfiguration createValidatorConfiguration() {
		ValidatorConfigurationImpl validatorConfiguration = new ValidatorConfigurationImpl();
		return validatorConfiguration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExporterConfiguration createExporterConfiguration() {
		ExporterConfigurationImpl exporterConfiguration = new ExporterConfigurationImpl();
		return exporterConfiguration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SchedulingStrategy createSchedulingStrategyFromString(EDataType eDataType, String initialValue) {
		SchedulingStrategy result = SchedulingStrategy.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertSchedulingStrategyToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConfigurationPackage getConfigurationPackage() {
		return (ConfigurationPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ConfigurationPackage getPackage() {
		return ConfigurationPackage.eINSTANCE;
	}

} //ConfigurationFactoryImpl
