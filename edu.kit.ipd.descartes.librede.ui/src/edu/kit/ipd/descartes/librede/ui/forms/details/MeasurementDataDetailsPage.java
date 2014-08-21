package edu.kit.ipd.descartes.librede.ui.forms.details;

import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.Resource;
import net.descartesresearch.librede.configuration.TimeSeries;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.GridData;

public class MeasurementDataDetailsPage implements IDetailsPage {

	private IManagedForm managedForm;
	private EditingDomain domain;
	private LibredeConfiguration model;
	private TimeSeries input;
	private Label lblMetric;
	private Text txtMetric;
	private Label lblInterval;
	private Spinner spnIntervalValue;
	private CCombo comboIntervalUnit;
	private Label lblUnit;
	private Combo comboUnit;
	private ComboViewer comboUnitViewer;
	private Label lblResources;
	private Table resourcesTable;
	private CheckboxTableViewer resourcesTableViewer;
	private Label lblServices;
	private Table servicesTable;
	private CheckboxTableViewer servicesTableViewer;
	private Label lblDataProvider;
	private Combo comboDataProvider;
	private ComboViewer comboDataProviderViewer;
	private Composite composite_1;

	private EMFDataBindingContext detailBindingContext;

	/**
	 * Create the details page.
	 */
	public MeasurementDataDetailsPage(EditingDomain domain, LibredeConfiguration model) {
		// Create the details page
		this.domain = domain;
		this.model = model;
	}

	/**
	 * Initialize the details page.
	 * @param form
	 */
	public void initialize(IManagedForm form) {
		managedForm = form;
	}

	/**
	 * Create contents of the details page.
	 * @param parent
	 */
	public void createContents(Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();
		parent.setLayout(new FillLayout());
		//		
		Section sctnMeasurementTraceDetails = toolkit.createSection(parent,
				ExpandableComposite.EXPANDED | ExpandableComposite.TITLE_BAR);
		sctnMeasurementTraceDetails.setText("Measurement Trace Details");
		//
		Composite composite = toolkit.createComposite(sctnMeasurementTraceDetails, SWT.NONE);
		toolkit.paintBordersFor(composite);
		sctnMeasurementTraceDetails.setClient(composite);
		composite.setLayout(new GridLayout(2, false));
		
		lblDataProvider = toolkit.createLabel(composite, "Data Provider:", SWT.NONE);
		
		comboDataProviderViewer = new ComboViewer(composite, SWT.READ_ONLY);
		comboDataProvider = comboDataProviderViewer.getCombo();
		comboDataProvider.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		toolkit.paintBordersFor(comboDataProvider);
		
		comboDataProviderViewer.setContentProvider(new ObservableListContentProvider());
		comboDataProviderViewer.setInput(EMFEditProperties.list(domain, FeaturePath.fromList(
				ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__INPUT, 
				ConfigurationPackage.Literals.INPUT_SPECIFICATION__DATA_SOURCES))
				.observe(model));
		
		lblMetric = toolkit.createLabel(composite, "Metric:", SWT.NONE);
		
		txtMetric = new Text(composite, SWT.BORDER);
		txtMetric.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txtMetric.setEditable(false);
		toolkit.adapt(txtMetric, true, true);
		
		lblInterval = toolkit.createLabel(composite, "Interval:", SWT.NONE);
		
		composite_1 = toolkit.createComposite(composite, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		toolkit.paintBordersFor(composite_1);
		RowLayout rl_composite_1 = new RowLayout(SWT.HORIZONTAL);
		rl_composite_1.marginTop = 0;
		rl_composite_1.marginRight = 0;
		rl_composite_1.marginLeft = 0;
		rl_composite_1.marginBottom = 0;
		composite_1.setLayout(rl_composite_1);
		
		spnIntervalValue = new Spinner(composite_1, SWT.BORDER);
		toolkit.adapt(spnIntervalValue);
		toolkit.paintBordersFor(spnIntervalValue);
		
		comboIntervalUnit = new CCombo(composite_1, SWT.BORDER);
		toolkit.adapt(comboIntervalUnit);
		toolkit.paintBordersFor(comboIntervalUnit);
		
		lblUnit = toolkit.createLabel(composite, "Unit:", SWT.NONE);
		
		comboUnitViewer = new ComboViewer(composite, SWT.NONE);
		comboUnit = comboUnitViewer.getCombo();
		comboUnit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		toolkit.paintBordersFor(comboUnit);
		
		lblResources = toolkit.createLabel(composite, "Resources:", SWT.NONE);
		
		resourcesTableViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER | SWT.FULL_SELECTION);
		resourcesTable = resourcesTableViewer.getTable();
		resourcesTable.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		toolkit.paintBordersFor(resourcesTable);
		
		resourcesTableViewer.setContentProvider(new ObservableListContentProvider());
		resourcesTableViewer.setLabelProvider(new LabelProvider());
		resourcesTableViewer.setInput(EMFEditProperties.list(domain, 
				FeaturePath.fromList(ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__WORKLOAD_DESCRIPTION, ConfigurationPackage.Literals.WORKLOAD_DESCRIPTION__RESOURCES))
				.observe(model));
		
		lblServices = toolkit.createLabel(composite, "Services:", SWT.NONE);
		
		servicesTableViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER | SWT.FULL_SELECTION);
		servicesTable = servicesTableViewer.getTable();
		servicesTable.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		toolkit.paintBordersFor(servicesTable);
		
		servicesTableViewer.setContentProvider(new ObservableListContentProvider());
		servicesTableViewer.setLabelProvider(new LabelProvider());
		servicesTableViewer.setInput(EMFEditProperties.list(domain, 
				FeaturePath.fromList(ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__WORKLOAD_DESCRIPTION, ConfigurationPackage.Literals.WORKLOAD_DESCRIPTION__SERVICES))
				.observe(model));
	}
	
	private void createBindings() {
		if (detailBindingContext != null) {
			detailBindingContext.dispose();
		}
		detailBindingContext = new EMFDataBindingContext();
		detailBindingContext.bindValue(WidgetProperties.text().observe(txtMetric), 
				EMFEditProperties.value(domain, ConfigurationPackage.Literals.TIME_SERIES__METRIC).observe(input));
		detailBindingContext.bindValue(WidgetProperties.selection().observe(spnIntervalValue), 
				EMFEditProperties.value(domain, ConfigurationPackage.Literals.TIME_SERIES__INTERVAL).observe(input));
		detailBindingContext.bindSet(ViewerProperties.checkedElements(Resource.class).observe(resourcesTableViewer), 
				EMFEditProperties.set(domain, ConfigurationPackage.Literals.TIME_SERIES__RESOURCES).observe(input));

	}

	public void dispose() {
		// Dispose
	}

	public void setFocus() {
		// Set focus
	}

	private void update() {
	}

	public boolean setFormInput(Object input) {
		return false;
	}

	public void selectionChanged(IFormPart part, ISelection selection) {
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		if (structuredSelection.size() == 1) {
			input = (TimeSeries) structuredSelection.getFirstElement();
			createBindings();
		} else {
			input = null;
		}
		update();
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
