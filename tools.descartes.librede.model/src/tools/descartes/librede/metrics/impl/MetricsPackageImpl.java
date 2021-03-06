/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2018, by Simon Spinner, Johannes Grohmann
 *  and Contributors.
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
package tools.descartes.librede.metrics.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import tools.descartes.librede.configuration.ConfigurationPackage;
import tools.descartes.librede.configuration.impl.ConfigurationPackageImpl;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.metrics.MetricsFactory;
import tools.descartes.librede.metrics.MetricsPackage;
import tools.descartes.librede.metrics.MetricsRepository;
import tools.descartes.librede.units.UnitsPackage;
import tools.descartes.librede.units.impl.UnitsPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MetricsPackageImpl extends EPackageImpl implements MetricsPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass metricEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass metricsRepositoryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum aggregationEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see tools.descartes.librede.metrics.MetricsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private MetricsPackageImpl() {
		super(eNS_URI, MetricsFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link MetricsPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static MetricsPackage init() {
		if (isInited) return (MetricsPackage)EPackage.Registry.INSTANCE.getEPackage(MetricsPackage.eNS_URI);

		// Obtain or create and register package
		MetricsPackageImpl theMetricsPackage = (MetricsPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof MetricsPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new MetricsPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		ConfigurationPackageImpl theConfigurationPackage = (ConfigurationPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(ConfigurationPackage.eNS_URI) instanceof ConfigurationPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ConfigurationPackage.eNS_URI) : ConfigurationPackage.eINSTANCE);
		UnitsPackageImpl theUnitsPackage = (UnitsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(UnitsPackage.eNS_URI) instanceof UnitsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(UnitsPackage.eNS_URI) : UnitsPackage.eINSTANCE);

		// Create package meta-data objects
		theMetricsPackage.createPackageContents();
		theConfigurationPackage.createPackageContents();
		theUnitsPackage.createPackageContents();

		// Initialize created meta-data
		theMetricsPackage.initializePackageContents();
		theConfigurationPackage.initializePackageContents();
		theUnitsPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theMetricsPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(MetricsPackage.eNS_URI, theMetricsPackage);
		return theMetricsPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMetric() {
		return metricEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMetric_Id() {
		return (EAttribute)metricEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMetric_Name() {
		return (EAttribute)metricEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMetric_Dimension() {
		return (EReference)metricEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMetric_AllowedAggregations() {
		return (EAttribute)metricEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getMetric__IsAggregationAllowed__Aggregation() {
		return metricEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMetricsRepository() {
		return metricsRepositoryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMetricsRepository_Metrics() {
		return (EReference)metricsRepositoryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getAggregation() {
		return aggregationEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MetricsFactory getMetricsFactory() {
		return (MetricsFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		metricEClass = createEClass(METRIC);
		createEAttribute(metricEClass, METRIC__ID);
		createEAttribute(metricEClass, METRIC__NAME);
		createEReference(metricEClass, METRIC__DIMENSION);
		createEAttribute(metricEClass, METRIC__ALLOWED_AGGREGATIONS);
		createEOperation(metricEClass, METRIC___IS_AGGREGATION_ALLOWED__AGGREGATION);

		metricsRepositoryEClass = createEClass(METRICS_REPOSITORY);
		createEReference(metricsRepositoryEClass, METRICS_REPOSITORY__METRICS);

		// Create enums
		aggregationEEnum = createEEnum(AGGREGATION);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		UnitsPackage theUnitsPackage = (UnitsPackage)EPackage.Registry.INSTANCE.getEPackage(UnitsPackage.eNS_URI);

		// Create type parameters
		ETypeParameter metricEClass_D = addETypeParameter(metricEClass, "D");

		// Set bounds for type parameters
		EGenericType g1 = createEGenericType(theUnitsPackage.getDimension());
		metricEClass_D.getEBounds().add(g1);

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		initEClass(metricEClass, Metric.class, "Metric", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMetric_Id(), ecorePackage.getEString(), "id", null, 1, 1, Metric.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetric_Name(), ecorePackage.getEString(), "name", null, 1, 1, Metric.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(metricEClass_D);
		initEReference(getMetric_Dimension(), g1, null, "dimension", null, 1, 1, Metric.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetric_AllowedAggregations(), this.getAggregation(), "allowedAggregations", null, 1, -1, Metric.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = initEOperation(getMetric__IsAggregationAllowed__Aggregation(), ecorePackage.getEBoolean(), "isAggregationAllowed", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getAggregation(), "aggregation", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(metricsRepositoryEClass, MetricsRepository.class, "MetricsRepository", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(this.getMetric());
		EGenericType g2 = createEGenericType();
		g1.getETypeArguments().add(g2);
		initEReference(getMetricsRepository_Metrics(), g1, null, "metrics", null, 0, -1, MetricsRepository.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(aggregationEEnum, Aggregation.class, "Aggregation");
		addEEnumLiteral(aggregationEEnum, Aggregation.NONE);
		addEEnumLiteral(aggregationEEnum, Aggregation.AVERAGE);
		addEEnumLiteral(aggregationEEnum, Aggregation.MAXIMUM);
		addEEnumLiteral(aggregationEEnum, Aggregation.MINIMUM);
		addEEnumLiteral(aggregationEEnum, Aggregation.SUM);
		addEEnumLiteral(aggregationEEnum, Aggregation.CUMULATIVE_SUM);

		// Create resource
		createResource(eNS_URI);
	}

} //MetricsPackageImpl
