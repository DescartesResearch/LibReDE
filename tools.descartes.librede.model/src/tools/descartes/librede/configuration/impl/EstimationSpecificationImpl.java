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

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import tools.descartes.librede.configuration.ConfigurationPackage;
import tools.descartes.librede.configuration.EstimationAlgorithmConfiguration;
import tools.descartes.librede.configuration.EstimationApproachConfiguration;
import tools.descartes.librede.configuration.EstimationSpecification;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Estimation Specification</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link tools.descartes.librede.configuration.impl.EstimationSpecificationImpl#getApproaches <em>Approaches</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.EstimationSpecificationImpl#isRecursive <em>Recursive</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.EstimationSpecificationImpl#getWindow <em>Window</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.EstimationSpecificationImpl#getAlgorithms <em>Algorithms</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.EstimationSpecificationImpl#getStepSize <em>Step Size</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.EstimationSpecificationImpl#getStartTimestamp <em>Start Timestamp</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.EstimationSpecificationImpl#getEndTimestamp <em>End Timestamp</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.EstimationSpecificationImpl#isAutomaticApproachSelection <em>Automatic Approach Selection</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EstimationSpecificationImpl extends MinimalEObjectImpl.Container implements EstimationSpecification {
	/**
	 * The cached value of the '{@link #getApproaches() <em>Approaches</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getApproaches()
	 * @generated
	 * @ordered
	 */
	protected EList<EstimationApproachConfiguration> approaches;

	/**
	 * The default value of the '{@link #isRecursive() <em>Recursive</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRecursive()
	 * @generated
	 * @ordered
	 */
	protected static final boolean RECURSIVE_EDEFAULT = false;
	/**
	 * The cached value of the '{@link #isRecursive() <em>Recursive</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRecursive()
	 * @generated
	 * @ordered
	 */
	protected boolean recursive = RECURSIVE_EDEFAULT;
	/**
	 * The default value of the '{@link #getWindow() <em>Window</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindow()
	 * @generated
	 * @ordered
	 */
	protected static final int WINDOW_EDEFAULT = 0;
	/**
	 * The cached value of the '{@link #getWindow() <em>Window</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindow()
	 * @generated
	 * @ordered
	 */
	protected int window = WINDOW_EDEFAULT;
	/**
	 * The cached value of the '{@link #getAlgorithms() <em>Algorithms</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAlgorithms()
	 * @generated
	 * @ordered
	 */
	protected EList<EstimationAlgorithmConfiguration> algorithms;

	/**
	 * The cached value of the '{@link #getStepSize() <em>Step Size</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStepSize()
	 * @generated
	 * @ordered
	 */
	protected Quantity<Time> stepSize;

	/**
	 * The cached value of the '{@link #getStartTimestamp() <em>Start Timestamp</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartTimestamp()
	 * @generated
	 * @ordered
	 */
	protected Quantity<Time> startTimestamp;

	/**
	 * The cached value of the '{@link #getEndTimestamp() <em>End Timestamp</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndTimestamp()
	 * @generated
	 * @ordered
	 */
	protected Quantity<Time> endTimestamp;

	/**
	 * The default value of the '{@link #isAutomaticApproachSelection() <em>Automatic Approach Selection</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAutomaticApproachSelection()
	 * @generated
	 * @ordered
	 */
	protected static final boolean AUTOMATIC_APPROACH_SELECTION_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isAutomaticApproachSelection() <em>Automatic Approach Selection</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAutomaticApproachSelection()
	 * @generated
	 * @ordered
	 */
	protected boolean automaticApproachSelection = AUTOMATIC_APPROACH_SELECTION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EstimationSpecificationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConfigurationPackage.Literals.ESTIMATION_SPECIFICATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EstimationApproachConfiguration> getApproaches() {
		if (approaches == null) {
			approaches = new EObjectContainmentEList<EstimationApproachConfiguration>(EstimationApproachConfiguration.class, this, ConfigurationPackage.ESTIMATION_SPECIFICATION__APPROACHES);
		}
		return approaches;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isRecursive() {
		return recursive;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRecursive(boolean newRecursive) {
		boolean oldRecursive = recursive;
		recursive = newRecursive;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.ESTIMATION_SPECIFICATION__RECURSIVE, oldRecursive, recursive));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getWindow() {
		return window;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWindow(int newWindow) {
		int oldWindow = window;
		window = newWindow;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.ESTIMATION_SPECIFICATION__WINDOW, oldWindow, window));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Quantity<Time> getStepSize() {
		return stepSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetStepSize(Quantity<Time> newStepSize, NotificationChain msgs) {
		Quantity<Time> oldStepSize = stepSize;
		stepSize = newStepSize;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ConfigurationPackage.ESTIMATION_SPECIFICATION__STEP_SIZE, oldStepSize, newStepSize);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStepSize(Quantity<Time> newStepSize) {
		if (newStepSize != stepSize) {
			NotificationChain msgs = null;
			if (stepSize != null)
				msgs = ((InternalEObject)stepSize).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ConfigurationPackage.ESTIMATION_SPECIFICATION__STEP_SIZE, null, msgs);
			if (newStepSize != null)
				msgs = ((InternalEObject)newStepSize).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ConfigurationPackage.ESTIMATION_SPECIFICATION__STEP_SIZE, null, msgs);
			msgs = basicSetStepSize(newStepSize, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.ESTIMATION_SPECIFICATION__STEP_SIZE, newStepSize, newStepSize));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Quantity<Time> getStartTimestamp() {
		return startTimestamp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetStartTimestamp(Quantity<Time> newStartTimestamp, NotificationChain msgs) {
		Quantity<Time> oldStartTimestamp = startTimestamp;
		startTimestamp = newStartTimestamp;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ConfigurationPackage.ESTIMATION_SPECIFICATION__START_TIMESTAMP, oldStartTimestamp, newStartTimestamp);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStartTimestamp(Quantity<Time> newStartTimestamp) {
		if (newStartTimestamp != startTimestamp) {
			NotificationChain msgs = null;
			if (startTimestamp != null)
				msgs = ((InternalEObject)startTimestamp).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ConfigurationPackage.ESTIMATION_SPECIFICATION__START_TIMESTAMP, null, msgs);
			if (newStartTimestamp != null)
				msgs = ((InternalEObject)newStartTimestamp).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ConfigurationPackage.ESTIMATION_SPECIFICATION__START_TIMESTAMP, null, msgs);
			msgs = basicSetStartTimestamp(newStartTimestamp, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.ESTIMATION_SPECIFICATION__START_TIMESTAMP, newStartTimestamp, newStartTimestamp));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Quantity<Time> getEndTimestamp() {
		return endTimestamp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetEndTimestamp(Quantity<Time> newEndTimestamp, NotificationChain msgs) {
		Quantity<Time> oldEndTimestamp = endTimestamp;
		endTimestamp = newEndTimestamp;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ConfigurationPackage.ESTIMATION_SPECIFICATION__END_TIMESTAMP, oldEndTimestamp, newEndTimestamp);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEndTimestamp(Quantity<Time> newEndTimestamp) {
		if (newEndTimestamp != endTimestamp) {
			NotificationChain msgs = null;
			if (endTimestamp != null)
				msgs = ((InternalEObject)endTimestamp).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ConfigurationPackage.ESTIMATION_SPECIFICATION__END_TIMESTAMP, null, msgs);
			if (newEndTimestamp != null)
				msgs = ((InternalEObject)newEndTimestamp).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ConfigurationPackage.ESTIMATION_SPECIFICATION__END_TIMESTAMP, null, msgs);
			msgs = basicSetEndTimestamp(newEndTimestamp, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.ESTIMATION_SPECIFICATION__END_TIMESTAMP, newEndTimestamp, newEndTimestamp));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isAutomaticApproachSelection() {
		return automaticApproachSelection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAutomaticApproachSelection(boolean newAutomaticApproachSelection) {
		boolean oldAutomaticApproachSelection = automaticApproachSelection;
		automaticApproachSelection = newAutomaticApproachSelection;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.ESTIMATION_SPECIFICATION__AUTOMATIC_APPROACH_SELECTION, oldAutomaticApproachSelection, automaticApproachSelection));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EstimationAlgorithmConfiguration> getAlgorithms() {
		if (algorithms == null) {
			algorithms = new EObjectContainmentEList<EstimationAlgorithmConfiguration>(EstimationAlgorithmConfiguration.class, this, ConfigurationPackage.ESTIMATION_SPECIFICATION__ALGORITHMS);
		}
		return algorithms;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__APPROACHES:
				return ((InternalEList<?>)getApproaches()).basicRemove(otherEnd, msgs);
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__ALGORITHMS:
				return ((InternalEList<?>)getAlgorithms()).basicRemove(otherEnd, msgs);
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__STEP_SIZE:
				return basicSetStepSize(null, msgs);
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__START_TIMESTAMP:
				return basicSetStartTimestamp(null, msgs);
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__END_TIMESTAMP:
				return basicSetEndTimestamp(null, msgs);
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
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__APPROACHES:
				return getApproaches();
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__RECURSIVE:
				return isRecursive();
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__WINDOW:
				return getWindow();
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__ALGORITHMS:
				return getAlgorithms();
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__STEP_SIZE:
				return getStepSize();
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__START_TIMESTAMP:
				return getStartTimestamp();
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__END_TIMESTAMP:
				return getEndTimestamp();
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__AUTOMATIC_APPROACH_SELECTION:
				return isAutomaticApproachSelection();
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
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__APPROACHES:
				getApproaches().clear();
				getApproaches().addAll((Collection<? extends EstimationApproachConfiguration>)newValue);
				return;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__RECURSIVE:
				setRecursive((Boolean)newValue);
				return;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__WINDOW:
				setWindow((Integer)newValue);
				return;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__ALGORITHMS:
				getAlgorithms().clear();
				getAlgorithms().addAll((Collection<? extends EstimationAlgorithmConfiguration>)newValue);
				return;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__STEP_SIZE:
				setStepSize((Quantity<Time>)newValue);
				return;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__START_TIMESTAMP:
				setStartTimestamp((Quantity<Time>)newValue);
				return;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__END_TIMESTAMP:
				setEndTimestamp((Quantity<Time>)newValue);
				return;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__AUTOMATIC_APPROACH_SELECTION:
				setAutomaticApproachSelection((Boolean)newValue);
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
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__APPROACHES:
				getApproaches().clear();
				return;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__RECURSIVE:
				setRecursive(RECURSIVE_EDEFAULT);
				return;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__WINDOW:
				setWindow(WINDOW_EDEFAULT);
				return;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__ALGORITHMS:
				getAlgorithms().clear();
				return;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__STEP_SIZE:
				setStepSize((Quantity<Time>)null);
				return;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__START_TIMESTAMP:
				setStartTimestamp((Quantity<Time>)null);
				return;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__END_TIMESTAMP:
				setEndTimestamp((Quantity<Time>)null);
				return;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__AUTOMATIC_APPROACH_SELECTION:
				setAutomaticApproachSelection(AUTOMATIC_APPROACH_SELECTION_EDEFAULT);
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
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__APPROACHES:
				return approaches != null && !approaches.isEmpty();
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__RECURSIVE:
				return recursive != RECURSIVE_EDEFAULT;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__WINDOW:
				return window != WINDOW_EDEFAULT;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__ALGORITHMS:
				return algorithms != null && !algorithms.isEmpty();
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__STEP_SIZE:
				return stepSize != null;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__START_TIMESTAMP:
				return startTimestamp != null;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__END_TIMESTAMP:
				return endTimestamp != null;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__AUTOMATIC_APPROACH_SELECTION:
				return automaticApproachSelection != AUTOMATIC_APPROACH_SELECTION_EDEFAULT;
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
		result.append(" (recursive: ");
		result.append(recursive);
		result.append(", window: ");
		result.append(window);
		result.append(", automaticApproachSelection: ");
		result.append(automaticApproachSelection);
		result.append(')');
		return result.toString();
	}

} //EstimationSpecificationImpl
