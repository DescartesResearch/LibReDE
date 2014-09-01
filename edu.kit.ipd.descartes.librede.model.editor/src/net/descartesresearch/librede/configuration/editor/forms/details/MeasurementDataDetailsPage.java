package net.descartesresearch.librede.configuration.editor.forms.details;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.descartesresearch.librede.configuration.ConfigurationFactory;
import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.DataProviderConfiguration;
import net.descartesresearch.librede.configuration.DataSourceConfiguration;
import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.Resource;
import net.descartesresearch.librede.configuration.Service;
import net.descartesresearch.librede.configuration.TraceConfiguration;
import net.descartesresearch.librede.configuration.editor.forms.AbstractEstimationConfigurationFormPage;
import net.descartesresearch.librede.configuration.editor.forms.ParametersBlock;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import edu.kit.ipd.descartes.librede.factory.ComponentRegistry;

public class MeasurementDataDetailsPage extends AbstractDetailsPage {

	private TraceConfiguration input;
	private Section sctnMeasurementTraceDetails;
	private Label lblMetric;
	private Text txtMetric;
	private Label lblInterval;
	private Spinner spnIntervalValue;
	private Combo comboIntervalUnit;
	private ComboViewer comboIntervalUnitViewer;
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
	private Composite dataItemConfgigurationComposite;
	private StackLayout layoutDataItemConfigurationComposite;

	private Map<Class<?>, ParametersBlock> dataItemParameterBlocks = new HashMap<>();
	private Map<Class<?>, Composite> dataItemParameterComposites = new HashMap<>();

	private EMFDataBindingContext detailBindingContext;

	/**
	 * Create the details page.
	 */
	public MeasurementDataDetailsPage(
			AbstractEstimationConfigurationFormPage page, EditingDomain domain,
			LibredeConfiguration model) {
		super(page, domain, model);
	}

	/**
	 * Create contents of the details page.
	 * 
	 * @param parent
	 */
	public void createContents(Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();
		parent.setLayout(new FillLayout());
		//
		sctnMeasurementTraceDetails = toolkit.createSection(parent,
				ExpandableComposite.EXPANDED | ExpandableComposite.TITLE_BAR);
		sctnMeasurementTraceDetails.setText("Measurement Trace Details");
		//
		Composite composite = toolkit.createComposite(
				sctnMeasurementTraceDetails, SWT.NONE);
		toolkit.paintBordersFor(composite);
		sctnMeasurementTraceDetails.setClient(composite);
		composite.setLayout(new GridLayout(2, false));

		lblDataProvider = toolkit.createLabel(composite, "Data Provider:",
				SWT.NONE);

		comboDataProviderViewer = new ComboViewer(composite, SWT.READ_ONLY);
		comboDataProvider = comboDataProviderViewer.getCombo();
		comboDataProvider.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		toolkit.paintBordersFor(comboDataProvider);

		comboDataProviderViewer
				.setContentProvider(new ObservableListContentProvider());
		comboDataProviderViewer.setLabelProvider(new AdapterFactoryLabelProvider(page.getAdapterFactory()));
		comboDataProviderViewer
				.setInput(EMFEditProperties
						.list(domain,
								FeaturePath
										.fromList(
												ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__INPUT,
												ConfigurationPackage.Literals.INPUT_SPECIFICATION__DATA_PROVIDERS))
						.observe(model));
		comboDataProviderViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						handleChangedDataProvider();
					}
				});

		// Create empty composite to align grid correctly
		Composite emptyComposite = toolkit.createComposite(composite, SWT.NONE);
		emptyComposite.setLayoutData(new GridData(0, 0));

		dataItemConfgigurationComposite = toolkit.createComposite(composite,
				SWT.NONE);
		toolkit.paintBordersFor(dataItemConfgigurationComposite);
		dataItemConfgigurationComposite.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		layoutDataItemConfigurationComposite = new StackLayout();
		dataItemConfgigurationComposite
				.setLayout(layoutDataItemConfigurationComposite);

		lblMetric = toolkit.createLabel(composite, "Metric:", SWT.NONE);

		txtMetric = new Text(composite, SWT.BORDER);
		txtMetric.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		txtMetric.setEditable(false);
		toolkit.adapt(txtMetric, true, true);

		lblInterval = toolkit.createLabel(composite, "Interval:", SWT.NONE);

		Composite intervalEditorComposite = toolkit.createComposite(composite,
				SWT.NONE);
		intervalEditorComposite.setLayoutData(new GridData(SWT.FILL,
				SWT.CENTER, false, false, 1, 1));
		toolkit.paintBordersFor(intervalEditorComposite);
		GridLayout gl_composite_1 = new GridLayout(2, false);
		gl_composite_1.marginTop = 0;
		gl_composite_1.marginRight = 0;
		gl_composite_1.marginLeft = 0;
		gl_composite_1.marginBottom = 0;
		gl_composite_1.marginHeight = 0;
		gl_composite_1.marginWidth = 0;
		intervalEditorComposite.setLayout(gl_composite_1);

		spnIntervalValue = new Spinner(intervalEditorComposite, SWT.BORDER);
		toolkit.adapt(spnIntervalValue);
		toolkit.paintBordersFor(spnIntervalValue);
		spnIntervalValue.setMaximum(Integer.MAX_VALUE); // Important: Default is 100. Much too low.
		spnIntervalValue.setMinimum(0);
		spnIntervalValue.setLayoutData(new GridData(GridData.FILL_BOTH));

		comboIntervalUnitViewer = new ComboViewer(intervalEditorComposite, SWT.READ_ONLY);
		comboIntervalUnit = comboIntervalUnitViewer.getCombo();
		toolkit.adapt(comboIntervalUnit);
		toolkit.paintBordersFor(comboIntervalUnit);
		comboIntervalUnitViewer.setContentProvider(new ArrayContentProvider());
		comboIntervalUnitViewer.setInput(new TimeUnit[] {TimeUnit.MILLISECONDS, TimeUnit.SECONDS, TimeUnit.MINUTES, TimeUnit.HOURS, TimeUnit.DAYS});
		comboIntervalUnitViewer.setSelection(new StructuredSelection(TimeUnit.SECONDS));
		comboIntervalUnitViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				handleChangedIntervalUnit();				
			}			
		});

		lblUnit = toolkit.createLabel(composite, "Unit:", SWT.NONE);

		comboUnitViewer = new ComboViewer(composite, SWT.NONE);
		comboUnit = comboUnitViewer.getCombo();
		comboUnit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		toolkit.paintBordersFor(comboUnit);

		lblResources = toolkit.createLabel(composite, "Resources:", SWT.NONE);

		resourcesTableViewer = CheckboxTableViewer.newCheckList(composite,
				SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		resourcesTable = resourcesTableViewer.getTable();
		resourcesTable.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		toolkit.paintBordersFor(resourcesTable);

		resourcesTableViewer
				.setContentProvider(new ObservableListContentProvider());
		resourcesTableViewer.setLabelProvider(new AdapterFactoryLabelProvider(page.getAdapterFactory()));
		resourcesTableViewer
				.setInput(EMFEditProperties
						.list(domain,
								FeaturePath
										.fromList(
												ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__WORKLOAD_DESCRIPTION,
												ConfigurationPackage.Literals.WORKLOAD_DESCRIPTION__RESOURCES))
						.observe(model));

		lblServices = toolkit.createLabel(composite, "Services:", SWT.NONE);

		servicesTableViewer = CheckboxTableViewer.newCheckList(composite,
				SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		servicesTable = servicesTableViewer.getTable();
		servicesTable.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		toolkit.paintBordersFor(servicesTable);

		servicesTableViewer
				.setContentProvider(new ObservableListContentProvider());
		servicesTableViewer.setLabelProvider(new AdapterFactoryLabelProvider(page.getAdapterFactory()));
		servicesTableViewer
				.setInput(EMFEditProperties
						.list(domain,
								FeaturePath
										.fromList(
												ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__WORKLOAD_DESCRIPTION,
												ConfigurationPackage.Literals.WORKLOAD_DESCRIPTION__SERVICES))
						.observe(model));
	}

	private void createBindings() {
		if (detailBindingContext != null) {
			detailBindingContext.dispose();
		}
		detailBindingContext = new EMFDataBindingContext();
		detailBindingContext
				.bindValue(
						WidgetProperties.text().observe(txtMetric),
						EMFEditProperties
								.value(domain,
										ConfigurationPackage.Literals.TRACE_CONFIGURATION__METRIC)
								.observe(input));
		
		EMFUpdateValueStrategy targetToModelStrategy = new EMFUpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
		targetToModelStrategy.setConverter(new Converter(int.class, long.class) {
			@Override
			public Object convert(Object fromObject) {
				IStructuredSelection sel = (IStructuredSelection)comboIntervalUnitViewer.getSelection();
				if (!sel.isEmpty()) {
					TimeUnit unit = (TimeUnit)sel.getFirstElement();
					return (long)TimeUnit.MILLISECONDS.convert((Integer)fromObject, unit);
				}
				return fromObject;
			}
			
		});
		EMFUpdateValueStrategy modelToTargetStrategy = new EMFUpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
		modelToTargetStrategy.setConverter(new Converter(long.class, int.class) {
			@Override
			public Object convert(Object fromObject) {
				IStructuredSelection sel = (IStructuredSelection)comboIntervalUnitViewer.getSelection();
				if (!sel.isEmpty()) {
					TimeUnit unit = (TimeUnit)sel.getFirstElement();
					return (int)unit.convert((Long)fromObject, TimeUnit.MILLISECONDS);
				}
				return fromObject;
			}
			
		});
		detailBindingContext
				.bindValue(
						WidgetProperties.selection().observe(spnIntervalValue),
						EMFEditProperties
								.value(domain,
										ConfigurationPackage.Literals.TRACE_CONFIGURATION__INTERVAL)
								.observe(input), targetToModelStrategy, modelToTargetStrategy);
		detailBindingContext
				.bindSet(
						ViewerProperties.checkedElements(Resource.class)
								.observe(resourcesTableViewer),
						EMFEditProperties
								.set(domain,
										ConfigurationPackage.Literals.TRACE_CONFIGURATION__RESOURCES)
								.observe(input));
		detailBindingContext
				.bindSet(
						ViewerProperties.checkedElements(Service.class)
								.observe(servicesTableViewer),
						EMFEditProperties
								.set(domain,
										ConfigurationPackage.Literals.TRACE_CONFIGURATION__SERVICES)
								.observe(input));

		detailBindingContext
				.bindValue(
						ViewerProperties.singleSelection().observe(
								comboDataProviderViewer),
						EMFEditProperties
								.value(domain,
										FeaturePath
												.fromList(
														ConfigurationPackage.Literals.TRACE_CONFIGURATION__DATA,
														ConfigurationPackage.Literals.DATA_SOURCE_CONFIGURATION__DATA_PROVIDER))
		
								.observe(input));

	}

	public void selectionChanged(IFormPart part, ISelection selection) {
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		if (structuredSelection.size() == 1) {
			input = (TraceConfiguration) structuredSelection.getFirstElement();
			createBindings();
		} else {
			input = null;
		}
		update();
	}

	private void handleChangedDataProvider() {
		IStructuredSelection selection = (IStructuredSelection) comboDataProviderViewer
				.getSelection();
		if (!selection.isEmpty()) {
			Object obj = selection.getFirstElement();
			if (obj instanceof DataSourceConfiguration) {
				DataProviderConfiguration dataProvider = (DataProviderConfiguration) obj;
				Class<?> itemType = ComponentRegistry.INSTANCE
						.getItemClass(dataProvider.getType());
				if (input.getData() == null) {
					setNewDataConfiguration(dataProvider, itemType);
				} else {
					if (input.getData().getType().equals(itemType)) {
						Command cmd = SetCommand
								.create(domain,
										input.getData(),
										ConfigurationPackage.Literals.DATA_SOURCE_CONFIGURATION__DATA_PROVIDER,
										dataProvider);
						domain.getCommandStack().execute(cmd);
					} else {
						setNewDataConfiguration(dataProvider, itemType);
					}
				}
				showParametersBlock(itemType);
			}
		}
	}

	private void setNewDataConfiguration(DataProviderConfiguration dataProvider,
			Class<?> itemType) {
		DataSourceConfiguration item = ConfigurationFactory.eINSTANCE
				.createDataSourceConfiguration();
		item.setDataProvider(dataProvider);
		item.setType(itemType);

		Command cmd = SetCommand.create(domain, input,
				ConfigurationPackage.Literals.TRACE_CONFIGURATION__DATA, item);

		domain.getCommandStack().execute(cmd);
	}

	private void showParametersBlock(Class<?> itemType) {
		//
		// TODO: Reset old parameters block correctly if item type is switched
		//

		ParametersBlock block = dataItemParameterBlocks.get(itemType);
		if (block == null) {
			Composite composite = managedForm.getToolkit().createComposite(
					dataItemConfgigurationComposite);

			block = new ParametersBlock();
			block.setObjectType(itemType);
			block.createControl(managedForm.getToolkit(), page.getSite()
					.getShell(), composite);

			dataItemParameterBlocks.put(itemType, block);
			dataItemParameterComposites.put(itemType, composite);
		}
		block.setInput(input.getData().getParameters());

		layoutDataItemConfigurationComposite.topControl = dataItemParameterComposites
				.get(itemType);

		this.sctnMeasurementTraceDetails.layout(true);
	}
	
	private void handleChangedIntervalUnit() {
		IStructuredSelection sel = (IStructuredSelection)comboIntervalUnitViewer.getSelection();
		if (!sel.isEmpty()) {
			TimeUnit newUnit = (TimeUnit)sel.getFirstElement();
			long newValue = newUnit.convert(input.getInterval(), TimeUnit.MILLISECONDS);
			spnIntervalValue.setSelection((int)newValue);
		}
	}
}
