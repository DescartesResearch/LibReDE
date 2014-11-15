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
package tools.descartes.librede.configuration.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

import tools.descartes.librede.configuration.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see tools.descartes.librede.configuration.ConfigurationPackage
 * @generated
 */
public class ConfigurationAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static ConfigurationPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConfigurationAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = ConfigurationPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ConfigurationSwitch<Adapter> modelSwitch =
		new ConfigurationSwitch<Adapter>() {
			@Override
			public Adapter caseLibredeConfiguration(LibredeConfiguration object) {
				return createLibredeConfigurationAdapter();
			}
			@Override
			public Adapter caseDataSourceConfiguration(DataSourceConfiguration object) {
				return createDataSourceConfigurationAdapter();
			}
			@Override
			public Adapter caseWorkloadDescription(WorkloadDescription object) {
				return createWorkloadDescriptionAdapter();
			}
			@Override
			public Adapter caseInputSpecification(InputSpecification object) {
				return createInputSpecificationAdapter();
			}
			@Override
			public Adapter caseEstimationApproachConfiguration(EstimationApproachConfiguration object) {
				return createEstimationApproachConfigurationAdapter();
			}
			@Override
			public Adapter caseOutputSpecification(OutputSpecification object) {
				return createOutputSpecificationAdapter();
			}
			@Override
			public Adapter caseResource(Resource object) {
				return createResourceAdapter();
			}
			@Override
			public Adapter caseService(Service object) {
				return createServiceAdapter();
			}
			@Override
			public Adapter caseTraceConfiguration(TraceConfiguration object) {
				return createTraceConfigurationAdapter();
			}
			@Override
			public Adapter caseParameter(Parameter object) {
				return createParameterAdapter();
			}
			@Override
			public Adapter caseEstimationSpecification(EstimationSpecification object) {
				return createEstimationSpecificationAdapter();
			}
			@Override
			public Adapter caseValidationSpecification(ValidationSpecification object) {
				return createValidationSpecificationAdapter();
			}
			@Override
			public Adapter caseValidatorConfiguration(ValidatorConfiguration object) {
				return createValidatorConfigurationAdapter();
			}
			@Override
			public Adapter caseExporterConfiguration(ExporterConfiguration object) {
				return createExporterConfigurationAdapter();
			}
			@Override
			public Adapter caseFileTraceConfiguration(FileTraceConfiguration object) {
				return createFileTraceConfigurationAdapter();
			}
			@Override
			public Adapter caseNamedElement(NamedElement object) {
				return createNamedElementAdapter();
			}
			@Override
			public Adapter caseTraceToEntityMapping(TraceToEntityMapping object) {
				return createTraceToEntityMappingAdapter();
			}
			@Override
			public Adapter caseModelEntity(ModelEntity object) {
				return createModelEntityAdapter();
			}
			@Override
			public Adapter caseEstimationAlgorithmConfiguration(EstimationAlgorithmConfiguration object) {
				return createEstimationAlgorithmConfigurationAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link tools.descartes.librede.configuration.LibredeConfiguration <em>Librede Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tools.descartes.librede.configuration.LibredeConfiguration
	 * @generated
	 */
	public Adapter createLibredeConfigurationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tools.descartes.librede.configuration.DataSourceConfiguration <em>Data Source Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tools.descartes.librede.configuration.DataSourceConfiguration
	 * @generated
	 */
	public Adapter createDataSourceConfigurationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tools.descartes.librede.configuration.WorkloadDescription <em>Workload Description</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tools.descartes.librede.configuration.WorkloadDescription
	 * @generated
	 */
	public Adapter createWorkloadDescriptionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tools.descartes.librede.configuration.InputSpecification <em>Input Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tools.descartes.librede.configuration.InputSpecification
	 * @generated
	 */
	public Adapter createInputSpecificationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tools.descartes.librede.configuration.EstimationApproachConfiguration <em>Estimation Approach Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tools.descartes.librede.configuration.EstimationApproachConfiguration
	 * @generated
	 */
	public Adapter createEstimationApproachConfigurationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tools.descartes.librede.configuration.OutputSpecification <em>Output Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tools.descartes.librede.configuration.OutputSpecification
	 * @generated
	 */
	public Adapter createOutputSpecificationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tools.descartes.librede.configuration.Resource <em>Resource</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tools.descartes.librede.configuration.Resource
	 * @generated
	 */
	public Adapter createResourceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tools.descartes.librede.configuration.Service <em>Service</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tools.descartes.librede.configuration.Service
	 * @generated
	 */
	public Adapter createServiceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tools.descartes.librede.configuration.TraceConfiguration <em>Trace Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tools.descartes.librede.configuration.TraceConfiguration
	 * @generated
	 */
	public Adapter createTraceConfigurationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tools.descartes.librede.configuration.Parameter <em>Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tools.descartes.librede.configuration.Parameter
	 * @generated
	 */
	public Adapter createParameterAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tools.descartes.librede.configuration.EstimationSpecification <em>Estimation Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tools.descartes.librede.configuration.EstimationSpecification
	 * @generated
	 */
	public Adapter createEstimationSpecificationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tools.descartes.librede.configuration.ValidationSpecification <em>Validation Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tools.descartes.librede.configuration.ValidationSpecification
	 * @generated
	 */
	public Adapter createValidationSpecificationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tools.descartes.librede.configuration.ValidatorConfiguration <em>Validator Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tools.descartes.librede.configuration.ValidatorConfiguration
	 * @generated
	 */
	public Adapter createValidatorConfigurationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tools.descartes.librede.configuration.ExporterConfiguration <em>Exporter Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tools.descartes.librede.configuration.ExporterConfiguration
	 * @generated
	 */
	public Adapter createExporterConfigurationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tools.descartes.librede.configuration.FileTraceConfiguration <em>File Trace Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tools.descartes.librede.configuration.FileTraceConfiguration
	 * @generated
	 */
	public Adapter createFileTraceConfigurationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tools.descartes.librede.configuration.NamedElement <em>Named Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tools.descartes.librede.configuration.NamedElement
	 * @generated
	 */
	public Adapter createNamedElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tools.descartes.librede.configuration.TraceToEntityMapping <em>Trace To Entity Mapping</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tools.descartes.librede.configuration.TraceToEntityMapping
	 * @generated
	 */
	public Adapter createTraceToEntityMappingAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tools.descartes.librede.configuration.ModelEntity <em>Model Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tools.descartes.librede.configuration.ModelEntity
	 * @generated
	 */
	public Adapter createModelEntityAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tools.descartes.librede.configuration.EstimationAlgorithmConfiguration <em>Estimation Algorithm Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tools.descartes.librede.configuration.EstimationAlgorithmConfiguration
	 * @generated
	 */
	public Adapter createEstimationAlgorithmConfigurationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //ConfigurationAdapterFactory
