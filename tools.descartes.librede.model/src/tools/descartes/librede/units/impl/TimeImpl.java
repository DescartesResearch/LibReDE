/**
 */
package tools.descartes.librede.units.impl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;
import tools.descartes.librede.units.UnitsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Time</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class TimeImpl extends DimensionImpl implements Time {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected TimeImpl() {
		super();
		EList<Unit<?>> units = getUnits();
		units.add(DAYS);
		units.add(HOURS);
		units.add(MINUTES);
		units.add(SECONDS);
		units.add(MILLISECONDS);
		units.add(MICROSECONDS);
		units.add(NANOSECONDS);
		setBaseUnit(SECONDS);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return UnitsPackage.Literals.TIME;
	}

} //TimeImpl
