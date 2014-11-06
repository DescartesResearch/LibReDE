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
package tools.descartes.librede.configuration.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import tools.descartes.librede.configuration.ConfigurationPackage;
import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.TraceToEntityMapping;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Trace To Entity Mapping</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link tools.descartes.librede.configuration.impl.TraceToEntityMappingImpl#getEntity <em>Entity</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.TraceToEntityMappingImpl#getTraceColumn <em>Trace Column</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TraceToEntityMappingImpl extends MinimalEObjectImpl.Container implements TraceToEntityMapping {
	/**
	 * The cached value of the '{@link #getEntity() <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntity()
	 * @generated
	 * @ordered
	 */
	protected ModelEntity entity;

	/**
	 * The default value of the '{@link #getTraceColumn() <em>Trace Column</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTraceColumn()
	 * @generated
	 * @ordered
	 */
	protected static final int TRACE_COLUMN_EDEFAULT = 1;

	/**
	 * The cached value of the '{@link #getTraceColumn() <em>Trace Column</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTraceColumn()
	 * @generated
	 * @ordered
	 */
	protected int traceColumn = TRACE_COLUMN_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TraceToEntityMappingImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConfigurationPackage.Literals.TRACE_TO_ENTITY_MAPPING;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelEntity getEntity() {
		if (entity != null && entity.eIsProxy()) {
			InternalEObject oldEntity = (InternalEObject)entity;
			entity = (ModelEntity)eResolveProxy(oldEntity);
			if (entity != oldEntity) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ConfigurationPackage.TRACE_TO_ENTITY_MAPPING__ENTITY, oldEntity, entity));
			}
		}
		return entity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelEntity basicGetEntity() {
		return entity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEntity(ModelEntity newEntity) {
		ModelEntity oldEntity = entity;
		entity = newEntity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.TRACE_TO_ENTITY_MAPPING__ENTITY, oldEntity, entity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getTraceColumn() {
		return traceColumn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTraceColumn(int newTraceColumn) {
		int oldTraceColumn = traceColumn;
		traceColumn = newTraceColumn;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.TRACE_TO_ENTITY_MAPPING__TRACE_COLUMN, oldTraceColumn, traceColumn));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ConfigurationPackage.TRACE_TO_ENTITY_MAPPING__ENTITY:
				if (resolve) return getEntity();
				return basicGetEntity();
			case ConfigurationPackage.TRACE_TO_ENTITY_MAPPING__TRACE_COLUMN:
				return getTraceColumn();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ConfigurationPackage.TRACE_TO_ENTITY_MAPPING__ENTITY:
				setEntity((ModelEntity)newValue);
				return;
			case ConfigurationPackage.TRACE_TO_ENTITY_MAPPING__TRACE_COLUMN:
				setTraceColumn((Integer)newValue);
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
			case ConfigurationPackage.TRACE_TO_ENTITY_MAPPING__ENTITY:
				setEntity((ModelEntity)null);
				return;
			case ConfigurationPackage.TRACE_TO_ENTITY_MAPPING__TRACE_COLUMN:
				setTraceColumn(TRACE_COLUMN_EDEFAULT);
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
			case ConfigurationPackage.TRACE_TO_ENTITY_MAPPING__ENTITY:
				return entity != null;
			case ConfigurationPackage.TRACE_TO_ENTITY_MAPPING__TRACE_COLUMN:
				return traceColumn != TRACE_COLUMN_EDEFAULT;
		}
		return super.eIsSet(featureID);
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
		result.append(" (traceColumn: ");
		result.append(traceColumn);
		result.append(')');
		return result.toString();
	}

} //TraceToEntityMappingImpl
