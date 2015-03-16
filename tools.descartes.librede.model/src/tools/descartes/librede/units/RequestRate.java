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
	public static final Unit<RequestRate> REQ_PER_NANOSECOND = UnitsFactory.eINSTANCE.createUnit("requests_per_nanosecond", "req/ns", 1e9);

	/**
	 * @generated NOT
	 */
	public static final Unit<RequestRate> REQ_PER_MICROSECOND = UnitsFactory.eINSTANCE.createUnit("requests_per_microsecond", "req/\u00B5s", 1e6);

	/**
	 * @generated NOT
	 */
	public static final Unit<RequestRate> REQ_PER_MILLISECOND = UnitsFactory.eINSTANCE.createUnit("requests_per_millisecond", "req/ms", 1e3);

	/**
	 * @generated NOT
	 */
	public static final Unit<RequestRate> REQ_PER_SECOND = UnitsFactory.eINSTANCE.createUnit("requests_per_second", "req/s", 1);

	/**
	 * @generated NOT
	 */
	public static final Unit<RequestRate> REQ_PER_MINUTE = UnitsFactory.eINSTANCE.createUnit("requests_per_minute", "req/min", 1.0 / 60);

	/**
	 * @generated NOT
	 */
	public static final Unit<RequestRate> REQ_PER_HOUR = UnitsFactory.eINSTANCE.createUnit("requests_per_hour", "req/h", 1.0 / (60 * 60));

	/**
	 * @generated NOT
	 */
	public static final Unit<RequestRate> REQ_PER_DAY = UnitsFactory.eINSTANCE.createUnit("requests_per_day", "req/d", 1.0 / (60 * 60 * 24));
	
} // RequestRate
