/**
 */
package net.descartesresearch.librede.configuration.impl;

import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.ModelEntity;
import net.descartesresearch.librede.configuration.TraceToEntityMapping;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Trace To Entity Mapping</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.descartesresearch.librede.configuration.impl.TraceToEntityMappingImpl#getEntity <em>Entity</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.impl.TraceToEntityMappingImpl#getTraceColumn <em>Trace Column</em>}</li>
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
