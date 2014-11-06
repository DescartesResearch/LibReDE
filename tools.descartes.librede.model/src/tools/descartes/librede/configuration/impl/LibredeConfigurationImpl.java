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
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import tools.descartes.librede.configuration.ConfigurationPackage;
import tools.descartes.librede.configuration.EstimationSpecification;
import tools.descartes.librede.configuration.InputSpecification;
import tools.descartes.librede.configuration.LibredeConfiguration;
import tools.descartes.librede.configuration.OutputSpecification;
import tools.descartes.librede.configuration.ValidationSpecification;
import tools.descartes.librede.configuration.WorkloadDescription;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Librede Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link tools.descartes.librede.configuration.impl.LibredeConfigurationImpl#getWorkloadDescription <em>Workload Description</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.LibredeConfigurationImpl#getInput <em>Input</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.LibredeConfigurationImpl#getEstimation <em>Estimation</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.LibredeConfigurationImpl#getOutput <em>Output</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.LibredeConfigurationImpl#getValidation <em>Validation</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LibredeConfigurationImpl extends MinimalEObjectImpl.Container implements LibredeConfiguration {
	/**
	 * The cached value of the '{@link #getWorkloadDescription() <em>Workload Description</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWorkloadDescription()
	 * @generated
	 * @ordered
	 */
	protected WorkloadDescription workloadDescription;

	/**
	 * The cached value of the '{@link #getInput() <em>Input</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInput()
	 * @generated
	 * @ordered
	 */
	protected InputSpecification input;

	/**
	 * The cached value of the '{@link #getEstimation() <em>Estimation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEstimation()
	 * @generated
	 * @ordered
	 */
	protected EstimationSpecification estimation;

	/**
	 * The cached value of the '{@link #getOutput() <em>Output</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutput()
	 * @generated
	 * @ordered
	 */
	protected OutputSpecification output;

	/**
	 * The cached value of the '{@link #getValidation() <em>Validation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValidation()
	 * @generated
	 * @ordered
	 */
	protected ValidationSpecification validation;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LibredeConfigurationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConfigurationPackage.Literals.LIBREDE_CONFIGURATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WorkloadDescription getWorkloadDescription() {
		return workloadDescription;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetWorkloadDescription(WorkloadDescription newWorkloadDescription, NotificationChain msgs) {
		WorkloadDescription oldWorkloadDescription = workloadDescription;
		workloadDescription = newWorkloadDescription;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ConfigurationPackage.LIBREDE_CONFIGURATION__WORKLOAD_DESCRIPTION, oldWorkloadDescription, newWorkloadDescription);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWorkloadDescription(WorkloadDescription newWorkloadDescription) {
		if (newWorkloadDescription != workloadDescription) {
			NotificationChain msgs = null;
			if (workloadDescription != null)
				msgs = ((InternalEObject)workloadDescription).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ConfigurationPackage.LIBREDE_CONFIGURATION__WORKLOAD_DESCRIPTION, null, msgs);
			if (newWorkloadDescription != null)
				msgs = ((InternalEObject)newWorkloadDescription).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ConfigurationPackage.LIBREDE_CONFIGURATION__WORKLOAD_DESCRIPTION, null, msgs);
			msgs = basicSetWorkloadDescription(newWorkloadDescription, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.LIBREDE_CONFIGURATION__WORKLOAD_DESCRIPTION, newWorkloadDescription, newWorkloadDescription));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InputSpecification getInput() {
		return input;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetInput(InputSpecification newInput, NotificationChain msgs) {
		InputSpecification oldInput = input;
		input = newInput;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ConfigurationPackage.LIBREDE_CONFIGURATION__INPUT, oldInput, newInput);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInput(InputSpecification newInput) {
		if (newInput != input) {
			NotificationChain msgs = null;
			if (input != null)
				msgs = ((InternalEObject)input).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ConfigurationPackage.LIBREDE_CONFIGURATION__INPUT, null, msgs);
			if (newInput != null)
				msgs = ((InternalEObject)newInput).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ConfigurationPackage.LIBREDE_CONFIGURATION__INPUT, null, msgs);
			msgs = basicSetInput(newInput, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.LIBREDE_CONFIGURATION__INPUT, newInput, newInput));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EstimationSpecification getEstimation() {
		return estimation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetEstimation(EstimationSpecification newEstimation, NotificationChain msgs) {
		EstimationSpecification oldEstimation = estimation;
		estimation = newEstimation;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ConfigurationPackage.LIBREDE_CONFIGURATION__ESTIMATION, oldEstimation, newEstimation);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEstimation(EstimationSpecification newEstimation) {
		if (newEstimation != estimation) {
			NotificationChain msgs = null;
			if (estimation != null)
				msgs = ((InternalEObject)estimation).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ConfigurationPackage.LIBREDE_CONFIGURATION__ESTIMATION, null, msgs);
			if (newEstimation != null)
				msgs = ((InternalEObject)newEstimation).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ConfigurationPackage.LIBREDE_CONFIGURATION__ESTIMATION, null, msgs);
			msgs = basicSetEstimation(newEstimation, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.LIBREDE_CONFIGURATION__ESTIMATION, newEstimation, newEstimation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OutputSpecification getOutput() {
		return output;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOutput(OutputSpecification newOutput, NotificationChain msgs) {
		OutputSpecification oldOutput = output;
		output = newOutput;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ConfigurationPackage.LIBREDE_CONFIGURATION__OUTPUT, oldOutput, newOutput);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOutput(OutputSpecification newOutput) {
		if (newOutput != output) {
			NotificationChain msgs = null;
			if (output != null)
				msgs = ((InternalEObject)output).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ConfigurationPackage.LIBREDE_CONFIGURATION__OUTPUT, null, msgs);
			if (newOutput != null)
				msgs = ((InternalEObject)newOutput).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ConfigurationPackage.LIBREDE_CONFIGURATION__OUTPUT, null, msgs);
			msgs = basicSetOutput(newOutput, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.LIBREDE_CONFIGURATION__OUTPUT, newOutput, newOutput));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ValidationSpecification getValidation() {
		return validation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetValidation(ValidationSpecification newValidation, NotificationChain msgs) {
		ValidationSpecification oldValidation = validation;
		validation = newValidation;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ConfigurationPackage.LIBREDE_CONFIGURATION__VALIDATION, oldValidation, newValidation);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setValidation(ValidationSpecification newValidation) {
		if (newValidation != validation) {
			NotificationChain msgs = null;
			if (validation != null)
				msgs = ((InternalEObject)validation).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ConfigurationPackage.LIBREDE_CONFIGURATION__VALIDATION, null, msgs);
			if (newValidation != null)
				msgs = ((InternalEObject)newValidation).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ConfigurationPackage.LIBREDE_CONFIGURATION__VALIDATION, null, msgs);
			msgs = basicSetValidation(newValidation, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.LIBREDE_CONFIGURATION__VALIDATION, newValidation, newValidation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ConfigurationPackage.LIBREDE_CONFIGURATION__WORKLOAD_DESCRIPTION:
				return basicSetWorkloadDescription(null, msgs);
			case ConfigurationPackage.LIBREDE_CONFIGURATION__INPUT:
				return basicSetInput(null, msgs);
			case ConfigurationPackage.LIBREDE_CONFIGURATION__ESTIMATION:
				return basicSetEstimation(null, msgs);
			case ConfigurationPackage.LIBREDE_CONFIGURATION__OUTPUT:
				return basicSetOutput(null, msgs);
			case ConfigurationPackage.LIBREDE_CONFIGURATION__VALIDATION:
				return basicSetValidation(null, msgs);
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
			case ConfigurationPackage.LIBREDE_CONFIGURATION__WORKLOAD_DESCRIPTION:
				return getWorkloadDescription();
			case ConfigurationPackage.LIBREDE_CONFIGURATION__INPUT:
				return getInput();
			case ConfigurationPackage.LIBREDE_CONFIGURATION__ESTIMATION:
				return getEstimation();
			case ConfigurationPackage.LIBREDE_CONFIGURATION__OUTPUT:
				return getOutput();
			case ConfigurationPackage.LIBREDE_CONFIGURATION__VALIDATION:
				return getValidation();
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
			case ConfigurationPackage.LIBREDE_CONFIGURATION__WORKLOAD_DESCRIPTION:
				setWorkloadDescription((WorkloadDescription)newValue);
				return;
			case ConfigurationPackage.LIBREDE_CONFIGURATION__INPUT:
				setInput((InputSpecification)newValue);
				return;
			case ConfigurationPackage.LIBREDE_CONFIGURATION__ESTIMATION:
				setEstimation((EstimationSpecification)newValue);
				return;
			case ConfigurationPackage.LIBREDE_CONFIGURATION__OUTPUT:
				setOutput((OutputSpecification)newValue);
				return;
			case ConfigurationPackage.LIBREDE_CONFIGURATION__VALIDATION:
				setValidation((ValidationSpecification)newValue);
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
			case ConfigurationPackage.LIBREDE_CONFIGURATION__WORKLOAD_DESCRIPTION:
				setWorkloadDescription((WorkloadDescription)null);
				return;
			case ConfigurationPackage.LIBREDE_CONFIGURATION__INPUT:
				setInput((InputSpecification)null);
				return;
			case ConfigurationPackage.LIBREDE_CONFIGURATION__ESTIMATION:
				setEstimation((EstimationSpecification)null);
				return;
			case ConfigurationPackage.LIBREDE_CONFIGURATION__OUTPUT:
				setOutput((OutputSpecification)null);
				return;
			case ConfigurationPackage.LIBREDE_CONFIGURATION__VALIDATION:
				setValidation((ValidationSpecification)null);
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
			case ConfigurationPackage.LIBREDE_CONFIGURATION__WORKLOAD_DESCRIPTION:
				return workloadDescription != null;
			case ConfigurationPackage.LIBREDE_CONFIGURATION__INPUT:
				return input != null;
			case ConfigurationPackage.LIBREDE_CONFIGURATION__ESTIMATION:
				return estimation != null;
			case ConfigurationPackage.LIBREDE_CONFIGURATION__OUTPUT:
				return output != null;
			case ConfigurationPackage.LIBREDE_CONFIGURATION__VALIDATION:
				return validation != null;
		}
		return super.eIsSet(featureID);
	}

} //LibredeConfigurationImpl
