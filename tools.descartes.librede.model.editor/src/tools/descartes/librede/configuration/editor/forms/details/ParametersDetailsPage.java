/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2018, by Simon Spinner, Johannes Grohmann
 *  and Contributors.
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
package tools.descartes.librede.configuration.editor.forms.details;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import tools.descartes.librede.configuration.ConfigurationPackage;
import tools.descartes.librede.configuration.NamedElement;
import tools.descartes.librede.configuration.editor.forms.AbstractEstimationConfigurationFormPage;
import tools.descartes.librede.configuration.editor.forms.ParametersBlock;

public class ParametersDetailsPage extends AbstractDetailsPage {
	
	private ParametersBlock parameters;
	private String sectionTitle;
	private EClass inputType;
	private String componentType;
	private EStructuralFeature parametersFeature;
	private Text txtName = null;
	
	private EMFDataBindingContext bindingContext = null;

	public ParametersDetailsPage(
			AbstractEstimationConfigurationFormPage page, AdapterFactoryEditingDomain domain,
			String sectionTitle, EClass inputType, String componentType, EStructuralFeature parametersFeature) {
		super(page, domain);
		this.sectionTitle = sectionTitle;
		this.inputType = inputType;
		this.componentType = componentType;
		this.parametersFeature = parametersFeature;
	}

	/**
	 * Create contents of the details page.
	 * @param parent
	 */
	@Override
	public void createContents(Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();
		parent.setLayout(new FillLayout());
		//		
		Section section = toolkit.createSection(parent,
				ExpandableComposite.EXPANDED | ExpandableComposite.TITLE_BAR);
		section.setText(sectionTitle);
		//
		Composite composite = toolkit.createComposite(section, SWT.NONE);	
		composite.setLayout(new GridLayout(2, false));
		toolkit.paintBordersFor(composite);
		section.setClient(composite);
		
		if (ConfigurationPackage.Literals.NAMED_ELEMENT.isSuperTypeOf(inputType)) {
			toolkit.createLabel(composite, "Name*:");
			txtName = toolkit.createText(composite, "");
			txtName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}
		
		parameters = new ParametersBlock(domain, parametersFeature);
		parameters.setObjectType(componentType);
		parameters.createControl(toolkit, page.getSite().getShell(), composite);
	}

	@Override
	public void selectionChanged(IFormPart part, ISelection selection) {
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		if (structuredSelection.size() == 1) {
			Object newVal = structuredSelection.getFirstElement();
			if (newVal instanceof NamedElement) {
				if (bindingContext != null) {
					bindingContext.dispose();
				}
				bindingContext = new EMFDataBindingContext();
				bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observeDelayed(1000, txtName), 
						EMFEditProperties.value(domain, ConfigurationPackage.Literals.NAMED_ELEMENT__NAME).observe(newVal));
			}
			parameters.setInput(newVal);
		} else {
			parameters.setInput(null);
			bindingContext.dispose();
			bindingContext = null;
		}
		update();
	}
}
