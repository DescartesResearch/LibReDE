/**
 */
package net.descartesresearch.librede.configuration;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see net.descartesresearch.librede.configuration.ConfigurationFactory
 * @model kind="package"
 * @generated
 */
public interface ConfigurationPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "configuration";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.descartes-research.net/librede/configuration/1.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "librede";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ConfigurationPackage eINSTANCE = net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl.init();

	/**
	 * The meta object id for the '{@link net.descartesresearch.librede.configuration.impl.LibredeConfigurationImpl <em>Librede Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.descartesresearch.librede.configuration.impl.LibredeConfigurationImpl
	 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getLibredeConfiguration()
	 * @generated
	 */
	int LIBREDE_CONFIGURATION = 0;

	/**
	 * The feature id for the '<em><b>Workload Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIBREDE_CONFIGURATION__WORKLOAD_DESCRIPTION = 0;

	/**
	 * The feature id for the '<em><b>Input</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIBREDE_CONFIGURATION__INPUT = 1;

	/**
	 * The feature id for the '<em><b>Estimation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIBREDE_CONFIGURATION__ESTIMATION = 2;

	/**
	 * The feature id for the '<em><b>Output</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIBREDE_CONFIGURATION__OUTPUT = 3;

	/**
	 * The feature id for the '<em><b>Validation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIBREDE_CONFIGURATION__VALIDATION = 4;

	/**
	 * The number of structural features of the '<em>Librede Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIBREDE_CONFIGURATION_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Librede Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIBREDE_CONFIGURATION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link net.descartesresearch.librede.configuration.impl.DataSourceConfigurationImpl <em>Data Source Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.descartesresearch.librede.configuration.impl.DataSourceConfigurationImpl
	 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getDataSourceConfiguration()
	 * @generated
	 */
	int DATA_SOURCE_CONFIGURATION = 1;

	/**
	 * The meta object id for the '{@link net.descartesresearch.librede.configuration.impl.WorkloadDescriptionImpl <em>Workload Description</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.descartesresearch.librede.configuration.impl.WorkloadDescriptionImpl
	 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getWorkloadDescription()
	 * @generated
	 */
	int WORKLOAD_DESCRIPTION = 2;

	/**
	 * The meta object id for the '{@link net.descartesresearch.librede.configuration.impl.InputSpecificationImpl <em>Input Specification</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.descartesresearch.librede.configuration.impl.InputSpecificationImpl
	 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getInputSpecification()
	 * @generated
	 */
	int INPUT_SPECIFICATION = 3;

	/**
	 * The meta object id for the '{@link net.descartesresearch.librede.configuration.impl.EstimationApproachConfigurationImpl <em>Estimation Approach Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.descartesresearch.librede.configuration.impl.EstimationApproachConfigurationImpl
	 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getEstimationApproachConfiguration()
	 * @generated
	 */
	int ESTIMATION_APPROACH_CONFIGURATION = 4;

	/**
	 * The meta object id for the '{@link net.descartesresearch.librede.configuration.impl.OutputSpecificationImpl <em>Output Specification</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.descartesresearch.librede.configuration.impl.OutputSpecificationImpl
	 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getOutputSpecification()
	 * @generated
	 */
	int OUTPUT_SPECIFICATION = 5;

	/**
	 * The meta object id for the '{@link net.descartesresearch.librede.configuration.impl.ResourceImpl <em>Resource</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.descartesresearch.librede.configuration.impl.ResourceImpl
	 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getResource()
	 * @generated
	 */
	int RESOURCE = 6;

	/**
	 * The meta object id for the '{@link net.descartesresearch.librede.configuration.impl.ServiceImpl <em>Service</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.descartesresearch.librede.configuration.impl.ServiceImpl
	 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getService()
	 * @generated
	 */
	int SERVICE = 7;

	/**
	 * The meta object id for the '{@link net.descartesresearch.librede.configuration.impl.TraceConfigurationImpl <em>Trace Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.descartesresearch.librede.configuration.impl.TraceConfigurationImpl
	 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getTraceConfiguration()
	 * @generated
	 */
	int TRACE_CONFIGURATION = 8;

	/**
	 * The meta object id for the '{@link net.descartesresearch.librede.configuration.impl.ParameterImpl <em>Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.descartesresearch.librede.configuration.impl.ParameterImpl
	 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getParameter()
	 * @generated
	 */
	int PARAMETER = 9;

	/**
	 * The meta object id for the '{@link net.descartesresearch.librede.configuration.impl.EstimationSpecificationImpl <em>Estimation Specification</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.descartesresearch.librede.configuration.impl.EstimationSpecificationImpl
	 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getEstimationSpecification()
	 * @generated
	 */
	int ESTIMATION_SPECIFICATION = 10;

	/**
	 * The meta object id for the '{@link net.descartesresearch.librede.configuration.impl.ValidationSpecificationImpl <em>Validation Specification</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.descartesresearch.librede.configuration.impl.ValidationSpecificationImpl
	 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getValidationSpecification()
	 * @generated
	 */
	int VALIDATION_SPECIFICATION = 11;

	/**
	 * The meta object id for the '{@link net.descartesresearch.librede.configuration.impl.ValidatorConfigurationImpl <em>Validator Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.descartesresearch.librede.configuration.impl.ValidatorConfigurationImpl
	 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getValidatorConfiguration()
	 * @generated
	 */
	int VALIDATOR_CONFIGURATION = 12;

	/**
	 * The meta object id for the '{@link net.descartesresearch.librede.configuration.impl.ExporterConfigurationImpl <em>Exporter Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.descartesresearch.librede.configuration.impl.ExporterConfigurationImpl
	 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getExporterConfiguration()
	 * @generated
	 */
	int EXPORTER_CONFIGURATION = 13;

	/**
	 * The meta object id for the '{@link net.descartesresearch.librede.configuration.impl.FileTraceConfigurationImpl <em>File Trace Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.descartesresearch.librede.configuration.impl.FileTraceConfigurationImpl
	 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getFileTraceConfiguration()
	 * @generated
	 */
	int FILE_TRACE_CONFIGURATION = 14;

	/**
	 * The meta object id for the '{@link net.descartesresearch.librede.configuration.NamedElement <em>Named Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.descartesresearch.librede.configuration.NamedElement
	 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getNamedElement()
	 * @generated
	 */
	int NAMED_ELEMENT = 15;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT__NAME = 0;

	/**
	 * The number of structural features of the '<em>Named Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Named Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT_OPERATION_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SOURCE_CONFIGURATION__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SOURCE_CONFIGURATION__TYPE = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SOURCE_CONFIGURATION__PARAMETERS = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Data Source Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SOURCE_CONFIGURATION_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Data Source Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SOURCE_CONFIGURATION_OPERATION_COUNT = NAMED_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Resources</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WORKLOAD_DESCRIPTION__RESOURCES = 0;

	/**
	 * The feature id for the '<em><b>Services</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WORKLOAD_DESCRIPTION__SERVICES = 1;

	/**
	 * The number of structural features of the '<em>Workload Description</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WORKLOAD_DESCRIPTION_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Workload Description</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WORKLOAD_DESCRIPTION_OPERATION_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Data Sources</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_SPECIFICATION__DATA_SOURCES = 0;

	/**
	 * The feature id for the '<em><b>Observations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_SPECIFICATION__OBSERVATIONS = 1;

	/**
	 * The number of structural features of the '<em>Input Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_SPECIFICATION_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Input Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_SPECIFICATION_OPERATION_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_APPROACH_CONFIGURATION__TYPE = 0;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_APPROACH_CONFIGURATION__PARAMETERS = 1;

	/**
	 * The number of structural features of the '<em>Estimation Approach Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_APPROACH_CONFIGURATION_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Estimation Approach Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_APPROACH_CONFIGURATION_OPERATION_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Exporters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_SPECIFICATION__EXPORTERS = 0;

	/**
	 * The number of structural features of the '<em>Output Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_SPECIFICATION_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Output Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_SPECIFICATION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link net.descartesresearch.librede.configuration.impl.ModelEntityImpl <em>Model Entity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.descartesresearch.librede.configuration.impl.ModelEntityImpl
	 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getModelEntity()
	 * @generated
	 */
	int MODEL_ENTITY = 17;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ENTITY__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The number of structural features of the '<em>Model Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ENTITY_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Model Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ENTITY_OPERATION_COUNT = NAMED_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE__NAME = MODEL_ENTITY__NAME;

	/**
	 * The feature id for the '<em><b>Number Of Servers</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE__NUMBER_OF_SERVERS = MODEL_ENTITY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Scheduling Strategy</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE__SCHEDULING_STRATEGY = MODEL_ENTITY_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Resource</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_FEATURE_COUNT = MODEL_ENTITY_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Resource</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_OPERATION_COUNT = MODEL_ENTITY_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE__NAME = MODEL_ENTITY__NAME;

	/**
	 * The number of structural features of the '<em>Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_FEATURE_COUNT = MODEL_ENTITY_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_OPERATION_COUNT = MODEL_ENTITY_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Metric</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_CONFIGURATION__METRIC = 0;

	/**
	 * The feature id for the '<em><b>Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_CONFIGURATION__UNIT = 1;

	/**
	 * The feature id for the '<em><b>Interval</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_CONFIGURATION__INTERVAL = 2;

	/**
	 * The feature id for the '<em><b>Provider</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_CONFIGURATION__PROVIDER = 3;

	/**
	 * The feature id for the '<em><b>Mappings</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_CONFIGURATION__MAPPINGS = 4;

	/**
	 * The number of structural features of the '<em>Trace Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_CONFIGURATION_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Trace Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_CONFIGURATION_OPERATION_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__NAME = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_OPERATION_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Approaches</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_SPECIFICATION__APPROACHES = 0;

	/**
	 * The feature id for the '<em><b>Recursive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_SPECIFICATION__RECURSIVE = 1;

	/**
	 * The feature id for the '<em><b>Window</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_SPECIFICATION__WINDOW = 2;

	/**
	 * The feature id for the '<em><b>Step Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_SPECIFICATION__STEP_SIZE = 3;

	/**
	 * The feature id for the '<em><b>Start Timestamp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_SPECIFICATION__START_TIMESTAMP = 4;

	/**
	 * The feature id for the '<em><b>End Timestamp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_SPECIFICATION__END_TIMESTAMP = 5;

	/**
	 * The number of structural features of the '<em>Estimation Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_SPECIFICATION_FEATURE_COUNT = 6;

	/**
	 * The number of operations of the '<em>Estimation Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_SPECIFICATION_OPERATION_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Validators</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALIDATION_SPECIFICATION__VALIDATORS = 0;

	/**
	 * The feature id for the '<em><b>Validation Folds</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALIDATION_SPECIFICATION__VALIDATION_FOLDS = 1;

	/**
	 * The feature id for the '<em><b>Validate Estimates</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALIDATION_SPECIFICATION__VALIDATE_ESTIMATES = 2;

	/**
	 * The number of structural features of the '<em>Validation Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALIDATION_SPECIFICATION_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Validation Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALIDATION_SPECIFICATION_OPERATION_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALIDATOR_CONFIGURATION__TYPE = 0;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALIDATOR_CONFIGURATION__PARAMETERS = 1;

	/**
	 * The number of structural features of the '<em>Validator Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALIDATOR_CONFIGURATION_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Validator Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALIDATOR_CONFIGURATION_OPERATION_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPORTER_CONFIGURATION__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPORTER_CONFIGURATION__TYPE = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPORTER_CONFIGURATION__PARAMETERS = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Exporter Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPORTER_CONFIGURATION_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Exporter Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPORTER_CONFIGURATION_OPERATION_COUNT = NAMED_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Metric</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_TRACE_CONFIGURATION__METRIC = TRACE_CONFIGURATION__METRIC;

	/**
	 * The feature id for the '<em><b>Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_TRACE_CONFIGURATION__UNIT = TRACE_CONFIGURATION__UNIT;

	/**
	 * The feature id for the '<em><b>Interval</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_TRACE_CONFIGURATION__INTERVAL = TRACE_CONFIGURATION__INTERVAL;

	/**
	 * The feature id for the '<em><b>Provider</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_TRACE_CONFIGURATION__PROVIDER = TRACE_CONFIGURATION__PROVIDER;

	/**
	 * The feature id for the '<em><b>Mappings</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_TRACE_CONFIGURATION__MAPPINGS = TRACE_CONFIGURATION__MAPPINGS;

	/**
	 * The feature id for the '<em><b>File</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_TRACE_CONFIGURATION__FILE = TRACE_CONFIGURATION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>File Trace Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_TRACE_CONFIGURATION_FEATURE_COUNT = TRACE_CONFIGURATION_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>File Trace Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_TRACE_CONFIGURATION_OPERATION_COUNT = TRACE_CONFIGURATION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link net.descartesresearch.librede.configuration.impl.TraceToEntityMappingImpl <em>Trace To Entity Mapping</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.descartesresearch.librede.configuration.impl.TraceToEntityMappingImpl
	 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getTraceToEntityMapping()
	 * @generated
	 */
	int TRACE_TO_ENTITY_MAPPING = 16;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_TO_ENTITY_MAPPING__ENTITY = 0;

	/**
	 * The feature id for the '<em><b>Trace Column</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_TO_ENTITY_MAPPING__TRACE_COLUMN = 1;

	/**
	 * The number of structural features of the '<em>Trace To Entity Mapping</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_TO_ENTITY_MAPPING_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Trace To Entity Mapping</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_TO_ENTITY_MAPPING_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link net.descartesresearch.librede.configuration.SchedulingStrategy <em>Scheduling Strategy</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.descartesresearch.librede.configuration.SchedulingStrategy
	 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getSchedulingStrategy()
	 * @generated
	 */
	int SCHEDULING_STRATEGY = 18;


	/**
	 * Returns the meta object for class '{@link net.descartesresearch.librede.configuration.LibredeConfiguration <em>Librede Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Librede Configuration</em>'.
	 * @see net.descartesresearch.librede.configuration.LibredeConfiguration
	 * @generated
	 */
	EClass getLibredeConfiguration();

	/**
	 * Returns the meta object for the containment reference '{@link net.descartesresearch.librede.configuration.LibredeConfiguration#getWorkloadDescription <em>Workload Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Workload Description</em>'.
	 * @see net.descartesresearch.librede.configuration.LibredeConfiguration#getWorkloadDescription()
	 * @see #getLibredeConfiguration()
	 * @generated
	 */
	EReference getLibredeConfiguration_WorkloadDescription();

	/**
	 * Returns the meta object for the containment reference '{@link net.descartesresearch.librede.configuration.LibredeConfiguration#getInput <em>Input</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Input</em>'.
	 * @see net.descartesresearch.librede.configuration.LibredeConfiguration#getInput()
	 * @see #getLibredeConfiguration()
	 * @generated
	 */
	EReference getLibredeConfiguration_Input();

	/**
	 * Returns the meta object for the containment reference '{@link net.descartesresearch.librede.configuration.LibredeConfiguration#getEstimation <em>Estimation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Estimation</em>'.
	 * @see net.descartesresearch.librede.configuration.LibredeConfiguration#getEstimation()
	 * @see #getLibredeConfiguration()
	 * @generated
	 */
	EReference getLibredeConfiguration_Estimation();

	/**
	 * Returns the meta object for the containment reference '{@link net.descartesresearch.librede.configuration.LibredeConfiguration#getOutput <em>Output</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Output</em>'.
	 * @see net.descartesresearch.librede.configuration.LibredeConfiguration#getOutput()
	 * @see #getLibredeConfiguration()
	 * @generated
	 */
	EReference getLibredeConfiguration_Output();

	/**
	 * Returns the meta object for the containment reference '{@link net.descartesresearch.librede.configuration.LibredeConfiguration#getValidation <em>Validation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Validation</em>'.
	 * @see net.descartesresearch.librede.configuration.LibredeConfiguration#getValidation()
	 * @see #getLibredeConfiguration()
	 * @generated
	 */
	EReference getLibredeConfiguration_Validation();

	/**
	 * Returns the meta object for class '{@link net.descartesresearch.librede.configuration.DataSourceConfiguration <em>Data Source Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Source Configuration</em>'.
	 * @see net.descartesresearch.librede.configuration.DataSourceConfiguration
	 * @generated
	 */
	EClass getDataSourceConfiguration();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.DataSourceConfiguration#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see net.descartesresearch.librede.configuration.DataSourceConfiguration#getType()
	 * @see #getDataSourceConfiguration()
	 * @generated
	 */
	EAttribute getDataSourceConfiguration_Type();

	/**
	 * Returns the meta object for the containment reference list '{@link net.descartesresearch.librede.configuration.DataSourceConfiguration#getParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameters</em>'.
	 * @see net.descartesresearch.librede.configuration.DataSourceConfiguration#getParameters()
	 * @see #getDataSourceConfiguration()
	 * @generated
	 */
	EReference getDataSourceConfiguration_Parameters();

	/**
	 * Returns the meta object for class '{@link net.descartesresearch.librede.configuration.WorkloadDescription <em>Workload Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Workload Description</em>'.
	 * @see net.descartesresearch.librede.configuration.WorkloadDescription
	 * @generated
	 */
	EClass getWorkloadDescription();

	/**
	 * Returns the meta object for the containment reference list '{@link net.descartesresearch.librede.configuration.WorkloadDescription#getResources <em>Resources</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Resources</em>'.
	 * @see net.descartesresearch.librede.configuration.WorkloadDescription#getResources()
	 * @see #getWorkloadDescription()
	 * @generated
	 */
	EReference getWorkloadDescription_Resources();

	/**
	 * Returns the meta object for the containment reference list '{@link net.descartesresearch.librede.configuration.WorkloadDescription#getServices <em>Services</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Services</em>'.
	 * @see net.descartesresearch.librede.configuration.WorkloadDescription#getServices()
	 * @see #getWorkloadDescription()
	 * @generated
	 */
	EReference getWorkloadDescription_Services();

	/**
	 * Returns the meta object for class '{@link net.descartesresearch.librede.configuration.InputSpecification <em>Input Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Input Specification</em>'.
	 * @see net.descartesresearch.librede.configuration.InputSpecification
	 * @generated
	 */
	EClass getInputSpecification();

	/**
	 * Returns the meta object for the containment reference list '{@link net.descartesresearch.librede.configuration.InputSpecification#getDataSources <em>Data Sources</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Data Sources</em>'.
	 * @see net.descartesresearch.librede.configuration.InputSpecification#getDataSources()
	 * @see #getInputSpecification()
	 * @generated
	 */
	EReference getInputSpecification_DataSources();

	/**
	 * Returns the meta object for the containment reference list '{@link net.descartesresearch.librede.configuration.InputSpecification#getObservations <em>Observations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Observations</em>'.
	 * @see net.descartesresearch.librede.configuration.InputSpecification#getObservations()
	 * @see #getInputSpecification()
	 * @generated
	 */
	EReference getInputSpecification_Observations();

	/**
	 * Returns the meta object for class '{@link net.descartesresearch.librede.configuration.EstimationApproachConfiguration <em>Estimation Approach Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Estimation Approach Configuration</em>'.
	 * @see net.descartesresearch.librede.configuration.EstimationApproachConfiguration
	 * @generated
	 */
	EClass getEstimationApproachConfiguration();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.EstimationApproachConfiguration#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see net.descartesresearch.librede.configuration.EstimationApproachConfiguration#getType()
	 * @see #getEstimationApproachConfiguration()
	 * @generated
	 */
	EAttribute getEstimationApproachConfiguration_Type();

	/**
	 * Returns the meta object for the containment reference list '{@link net.descartesresearch.librede.configuration.EstimationApproachConfiguration#getParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameters</em>'.
	 * @see net.descartesresearch.librede.configuration.EstimationApproachConfiguration#getParameters()
	 * @see #getEstimationApproachConfiguration()
	 * @generated
	 */
	EReference getEstimationApproachConfiguration_Parameters();

	/**
	 * Returns the meta object for class '{@link net.descartesresearch.librede.configuration.OutputSpecification <em>Output Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Output Specification</em>'.
	 * @see net.descartesresearch.librede.configuration.OutputSpecification
	 * @generated
	 */
	EClass getOutputSpecification();

	/**
	 * Returns the meta object for the containment reference list '{@link net.descartesresearch.librede.configuration.OutputSpecification#getExporters <em>Exporters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Exporters</em>'.
	 * @see net.descartesresearch.librede.configuration.OutputSpecification#getExporters()
	 * @see #getOutputSpecification()
	 * @generated
	 */
	EReference getOutputSpecification_Exporters();

	/**
	 * Returns the meta object for class '{@link net.descartesresearch.librede.configuration.Resource <em>Resource</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Resource</em>'.
	 * @see net.descartesresearch.librede.configuration.Resource
	 * @generated
	 */
	EClass getResource();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.Resource#getNumberOfServers <em>Number Of Servers</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Number Of Servers</em>'.
	 * @see net.descartesresearch.librede.configuration.Resource#getNumberOfServers()
	 * @see #getResource()
	 * @generated
	 */
	EAttribute getResource_NumberOfServers();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.Resource#getSchedulingStrategy <em>Scheduling Strategy</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Scheduling Strategy</em>'.
	 * @see net.descartesresearch.librede.configuration.Resource#getSchedulingStrategy()
	 * @see #getResource()
	 * @generated
	 */
	EAttribute getResource_SchedulingStrategy();

	/**
	 * Returns the meta object for class '{@link net.descartesresearch.librede.configuration.Service <em>Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Service</em>'.
	 * @see net.descartesresearch.librede.configuration.Service
	 * @generated
	 */
	EClass getService();

	/**
	 * Returns the meta object for class '{@link net.descartesresearch.librede.configuration.TraceConfiguration <em>Trace Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Trace Configuration</em>'.
	 * @see net.descartesresearch.librede.configuration.TraceConfiguration
	 * @generated
	 */
	EClass getTraceConfiguration();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.TraceConfiguration#getMetric <em>Metric</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Metric</em>'.
	 * @see net.descartesresearch.librede.configuration.TraceConfiguration#getMetric()
	 * @see #getTraceConfiguration()
	 * @generated
	 */
	EAttribute getTraceConfiguration_Metric();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.TraceConfiguration#getUnit <em>Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Unit</em>'.
	 * @see net.descartesresearch.librede.configuration.TraceConfiguration#getUnit()
	 * @see #getTraceConfiguration()
	 * @generated
	 */
	EAttribute getTraceConfiguration_Unit();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.TraceConfiguration#getInterval <em>Interval</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Interval</em>'.
	 * @see net.descartesresearch.librede.configuration.TraceConfiguration#getInterval()
	 * @see #getTraceConfiguration()
	 * @generated
	 */
	EAttribute getTraceConfiguration_Interval();

	/**
	 * Returns the meta object for the reference '{@link net.descartesresearch.librede.configuration.TraceConfiguration#getProvider <em>Provider</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Provider</em>'.
	 * @see net.descartesresearch.librede.configuration.TraceConfiguration#getProvider()
	 * @see #getTraceConfiguration()
	 * @generated
	 */
	EReference getTraceConfiguration_Provider();

	/**
	 * Returns the meta object for the containment reference list '{@link net.descartesresearch.librede.configuration.TraceConfiguration#getMappings <em>Mappings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Mappings</em>'.
	 * @see net.descartesresearch.librede.configuration.TraceConfiguration#getMappings()
	 * @see #getTraceConfiguration()
	 * @generated
	 */
	EReference getTraceConfiguration_Mappings();

	/**
	 * Returns the meta object for class '{@link net.descartesresearch.librede.configuration.Parameter <em>Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parameter</em>'.
	 * @see net.descartesresearch.librede.configuration.Parameter
	 * @generated
	 */
	EClass getParameter();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.Parameter#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see net.descartesresearch.librede.configuration.Parameter#getName()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_Name();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.Parameter#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see net.descartesresearch.librede.configuration.Parameter#getValue()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_Value();

	/**
	 * Returns the meta object for class '{@link net.descartesresearch.librede.configuration.EstimationSpecification <em>Estimation Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Estimation Specification</em>'.
	 * @see net.descartesresearch.librede.configuration.EstimationSpecification
	 * @generated
	 */
	EClass getEstimationSpecification();

	/**
	 * Returns the meta object for the containment reference list '{@link net.descartesresearch.librede.configuration.EstimationSpecification#getApproaches <em>Approaches</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Approaches</em>'.
	 * @see net.descartesresearch.librede.configuration.EstimationSpecification#getApproaches()
	 * @see #getEstimationSpecification()
	 * @generated
	 */
	EReference getEstimationSpecification_Approaches();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.EstimationSpecification#isRecursive <em>Recursive</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Recursive</em>'.
	 * @see net.descartesresearch.librede.configuration.EstimationSpecification#isRecursive()
	 * @see #getEstimationSpecification()
	 * @generated
	 */
	EAttribute getEstimationSpecification_Recursive();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.EstimationSpecification#getWindow <em>Window</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Window</em>'.
	 * @see net.descartesresearch.librede.configuration.EstimationSpecification#getWindow()
	 * @see #getEstimationSpecification()
	 * @generated
	 */
	EAttribute getEstimationSpecification_Window();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.EstimationSpecification#getStepSize <em>Step Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Step Size</em>'.
	 * @see net.descartesresearch.librede.configuration.EstimationSpecification#getStepSize()
	 * @see #getEstimationSpecification()
	 * @generated
	 */
	EAttribute getEstimationSpecification_StepSize();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.EstimationSpecification#getStartTimestamp <em>Start Timestamp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Timestamp</em>'.
	 * @see net.descartesresearch.librede.configuration.EstimationSpecification#getStartTimestamp()
	 * @see #getEstimationSpecification()
	 * @generated
	 */
	EAttribute getEstimationSpecification_StartTimestamp();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.EstimationSpecification#getEndTimestamp <em>End Timestamp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End Timestamp</em>'.
	 * @see net.descartesresearch.librede.configuration.EstimationSpecification#getEndTimestamp()
	 * @see #getEstimationSpecification()
	 * @generated
	 */
	EAttribute getEstimationSpecification_EndTimestamp();

	/**
	 * Returns the meta object for class '{@link net.descartesresearch.librede.configuration.ValidationSpecification <em>Validation Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Validation Specification</em>'.
	 * @see net.descartesresearch.librede.configuration.ValidationSpecification
	 * @generated
	 */
	EClass getValidationSpecification();

	/**
	 * Returns the meta object for the containment reference list '{@link net.descartesresearch.librede.configuration.ValidationSpecification#getValidators <em>Validators</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Validators</em>'.
	 * @see net.descartesresearch.librede.configuration.ValidationSpecification#getValidators()
	 * @see #getValidationSpecification()
	 * @generated
	 */
	EReference getValidationSpecification_Validators();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.ValidationSpecification#getValidationFolds <em>Validation Folds</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Validation Folds</em>'.
	 * @see net.descartesresearch.librede.configuration.ValidationSpecification#getValidationFolds()
	 * @see #getValidationSpecification()
	 * @generated
	 */
	EAttribute getValidationSpecification_ValidationFolds();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.ValidationSpecification#isValidateEstimates <em>Validate Estimates</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Validate Estimates</em>'.
	 * @see net.descartesresearch.librede.configuration.ValidationSpecification#isValidateEstimates()
	 * @see #getValidationSpecification()
	 * @generated
	 */
	EAttribute getValidationSpecification_ValidateEstimates();

	/**
	 * Returns the meta object for class '{@link net.descartesresearch.librede.configuration.ValidatorConfiguration <em>Validator Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Validator Configuration</em>'.
	 * @see net.descartesresearch.librede.configuration.ValidatorConfiguration
	 * @generated
	 */
	EClass getValidatorConfiguration();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.ValidatorConfiguration#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see net.descartesresearch.librede.configuration.ValidatorConfiguration#getType()
	 * @see #getValidatorConfiguration()
	 * @generated
	 */
	EAttribute getValidatorConfiguration_Type();

	/**
	 * Returns the meta object for the containment reference list '{@link net.descartesresearch.librede.configuration.ValidatorConfiguration#getParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameters</em>'.
	 * @see net.descartesresearch.librede.configuration.ValidatorConfiguration#getParameters()
	 * @see #getValidatorConfiguration()
	 * @generated
	 */
	EReference getValidatorConfiguration_Parameters();

	/**
	 * Returns the meta object for class '{@link net.descartesresearch.librede.configuration.ExporterConfiguration <em>Exporter Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Exporter Configuration</em>'.
	 * @see net.descartesresearch.librede.configuration.ExporterConfiguration
	 * @generated
	 */
	EClass getExporterConfiguration();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.ExporterConfiguration#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see net.descartesresearch.librede.configuration.ExporterConfiguration#getType()
	 * @see #getExporterConfiguration()
	 * @generated
	 */
	EAttribute getExporterConfiguration_Type();

	/**
	 * Returns the meta object for the containment reference list '{@link net.descartesresearch.librede.configuration.ExporterConfiguration#getParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameters</em>'.
	 * @see net.descartesresearch.librede.configuration.ExporterConfiguration#getParameters()
	 * @see #getExporterConfiguration()
	 * @generated
	 */
	EReference getExporterConfiguration_Parameters();

	/**
	 * Returns the meta object for class '{@link net.descartesresearch.librede.configuration.FileTraceConfiguration <em>File Trace Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>File Trace Configuration</em>'.
	 * @see net.descartesresearch.librede.configuration.FileTraceConfiguration
	 * @generated
	 */
	EClass getFileTraceConfiguration();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.FileTraceConfiguration#getFile <em>File</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>File</em>'.
	 * @see net.descartesresearch.librede.configuration.FileTraceConfiguration#getFile()
	 * @see #getFileTraceConfiguration()
	 * @generated
	 */
	EAttribute getFileTraceConfiguration_File();

	/**
	 * Returns the meta object for class '{@link net.descartesresearch.librede.configuration.NamedElement <em>Named Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Named Element</em>'.
	 * @see net.descartesresearch.librede.configuration.NamedElement
	 * @generated
	 */
	EClass getNamedElement();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.NamedElement#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see net.descartesresearch.librede.configuration.NamedElement#getName()
	 * @see #getNamedElement()
	 * @generated
	 */
	EAttribute getNamedElement_Name();

	/**
	 * Returns the meta object for class '{@link net.descartesresearch.librede.configuration.TraceToEntityMapping <em>Trace To Entity Mapping</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Trace To Entity Mapping</em>'.
	 * @see net.descartesresearch.librede.configuration.TraceToEntityMapping
	 * @generated
	 */
	EClass getTraceToEntityMapping();

	/**
	 * Returns the meta object for the reference '{@link net.descartesresearch.librede.configuration.TraceToEntityMapping#getEntity <em>Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Entity</em>'.
	 * @see net.descartesresearch.librede.configuration.TraceToEntityMapping#getEntity()
	 * @see #getTraceToEntityMapping()
	 * @generated
	 */
	EReference getTraceToEntityMapping_Entity();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.TraceToEntityMapping#getTraceColumn <em>Trace Column</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Trace Column</em>'.
	 * @see net.descartesresearch.librede.configuration.TraceToEntityMapping#getTraceColumn()
	 * @see #getTraceToEntityMapping()
	 * @generated
	 */
	EAttribute getTraceToEntityMapping_TraceColumn();

	/**
	 * Returns the meta object for class '{@link net.descartesresearch.librede.configuration.ModelEntity <em>Model Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model Entity</em>'.
	 * @see net.descartesresearch.librede.configuration.ModelEntity
	 * @generated
	 */
	EClass getModelEntity();

	/**
	 * Returns the meta object for enum '{@link net.descartesresearch.librede.configuration.SchedulingStrategy <em>Scheduling Strategy</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Scheduling Strategy</em>'.
	 * @see net.descartesresearch.librede.configuration.SchedulingStrategy
	 * @generated
	 */
	EEnum getSchedulingStrategy();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ConfigurationFactory getConfigurationFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link net.descartesresearch.librede.configuration.impl.LibredeConfigurationImpl <em>Librede Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.descartesresearch.librede.configuration.impl.LibredeConfigurationImpl
		 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getLibredeConfiguration()
		 * @generated
		 */
		EClass LIBREDE_CONFIGURATION = eINSTANCE.getLibredeConfiguration();

		/**
		 * The meta object literal for the '<em><b>Workload Description</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LIBREDE_CONFIGURATION__WORKLOAD_DESCRIPTION = eINSTANCE.getLibredeConfiguration_WorkloadDescription();

		/**
		 * The meta object literal for the '<em><b>Input</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LIBREDE_CONFIGURATION__INPUT = eINSTANCE.getLibredeConfiguration_Input();

		/**
		 * The meta object literal for the '<em><b>Estimation</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LIBREDE_CONFIGURATION__ESTIMATION = eINSTANCE.getLibredeConfiguration_Estimation();

		/**
		 * The meta object literal for the '<em><b>Output</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LIBREDE_CONFIGURATION__OUTPUT = eINSTANCE.getLibredeConfiguration_Output();

		/**
		 * The meta object literal for the '<em><b>Validation</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LIBREDE_CONFIGURATION__VALIDATION = eINSTANCE.getLibredeConfiguration_Validation();

		/**
		 * The meta object literal for the '{@link net.descartesresearch.librede.configuration.impl.DataSourceConfigurationImpl <em>Data Source Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.descartesresearch.librede.configuration.impl.DataSourceConfigurationImpl
		 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getDataSourceConfiguration()
		 * @generated
		 */
		EClass DATA_SOURCE_CONFIGURATION = eINSTANCE.getDataSourceConfiguration();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATA_SOURCE_CONFIGURATION__TYPE = eINSTANCE.getDataSourceConfiguration_Type();

		/**
		 * The meta object literal for the '<em><b>Parameters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_SOURCE_CONFIGURATION__PARAMETERS = eINSTANCE.getDataSourceConfiguration_Parameters();

		/**
		 * The meta object literal for the '{@link net.descartesresearch.librede.configuration.impl.WorkloadDescriptionImpl <em>Workload Description</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.descartesresearch.librede.configuration.impl.WorkloadDescriptionImpl
		 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getWorkloadDescription()
		 * @generated
		 */
		EClass WORKLOAD_DESCRIPTION = eINSTANCE.getWorkloadDescription();

		/**
		 * The meta object literal for the '<em><b>Resources</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference WORKLOAD_DESCRIPTION__RESOURCES = eINSTANCE.getWorkloadDescription_Resources();

		/**
		 * The meta object literal for the '<em><b>Services</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference WORKLOAD_DESCRIPTION__SERVICES = eINSTANCE.getWorkloadDescription_Services();

		/**
		 * The meta object literal for the '{@link net.descartesresearch.librede.configuration.impl.InputSpecificationImpl <em>Input Specification</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.descartesresearch.librede.configuration.impl.InputSpecificationImpl
		 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getInputSpecification()
		 * @generated
		 */
		EClass INPUT_SPECIFICATION = eINSTANCE.getInputSpecification();

		/**
		 * The meta object literal for the '<em><b>Data Sources</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INPUT_SPECIFICATION__DATA_SOURCES = eINSTANCE.getInputSpecification_DataSources();

		/**
		 * The meta object literal for the '<em><b>Observations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INPUT_SPECIFICATION__OBSERVATIONS = eINSTANCE.getInputSpecification_Observations();

		/**
		 * The meta object literal for the '{@link net.descartesresearch.librede.configuration.impl.EstimationApproachConfigurationImpl <em>Estimation Approach Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.descartesresearch.librede.configuration.impl.EstimationApproachConfigurationImpl
		 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getEstimationApproachConfiguration()
		 * @generated
		 */
		EClass ESTIMATION_APPROACH_CONFIGURATION = eINSTANCE.getEstimationApproachConfiguration();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESTIMATION_APPROACH_CONFIGURATION__TYPE = eINSTANCE.getEstimationApproachConfiguration_Type();

		/**
		 * The meta object literal for the '<em><b>Parameters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESTIMATION_APPROACH_CONFIGURATION__PARAMETERS = eINSTANCE.getEstimationApproachConfiguration_Parameters();

		/**
		 * The meta object literal for the '{@link net.descartesresearch.librede.configuration.impl.OutputSpecificationImpl <em>Output Specification</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.descartesresearch.librede.configuration.impl.OutputSpecificationImpl
		 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getOutputSpecification()
		 * @generated
		 */
		EClass OUTPUT_SPECIFICATION = eINSTANCE.getOutputSpecification();

		/**
		 * The meta object literal for the '<em><b>Exporters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OUTPUT_SPECIFICATION__EXPORTERS = eINSTANCE.getOutputSpecification_Exporters();

		/**
		 * The meta object literal for the '{@link net.descartesresearch.librede.configuration.impl.ResourceImpl <em>Resource</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.descartesresearch.librede.configuration.impl.ResourceImpl
		 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getResource()
		 * @generated
		 */
		EClass RESOURCE = eINSTANCE.getResource();

		/**
		 * The meta object literal for the '<em><b>Number Of Servers</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESOURCE__NUMBER_OF_SERVERS = eINSTANCE.getResource_NumberOfServers();

		/**
		 * The meta object literal for the '<em><b>Scheduling Strategy</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESOURCE__SCHEDULING_STRATEGY = eINSTANCE.getResource_SchedulingStrategy();

		/**
		 * The meta object literal for the '{@link net.descartesresearch.librede.configuration.impl.ServiceImpl <em>Service</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.descartesresearch.librede.configuration.impl.ServiceImpl
		 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getService()
		 * @generated
		 */
		EClass SERVICE = eINSTANCE.getService();

		/**
		 * The meta object literal for the '{@link net.descartesresearch.librede.configuration.impl.TraceConfigurationImpl <em>Trace Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.descartesresearch.librede.configuration.impl.TraceConfigurationImpl
		 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getTraceConfiguration()
		 * @generated
		 */
		EClass TRACE_CONFIGURATION = eINSTANCE.getTraceConfiguration();

		/**
		 * The meta object literal for the '<em><b>Metric</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRACE_CONFIGURATION__METRIC = eINSTANCE.getTraceConfiguration_Metric();

		/**
		 * The meta object literal for the '<em><b>Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRACE_CONFIGURATION__UNIT = eINSTANCE.getTraceConfiguration_Unit();

		/**
		 * The meta object literal for the '<em><b>Interval</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRACE_CONFIGURATION__INTERVAL = eINSTANCE.getTraceConfiguration_Interval();

		/**
		 * The meta object literal for the '<em><b>Provider</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRACE_CONFIGURATION__PROVIDER = eINSTANCE.getTraceConfiguration_Provider();

		/**
		 * The meta object literal for the '<em><b>Mappings</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRACE_CONFIGURATION__MAPPINGS = eINSTANCE.getTraceConfiguration_Mappings();

		/**
		 * The meta object literal for the '{@link net.descartesresearch.librede.configuration.impl.ParameterImpl <em>Parameter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.descartesresearch.librede.configuration.impl.ParameterImpl
		 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getParameter()
		 * @generated
		 */
		EClass PARAMETER = eINSTANCE.getParameter();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__NAME = eINSTANCE.getParameter_Name();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__VALUE = eINSTANCE.getParameter_Value();

		/**
		 * The meta object literal for the '{@link net.descartesresearch.librede.configuration.impl.EstimationSpecificationImpl <em>Estimation Specification</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.descartesresearch.librede.configuration.impl.EstimationSpecificationImpl
		 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getEstimationSpecification()
		 * @generated
		 */
		EClass ESTIMATION_SPECIFICATION = eINSTANCE.getEstimationSpecification();

		/**
		 * The meta object literal for the '<em><b>Approaches</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESTIMATION_SPECIFICATION__APPROACHES = eINSTANCE.getEstimationSpecification_Approaches();

		/**
		 * The meta object literal for the '<em><b>Recursive</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESTIMATION_SPECIFICATION__RECURSIVE = eINSTANCE.getEstimationSpecification_Recursive();

		/**
		 * The meta object literal for the '<em><b>Window</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESTIMATION_SPECIFICATION__WINDOW = eINSTANCE.getEstimationSpecification_Window();

		/**
		 * The meta object literal for the '<em><b>Step Size</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESTIMATION_SPECIFICATION__STEP_SIZE = eINSTANCE.getEstimationSpecification_StepSize();

		/**
		 * The meta object literal for the '<em><b>Start Timestamp</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESTIMATION_SPECIFICATION__START_TIMESTAMP = eINSTANCE.getEstimationSpecification_StartTimestamp();

		/**
		 * The meta object literal for the '<em><b>End Timestamp</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESTIMATION_SPECIFICATION__END_TIMESTAMP = eINSTANCE.getEstimationSpecification_EndTimestamp();

		/**
		 * The meta object literal for the '{@link net.descartesresearch.librede.configuration.impl.ValidationSpecificationImpl <em>Validation Specification</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.descartesresearch.librede.configuration.impl.ValidationSpecificationImpl
		 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getValidationSpecification()
		 * @generated
		 */
		EClass VALIDATION_SPECIFICATION = eINSTANCE.getValidationSpecification();

		/**
		 * The meta object literal for the '<em><b>Validators</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VALIDATION_SPECIFICATION__VALIDATORS = eINSTANCE.getValidationSpecification_Validators();

		/**
		 * The meta object literal for the '<em><b>Validation Folds</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VALIDATION_SPECIFICATION__VALIDATION_FOLDS = eINSTANCE.getValidationSpecification_ValidationFolds();

		/**
		 * The meta object literal for the '<em><b>Validate Estimates</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VALIDATION_SPECIFICATION__VALIDATE_ESTIMATES = eINSTANCE.getValidationSpecification_ValidateEstimates();

		/**
		 * The meta object literal for the '{@link net.descartesresearch.librede.configuration.impl.ValidatorConfigurationImpl <em>Validator Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.descartesresearch.librede.configuration.impl.ValidatorConfigurationImpl
		 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getValidatorConfiguration()
		 * @generated
		 */
		EClass VALIDATOR_CONFIGURATION = eINSTANCE.getValidatorConfiguration();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VALIDATOR_CONFIGURATION__TYPE = eINSTANCE.getValidatorConfiguration_Type();

		/**
		 * The meta object literal for the '<em><b>Parameters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VALIDATOR_CONFIGURATION__PARAMETERS = eINSTANCE.getValidatorConfiguration_Parameters();

		/**
		 * The meta object literal for the '{@link net.descartesresearch.librede.configuration.impl.ExporterConfigurationImpl <em>Exporter Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.descartesresearch.librede.configuration.impl.ExporterConfigurationImpl
		 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getExporterConfiguration()
		 * @generated
		 */
		EClass EXPORTER_CONFIGURATION = eINSTANCE.getExporterConfiguration();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPORTER_CONFIGURATION__TYPE = eINSTANCE.getExporterConfiguration_Type();

		/**
		 * The meta object literal for the '<em><b>Parameters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXPORTER_CONFIGURATION__PARAMETERS = eINSTANCE.getExporterConfiguration_Parameters();

		/**
		 * The meta object literal for the '{@link net.descartesresearch.librede.configuration.impl.FileTraceConfigurationImpl <em>File Trace Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.descartesresearch.librede.configuration.impl.FileTraceConfigurationImpl
		 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getFileTraceConfiguration()
		 * @generated
		 */
		EClass FILE_TRACE_CONFIGURATION = eINSTANCE.getFileTraceConfiguration();

		/**
		 * The meta object literal for the '<em><b>File</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FILE_TRACE_CONFIGURATION__FILE = eINSTANCE.getFileTraceConfiguration_File();

		/**
		 * The meta object literal for the '{@link net.descartesresearch.librede.configuration.NamedElement <em>Named Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.descartesresearch.librede.configuration.NamedElement
		 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getNamedElement()
		 * @generated
		 */
		EClass NAMED_ELEMENT = eINSTANCE.getNamedElement();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NAMED_ELEMENT__NAME = eINSTANCE.getNamedElement_Name();

		/**
		 * The meta object literal for the '{@link net.descartesresearch.librede.configuration.impl.TraceToEntityMappingImpl <em>Trace To Entity Mapping</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.descartesresearch.librede.configuration.impl.TraceToEntityMappingImpl
		 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getTraceToEntityMapping()
		 * @generated
		 */
		EClass TRACE_TO_ENTITY_MAPPING = eINSTANCE.getTraceToEntityMapping();

		/**
		 * The meta object literal for the '<em><b>Entity</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRACE_TO_ENTITY_MAPPING__ENTITY = eINSTANCE.getTraceToEntityMapping_Entity();

		/**
		 * The meta object literal for the '<em><b>Trace Column</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRACE_TO_ENTITY_MAPPING__TRACE_COLUMN = eINSTANCE.getTraceToEntityMapping_TraceColumn();

		/**
		 * The meta object literal for the '{@link net.descartesresearch.librede.configuration.impl.ModelEntityImpl <em>Model Entity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.descartesresearch.librede.configuration.impl.ModelEntityImpl
		 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getModelEntity()
		 * @generated
		 */
		EClass MODEL_ENTITY = eINSTANCE.getModelEntity();

		/**
		 * The meta object literal for the '{@link net.descartesresearch.librede.configuration.SchedulingStrategy <em>Scheduling Strategy</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.descartesresearch.librede.configuration.SchedulingStrategy
		 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getSchedulingStrategy()
		 * @generated
		 */
		EEnum SCHEDULING_STRATEGY = eINSTANCE.getSchedulingStrategy();

	}

} //ConfigurationPackage
