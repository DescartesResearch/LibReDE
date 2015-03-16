/**
 */
package tools.descartes.librede.units;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * This class describes a physical unit of measurement data.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link tools.descartes.librede.units.Unit#getName <em>Name</em>}</li>
 *   <li>{@link tools.descartes.librede.units.Unit#getSymbol <em>Symbol</em>}</li>
 *   <li>{@link tools.descartes.librede.units.Unit#getBaseFactor <em>Base Factor</em>}</li>
 * </ul>
 *
 * @see tools.descartes.librede.units.UnitsPackage#getUnit()
 * @model
 * @generated
 */
public interface Unit<D extends Dimension> extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * A human-readable name of the unit.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see tools.descartes.librede.units.UnitsPackage#getUnit_Name()
	 * @model required="true" changeable="false"
	 * @generated
	 */
	String getName();

	/**
	 * Returns the value of the '<em><b>Symbol</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * The physical symbol of the symbol.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Symbol</em>' attribute.
	 * @see tools.descartes.librede.units.UnitsPackage#getUnit_Symbol()
	 * @model required="true" changeable="false"
	 * @generated
	 */
	String getSymbol();

	/**
	 * Returns the value of the '<em><b>Base Factor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Factor</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Factor</em>' attribute.
	 * @see tools.descartes.librede.units.UnitsPackage#getUnit_BaseFactor()
	 * @model required="true" changeable="false"
	 * @generated
	 */
	double getBaseFactor();

	/**
	 * <!-- begin-user-doc -->
	 * Converts the given value to another unit.
	 * @param value the input value of the source unit.
	 * @param targetUnit the target unit must have the same dimension.
	 * <!-- end-user-doc -->
	 * @model required="true" valueRequired="true" targetUnitRequired="true"
	 * @generated
	 */
	double convertTo(double value, Unit<D> targetUnit);

	/**
	 * <!-- begin-user-doc -->
	 * Converts the given value from another unit.
	 * @param value the input value of the source unit.
	 * @param sourceUnit the source unit must have the same dimension. 
	 * <!-- end-user-doc -->
	 * @model required="true" valueRequired="true" sourceUnitRequired="true"
	 * @generated
	 */
	double convertFrom(double value, Unit<D> sourceUnit);

} // Unit
