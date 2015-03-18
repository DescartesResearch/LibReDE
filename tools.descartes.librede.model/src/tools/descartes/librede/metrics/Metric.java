/**
 */
package tools.descartes.librede.metrics;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import tools.descartes.librede.units.Dimension;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Metric</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link tools.descartes.librede.metrics.Metric#getName <em>Name</em>}</li>
 *   <li>{@link tools.descartes.librede.metrics.Metric#getDimension <em>Dimension</em>}</li>
 *   <li>{@link tools.descartes.librede.metrics.Metric#getAllowedAggregations <em>Allowed Aggregations</em>}</li>
 * </ul>
 *
 * @see tools.descartes.librede.metrics.MetricsPackage#getMetric()
 * @model
 * @generated
 */
public interface Metric extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see tools.descartes.librede.metrics.MetricsPackage#getMetric_Name()
	 * @model id="true" required="true" changeable="false"
	 * @generated
	 */
	String getName();

	/**
	 * Returns the value of the '<em><b>Dimension</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dimension</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dimension</em>' reference.
	 * @see tools.descartes.librede.metrics.MetricsPackage#getMetric_Dimension()
	 * @model required="true" changeable="false"
	 * @generated
	 */
	Dimension getDimension();

	/**
	 * Returns the value of the '<em><b>Allowed Aggregations</b></em>' attribute list.
	 * The list contents are of type {@link tools.descartes.librede.metrics.Aggregation}.
	 * The literals are from the enumeration {@link tools.descartes.librede.metrics.Aggregation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Allowed Aggregations</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Allowed Aggregations</em>' attribute list.
	 * @see tools.descartes.librede.metrics.Aggregation
	 * @see tools.descartes.librede.metrics.MetricsPackage#getMetric_AllowedAggregations()
	 * @model required="true"
	 * @generated
	 */
	EList<Aggregation> getAllowedAggregations();

} // Metric
