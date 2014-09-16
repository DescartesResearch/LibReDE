package net.descartesresearch.librede.configuration.editor.forms;

import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.editor.forms.master.AbstractMasterBlock;
import net.descartesresearch.librede.configuration.presentation.ConfigurationEditor;
import net.descartesresearch.librede.configuration.presentation.LibredeEditorPlugin;

import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class MasterDetailsFormPage extends
		AbstractEstimationConfigurationFormPage {

	private AbstractMasterBlock masterBlock;
	private String icon;

	/**
	 * Create the form page.
	 * 
	 * @param editor
	 * @param id
	 * @param title
	 */
	public MasterDetailsFormPage(ConfigurationEditor editor, String id,
			String title, String icon,
			AdapterFactoryEditingDomain editingDomain,
			LibredeConfiguration model, AbstractMasterBlock masterBlock) {
		super(editor, id, title, editingDomain, model);
		this.icon = icon;
		this.masterBlock = masterBlock;
	}

	/**
	 * Create contents of the form.
	 * 
	 * @param managedForm
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		FormToolkit toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();
		form.setText(getTitle());
		form.setImage(ExtendedImageRegistry.INSTANCE
				.getImage(LibredeEditorPlugin.getPlugin().getImage(icon)));
		toolkit.decorateFormHeading(form.getForm());

		Composite body = managedForm.getForm().getBody();
		
		createFormContentBeginning(toolkit, body);
		
		masterBlock.setFormPage(this);
		masterBlock.createContent(managedForm);		
		

	}

	protected void createFormContentBeginning(FormToolkit toolkit,
			Composite parent) {

	}

}
