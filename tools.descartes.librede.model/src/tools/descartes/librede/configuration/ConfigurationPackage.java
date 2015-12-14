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
 * @see tools.descartes.librede.configuration.ConfigurationFactory
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
	ConfigurationPackage eINSTANCE = tools.descartes.librede.configuration.impl.ConfigurationPackageImpl.init();

	/**
	 * The meta object id for the '{@link tools.descartes.librede.configuration.impl.LibredeConfigurationImpl <em>Librede Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.configuration.impl.LibredeConfigurationImpl
	 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getLibredeConfiguration()
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
	 * The meta object id for the '{@link tools.descartes.librede.configuration.impl.DataSourceConfigurationImpl <em>Data Source Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.configuration.impl.DataSourceConfigurationImpl
	 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getDataSourceConfiguration()
	 * @generated
	 */
	int DATA_SOURCE_CONFIGURATION = 1;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.configuration.impl.WorkloadDescriptionImpl <em>Workload Description</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.configuration.impl.WorkloadDescriptionImpl
	 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getWorkloadDescription()
	 * @generated
	 */
	int WORKLOAD_DESCRIPTION = 2;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.configuration.impl.InputSpecificationImpl <em>Input Specification</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.configuration.impl.InputSpecificationImpl
	 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getInputSpecification()
	 * @generated
	 */
	int INPUT_SPECIFICATION = 3;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.configuration.impl.EstimationApproachConfigurationImpl <em>Estimation Approach Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.configuration.impl.EstimationApproachConfigurationImpl
	 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getEstimationApproachConfiguration()
	 * @generated
	 */
	int ESTIMATION_APPROACH_CONFIGURATION = 4;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.configuration.impl.OutputSpecificationImpl <em>Output Specification</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.configuration.impl.OutputSpecificationImpl
	 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getOutputSpecification()
	 * @generated
	 */
	int OUTPUT_SPECIFICATION = 5;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.configuration.impl.ResourceImpl <em>Resource</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.configuration.impl.ResourceImpl
	 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getResource()
	 * @generated
	 */
	int RESOURCE = 6;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.configuration.impl.ServiceImpl <em>Service</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.configuration.impl.ServiceImpl
	 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getService()
	 * @generated
	 */
	int SERVICE = 7;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.configuration.impl.TraceConfigurationImpl <em>Trace Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.configuration.impl.TraceConfigurationImpl
	 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getTraceConfiguration()
	 * @generated
	 */
	int TRACE_CONFIGURATION = 8;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.configuration.impl.ParameterImpl <em>Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.configuration.impl.ParameterImpl
	 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getParameter()
	 * @generated
	 */
	int PARAMETER = 9;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.configuration.impl.EstimationSpecificationImpl <em>Estimation Specification</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.configuration.impl.EstimationSpecificationImpl
	 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getEstimationSpecification()
	 * @generated
	 */
	int ESTIMATION_SPECIFICATION = 10;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.configuration.impl.ValidationSpecificationImpl <em>Validation Specification</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.configuration.impl.ValidationSpecificationImpl
	 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getValidationSpecification()
	 * @generated
	 */
	int VALIDATION_SPECIFICATION = 11;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.configuration.impl.ValidatorConfigurationImpl <em>Validator Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.configuration.impl.ValidatorConfigurationImpl
	 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getValidatorConfiguration()
	 * @generated
	 */
	int VALIDATOR_CONFIGURATION = 12;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.configuration.impl.ExporterConfigurationImpl <em>Exporter Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.configuration.impl.ExporterConfigurationImpl
	 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getExporterConfiguration()
	 * @generated
	 */
	int EXPORTER_CONFIGURATION = 13;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.configuration.impl.FileTraceConfigurationImpl <em>File Trace Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.configuration.impl.FileTraceConfigurationImpl
	 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getFileTraceConfiguration()
	 * @generated
	 */
	int FILE_TRACE_CONFIGURATION = 14;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.configuration.NamedElement <em>Named Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.configuration.NamedElement
	 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getNamedElement()
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
	 * The meta object id for the '{@link tools.descartes.librede.configuration.impl.ModelEntityImpl <em>Model Entity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.configuration.impl.ModelEntityImpl
	 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getModelEntity()
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
	 * The feature id for the '<em><b>Child Resources</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE__CHILD_RESOURCES = MODEL_ENTITY_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Demands</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE__DEMANDS = MODEL_ENTITY_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Accessing Services</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE__ACCESSING_SERVICES = MODEL_ENTITY_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Resource</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_FEATURE_COUNT = MODEL_ENTITY_FEATURE_COUNT + 5;

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
	 * The feature id for the '<em><b>Background Service</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE__BACKGROUND_SERVICE = MODEL_ENTITY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Tasks</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE__TASKS = MODEL_ENTITY_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Accessed Resources</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE__ACCESSED_RESOURCES = MODEL_ENTITY_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Incoming Calls</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE__INCOMING_CALLS = MODEL_ENTITY_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Outgoing Calls</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE__OUTGOING_CALLS = MODEL_ENTITY_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Resource Demands</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE__RESOURCE_DEMANDS = MODEL_ENTITY_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_FEATURE_COUNT = MODEL_ENTITY_FEATURE_COUNT + 6;

	/**
	 * The number of operations of the '<em>Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_OPERATION_COUNT = MODEL_ENTITY_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Metric</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_CONFIGURATION__METRIC = 0;

	/**
	 * The feature id for the '<em><b>Data Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_CONFIGURATION__DATA_SOURCE = 1;

	/**
	 * The feature id for the '<em><b>Mappings</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_CONFIGURATION__MAPPINGS = 2;

	/**
	 * The feature id for the '<em><b>Unit</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_CONFIGURATION__UNIT = 3;

	/**
	 * The feature id for the '<em><b>Interval</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_CONFIGURATION__INTERVAL = 4;

	/**
	 * The feature id for the '<em><b>Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_CONFIGURATION__LOCATION = 5;

	/**
	 * The feature id for the '<em><b>Aggregation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_CONFIGURATION__AGGREGATION = 6;

	/**
	 * The number of structural features of the '<em>Trace Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_CONFIGURATION_FEATURE_COUNT = 7;

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
	 * The feature id for the '<em><b>Algorithms</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_SPECIFICATION__ALGORITHMS = 3;

	/**
	 * The feature id for the '<em><b>Step Size</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_SPECIFICATION__STEP_SIZE = 4;

	/**
	 * The feature id for the '<em><b>Start Timestamp</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_SPECIFICATION__START_TIMESTAMP = 5;

	/**
	 * The feature id for the '<em><b>End Timestamp</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_SPECIFICATION__END_TIMESTAMP = 6;

	/**
	 * The number of structural features of the '<em>Estimation Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_SPECIFICATION_FEATURE_COUNT = 7;

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
	 * The feature id for the '<em><b>Metric</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_TRACE_CONFIGURATION__METRIC = TRACE_CONFIGURATION__METRIC;

	/**
	 * The feature id for the '<em><b>Data Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_TRACE_CONFIGURATION__DATA_SOURCE = TRACE_CONFIGURATION__DATA_SOURCE;

	/**
	 * The feature id for the '<em><b>Mappings</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_TRACE_CONFIGURATION__MAPPINGS = TRACE_CONFIGURATION__MAPPINGS;

	/**
	 * The feature id for the '<em><b>Unit</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_TRACE_CONFIGURATION__UNIT = TRACE_CONFIGURATION__UNIT;

	/**
	 * The feature id for the '<em><b>Interval</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_TRACE_CONFIGURATION__INTERVAL = TRACE_CONFIGURATION__INTERVAL;

	/**
	 * The feature id for the '<em><b>Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_TRACE_CONFIGURATION__LOCATION = TRACE_CONFIGURATION__LOCATION;

	/**
	 * The feature id for the '<em><b>Aggregation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_TRACE_CONFIGURATION__AGGREGATION = TRACE_CONFIGURATION__AGGREGATION;

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
	 * The meta object id for the '{@link tools.descartes.librede.configuration.impl.TraceToEntityMappingImpl <em>Trace To Entity Mapping</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.configuration.impl.TraceToEntityMappingImpl
	 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getTraceToEntityMapping()
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
	 * The feature id for the '<em><b>Filters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_TO_ENTITY_MAPPING__FILTERS = 2;

	/**
	 * The number of structural features of the '<em>Trace To Entity Mapping</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_TO_ENTITY_MAPPING_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Trace To Entity Mapping</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_TO_ENTITY_MAPPING_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.configuration.impl.EstimationAlgorithmConfigurationImpl <em>Estimation Algorithm Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.configuration.impl.EstimationAlgorithmConfigurationImpl
	 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getEstimationAlgorithmConfiguration()
	 * @generated
	 */
	int ESTIMATION_ALGORITHM_CONFIGURATION = 18;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_ALGORITHM_CONFIGURATION__TYPE = 0;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_ALGORITHM_CONFIGURATION__PARAMETERS = 1;

	/**
	 * The number of structural features of the '<em>Estimation Algorithm Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_ALGORITHM_CONFIGURATION_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Estimation Algorithm Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTIMATION_ALGORITHM_CONFIGURATION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.configuration.impl.TraceFilterImpl <em>Trace Filter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.configuration.impl.TraceFilterImpl
	 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getTraceFilter()
	 * @generated
	 */
	int TRACE_FILTER = 19;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_FILTER__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Trace Column</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_FILTER__TRACE_COLUMN = 1;

	/**
	 * The number of structural features of the '<em>Trace Filter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_FILTER_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Trace Filter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_FILTER_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.configuration.impl.TaskImpl <em>Task</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.configuration.impl.TaskImpl
	 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getTask()
	 * @generated
	 */
	int TASK = 23;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK__NAME = MODEL_ENTITY__NAME;

	/**
	 * The feature id for the '<em><b>Service</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK__SERVICE = MODEL_ENTITY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Task</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK_FEATURE_COUNT = MODEL_ENTITY_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Task</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK_OPERATION_COUNT = MODEL_ENTITY_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.configuration.impl.ResourceDemandImpl <em>Resource Demand</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.configuration.impl.ResourceDemandImpl
	 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getResourceDemand()
	 * @generated
	 */
	int RESOURCE_DEMAND = 20;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DEMAND__NAME = TASK__NAME;

	/**
	 * The feature id for the '<em><b>Service</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DEMAND__SERVICE = TASK__SERVICE;

	/**
	 * The feature id for the '<em><b>Resource</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DEMAND__RESOURCE = TASK_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Resource Demand</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DEMAND_FEATURE_COUNT = TASK_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Resource Demand</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DEMAND_OPERATION_COUNT = TASK_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.configuration.impl.ExternalCallImpl <em>External Call</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.configuration.impl.ExternalCallImpl
	 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getExternalCall()
	 * @generated
	 */
	int EXTERNAL_CALL = 21;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTERNAL_CALL__NAME = TASK__NAME;

	/**
	 * The feature id for the '<em><b>Service</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTERNAL_CALL__SERVICE = TASK__SERVICE;

	/**
	 * The feature id for the '<em><b>Called Service</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTERNAL_CALL__CALLED_SERVICE = TASK_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>External Call</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTERNAL_CALL_FEATURE_COUNT = TASK_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>External Call</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTERNAL_CALL_OPERATION_COUNT = TASK_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.configuration.impl.CompositeServiceImpl <em>Composite Service</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.configuration.impl.CompositeServiceImpl
	 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getCompositeService()
	 * @generated
	 */
	int COMPOSITE_SERVICE = 22;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_SERVICE__NAME = SERVICE__NAME;

	/**
	 * The feature id for the '<em><b>Background Service</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_SERVICE__BACKGROUND_SERVICE = SERVICE__BACKGROUND_SERVICE;

	/**
	 * The feature id for the '<em><b>Tasks</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_SERVICE__TASKS = SERVICE__TASKS;

	/**
	 * The feature id for the '<em><b>Accessed Resources</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_SERVICE__ACCESSED_RESOURCES = SERVICE__ACCESSED_RESOURCES;

	/**
	 * The feature id for the '<em><b>Incoming Calls</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_SERVICE__INCOMING_CALLS = SERVICE__INCOMING_CALLS;

	/**
	 * The feature id for the '<em><b>Outgoing Calls</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_SERVICE__OUTGOING_CALLS = SERVICE__OUTGOING_CALLS;

	/**
	 * The feature id for the '<em><b>Resource Demands</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_SERVICE__RESOURCE_DEMANDS = SERVICE__RESOURCE_DEMANDS;

	/**
	 * The feature id for the '<em><b>Sub Services</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_SERVICE__SUB_SERVICES = SERVICE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Composite Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_SERVICE_FEATURE_COUNT = SERVICE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Composite Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_SERVICE_OPERATION_COUNT = SERVICE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.configuration.SchedulingStrategy <em>Scheduling Strategy</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.configuration.SchedulingStrategy
	 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getSchedulingStrategy()
	 * @generated
	 */
	int SCHEDULING_STRATEGY = 24;


	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.configuration.LibredeConfiguration <em>Librede Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Librede Configuration</em>'.
	 * @see tools.descartes.librede.configuration.LibredeConfiguration
	 * @generated
	 */
	EClass getLibredeConfiguration();

	/**
	 * Returns the meta object for the containment reference '{@link tools.descartes.librede.configuration.LibredeConfiguration#getWorkloadDescription <em>Workload Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Workload Description</em>'.
	 * @see tools.descartes.librede.configuration.LibredeConfiguration#getWorkloadDescription()
	 * @see #getLibredeConfiguration()
	 * @generated
	 */
	EReference getLibredeConfiguration_WorkloadDescription();

	/**
	 * Returns the meta object for the containment reference '{@link tools.descartes.librede.configuration.LibredeConfiguration#getInput <em>Input</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Input</em>'.
	 * @see tools.descartes.librede.configuration.LibredeConfiguration#getInput()
	 * @see #getLibredeConfiguration()
	 * @generated
	 */
	EReference getLibredeConfiguration_Input();

	/**
	 * Returns the meta object for the containment reference '{@link tools.descartes.librede.configuration.LibredeConfiguration#getEstimation <em>Estimation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Estimation</em>'.
	 * @see tools.descartes.librede.configuration.LibredeConfiguration#getEstimation()
	 * @see #getLibredeConfiguration()
	 * @generated
	 */
	EReference getLibredeConfiguration_Estimation();

	/**
	 * Returns the meta object for the containment reference '{@link tools.descartes.librede.configuration.LibredeConfiguration#getOutput <em>Output</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Output</em>'.
	 * @see tools.descartes.librede.configuration.LibredeConfiguration#getOutput()
	 * @see #getLibredeConfiguration()
	 * @generated
	 */
	EReference getLibredeConfiguration_Output();

	/**
	 * Returns the meta object for the containment reference '{@link tools.descartes.librede.configuration.LibredeConfiguration#getValidation <em>Validation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Validation</em>'.
	 * @see tools.descartes.librede.configuration.LibredeConfiguration#getValidation()
	 * @see #getLibredeConfiguration()
	 * @generated
	 */
	EReference getLibredeConfiguration_Validation();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.configuration.DataSourceConfiguration <em>Data Source Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Source Configuration</em>'.
	 * @see tools.descartes.librede.configuration.DataSourceConfiguration
	 * @generated
	 */
	EClass getDataSourceConfiguration();

	/**
	 * Returns the meta object for the attribute '{@link tools.descartes.librede.configuration.DataSourceConfiguration#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see tools.descartes.librede.configuration.DataSourceConfiguration#getType()
	 * @see #getDataSourceConfiguration()
	 * @generated
	 */
	EAttribute getDataSourceConfiguration_Type();

	/**
	 * Returns the meta object for the containment reference list '{@link tools.descartes.librede.configuration.DataSourceConfiguration#getParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameters</em>'.
	 * @see tools.descartes.librede.configuration.DataSourceConfiguration#getParameters()
	 * @see #getDataSourceConfiguration()
	 * @generated
	 */
	EReference getDataSourceConfiguration_Parameters();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.configuration.WorkloadDescription <em>Workload Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Workload Description</em>'.
	 * @see tools.descartes.librede.configuration.WorkloadDescription
	 * @generated
	 */
	EClass getWorkloadDescription();

	/**
	 * Returns the meta object for the containment reference list '{@link tools.descartes.librede.configuration.WorkloadDescription#getResources <em>Resources</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Resources</em>'.
	 * @see tools.descartes.librede.configuration.WorkloadDescription#getResources()
	 * @see #getWorkloadDescription()
	 * @generated
	 */
	EReference getWorkloadDescription_Resources();

	/**
	 * Returns the meta object for the containment reference list '{@link tools.descartes.librede.configuration.WorkloadDescription#getServices <em>Services</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Services</em>'.
	 * @see tools.descartes.librede.configuration.WorkloadDescription#getServices()
	 * @see #getWorkloadDescription()
	 * @generated
	 */
	EReference getWorkloadDescription_Services();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.configuration.InputSpecification <em>Input Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Input Specification</em>'.
	 * @see tools.descartes.librede.configuration.InputSpecification
	 * @generated
	 */
	EClass getInputSpecification();

	/**
	 * Returns the meta object for the containment reference list '{@link tools.descartes.librede.configuration.InputSpecification#getDataSources <em>Data Sources</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Data Sources</em>'.
	 * @see tools.descartes.librede.configuration.InputSpecification#getDataSources()
	 * @see #getInputSpecification()
	 * @generated
	 */
	EReference getInputSpecification_DataSources();

	/**
	 * Returns the meta object for the containment reference list '{@link tools.descartes.librede.configuration.InputSpecification#getObservations <em>Observations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Observations</em>'.
	 * @see tools.descartes.librede.configuration.InputSpecification#getObservations()
	 * @see #getInputSpecification()
	 * @generated
	 */
	EReference getInputSpecification_Observations();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.configuration.EstimationApproachConfiguration <em>Estimation Approach Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Estimation Approach Configuration</em>'.
	 * @see tools.descartes.librede.configuration.EstimationApproachConfiguration
	 * @generated
	 */
	EClass getEstimationApproachConfiguration();

	/**
	 * Returns the meta object for the attribute '{@link tools.descartes.librede.configuration.EstimationApproachConfiguration#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see tools.descartes.librede.configuration.EstimationApproachConfiguration#getType()
	 * @see #getEstimationApproachConfiguration()
	 * @generated
	 */
	EAttribute getEstimationApproachConfiguration_Type();

	/**
	 * Returns the meta object for the containment reference list '{@link tools.descartes.librede.configuration.EstimationApproachConfiguration#getParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameters</em>'.
	 * @see tools.descartes.librede.configuration.EstimationApproachConfiguration#getParameters()
	 * @see #getEstimationApproachConfiguration()
	 * @generated
	 */
	EReference getEstimationApproachConfiguration_Parameters();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.configuration.OutputSpecification <em>Output Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Output Specification</em>'.
	 * @see tools.descartes.librede.configuration.OutputSpecification
	 * @generated
	 */
	EClass getOutputSpecification();

	/**
	 * Returns the meta object for the containment reference list '{@link tools.descartes.librede.configuration.OutputSpecification#getExporters <em>Exporters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Exporters</em>'.
	 * @see tools.descartes.librede.configuration.OutputSpecification#getExporters()
	 * @see #getOutputSpecification()
	 * @generated
	 */
	EReference getOutputSpecification_Exporters();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.configuration.Resource <em>Resource</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Resource</em>'.
	 * @see tools.descartes.librede.configuration.Resource
	 * @generated
	 */
	EClass getResource();

	/**
	 * Returns the meta object for the attribute '{@link tools.descartes.librede.configuration.Resource#getNumberOfServers <em>Number Of Servers</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Number Of Servers</em>'.
	 * @see tools.descartes.librede.configuration.Resource#getNumberOfServers()
	 * @see #getResource()
	 * @generated
	 */
	EAttribute getResource_NumberOfServers();

	/**
	 * Returns the meta object for the attribute '{@link tools.descartes.librede.configuration.Resource#getSchedulingStrategy <em>Scheduling Strategy</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Scheduling Strategy</em>'.
	 * @see tools.descartes.librede.configuration.Resource#getSchedulingStrategy()
	 * @see #getResource()
	 * @generated
	 */
	EAttribute getResource_SchedulingStrategy();

	/**
	 * Returns the meta object for the reference list '{@link tools.descartes.librede.configuration.Resource#getChildResources <em>Child Resources</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Child Resources</em>'.
	 * @see tools.descartes.librede.configuration.Resource#getChildResources()
	 * @see #getResource()
	 * @generated
	 */
	EReference getResource_ChildResources();

	/**
	 * Returns the meta object for the reference list '{@link tools.descartes.librede.configuration.Resource#getDemands <em>Demands</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Demands</em>'.
	 * @see tools.descartes.librede.configuration.Resource#getDemands()
	 * @see #getResource()
	 * @generated
	 */
	EReference getResource_Demands();

	/**
	 * Returns the meta object for the reference list '{@link tools.descartes.librede.configuration.Resource#getAccessingServices <em>Accessing Services</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Accessing Services</em>'.
	 * @see tools.descartes.librede.configuration.Resource#getAccessingServices()
	 * @see #getResource()
	 * @generated
	 */
	EReference getResource_AccessingServices();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.configuration.Service <em>Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Service</em>'.
	 * @see tools.descartes.librede.configuration.Service
	 * @generated
	 */
	EClass getService();

	/**
	 * Returns the meta object for the attribute '{@link tools.descartes.librede.configuration.Service#isBackgroundService <em>Background Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Background Service</em>'.
	 * @see tools.descartes.librede.configuration.Service#isBackgroundService()
	 * @see #getService()
	 * @generated
	 */
	EAttribute getService_BackgroundService();

	/**
	 * Returns the meta object for the containment reference list '{@link tools.descartes.librede.configuration.Service#getTasks <em>Tasks</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Tasks</em>'.
	 * @see tools.descartes.librede.configuration.Service#getTasks()
	 * @see #getService()
	 * @generated
	 */
	EReference getService_Tasks();

	/**
	 * Returns the meta object for the reference list '{@link tools.descartes.librede.configuration.Service#getAccessedResources <em>Accessed Resources</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Accessed Resources</em>'.
	 * @see tools.descartes.librede.configuration.Service#getAccessedResources()
	 * @see #getService()
	 * @generated
	 */
	EReference getService_AccessedResources();

	/**
	 * Returns the meta object for the reference list '{@link tools.descartes.librede.configuration.Service#getIncomingCalls <em>Incoming Calls</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Incoming Calls</em>'.
	 * @see tools.descartes.librede.configuration.Service#getIncomingCalls()
	 * @see #getService()
	 * @generated
	 */
	EReference getService_IncomingCalls();

	/**
	 * Returns the meta object for the reference list '{@link tools.descartes.librede.configuration.Service#getOutgoingCalls <em>Outgoing Calls</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Outgoing Calls</em>'.
	 * @see tools.descartes.librede.configuration.Service#getOutgoingCalls()
	 * @see #getService()
	 * @generated
	 */
	EReference getService_OutgoingCalls();

	/**
	 * Returns the meta object for the reference list '{@link tools.descartes.librede.configuration.Service#getResourceDemands <em>Resource Demands</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Resource Demands</em>'.
	 * @see tools.descartes.librede.configuration.Service#getResourceDemands()
	 * @see #getService()
	 * @generated
	 */
	EReference getService_ResourceDemands();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.configuration.TraceConfiguration <em>Trace Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Trace Configuration</em>'.
	 * @see tools.descartes.librede.configuration.TraceConfiguration
	 * @generated
	 */
	EClass getTraceConfiguration();

	/**
	 * Returns the meta object for the reference '{@link tools.descartes.librede.configuration.TraceConfiguration#getMetric <em>Metric</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Metric</em>'.
	 * @see tools.descartes.librede.configuration.TraceConfiguration#getMetric()
	 * @see #getTraceConfiguration()
	 * @generated
	 */
	EReference getTraceConfiguration_Metric();

	/**
	 * Returns the meta object for the reference '{@link tools.descartes.librede.configuration.TraceConfiguration#getUnit <em>Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Unit</em>'.
	 * @see tools.descartes.librede.configuration.TraceConfiguration#getUnit()
	 * @see #getTraceConfiguration()
	 * @generated
	 */
	EReference getTraceConfiguration_Unit();

	/**
	 * Returns the meta object for the containment reference '{@link tools.descartes.librede.configuration.TraceConfiguration#getInterval <em>Interval</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Interval</em>'.
	 * @see tools.descartes.librede.configuration.TraceConfiguration#getInterval()
	 * @see #getTraceConfiguration()
	 * @generated
	 */
	EReference getTraceConfiguration_Interval();

	/**
	 * Returns the meta object for the attribute '{@link tools.descartes.librede.configuration.TraceConfiguration#getLocation <em>Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Location</em>'.
	 * @see tools.descartes.librede.configuration.TraceConfiguration#getLocation()
	 * @see #getTraceConfiguration()
	 * @generated
	 */
	EAttribute getTraceConfiguration_Location();

	/**
	 * Returns the meta object for the attribute '{@link tools.descartes.librede.configuration.TraceConfiguration#getAggregation <em>Aggregation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Aggregation</em>'.
	 * @see tools.descartes.librede.configuration.TraceConfiguration#getAggregation()
	 * @see #getTraceConfiguration()
	 * @generated
	 */
	EAttribute getTraceConfiguration_Aggregation();

	/**
	 * Returns the meta object for the reference '{@link tools.descartes.librede.configuration.TraceConfiguration#getDataSource <em>Data Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Data Source</em>'.
	 * @see tools.descartes.librede.configuration.TraceConfiguration#getDataSource()
	 * @see #getTraceConfiguration()
	 * @generated
	 */
	EReference getTraceConfiguration_DataSource();

	/**
	 * Returns the meta object for the containment reference list '{@link tools.descartes.librede.configuration.TraceConfiguration#getMappings <em>Mappings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Mappings</em>'.
	 * @see tools.descartes.librede.configuration.TraceConfiguration#getMappings()
	 * @see #getTraceConfiguration()
	 * @generated
	 */
	EReference getTraceConfiguration_Mappings();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.configuration.Parameter <em>Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parameter</em>'.
	 * @see tools.descartes.librede.configuration.Parameter
	 * @generated
	 */
	EClass getParameter();

	/**
	 * Returns the meta object for the attribute '{@link tools.descartes.librede.configuration.Parameter#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see tools.descartes.librede.configuration.Parameter#getName()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_Name();

	/**
	 * Returns the meta object for the attribute '{@link tools.descartes.librede.configuration.Parameter#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see tools.descartes.librede.configuration.Parameter#getValue()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_Value();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.configuration.EstimationSpecification <em>Estimation Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Estimation Specification</em>'.
	 * @see tools.descartes.librede.configuration.EstimationSpecification
	 * @generated
	 */
	EClass getEstimationSpecification();

	/**
	 * Returns the meta object for the containment reference list '{@link tools.descartes.librede.configuration.EstimationSpecification#getApproaches <em>Approaches</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Approaches</em>'.
	 * @see tools.descartes.librede.configuration.EstimationSpecification#getApproaches()
	 * @see #getEstimationSpecification()
	 * @generated
	 */
	EReference getEstimationSpecification_Approaches();

	/**
	 * Returns the meta object for the attribute '{@link tools.descartes.librede.configuration.EstimationSpecification#isRecursive <em>Recursive</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Recursive</em>'.
	 * @see tools.descartes.librede.configuration.EstimationSpecification#isRecursive()
	 * @see #getEstimationSpecification()
	 * @generated
	 */
	EAttribute getEstimationSpecification_Recursive();

	/**
	 * Returns the meta object for the attribute '{@link tools.descartes.librede.configuration.EstimationSpecification#getWindow <em>Window</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Window</em>'.
	 * @see tools.descartes.librede.configuration.EstimationSpecification#getWindow()
	 * @see #getEstimationSpecification()
	 * @generated
	 */
	EAttribute getEstimationSpecification_Window();

	/**
	 * Returns the meta object for the containment reference '{@link tools.descartes.librede.configuration.EstimationSpecification#getStepSize <em>Step Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Step Size</em>'.
	 * @see tools.descartes.librede.configuration.EstimationSpecification#getStepSize()
	 * @see #getEstimationSpecification()
	 * @generated
	 */
	EReference getEstimationSpecification_StepSize();

	/**
	 * Returns the meta object for the containment reference '{@link tools.descartes.librede.configuration.EstimationSpecification#getStartTimestamp <em>Start Timestamp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Start Timestamp</em>'.
	 * @see tools.descartes.librede.configuration.EstimationSpecification#getStartTimestamp()
	 * @see #getEstimationSpecification()
	 * @generated
	 */
	EReference getEstimationSpecification_StartTimestamp();

	/**
	 * Returns the meta object for the containment reference '{@link tools.descartes.librede.configuration.EstimationSpecification#getEndTimestamp <em>End Timestamp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>End Timestamp</em>'.
	 * @see tools.descartes.librede.configuration.EstimationSpecification#getEndTimestamp()
	 * @see #getEstimationSpecification()
	 * @generated
	 */
	EReference getEstimationSpecification_EndTimestamp();

	/**
	 * Returns the meta object for the containment reference list '{@link tools.descartes.librede.configuration.EstimationSpecification#getAlgorithms <em>Algorithms</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Algorithms</em>'.
	 * @see tools.descartes.librede.configuration.EstimationSpecification#getAlgorithms()
	 * @see #getEstimationSpecification()
	 * @generated
	 */
	EReference getEstimationSpecification_Algorithms();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.configuration.ValidationSpecification <em>Validation Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Validation Specification</em>'.
	 * @see tools.descartes.librede.configuration.ValidationSpecification
	 * @generated
	 */
	EClass getValidationSpecification();

	/**
	 * Returns the meta object for the containment reference list '{@link tools.descartes.librede.configuration.ValidationSpecification#getValidators <em>Validators</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Validators</em>'.
	 * @see tools.descartes.librede.configuration.ValidationSpecification#getValidators()
	 * @see #getValidationSpecification()
	 * @generated
	 */
	EReference getValidationSpecification_Validators();

	/**
	 * Returns the meta object for the attribute '{@link tools.descartes.librede.configuration.ValidationSpecification#getValidationFolds <em>Validation Folds</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Validation Folds</em>'.
	 * @see tools.descartes.librede.configuration.ValidationSpecification#getValidationFolds()
	 * @see #getValidationSpecification()
	 * @generated
	 */
	EAttribute getValidationSpecification_ValidationFolds();

	/**
	 * Returns the meta object for the attribute '{@link tools.descartes.librede.configuration.ValidationSpecification#isValidateEstimates <em>Validate Estimates</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Validate Estimates</em>'.
	 * @see tools.descartes.librede.configuration.ValidationSpecification#isValidateEstimates()
	 * @see #getValidationSpecification()
	 * @generated
	 */
	EAttribute getValidationSpecification_ValidateEstimates();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.configuration.ValidatorConfiguration <em>Validator Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Validator Configuration</em>'.
	 * @see tools.descartes.librede.configuration.ValidatorConfiguration
	 * @generated
	 */
	EClass getValidatorConfiguration();

	/**
	 * Returns the meta object for the attribute '{@link tools.descartes.librede.configuration.ValidatorConfiguration#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see tools.descartes.librede.configuration.ValidatorConfiguration#getType()
	 * @see #getValidatorConfiguration()
	 * @generated
	 */
	EAttribute getValidatorConfiguration_Type();

	/**
	 * Returns the meta object for the containment reference list '{@link tools.descartes.librede.configuration.ValidatorConfiguration#getParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameters</em>'.
	 * @see tools.descartes.librede.configuration.ValidatorConfiguration#getParameters()
	 * @see #getValidatorConfiguration()
	 * @generated
	 */
	EReference getValidatorConfiguration_Parameters();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.configuration.ExporterConfiguration <em>Exporter Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Exporter Configuration</em>'.
	 * @see tools.descartes.librede.configuration.ExporterConfiguration
	 * @generated
	 */
	EClass getExporterConfiguration();

	/**
	 * Returns the meta object for the attribute '{@link tools.descartes.librede.configuration.ExporterConfiguration#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see tools.descartes.librede.configuration.ExporterConfiguration#getType()
	 * @see #getExporterConfiguration()
	 * @generated
	 */
	EAttribute getExporterConfiguration_Type();

	/**
	 * Returns the meta object for the containment reference list '{@link tools.descartes.librede.configuration.ExporterConfiguration#getParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameters</em>'.
	 * @see tools.descartes.librede.configuration.ExporterConfiguration#getParameters()
	 * @see #getExporterConfiguration()
	 * @generated
	 */
	EReference getExporterConfiguration_Parameters();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.configuration.FileTraceConfiguration <em>File Trace Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>File Trace Configuration</em>'.
	 * @see tools.descartes.librede.configuration.FileTraceConfiguration
	 * @generated
	 */
	EClass getFileTraceConfiguration();

	/**
	 * Returns the meta object for the attribute '{@link tools.descartes.librede.configuration.FileTraceConfiguration#getFile <em>File</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>File</em>'.
	 * @see tools.descartes.librede.configuration.FileTraceConfiguration#getFile()
	 * @see #getFileTraceConfiguration()
	 * @generated
	 */
	EAttribute getFileTraceConfiguration_File();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.configuration.NamedElement <em>Named Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Named Element</em>'.
	 * @see tools.descartes.librede.configuration.NamedElement
	 * @generated
	 */
	EClass getNamedElement();

	/**
	 * Returns the meta object for the attribute '{@link tools.descartes.librede.configuration.NamedElement#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see tools.descartes.librede.configuration.NamedElement#getName()
	 * @see #getNamedElement()
	 * @generated
	 */
	EAttribute getNamedElement_Name();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.configuration.TraceToEntityMapping <em>Trace To Entity Mapping</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Trace To Entity Mapping</em>'.
	 * @see tools.descartes.librede.configuration.TraceToEntityMapping
	 * @generated
	 */
	EClass getTraceToEntityMapping();

	/**
	 * Returns the meta object for the reference '{@link tools.descartes.librede.configuration.TraceToEntityMapping#getEntity <em>Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Entity</em>'.
	 * @see tools.descartes.librede.configuration.TraceToEntityMapping#getEntity()
	 * @see #getTraceToEntityMapping()
	 * @generated
	 */
	EReference getTraceToEntityMapping_Entity();

	/**
	 * Returns the meta object for the attribute '{@link tools.descartes.librede.configuration.TraceToEntityMapping#getTraceColumn <em>Trace Column</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Trace Column</em>'.
	 * @see tools.descartes.librede.configuration.TraceToEntityMapping#getTraceColumn()
	 * @see #getTraceToEntityMapping()
	 * @generated
	 */
	EAttribute getTraceToEntityMapping_TraceColumn();

	/**
	 * Returns the meta object for the containment reference list '{@link tools.descartes.librede.configuration.TraceToEntityMapping#getFilters <em>Filters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Filters</em>'.
	 * @see tools.descartes.librede.configuration.TraceToEntityMapping#getFilters()
	 * @see #getTraceToEntityMapping()
	 * @generated
	 */
	EReference getTraceToEntityMapping_Filters();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.configuration.ModelEntity <em>Model Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model Entity</em>'.
	 * @see tools.descartes.librede.configuration.ModelEntity
	 * @generated
	 */
	EClass getModelEntity();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.configuration.EstimationAlgorithmConfiguration <em>Estimation Algorithm Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Estimation Algorithm Configuration</em>'.
	 * @see tools.descartes.librede.configuration.EstimationAlgorithmConfiguration
	 * @generated
	 */
	EClass getEstimationAlgorithmConfiguration();

	/**
	 * Returns the meta object for the attribute '{@link tools.descartes.librede.configuration.EstimationAlgorithmConfiguration#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see tools.descartes.librede.configuration.EstimationAlgorithmConfiguration#getType()
	 * @see #getEstimationAlgorithmConfiguration()
	 * @generated
	 */
	EAttribute getEstimationAlgorithmConfiguration_Type();

	/**
	 * Returns the meta object for the containment reference list '{@link tools.descartes.librede.configuration.EstimationAlgorithmConfiguration#getParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameters</em>'.
	 * @see tools.descartes.librede.configuration.EstimationAlgorithmConfiguration#getParameters()
	 * @see #getEstimationAlgorithmConfiguration()
	 * @generated
	 */
	EReference getEstimationAlgorithmConfiguration_Parameters();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.configuration.TraceFilter <em>Trace Filter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Trace Filter</em>'.
	 * @see tools.descartes.librede.configuration.TraceFilter
	 * @generated
	 */
	EClass getTraceFilter();

	/**
	 * Returns the meta object for the attribute '{@link tools.descartes.librede.configuration.TraceFilter#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see tools.descartes.librede.configuration.TraceFilter#getValue()
	 * @see #getTraceFilter()
	 * @generated
	 */
	EAttribute getTraceFilter_Value();

	/**
	 * Returns the meta object for the attribute '{@link tools.descartes.librede.configuration.TraceFilter#getTraceColumn <em>Trace Column</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Trace Column</em>'.
	 * @see tools.descartes.librede.configuration.TraceFilter#getTraceColumn()
	 * @see #getTraceFilter()
	 * @generated
	 */
	EAttribute getTraceFilter_TraceColumn();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.configuration.ResourceDemand <em>Resource Demand</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Resource Demand</em>'.
	 * @see tools.descartes.librede.configuration.ResourceDemand
	 * @generated
	 */
	EClass getResourceDemand();

	/**
	 * Returns the meta object for the reference '{@link tools.descartes.librede.configuration.ResourceDemand#getResource <em>Resource</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Resource</em>'.
	 * @see tools.descartes.librede.configuration.ResourceDemand#getResource()
	 * @see #getResourceDemand()
	 * @generated
	 */
	EReference getResourceDemand_Resource();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.configuration.ExternalCall <em>External Call</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>External Call</em>'.
	 * @see tools.descartes.librede.configuration.ExternalCall
	 * @generated
	 */
	EClass getExternalCall();

	/**
	 * Returns the meta object for the reference '{@link tools.descartes.librede.configuration.ExternalCall#getCalledService <em>Called Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Called Service</em>'.
	 * @see tools.descartes.librede.configuration.ExternalCall#getCalledService()
	 * @see #getExternalCall()
	 * @generated
	 */
	EReference getExternalCall_CalledService();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.configuration.CompositeService <em>Composite Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Composite Service</em>'.
	 * @see tools.descartes.librede.configuration.CompositeService
	 * @generated
	 */
	EClass getCompositeService();

	/**
	 * Returns the meta object for the containment reference list '{@link tools.descartes.librede.configuration.CompositeService#getSubServices <em>Sub Services</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Sub Services</em>'.
	 * @see tools.descartes.librede.configuration.CompositeService#getSubServices()
	 * @see #getCompositeService()
	 * @generated
	 */
	EReference getCompositeService_SubServices();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.configuration.Task <em>Task</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Task</em>'.
	 * @see tools.descartes.librede.configuration.Task
	 * @generated
	 */
	EClass getTask();

	/**
	 * Returns the meta object for the container reference '{@link tools.descartes.librede.configuration.Task#getService <em>Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Service</em>'.
	 * @see tools.descartes.librede.configuration.Task#getService()
	 * @see #getTask()
	 * @generated
	 */
	EReference getTask_Service();

	/**
	 * Returns the meta object for enum '{@link tools.descartes.librede.configuration.SchedulingStrategy <em>Scheduling Strategy</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Scheduling Strategy</em>'.
	 * @see tools.descartes.librede.configuration.SchedulingStrategy
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
		 * The meta object literal for the '{@link tools.descartes.librede.configuration.impl.LibredeConfigurationImpl <em>Librede Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.configuration.impl.LibredeConfigurationImpl
		 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getLibredeConfiguration()
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
		 * The meta object literal for the '{@link tools.descartes.librede.configuration.impl.DataSourceConfigurationImpl <em>Data Source Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.configuration.impl.DataSourceConfigurationImpl
		 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getDataSourceConfiguration()
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
		 * The meta object literal for the '{@link tools.descartes.librede.configuration.impl.WorkloadDescriptionImpl <em>Workload Description</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.configuration.impl.WorkloadDescriptionImpl
		 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getWorkloadDescription()
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
		 * The meta object literal for the '{@link tools.descartes.librede.configuration.impl.InputSpecificationImpl <em>Input Specification</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.configuration.impl.InputSpecificationImpl
		 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getInputSpecification()
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
		 * The meta object literal for the '{@link tools.descartes.librede.configuration.impl.EstimationApproachConfigurationImpl <em>Estimation Approach Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.configuration.impl.EstimationApproachConfigurationImpl
		 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getEstimationApproachConfiguration()
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
		 * The meta object literal for the '{@link tools.descartes.librede.configuration.impl.OutputSpecificationImpl <em>Output Specification</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.configuration.impl.OutputSpecificationImpl
		 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getOutputSpecification()
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
		 * The meta object literal for the '{@link tools.descartes.librede.configuration.impl.ResourceImpl <em>Resource</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.configuration.impl.ResourceImpl
		 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getResource()
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
		 * The meta object literal for the '<em><b>Child Resources</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESOURCE__CHILD_RESOURCES = eINSTANCE.getResource_ChildResources();

		/**
		 * The meta object literal for the '<em><b>Demands</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESOURCE__DEMANDS = eINSTANCE.getResource_Demands();

		/**
		 * The meta object literal for the '<em><b>Accessing Services</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESOURCE__ACCESSING_SERVICES = eINSTANCE.getResource_AccessingServices();

		/**
		 * The meta object literal for the '{@link tools.descartes.librede.configuration.impl.ServiceImpl <em>Service</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.configuration.impl.ServiceImpl
		 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getService()
		 * @generated
		 */
		EClass SERVICE = eINSTANCE.getService();

		/**
		 * The meta object literal for the '<em><b>Background Service</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SERVICE__BACKGROUND_SERVICE = eINSTANCE.getService_BackgroundService();

		/**
		 * The meta object literal for the '<em><b>Tasks</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SERVICE__TASKS = eINSTANCE.getService_Tasks();

		/**
		 * The meta object literal for the '<em><b>Accessed Resources</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SERVICE__ACCESSED_RESOURCES = eINSTANCE.getService_AccessedResources();

		/**
		 * The meta object literal for the '<em><b>Incoming Calls</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SERVICE__INCOMING_CALLS = eINSTANCE.getService_IncomingCalls();

		/**
		 * The meta object literal for the '<em><b>Outgoing Calls</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SERVICE__OUTGOING_CALLS = eINSTANCE.getService_OutgoingCalls();

		/**
		 * The meta object literal for the '<em><b>Resource Demands</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SERVICE__RESOURCE_DEMANDS = eINSTANCE.getService_ResourceDemands();

		/**
		 * The meta object literal for the '{@link tools.descartes.librede.configuration.impl.TraceConfigurationImpl <em>Trace Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.configuration.impl.TraceConfigurationImpl
		 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getTraceConfiguration()
		 * @generated
		 */
		EClass TRACE_CONFIGURATION = eINSTANCE.getTraceConfiguration();

		/**
		 * The meta object literal for the '<em><b>Metric</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRACE_CONFIGURATION__METRIC = eINSTANCE.getTraceConfiguration_Metric();

		/**
		 * The meta object literal for the '<em><b>Unit</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRACE_CONFIGURATION__UNIT = eINSTANCE.getTraceConfiguration_Unit();

		/**
		 * The meta object literal for the '<em><b>Interval</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRACE_CONFIGURATION__INTERVAL = eINSTANCE.getTraceConfiguration_Interval();

		/**
		 * The meta object literal for the '<em><b>Location</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRACE_CONFIGURATION__LOCATION = eINSTANCE.getTraceConfiguration_Location();

		/**
		 * The meta object literal for the '<em><b>Aggregation</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRACE_CONFIGURATION__AGGREGATION = eINSTANCE.getTraceConfiguration_Aggregation();

		/**
		 * The meta object literal for the '<em><b>Data Source</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRACE_CONFIGURATION__DATA_SOURCE = eINSTANCE.getTraceConfiguration_DataSource();

		/**
		 * The meta object literal for the '<em><b>Mappings</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRACE_CONFIGURATION__MAPPINGS = eINSTANCE.getTraceConfiguration_Mappings();

		/**
		 * The meta object literal for the '{@link tools.descartes.librede.configuration.impl.ParameterImpl <em>Parameter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.configuration.impl.ParameterImpl
		 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getParameter()
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
		 * The meta object literal for the '{@link tools.descartes.librede.configuration.impl.EstimationSpecificationImpl <em>Estimation Specification</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.configuration.impl.EstimationSpecificationImpl
		 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getEstimationSpecification()
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
		 * The meta object literal for the '<em><b>Step Size</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESTIMATION_SPECIFICATION__STEP_SIZE = eINSTANCE.getEstimationSpecification_StepSize();

		/**
		 * The meta object literal for the '<em><b>Start Timestamp</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESTIMATION_SPECIFICATION__START_TIMESTAMP = eINSTANCE.getEstimationSpecification_StartTimestamp();

		/**
		 * The meta object literal for the '<em><b>End Timestamp</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESTIMATION_SPECIFICATION__END_TIMESTAMP = eINSTANCE.getEstimationSpecification_EndTimestamp();

		/**
		 * The meta object literal for the '<em><b>Algorithms</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESTIMATION_SPECIFICATION__ALGORITHMS = eINSTANCE.getEstimationSpecification_Algorithms();

		/**
		 * The meta object literal for the '{@link tools.descartes.librede.configuration.impl.ValidationSpecificationImpl <em>Validation Specification</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.configuration.impl.ValidationSpecificationImpl
		 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getValidationSpecification()
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
		 * The meta object literal for the '{@link tools.descartes.librede.configuration.impl.ValidatorConfigurationImpl <em>Validator Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.configuration.impl.ValidatorConfigurationImpl
		 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getValidatorConfiguration()
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
		 * The meta object literal for the '{@link tools.descartes.librede.configuration.impl.ExporterConfigurationImpl <em>Exporter Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.configuration.impl.ExporterConfigurationImpl
		 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getExporterConfiguration()
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
		 * The meta object literal for the '{@link tools.descartes.librede.configuration.impl.FileTraceConfigurationImpl <em>File Trace Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.configuration.impl.FileTraceConfigurationImpl
		 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getFileTraceConfiguration()
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
		 * The meta object literal for the '{@link tools.descartes.librede.configuration.NamedElement <em>Named Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.configuration.NamedElement
		 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getNamedElement()
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
		 * The meta object literal for the '{@link tools.descartes.librede.configuration.impl.TraceToEntityMappingImpl <em>Trace To Entity Mapping</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.configuration.impl.TraceToEntityMappingImpl
		 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getTraceToEntityMapping()
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
		 * The meta object literal for the '<em><b>Filters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRACE_TO_ENTITY_MAPPING__FILTERS = eINSTANCE.getTraceToEntityMapping_Filters();

		/**
		 * The meta object literal for the '{@link tools.descartes.librede.configuration.impl.ModelEntityImpl <em>Model Entity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.configuration.impl.ModelEntityImpl
		 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getModelEntity()
		 * @generated
		 */
		EClass MODEL_ENTITY = eINSTANCE.getModelEntity();

		/**
		 * The meta object literal for the '{@link tools.descartes.librede.configuration.impl.EstimationAlgorithmConfigurationImpl <em>Estimation Algorithm Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.configuration.impl.EstimationAlgorithmConfigurationImpl
		 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getEstimationAlgorithmConfiguration()
		 * @generated
		 */
		EClass ESTIMATION_ALGORITHM_CONFIGURATION = eINSTANCE.getEstimationAlgorithmConfiguration();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESTIMATION_ALGORITHM_CONFIGURATION__TYPE = eINSTANCE.getEstimationAlgorithmConfiguration_Type();

		/**
		 * The meta object literal for the '<em><b>Parameters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESTIMATION_ALGORITHM_CONFIGURATION__PARAMETERS = eINSTANCE.getEstimationAlgorithmConfiguration_Parameters();

		/**
		 * The meta object literal for the '{@link tools.descartes.librede.configuration.impl.TraceFilterImpl <em>Trace Filter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.configuration.impl.TraceFilterImpl
		 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getTraceFilter()
		 * @generated
		 */
		EClass TRACE_FILTER = eINSTANCE.getTraceFilter();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRACE_FILTER__VALUE = eINSTANCE.getTraceFilter_Value();

		/**
		 * The meta object literal for the '<em><b>Trace Column</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRACE_FILTER__TRACE_COLUMN = eINSTANCE.getTraceFilter_TraceColumn();

		/**
		 * The meta object literal for the '{@link tools.descartes.librede.configuration.impl.ResourceDemandImpl <em>Resource Demand</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.configuration.impl.ResourceDemandImpl
		 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getResourceDemand()
		 * @generated
		 */
		EClass RESOURCE_DEMAND = eINSTANCE.getResourceDemand();

		/**
		 * The meta object literal for the '<em><b>Resource</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESOURCE_DEMAND__RESOURCE = eINSTANCE.getResourceDemand_Resource();

		/**
		 * The meta object literal for the '{@link tools.descartes.librede.configuration.impl.ExternalCallImpl <em>External Call</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.configuration.impl.ExternalCallImpl
		 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getExternalCall()
		 * @generated
		 */
		EClass EXTERNAL_CALL = eINSTANCE.getExternalCall();

		/**
		 * The meta object literal for the '<em><b>Called Service</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXTERNAL_CALL__CALLED_SERVICE = eINSTANCE.getExternalCall_CalledService();

		/**
		 * The meta object literal for the '{@link tools.descartes.librede.configuration.impl.CompositeServiceImpl <em>Composite Service</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.configuration.impl.CompositeServiceImpl
		 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getCompositeService()
		 * @generated
		 */
		EClass COMPOSITE_SERVICE = eINSTANCE.getCompositeService();

		/**
		 * The meta object literal for the '<em><b>Sub Services</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPOSITE_SERVICE__SUB_SERVICES = eINSTANCE.getCompositeService_SubServices();

		/**
		 * The meta object literal for the '{@link tools.descartes.librede.configuration.impl.TaskImpl <em>Task</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.configuration.impl.TaskImpl
		 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getTask()
		 * @generated
		 */
		EClass TASK = eINSTANCE.getTask();

		/**
		 * The meta object literal for the '<em><b>Service</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TASK__SERVICE = eINSTANCE.getTask_Service();

		/**
		 * The meta object literal for the '{@link tools.descartes.librede.configuration.SchedulingStrategy <em>Scheduling Strategy</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.configuration.SchedulingStrategy
		 * @see tools.descartes.librede.configuration.impl.ConfigurationPackageImpl#getSchedulingStrategy()
		 * @generated
		 */
		EEnum SCHEDULING_STRATEGY = eINSTANCE.getSchedulingStrategy();

	}

} //ConfigurationPackage
