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
package tools.descartes.librede.configuration.editor.forms.master;

import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import tools.descartes.librede.configuration.LibredeConfiguration;
import tools.descartes.librede.configuration.actions.RunEstimationAction;
import tools.descartes.librede.configuration.editor.forms.AbstractEstimationConfigurationFormPage;
import tools.descartes.librede.configuration.presentation.LibredeEditorPlugin;

public abstract class AbstractMasterBlock extends MasterDetailsBlock implements ISelectionChangedListener {
	
	protected IManagedForm managedForm;
	protected FormToolkit toolkit;
	protected AbstractEstimationConfigurationFormPage page;
	protected SectionPart masterPart;
	protected AdapterFactoryEditingDomain domain;
	protected LibredeConfiguration model;
	
	public AbstractMasterBlock(AdapterFactoryEditingDomain domain, LibredeConfiguration model) {
		this.domain = domain;
		this.model = model;
	}
	
	public void setFormPage(AbstractEstimationConfigurationFormPage page) {
		this.page = page;
	}
	
	@Override
	protected void applyLayout(Composite parent) {
		GridLayout layout = new GridLayout(2, true);
		// Values from org.eclipse.pde.internal.ui.editor.FormLayoutFactory
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.marginTop = 12;
		layout.marginBottom = 12;
		layout.marginLeft = 6;
		layout.marginRight = 6;
		layout.horizontalSpacing = 20;
		layout.verticalSpacing = 17;
		parent.setLayout(layout);
	}
	
	@Override
	protected void applyLayoutData(SashForm sashForm) {
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		sashForm.setLayoutData(data);
	}
	
	@Override
	protected void createToolBarActions(IManagedForm managedForm) {
		managedForm.getForm().getToolBarManager().add(new RunEstimationAction(model));
	}
	
	@Override
	protected void createMasterPart(IManagedForm managedForm, Composite parent) {
		this.managedForm = managedForm;
		this.toolkit = managedForm.getToolkit();
		
		//Composite container = toolkit.createComposite(parent);
	
		Section sctnMaster = toolkit.createSection(parent,
				ExpandableComposite.EXPANDED | ExpandableComposite.TITLE_BAR);
		sctnMaster.setText(getMasterSectionTitle());
		sctnMaster.setLayoutData(new GridData(GridData.FILL_BOTH));
		//
		Composite composite = toolkit.createComposite(sctnMaster, SWT.NONE);
		toolkit.paintBordersFor(composite);
		sctnMaster.setClient(composite);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Control list = createItemsList(composite);
		GridData gd_list = new GridData(GridData.FILL_BOTH);
		gd_list.verticalSpan = 2;
		gd_list.widthHint = 100;
		gd_list.heightHint = 100;
		list.setLayoutData(gd_list);
		
		createButtons(composite);
		
		masterPart = new SectionPart(sctnMaster);
		managedForm.addPart(masterPart);
	}
	
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		managedForm.fireSelectionChanged(masterPart, event.getSelection());
	}
	
	protected abstract String getMasterSectionTitle();
	
	protected abstract Control createItemsList(Composite parent);
	
	protected void createButtons(Composite parent) {
	}

}
