/**
 */
package tools.descartes.librede.units;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see tools.descartes.librede.units.UnitsPackage
 * @generated
 */
public interface UnitsFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	UnitsFactory eINSTANCE = tools.descartes.librede.units.impl.UnitsFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Unit</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Unit</em>'.
	 * @generated
	 */
	<D extends Dimension> Unit<D> createUnit();

	/**
	 * Returns a new object of class '<em>Unit</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Unit</em>'.
	 * @generated NOT
	 */
	<D extends Dimension> Unit<D> createUnit(D dimension, String id, String name, String symbol, double baseFactor);
	
	/**
	 * Returns a new object of class '<em>Unit</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Unit</em>'.
	 * @generated NOT
	 */
	<D extends Dimension> Unit<D> createBaseUnit(D dimension, String id, String name, String symbol);

	/**
	 * Returns a new object of class '<em>Request Rate</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Request Rate</em>'.
	 * @generated
	 */
	RequestRate createRequestRate();

	/**
	 * Returns a new object of class '<em>Time</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Time</em>'.
	 * @generated
	 */
	Time createTime();

	/**
	 * Returns a new object of class '<em>Request Count</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Request Count</em>'.
	 * @generated
	 */
	RequestCount createRequestCount();

	/**
	 * Returns a new object of class '<em>Ratio</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Ratio</em>'.
	 * @generated
	 */
	Ratio createRatio();

	/**
	 * Returns a new object of class '<em>Repository</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Repository</em>'.
	 * @generated
	 */
	UnitsRepository createUnitsRepository();

	/**
	 * Returns a new object of class '<em>Quantity</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Quantity</em>'.
	 * @generated
	 */
	<D extends Dimension> Quantity<D> createQuantity();
	
	/**
	 * Returns a new object of class '<em>Quantity</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Quantity</em>'.
	 * @generated NOT
	 */
	<D extends Dimension> Quantity<D> createQuantity(double value, Unit<D> unit);

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	UnitsPackage getUnitsPackage();

} //UnitsFactory
