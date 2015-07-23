/**
 */
package tools.descartes.librede.configuration;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link tools.descartes.librede.configuration.Task#getService <em>Service</em>}</li>
 * </ul>
 *
 * @see tools.descartes.librede.configuration.ConfigurationPackage#getTask()
 * @model abstract="true"
 * @generated
 */
public interface Task extends ModelEntity {

	/**
	 * Returns the value of the '<em><b>Service</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Service</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Service</em>' reference.
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getTask_Service()
	 * @model required="true" transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	Service getService();
} // Task
