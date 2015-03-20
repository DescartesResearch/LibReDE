/**
 */
package tools.descartes.librede.units;

import java.lang.Comparable;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Quantity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link tools.descartes.librede.units.Quantity#getValue <em>Value</em>}</li>
 *   <li>{@link tools.descartes.librede.units.Quantity#getUnit <em>Unit</em>}</li>
 * </ul>
 *
 * @see tools.descartes.librede.units.UnitsPackage#getQuantity()
 * @model superTypes="tools.descartes.librede.units.Comparable<tools.descartes.librede.units.Quantity>"
 * @generated
 */
public interface Quantity extends EObject, Comparable<Quantity> {
	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(double)
	 * @see tools.descartes.librede.units.UnitsPackage#getQuantity_Value()
	 * @model required="true"
	 * @generated
	 */
	double getValue();

	/**
	 * Sets the value of the '{@link tools.descartes.librede.units.Quantity#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(double value);

	/**
	 * Returns the value of the '<em><b>Unit</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Unit</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Unit</em>' reference.
	 * @see #setUnit(Unit)
	 * @see tools.descartes.librede.units.UnitsPackage#getQuantity_Unit()
	 * @model required="true"
	 * @generated
	 */
	Unit getUnit();

	/**
	 * Sets the value of the '{@link tools.descartes.librede.units.Quantity#getUnit <em>Unit</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Unit</em>' reference.
	 * @see #getUnit()
	 * @generated
	 */
	void setUnit(Unit value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true" targetUnitRequired="true"
	 * @generated
	 */
	Quantity convertTo(Unit targetUnit);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true" unitRequired="true"
	 * @generated
	 */
	double getValue(Unit unit);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true" quantityRequired="true"
	 * @generated
	 */
	Quantity plus(Quantity quantity);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true" quantityRequired="true"
	 * @generated
	 */
	Quantity minus(Quantity quantity);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true" factorRequired="true"
	 * @generated
	 */
	Quantity times(double factor);

} // Quantity
