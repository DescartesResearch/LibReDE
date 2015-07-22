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
package tools.descartes.librede.configuration.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import tools.descartes.librede.configuration.*;

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
			case ConfigurationPackage.DATA_SOURCE_CONFIGURATION: return createDataSourceConfiguration();
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
			case ConfigurationPackage.VALIDATOR_CONFIGURATION: return createValidatorConfiguration();
			case ConfigurationPackage.EXPORTER_CONFIGURATION: return createExporterConfiguration();
			case ConfigurationPackage.FILE_TRACE_CONFIGURATION: return createFileTraceConfiguration();
			case ConfigurationPackage.TRACE_TO_ENTITY_MAPPING: return createTraceToEntityMapping();
			case ConfigurationPackage.MODEL_ENTITY: return createModelEntity();
			case ConfigurationPackage.ESTIMATION_ALGORITHM_CONFIGURATION: return createEstimationAlgorithmConfiguration();
			case ConfigurationPackage.TRACE_FILTER: return createTraceFilter();
			case ConfigurationPackage.RESOURCE_DEMAND: return createResourceDemand();
			case ConfigurationPackage.EXTERNAL_CALL: return createExternalCall();
			case ConfigurationPackage.COMPOSITE_SERVICE: return createCompositeService();
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
	public FileTraceConfiguration createFileTraceConfiguration() {
		FileTraceConfigurationImpl fileTraceConfiguration = new FileTraceConfigurationImpl();
		return fileTraceConfiguration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TraceToEntityMapping createTraceToEntityMapping() {
		TraceToEntityMappingImpl traceToEntityMapping = new TraceToEntityMappingImpl();
		return traceToEntityMapping;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelEntity createModelEntity() {
		ModelEntityImpl modelEntity = new ModelEntityImpl();
		return modelEntity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EstimationAlgorithmConfiguration createEstimationAlgorithmConfiguration() {
		EstimationAlgorithmConfigurationImpl estimationAlgorithmConfiguration = new EstimationAlgorithmConfigurationImpl();
		return estimationAlgorithmConfiguration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TraceFilter createTraceFilter() {
		TraceFilterImpl traceFilter = new TraceFilterImpl();
		return traceFilter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceDemand createResourceDemand() {
		ResourceDemandImpl resourceDemand = new ResourceDemandImpl();
		return resourceDemand;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExternalCall createExternalCall() {
		ExternalCallImpl externalCall = new ExternalCallImpl();
		return externalCall;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CompositeService createCompositeService() {
		CompositeServiceImpl compositeService = new CompositeServiceImpl();
		return compositeService;
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
