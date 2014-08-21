package edu.kit.ipd.descartes.librede.ui.forms;

import net.descartesresearch.librede.configuration.LibredeConfiguration;

import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import edu.kit.ipd.descartes.librede.ui.forms.master.MeasurementDataMasterBlock;

public class MeasurementDataFormPage extends AbstractEstimationConfigurationFormPage {

	/**
	 * Create the form page.
	 * @param editor
	 * @param id
	 * @param title
	 * @wbp.parser.constructor
	 * @wbp.eval.method.parameter id "Some id"
	 * @wbp.eval.method.parameter title "Some title"
	 */
	public MeasurementDataFormPage(FormEditor editor, String id, String title, AdapterFactoryEditingDomain domain, LibredeConfiguration model) {
		super(editor, id, title, domain, model);
	}

	/**
	 * Create contents of the form.
	 * @param managedForm
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		FormToolkit toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();
		form.setText("Measurements");
		Composite body = form.getBody();
		toolkit.decorateFormHeading(form.getForm());
		toolkit.paintBordersFor(body);

		MeasurementDataMasterBlock masterBlock = new MeasurementDataMasterBlock(this, getEditingDomain(), getModel());
		masterBlock.createContent(managedForm, body);
	}
}
