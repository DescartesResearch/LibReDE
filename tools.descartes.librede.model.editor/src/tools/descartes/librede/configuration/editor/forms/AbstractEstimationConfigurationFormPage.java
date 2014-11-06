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
package tools.descartes.librede.configuration.editor.forms;

import java.util.Collection;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;

import tools.descartes.librede.configuration.LibredeConfiguration;
import tools.descartes.librede.configuration.presentation.ConfigurationEditor;

public abstract class AbstractEstimationConfigurationFormPage extends FormPage {

	public static abstract class EObjectEditingSupport extends
			EditingSupport {
		protected TableViewer viewer;
		protected EStructuralFeature attribute;
		protected AdapterFactoryEditingDomain domain;

		public EObjectEditingSupport(TableViewer viewer, AdapterFactoryEditingDomain domain, EStructuralFeature attribute) {
			super(viewer);
			this.attribute = attribute;
			this.domain = domain;
			this.viewer = viewer;
		}
		
		public static EObjectEditingSupport create(TableViewer viewer, AdapterFactoryEditingDomain domain, EStructuralFeature attribute) {
			if (attribute instanceof EReference || attribute.getEType() instanceof EEnum) {
				return new ChoiceEObjectEditingSupport(viewer, domain, attribute);
			}
			return new TextEObjectEditingSupport(viewer, domain, attribute);
		}

		@Override
		protected boolean canEdit(Object element) {
			return true;
		}
		
		@Override
		protected void setValue(Object element, Object value) {
			Object oldValue = ((EObject)element).eGet(attribute);
			if (((oldValue == null) && (value != null))
					|| ((oldValue != null) && !oldValue.equals(value))) {
				Command cmd = SetCommand.create(domain, element, attribute, value);
				domain.getCommandStack().execute(cmd);
			}
		}

	}
	
	protected static class ChoiceEObjectEditingSupport extends EObjectEditingSupport {
		
		private ComboBoxViewerCellEditor cellEditor;
		
		public ChoiceEObjectEditingSupport(TableViewer viewer,
				AdapterFactoryEditingDomain domain, EStructuralFeature attribute) {
			super(viewer, domain, attribute);
		}
		
		@Override
		protected CellEditor getCellEditor(Object element) {
			IItemPropertySource source = (IItemPropertySource)domain.getAdapterFactory().adapt(element, IItemPropertySource.class);
			IItemPropertyDescriptor desc = source.getPropertyDescriptor(element, attribute);
			
			if (cellEditor == null) {
				ComboBoxViewerCellEditor comboBoxEditor = new ComboBoxViewerCellEditor(viewer.getTable());
				comboBoxEditor.setContentProvider(new ArrayContentProvider());
				comboBoxEditor.setLabelProvider(new AdapterFactoryLabelProvider(domain.getAdapterFactory()));
				cellEditor = comboBoxEditor;
			}
			
			Collection<?> d = desc.getChoiceOfValues(element);
			d.remove(null);
			cellEditor.setInput(d);
			return cellEditor;
		}

		@Override
		protected Object getValue(Object element) {
			return ((EObject)element).eGet(attribute);
		}
		
	}
	
	protected static class TextEObjectEditingSupport extends EObjectEditingSupport {
		
		private TextCellEditor cellEditor;

		public TextEObjectEditingSupport(TableViewer viewer,
				AdapterFactoryEditingDomain domain, EStructuralFeature attribute) {
			super(viewer, domain, attribute);
		}
		
		@Override
		protected CellEditor getCellEditor(Object element) {
			if (cellEditor == null) {
				cellEditor = new TextCellEditor(viewer.getTable());
			}
			return cellEditor;
		}
		
		@Override
		protected Object getValue(Object element) {
			return EcoreUtil.convertToString((EDataType) attribute.getEType(), ((EObject)element).eGet(attribute));
		}
		
		@Override
		protected void setValue(Object element, Object value) {
			Object objValue = EcoreUtil.createFromString((EDataType) attribute.getEType(), (String)value);
			super.setValue(element, objValue);
		}
	}

	protected final class RemoveSelectionSelectionListener extends
			SelectionAdapter {
		
		private ISelectionProvider provider;	
		
		public RemoveSelectionSelectionListener(ISelectionProvider provider) {
			this.provider = provider;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			IStructuredSelection selection = (IStructuredSelection) provider
					.getSelection();
			if (!selection.isEmpty()) {
				Command remove = RemoveCommand.create(editingDomain,
						selection.toList());
				editingDomain.getCommandStack().execute(remove);
			}
		}
	}

	private LibredeConfiguration model;
	private AdapterFactoryEditingDomain editingDomain;

	public AbstractEstimationConfigurationFormPage(ConfigurationEditor editor,
			String id, String title, AdapterFactoryEditingDomain editingDomain, LibredeConfiguration model) {
		super(editor, id, title);
		this.editingDomain = editingDomain;
		this.model = model;
	}

	public LibredeConfiguration getModel() {
		return model;
	}
	
	public AdapterFactoryEditingDomain getEditingDomain() {
		return editingDomain;
	}
	
	public AdapterFactory getAdapterFactory() {
		return ((ConfigurationEditor)getEditor()).getAdapterFactory();
	}
}
