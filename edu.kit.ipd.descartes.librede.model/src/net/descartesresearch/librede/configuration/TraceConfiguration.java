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
 *   <li>{@link net.descartesresearch.librede.configuration.TraceConfiguration#getInterval <em>Interval</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.TraceConfiguration#getProvider <em>Provider</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.TraceConfiguration#getMappings <em>Mappings</em>}</li>
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
	 * @model default="0" required="true"
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
	 * Returns the value of the '<em><b>Provider</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Provider</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Provider</em>' reference.
	 * @see #setProvider(DataSourceConfiguration)
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getTraceConfiguration_Provider()
	 * @model required="true"
	 * @generated
	 */
	DataSourceConfiguration getProvider();

	/**
	 * Sets the value of the '{@link net.descartesresearch.librede.configuration.TraceConfiguration#getProvider <em>Provider</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Provider</em>' reference.
	 * @see #getProvider()
	 * @generated
	 */
	void setProvider(DataSourceConfiguration value);

	/**
	 * Returns the value of the '<em><b>Mappings</b></em>' containment reference list.
	 * The list contents are of type {@link net.descartesresearch.librede.configuration.TraceToEntityMapping}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mappings</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mappings</em>' containment reference list.
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getTraceConfiguration_Mappings()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EList<TraceToEntityMapping> getMappings();

} // TraceConfiguration
