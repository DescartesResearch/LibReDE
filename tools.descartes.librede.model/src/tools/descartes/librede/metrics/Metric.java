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
 *   <li>{@link tools.descartes.librede.metrics.Metric#getId <em>Id</em>}</li>
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
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see tools.descartes.librede.metrics.MetricsPackage#getMetric_Id()
	 * @model id="true" required="true"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link tools.descartes.librede.metrics.Metric#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

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
	 * @model required="true" changeable="false"
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

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true" aggregationRequired="true"
	 * @generated
	 */
	boolean isAggregationAllowed(Aggregation aggregation);

} // Metric
