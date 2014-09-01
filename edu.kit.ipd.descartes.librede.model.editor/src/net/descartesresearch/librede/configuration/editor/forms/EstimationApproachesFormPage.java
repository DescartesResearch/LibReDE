package net.descartesresearch.librede.configuration.editor.forms;

import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.editor.forms.master.EstimationApproachesMasterBlock;

import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class EstimationApproachesFormPage extends AbstractEstimationConfigurationFormPage {

	/**
	 * Create the form page.
	 * @param id
	 * @param title
	 */
	public EstimationApproachesFormPage(FormEditor editor, String id, String title, AdapterFactoryEditingDomain editingDomain, LibredeConfiguration model) {
		super(editor, id, title, editingDomain, model);
	}

	/**
	 * Create contents of the form.
	 * @param managedForm
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		FormToolkit toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();
		form.setText("Estimation Approaches");
		toolkit.decorateFormHeading(form.getForm());

		EstimationApproachesMasterBlock block = new EstimationApproachesMasterBlock(this, getEditingDomain(), getModel());
		block.createContent(managedForm);
	}

}
