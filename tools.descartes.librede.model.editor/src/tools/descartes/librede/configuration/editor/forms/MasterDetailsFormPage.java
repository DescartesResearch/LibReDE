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

import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import tools.descartes.librede.configuration.LibredeConfiguration;
import tools.descartes.librede.configuration.editor.forms.master.AbstractMasterBlock;
import tools.descartes.librede.configuration.presentation.ConfigurationEditor;
import tools.descartes.librede.configuration.presentation.LibredeEditorPlugin;

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
