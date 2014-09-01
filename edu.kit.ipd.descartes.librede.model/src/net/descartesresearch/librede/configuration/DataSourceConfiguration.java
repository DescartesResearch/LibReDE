/**
 */
package net.descartesresearch.librede.configuration;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Source Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.descartesresearch.librede.configuration.DataSourceConfiguration#getDataProvider <em>Data Provider</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.DataSourceConfiguration#getType <em>Type</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.DataSourceConfiguration#getParameters <em>Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getDataSourceConfiguration()
 * @model
 * @generated
 */
public interface DataSourceConfiguration extends EObject {
	/**
	 * Returns the value of the '<em><b>Data Provider</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Data Provider</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Data Provider</em>' reference.
	 * @see #setDataProvider(DataProviderConfiguration)
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getDataSourceConfiguration_DataProvider()
	 * @model required="true"
	 * @generated
	 */
	DataProviderConfiguration getDataProvider();

	/**
	 * Sets the value of the '{@link net.descartesresearch.librede.configuration.DataSourceConfiguration#getDataProvider <em>Data Provider</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Data Provider</em>' reference.
	 * @see #getDataProvider()
	 * @generated
	 */
	void setDataProvider(DataProviderConfiguration value);

	/**
	 * Returns the value of the '<em><b>Parameters</b></em>' containment reference list.
	 * The list contents are of type {@link net.descartesresearch.librede.configuration.Parameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameters</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameters</em>' containment reference list.
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getDataSourceConfiguration_Parameters()
	 * @model containment="true"
	 * @generated
	 */
	EList<Parameter> getParameters();

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(Class)
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getDataSourceConfiguration_Type()
	 * @model required="true"
	 * @generated
	 */
	Class<?> getType();

	/**
	 * Sets the value of the '{@link net.descartesresearch.librede.configuration.DataSourceConfiguration#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(Class<?> value);

} // DataSourceConfiguration
