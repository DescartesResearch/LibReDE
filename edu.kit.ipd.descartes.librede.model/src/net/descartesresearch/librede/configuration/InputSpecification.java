/**
 */
package net.descartesresearch.librede.configuration;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Input Specification</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.descartesresearch.librede.configuration.InputSpecification#getDataProviders <em>Data Providers</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.InputSpecification#getObservations <em>Observations</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getInputSpecification()
 * @model
 * @generated
 */
public interface InputSpecification extends EObject {
	/**
	 * Returns the value of the '<em><b>Data Providers</b></em>' containment reference list.
	 * The list contents are of type {@link net.descartesresearch.librede.configuration.DataProviderConfiguration}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Data Providers</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Data Providers</em>' containment reference list.
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getInputSpecification_DataProviders()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EList<DataProviderConfiguration> getDataProviders();

	/**
	 * Returns the value of the '<em><b>Observations</b></em>' containment reference list.
	 * The list contents are of type {@link net.descartesresearch.librede.configuration.TraceConfiguration}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Observations</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Observations</em>' containment reference list.
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getInputSpecification_Observations()
	 * @model containment="true"
	 * @generated
	 */
	EList<TraceConfiguration> getObservations();

} // InputSpecification
