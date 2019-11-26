/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2018, by Simon Spinner, Johannes Grohmann
 *  and Contributors.
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

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.DateAndTimeObservableValue;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import tools.descartes.librede.approach.IEstimationApproach;
import tools.descartes.librede.configuration.ConfigurationFactory;
import tools.descartes.librede.configuration.ConfigurationPackage;
import tools.descartes.librede.configuration.DataSourceConfiguration;
import tools.descartes.librede.configuration.EstimationApproachConfiguration;
import tools.descartes.librede.configuration.FileTraceConfiguration;
import tools.descartes.librede.configuration.LibredeConfiguration;
import tools.descartes.librede.configuration.TraceConfiguration;
import tools.descartes.librede.configuration.editor.forms.master.AbstractMasterBlock;
import tools.descartes.librede.configuration.editor.util.TimeUnitSpinnerBuilder;
import tools.descartes.librede.configuration.presentation.ConfigurationEditor;
import tools.descartes.librede.configuration.presentation.LibredeEditorPlugin;
import tools.descartes.librede.datasource.IDataSource;
import tools.descartes.librede.registry.Instantiator;
import tools.descartes.librede.registry.Registry;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.UnitsFactory;
import tools.descartes.librede.units.UnitsPackage;

public class EstimationFormPage extends MasterDetailsFormPage {

	private Spinner spnStepValue;
	private ComboViewer comboStepUnitViewer;
	private ComboViewer comboStartUnitViewer;
	private ComboViewer comboEndUnitViewer;
	private Button btnRecursive;
	private Button btnAutomaticApproachSelection;
	private Spinner spnWindow;

	private EMFDataBindingContext bindingContext = new EMFDataBindingContext();

	public EstimationFormPage(ConfigurationEditor editor, String id, String title, String icon,
			AdapterFactoryEditingDomain editingDomain, LibredeConfiguration model, AbstractMasterBlock masterBlock) {
		super(editor, id, title, icon, editingDomain, model, masterBlock);
		initializeQuantityFields();
	}

	@Override
	protected void createFormContentBeginning(FormToolkit toolkit, Composite parent) {
		super.createFormContentBeginning(toolkit, parent);

		createEstimationSection(toolkit, parent);

		createIntervalSection(toolkit, parent);
	}

	private void initializeQuantityFields() {
		if (getModel().getEstimation().getStepSize() == null) {
			Quantity<Time> stepSize = UnitsFactory.eINSTANCE.createQuantity();
			stepSize.setValue(60);
			stepSize.setUnit(Time.SECONDS);
			Command cmd = SetCommand.create(getEditingDomain(), getModel().getEstimation(),
					ConfigurationPackage.Literals.ESTIMATION_SPECIFICATION__STEP_SIZE, stepSize);
			getEditingDomain().getCommandStack().execute(cmd);
		}

		if (getModel().getEstimation().getStartTimestamp() == null) {
			Quantity<Time> startTimestamp = UnitsFactory.eINSTANCE.createQuantity();
			startTimestamp.setValue(0);
			startTimestamp.setUnit(Time.SECONDS);
			Command cmd = SetCommand.create(getEditingDomain(), getModel().getEstimation(),
					ConfigurationPackage.Literals.ESTIMATION_SPECIFICATION__START_TIMESTAMP, startTimestamp);
			getEditingDomain().getCommandStack().execute(cmd);
		}

		if (getModel().getEstimation().getEndTimestamp() == null) {
			Quantity<Time> endTimestamp = UnitsFactory.eINSTANCE.createQuantity();
			endTimestamp.setValue(0);
			endTimestamp.setUnit(Time.SECONDS);
			Command cmd = SetCommand.create(getEditingDomain(), getModel().getEstimation(),
					ConfigurationPackage.Literals.ESTIMATION_SPECIFICATION__END_TIMESTAMP, endTimestamp);
			getEditingDomain().getCommandStack().execute(cmd);
		}
	}

	private void createIntervalSection(FormToolkit toolkit, Composite body) {
		Section sctnIntervals = toolkit.createSection(body, Section.TWISTIE | Section.TITLE_BAR);
		sctnIntervals.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		sctnIntervals.setText("Interval Settings");
		sctnIntervals.setExpanded(true);

		createToolbar(sctnIntervals);

		Composite client = toolkit.createComposite(sctnIntervals, SWT.NONE);
		sctnIntervals.setClient(client);
		toolkit.paintBordersFor(client);
		client.setLayout(new GridLayout(4, false));

		toolkit.createLabel(client, "Step Size:");

		spnStepValue = TimeUnitSpinnerBuilder.createSpinnerControl(toolkit, client);
		spnStepValue.setLayoutData(new GridData());
		comboStepUnitViewer = TimeUnitSpinnerBuilder.createTimeUnitControl(toolkit, getAdapterFactory(), client);
		comboStepUnitViewer.getCombo().setLayoutData(new GridData());
		toolkit.createLabel(client, "");

		Label lblStartDate = toolkit.createLabel(client, "Start Date:");
		GridData gd_lblStartDate = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd_lblStartDate.verticalSpan = 2;
		lblStartDate.setLayoutData(gd_lblStartDate);
		DateTime startDate = new DateTime(client, SWT.DATE | SWT.DROP_DOWN);
		DateTime startTime = new DateTime(client, SWT.TIME);
		toolkit.createLabel(client, "");

		toolkit.createLabel(client, "As Timestamp:");
		Text txtStartTimestamp = toolkit.createText(client, "");
		GridData gd_txtStartTimestamp = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd_txtStartTimestamp.minimumWidth = 150;
		txtStartTimestamp.setLayoutData(gd_txtStartTimestamp);
		comboStartUnitViewer = TimeUnitSpinnerBuilder.createTimeUnitControl(toolkit, getAdapterFactory(), client);
		comboStartUnitViewer.getCombo().setLayoutData(new GridData());

		Label lblEndDate = toolkit.createLabel(client, "End Date:");
		GridData gd_lblEndDate = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd_lblEndDate.verticalSpan = 2;
		lblEndDate.setLayoutData(gd_lblEndDate);
		DateTime endDate = new DateTime(client, SWT.DATE | SWT.DROP_DOWN);
		DateTime endTime = new DateTime(client, SWT.TIME);
		GridData gd_endTime = new GridData();
		gd_endTime.horizontalSpan = 2;
		endTime.setLayoutData(gd_endTime);

		toolkit.createLabel(client, "As Timestamp:");
		Text txtEndTimestamp = toolkit.createText(client, "");
		GridData gd_txtEndTimestamp = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd_txtEndTimestamp.minimumWidth = 150;
		txtEndTimestamp.setLayoutData(gd_txtEndTimestamp);
		comboEndUnitViewer = TimeUnitSpinnerBuilder.createTimeUnitControl(toolkit, getAdapterFactory(), client);
		comboEndUnitViewer.getCombo().setLayoutData(new GridData());

		btnRecursive = toolkit.createButton(client, "Recursive Execution", SWT.CHECK);
		GridData gd_btnRecursive = new GridData();
		gd_btnRecursive.horizontalSpan = 4;
		btnRecursive.setLayoutData(gd_btnRecursive);
		
		btnAutomaticApproachSelection = toolkit.createButton(client, "Automatic Approach Selection", SWT.CHECK);
		GridData gd_btnAutomaticApproachSelection = new GridData();
		gd_btnAutomaticApproachSelection.horizontalSpan = 4;
		btnAutomaticApproachSelection.setLayoutData(gd_btnAutomaticApproachSelection);

		toolkit.createLabel(client, "Window Size:");
		spnWindow = new Spinner(client, SWT.BORDER);
		spnWindow.setMinimum(1);
		spnWindow.setMaximum(Integer.MAX_VALUE);
		toolkit.paintBordersFor(spnWindow);

		bindingContext.bindValue(
				WidgetProperties.selection().observe(spnStepValue),
				EMFEditProperties.value(
						getEditingDomain(),
						FeaturePath.fromList(ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__ESTIMATION,
								ConfigurationPackage.Literals.ESTIMATION_SPECIFICATION__STEP_SIZE,
								UnitsPackage.Literals.QUANTITY__VALUE)).observe(getModel()), TimeUnitSpinnerBuilder
						.createTargetToModelConverter(), TimeUnitSpinnerBuilder.createModelToTargetConverter());

		bindingContext.bindValue(
				ViewerProperties.singleSelection().observe(comboStepUnitViewer),
				EMFEditProperties.value(
						getEditingDomain(),
						FeaturePath.fromList(ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__ESTIMATION,
								ConfigurationPackage.Literals.ESTIMATION_SPECIFICATION__STEP_SIZE,
								UnitsPackage.Literals.QUANTITY__UNIT)).observe(getModel()));

		bindingContext.bindValue(
				new DateAndTimeObservableValue(WidgetProperties.selection().observe(startDate), WidgetProperties
						.selection().observe(startTime)),
				EMFEditProperties.value(
						getEditingDomain(),
						FeaturePath.fromList(ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__ESTIMATION,
								ConfigurationPackage.Literals.ESTIMATION_SPECIFICATION__START_TIMESTAMP,
								UnitsPackage.Literals.QUANTITY__VALUE)).observe(getModel()),
				createDateConverterTargetToModel(getModel().getEstimation().getStartTimestamp()),
				createDateConverterModelToTarget(getModel().getEstimation().getStartTimestamp()));
		bindingContext.bindValue(
				new DateAndTimeObservableValue(WidgetProperties.selection().observe(endDate), WidgetProperties
						.selection().observe(endTime)),
				EMFEditProperties.value(
						getEditingDomain(),
						FeaturePath.fromList(ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__ESTIMATION,
								ConfigurationPackage.Literals.ESTIMATION_SPECIFICATION__END_TIMESTAMP,
								UnitsPackage.Literals.QUANTITY__VALUE)).observe(getModel()),
				createDateConverterTargetToModel(getModel().getEstimation().getEndTimestamp()),
				createDateConverterModelToTarget(getModel().getEstimation().getEndTimestamp()));
		bindingContext.bindValue(
				WidgetProperties.text(SWT.Modify).observe(txtStartTimestamp),
				EMFEditProperties.value(
						getEditingDomain(),
						FeaturePath.fromList(ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__ESTIMATION,
								ConfigurationPackage.Literals.ESTIMATION_SPECIFICATION__START_TIMESTAMP,
								UnitsPackage.Literals.QUANTITY__VALUE)).observe(getModel()));
		bindingContext.bindValue(
				ViewerProperties.singleSelection().observe(comboStartUnitViewer),
				EMFEditProperties.value(
						getEditingDomain(),
						FeaturePath.fromList(ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__ESTIMATION,
								ConfigurationPackage.Literals.ESTIMATION_SPECIFICATION__START_TIMESTAMP,
								UnitsPackage.Literals.QUANTITY__UNIT)).observe(getModel()));
		bindingContext.bindValue(
				WidgetProperties.text(SWT.Modify).observe(txtEndTimestamp),
				EMFEditProperties.value(
						getEditingDomain(),
						FeaturePath.fromList(ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__ESTIMATION,
								ConfigurationPackage.Literals.ESTIMATION_SPECIFICATION__END_TIMESTAMP,
								UnitsPackage.Literals.QUANTITY__VALUE)).observe(getModel()));
		bindingContext.bindValue(
				ViewerProperties.singleSelection().observe(comboEndUnitViewer),
				EMFEditProperties.value(
						getEditingDomain(),
						FeaturePath.fromList(ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__ESTIMATION,
								ConfigurationPackage.Literals.ESTIMATION_SPECIFICATION__END_TIMESTAMP,
								UnitsPackage.Literals.QUANTITY__UNIT)).observe(getModel()));
		bindingContext
				.bindValue(
						WidgetProperties.selection().observe(btnRecursive),
						EMFEditProperties.value(
								getEditingDomain(),
								FeaturePath.fromList(ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__ESTIMATION,
										ConfigurationPackage.Literals.ESTIMATION_SPECIFICATION__RECURSIVE)).observe(
								getModel()));
		bindingContext
				.bindValue(
						WidgetProperties.selection().observe(btnAutomaticApproachSelection),
						EMFEditProperties.value(
								getEditingDomain(),
								FeaturePath.fromList(ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__ESTIMATION,
										ConfigurationPackage.Literals.ESTIMATION_SPECIFICATION__AUTOMATIC_APPROACH_SELECTION)).observe(
								getModel()));
		bindingContext.bindValue(
				WidgetProperties.selection().observe(spnWindow),
				EMFEditProperties.value(
						getEditingDomain(),
						FeaturePath.fromList(ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__ESTIMATION,
								ConfigurationPackage.Literals.ESTIMATION_SPECIFICATION__WINDOW)).observe(getModel()));
	}

	private void createToolbar(Section section) {
		ToolBar toolbar = new ToolBar(section, SWT.NONE);
		ToolItem readFromTracesItem = new ToolItem(toolbar, SWT.NONE);
		readFromTracesItem.setImage(ExtendedImageRegistry.INSTANCE.getImage(LibredeEditorPlugin.getPlugin().getImage(
				"full/obj16/refresh_remote")));
		readFromTracesItem.setToolTipText("Update from Traces");
		readFromTracesItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleReadFromTrace();
			}
		});
		section.setTextClient(toolbar);
	}

	private void createEstimationSection(FormToolkit toolkit, Composite body) {
		Section sctnEstimation = toolkit.createSection(body, Section.TWISTIE | Section.TITLE_BAR);
		sctnEstimation.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
		sctnEstimation.setText("Activated Estimation Approaches");
		sctnEstimation.setExpanded(true);

		Composite client = toolkit.createComposite(sctnEstimation, SWT.NONE);
		sctnEstimation.setClient(client);
		toolkit.paintBordersFor(client);
		client.setLayout(new GridLayout(2, false));

		CheckboxTableViewer tableApproachesViewer = CheckboxTableViewer.newCheckList(client, SWT.BORDER
				| SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		Table tableApproaches = tableApproachesViewer.getTable();
		tableApproaches.setLayoutData(new GridData(GridData.FILL_BOTH));

		tableApproachesViewer.setLabelProvider(new AdapterFactoryLabelProvider(getAdapterFactory()));
		tableApproachesViewer.setContentProvider(new ObservableListContentProvider());

		// We need to add the existing validators in the configuration first, so
		// that the test for
		// equality in the checked table binding works correctly. EMF always
		// does an equality check on
		// the object instance.
		IObservableList approaches = new WritableList();
		Set<String> existingApproaches = new HashSet<String>();
		for (EstimationApproachConfiguration v : getModel().getEstimation().getApproaches()) {
			approaches.add(v);
			existingApproaches.add(v.getType());
		}
		for (String instance : Registry.INSTANCE.getInstances(IEstimationApproach.class)) {
			if (!existingApproaches.contains(instance)) {
				EstimationApproachConfiguration a = ConfigurationFactory.eINSTANCE
						.createEstimationApproachConfiguration();
				a.setType(instance);
				approaches.add(a);
			}
		}
		tableApproachesViewer.setInput(approaches);

		// Binding: All checked approaches are added to the model instance
		bindingContext.bindSet(
				ViewerProperties.checkedElements(EstimationApproachConfiguration.class).observe(tableApproachesViewer),
				EMFEditProperties.set(
						getEditingDomain(),
						FeaturePath.fromList(ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__ESTIMATION,
								ConfigurationPackage.Literals.ESTIMATION_SPECIFICATION__APPROACHES))
						.observe(getModel()));

	}

	private EMFUpdateValueStrategy createDateConverterTargetToModel(final Quantity<Time> quantity) {
		EMFUpdateValueStrategy strategy = new EMFUpdateValueStrategy(EMFUpdateValueStrategy.POLICY_UPDATE);
		strategy.setConverter(new Converter(Date.class, Double.class) {
			@Override
			public Object convert(Object fromObject) {
				return quantity.getUnit().convertFrom(((Date) fromObject).getTime(), Time.MILLISECONDS);
			}
		});
		return strategy;
	}

	private EMFUpdateValueStrategy createDateConverterModelToTarget(final Quantity<Time> quantity) {
		EMFUpdateValueStrategy strategy = new EMFUpdateValueStrategy(EMFUpdateValueStrategy.POLICY_UPDATE);
		strategy.setConverter(new Converter(Double.class, Date.class) {
			@Override
			public Object convert(Object fromObject) {
				return new Date((long) quantity.getUnit().convertTo((double) fromObject, Time.MILLISECONDS));
			}
		});
		return strategy;
	}

	private void handleReadFromTrace() {
		Map<String, IDataSource> dataSources = new HashMap<String, IDataSource>();

		double maxStart = Double.MIN_VALUE;
		double minEnd = Double.MAX_VALUE;

		for (TraceConfiguration trace : getModel().getInput().getObservations()) {
			if (trace instanceof FileTraceConfiguration) {
				FileTraceConfiguration fileTrace = (FileTraceConfiguration) trace;
				File inputFile = new File(fileTrace.getFile());
				if (inputFile.exists()) {
					DataSourceConfiguration dataSourceConf = fileTrace.getDataSource();
					if (dataSourceConf != null) {
						IDataSource ds = dataSources.get(dataSourceConf.getType());
						if (ds == null) {
							try {
								Class<?> cl = Registry.INSTANCE.getInstanceClass(dataSourceConf.getType());
								ds = (IDataSource) Instantiator.newInstance(cl, dataSourceConf.getParameters());
								dataSources.put(dataSourceConf.getType(), ds);
							} catch (Exception e) {
								ErrorDialog.openError(getEditor().getEditorSite().getShell(), null, null, new Status(
										IStatus.ERROR, LibredeEditorPlugin.ID,
										"Error instantiating data source instance.", e));
								return;
							}
						}
						// it is enough to load the first mapping as all will
						// have the same timestamps
						if (fileTrace.getMappings().size() >= 1) {
							int col = fileTrace.getMappings().get(0).getTraceColumn();
							FileInputStream in = null;
							try {
								in = new FileInputStream(inputFile);
//								TimeSeries series = ds.load(in, col);
//								maxStart = Math.max(series.getStartTime(), maxStart);
//								minEnd = Math.min(series.getEndTime(), minEnd);
							} catch (Exception e) {
								// Fail graciously
							}
						}
					}
				}
			}
		}

		if (maxStart >= minEnd) {
			ErrorDialog.openError(getEditor().getEditorSite().getShell(), null, null, new Status(IStatus.WARNING,
					LibredeEditorPlugin.ID, "The time spans of the traces to no overlap."));
			return;
		}
		if (maxStart != Double.MIN_VALUE) {
			Command cmd = SetCommand.create(getEditingDomain(), getModel().getEstimation().getStartTimestamp(),
					UnitsPackage.Literals.QUANTITY__VALUE, maxStart);
			getEditingDomain().getCommandStack().execute(cmd);
		}
		if (minEnd != Double.MAX_VALUE) {
			Command cmd = SetCommand.create(getEditingDomain(), getModel().getEstimation().getEndTimestamp(),
					UnitsPackage.Literals.QUANTITY__VALUE, minEnd);
			getEditingDomain().getCommandStack().execute(cmd);
		}

	}

}
