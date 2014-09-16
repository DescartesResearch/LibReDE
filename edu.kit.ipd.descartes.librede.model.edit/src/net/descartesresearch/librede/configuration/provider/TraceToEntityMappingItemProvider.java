/**
 */
package net.descartesresearch.librede.configuration.provider;


import java.util.Collection;
import java.util.List;

import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.TraceToEntityMapping;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITableItemLabelProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link net.descartesresearch.librede.configuration.TraceToEntityMapping} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class TraceToEntityMappingItemProvider 
	extends ItemProviderAdapter
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource,
		ITableItemLabelProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TraceToEntityMappingItemProvider(AdapterFactory adapterFactory) {
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

			addEntityPropertyDescriptor(object);
			addTraceColumnPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Entity feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEntityPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TraceToEntityMapping_entity_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_TraceToEntityMapping_entity_feature", "_UI_TraceToEntityMapping_type"),
				 ConfigurationPackage.Literals.TRACE_TO_ENTITY_MAPPING__ENTITY,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Trace Column feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTraceColumnPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TraceToEntityMapping_traceColumn_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_TraceToEntityMapping_traceColumn_feature", "_UI_TraceToEntityMapping_type"),
				 ConfigurationPackage.Literals.TRACE_TO_ENTITY_MAPPING__TRACE_COLUMN,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns TraceToEntityMapping.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/TraceToEntityMapping"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		TraceToEntityMapping traceToEntityMapping = (TraceToEntityMapping)object;
		return getString("_UI_TraceToEntityMapping_type") + " " + traceToEntityMapping.getTraceColumn();
	}
	

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(TraceToEntityMapping.class)) {
			case ConfigurationPackage.TRACE_TO_ENTITY_MAPPING__ENTITY: // IMPORTANT: also get notified of changes to entity
			case ConfigurationPackage.TRACE_TO_ENTITY_MAPPING__TRACE_COLUMN:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
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
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return LibredeEditPlugin.INSTANCE;
	}
	
	/**
	 * Return the column image for a column index.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Object getColumnImage(Object object, int columnIndex) {
		if (columnIndex == 0) {
			TraceToEntityMapping mapping = (TraceToEntityMapping)object;
			if (mapping.getEntity() != null) {
				IItemLabelProvider prov = (IItemLabelProvider)getAdapterFactory().adapt(mapping.getEntity(), IItemLabelProvider.class);
				return prov.getImage(mapping.getEntity());
			}
		}
		return super.getColumnImage(object, columnIndex);
	}

	/**
	 * Return the column text for a column index.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getColumnText(Object object, int columnIndex) {
		TraceToEntityMapping mapping = (TraceToEntityMapping)object;
		switch(columnIndex) {
		case 0:
			if (mapping.getEntity() != null) {
				IItemLabelProvider prov = (IItemLabelProvider)getAdapterFactory().adapt(mapping.getEntity(), IItemLabelProvider.class);
				return prov.getText(mapping.getEntity());
			} else {
				// Indicate to user that he should select an entity.
				return "Select entity...";
			}
		case 1:
			return Integer.toString(mapping.getTraceColumn());
		}
		return super.getColumnText(object, columnIndex);
	}

}
