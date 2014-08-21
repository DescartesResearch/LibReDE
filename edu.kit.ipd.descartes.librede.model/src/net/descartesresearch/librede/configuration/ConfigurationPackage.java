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
	 * The feature id for the '<em><b>Ouptut</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIBREDE_CONFIGURATION__OUPTUT = 3;

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
	 * The meta object id for the '{@link net.descartesresearch.librede.configuration.impl.DataSourceImpl <em>Data Source</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.descartesresearch.librede.configuration.impl.DataSourceImpl
	 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getDataSource()
	 * @generated
	 */
	int DATA_SOURCE = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SOURCE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Configuration</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SOURCE__CONFIGURATION = 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SOURCE__TYPE = 2;

	/**
	 * The number of structural features of the '<em>Data Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SOURCE_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Data Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SOURCE_OPERATION_COUNT = 0;

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
	 * The meta object id for the '{@link net.descartesresearch.librede.configuration.impl.InputSpecificationImpl <em>Input Specification</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.descartesresearch.librede.configuration.impl.InputSpecificationImpl
	 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getInputSpecification()
	 * @generated
	 */
	int INPUT_SPECIFICATION = 3;

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
	 * The meta object id for the '{@link net.descartesresearch.librede.configuration.impl.EstimationApproachImpl <em>Estimation Approach</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.descartesresearch.librede.configuration.impl.EstimationApproachImpl
	 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getEstimationApproach()
	 * @generated
	 */
	int ESTIMATION_APPROACH = 4;

	/**
	 * The feature id for the '<em><b>Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_APPROACH__CLASS = 0;

	/**
	 * The feature id for the '<em><b>Configuration</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_APPROACH__CONFIGURATION = 1;

	/**
	 * The number of structural features of the '<em>Estimation Approach</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_APPROACH_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Estimation Approach</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_APPROACH_OPERATION_COUNT = 0;

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
	 * The number of structural features of the '<em>Output Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_SPECIFICATION_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Output Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_SPECIFICATION_OPERATION_COUNT = 0;

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
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Number Of Servers</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE__NUMBER_OF_SERVERS = 1;

	/**
	 * The feature id for the '<em><b>Scheduling Strategy</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE__SCHEDULING_STRATEGY = 2;

	/**
	 * The number of structural features of the '<em>Resource</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Resource</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_OPERATION_COUNT = 0;

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
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE__NAME = 0;

	/**
	 * The number of structural features of the '<em>Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link net.descartesresearch.librede.configuration.impl.TimeSeriesImpl <em>Time Series</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.descartesresearch.librede.configuration.impl.TimeSeriesImpl
	 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getTimeSeries()
	 * @generated
	 */
	int TIME_SERIES = 8;

	/**
	 * The feature id for the '<em><b>Metric</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIME_SERIES__METRIC = 0;

	/**
	 * The feature id for the '<em><b>Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIME_SERIES__UNIT = 1;

	/**
	 * The feature id for the '<em><b>Resources</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIME_SERIES__RESOURCES = 2;

	/**
	 * The feature id for the '<em><b>Services</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIME_SERIES__SERVICES = 3;

	/**
	 * The feature id for the '<em><b>Interval</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIME_SERIES__INTERVAL = 4;

	/**
	 * The number of structural features of the '<em>Time Series</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIME_SERIES_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Time Series</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIME_SERIES_OPERATION_COUNT = 0;

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
	 * The meta object id for the '{@link net.descartesresearch.librede.configuration.impl.EstimationSpecificationImpl <em>Estimation Specification</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.descartesresearch.librede.configuration.impl.EstimationSpecificationImpl
	 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getEstimationSpecification()
	 * @generated
	 */
	int ESTIMATION_SPECIFICATION = 10;

	/**
	 * The feature id for the '<em><b>Approaches</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_SPECIFICATION__APPROACHES = 0;

	/**
	 * The number of structural features of the '<em>Estimation Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_SPECIFICATION_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Estimation Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_SPECIFICATION_OPERATION_COUNT = 0;

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
	 * The number of structural features of the '<em>Validation Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALIDATION_SPECIFICATION_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Validation Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALIDATION_SPECIFICATION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link net.descartesresearch.librede.configuration.SchedulingStrategy <em>Scheduling Strategy</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.descartesresearch.librede.configuration.SchedulingStrategy
	 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getSchedulingStrategy()
	 * @generated
	 */
	int SCHEDULING_STRATEGY = 12;


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
	 * Returns the meta object for the containment reference '{@link net.descartesresearch.librede.configuration.LibredeConfiguration#getOuptut <em>Ouptut</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Ouptut</em>'.
	 * @see net.descartesresearch.librede.configuration.LibredeConfiguration#getOuptut()
	 * @see #getLibredeConfiguration()
	 * @generated
	 */
	EReference getLibredeConfiguration_Ouptut();

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
	 * Returns the meta object for class '{@link net.descartesresearch.librede.configuration.DataSource <em>Data Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Source</em>'.
	 * @see net.descartesresearch.librede.configuration.DataSource
	 * @generated
	 */
	EClass getDataSource();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.DataSource#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see net.descartesresearch.librede.configuration.DataSource#getName()
	 * @see #getDataSource()
	 * @generated
	 */
	EAttribute getDataSource_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link net.descartesresearch.librede.configuration.DataSource#getConfiguration <em>Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Configuration</em>'.
	 * @see net.descartesresearch.librede.configuration.DataSource#getConfiguration()
	 * @see #getDataSource()
	 * @generated
	 */
	EReference getDataSource_Configuration();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.DataSource#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see net.descartesresearch.librede.configuration.DataSource#getType()
	 * @see #getDataSource()
	 * @generated
	 */
	EAttribute getDataSource_Type();

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
	 * Returns the meta object for class '{@link net.descartesresearch.librede.configuration.EstimationApproach <em>Estimation Approach</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Estimation Approach</em>'.
	 * @see net.descartesresearch.librede.configuration.EstimationApproach
	 * @generated
	 */
	EClass getEstimationApproach();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.EstimationApproach#getClass_ <em>Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Class</em>'.
	 * @see net.descartesresearch.librede.configuration.EstimationApproach#getClass_()
	 * @see #getEstimationApproach()
	 * @generated
	 */
	EAttribute getEstimationApproach_Class();

	/**
	 * Returns the meta object for the containment reference list '{@link net.descartesresearch.librede.configuration.EstimationApproach#getConfiguration <em>Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Configuration</em>'.
	 * @see net.descartesresearch.librede.configuration.EstimationApproach#getConfiguration()
	 * @see #getEstimationApproach()
	 * @generated
	 */
	EReference getEstimationApproach_Configuration();

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
	 * Returns the meta object for class '{@link net.descartesresearch.librede.configuration.Resource <em>Resource</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Resource</em>'.
	 * @see net.descartesresearch.librede.configuration.Resource
	 * @generated
	 */
	EClass getResource();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.Resource#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see net.descartesresearch.librede.configuration.Resource#getName()
	 * @see #getResource()
	 * @generated
	 */
	EAttribute getResource_Name();

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
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.Service#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see net.descartesresearch.librede.configuration.Service#getName()
	 * @see #getService()
	 * @generated
	 */
	EAttribute getService_Name();

	/**
	 * Returns the meta object for class '{@link net.descartesresearch.librede.configuration.TimeSeries <em>Time Series</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Time Series</em>'.
	 * @see net.descartesresearch.librede.configuration.TimeSeries
	 * @generated
	 */
	EClass getTimeSeries();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.TimeSeries#getMetric <em>Metric</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Metric</em>'.
	 * @see net.descartesresearch.librede.configuration.TimeSeries#getMetric()
	 * @see #getTimeSeries()
	 * @generated
	 */
	EAttribute getTimeSeries_Metric();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.TimeSeries#getUnit <em>Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Unit</em>'.
	 * @see net.descartesresearch.librede.configuration.TimeSeries#getUnit()
	 * @see #getTimeSeries()
	 * @generated
	 */
	EAttribute getTimeSeries_Unit();

	/**
	 * Returns the meta object for the reference list '{@link net.descartesresearch.librede.configuration.TimeSeries#getResources <em>Resources</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Resources</em>'.
	 * @see net.descartesresearch.librede.configuration.TimeSeries#getResources()
	 * @see #getTimeSeries()
	 * @generated
	 */
	EReference getTimeSeries_Resources();

	/**
	 * Returns the meta object for the reference list '{@link net.descartesresearch.librede.configuration.TimeSeries#getServices <em>Services</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Services</em>'.
	 * @see net.descartesresearch.librede.configuration.TimeSeries#getServices()
	 * @see #getTimeSeries()
	 * @generated
	 */
	EReference getTimeSeries_Services();

	/**
	 * Returns the meta object for the attribute '{@link net.descartesresearch.librede.configuration.TimeSeries#getInterval <em>Interval</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Interval</em>'.
	 * @see net.descartesresearch.librede.configuration.TimeSeries#getInterval()
	 * @see #getTimeSeries()
	 * @generated
	 */
	EAttribute getTimeSeries_Interval();

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
	 * Returns the meta object for class '{@link net.descartesresearch.librede.configuration.ValidationSpecification <em>Validation Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Validation Specification</em>'.
	 * @see net.descartesresearch.librede.configuration.ValidationSpecification
	 * @generated
	 */
	EClass getValidationSpecification();

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
		 * The meta object literal for the '<em><b>Ouptut</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LIBREDE_CONFIGURATION__OUPTUT = eINSTANCE.getLibredeConfiguration_Ouptut();

		/**
		 * The meta object literal for the '<em><b>Validation</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LIBREDE_CONFIGURATION__VALIDATION = eINSTANCE.getLibredeConfiguration_Validation();

		/**
		 * The meta object literal for the '{@link net.descartesresearch.librede.configuration.impl.DataSourceImpl <em>Data Source</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.descartesresearch.librede.configuration.impl.DataSourceImpl
		 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getDataSource()
		 * @generated
		 */
		EClass DATA_SOURCE = eINSTANCE.getDataSource();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATA_SOURCE__NAME = eINSTANCE.getDataSource_Name();

		/**
		 * The meta object literal for the '<em><b>Configuration</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_SOURCE__CONFIGURATION = eINSTANCE.getDataSource_Configuration();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATA_SOURCE__TYPE = eINSTANCE.getDataSource_Type();

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
		 * The meta object literal for the '{@link net.descartesresearch.librede.configuration.impl.EstimationApproachImpl <em>Estimation Approach</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.descartesresearch.librede.configuration.impl.EstimationApproachImpl
		 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getEstimationApproach()
		 * @generated
		 */
		EClass ESTIMATION_APPROACH = eINSTANCE.getEstimationApproach();

		/**
		 * The meta object literal for the '<em><b>Class</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESTIMATION_APPROACH__CLASS = eINSTANCE.getEstimationApproach_Class();

		/**
		 * The meta object literal for the '<em><b>Configuration</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESTIMATION_APPROACH__CONFIGURATION = eINSTANCE.getEstimationApproach_Configuration();

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
		 * The meta object literal for the '{@link net.descartesresearch.librede.configuration.impl.ResourceImpl <em>Resource</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.descartesresearch.librede.configuration.impl.ResourceImpl
		 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getResource()
		 * @generated
		 */
		EClass RESOURCE = eINSTANCE.getResource();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESOURCE__NAME = eINSTANCE.getResource_Name();

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
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SERVICE__NAME = eINSTANCE.getService_Name();

		/**
		 * The meta object literal for the '{@link net.descartesresearch.librede.configuration.impl.TimeSeriesImpl <em>Time Series</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.descartesresearch.librede.configuration.impl.TimeSeriesImpl
		 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getTimeSeries()
		 * @generated
		 */
		EClass TIME_SERIES = eINSTANCE.getTimeSeries();

		/**
		 * The meta object literal for the '<em><b>Metric</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TIME_SERIES__METRIC = eINSTANCE.getTimeSeries_Metric();

		/**
		 * The meta object literal for the '<em><b>Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TIME_SERIES__UNIT = eINSTANCE.getTimeSeries_Unit();

		/**
		 * The meta object literal for the '<em><b>Resources</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TIME_SERIES__RESOURCES = eINSTANCE.getTimeSeries_Resources();

		/**
		 * The meta object literal for the '<em><b>Services</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TIME_SERIES__SERVICES = eINSTANCE.getTimeSeries_Services();

		/**
		 * The meta object literal for the '<em><b>Interval</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TIME_SERIES__INTERVAL = eINSTANCE.getTimeSeries_Interval();

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
		 * The meta object literal for the '{@link net.descartesresearch.librede.configuration.impl.ValidationSpecificationImpl <em>Validation Specification</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.descartesresearch.librede.configuration.impl.ValidationSpecificationImpl
		 * @see net.descartesresearch.librede.configuration.impl.ConfigurationPackageImpl#getValidationSpecification()
		 * @generated
		 */
		EClass VALIDATION_SPECIFICATION = eINSTANCE.getValidationSpecification();

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
