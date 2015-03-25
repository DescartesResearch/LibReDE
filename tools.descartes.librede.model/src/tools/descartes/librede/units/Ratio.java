/**
 */
package tools.descartes.librede.units;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ratio</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see tools.descartes.librede.units.UnitsPackage#getRatio()
 * @model
 * @generated
 */
public interface Ratio extends Dimension {

	/**
	 * @generated NOT
	 */
	public static final Ratio INSTANCE = UnitsFactory.eINSTANCE.createRatio();

	/**
	 * @generated NOT
	 */
	public static final Unit<Ratio> NONE = UnitsFactory.eINSTANCE.createBaseUnit(INSTANCE, "NONE", "no unit", "");

	/**
	 * @generated NOT
	 */
	public static final Unit<Ratio> PERCENTAGE = UnitsFactory.eINSTANCE.createUnit(INSTANCE, "PERCENTAGE", "percentage", "%", 100);
	
} // Ratio
