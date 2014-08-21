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
 *   <li>{@link net.descartesresearch.librede.configuration.InputSpecification#getDataSources <em>Data Sources</em>}</li>
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
	 * Returns the value of the '<em><b>Data Sources</b></em>' containment reference list.
	 * The list contents are of type {@link net.descartesresearch.librede.configuration.DataSource}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Data Sources</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Data Sources</em>' containment reference list.
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getInputSpecification_DataSources()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EList<DataSource> getDataSources();

	/**
	 * Returns the value of the '<em><b>Observations</b></em>' containment reference list.
	 * The list contents are of type {@link net.descartesresearch.librede.configuration.TimeSeries}.
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
	EList<TimeSeries> getObservations();

} // InputSpecification
