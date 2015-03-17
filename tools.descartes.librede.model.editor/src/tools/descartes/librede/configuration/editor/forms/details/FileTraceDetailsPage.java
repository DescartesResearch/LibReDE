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
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
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
import tools.descartes.librede.configuration.FileTraceConfiguration;
import tools.descartes.librede.configuration.LibredeConfiguration;
import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.TraceToEntityMapping;
import tools.descartes.librede.configuration.editor.forms.AbstractEstimationConfigurationFormPage;
import tools.descartes.librede.configuration.editor.util.PrettyPrinter;
import tools.descartes.librede.configuration.editor.util.TimeUnitSpinnerBuilder;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.registry.Registry;
import tools.descartes.librede.repository.IMetric;

public class FileTraceDetailsPage extends AbstractDetailsPage {

	private LibredeConfiguration model;
	private FileTraceConfiguration input;
	private Section sctnMeasurementTraceDetails;
	private Label lblMetricValue;
	private Spinner spnIntervalValue;
	private Combo comboIntervalUnit;
	private ComboViewer comboIntervalUnitViewer;
	private Combo comboUnit;
	private ComboViewer comboUnitViewer;
	private Table mappingTable;
	private TableViewer mappingTableViewer;
	private Combo comboDataSource;
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

		comboDataSourceViewer = new ComboViewer(composite, SWT.READ_ONLY);
		comboDataSource = comboDataSourceViewer.getCombo();
		comboDataSource.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		toolkit.paintBordersFor(comboDataSource);

		comboDataSourceViewer
				.setContentProvider(new ObservableListContentProvider());
		comboDataSourceViewer
				.setLabelProvider(new AdapterFactoryLabelProvider(page
						.getAdapterFactory()));
		comboDataSourceViewer
				.setInput(EMFEditProperties
						.list(domain,
								FeaturePath
										.fromList(
												ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__INPUT,
												ConfigurationPackage.Literals.INPUT_SPECIFICATION__DATA_SOURCES))
						.observe(model));
		Label lblMetric = toolkit.createLabel(composite, "Metric:", SWT.NONE);

		lblMetricValue = toolkit.createLabel(composite, "", SWT.NONE);
		lblMetricValue.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label lblUnit = toolkit.createLabel(composite, "Unit:", SWT.READ_ONLY);
		comboUnitViewer = new ComboViewer(composite, SWT.NONE);
		comboUnit = comboUnitViewer.getCombo();
		comboUnit.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		toolkit.paintBordersFor(comboUnit);
		
		comboUnitViewer.setContentProvider(new ArrayContentProvider());
		comboUnitViewer.setLabelProvider(new LabelProvider());

		Label lblInterval = toolkit.createLabel(composite, "Interval:",
				SWT.NONE);

		Composite intervalEditorComposite = TimeUnitSpinnerBuilder.createComposite(toolkit, composite);
		spnIntervalValue = TimeUnitSpinnerBuilder.createSpinnerControl(toolkit, intervalEditorComposite);
		comboIntervalUnitViewer = TimeUnitSpinnerBuilder.createTimeUnitControl(toolkit, intervalEditorComposite, spnIntervalValue);

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

	private void createBindings() {
		detailBindingContext = new EMFDataBindingContext();
		detailBindingContext
				.bindValue(
						WidgetProperties.text(SWT.Modify).observe(txtFilePath),
						EMFEditProperties
								.value(domain,
										ConfigurationPackage.Literals.FILE_TRACE_CONFIGURATION__FILE)
								.observe(input));
		
		EMFUpdateValueStrategy metricConverter = new EMFUpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
		metricConverter.setConverter(new Converter(String.class, String.class) {

			@Override
			public Object convert(Object fromObject) {
				Metric metric = (Metric)fromObject;
				if (metric != null) {
					return PrettyPrinter.toCamelCase(metric.getName());
				}
				return "Unkown";
			}
			
		});
		detailBindingContext
				.bindValue(
						WidgetProperties.text().observe(lblMetricValue),
						EMFEditProperties
								.value(domain,
										ConfigurationPackage.Literals.TRACE_CONFIGURATION__METRIC)
								.observe(input), null, metricConverter);

		detailBindingContext
				.bindValue(
						WidgetProperties.selection().observe(spnIntervalValue),
						EMFEditProperties
								.value(domain,
										ConfigurationPackage.Literals.TRACE_CONFIGURATION__INTERVAL)
								.observe(input), TimeUnitSpinnerBuilder.createTargetToModelStrategy(comboIntervalUnitViewer),
								TimeUnitSpinnerBuilder.createModelToTargetStrategy(comboIntervalUnitViewer));
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
			createBindings();
		} else {
			input = null;
			mappingTableViewer.setInput(null);
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
