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
package tools.descartes.librede.configuration;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Estimation Specification</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link tools.descartes.librede.configuration.EstimationSpecification#getApproaches <em>Approaches</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.EstimationSpecification#isRecursive <em>Recursive</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.EstimationSpecification#getWindow <em>Window</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.EstimationSpecification#getStepSize <em>Step Size</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.EstimationSpecification#getStartTimestamp <em>Start Timestamp</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.EstimationSpecification#getEndTimestamp <em>End Timestamp</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.EstimationSpecification#getAlgorithms <em>Algorithms</em>}</li>
 * </ul>
 * </p>
 *
 * @see tools.descartes.librede.configuration.ConfigurationPackage#getEstimationSpecification()
 * @model
 * @generated
 */
public interface EstimationSpecification extends EObject {
	/**
	 * Returns the value of the '<em><b>Approaches</b></em>' containment reference list.
	 * The list contents are of type {@link tools.descartes.librede.configuration.EstimationApproachConfiguration}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Approaches</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Approaches</em>' containment reference list.
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getEstimationSpecification_Approaches()
	 * @model containment="true"
	 * @generated
	 */
	EList<EstimationApproachConfiguration> getApproaches();

	/**
	 * Returns the value of the '<em><b>Recursive</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Recursive</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Recursive</em>' attribute.
	 * @see #setRecursive(boolean)
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getEstimationSpecification_Recursive()
	 * @model default="false" required="true"
	 * @generated
	 */
	boolean isRecursive();

	/**
	 * Sets the value of the '{@link tools.descartes.librede.configuration.EstimationSpecification#isRecursive <em>Recursive</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Recursive</em>' attribute.
	 * @see #isRecursive()
	 * @generated
	 */
	void setRecursive(boolean value);

	/**
	 * Returns the value of the '<em><b>Window</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Window</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Window</em>' attribute.
	 * @see #setWindow(int)
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getEstimationSpecification_Window()
	 * @model required="true"
	 * @generated
	 */
	int getWindow();

	/**
	 * Sets the value of the '{@link tools.descartes.librede.configuration.EstimationSpecification#getWindow <em>Window</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Window</em>' attribute.
	 * @see #getWindow()
	 * @generated
	 */
	void setWindow(int value);

	/**
	 * Returns the value of the '<em><b>Step Size</b></em>' attribute.
	 * The default value is <code>"1000"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Step Size</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Step Size</em>' attribute.
	 * @see #setStepSize(long)
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getEstimationSpecification_StepSize()
	 * @model default="1000" required="true"
	 * @generated
	 */
	long getStepSize();

	/**
	 * Sets the value of the '{@link tools.descartes.librede.configuration.EstimationSpecification#getStepSize <em>Step Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Step Size</em>' attribute.
	 * @see #getStepSize()
	 * @generated
	 */
	void setStepSize(long value);

	/**
	 * Returns the value of the '<em><b>Start Timestamp</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Timestamp</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Timestamp</em>' attribute.
	 * @see #setStartTimestamp(long)
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getEstimationSpecification_StartTimestamp()
	 * @model default="0" required="true"
	 * @generated
	 */
	long getStartTimestamp();

	/**
	 * Sets the value of the '{@link tools.descartes.librede.configuration.EstimationSpecification#getStartTimestamp <em>Start Timestamp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Timestamp</em>' attribute.
	 * @see #getStartTimestamp()
	 * @generated
	 */
	void setStartTimestamp(long value);

	/**
	 * Returns the value of the '<em><b>End Timestamp</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Timestamp</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Timestamp</em>' attribute.
	 * @see #setEndTimestamp(long)
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getEstimationSpecification_EndTimestamp()
	 * @model default="0" required="true"
	 * @generated
	 */
	long getEndTimestamp();

	/**
	 * Sets the value of the '{@link tools.descartes.librede.configuration.EstimationSpecification#getEndTimestamp <em>End Timestamp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Timestamp</em>' attribute.
	 * @see #getEndTimestamp()
	 * @generated
	 */
	void setEndTimestamp(long value);

	/**
	 * Returns the value of the '<em><b>Algorithms</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Algorithms</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Algorithms</em>' containment reference.
	 * @see #setAlgorithms(EstimationAlgorithmConfiguration)
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getEstimationSpecification_Algorithms()
	 * @model containment="true"
	 * @generated
	 */
	EstimationAlgorithmConfiguration getAlgorithms();

	/**
	 * Sets the value of the '{@link tools.descartes.librede.configuration.EstimationSpecification#getAlgorithms <em>Algorithms</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Algorithms</em>' containment reference.
	 * @see #getAlgorithms()
	 * @generated
	 */
	void setAlgorithms(EstimationAlgorithmConfiguration value);

} // EstimationSpecification
