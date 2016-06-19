/**
 */
package tools.descartes.librede.configuration;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Observation To Entity Mapping</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link tools.descartes.librede.configuration.ObservationToEntityMapping#getEntity <em>Entity</em>}</li>
 * </ul>
 *
 * @see tools.descartes.librede.configuration.ConfigurationPackage#getObservationToEntityMapping()
 * @model
 * @generated
 */
public interface ObservationToEntityMapping extends EObject {
	/**
	 * Returns the value of the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entity</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity</em>' reference.
	 * @see #setEntity(ModelEntity)
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getObservationToEntityMapping_Entity()
	 * @model required="true"
	 * @generated
	 */
	ModelEntity getEntity();

	/**
	 * Sets the value of the '{@link tools.descartes.librede.configuration.ObservationToEntityMapping#getEntity <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entity</em>' reference.
	 * @see #getEntity()
	 * @generated
	 */
	void setEntity(ModelEntity value);

} // ObservationToEntityMapping
