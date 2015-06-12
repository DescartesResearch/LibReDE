/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2014, by Simon Spinner and Contributors.
 *
 * Project Info:   http://www.descartes-research.net/
 *
 * All rights reserved. This software is made available under the terms of the
 * Eclipse Public License (EPL) v1.0 as published by the Eclipse Foundation
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Eclipse Public License (EPL)
 * for more details.
 *
 * You should have received a copy of the Eclipse Public License (EPL)
 * along with this software; if not visit http://www.eclipse.org or write to
 * Eclipse Foundation, Inc., 308 SW First Avenue, Suite 110, Portland, 97204 USA
 * Email: license (at) eclipse.org
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 */
/**
 */
package tools.descartes.librede.configuration.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.DelegatingWrapperItemProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;
import tools.descartes.librede.configuration.ConfigurationFactory;
import tools.descartes.librede.configuration.ConfigurationPackage;
import tools.descartes.librede.configuration.Service;

/**
 * This is the item provider adapter for a {@link tools.descartes.librede.configuration.Service} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ServiceItemProvider 
	extends ModelEntityItemProvider {
	
	private class CalledServiceItemProvider extends DelegatingWrapperItemProvider {

		public CalledServiceItemProvider(Object value, Object owner, EStructuralFeature feature, int index,
				AdapterFactory adapterFactory) {
			super(value, owner, feature, index, adapterFactory);
		}
		
		@Override
		public Object getColumnImage(Object object, int columnIndex) {
			if (columnIndex == 0) {
				return overlayImage(object, getResourceLocator().getImage("full/obj16/CalledService"));
			}
			return super.getColumnImage(object, columnIndex);
		}
		
		
		@Override
		public String getColumnText(Object object, int columnIndex) {
			if (columnIndex == 0) {
				return super.getColumnText(object, columnIndex);
			}
			return "";			
		}
	}
	
	private class UsedResourceItemProvider extends DelegatingWrapperItemProvider {

		public UsedResourceItemProvider(Object value, Object owner, EStructuralFeature feature, int index,
				AdapterFactory adapterFactory) {
			super(value, owner, feature, index, adapterFactory);
		}
		
		@Override
		public String getColumnText(Object object, int columnIndex) {
			if (columnIndex == 0) {
				return super.getColumnText(object, columnIndex);
			}
			return "";			
		}
	}
	
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ServiceItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addBackgroundServicePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Background Service feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBackgroundServicePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Service_backgroundService_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Service_backgroundService_feature", "_UI_Service_type"),
				 ConfigurationPackage.Literals.SERVICE__BACKGROUND_SERVICE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(ConfigurationPackage.Literals.SERVICE__RESOURCES);
			childrenFeatures.add(ConfigurationPackage.Literals.SERVICE__SUB_SERVICES);
			childrenFeatures.add(ConfigurationPackage.Literals.SERVICE__CALLED_SERVICES);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns Service.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/Service"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		String label = ((Service)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_Service_type") :
			label;
	}
	

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(Service.class)) {
			case ConfigurationPackage.SERVICE__BACKGROUND_SERVICE:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case ConfigurationPackage.SERVICE__RESOURCES:
			case ConfigurationPackage.SERVICE__SUB_SERVICES:
			case ConfigurationPackage.SERVICE__CALLED_SERVICES:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(ConfigurationPackage.Literals.SERVICE__RESOURCES,
				 ConfigurationFactory.eINSTANCE.createResource()));

		newChildDescriptors.add
			(createChildParameter
				(ConfigurationPackage.Literals.SERVICE__SUB_SERVICES,
				 ConfigurationFactory.eINSTANCE.createService()));

		newChildDescriptors.add
			(createChildParameter
				(ConfigurationPackage.Literals.SERVICE__CALLED_SERVICES,
				 ConfigurationFactory.eINSTANCE.createService()));
	}

	/**
	 * This returns the label text for {@link org.eclipse.emf.edit.command.CreateChildCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCreateChildText(Object owner, Object feature, Object child, Collection<?> selection) {
		Object childFeature = feature;
		Object childObject = child;

		boolean qualify =
			childFeature == ConfigurationPackage.Literals.SERVICE__SUB_SERVICES ||
			childFeature == ConfigurationPackage.Literals.SERVICE__CALLED_SERVICES;

		if (qualify) {
			return getString
				("_UI_CreateChild_text2",
				 new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
		}
		return super.getCreateChildText(owner, feature, child, selection);
	}

	@Override
	public Object getColumnImage(Object object, int columnIndex) {
		if (columnIndex == 0) {
			return getImage(object);
		}
		return super.getColumnImage(object, columnIndex);
	}
	
	@Override
	public String getColumnText(Object object, int columnIndex) {
		if (columnIndex == 0) {
			return getText(object);
		} else if(columnIndex == 1) {
			return ((Service)object).isBackgroundService() ? "Yes" : "No";
		}
		return super.getColumnText(object, columnIndex);
	}
	
	@Override
	protected boolean isWrappingNeeded(Object object) {
		return true;
	}
	
	@Override
	protected Object createWrapper(EObject object, EStructuralFeature feature, Object value, int index) {
		/*
		 * Add a wrapper to change the column texts and image 
		 */
		if (feature == ConfigurationPackage.Literals.SERVICE__CALLED_SERVICES) {
			return new CalledServiceItemProvider(value, object, feature, index, adapterFactory);
		} else if (feature == ConfigurationPackage.Literals.SERVICE__RESOURCES) {
			return new UsedResourceItemProvider(value, object, feature, index, adapterFactory);
		}
		return super.createWrapper(object, feature, value, index);
	}
}
