package net.descartesresearch.librede.configuration.editor.forms;

import net.descartesresearch.librede.configuration.LibredeConfiguration;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;

public abstract class AbstractEstimationConfigurationFormPage extends FormPage {

	protected static class ComboBoxFromEnumEditingSupport extends
			ObservableValueEditingSupport {

		private IValueProperty model;
		private ComboBoxViewerCellEditor cellEditor;

		public ComboBoxFromEnumEditingSupport(ColumnViewer viewer,
				EMFDataBindingContext dbc, IValueProperty model) {
			super(viewer, dbc);
			this.model = model;

			this.cellEditor = new ComboBoxViewerCellEditor(
					(Composite) viewer.getControl(), SWT.READ_ONLY);
			this.cellEditor.setLabelProvider(new LabelProvider());
			this.cellEditor.setContentProvider(new ArrayContentProvider());
		}

		@Override
		protected IObservableValue doCreateCellEditorObservable(
				CellEditor cellEditor) {
			return ViewerProperties.singleSelection().observe(
					this.cellEditor.getViewer());
		}

		@Override
		protected IObservableValue doCreateElementObservable(Object element,
				ViewerCell cell) {
			return model.observe(element);
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			Object o = model.getValue(element);
			cellEditor.setInput(o.getClass().getEnumConstants());
			return cellEditor;
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

	public AbstractEstimationConfigurationFormPage(FormEditor editor,
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
}
