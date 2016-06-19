/**
 */
package tools.descartes.librede.configuration;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;

import tools.descartes.librede.units.Dimension;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Observation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link tools.descartes.librede.configuration.Observation#getAggregation <em>Aggregation</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.Observation#getMetric <em>Metric</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.Observation#getMappings <em>Mappings</em>}</li>
 * </ul>
 *
 * @see tools.descartes.librede.configuration.ConfigurationPackage#getObservation()
 * @model abstract="true"
 * @generated
 */
public interface Observation<M extends ObservationToEntityMapping> extends EObject {
	/**
	 * Returns the value of the '<em><b>Aggregation</b></em>' attribute.
	 * The literals are from the enumeration {@link tools.descartes.librede.metrics.Aggregation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Aggregation</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Aggregation</em>' attribute.
	 * @see tools.descartes.librede.metrics.Aggregation
	 * @see #setAggregation(Aggregation)
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getObservation_Aggregation()
	 * @model
	 * @generated
	 */
	Aggregation getAggregation();

	/**
	 * Sets the value of the '{@link tools.descartes.librede.configuration.Observation#getAggregation <em>Aggregation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Aggregation</em>' attribute.
	 * @see tools.descartes.librede.metrics.Aggregation
	 * @see #getAggregation()
	 * @generated
	 */
	void setAggregation(Aggregation value);

	/**
	 * Returns the value of the '<em><b>Metric</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Metric</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Metric</em>' reference.
	 * @see #setMetric(Metric)
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getObservation_Metric()
	 * @model required="true"
	 * @generated
	 */
	Metric<? extends Dimension> getMetric();

	/**
	 * Sets the value of the '{@link tools.descartes.librede.configuration.Observation#getMetric <em>Metric</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Metric</em>' reference.
	 * @see #getMetric()
	 * @generated
	 */
	void setMetric(Metric<? extends Dimension> value);

	/**
	 * Returns the value of the '<em><b>Mappings</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mappings</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mappings</em>' containment reference list.
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getObservation_Mappings()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EList<M> getMappings();

} // Observation
