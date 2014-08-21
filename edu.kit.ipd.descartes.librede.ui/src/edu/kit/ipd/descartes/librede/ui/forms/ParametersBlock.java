package edu.kit.ipd.descartes.librede.ui.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.descartesresearch.librede.configuration.ConfigurationFactory;
import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.Parameter;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class ParametersBlock {
	
	public static enum ParameterType {
		TEXT, NUMBER, PATH
	}
	
	public static class ParameterDefinition {
		private String name;
		private ParameterType type;
		private boolean required;
		
		public ParameterDefinition(String name, ParameterType type,
				boolean required) {
			this.name = name;
			this.type = type;
			this.required = required;
		}
		
		public String getName() {
			return name;
		}
		
		public ParameterType getType() {
			return type;
		}
		
		public boolean isRequired() {
			return required;
		}
	}
	
	private interface ParameterEditor {
		Control createControl(FormToolkit toolkit, Composite parent);
		void activate();
		void update(Parameter param);
		void reset();
	}
	
	private class TextParameterEditor implements ParameterEditor, ModifyListener {
		private ParameterDefinition def;
		private Parameter param;
		private Text control;

		public TextParameterEditor(ParameterDefinition def) {
			this.def = def;
		}

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
			control.setText("");
		}

		@Override
		public void modifyText(ModifyEvent e) {
			if (!control.getText().isEmpty()) {
				if (param == null) {
					param = ConfigurationFactory.eINSTANCE.createParameter();
					param.setName(def.getName());
					addParameter(param);
				}
				param.setValue(control.getText());
			} else {
				removeParameter(param);
				param = null;
			}
		}		
	}
	
	private List<ParameterDefinition> parameters = new ArrayList<>();
	private List<ParameterEditor> editors = new ArrayList<>();
	private Map<String, Integer> editorIndexes = new HashMap<>();
	private Object input;


	public void setParameterDefinitions(List<ParameterDefinition> parameters) {
		this.parameters = parameters;
		int idx = 0;
		for (ParameterDefinition param : parameters) {
			editors.add(createEditor(param));
			editorIndexes.put(param.getName().toLowerCase(), idx);
			idx++;
		}
	}
	
	public void setInput(Object input) {
		this.input = input;
		update();
	}
	
	public void createControl(FormToolkit toolkit, Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		for (int i = 0; i < parameters.size(); i++) {
			ParameterDefinition param = parameters.get(i);
			String labelText = param.getName();
			if (param.isRequired()) {
				labelText = labelText + "*";
			}
			labelText = labelText + ":";
			Label label = toolkit.createLabel(parent,labelText);
			
			Control value = editors.get(i).createControl(toolkit, parent);
			value.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		}
	}
	
	private ParameterEditor createEditor(ParameterDefinition param) {
		switch(param.getType()) {
		default:
			return new TextParameterEditor(param);
		}
	}
	
	private void addParameter(Parameter param) {
		if (input instanceof List) {
			List<Object> content = (List<Object>)input;
			content.add(param);
		}
	}
	
	private void removeParameter(Parameter param) {
		if (input instanceof List) {
			List<Object> content = (List<Object>)input;
			content.remove(param);
		}
	}
	
	private void update() {
		for (ParameterEditor editor: editors) {
			editor.reset();
		}
		
		if (input instanceof List) {
			List<?> content = (List<?>)input;
			for (int i = 0; i < content.size(); i++) {
				Parameter p = (Parameter)content.get(i);
				Integer idx = editorIndexes.get(p.getName().toLowerCase());
				if (idx != null) {
					editors.get(idx).update(p);
				}
			}
		}
		
		for (ParameterEditor editor: editors) {
			editor.activate();
		}
	}
}
