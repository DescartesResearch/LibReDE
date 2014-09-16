/**
 */
package net.descartesresearch.librede.configuration;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Trace To Entity Mapping</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.descartesresearch.librede.configuration.TraceToEntityMapping#getEntity <em>Entity</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.TraceToEntityMapping#getTraceColumn <em>Trace Column</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getTraceToEntityMapping()
 * @model
 * @generated
 */
public interface TraceToEntityMapping extends EObject {
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
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getTraceToEntityMapping_Entity()
	 * @model required="true"
	 * @generated
	 */
	ModelEntity getEntity();

	/**
	 * Sets the value of the '{@link net.descartesresearch.librede.configuration.TraceToEntityMapping#getEntity <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entity</em>' reference.
	 * @see #getEntity()
	 * @generated
	 */
	void setEntity(ModelEntity value);

	/**
	 * Returns the value of the '<em><b>Trace Column</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Trace Column</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Trace Column</em>' attribute.
	 * @see #setTraceColumn(int)
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getTraceToEntityMapping_TraceColumn()
	 * @model default="1"
	 * @generated
	 */
	int getTraceColumn();

	/**
	 * Sets the value of the '{@link net.descartesresearch.librede.configuration.TraceToEntityMapping#getTraceColumn <em>Trace Column</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Trace Column</em>' attribute.
	 * @see #getTraceColumn()
	 * @generated
	 */
	void setTraceColumn(int value);

} // TraceToEntityMapping
