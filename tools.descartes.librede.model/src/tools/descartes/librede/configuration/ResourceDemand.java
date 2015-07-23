/**
 */
package tools.descartes.librede.configuration;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Resource Demand</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link tools.descartes.librede.configuration.ResourceDemand#getResource <em>Resource</em>}</li>
 * </ul>
 *
 * @see tools.descartes.librede.configuration.ConfigurationPackage#getResourceDemand()
 * @model
 * @generated
 */
public interface ResourceDemand extends Task {
	/**
	 * Returns the value of the '<em><b>Resource</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link tools.descartes.librede.configuration.Resource#getDemands <em>Demands</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Resource</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Resource</em>' reference.
	 * @see #setResource(Resource)
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getResourceDemand_Resource()
	 * @see tools.descartes.librede.configuration.Resource#getDemands
	 * @model opposite="demands" required="true"
	 * @generated
	 */
	Resource getResource();

	/**
	 * Sets the value of the '{@link tools.descartes.librede.configuration.ResourceDemand#getResource <em>Resource</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Resource</em>' reference.
	 * @see #getResource()
	 * @generated
	 */
	void setResource(Resource value);

} // ResourceDemand
