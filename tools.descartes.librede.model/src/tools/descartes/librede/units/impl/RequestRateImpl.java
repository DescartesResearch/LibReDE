/**
 */
package tools.descartes.librede.units.impl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import tools.descartes.librede.units.RequestRate;
import tools.descartes.librede.units.Unit;
import tools.descartes.librede.units.UnitsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Request Rate</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class RequestRateImpl extends DimensionImpl implements RequestRate {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected RequestRateImpl() {
		super();
		EList<Unit<?>> units = getUnits();
		units.add(REQ_PER_DAY);
		units.add(REQ_PER_HOUR);
		units.add(REQ_PER_MINUTE);
		units.add(REQ_PER_SECOND);
		units.add(REQ_PER_MILLISECOND);
		units.add(REQ_PER_MICROSECOND);
		units.add(REQ_PER_NANOSECOND);
		setBaseUnit(REQ_PER_SECOND);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return UnitsPackage.Literals.REQUEST_RATE;
	}

} //RequestRateImpl
