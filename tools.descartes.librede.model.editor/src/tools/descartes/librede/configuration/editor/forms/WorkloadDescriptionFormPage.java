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

import java.util.Arrays;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.internal.runtime.DataArea;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import tools.descartes.librede.configuration.ConfigurationFactory;
import tools.descartes.librede.configuration.ConfigurationPackage;
import tools.descartes.librede.configuration.LibredeConfiguration;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.configuration.actions.RunEstimationAction;
import tools.descartes.librede.configuration.presentation.ConfigurationEditor;
import tools.descartes.librede.configuration.presentation.LibredeEditorPlugin;

public class WorkloadDescriptionFormPage extends AbstractEstimationConfigurationFormPage {
	
	private static class DisableOnEmptySelection extends UpdateValueStrategy {
		@Override
		public Object convert(Object value) {
			return value != null;
		}
	}

	private Tree treeResources;
	private Tree treeClasses;
	private Composite resourcesComposite;
	private Button btnNewResource;
	private Button btnRemoveResource;
	private Button btnNewClass;
	private Button btnNewSubService;
	private Button btnNewCallService;
	private Button btnAddServiceMapping;
	private Button btnRemoveClass;
	private Section sctnResources;
	private Section sctnServices;
	private TreeViewer treeViewerResources;
	private TreeViewer treeViewerServices;
	
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
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.makeColumnsEqualWidth = true;
		gridLayout.marginBottom = 12;
		gridLayout.marginTop = 12;
		gridLayout.marginLeft = 6;
		gridLayout.marginRight = 6;
		gridLayout.verticalSpacing = 17;
		gridLayout.horizontalSpacing = 20;
		managedForm.getForm().getBody().setLayout(gridLayout);
		
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
		GridData gd_sctnResources = new GridData(GridData.FILL_BOTH);
		sctnResources.setLayoutData(gd_sctnResources);
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
		gd_tableComposite_1.grabExcessVerticalSpace = true;
		gd_tableComposite_1.widthHint = 50;
		gd_tableComposite_1.verticalSpan = 3;
		tableComposite.setLayoutData(gd_tableComposite_1);
		
		treeViewerResources = new TreeViewer(tableComposite, SWT.BORDER | SWT.FULL_SELECTION);
		treeResources = treeViewerResources.getTree();
		treeResources.setHeaderVisible(true);
		treeResources.setLinesVisible(true);
		
		initTreeFromEMF(tableComposite, treeViewerResources, 
				Resource.class,
				new EStructuralFeature[] { ConfigurationPackage.Literals.NAMED_ELEMENT__NAME, ConfigurationPackage.Literals.RESOURCE__NUMBER_OF_SERVERS, ConfigurationPackage.Literals.RESOURCE__SCHEDULING_STRATEGY },
				new String[] { "Name", "Number of Servers", "Scheduling Strategy"}, new int[] { 10, 0, 0});
		/*
		 * IMPORTANT: filter out resources below service entries (redundant)
		 */
		treeViewerResources.addFilter(new ClassesViewerFilter(Service.class, Resource.class));
				
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
		
		btnAddServiceMapping = new Button(resourcesComposite, SWT.NONE);
		GridData gd_btnAddServiceMapping = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd_btnAddServiceMapping.widthHint = 90;
		btnAddServiceMapping.setLayoutData(gd_btnAddServiceMapping);
		btnAddServiceMapping.setText("Add Mapping");
		btnAddServiceMapping.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleAddMappedService();
			}
		});
		
		btnRemoveResource = new Button(resourcesComposite, SWT.NONE);
		GridData gd_btnRemoveResource = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd_btnRemoveResource.widthHint = 90;
		btnRemoveResource.setLayoutData(gd_btnRemoveResource);
		btnRemoveResource.setText("Remove");
		btnRemoveResource.addSelectionListener(new RemoveSelectionSelectionListener(treeViewerResources));
		
		bindingContext.bindValue(WidgetProperties.enabled().observe(btnRemoveResource), ViewerProperties.singleSelection().observe(treeViewerResources)
		, null, new UpdateValueStrategy() {
			@Override
			public Object convert(Object value) {
				return value != null;
			}
		});
	}

	private void createServicesSection(IManagedForm managedForm) {
		sctnServices = managedForm.getToolkit().createSection(managedForm.getForm().getBody(), Section.TWISTIE | Section.TITLE_BAR | Section.DESCRIPTION);
		GridData gd_sctnServices = new GridData(GridData.FILL_BOTH);
		sctnServices.setLayoutData(gd_sctnServices);
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
		gd_tableComposite_1_1.verticalSpan = 4;
		tableComposite_1.setLayoutData(gd_tableComposite_1_1);
		
		treeViewerServices = new TreeViewer(tableComposite_1, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		treeClasses = treeViewerServices.getTree();
		treeClasses.setHeaderVisible(true);
		treeClasses.setLinesVisible(true);
		
		initTreeFromEMF(tableComposite_1, treeViewerServices, 
				Service.class, 
				new EStructuralFeature[] { ConfigurationPackage.Literals.NAMED_ELEMENT__NAME, ConfigurationPackage.Literals.SERVICE__BACKGROUND_SERVICE },
				new String[] { "Name", "Background Service" }, new int[] { 10, 0 });
		
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
		
		btnNewSubService = new Button(wclComposite, SWT.NONE);
		GridData gd_btnNewSubService = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd_btnNewSubService.widthHint = 90;
		btnNewSubService.setLayoutData(gd_btnNewSubService);
		btnNewSubService.setText("Add Child");
		btnNewSubService.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleAddSubService();
			}
		});
		
		btnNewCallService = new Button(wclComposite, SWT.NONE);
		GridData gd_btnNewCallService = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd_btnNewCallService.widthHint = 90;
		btnNewCallService.setLayoutData(gd_btnNewSubService);
		btnNewCallService.setText("Add Call");
		btnNewCallService.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleAddCalledService();
			}
		});
		
		btnRemoveClass = new Button(wclComposite, SWT.NONE);
		GridData gd_btnRemoveClass = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd_btnRemoveClass.widthHint = 90;
		btnRemoveClass.setLayoutData(gd_btnRemoveClass);
		btnRemoveClass.setText("Remove");
		btnRemoveClass.addSelectionListener(new RemoveSelectionSelectionListener(treeViewerServices));
		
		bindingContext.bindValue(WidgetProperties.enabled().observe(btnRemoveClass), ViewerProperties.singleSelection().observe(treeViewerServices), null, new DisableOnEmptySelection());
	}
	
	private void initTreeFromEMF(Composite treeComposite, TreeViewer treeViewer, Class<?> objectType, EStructuralFeature[] attributes, String[] headers, int[] weights) {
		TreeColumnLayout colLayout = new TreeColumnLayout();
		treeComposite.setLayout(colLayout);
		
		for (int i = 0; i < headers.length; i++) {
			// Create column
			TreeViewerColumn tableViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
			TreeColumn tableColumn = tableViewerColumn.getColumn();
			colLayout.setColumnData(tableColumn, new ColumnWeightData(weights[i], 130, true));
			tableColumn.setText(headers[i]);
			tableViewerColumn.setEditingSupport(EObjectEditingSupport.create(treeViewer, getEditingDomain(), attributes[i]));
		}
		treeViewer.setContentProvider(new AdapterFactoryContentProvider(getAdapterFactory()));
		treeViewer.setLabelProvider(new AdapterFactoryLabelProvider(getAdapterFactory()));
		treeViewer.addFilter(new ClassesViewerFilter(WorkloadDescription.class, objectType));
		treeViewer.setInput(getModel().getWorkloadDescription());
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
	
	private void handleAddSubService() {
		Service service = ConfigurationFactory.eINSTANCE.createService();
		service.setName("New Service");
		
		ISelection sel = treeViewerServices.getSelection();	
		if (sel instanceof IStructuredSelection) {
			if (!sel.isEmpty()) {
				EObject parent = (EObject) ((IStructuredSelection) sel).getFirstElement();
				Command cmd = AddCommand.create(getEditingDomain(), parent, ConfigurationPackage.Literals.SERVICE__SUB_SERVICES, service);
				getEditingDomain().getCommandStack().execute(cmd);
			}
		}		
	}
	
	private void handleAddCalledService() {		
		ISelection sel = treeViewerServices.getSelection();	
		if (sel instanceof IStructuredSelection) {			
			if (!sel.isEmpty()) {
				EObject parent = (EObject) ((IStructuredSelection) sel).getFirstElement();
				ElementListSelectionDialog dialog = createServiceSelectionDialog();
				if (dialog.open() == ElementListSelectionDialog.OK) {
					Object[] res = dialog.getResult();
					if (res != null && res.length > 0) {					
						Command cmd = AddCommand.create(getEditingDomain(), parent, ConfigurationPackage.Literals.SERVICE__CALLED_SERVICES, Arrays.asList(res));
						getEditingDomain().getCommandStack().execute(cmd);
					}
				}
			}
		}		
	}
	
	private void handleAddMappedService() {
		ISelection sel = treeViewerResources.getSelection();	
		if (sel instanceof IStructuredSelection) {			
			if (!sel.isEmpty()) {
				EObject parent = (EObject) ((IStructuredSelection) sel).getFirstElement();
				ElementListSelectionDialog dialog = createServiceSelectionDialog();
				if (dialog.open() == ElementListSelectionDialog.OK) {
					Object[] res = dialog.getResult();
					if (res != null && res.length > 0) {					
						Command cmd = AddCommand.create(getEditingDomain(), parent, ConfigurationPackage.Literals.RESOURCE__SERVICES, Arrays.asList(res));
						getEditingDomain().getCommandStack().execute(cmd);
					}
				}
			}
		}
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
	
	private ElementListSelectionDialog createServiceSelectionDialog() {
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(getEditorSite().getShell(), new AdapterFactoryLabelProvider(getAdapterFactory()));
		dialog.setHelpAvailable(false);
		dialog.setIgnoreCase(true);
		dialog.setTitle("Services");
		dialog.setMessage("Enter service name prefix or pattern (*, ?):");
		dialog.setMultipleSelection(true);
		dialog.setElements(getModel().getWorkloadDescription().getServices().toArray());
		return dialog;
	}
}
