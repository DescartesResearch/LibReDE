/**
 */
package tools.descartes.librede.units;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Repository</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link tools.descartes.librede.units.UnitsRepository#getDimensions <em>Dimensions</em>}</li>
 * </ul>
 *
 * @see tools.descartes.librede.units.UnitsPackage#getUnitsRepository()
 * @model
 * @generated
 */
public interface UnitsRepository extends EObject {
	/**
	 * Returns the value of the '<em><b>Dimensions</b></em>' containment reference list.
	 * The list contents are of type {@link tools.descartes.librede.units.Dimension}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dimensions</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dimensions</em>' containment reference list.
	 * @see tools.descartes.librede.units.UnitsPackage#getUnitsRepository_Dimensions()
	 * @model containment="true"
	 * @generated
	 */
	EList<Dimension> getDimensions();

} // UnitsRepository
