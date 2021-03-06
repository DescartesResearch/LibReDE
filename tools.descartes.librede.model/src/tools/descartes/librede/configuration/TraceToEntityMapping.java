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
package tools.descartes.librede.configuration;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Trace To Entity Mapping</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link tools.descartes.librede.configuration.TraceToEntityMapping#getTraceColumn <em>Trace Column</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.TraceToEntityMapping#getFilters <em>Filters</em>}</li>
 * </ul>
 *
 * @see tools.descartes.librede.configuration.ConfigurationPackage#getTraceToEntityMapping()
 * @model
 * @generated
 */
public interface TraceToEntityMapping extends ObservationToEntityMapping {
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
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getTraceToEntityMapping_TraceColumn()
	 * @model default="1"
	 * @generated
	 */
	int getTraceColumn();

	/**
	 * Sets the value of the '{@link tools.descartes.librede.configuration.TraceToEntityMapping#getTraceColumn <em>Trace Column</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Trace Column</em>' attribute.
	 * @see #getTraceColumn()
	 * @generated
	 */
	void setTraceColumn(int value);

	/**
	 * Returns the value of the '<em><b>Filters</b></em>' containment reference list.
	 * The list contents are of type {@link tools.descartes.librede.configuration.TraceFilter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Filters</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Filters</em>' containment reference list.
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getTraceToEntityMapping_Filters()
	 * @model containment="true"
	 * @generated
	 */
	EList<TraceFilter> getFilters();

} // TraceToEntityMapping
