package net.descartesresearch.librede.configuration.editor.forms.details;

import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.editor.forms.AbstractEstimationConfigurationFormPage;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IManagedForm;

public abstract class AbstractDetailsPage implements IDetailsPage {
	
	protected IManagedForm managedForm;
	protected EditingDomain domain;
	protected LibredeConfiguration model;
	protected AbstractEstimationConfigurationFormPage page;
	
	public AbstractDetailsPage(AbstractEstimationConfigurationFormPage page, EditingDomain domain, LibredeConfiguration model) {
		super();
		this.page = page;
		this.domain = domain;
		this.model = model;
	}

	/**
	 * Initialize the details page.
	 * @param form
	 */
	public void initialize(IManagedForm form) {
		managedForm = form;
	}

	public void dispose() {
		// Dispose
	}

	public void setFocus() {
		// Set focus
	}

	protected void update() {
	}

	public boolean setFormInput(Object input) {
		return false;
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
