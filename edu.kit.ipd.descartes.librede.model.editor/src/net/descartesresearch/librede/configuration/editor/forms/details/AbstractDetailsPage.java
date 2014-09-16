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
package net.descartesresearch.librede.configuration.editor.forms.details;

import net.descartesresearch.librede.configuration.editor.forms.AbstractEstimationConfigurationFormPage;

import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IManagedForm;

public abstract class AbstractDetailsPage implements IDetailsPage {

	protected IManagedForm managedForm;
	protected AdapterFactoryEditingDomain domain;
	protected AbstractEstimationConfigurationFormPage page;
	
	public AbstractDetailsPage(AbstractEstimationConfigurationFormPage page, AdapterFactoryEditingDomain domain) {
		super();
		this.page = page;
		this.domain = domain;
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
