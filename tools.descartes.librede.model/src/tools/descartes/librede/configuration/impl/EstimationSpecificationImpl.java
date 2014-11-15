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

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Estimation Specification</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link tools.descartes.librede.configuration.impl.EstimationSpecificationImpl#getApproaches <em>Approaches</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.EstimationSpecificationImpl#isRecursive <em>Recursive</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.EstimationSpecificationImpl#getWindow <em>Window</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.EstimationSpecificationImpl#getStepSize <em>Step Size</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.EstimationSpecificationImpl#getStartTimestamp <em>Start Timestamp</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.EstimationSpecificationImpl#getEndTimestamp <em>End Timestamp</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.impl.EstimationSpecificationImpl#getAlgorithms <em>Algorithms</em>}</li>
 * </ul>
 * </p>
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
	 * The default value of the '{@link #getStepSize() <em>Step Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStepSize()
	 * @generated
	 * @ordered
	 */
	protected static final long STEP_SIZE_EDEFAULT = 1000L;
	/**
	 * The cached value of the '{@link #getStepSize() <em>Step Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStepSize()
	 * @generated
	 * @ordered
	 */
	protected long stepSize = STEP_SIZE_EDEFAULT;
	/**
	 * The default value of the '{@link #getStartTimestamp() <em>Start Timestamp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartTimestamp()
	 * @generated
	 * @ordered
	 */
	protected static final long START_TIMESTAMP_EDEFAULT = 0L;
	/**
	 * The cached value of the '{@link #getStartTimestamp() <em>Start Timestamp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartTimestamp()
	 * @generated
	 * @ordered
	 */
	protected long startTimestamp = START_TIMESTAMP_EDEFAULT;
	/**
	 * The default value of the '{@link #getEndTimestamp() <em>End Timestamp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndTimestamp()
	 * @generated
	 * @ordered
	 */
	protected static final long END_TIMESTAMP_EDEFAULT = 0L;
	/**
	 * The cached value of the '{@link #getEndTimestamp() <em>End Timestamp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndTimestamp()
	 * @generated
	 * @ordered
	 */
	protected long endTimestamp = END_TIMESTAMP_EDEFAULT;

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
	public long getStepSize() {
		return stepSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStepSize(long newStepSize) {
		long oldStepSize = stepSize;
		stepSize = newStepSize;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.ESTIMATION_SPECIFICATION__STEP_SIZE, oldStepSize, stepSize));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getStartTimestamp() {
		return startTimestamp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStartTimestamp(long newStartTimestamp) {
		long oldStartTimestamp = startTimestamp;
		startTimestamp = newStartTimestamp;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.ESTIMATION_SPECIFICATION__START_TIMESTAMP, oldStartTimestamp, startTimestamp));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getEndTimestamp() {
		return endTimestamp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEndTimestamp(long newEndTimestamp) {
		long oldEndTimestamp = endTimestamp;
		endTimestamp = newEndTimestamp;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.ESTIMATION_SPECIFICATION__END_TIMESTAMP, oldEndTimestamp, endTimestamp));
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
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__STEP_SIZE:
				return getStepSize();
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__START_TIMESTAMP:
				return getStartTimestamp();
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__END_TIMESTAMP:
				return getEndTimestamp();
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__ALGORITHMS:
				return getAlgorithms();
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
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__STEP_SIZE:
				setStepSize((Long)newValue);
				return;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__START_TIMESTAMP:
				setStartTimestamp((Long)newValue);
				return;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__END_TIMESTAMP:
				setEndTimestamp((Long)newValue);
				return;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__ALGORITHMS:
				getAlgorithms().clear();
				getAlgorithms().addAll((Collection<? extends EstimationAlgorithmConfiguration>)newValue);
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
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__STEP_SIZE:
				setStepSize(STEP_SIZE_EDEFAULT);
				return;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__START_TIMESTAMP:
				setStartTimestamp(START_TIMESTAMP_EDEFAULT);
				return;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__END_TIMESTAMP:
				setEndTimestamp(END_TIMESTAMP_EDEFAULT);
				return;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__ALGORITHMS:
				getAlgorithms().clear();
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
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__STEP_SIZE:
				return stepSize != STEP_SIZE_EDEFAULT;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__START_TIMESTAMP:
				return startTimestamp != START_TIMESTAMP_EDEFAULT;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__END_TIMESTAMP:
				return endTimestamp != END_TIMESTAMP_EDEFAULT;
			case ConfigurationPackage.ESTIMATION_SPECIFICATION__ALGORITHMS:
				return algorithms != null && !algorithms.isEmpty();
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
		result.append(", stepSize: ");
		result.append(stepSize);
		result.append(", startTimestamp: ");
		result.append(startTimestamp);
		result.append(", endTimestamp: ");
		result.append(endTimestamp);
		result.append(')');
		return result.toString();
	}

} //EstimationSpecificationImpl
