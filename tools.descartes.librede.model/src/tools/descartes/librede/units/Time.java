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


/**
 * <!-- begin-user-doc -->
 * Units for time quantities (e.g., response time).
 * <!-- end-user-doc -->
 *
 *
 * @see tools.descartes.librede.units.UnitsPackage#getTime()
 * @model
 * @generated
 */
public interface Time extends Dimension {
	
	/**
	 * @generated NOT
	 */
	public static final Time INSTANCE = UnitsFactory.eINSTANCE.createTime();
	
	/**
	 * @generated NOT
	 */
	public static final Unit<Time> NANOSECONDS = UnitsFactory.eINSTANCE.createUnit(INSTANCE, "NANOSECONDS", "nanoseconds", "ns", 1e-9);
	
	/**
	 * @generated NOT
	 */
	public static final Unit<Time> MICROSECONDS = UnitsFactory.eINSTANCE.createUnit(INSTANCE, "MICROSECONDS", "microseconds", "\u00B5s", 1e-6);
	
	/**
	 * @generated NOT
	 */
	public static final Unit<Time> MILLISECONDS = UnitsFactory.eINSTANCE.createUnit(INSTANCE, "MILLISECONDS", "milliseconds", "ms", 1e-3);
	
	/**
	 * @generated NOT
	 */
	public static final Unit<Time> SECONDS = UnitsFactory.eINSTANCE.createBaseUnit(INSTANCE, "SECONDS", "seconds", "s");

	/**
	 * @generated NOT
	 */
	public static final Unit<Time> MINUTES = UnitsFactory.eINSTANCE.createUnit(INSTANCE, "MINUTES", "minutes", "min", 60);
	
	/**
	 * @generated NOT
	 */
	public static final Unit<Time> HOURS = UnitsFactory.eINSTANCE.createUnit(INSTANCE, "HOURS", "hours", "h", 60 * 60);
	
	/**
	 * @generated NOT
	 */
	public static final Unit<Time> DAYS = UnitsFactory.eINSTANCE.createUnit(INSTANCE, "DAYS", "days", "d", 60 * 60 * 24);

} // Time
