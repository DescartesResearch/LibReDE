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


/**
 * <!-- begin-user-doc -->
 * Unit for request rates (e.g., throughput).
 * <!-- end-user-doc -->
 *
 *
 * @see tools.descartes.librede.units.UnitsPackage#getRequestRate()
 * @model
 * @generated
 */
public interface RequestRate extends Dimension {
	
	/**
	 * @generated NOT
	 */
	public static final RequestRate INSTANCE = UnitsFactory.eINSTANCE.createRequestRate();

	/**
	 * @generated NOT
	 */
	public static final Unit<RequestRate> REQ_PER_NANOSECOND = UnitsFactory.eINSTANCE.createUnit(INSTANCE, "REQUESTS_PER_NANOSECOND", "requests per nanosecond", "req/ns", 1e9);

	/**
	 * @generated NOT
	 */
	public static final Unit<RequestRate> REQ_PER_MICROSECOND = UnitsFactory.eINSTANCE.createUnit(INSTANCE, "REQUESTS_PER_MICROSECOND", "requests per microsecond", "req/\u00B5s", 1e6);

	/**
	 * @generated NOT
	 */
	public static final Unit<RequestRate> REQ_PER_MILLISECOND = UnitsFactory.eINSTANCE.createUnit(INSTANCE, "REQUESTS_PER_MILLISECOND", "requests per millisecond", "req/ms", 1e3);

	/**
	 * @generated NOT
	 */
	public static final Unit<RequestRate> REQ_PER_SECOND = UnitsFactory.eINSTANCE.createBaseUnit(INSTANCE, "REQUESTS_PER_SECOND", "requests per second", "req/s");

	/**
	 * @generated NOT
	 */
	public static final Unit<RequestRate> REQ_PER_MINUTE = UnitsFactory.eINSTANCE.createUnit(INSTANCE, "REQUESTS_PER_MINUTE", "requests per minute", "req/min", 1.0 / 60);

	/**
	 * @generated NOT
	 */
	public static final Unit<RequestRate> REQ_PER_HOUR = UnitsFactory.eINSTANCE.createUnit(INSTANCE, "REQUESTS_PER_HOUR", "requests per hour", "req/h", 1.0 / (60 * 60));

	/**
	 * @generated NOT
	 */
	public static final Unit<RequestRate> REQ_PER_DAY = UnitsFactory.eINSTANCE.createUnit(INSTANCE, "REQUESTS_PER_DAY", "requests per day", "req/d", 1.0 / (60 * 60 * 24));
	
} // RequestRate
