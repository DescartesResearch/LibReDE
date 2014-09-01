/**
 */
package net.descartesresearch.librede.configuration;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Trace Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.descartesresearch.librede.configuration.TraceConfiguration#getMetric <em>Metric</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.TraceConfiguration#getUnit <em>Unit</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.TraceConfiguration#getResources <em>Resources</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.TraceConfiguration#getServices <em>Services</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.TraceConfiguration#getInterval <em>Interval</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.TraceConfiguration#getData <em>Data</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getTraceConfiguration()
 * @model
 * @generated
 */
public interface TraceConfiguration extends EObject {
	/**
	 * Returns the value of the '<em><b>Metric</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Metric</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Metric</em>' attribute.
	 * @see #setMetric(String)
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getTraceConfiguration_Metric()
	 * @model required="true"
	 * @generated
	 */
	String getMetric();

	/**
	 * Sets the value of the '{@link net.descartesresearch.librede.configuration.TraceConfiguration#getMetric <em>Metric</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Metric</em>' attribute.
	 * @see #getMetric()
	 * @generated
	 */
	void setMetric(String value);

	/**
	 * Returns the value of the '<em><b>Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Unit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Unit</em>' attribute.
	 * @see #setUnit(String)
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getTraceConfiguration_Unit()
	 * @model
	 * @generated
	 */
	String getUnit();

	/**
	 * Sets the value of the '{@link net.descartesresearch.librede.configuration.TraceConfiguration#getUnit <em>Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Unit</em>' attribute.
	 * @see #getUnit()
	 * @generated
	 */
	void setUnit(String value);

	/**
	 * Returns the value of the '<em><b>Resources</b></em>' reference list.
	 * The list contents are of type {@link net.descartesresearch.librede.configuration.Resource}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Resources</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Resources</em>' reference list.
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getTraceConfiguration_Resources()
	 * @model
	 * @generated
	 */
	EList<Resource> getResources();

	/**
	 * Returns the value of the '<em><b>Services</b></em>' reference list.
	 * The list contents are of type {@link net.descartesresearch.librede.configuration.Service}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Services</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Services</em>' reference list.
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getTraceConfiguration_Services()
	 * @model
	 * @generated
	 */
	EList<Service> getServices();

	/**
	 * Returns the value of the '<em><b>Interval</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Interval</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Interval</em>' attribute.
	 * @see #setInterval(long)
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getTraceConfiguration_Interval()
	 * @model default="0"
	 * @generated
	 */
	long getInterval();

	/**
	 * Sets the value of the '{@link net.descartesresearch.librede.configuration.TraceConfiguration#getInterval <em>Interval</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Interval</em>' attribute.
	 * @see #getInterval()
	 * @generated
	 */
	void setInterval(long value);

	/**
	 * Returns the value of the '<em><b>Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Data</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Data</em>' containment reference.
	 * @see #setData(DataSourceConfiguration)
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getTraceConfiguration_Data()
	 * @model containment="true" required="true"
	 * @generated
	 */
	DataSourceConfiguration getData();

	/**
	 * Sets the value of the '{@link net.descartesresearch.librede.configuration.TraceConfiguration#getData <em>Data</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Data</em>' containment reference.
	 * @see #getData()
	 * @generated
	 */
	void setData(DataSourceConfiguration value);

} // TraceConfiguration
