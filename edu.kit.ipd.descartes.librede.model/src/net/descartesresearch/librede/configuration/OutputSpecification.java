/**
 */
package net.descartesresearch.librede.configuration;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Output Specification</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.descartesresearch.librede.configuration.OutputSpecification#getExporters <em>Exporters</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getOutputSpecification()
 * @model
 * @generated
 */
public interface OutputSpecification extends EObject {

	/**
	 * Returns the value of the '<em><b>Exporters</b></em>' containment reference list.
	 * The list contents are of type {@link net.descartesresearch.librede.configuration.ExporterConfiguration}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Exporters</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Exporters</em>' containment reference list.
	 * @see net.descartesresearch.librede.configuration.ConfigurationPackage#getOutputSpecification_Exporters()
	 * @model containment="true"
	 * @generated
	 */
	EList<ExporterConfiguration> getExporters();
} // OutputSpecification
