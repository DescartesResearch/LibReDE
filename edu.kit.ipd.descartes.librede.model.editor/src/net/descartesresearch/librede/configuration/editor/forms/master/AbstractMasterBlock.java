package net.descartesresearch.librede.configuration.editor.forms.master;

import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.editor.forms.AbstractEstimationConfigurationFormPage;
import net.descartesresearch.librede.configuration.editor.forms.MeasurementDataFormPage;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public abstract class AbstractMasterBlock extends MasterDetailsBlock implements ISelectionChangedListener {
	
	protected IManagedForm managedForm;
	protected FormToolkit toolkit;
	protected AbstractEstimationConfigurationFormPage page;
	protected SectionPart masterPart;
	protected EditingDomain domain;
	protected LibredeConfiguration model;
	
	public AbstractMasterBlock(AbstractEstimationConfigurationFormPage page, EditingDomain domain, LibredeConfiguration model) {
		this.page = page;
		this.domain = domain;
		this.model = model;
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
		gd_list.widthHint = 50;
		gd_list.heightHint = 50;
		list.setLayoutData(gd_list);
		
		createButtons(composite);
		
		masterPart = new SectionPart(sctnMaster);
		managedForm.addPart(masterPart);
	}
	
	@Override
	public void createContent(IManagedForm managedForm) {
		super.createContent(managedForm);
		 GridLayout layout = new GridLayout(1, false);
		 // Values from org.eclipse.pde.internal.ui.editor.FormLayoutFactory
		 layout.marginHeight = 0;
		 layout.marginWidth = 0;
		 layout.marginTop = 12;
		 layout.marginBottom = 12;
		 layout.marginLeft = 6;
		 layout.marginRight = 6;
		 layout.horizontalSpacing = 20;
		 layout.verticalSpacing = 17;
		 managedForm.getForm().getBody().setLayout(layout);
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
