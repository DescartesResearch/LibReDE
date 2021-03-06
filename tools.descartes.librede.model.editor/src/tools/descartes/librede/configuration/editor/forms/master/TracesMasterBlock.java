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
package tools.descartes.librede.configuration.editor.forms.master;

import java.util.Iterator;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.DetailsPart;

import tools.descartes.librede.configuration.ConfigurationFactory;
import tools.descartes.librede.configuration.ConfigurationPackage;
import tools.descartes.librede.configuration.InputSpecification;
import tools.descartes.librede.configuration.LibredeConfiguration;
import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.TraceConfiguration;
import tools.descartes.librede.configuration.TraceToEntityMapping;
import tools.descartes.librede.configuration.editor.forms.ClassesViewerFilter;
import tools.descartes.librede.configuration.editor.forms.details.FileTraceDetailsPage;
import tools.descartes.librede.configuration.impl.FileTraceConfigurationImpl;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.UnitsFactory;

public class TracesMasterBlock extends AbstractMasterBlockWithButtons {

	private Table tableTraces;
	private TableViewer tableTracesViewer;
	private FileTraceDetailsPage details;

	/**
	 * Create the master details block.
	 */
	public TracesMasterBlock(AdapterFactoryEditingDomain domain, LibredeConfiguration model) {
		super(domain, model);
		initializeValues();
	}

	/**
	 * Register the pages.
	 * @param part
	 */
	@Override
	protected void registerPages(DetailsPart part) {
		details = new FileTraceDetailsPage(page, domain, model);
		part.registerPage(FileTraceConfigurationImpl.class, details);
	}

	@Override
	protected String getMasterSectionTitle() {
		return "All Measurement Traces";
	}

	@Override
	protected Control createItemsList(Composite parent) {
		tableTracesViewer = new TableViewer(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		tableTraces = tableTracesViewer.getTable();
		
		tableTracesViewer.setContentProvider(new AdapterFactoryContentProvider(page.getAdapterFactory()));
		tableTracesViewer.setLabelProvider(new AdapterFactoryLabelProvider(page.getAdapterFactory()));
		
		tableTracesViewer.setInput(model.getInput());
		tableTracesViewer.addFilter(new ClassesViewerFilter(InputSpecification.class, TraceConfiguration.class));
		tableTracesViewer.addSelectionChangedListener(this);
		
		registerViewer(tableTracesViewer);
		
		return tableTraces;
	}

	@Override
	protected void handleAdd() {
		TraceConfiguration series = null;
		
		// The user can select any trace before clicking add
		// and the new configuration will have the same initial values
		ISelection sel = tableTracesViewer.getSelection();
		if (!sel.isEmpty()) {
			TraceConfiguration selSeries = (TraceConfiguration)((IStructuredSelection)sel).getFirstElement();
			series = EcoreUtil.copy(selSeries);
		}
		
		if (series == null) {
			series = ConfigurationFactory.eINSTANCE.createFileTraceConfiguration();
			
			// Initialize default values
			if (model.getInput().getDataSources().size() > 0) {
				series.setDataSource(model.getInput().getDataSources().get(0));
				series.setMetric(StandardMetrics.RESPONSE_TIME);
				series.setUnit(Time.INSTANCE.getBaseUnit());
				series.setAggregation(Aggregation.NONE);
				Quantity<Time> interval = UnitsFactory.eINSTANCE.createQuantity();
				interval.setUnit(Time.SECONDS);
				interval.setValue(0);				
				series.setInterval(interval);
				
				ModelEntity entity = null;
				if (model.getWorkloadDescription().getResources().size() > 0) {
					entity = model.getWorkloadDescription().getResources().get(0);
				} else if (model.getWorkloadDescription().getServices().size() > 0) {
					entity = model.getWorkloadDescription().getServices().get(0);
				}
				
				if (entity != null) {
					TraceToEntityMapping mapping = ConfigurationFactory.eINSTANCE.createTraceToEntityMapping();
					mapping.setEntity(entity);
					mapping.setTraceColumn(1);
					series.getMappings().add(mapping);
				}
			}
		}		
		Command cmd = AddCommand.create(domain, model.getInput(), ConfigurationPackage.Literals.INPUT_SPECIFICATION__OBSERVATIONS, series);
		domain.getCommandStack().execute(cmd);
	}

	@Override
	protected void handleRemove() {
		IStructuredSelection selection = (IStructuredSelection)tableTracesViewer.getSelection();
		Iterator<?> iterator = selection.iterator();
		while(iterator.hasNext()) {
			Object o = iterator.next();
			if (o instanceof TraceConfiguration) {
				Command cmd = RemoveCommand.create(domain, o);
				domain.getCommandStack().execute(cmd);
			}
		}	
	}
	
	private void initializeValues() {
		for (TraceConfiguration trace : model.getInput().getObservations()) {
			if (trace.getInterval() == null) {
				Quantity<Time> interval = UnitsFactory.eINSTANCE.createQuantity();
				interval.setValue(0);
				interval.setUnit(Time.SECONDS);
				Command cmd = SetCommand.create(domain, trace,
						ConfigurationPackage.Literals.TRACE_CONFIGURATION__INTERVAL, interval);
				domain.getCommandStack().execute(cmd);
			}
			if (trace.getAggregation() == null) {
				if (trace.getInterval().getValue() > 0) {
					trace.setAggregation(Aggregation.AVERAGE);
				} else {
					trace.setAggregation(Aggregation.NONE);
				}
			}
		}
	}
}
