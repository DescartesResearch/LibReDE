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
package tools.descartes.librede.metrics;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
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
 * @see tools.descartes.librede.metrics.MetricsFactory
 * @model kind="package"
 * @generated
 */
public interface MetricsPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "metrics";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.descartes-research.net/librede/metrics/1.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "librede-metrics";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MetricsPackage eINSTANCE = tools.descartes.librede.metrics.impl.MetricsPackageImpl.init();

	/**
	 * The meta object id for the '{@link tools.descartes.librede.metrics.impl.MetricImpl <em>Metric</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.metrics.impl.MetricImpl
	 * @see tools.descartes.librede.metrics.impl.MetricsPackageImpl#getMetric()
	 * @generated
	 */
	int METRIC = 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METRIC__ID = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METRIC__NAME = 1;

	/**
	 * The feature id for the '<em><b>Dimension</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METRIC__DIMENSION = 2;

	/**
	 * The feature id for the '<em><b>Allowed Aggregations</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METRIC__ALLOWED_AGGREGATIONS = 3;

	/**
	 * The number of structural features of the '<em>Metric</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METRIC_FEATURE_COUNT = 4;

	/**
	 * The operation id for the '<em>Is Aggregation Allowed</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METRIC___IS_AGGREGATION_ALLOWED__AGGREGATION = 0;

	/**
	 * The number of operations of the '<em>Metric</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METRIC_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.metrics.impl.MetricsRepositoryImpl <em>Repository</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.metrics.impl.MetricsRepositoryImpl
	 * @see tools.descartes.librede.metrics.impl.MetricsPackageImpl#getMetricsRepository()
	 * @generated
	 */
	int METRICS_REPOSITORY = 1;

	/**
	 * The feature id for the '<em><b>Metrics</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METRICS_REPOSITORY__METRICS = 0;

	/**
	 * The number of structural features of the '<em>Repository</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METRICS_REPOSITORY_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Repository</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METRICS_REPOSITORY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link tools.descartes.librede.metrics.Aggregation <em>Aggregation</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.descartes.librede.metrics.Aggregation
	 * @see tools.descartes.librede.metrics.impl.MetricsPackageImpl#getAggregation()
	 * @generated
	 */
	int AGGREGATION = 2;


	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.metrics.Metric <em>Metric</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Metric</em>'.
	 * @see tools.descartes.librede.metrics.Metric
	 * @generated
	 */
	EClass getMetric();

	/**
	 * Returns the meta object for the attribute '{@link tools.descartes.librede.metrics.Metric#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see tools.descartes.librede.metrics.Metric#getId()
	 * @see #getMetric()
	 * @generated
	 */
	EAttribute getMetric_Id();

	/**
	 * Returns the meta object for the attribute '{@link tools.descartes.librede.metrics.Metric#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see tools.descartes.librede.metrics.Metric#getName()
	 * @see #getMetric()
	 * @generated
	 */
	EAttribute getMetric_Name();

	/**
	 * Returns the meta object for the reference '{@link tools.descartes.librede.metrics.Metric#getDimension <em>Dimension</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Dimension</em>'.
	 * @see tools.descartes.librede.metrics.Metric#getDimension()
	 * @see #getMetric()
	 * @generated
	 */
	EReference getMetric_Dimension();

	/**
	 * Returns the meta object for the attribute list '{@link tools.descartes.librede.metrics.Metric#getAllowedAggregations <em>Allowed Aggregations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Allowed Aggregations</em>'.
	 * @see tools.descartes.librede.metrics.Metric#getAllowedAggregations()
	 * @see #getMetric()
	 * @generated
	 */
	EAttribute getMetric_AllowedAggregations();

	/**
	 * Returns the meta object for the '{@link tools.descartes.librede.metrics.Metric#isAggregationAllowed(tools.descartes.librede.metrics.Aggregation) <em>Is Aggregation Allowed</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Is Aggregation Allowed</em>' operation.
	 * @see tools.descartes.librede.metrics.Metric#isAggregationAllowed(tools.descartes.librede.metrics.Aggregation)
	 * @generated
	 */
	EOperation getMetric__IsAggregationAllowed__Aggregation();

	/**
	 * Returns the meta object for class '{@link tools.descartes.librede.metrics.MetricsRepository <em>Repository</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Repository</em>'.
	 * @see tools.descartes.librede.metrics.MetricsRepository
	 * @generated
	 */
	EClass getMetricsRepository();

	/**
	 * Returns the meta object for the containment reference list '{@link tools.descartes.librede.metrics.MetricsRepository#getMetrics <em>Metrics</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Metrics</em>'.
	 * @see tools.descartes.librede.metrics.MetricsRepository#getMetrics()
	 * @see #getMetricsRepository()
	 * @generated
	 */
	EReference getMetricsRepository_Metrics();

	/**
	 * Returns the meta object for enum '{@link tools.descartes.librede.metrics.Aggregation <em>Aggregation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Aggregation</em>'.
	 * @see tools.descartes.librede.metrics.Aggregation
	 * @generated
	 */
	EEnum getAggregation();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	MetricsFactory getMetricsFactory();

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
		 * The meta object literal for the '{@link tools.descartes.librede.metrics.impl.MetricImpl <em>Metric</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.metrics.impl.MetricImpl
		 * @see tools.descartes.librede.metrics.impl.MetricsPackageImpl#getMetric()
		 * @generated
		 */
		EClass METRIC = eINSTANCE.getMetric();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METRIC__ID = eINSTANCE.getMetric_Id();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METRIC__NAME = eINSTANCE.getMetric_Name();

		/**
		 * The meta object literal for the '<em><b>Dimension</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference METRIC__DIMENSION = eINSTANCE.getMetric_Dimension();

		/**
		 * The meta object literal for the '<em><b>Allowed Aggregations</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METRIC__ALLOWED_AGGREGATIONS = eINSTANCE.getMetric_AllowedAggregations();

		/**
		 * The meta object literal for the '<em><b>Is Aggregation Allowed</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation METRIC___IS_AGGREGATION_ALLOWED__AGGREGATION = eINSTANCE.getMetric__IsAggregationAllowed__Aggregation();

		/**
		 * The meta object literal for the '{@link tools.descartes.librede.metrics.impl.MetricsRepositoryImpl <em>Repository</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.metrics.impl.MetricsRepositoryImpl
		 * @see tools.descartes.librede.metrics.impl.MetricsPackageImpl#getMetricsRepository()
		 * @generated
		 */
		EClass METRICS_REPOSITORY = eINSTANCE.getMetricsRepository();

		/**
		 * The meta object literal for the '<em><b>Metrics</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference METRICS_REPOSITORY__METRICS = eINSTANCE.getMetricsRepository_Metrics();

		/**
		 * The meta object literal for the '{@link tools.descartes.librede.metrics.Aggregation <em>Aggregation</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see tools.descartes.librede.metrics.Aggregation
		 * @see tools.descartes.librede.metrics.impl.MetricsPackageImpl#getAggregation()
		 * @generated
		 */
		EEnum AGGREGATION = eINSTANCE.getAggregation();

	}

} //MetricsPackage
