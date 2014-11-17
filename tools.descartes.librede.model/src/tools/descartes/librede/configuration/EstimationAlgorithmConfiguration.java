/**
 */
package tools.descartes.librede.configuration;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Estimation Algorithm Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link tools.descartes.librede.configuration.EstimationAlgorithmConfiguration#getType <em>Type</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.EstimationAlgorithmConfiguration#getParameters <em>Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @see tools.descartes.librede.configuration.ConfigurationPackage#getEstimationAlgorithmConfiguration()
 * @model
 * @generated
 */
public interface EstimationAlgorithmConfiguration extends EObject {
	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getEstimationAlgorithmConfiguration_Type()
	 * @model required="true"
	 * @generated
	 */
	String getType();

	/**
	 * Sets the value of the '{@link tools.descartes.librede.configuration.EstimationAlgorithmConfiguration#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);

	/**
	 * Returns the value of the '<em><b>Parameters</b></em>' containment reference list.
	 * The list contents are of type {@link tools.descartes.librede.configuration.Parameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameters</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameters</em>' containment reference list.
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getEstimationAlgorithmConfiguration_Parameters()
	 * @model containment="true"
	 * @generated
	 */
	EList<Parameter> getParameters();

} // EstimationAlgorithmConfiguration
