package edu.kit.ipd.descartes.librede.ui.forms.details;

import java.util.Arrays;

import net.descartesresearch.librede.configuration.DataSource;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import edu.kit.ipd.descartes.librede.ui.forms.ParametersBlock;
import edu.kit.ipd.descartes.librede.ui.forms.ParametersBlock.ParameterType;

public class DataProviderDetailsPage implements IDetailsPage {

	private IManagedForm managedForm;
	private ParametersBlock parameters;

	/**
	 * Create the details page.
	 */
	public DataProviderDetailsPage() {
		// Create the details page
	}

	/**
	 * Initialize the details page.
	 * @param form
	 */
	public void initialize(IManagedForm form) {
		managedForm = form;
	}

	/**
	 * Create contents of the details page.
	 * @param parent
	 */
	public void createContents(Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();
		parent.setLayout(new FillLayout());
		//		
		Section section = toolkit.createSection(parent,
				ExpandableComposite.EXPANDED | ExpandableComposite.TITLE_BAR);
		section.setText("Data Provider Configuration");
		//
		Composite composite = toolkit.createComposite(section, SWT.NONE);
		toolkit.paintBordersFor(composite);
		section.setClient(composite);
		
		parameters = new ParametersBlock();
		parameters.setParameterDefinitions(Arrays.asList(new ParametersBlock.ParameterDefinition("Test", ParameterType.TEXT, true)));
		parameters.createControl(toolkit, composite);
	}

	public void dispose() {
		// Dispose
	}

	public void setFocus() {
		// Set focus
	}

	private void update() {
		// Update
	}

	public boolean setFormInput(Object input) {
		return false;
	}

	public void selectionChanged(IFormPart part, ISelection selection) {
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		if (structuredSelection.size() == 1) {
			DataSource sel = (DataSource)structuredSelection.getFirstElement();
			parameters.setInput(sel.getConfiguration());
		} else {
			parameters.setInput(null);
		}
		update();
	}

	public void commit(boolean onSave) {
		// Commit
	}

	public boolean isDirty() {
		return false;
	}

	public boolean isStale() {
		return false;
	}

	public void refresh() {
		update();
	}

}
