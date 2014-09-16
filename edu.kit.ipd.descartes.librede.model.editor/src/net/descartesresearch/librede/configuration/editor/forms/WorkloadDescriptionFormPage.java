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
package net.descartesresearch.librede.configuration.editor.forms;

import java.io.ObjectInputStream.GetField;

import net.descartesresearch.librede.configuration.ConfigurationFactory;
import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.Resource;
import net.descartesresearch.librede.configuration.Service;
import net.descartesresearch.librede.configuration.WorkloadDescription;
import net.descartesresearch.librede.configuration.actions.RunEstimationAction;
import net.descartesresearch.librede.configuration.presentation.ConfigurationEditor;
import net.descartesresearch.librede.configuration.presentation.LibredeEditorPlugin;

import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.CellEditorProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapCellLabelProvider;
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellEditor.LayoutData;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class WorkloadDescriptionFormPage extends AbstractEstimationConfigurationFormPage {

	private Table tblResources;
	private Table tblClasses;
	private Composite resourcesComposite;
	private Button btnNewResource;
	private Button btnRemoveResource;
	private Button btnNewClass;
	private Button btnRemoveClass;
	private Section sctnResources;
	private Section sctnServices;
	private TableViewer tblViewerResources;
	private TableViewer tblViewerServices;
	
	private EMFDataBindingContext bindingContext = new EMFDataBindingContext();
	private Section sctnImport;
	
	/**
	 * Create the form page.
	 * @param editor
	 * @param id
	 * @param title
	 * @wbp.parser.constructor
	 * @wbp.eval.method.parameter id "Some id"
	 * @wbp.eval.method.parameter title "Some title"
	 */
	public WorkloadDescriptionFormPage(ConfigurationEditor editor, String id,
			String title, AdapterFactoryEditingDomain editingDomain, LibredeConfiguration model) {
		super(editor, id, title, editingDomain, model);
	}

	/**
	 * Create contents of the form.
	 * @param managedForm
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		managedForm.getForm().setText("Workload Description");
		FormToolkit toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();
		form.setImage(ExtendedImageRegistry.INSTANCE.getImage(LibredeEditorPlugin.getPlugin().getImage("full/page/WorkloadDescription")));
		Composite body = form.getBody();
		toolkit.decorateFormHeading(form.getForm());
		toolkit.paintBordersFor(body);
		
		// Add run estimation toolbar button
		form.getToolBarManager().add(new RunEstimationAction(getModel()));
		form.getToolBarManager().update(true);
		
		TableWrapLayout tableWrapLayout = new TableWrapLayout();
		tableWrapLayout.numColumns = 2;
		tableWrapLayout.makeColumnsEqualWidth = true;
		tableWrapLayout.bottomMargin = 12;
		tableWrapLayout.topMargin = 12;
		tableWrapLayout.leftMargin = 6;
		tableWrapLayout.rightMargin = 6;
		tableWrapLayout.verticalSpacing = 17;
		tableWrapLayout.horizontalSpacing = 20;
		managedForm.getForm().getBody().setLayout(tableWrapLayout);
		
		createServicesSection(managedForm);
		
		createResourcesSection(managedForm);
		
//		sctnImport = managedForm.getToolkit().createSection(managedForm.getForm().getBody(), Section.TWISTIE | Section.TITLE_BAR);
//		sctnImport.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 2));
//		managedForm.getToolkit().paintBordersFor(sctnImport);
//		sctnImport.setText("Import");
//		sctnImport.setExpanded(true);
	}

	private void createResourcesSection(IManagedForm managedForm) {
		sctnResources = managedForm.getToolkit().createSection(managedForm.getForm().getBody(), Section.TWISTIE | Section.TITLE_BAR | Section.DESCRIPTION);
		TableWrapData twd_sctnResources = new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.FILL_GRAB, 1, 1);
		sctnResources.setLayoutData(twd_sctnResources);
		managedForm.getToolkit().paintBordersFor(sctnResources);
		sctnResources.setText("Resources");
		sctnResources.descriptionVerticalSpacing = 10;
		sctnResources.setDescription("List all processing resources for which resource demands should be determined.");
		sctnResources.setExpanded(true);
		
		resourcesComposite = managedForm.getToolkit().createComposite(sctnResources, SWT.NONE);
		sctnResources.setClient(resourcesComposite);
		managedForm.getToolkit().paintBordersFor(resourcesComposite);
		resourcesComposite.setLayout(new GridLayout(2, false));
		
		Composite tableComposite = new Composite(resourcesComposite, SWT.NONE);
		
		GridData gd_tableComposite_1 = new GridData(GridData.FILL_BOTH);
		gd_tableComposite_1.heightHint = 200;
		gd_tableComposite_1.widthHint = 50;
		gd_tableComposite_1.verticalSpan = 2;
		tableComposite.setLayoutData(gd_tableComposite_1);
		
		tblViewerResources = new TableViewer(tableComposite, SWT.BORDER | SWT.FULL_SELECTION);
		tblResources = tblViewerResources.getTable();
		tblResources.setHeaderVisible(true);
		tblResources.setLinesVisible(true);
		
		initTableFromEMF(tableComposite, tblViewerResources, 
				Resource.class,
				new EStructuralFeature[] { ConfigurationPackage.Literals.NAMED_ELEMENT__NAME, ConfigurationPackage.Literals.RESOURCE__NUMBER_OF_SERVERS, ConfigurationPackage.Literals.RESOURCE__SCHEDULING_STRATEGY },
				new String[] { "Name", "Number of Servers", "Scheduling Strategy"});
		
				
		btnNewResource = new Button(resourcesComposite, SWT.NONE);
		GridData gd_btnNewResource = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd_btnNewResource.widthHint = 90;
		btnNewResource.setLayoutData(gd_btnNewResource);
		btnNewResource.setText("Add");
		btnNewResource.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleAddResource();
			}
		});
		
		btnRemoveResource = new Button(resourcesComposite, SWT.NONE);
		GridData gd_btnRemoveResource = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd_btnRemoveResource.widthHint = 90;
		btnRemoveResource.setLayoutData(gd_btnRemoveResource);
		btnRemoveResource.setText("Remove");
		btnRemoveResource.addSelectionListener(new RemoveSelectionSelectionListener(tblViewerResources));
	}

	private void createServicesSection(IManagedForm managedForm) {
		sctnServices = managedForm.getToolkit().createSection(managedForm.getForm().getBody(), Section.TWISTIE | Section.TITLE_BAR | Section.DESCRIPTION);
		TableWrapData twd_sctnServices = new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.FILL_GRAB, 1, 1);
		sctnServices.setLayoutData(twd_sctnServices);
		managedForm.getToolkit().paintBordersFor(sctnServices);
		sctnServices.descriptionVerticalSpacing = 10;
		sctnServices.setDescription("Services (or workload classes) are groups of requests with similar resource demand behaviors.");
		sctnServices.setText("Services");
		sctnServices.setExpanded(true);
		
		Composite wclComposite = managedForm.getToolkit().createComposite(sctnServices, SWT.NONE);
		sctnServices.setClient(wclComposite);
		managedForm.getToolkit().paintBordersFor(wclComposite);
		wclComposite.setLayout(new GridLayout(2, false));
		
		Composite tableComposite_1 = new Composite(wclComposite, SWT.NONE);
		
		GridData gd_tableComposite_1_1 = new GridData(GridData.FILL_BOTH);
		gd_tableComposite_1_1.heightHint = 200;
		gd_tableComposite_1_1.widthHint = 50;
		gd_tableComposite_1_1.verticalSpan = 2;
		tableComposite_1.setLayoutData(gd_tableComposite_1_1);
		
		tblViewerServices = new TableViewer(tableComposite_1, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		tblClasses = tblViewerServices.getTable();
		tblClasses.setHeaderVisible(true);
		tblClasses.setLinesVisible(true);
		
		initTableFromEMF(tableComposite_1, tblViewerServices, 
				Service.class, 
				new EStructuralFeature[] { ConfigurationPackage.Literals.NAMED_ELEMENT__NAME },
				new String[] { "Name" });
		
		btnNewClass = new Button(wclComposite, SWT.NONE);
		GridData gd_btnNewClass = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd_btnNewClass.widthHint = 90;
		btnNewClass.setLayoutData(gd_btnNewClass);
		btnNewClass.setText("Add");
		btnNewClass.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleAddService();
			}
		});
		
		btnRemoveClass = new Button(wclComposite, SWT.NONE);
		GridData gd_btnRemoveClass = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd_btnRemoveClass.widthHint = 90;
		btnRemoveClass.setLayoutData(gd_btnRemoveClass);
		btnRemoveClass.setText("Remove");
		btnRemoveClass.addSelectionListener(new RemoveSelectionSelectionListener(tblViewerServices));
	}
	

	private void initTableFromEMF(Composite tableComposite, TableViewer tableViewer, Class<?> objectType, EStructuralFeature[] attributes, String[] headers) {
		TableColumnLayout colLayout = new TableColumnLayout();
		tableComposite.setLayout(colLayout);
		
		for (int i = 0; i < headers.length; i++) {
			// Create column
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
			TableColumn tableColumn = tableViewerColumn.getColumn();
			colLayout.setColumnData(tableColumn, new ColumnWeightData(10, 50, true));
			tableColumn.setText(headers[i]);
			tableViewerColumn.setEditingSupport(EObjectEditingSupport.create(tableViewer, getEditingDomain(), attributes[i]));
		}
		tableViewer.setContentProvider(new AdapterFactoryContentProvider(getAdapterFactory()));
		tableViewer.setLabelProvider(new AdapterFactoryLabelProvider(getAdapterFactory()));
		tableViewer.addFilter(new ClassesViewerFilter(objectType));
		tableViewer.setInput(getModel().getWorkloadDescription());
	}

	private void handleAddResource() {
		Resource res = ConfigurationFactory.eINSTANCE.createResource();
		res.setName("New Resource");
		
		Command cmd;
		if (getModel().getWorkloadDescription() == null) {
			WorkloadDescription description = ConfigurationFactory.eINSTANCE.createWorkloadDescription();
			description.getResources().add(res);
			cmd = SetCommand.create(getEditingDomain(), 
					getModel(), 
					ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__WORKLOAD_DESCRIPTION, 
					description);
		} else {
			cmd = AddCommand.create(getEditingDomain(), 
					getModel().getWorkloadDescription(), 
					ConfigurationPackage.Literals.WORKLOAD_DESCRIPTION__RESOURCES, 
					res);	
		}
		getEditingDomain().getCommandStack().execute(cmd);
	}
	
	private void handleAddService() {
		Service service = ConfigurationFactory.eINSTANCE.createService();
		service.setName("New Service");
		
		Command cmd;
		if (getModel().getWorkloadDescription() == null) {
			WorkloadDescription description = ConfigurationFactory.eINSTANCE.createWorkloadDescription();
			description.getServices().add(service);
			cmd = SetCommand.create(getEditingDomain(), 
					getModel(), 
					ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__WORKLOAD_DESCRIPTION, 
					description);
		} else {
			cmd = AddCommand.create(getEditingDomain(), 
					getModel().getWorkloadDescription(), 
					ConfigurationPackage.Literals.WORKLOAD_DESCRIPTION__SERVICES, 
					service);	
		}
		getEditingDomain().getCommandStack().execute(cmd);
	}
}
