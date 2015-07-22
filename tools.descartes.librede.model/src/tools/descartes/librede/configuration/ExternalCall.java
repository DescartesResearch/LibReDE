/**
 */
package tools.descartes.librede.configuration;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>External Call</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link tools.descartes.librede.configuration.ExternalCall#getCalledService <em>Called Service</em>}</li>
 * </ul>
 *
 * @see tools.descartes.librede.configuration.ConfigurationPackage#getExternalCall()
 * @model
 * @generated
 */
public interface ExternalCall extends Task {
	/**
	 * Returns the value of the '<em><b>Called Service</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Called Service</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Called Service</em>' reference.
	 * @see #setCalledService(Service)
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getExternalCall_CalledService()
	 * @model required="true"
	 * @generated
	 */
	Service getCalledService();

	/**
	 * Sets the value of the '{@link tools.descartes.librede.configuration.ExternalCall#getCalledService <em>Called Service</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Called Service</em>' reference.
	 * @see #getCalledService()
	 * @generated
	 */
	void setCalledService(Service value);

} // ExternalCall
