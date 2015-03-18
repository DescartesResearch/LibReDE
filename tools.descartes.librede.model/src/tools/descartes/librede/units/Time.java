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
	public static final Unit NANOSECONDS = UnitsFactory.eINSTANCE.createUnit(INSTANCE, "NANOSECONDS", "nanoseconds", "ns", 1e-9);
	
	/**
	 * @generated NOT
	 */
	public static final Unit MICROSECONDS = UnitsFactory.eINSTANCE.createUnit(INSTANCE, "MICROSECONDS", "microseconds", "\u00B5s", 1e-6);
	
	/**
	 * @generated NOT
	 */
	public static final Unit MILLISECONDS = UnitsFactory.eINSTANCE.createUnit(INSTANCE, "MILLISECONDS", "milliseconds", "ms", 1e-3);
	
	/**
	 * @generated NOT
	 */
	public static final Unit SECONDS = UnitsFactory.eINSTANCE.createBaseUnit(INSTANCE, "SECONDS", "seconds", "s");

	/**
	 * @generated NOT
	 */
	public static final Unit MINUTES = UnitsFactory.eINSTANCE.createUnit(INSTANCE, "MINUTES", "minutes", "min", 60);
	
	/**
	 * @generated NOT
	 */
	public static final Unit HOURS = UnitsFactory.eINSTANCE.createUnit(INSTANCE, "HOURS", "hours", "h", 60 * 60);
	
	/**
	 * @generated NOT
	 */
	public static final Unit DAYS = UnitsFactory.eINSTANCE.createUnit(INSTANCE, "DAYS", "days", "d", 60 * 60 * 24);

} // Time
