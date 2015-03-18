/**
 */
package tools.descartes.librede.units;


/**
 * <!-- begin-user-doc -->
 * Unit for number of requests (e.g., arrival or departure count).
 * <!-- end-user-doc -->
 *
 *
 * @see tools.descartes.librede.units.UnitsPackage#getRequestCount()
 * @model
 * @generated
 */
public interface RequestCount extends Dimension {
	
	/**
	 * @generated NOT
	 */
	public static final RequestCount INSTANCE = UnitsFactory.eINSTANCE.createRequestCount();

	/**
	 * @generated NOT
	 */
	public static final Unit<RequestCount> REQUESTS = UnitsFactory.eINSTANCE.createBaseUnit(INSTANCE, "requests", "req");

} // RequestCount
