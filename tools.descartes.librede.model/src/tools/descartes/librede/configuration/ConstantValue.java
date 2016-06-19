/**
 */
package tools.descartes.librede.configuration;

import tools.descartes.librede.units.Quantity;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Constant Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link tools.descartes.librede.configuration.ConstantValue#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see tools.descartes.librede.configuration.ConfigurationPackage#getConstantValue()
 * @model
 * @generated
 */
public interface ConstantValue extends Observation<ObservationToEntityMapping> {
	/**
	 * Returns the value of the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' containment reference.
	 * @see #setValue(Quantity)
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getConstantValue_Value()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Quantity<?> getValue();

	/**
	 * Sets the value of the '{@link tools.descartes.librede.configuration.ConstantValue#getValue <em>Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' containment reference.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(Quantity<?> value);

} // ConstantValue
