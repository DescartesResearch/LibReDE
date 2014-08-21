/**
 */
package net.descartesresearch.librede.configuration;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Resource</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.descartesresearch.librede.configuration.Resource#getName <em>Name</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.Resource#getNumberOfServers <em>Number Of Servers</em>}</li>
 *   <li>{@link net.descartesresearch.librede.configuration.Resource#getSchedulingStrategy <em>Scheduling Strategy</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getResource()
 * @model
 * @generated
 */
public interface Resource extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getResource_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link net.descartesresearch.librede.configuration.Resource#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Number Of Servers</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Number Of Servers</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Number Of Servers</em>' attribute.
	 * @see #setNumberOfServers(int)
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getResource_NumberOfServers()
	 * @model default="1"
	 * @generated
	 */
	int getNumberOfServers();

	/**
	 * Sets the value of the '{@link net.descartesresearch.librede.configuration.Resource#getNumberOfServers <em>Number Of Servers</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Number Of Servers</em>' attribute.
	 * @see #getNumberOfServers()
	 * @generated
	 */
	void setNumberOfServers(int value);

	/**
	 * Returns the value of the '<em><b>Scheduling Strategy</b></em>' attribute.
	 * The default value is <code>"UNKOWN"</code>.
	 * The literals are from the enumeration {@link net.descartesresearch.librede.configuration.SchedulingStrategy}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scheduling Strategy</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scheduling Strategy</em>' attribute.
	 * @see net.descartesresearch.librede.configuration.SchedulingStrategy
	 * @see #setSchedulingStrategy(SchedulingStrategy)
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getResource_SchedulingStrategy()
	 * @model default="UNKOWN"
	 * @generated
	 */
	SchedulingStrategy getSchedulingStrategy();

	/**
	 * Sets the value of the '{@link net.descartesresearch.librede.configuration.Resource#getSchedulingStrategy <em>Scheduling Strategy</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Scheduling Strategy</em>' attribute.
	 * @see net.descartesresearch.librede.configuration.SchedulingStrategy
	 * @see #getSchedulingStrategy()
	 * @generated
	 */
	void setSchedulingStrategy(SchedulingStrategy value);

} // Resource
