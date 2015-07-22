/**
 */
package tools.descartes.librede.configuration;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Composite Service</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link tools.descartes.librede.configuration.CompositeService#getSubServices <em>Sub Services</em>}</li>
 * </ul>
 *
 * @see tools.descartes.librede.configuration.ConfigurationPackage#getCompositeService()
 * @model
 * @generated
 */
public interface CompositeService extends Service {
	/**
	 * Returns the value of the '<em><b>Sub Services</b></em>' containment reference list.
	 * The list contents are of type {@link tools.descartes.librede.configuration.Service}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sub Services</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sub Services</em>' containment reference list.
	 * @see tools.descartes.librede.configuration.ConfigurationPackage#getCompositeService_SubServices()
	 * @model containment="true"
	 * @generated
	 */
	EList<Service> getSubServices();

} // CompositeService
