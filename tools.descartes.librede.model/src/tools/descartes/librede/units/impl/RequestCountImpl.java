/**
 */
package tools.descartes.librede.units.impl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import tools.descartes.librede.units.RequestCount;
import tools.descartes.librede.units.Unit;
import tools.descartes.librede.units.UnitsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Request Count</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class RequestCountImpl extends DimensionImpl implements RequestCount {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected RequestCountImpl() {
		super();
		EList<Unit<?>> units = getUnits();
		units.add(REQUESTS);
		setBaseUnit(REQUESTS);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return UnitsPackage.Literals.REQUEST_COUNT;
	}

} //RequestCountImpl
