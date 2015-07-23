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
package tools.descartes.librede.configuration.editor.forms.details;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import tools.descartes.librede.configuration.ConfigurationFactory;
import tools.descartes.librede.configuration.ConfigurationPackage;
import tools.descartes.librede.configuration.DataSourceConfiguration;
import tools.descartes.librede.configuration.FileTraceConfiguration;
import tools.descartes.librede.configuration.LibredeConfiguration;
import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.TraceToEntityMapping;
import tools.descartes.librede.configuration.editor.forms.AbstractEstimationConfigurationFormPage;
import tools.descartes.librede.configuration.editor.util.TimeUnitSpinnerBuilder;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.metrics.MetricsPackage;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.model.util.PrettyPrinter;
import tools.descartes.librede.registry.Registry;
import tools.descartes.librede.repository.IMetricAdapter;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.RequestRate;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;
import tools.descartes.librede.units.UnitsFactory;
import tools.descartes.librede.units.UnitsPackage;

public class FileTraceDetailsPage extends AbstractDetailsPage {

	private LibredeConfiguration model;
	private FileTraceConfiguration input;
	private Section sctnMeasurementTraceDetails;
	private Spinner spnIntervalValue;
	private ComboViewer comboMetricViewer;
	private ComboViewer comboIntervalUnitViewer;
	private ComboViewer comboUnitViewer;
	private ComboViewer comboAggregationViewer;
	private Table mappingTable;
	private TableViewer mappingTableViewer;
	private ComboViewer comboDataSourceViewer;
	private Text txtFilePath;
	private FileDialog fileDialog;

	private EMFDataBindingContext detailBindingContext;

	/**
	 * Create the details page.
	 */
	public FileTraceDetailsPage(AbstractEstimationConfigurationFormPage page,
			AdapterFactoryEditingDomain domain, LibredeConfiguration model) {
		super(page, domain);
		this.model = model;
	}

	/**
	 * Create contents of the details page.
	 * 
	 * @param parent
	 */
	public void createContents(Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();
		GridLayout parentLayout = new GridLayout();
		parentLayout.marginHeight = 0;
		parentLayout.marginWidth = 0;		
		parent.setLayout(parentLayout);
		//
		sctnMeasurementTraceDetails = toolkit.createSection(parent,
				ExpandableComposite.EXPANDED | ExpandableComposite.TITLE_BAR);
		sctnMeasurementTraceDetails.setText("Measurement Trace Details");
		sctnMeasurementTraceDetails.setLayoutData(new GridData(GridData.FILL_BOTH));
		//

		Composite composite = toolkit.createComposite(sctnMeasurementTraceDetails, SWT.NONE);
		toolkit.paintBordersFor(composite);
		sctnMeasurementTraceDetails.setClient(composite);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblMetric = toolkit.createLabel(composite, "Metric:", SWT.NONE);
		IObservableList metrics = EMFEditProperties.list(domain, MetricsPackage.Literals.METRICS_REPOSITORY__METRICS).observe(Registry.INSTANCE.getMetricsRepository());
		comboMetricViewer = createComboBoxViewer(composite, toolkit, metrics);
		
		// make this a post selection listener, as changes should only propagated to the 
		// unit combo box if they are persistent
		comboMetricViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				if (!event.getSelection().isEmpty()) {
					Metric<?> newMetric = (Metric<?>)((IStructuredSelection)event.getSelection()).getFirstElement();
					if ((input.getUnit() == null) || (newMetric.getDimension() != input.getUnit().getDimension())) {
						input.setUnit(newMetric.getDimension().getBaseUnit());
					}
				}
			}
		});

		Label lblFile = toolkit.createLabel(composite, "File:");

		Composite fileEditorComposite = toolkit.createComposite(composite,
				SWT.NONE);
		fileEditorComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		toolkit.paintBordersFor(fileEditorComposite);
		GridLayout gl_fileEditorComposite = new GridLayout(2, false);
		gl_fileEditorComposite.marginHeight = 0;
		gl_fileEditorComposite.marginWidth = 0;
		fileEditorComposite.setLayout(gl_fileEditorComposite);

		txtFilePath = toolkit.createText(fileEditorComposite, "");
		txtFilePath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Button btnBrowse = toolkit.createButton(fileEditorComposite, "Browse...",
				SWT.PUSH);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (fileDialog == null) {
					fileDialog = new FileDialog(page.getSite().getShell(), SWT.OPEN);
				}
				if (new File(txtFilePath.getText()).exists()) {
					fileDialog.setFileName(input.getFile());
				}
				String res = fileDialog.open();
				if (res != null) {
					txtFilePath.setText(res);
				}
			}
		});

		Label lblDataSource = toolkit.createLabel(composite,
				"Data Source:", SWT.NONE);
		comboDataSourceViewer = createComboBoxViewer(composite, 
				toolkit, 
				EMFEditProperties.list(domain,
						FeaturePath
								.fromList(
										ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__INPUT,
										ConfigurationPackage.Literals.INPUT_SPECIFICATION__DATA_SOURCES))
				.observe(model));
		
		Label lblUnit = toolkit.createLabel(composite, "Unit:", SWT.READ_ONLY);
		comboUnitViewer = createComboBoxViewer(composite, toolkit, new WritableList());
		
		Label lblAggregation = toolkit.createLabel(composite, "Aggregation:", SWT.READ_ONLY);
		comboAggregationViewer = createComboBoxViewer(composite, toolkit, new WritableList(Aggregation.VALUES, Aggregation.class));
		
		Label lblInterval = toolkit.createLabel(composite, "Interval:",
				SWT.NONE);

		Composite intervalEditorComposite = TimeUnitSpinnerBuilder.createComposite(toolkit, composite);
		spnIntervalValue = TimeUnitSpinnerBuilder.createSpinnerControl(toolkit, intervalEditorComposite);
		comboIntervalUnitViewer = TimeUnitSpinnerBuilder.createTimeUnitControl(toolkit, page.getAdapterFactory(), intervalEditorComposite);

		Label lblMapping = toolkit.createLabel(composite, "Mapping:",
				SWT.NONE);
		lblMapping.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		
		Composite mappingTableButtonComposite = toolkit.createComposite(composite);
		GridLayout mappingTableButtonCompositeLayout = new GridLayout(2, false);
		mappingTableButtonCompositeLayout.marginWidth = 0;
		mappingTableButtonCompositeLayout.marginHeight = 0;
		mappingTableButtonComposite.setLayout(mappingTableButtonCompositeLayout);
		mappingTableButtonComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite mappingTableComposite = new Composite(mappingTableButtonComposite, SWT.NONE);
		GridData gd_mappingTableComposite = new GridData(GridData.FILL_BOTH);
		gd_mappingTableComposite.widthHint = 50;
		gd_mappingTableComposite.heightHint = 50;
		gd_mappingTableComposite.verticalSpan = 2;
		mappingTableComposite.setLayoutData(gd_mappingTableComposite);
		
		TableColumnLayout mappingTableLayout = new TableColumnLayout();
		mappingTableComposite.setLayout(mappingTableLayout);
		
		mappingTableViewer = new TableViewer(mappingTableComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		mappingTable = mappingTableViewer.getTable();
		mappingTable.setHeaderVisible(true);
		mappingTable.setLinesVisible(true);
		
		TableViewerColumn entityViewerColumn = new TableViewerColumn(mappingTableViewer, SWT.NONE);
		TableColumn entityColumn = entityViewerColumn.getColumn();
		mappingTableLayout.setColumnData(entityColumn, new ColumnWeightData(10, 50, true));
		entityColumn.setText("Entity");
		entityViewerColumn.setEditingSupport(AbstractEstimationConfigurationFormPage.EObjectEditingSupport.create(mappingTableViewer, domain, ConfigurationPackage.Literals.TRACE_TO_ENTITY_MAPPING__ENTITY));

		
		TableViewerColumn indexViewerColumn = new TableViewerColumn(mappingTableViewer, SWT.NONE);
		TableColumn indexColumn = indexViewerColumn.getColumn();
		mappingTableLayout.setColumnData(indexColumn, new ColumnWeightData(10, 50, true));
		indexColumn.setText("Column Index");
		indexViewerColumn.setEditingSupport(AbstractEstimationConfigurationFormPage.EObjectEditingSupport.create(mappingTableViewer, domain, ConfigurationPackage.Literals.TRACE_TO_ENTITY_MAPPING__TRACE_COLUMN));

		mappingTableViewer
				.setContentProvider(new AdapterFactoryContentProvider(page.getAdapterFactory()));
		mappingTableViewer.setLabelProvider(new AdapterFactoryLabelProvider(
				page.getAdapterFactory()));
		
		mappingTableViewer.getControl().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				page.getConfigurationEditor().setCurrentViewer(mappingTableViewer);
			}
		});
		page.getConfigurationEditor().createContextMenuFor(mappingTableViewer);
		
		Button btnAddMapping = toolkit.createButton(mappingTableButtonComposite, "Add", SWT.PUSH);
		GridData gd_btnAddMapping = new GridData();
		gd_btnAddMapping.verticalAlignment = SWT.TOP;
		gd_btnAddMapping.widthHint = 90;
		btnAddMapping.setLayoutData(gd_btnAddMapping);
		btnAddMapping.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleAddMapping();
			}
		});
		
		Button btnRemoveMapping = toolkit.createButton(mappingTableButtonComposite, "Remove", SWT.PUSH);
		GridData gd_btnRemoveMapping = new GridData();
		gd_btnRemoveMapping.verticalAlignment = SWT.TOP;
		gd_btnRemoveMapping.widthHint = 90;
		btnRemoveMapping.setLayoutData(gd_btnRemoveMapping);
		btnRemoveMapping.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleRemoveMapping();
			}
		});
	}
	
	private ComboViewer createComboBoxViewer(Composite composite, FormToolkit toolkit, IObservableList content) {
		ComboViewer viewer = new ComboViewer(composite, SWT.READ_ONLY);
		Combo combo = viewer.getCombo();
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		toolkit.paintBordersFor(combo);

		viewer.setContentProvider(new ObservableListContentProvider());
		viewer.setLabelProvider(new AdapterFactoryLabelProvider(page
						.getAdapterFactory()));
		viewer.setInput(content);
		return viewer;
	}

	private void createBindings() {
		detailBindingContext = new EMFDataBindingContext();
		detailBindingContext
				.bindValue(
						WidgetProperties.text(SWT.Modify).observe(txtFilePath),
						EMFEditProperties
								.value(domain,
										ConfigurationPackage.Literals.FILE_TRACE_CONFIGURATION__FILE)
								.observe(input));
		detailBindingContext
				.bindValue(
						ViewerProperties.singleSelection().observe(comboMetricViewer),
						EMFEditProperties
								.value(domain,
										ConfigurationPackage.Literals.TRACE_CONFIGURATION__METRIC)
								.observe(input));
		detailBindingContext
				.bindValue(
						ViewerProperties.singleSelection().observe(comboUnitViewer),
						EMFEditProperties
								.value(domain,
										ConfigurationPackage.Literals.TRACE_CONFIGURATION__UNIT)
								.observe(input));		
		detailBindingContext
			.bindValue(
					ViewerProperties.singleSelection().observe(comboAggregationViewer),
					EMFEditProperties
							.value(domain,
									ConfigurationPackage.Literals.TRACE_CONFIGURATION__AGGREGATION)
							.observe(input));
		detailBindingContext
				.bindValue(
						WidgetProperties.selection().observe(spnIntervalValue),
						EMFEditProperties
								.value(domain,
										FeaturePath.fromList(
												ConfigurationPackage.Literals.TRACE_CONFIGURATION__INTERVAL,
												UnitsPackage.Literals.QUANTITY__VALUE))
								.observe(input), TimeUnitSpinnerBuilder.createTargetToModelConverter(),
								TimeUnitSpinnerBuilder.createModelToTargetConverter());
		detailBindingContext
				.bindValue(
						ViewerProperties.singleSelection().observe(comboIntervalUnitViewer),
						EMFEditProperties
								.value(domain,
										FeaturePath.fromList(
												ConfigurationPackage.Literals.TRACE_CONFIGURATION__INTERVAL,
												UnitsPackage.Literals.QUANTITY__UNIT))
								.observe(input));
		detailBindingContext
				.bindValue(
						ViewerProperties.singleSelection().observe(
								comboDataSourceViewer),
						EMFEditProperties
								.value(domain,
										FeaturePath
												.fromList(ConfigurationPackage.Literals.TRACE_CONFIGURATION__DATA_SOURCE))
								.observe(input));

	}

	public void selectionChanged(IFormPart part, ISelection selection) {
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		if (detailBindingContext != null) {
			detailBindingContext.dispose();
			detailBindingContext = null;
		}
		if (structuredSelection.size() == 1) {
			input = (FileTraceConfiguration) structuredSelection.getFirstElement();
			mappingTableViewer.setInput(input);
			IObservableList units = EMFEditProperties.list(domain, FeaturePath.fromList(
					ConfigurationPackage.Literals.TRACE_CONFIGURATION__METRIC,
					MetricsPackage.Literals.METRIC__DIMENSION,
					UnitsPackage.Literals.DIMENSION__UNITS
					)).observe(input);
			comboUnitViewer.setInput(units);
			createBindings();
		} else {
			input = null;
			mappingTableViewer.setInput(null);
			comboUnitViewer.setInput(null);
		}
		update();
	}

	private void handleAddMapping() {
		Set<ModelEntity> usedEntities = new HashSet<>();
		int maxIndex = 0;
		for (TraceToEntityMapping m : input.getMappings()) {
			usedEntities.add(m.getEntity());
			maxIndex = Math.max(maxIndex, m.getTraceColumn());
		}

		TraceToEntityMapping mapping = ConfigurationFactory.eINSTANCE.createTraceToEntityMapping();
		mapping.setTraceColumn(maxIndex + 1);
		
		// Search for a resource or service that has not been set yet. 
		// If all entities have been used, entity is set to null.
		// The label provider will then indicate to the user that he needs to select one manually.
		for (Resource r : model.getWorkloadDescription().getResources()) {
			if (!usedEntities.contains(r)) {
				mapping.setEntity(r);
				break;
			}
		}
		if (mapping.getEntity() == null) {
			for (Service s : model.getWorkloadDescription().getServices()) {
				if (!usedEntities.contains(s)) {
					mapping.setEntity(s);
					break;
				}
			}
		}
		Command cmd = AddCommand.create(domain, input, ConfigurationPackage.Literals.TRACE_CONFIGURATION__MAPPINGS, mapping);
		domain.getCommandStack().execute(cmd);
	}
	
	private void handleRemoveMapping() {
		IStructuredSelection sel = (IStructuredSelection)mappingTableViewer.getSelection();
		if (!sel.isEmpty()) {		
			Command cmd = RemoveCommand.create(domain, sel.getFirstElement());
			domain.getCommandStack().execute(cmd);
		}
	}
}
