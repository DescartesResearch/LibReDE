package edu.kit.ipd.descartes.librede.ui.forms.master;

import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.LibredeConfiguration;

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

import edu.kit.ipd.descartes.librede.ui.forms.AbstractEstimationConfigurationFormPage;
import edu.kit.ipd.descartes.librede.ui.forms.MeasurementDataFormPage;

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
		
		Section sctnMaster = toolkit.createSection(parent,
				ExpandableComposite.EXPANDED | ExpandableComposite.TITLE_BAR);
		sctnMaster.setText(getMasterSectionTitle());
		//
		Composite composite = toolkit.createComposite(sctnMaster, SWT.NONE);
		toolkit.paintBordersFor(composite);
		sctnMaster.setClient(composite);
		composite.setLayout(new GridLayout(2, false));
		
		Control list = createItemsList(composite);
		GridData gd_list = new GridData();
		gd_list.verticalSpan = 2;
		gd_list.verticalAlignment = SWT.FILL;
		gd_list.horizontalAlignment = SWT.FILL;
		gd_list.grabExcessHorizontalSpace = true;
		gd_list.grabExcessVerticalSpace = true;
		list.setLayoutData(gd_list);
		
		Button btnAdd = toolkit.createButton(composite, "Add...", SWT.NONE);
		GridData gd_btnAdd = new GridData();
		gd_btnAdd.widthHint = 90;
		gd_btnAdd.verticalAlignment = SWT.TOP;
		btnAdd.setLayoutData(gd_btnAdd);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleAdd();
			}			
		});
		
		Button btnRemove = toolkit.createButton(composite, "Remove", SWT.NONE);
		GridData gd_btnRemove = new GridData();
		gd_btnRemove.widthHint = 90;
		gd_btnRemove.verticalAlignment = SWT.TOP;
		btnRemove.setLayoutData(gd_btnRemove);
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleRemove();
			}			
		});
		
		masterPart = new SectionPart(sctnMaster);
		managedForm.addPart(masterPart);
	}
	
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		managedForm.fireSelectionChanged(masterPart, event.getSelection());
	}
	
	protected abstract String getMasterSectionTitle();
	
	protected abstract Control createItemsList(Composite parent);
	
	protected abstract void handleAdd();
	
	protected abstract void handleRemove();

}
