/**
 */
package net.descartesresearch.librede.configuration;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>File Trace Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.descartesresearch.librede.configuration.FileTraceConfiguration#getFile <em>File</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getFileTraceConfiguration()
 * @model
 * @generated
 */
public interface FileTraceConfiguration extends TraceConfiguration {

	/**
	 * Returns the value of the '<em><b>File</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>File</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>File</em>' attribute.
	 * @see #setFile(String)
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getFileTraceConfiguration_File()
	 * @model required="true"
	 * @generated
	 */
	String getFile();

	/**
	 * Sets the value of the '{@link net.descartesresearch.librede.configuration.FileTraceConfiguration#getFile <em>File</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>File</em>' attribute.
	 * @see #getFile()
	 * @generated
	 */
	void setFile(String value);
} // FileTraceConfiguration
