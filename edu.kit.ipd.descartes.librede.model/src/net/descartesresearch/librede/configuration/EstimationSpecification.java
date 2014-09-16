/**
 */
package net.descartesresearch.librede.configuration;

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
 *   <li>{@link net.descartesresearch.librede.configuration.EstimationSpecification#getApproaches <em>Approaches</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.EstimationSpecification#isRecursive <em>Recursive</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.EstimationSpecification#getWindow <em>Window</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.EstimationSpecification#getStepSize <em>Step Size</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.EstimationSpecification#getStartTimestamp <em>Start Timestamp</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.EstimationSpecification#getEndTimestamp <em>End Timestamp</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getEstimationSpecification()
 * @model
 * @generated
 */
public interface EstimationSpecification extends EObject {
	/**
	 * Returns the value of the '<em><b>Approaches</b></em>' containment reference list.
	 * The list contents are of type {@link net.descartesresearch.librede.configuration.EstimationApproachConfiguration}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Approaches</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Approaches</em>' containment reference list.
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getEstimationSpecification_Approaches()
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
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getEstimationSpecification_Recursive()
	 * @model default="false" required="true"
	 * @generated
	 */
	boolean isRecursive();

	/**
	 * Sets the value of the '{@link net.descartesresearch.librede.configuration.EstimationSpecification#isRecursive <em>Recursive</em>}' attribute.
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
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getEstimationSpecification_Window()
	 * @model required="true"
	 * @generated
	 */
	int getWindow();

	/**
	 * Sets the value of the '{@link net.descartesresearch.librede.configuration.EstimationSpecification#getWindow <em>Window</em>}' attribute.
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
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getEstimationSpecification_StepSize()
	 * @model default="1000" required="true"
	 * @generated
	 */
	long getStepSize();

	/**
	 * Sets the value of the '{@link net.descartesresearch.librede.configuration.EstimationSpecification#getStepSize <em>Step Size</em>}' attribute.
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
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getEstimationSpecification_StartTimestamp()
	 * @model default="0" required="true"
	 * @generated
	 */
	long getStartTimestamp();

	/**
	 * Sets the value of the '{@link net.descartesresearch.librede.configuration.EstimationSpecification#getStartTimestamp <em>Start Timestamp</em>}' attribute.
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
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getEstimationSpecification_EndTimestamp()
	 * @model default="0" required="true"
	 * @generated
	 */
	long getEndTimestamp();

	/**
	 * Sets the value of the '{@link net.descartesresearch.librede.configuration.EstimationSpecification#getEndTimestamp <em>End Timestamp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Timestamp</em>' attribute.
	 * @see #getEndTimestamp()
	 * @generated
	 */
	void setEndTimestamp(long value);

} // EstimationSpecification
