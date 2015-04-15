/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2014, by Simon Spinner and Contributors.
 *
 * Project Info:   http://www.descartes-research.net/
 *
 * All rights reserved. This software is made available under the terms of the
 * Eclipse Public License (EPL) v1.0 as published by the Eclipse Foundation
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Eclipse Public License (EPL)
 * for more details.
 *
 * You should have received a copy of the Eclipse Public License (EPL)
 * along with this software; if not visit http://www.eclipse.org or write to
 * Eclipse Foundation, Inc., 308 SW First Avenue, Suite 110, Portland, 97204 USA
 * Email: license (at) eclipse.org
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 */
/**
 */
package tools.descartes.librede.units;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * Each physical unit has a dimension (e.g., time). Different units of the same
 * dimension can be converted to each other.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link tools.descartes.librede.units.Dimension#getBaseUnit <em>Base Unit</em>}</li>
 *   <li>{@link tools.descartes.librede.units.Dimension#getUnits <em>Units</em>}</li>
 * </ul>
 *
 * @see tools.descartes.librede.units.UnitsPackage#getDimension()
 * @model abstract="true"
 * @generated
 */
public interface Dimension extends EObject {
	/**
	 * Returns the value of the '<em><b>Base Unit</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * The default unit of this dimension.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Unit</em>' reference.
	 * @see #setBaseUnit(Unit)
	 * @see tools.descartes.librede.units.UnitsPackage#getDimension_BaseUnit()
	 * @model required="true"
	 * @generated
	 */
	Unit<?> getBaseUnit();

	/**
	 * Sets the value of the '{@link tools.descartes.librede.units.Dimension#getBaseUnit <em>Base Unit</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Unit</em>' reference.
	 * @see #getBaseUnit()
	 * @generated
	 */
	void setBaseUnit(Unit<?> value);

	/**
	 * Returns the value of the '<em><b>Units</b></em>' containment reference list.
	 * The list contents are of type {@link tools.descartes.librede.units.Unit}&lt;?>.
	 * It is bidirectional and its opposite is '{@link tools.descartes.librede.units.Unit#getDimension <em>Dimension</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * A list of all defined units of this dimension.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Units</em>' containment reference list.
	 * @see tools.descartes.librede.units.UnitsPackage#getDimension_Units()
	 * @see tools.descartes.librede.units.Unit#getDimension
	 * @model opposite="dimension" containment="true" required="true"
	 * @generated
	 */
	EList<Unit<?>> getUnits();

} // Dimension
