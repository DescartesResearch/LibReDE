/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2018, by Simon Spinner, Johannes Grohmann
 *  and Contributors.
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
 *   <li>{@link tools.descartes.librede.units.Unit#getId <em>Id</em>}</li>
 *   <li>{@link tools.descartes.librede.units.Unit#getName <em>Name</em>}</li>
 *   <li>{@link tools.descartes.librede.units.Unit#getSymbol <em>Symbol</em>}</li>
 *   <li>{@link tools.descartes.librede.units.Unit#getBaseFactor <em>Base Factor</em>}</li>
 *   <li>{@link tools.descartes.librede.units.Unit#getDimension <em>Dimension</em>}</li>
 * </ul>
 *
 * @see tools.descartes.librede.units.UnitsPackage#getUnit()
 * @model
 * @generated
 */
public interface Unit<D extends Dimension> extends EObject {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see tools.descartes.librede.units.UnitsPackage#getUnit_Id()
	 * @model id="true" required="true"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link tools.descartes.librede.units.Unit#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

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
	 * Returns the value of the '<em><b>Dimension</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link tools.descartes.librede.units.Dimension#getUnits <em>Units</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dimension</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dimension</em>' container reference.
	 * @see #setDimension(Dimension)
	 * @see tools.descartes.librede.units.UnitsPackage#getUnit_Dimension()
	 * @see tools.descartes.librede.units.Dimension#getUnits
	 * @model opposite="units" required="true" transient="false"
	 * @generated
	 */
	D getDimension();

	/**
	 * Sets the value of the '{@link tools.descartes.librede.units.Unit#getDimension <em>Dimension</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dimension</em>' container reference.
	 * @see #getDimension()
	 * @generated
	 */
	void setDimension(D value);

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
