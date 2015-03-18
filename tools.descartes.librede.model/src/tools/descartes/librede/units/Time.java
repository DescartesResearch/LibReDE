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
	public static final Unit<Time> NANOSECONDS = UnitsFactory.eINSTANCE.createUnit(INSTANCE, "nanoseconds", "ns", 1e-9);
	
	/**
	 * @generated NOT
	 */
	public static final Unit<Time> MICROSECONDS = UnitsFactory.eINSTANCE.createUnit(INSTANCE, "microseconds", "\u00B5s", 1e-6);
	
	/**
	 * @generated NOT
	 */
	public static final Unit<Time> MILLISECONDS = UnitsFactory.eINSTANCE.createUnit(INSTANCE, "milliseconds", "ms", 1e-3);
	
	/**
	 * @generated NOT
	 */
	public static final Unit<Time> SECONDS = UnitsFactory.eINSTANCE.createBaseUnit(INSTANCE, "seconds", "s");

	/**
	 * @generated NOT
	 */
	public static final Unit<Time> MINUTES = UnitsFactory.eINSTANCE.createUnit(INSTANCE, "minutes", "min", 60);
	
	/**
	 * @generated NOT
	 */
	public static final Unit<Time> HOURS = UnitsFactory.eINSTANCE.createUnit(INSTANCE, "hours", "h", 60 * 60);
	
	/**
	 * @generated NOT
	 */
	public static final Unit<Time> DAYS = UnitsFactory.eINSTANCE.createUnit(INSTANCE, "days", "d", 60 * 60 * 24);

} // Time
