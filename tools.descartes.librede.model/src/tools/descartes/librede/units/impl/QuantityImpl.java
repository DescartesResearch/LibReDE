/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2014, by Simon Spinner and Contributors.
 *
 * Project Info:   http://www.descartes-research.net/
 *
 * All rights reserved. This software is made available under the terms of the
 * Eclipse Public License (EPL) v1.0 as published by the Eclipse Foundation
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Eclipse Public License (EPL)
 * for more details.
 *
 * You should have received a copy of the Eclipse Public License (EPL)
 * along with this software; if not visit http://www.eclipse.org or write to
 * Eclipse Foundation, Inc., 308 SW First Avenue, Suite 110, Portland, 97204 USA
 * Email: license (at) eclipse.org
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 */
/**
 */
package tools.descartes.librede.units.impl;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import tools.descartes.librede.units.Dimension;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Unit;
import tools.descartes.librede.units.UnitsFactory;
import tools.descartes.librede.units.UnitsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Quantity</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link tools.descartes.librede.units.impl.QuantityImpl#getValue <em>Value</em>}</li>
 *   <li>{@link tools.descartes.librede.units.impl.QuantityImpl#getUnit <em>Unit</em>}</li>
 * </ul>
 *
 * @generated
 */
public class QuantityImpl<D extends Dimension> extends MinimalEObjectImpl.Container implements Quantity<D> {
	/**
	 * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected static final double VALUE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected double value = VALUE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getUnit() <em>Unit</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnit()
	 * @generated
	 * @ordered
	 */
	protected Unit<D> unit;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected QuantityImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return UnitsPackage.Literals.QUANTITY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setValue(double newValue) {
		double oldValue = value;
		value = newValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UnitsPackage.QUANTITY__VALUE, oldValue, value));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public Unit<D> getUnit() {
		if (unit != null && unit.eIsProxy()) {
			InternalEObject oldUnit = (InternalEObject)unit;
			unit = (Unit<D>)eResolveProxy(oldUnit);
			if (unit != oldUnit) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, UnitsPackage.QUANTITY__UNIT, oldUnit, unit));
			}
		}
		return unit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Unit<D> basicGetUnit() {
		return unit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUnit(Unit<D> newUnit) {
		Unit<D> oldUnit = unit;
		unit = newUnit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UnitsPackage.QUANTITY__UNIT, oldUnit, unit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Quantity<D> convertTo(Unit<D> targetUnit) {
		return UnitsFactory.eINSTANCE.createQuantity(unit.convertTo(value, targetUnit), targetUnit);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public double getValue(Unit<D> unit) {
		return this.unit.convertTo(value, unit);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Quantity<D> plus(Quantity<D> quantity) {
		return UnitsFactory.eINSTANCE.createQuantity(value + quantity.getValue(unit), unit);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Quantity<D> minus(Quantity<D> quantity) {
		return UnitsFactory.eINSTANCE.createQuantity(value - quantity.getValue(unit), unit);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Quantity<D> times(double factor) {
		return UnitsFactory.eINSTANCE.createQuantity(value * factor, unit);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case UnitsPackage.QUANTITY__VALUE:
				return getValue();
			case UnitsPackage.QUANTITY__UNIT:
				if (resolve) return getUnit();
				return basicGetUnit();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case UnitsPackage.QUANTITY__VALUE:
				setValue((Double)newValue);
				return;
			case UnitsPackage.QUANTITY__UNIT:
				setUnit((Unit<D>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case UnitsPackage.QUANTITY__VALUE:
				setValue(VALUE_EDEFAULT);
				return;
			case UnitsPackage.QUANTITY__UNIT:
				setUnit((Unit<D>)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case UnitsPackage.QUANTITY__VALUE:
				return value != VALUE_EDEFAULT;
			case UnitsPackage.QUANTITY__UNIT:
				return unit != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case UnitsPackage.QUANTITY___CONVERT_TO__UNIT:
				return convertTo((Unit<D>)arguments.get(0));
			case UnitsPackage.QUANTITY___GET_VALUE__UNIT:
				return getValue((Unit<D>)arguments.get(0));
			case UnitsPackage.QUANTITY___PLUS__QUANTITY:
				return plus((Quantity<D>)arguments.get(0));
			case UnitsPackage.QUANTITY___MINUS__QUANTITY:
				return minus((Quantity<D>)arguments.get(0));
			case UnitsPackage.QUANTITY___TIMES__DOUBLE:
				return times((Double)arguments.get(0));
		}
		return super.eInvoke(operationID, arguments);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (value: ");
		result.append(value);
		result.append(')');
		return result.toString();
	}

	@Override
	public int compareTo(Quantity<D> o) {
		return Double.compare(value, o.getValue(unit));
	}

} //QuantityImpl
