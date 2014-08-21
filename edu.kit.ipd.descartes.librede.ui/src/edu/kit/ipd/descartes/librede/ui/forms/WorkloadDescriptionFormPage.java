package edu.kit.ipd.descartes.librede.ui.forms;

import net.descartesresearch.librede.configuration.ConfigurationFactory;
import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.Resource;
import net.descartesresearch.librede.configuration.Service;

import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.CellEditorProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapCellLabelProvider;
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
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
	public WorkloadDescriptionFormPage(FormEditor editor, String id,
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
		Composite body = form.getBody();
		toolkit.decorateFormHeading(form.getForm());
		toolkit.paintBordersFor(body);
		
		TableWrapLayout tableWrapLayout = new TableWrapLayout();
		tableWrapLayout.numColumns = 2;
		managedForm.getForm().getBody().setLayout(tableWrapLayout);
		
		sctnResources = managedForm.getToolkit().createSection(managedForm.getForm().getBody(), Section.TWISTIE | Section.TITLE_BAR);
		TableWrapData twd_sctnResources = new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1);
		sctnResources.setLayoutData(twd_sctnResources);
		managedForm.getToolkit().paintBordersFor(sctnResources);
		sctnResources.setText("Resources");
		sctnResources.setExpanded(true);
		
		resourcesComposite = managedForm.getToolkit().createComposite(sctnResources, SWT.NONE);
		sctnResources.setClient(resourcesComposite);
		managedForm.getToolkit().paintBordersFor(resourcesComposite);
		resourcesComposite.setLayout(new FormLayout());
		
		Composite tableComposite = new Composite(resourcesComposite, SWT.NONE);
		
		FormData fd_tableComposite_1 = new FormData();
		fd_tableComposite_1.height = 200;
		fd_tableComposite_1.left = new FormAttachment(0);
		fd_tableComposite_1.top = new FormAttachment(0);
		fd_tableComposite_1.bottom = new FormAttachment(100);
		tableComposite.setLayoutData(fd_tableComposite_1);
		
		tblViewerResources = new TableViewer(tableComposite, SWT.BORDER | SWT.FULL_SELECTION);
		tblResources = tblViewerResources.getTable();
		tblResources.setHeaderVisible(true);
		tblResources.setLinesVisible(true);
		
		initTableFromEMF(tableComposite, tblViewerResources, 
				FeaturePath.fromList(ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__WORKLOAD_DESCRIPTION, ConfigurationPackage.Literals.WORKLOAD_DESCRIPTION__RESOURCES), 
				new EAttribute[] {ConfigurationPackage.Literals.RESOURCE__NAME, ConfigurationPackage.Literals.RESOURCE__NUMBER_OF_SERVERS, ConfigurationPackage.Literals.RESOURCE__SCHEDULING_STRATEGY}, 
				new String[] { "Name", "Number of Servers", "Scheduling Strategy"});
		
				
		btnNewResource = new Button(resourcesComposite, SWT.NONE);
		fd_tableComposite_1.right = new FormAttachment(btnNewResource, -6);
		FormData fd_btnNewResource = new FormData();
		fd_btnNewResource.width = 70;
		fd_btnNewResource.right = new FormAttachment(100);
		btnNewResource.setLayoutData(fd_btnNewResource);
		btnNewResource.setText("New");
		btnNewResource.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Resource res = ConfigurationFactory.eINSTANCE.createResource();
				res.setName("New Resource");
				
				Command add = AddCommand.create(getEditingDomain(), 
						getModel().getWorkloadDescription(), 
						ConfigurationPackage.Literals.WORKLOAD_DESCRIPTION__RESOURCES, 
						res);				

				getEditingDomain().getCommandStack().execute(add);
			}
		});
		
		btnRemoveResource = new Button(resourcesComposite, SWT.NONE);
		FormData fd_btnRemoveResource = new FormData();
		fd_btnRemoveResource.width = 70;
		fd_btnRemoveResource.top = new FormAttachment(btnNewResource, 6);
		fd_btnRemoveResource.right = new FormAttachment(100);
		btnRemoveResource.setLayoutData(fd_btnRemoveResource);
		btnRemoveResource.setText("Remove");
		btnRemoveResource.addSelectionListener(new RemoveSelectionSelectionListener(tblViewerResources));
		
		sctnServices = managedForm.getToolkit().createSection(managedForm.getForm().getBody(), Section.TWISTIE | Section.TITLE_BAR);
		TableWrapData twd_sctnServices = new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1);
		sctnServices.setLayoutData(twd_sctnServices);
		managedForm.getToolkit().paintBordersFor(sctnServices);
		sctnServices.setText("Services");
		sctnServices.setExpanded(true);
		
		Composite wclComposite = managedForm.getToolkit().createComposite(sctnServices, SWT.NONE);
		sctnServices.setClient(wclComposite);
		managedForm.getToolkit().paintBordersFor(wclComposite);
		wclComposite.setLayout(new FormLayout());
		
		Composite tableComposite_1 = new Composite(wclComposite, SWT.NONE);
		
		FormData fd_tableComposite_1_1 = new FormData();
		fd_tableComposite_1_1.height = 200;
		fd_tableComposite_1_1.bottom = new FormAttachment(100);
		fd_tableComposite_1_1.right = new FormAttachment(100);
		fd_tableComposite_1_1.top = new FormAttachment(0);
		fd_tableComposite_1_1.left = new FormAttachment(0);
		tableComposite_1.setLayoutData(fd_tableComposite_1_1);
		
		tblViewerServices = new TableViewer(tableComposite_1, SWT.BORDER | SWT.FULL_SELECTION);
		tblClasses = tblViewerServices.getTable();
		tblClasses.setHeaderVisible(true);
		tblClasses.setLinesVisible(true);
		
		initTableFromEMF(tableComposite_1, tblViewerServices, 
				FeaturePath.fromList(ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__WORKLOAD_DESCRIPTION, ConfigurationPackage.Literals.WORKLOAD_DESCRIPTION__SERVICES), 
				new EAttribute[] {ConfigurationPackage.Literals.SERVICE__NAME}, 
				new String[] { "Name" });
		
		btnNewClass = new Button(wclComposite, SWT.NONE);
		fd_tableComposite_1_1.right = new FormAttachment(btnNewClass, -6);
		FormData fd_btnNewClass = new FormData();
		fd_btnNewClass.width = 70;
		fd_btnNewClass.right = new FormAttachment(100);
		btnNewClass.setLayoutData(fd_btnNewClass);
		btnNewClass.setText("New");
		btnNewClass.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Service service = ConfigurationFactory.eINSTANCE.createService();
				service.setName("New Service");
				
				Command add = AddCommand.create(getEditingDomain(), 
						getModel().getWorkloadDescription(), 
						ConfigurationPackage.Literals.WORKLOAD_DESCRIPTION__SERVICES, 
						service);				

				getEditingDomain().getCommandStack().execute(add);
			}
		});
		
		btnRemoveClass = new Button(wclComposite, SWT.NONE);
		FormData fd_btnRemoveClass = new FormData();
		fd_btnRemoveClass.width = 70;
		fd_btnRemoveClass.top = new FormAttachment(btnNewClass, 6);
		fd_btnRemoveClass.right = new FormAttachment(100);
		btnRemoveClass.setLayoutData(fd_btnRemoveClass);
		btnRemoveClass.setText("Remove");
		btnRemoveClass.addSelectionListener(new RemoveSelectionSelectionListener(tblViewerServices));
		
		sctnImport = managedForm.getToolkit().createSection(managedForm.getForm().getBody(), Section.TWISTIE | Section.TITLE_BAR);
		sctnImport.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 2));
		managedForm.getToolkit().paintBordersFor(sctnImport);
		sctnImport.setText("Import");
		sctnImport.setExpanded(true);
	}
	

	private void initTableFromEMF(Composite tableComposite, TableViewer tableViewer, FeaturePath object, EAttribute[] attributes, String[] headers) {
		ObservableListContentProvider cp = new ObservableListContentProvider();
		TableColumnLayout colLayout = new TableColumnLayout();
		tableComposite.setLayout(colLayout);
		
		int i = 0;
		for (EAttribute attr : attributes) {
			// Binding model 
			IValueProperty property = EMFEditProperties.value(getEditingDomain(), attr);			
			
			// Create column
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
			TableColumn tableColumn = tableViewerColumn.getColumn();
			colLayout.setColumnData(tableColumn, new ColumnPixelData(150));
			tableColumn.setText(headers[i]);
			
			// Initialize presentation and editing support
			tableViewerColumn.setLabelProvider(new ObservableMapCellLabelProvider(property.observeDetail(cp.getKnownElements())));
			
			// If attribute is enum create special combobox editor
			if (attributes[i].getEType() instanceof EEnum) {
				tableViewerColumn.setEditingSupport(new AbstractEstimationConfigurationFormPage.ComboBoxFromEnumEditingSupport(
						tableViewerColumn.getViewer(), bindingContext, property));
			} else {
				tableViewerColumn.setEditingSupport(ObservableValueEditingSupport.create(
						tableViewerColumn.getViewer(), 
						bindingContext, 
						new TextCellEditor(tableViewer.getTable()), 
						CellEditorProperties.control().value(WidgetProperties.text(SWT.Modify)), 
						property));
			}
			i++;
		}		
		
		tableViewer.setContentProvider(cp);
		tableViewer.setInput(EMFEditProperties.list(getEditingDomain(), object).observe(getModel()));
	}
}
