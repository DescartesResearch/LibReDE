/**
 */
package net.descartesresearch.librede.configuration.impl;

import java.util.Collection;
import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.ExporterConfiguration;
import net.descartesresearch.librede.configuration.OutputSpecification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Output Specification</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.descartesresearch.librede.configuration.impl.OutputSpecificationImpl#getExporters <em>Exporters</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OutputSpecificationImpl extends MinimalEObjectImpl.Container implements OutputSpecification {
	/**
	 * The cached value of the '{@link #getExporters() <em>Exporters</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExporters()
	 * @generated
	 * @ordered
	 */
	protected EList<ExporterConfiguration> exporters;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OutputSpecificationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConfigurationPackage.Literals.OUTPUT_SPECIFICATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ExporterConfiguration> getExporters() {
		if (exporters == null) {
			exporters = new EObjectContainmentEList<ExporterConfiguration>(ExporterConfiguration.class, this, ConfigurationPackage.OUTPUT_SPECIFICATION__EXPORTERS);
		}
		return exporters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ConfigurationPackage.OUTPUT_SPECIFICATION__EXPORTERS:
				return ((InternalEList<?>)getExporters()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ConfigurationPackage.OUTPUT_SPECIFICATION__EXPORTERS:
				return getExporters();
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
			case ConfigurationPackage.OUTPUT_SPECIFICATION__EXPORTERS:
				getExporters().clear();
				getExporters().addAll((Collection<? extends ExporterConfiguration>)newValue);
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
			case ConfigurationPackage.OUTPUT_SPECIFICATION__EXPORTERS:
				getExporters().clear();
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
			case ConfigurationPackage.OUTPUT_SPECIFICATION__EXPORTERS:
				return exporters != null && !exporters.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //OutputSpecificationImpl
