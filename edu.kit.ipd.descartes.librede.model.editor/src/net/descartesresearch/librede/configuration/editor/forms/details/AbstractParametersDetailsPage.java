package net.descartesresearch.librede.configuration.editor.forms.details;

import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.editor.forms.AbstractEstimationConfigurationFormPage;
import net.descartesresearch.librede.configuration.editor.forms.ParametersBlock;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public abstract class AbstractParametersDetailsPage extends AbstractDetailsPage {
	
	private ParametersBlock parameters;	
	private Class<?> contentType;

	public AbstractParametersDetailsPage(
			AbstractEstimationConfigurationFormPage page, EditingDomain domain,
			LibredeConfiguration model, Class<?> contentType) {
		super(page, domain, model);
		this.contentType = contentType;
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
		section.setText(getDetailsSectionTitle());
		//
		Composite composite = toolkit.createComposite(section, SWT.NONE);
		toolkit.paintBordersFor(composite);
		section.setClient(composite);
		
		parameters = new ParametersBlock();
		parameters.setObjectType(contentType);
		parameters.createControl(toolkit, page.getSite().getShell(), composite);
	}

	@Override
	public void selectionChanged(IFormPart part, ISelection selection) {
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		if (structuredSelection.size() == 1) {
			parameters.setInput(getParameterList(structuredSelection.getFirstElement()));
		} else {
			parameters.setInput(null);
		}
		update();
	}
	
	protected abstract Object getParameterList(Object element);
	
	protected abstract String getDetailsSectionTitle();
}
