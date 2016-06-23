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

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import tools.descartes.librede.configuration.ConfigurationFactory;
import tools.descartes.librede.configuration.Parameter;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.registry.ParameterDefinition;
import tools.descartes.librede.registry.Registry;

public class ParametersBlock {
	
	private abstract class ParameterEditor {
		protected String name;
		protected String label;
		protected String defaultValue;
		protected boolean required;
		protected Parameter param;
		
		abstract Control createControl(FormToolkit toolkit, Composite parent);
		
		Label createLabel(FormToolkit toolkit, Composite parent) {
			String labelText = label;
			if (required) {
				labelText = labelText + "*";
			}
			labelText = labelText + ":";
			return toolkit.createLabel(parent,labelText);
		}

		abstract void activate();
		abstract void update(Parameter param);
		abstract void reset();
	}
	
	private class CheckboxParameterEditor extends ParameterEditor implements SelectionListener {
		
		private Button checkbox;

		@Override
		Control createControl(FormToolkit toolkit, Composite parent) {
			String labelText = label;
			if (required) {
				labelText = labelText + "*";
			}
			checkbox = toolkit.createButton(parent, labelText, SWT.CHECK);
			return checkbox;
		}
		
		@Override
		Label createLabel(FormToolkit toolkit, Composite parent) {
			return toolkit.createLabel(parent, "");
		}

		@Override
		void activate() {
			checkbox.addSelectionListener(this);
		}

		@Override
		void update(Parameter param) {
			this.param = param;
			checkbox.setSelection(param.getValue().equals("on"));
		}

		@Override
		void reset() {
			checkbox.removeSelectionListener(this);
			param = null;
			checkbox.setSelection(Boolean.parseBoolean(defaultValue));			
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			if (param == null) {
				param = ConfigurationFactory.eINSTANCE.createParameter();
				param.setName(name);
				addParameter(param);
			}
			if (checkbox.getSelection()) {
				param.setValue("on");
			} else {
				param.setValue("off");
			}
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			
		}
		
	}
	
	private class TextParameterEditor extends ParameterEditor implements ModifyListener {
		
		private Text control;		

		@Override
		public Control createControl(FormToolkit toolkit, Composite parent) {
			control = toolkit.createText(parent, "");
			return control;
		}

		@Override
		public void update(Parameter param) {
			this.param = param;
			control.setText(param.getValue());
		}
		
		@Override
		public void activate() {
			control.addModifyListener(this);			
		}
		
		@Override
		public void reset() {
			control.removeModifyListener(this);
			param = null;
			control.setText(defaultValue);
		}

		@Override
		public void modifyText(ModifyEvent e) {
			if (!control.getText().isEmpty()) {
				if (param == null) {
					param = ConfigurationFactory.eINSTANCE.createParameter();
					param.setName(name);
					addParameter(param);
				}
				param.setValue(control.getText());
			} else {
				removeParameter(param);
				param = null;
			}
		}		
	}
	
	private class FileParameterEditor extends ParameterEditor implements ModifyListener {
		private Text path;
		private Button browse;
		private Dialog dialog;
		private String subtype;
		
		public FileParameterEditor(String subtype) {
			this.subtype = subtype;
		}

		@Override
		Control createControl(FormToolkit toolkit, Composite parent) {
			Composite container = toolkit.createComposite(parent, SWT.NONE);
			GridLayout layout = new GridLayout(2, false);
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			container.setLayout(layout);
			path = toolkit.createText(container, "");
			path.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			browse = toolkit.createButton(container, "Browse...", SWT.PUSH);
			browse.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (dialog == null) {
						if (subtype.isEmpty()) {
							dialog = new DirectoryDialog(shell, SWT.NONE);
						} else {
							FileDialog newDialog = new FileDialog(shell, SWT.OPEN);
							newDialog.setFilterExtensions(subtype.split("|"));
							dialog = newDialog;
						}
					}
					String selPath;
					if (subtype.isEmpty()) {
						selPath = ((DirectoryDialog)dialog).open();
					} else {
						selPath = ((FileDialog)dialog).open();
					}
					if (selPath != null) {
						path.setText(selPath);
					}
				}
			});
			return container;
		}

		@Override
		void activate() {
			path.addModifyListener(this);		
		}

		@Override
		void update(Parameter param) {
			this.param = param;
			path.setText(param.getValue());
		}

		@Override
		void reset() {
			path.removeModifyListener(this);
			param = null;
			path.setText(defaultValue);			
		}
		
		@Override
		public void modifyText(ModifyEvent e) {
			if (!path.getText().isEmpty()) {
				if (param == null) {
					param = ConfigurationFactory.eINSTANCE.createParameter();
					param.setName(name);
					addParameter(param);
				}
				param.setValue(path.getText());
			} else {
				removeParameter(param);
				param = null;
			}
		}
		
	}
	
	private List<ParameterEditor> editors = new ArrayList<>();
	private Map<String, ParameterEditor> nameToEditor = new HashMap<>();
	private Object input;
	private Shell shell;
	private EStructuralFeature parametersFeature;
	private EditingDomain editingDomain;
	
	public ParametersBlock(EditingDomain domain, EStructuralFeature parametersFeature) {
		this.editingDomain = domain;
		this.parametersFeature = parametersFeature;
	}
	
	public void setObjectType(String type) {
		Class<?> cl = Registry.INSTANCE.getInstanceClass(type);
		if (cl != null) {
			if (cl.isAnnotationPresent(Component.class)) {
				for (Field curField : cl.getDeclaredFields()) {
					ParameterDefinition param = curField.getAnnotation(ParameterDefinition.class);
					if (param != null) {
						ParameterEditor editor = createEditor(curField.getType(), param.subType());
						editor.label = param.label();
						editor.name = param.name();
						editor.required = param.required();
						editor.defaultValue = param.defaultValue();
						editors.add(editor);
						nameToEditor.put(param.name().toLowerCase(), editor);
					}
				}
			}
		}
	}
	
	public void setInput(Object input) {
		this.input = input;
		update();
	}
	
	public void createControl(FormToolkit toolkit, Shell shell, Composite parent) {
		this.shell = shell;

		for (ParameterEditor curEditor : editors) {
			curEditor.createLabel(toolkit, parent);
			
			Control value = curEditor.createControl(toolkit, parent);
			value.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		}
	}
	
	private ParameterEditor createEditor(Class<?> type, String subType) {
		if (type.equals(Boolean.TYPE)) {
			return new CheckboxParameterEditor();
		} else if (type.equals(File.class)) {
			return new FileParameterEditor(subType);
		}
		return new TextParameterEditor();
	}
	
	private void addParameter(Parameter param) {
		Command cmd = AddCommand.create(editingDomain, input, parametersFeature, param);
		editingDomain.getCommandStack().execute(cmd);
	}
	
	private void removeParameter(Parameter param) {
		Command cmd = RemoveCommand.create(editingDomain, param);
		editingDomain.getCommandStack().execute(cmd);
	}
	
	private void update() {
		for (ParameterEditor editor: editors) {
			editor.reset();
		}
		
		if (input instanceof EObject) {
			List<?> content = (List<?>)((EObject)input).eGet(parametersFeature);
			for (int i = 0; i < content.size(); i++) {
				Parameter p = (Parameter)content.get(i);
				ParameterEditor editor = nameToEditor.get(p.getName().toLowerCase());
				if (editor != null) {
					editor.update(p);
				}
			}
		}
		
		for (ParameterEditor editor: editors) {
			editor.activate();
		}
	}
}
