/**
 */
package tools.descartes.librede.units;


/**
 * <!-- begin-user-doc -->
 * Unit for a proportion (e.g., utilization).
 * <!-- end-user-doc -->
 *
 *
 * @see tools.descartes.librede.units.UnitsPackage#getProportion()
 * @model
 * @generated
 */
public interface Proportion extends Dimension {

	/**
	 * @generated NOT
	 */
	public static final Proportion INSTANCE = UnitsFactory.eINSTANCE.createProportion();

	/**
	 * @generated NOT
	 */
	public static final Unit<Proportion> NONE = UnitsFactory.eINSTANCE.createBaseUnit(INSTANCE, "none", "");

	/**
	 * @generated NOT
	 */
	public static final Unit<Proportion> PERCENTAGE = UnitsFactory.eINSTANCE.createUnit(INSTANCE, "precentage", "%", 100);
	
} // Proportion
