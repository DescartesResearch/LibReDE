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
package tools.descartes.librede.configuration.editor.forms.master;

import java.util.Iterator;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;

import tools.descartes.librede.configuration.ConfigurationFactory;
import tools.descartes.librede.configuration.ConfigurationPackage;
import tools.descartes.librede.configuration.LibredeConfiguration;
import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.TraceConfiguration;
import tools.descartes.librede.configuration.TraceToEntityMapping;
import tools.descartes.librede.configuration.editor.forms.ClassesViewerFilter;
import tools.descartes.librede.configuration.editor.forms.details.FileTraceDetailsPage;
import tools.descartes.librede.configuration.editor.util.PrettyPrinter;
import tools.descartes.librede.configuration.impl.FileTraceConfigurationImpl;
import tools.descartes.librede.registry.Registry;
import tools.descartes.librede.repository.IMetric;
import tools.descartes.librede.repository.StandardMetric;

public class TracesMasterBlock extends AbstractMasterBlockWithButtons {

	private Table tableTraces;
	private TableViewer tableTracesViewer;

	/**
	 * Create the master details block.
	 */
	public TracesMasterBlock(AdapterFactoryEditingDomain domain, LibredeConfiguration model) {
		super(domain, model);
	}

	/**
	 * Register the pages.
	 * @param part
	 */
	@Override
	protected void registerPages(DetailsPart part) {
		FileTraceDetailsPage details = new FileTraceDetailsPage(page, domain, model);
		part.registerPage(FileTraceConfigurationImpl.class, details);
	}

	private void addMeasurementTraces(Object[] results) {
		for (Object r : results) {
			TraceConfiguration series = ConfigurationFactory.eINSTANCE.createFileTraceConfiguration();
			if (model.getInput().getDataSources().size() > 0) {
				series.setProvider(model.getInput().getDataSources().get(0));
			}
			series.setMetric(r.toString());
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
			
			Command cmd = AddCommand.create(domain, model.getInput(), ConfigurationPackage.Literals.INPUT_SPECIFICATION__OBSERVATIONS, series);
			domain.getCommandStack().execute(cmd);
		}
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
		tableTracesViewer.addFilter(new ClassesViewerFilter(TraceConfiguration.class));
		tableTracesViewer.addSelectionChangedListener(this);
		
		return tableTraces;
	}

	@Override
	protected void handleAdd() {
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(page.getSite().getShell(), new LabelProvider() {
			@Override
			public String getText(Object element) {
				return PrettyPrinter.toCamelCase(((IMetric)element).getDisplayName());
			}
		});
		dialog.setElements(Registry.INSTANCE.getMetrics().toArray());
		dialog.setAllowDuplicates(false);
		dialog.setMultipleSelection(false);
		dialog.setTitle("Metrics");
		dialog.setMessage("Select a metric for the new measurement trace:");
		dialog.create();
		if (dialog.open() == Window.OK) {
			addMeasurementTraces(dialog.getResult());
		}		
	}

	@Override
	protected void handleRemove() {
		IStructuredSelection selection = (IStructuredSelection)tableTracesViewer.getSelection();
		Iterator<?> iterator = selection.iterator();
		while(iterator.hasNext()) {
			Object o = iterator.next();
			if (o instanceof TraceConfiguration) {
				model.getInput().getObservations().remove(o);
			}
		}	
	}
}
