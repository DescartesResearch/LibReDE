/**
 */
package tools.descartes.librede.units.impl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import tools.descartes.librede.units.Proportion;
import tools.descartes.librede.units.Unit;
import tools.descartes.librede.units.UnitsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Proportion</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class ProportionImpl extends DimensionImpl implements Proportion {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected ProportionImpl() {
		super();
		EList<Unit<?>> units = getUnits();
		units.add(NONE);
		units.add(PERCENTAGE);
		setBaseUnit(NONE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return UnitsPackage.Literals.PROPORTION;
	}

} //ProportionImpl