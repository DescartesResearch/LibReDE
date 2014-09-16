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

import java.util.Date;

import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.editor.forms.master.AbstractMasterBlock;
import net.descartesresearch.librede.configuration.editor.util.TimeUnitSpinnerBuilder;
import net.descartesresearch.librede.configuration.presentation.ConfigurationEditor;

import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.observable.value.DateAndTimeObservableValue;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public class EstimationFormPage extends MasterDetailsFormPage {
	
	private Spinner spnStepValue;
	private ComboViewer comboStepUnitViewer;
	private Button btnRecursive;
	private Spinner spnWindow;
	
	private EMFDataBindingContext bindingContext = new EMFDataBindingContext();

	public EstimationFormPage(ConfigurationEditor editor, String id,
			String title, String icon,
			AdapterFactoryEditingDomain editingDomain,
			LibredeConfiguration model, AbstractMasterBlock masterBlock) {
		super(editor, id, title, icon, editingDomain, model, masterBlock);
	}
	
	@Override
	protected void createFormContentBeginning(FormToolkit toolkit,
			Composite parent) {
		super.createFormContentBeginning(toolkit, parent);
		
		createIntervalSection(toolkit, parent);
		
		createEstimationSection(toolkit, parent);
	}
	
	private void createIntervalSection(FormToolkit toolkit, Composite body) {
		Section sctnIntervals = toolkit.createSection(body, Section.TWISTIE | Section.TITLE_BAR);
		sctnIntervals.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		sctnIntervals.setText("Estimation Interval Settings");
		sctnIntervals.setExpanded(true);
		
		Composite client = toolkit.createComposite(sctnIntervals, SWT.NONE);
		sctnIntervals.setClient(client);
		toolkit.paintBordersFor(client);
		client.setLayout(new GridLayout(3, false));
		
		toolkit.createLabel(client, "Step Size:");
		
		spnStepValue = TimeUnitSpinnerBuilder.createSpinnerControl(toolkit, client);
		spnStepValue.setLayoutData(new GridData());
		comboStepUnitViewer = TimeUnitSpinnerBuilder.createTimeUnitControl(toolkit, client, spnStepValue);
		comboStepUnitViewer.getCombo().setLayoutData(new GridData());
		
		Label lblStartDate = toolkit.createLabel(client, "Start Date:");
		GridData gd_lblStartDate = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd_lblStartDate.verticalSpan = 2;
		lblStartDate.setLayoutData(gd_lblStartDate);
		DateTime startDate = new DateTime(client, SWT.DATE | SWT.DROP_DOWN);
		DateTime startTime = new DateTime(client, SWT.TIME);
		
		toolkit.createLabel(client, "In Unix Time:");
		Text txtStartTimestamp = toolkit.createText(client, "");
		
		Label lblEndDate = toolkit.createLabel(client, "End Date:");
		GridData gd_lblEndDate = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd_lblEndDate.verticalSpan = 2;
		lblEndDate.setLayoutData(gd_lblEndDate);
		DateTime endDate = new DateTime(client, SWT.DATE | SWT.DROP_DOWN);
		DateTime endTime = new DateTime(client, SWT.TIME);
		
		toolkit.createLabel(client, "In Unix Time:");
		Text txtEndTimestamp = toolkit.createText(client, "");
		
		bindingContext.bindValue(WidgetProperties.selection().observe(spnStepValue), 
				EMFEditProperties.value(getEditingDomain(), 
						FeaturePath.fromList(
								ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__ESTIMATION,
								ConfigurationPackage.Literals.ESTIMATION_SPECIFICATION__STEP_SIZE
								)).observe(getModel()),
				TimeUnitSpinnerBuilder.createTargetToModelStrategy(comboStepUnitViewer),
				TimeUnitSpinnerBuilder.createModelToTargetStrategy(comboStepUnitViewer));
		
		bindingContext.bindValue(new DateAndTimeObservableValue(
				WidgetProperties.selection().observe(startDate),
				WidgetProperties.selection().observe(startTime)), 
				EMFEditProperties.value(getEditingDomain(), 
						FeaturePath.fromList(
								ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__ESTIMATION,
								ConfigurationPackage.Literals.ESTIMATION_SPECIFICATION__START_TIMESTAMP
						)).observe(getModel()),
				createDateConverterTargetToModel(),
				createDateConverterModelToTarget());
		bindingContext.bindValue(new DateAndTimeObservableValue(
				WidgetProperties.selection().observe(endDate),
				WidgetProperties.selection().observe(endTime)), 
				EMFEditProperties.value(getEditingDomain(), 
						FeaturePath.fromList(
								ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__ESTIMATION,
								ConfigurationPackage.Literals.ESTIMATION_SPECIFICATION__END_TIMESTAMP
						)).observe(getModel()),
				createDateConverterTargetToModel(),
				createDateConverterModelToTarget());
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtStartTimestamp), 
				EMFEditProperties.value(getEditingDomain(), 
						FeaturePath.fromList(
								ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__ESTIMATION,
								ConfigurationPackage.Literals.ESTIMATION_SPECIFICATION__START_TIMESTAMP
						)).observe(getModel()));
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtEndTimestamp), 
				EMFEditProperties.value(getEditingDomain(), 
						FeaturePath.fromList(
								ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__ESTIMATION,
								ConfigurationPackage.Literals.ESTIMATION_SPECIFICATION__END_TIMESTAMP
						)).observe(getModel()));
	}
	
	private void createEstimationSection(FormToolkit toolkit, Composite body) {
		Section sctnEstimation = toolkit.createSection(body, Section.TWISTIE | Section.TITLE_BAR);
		sctnEstimation.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
		sctnEstimation.setText("Recursive Execution Settings");
		sctnEstimation.setExpanded(true);
		
		Composite client = toolkit.createComposite(sctnEstimation, SWT.NONE);
		sctnEstimation.setClient(client);
		toolkit.paintBordersFor(client);
		client.setLayout(new GridLayout(2, false));
		
		btnRecursive = toolkit.createButton(client, "Recursive Execution", SWT.CHECK);
		GridData gd_btnRecursive = new GridData();
		gd_btnRecursive.horizontalSpan = 2;
		btnRecursive.setLayoutData(gd_btnRecursive);
		
		toolkit.createLabel(client, "Window Size:");
		spnWindow = new Spinner(client, SWT.BORDER);
		spnWindow.setMinimum(1);
		spnWindow.setMaximum(Integer.MAX_VALUE);
		toolkit.paintBordersFor(spnWindow);
		
		bindingContext.bindValue(WidgetProperties.selection().observe(btnRecursive), 
				EMFEditProperties.value(getEditingDomain(), 
						FeaturePath.fromList(
								ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__ESTIMATION,
								ConfigurationPackage.Literals.ESTIMATION_SPECIFICATION__RECURSIVE
								)).observe(getModel()));
		bindingContext.bindValue(WidgetProperties.selection().observe(spnWindow), 
				EMFEditProperties.value(getEditingDomain(), 
						FeaturePath.fromList(
								ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__ESTIMATION,
								ConfigurationPackage.Literals.ESTIMATION_SPECIFICATION__WINDOW
								)).observe(getModel()));
	}
	
	private EMFUpdateValueStrategy createDateConverterTargetToModel() {
		EMFUpdateValueStrategy strategy = new EMFUpdateValueStrategy(EMFUpdateValueStrategy.POLICY_UPDATE);
		strategy.setConverter(new Converter(Date.class, Long.class) {			
			@Override
			public Object convert(Object fromObject) {
				return ((Date)fromObject).getTime();
			}
		});
		return strategy;
	}
	
	private EMFUpdateValueStrategy createDateConverterModelToTarget() {
		EMFUpdateValueStrategy strategy = new EMFUpdateValueStrategy(EMFUpdateValueStrategy.POLICY_UPDATE);
		strategy.setConverter(new Converter(Long.class, Date.class) {			
			@Override
			public Object convert(Object fromObject) {
				return new Date((long)fromObject);
			}
		});
		return strategy;
	}

}
