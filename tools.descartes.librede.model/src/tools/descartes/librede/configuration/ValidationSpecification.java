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
 * A representation of the model object '<em><b>Validation Specification</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link tools.descartes.librede.configuration.ValidationSpecification#getValidators <em>Validators</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.ValidationSpecification#getValidationFolds <em>Validation Folds</em>}</li>
 *   <li>{@link tools.descartes.librede.configuration.ValidationSpecification#isValidateEstimates <em>Validate Estimates</em>}</li>
 * </ul>
 *
 * @see tools.descartes.librede.configuration.ConfigurationPackage#getValidationSpecification()
 * @model
 * @generated
 */
public interface ValidationSpecification extends EObject {

	/**
	 * Returns the value of the '<em><b>Validators</b></em>' containment reference list.
	 * The list contents are of type {@link tools.descartes.librede.configuration.ValidatorConfiguration}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Validators</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Validators</em>' containment reference list.
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getValidationSpecification_Validators()
	 * @model containment="true"
	 * @generated
	 */
	EList<ValidatorConfiguration> getValidators();

	/**
	 * Returns the value of the '<em><b>Validation Folds</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Validation Folds</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Validation Folds</em>' attribute.
	 * @see #setValidationFolds(int)
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getValidationSpecification_ValidationFolds()
	 * @model default="1" required="true"
	 * @generated
	 */
	int getValidationFolds();

	/**
	 * Sets the value of the '{@link tools.descartes.librede.configuration.ValidationSpecification#getValidationFolds <em>Validation Folds</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Validation Folds</em>' attribute.
	 * @see #getValidationFolds()
	 * @generated
	 */
	void setValidationFolds(int value);

	/**
	 * Returns the value of the '<em><b>Validate Estimates</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Validate Estimates</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Validate Estimates</em>' attribute.
	 * @see #setValidateEstimates(boolean)
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getValidationSpecification_ValidateEstimates()
	 * @model default="false" required="true"
	 * @generated
	 */
	boolean isValidateEstimates();

	/**
	 * Sets the value of the '{@link tools.descartes.librede.configuration.ValidationSpecification#isValidateEstimates <em>Validate Estimates</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Validate Estimates</em>' attribute.
	 * @see #isValidateEstimates()
	 * @generated
	 */
	void setValidateEstimates(boolean value);
} // ValidationSpecification
