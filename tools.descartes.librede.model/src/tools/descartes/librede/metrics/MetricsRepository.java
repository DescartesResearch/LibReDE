/**
 */
package tools.descartes.librede.metrics;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Repository</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link tools.descartes.librede.metrics.MetricsRepository#getMetrics <em>Metrics</em>}</li>
 * </ul>
 *
 * @see tools.descartes.librede.metrics.MetricsPackage#getMetricsRepository()
 * @model
 * @generated
 */
public interface MetricsRepository extends EObject {
	/**
	 * Returns the value of the '<em><b>Metrics</b></em>' containment reference list.
	 * The list contents are of type {@link tools.descartes.librede.metrics.Metric}&lt;?>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Metrics</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Metrics</em>' containment reference list.
	 * @see tools.descartes.librede.metrics.MetricsPackage#getMetricsRepository_Metrics()
	 * @model containment="true"
	 * @generated
	 */
	EList<Metric<?>> getMetrics();

} // MetricsRepository
