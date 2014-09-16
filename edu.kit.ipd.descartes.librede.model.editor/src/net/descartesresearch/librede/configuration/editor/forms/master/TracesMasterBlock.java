package net.descartesresearch.librede.configuration.editor.forms.master;

import java.util.Iterator;

import net.descartesresearch.librede.configuration.ConfigurationFactory;
import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.ModelEntity;
import net.descartesresearch.librede.configuration.TraceConfiguration;
import net.descartesresearch.librede.configuration.TraceToEntityMapping;
import net.descartesresearch.librede.configuration.editor.forms.ClassesViewerFilter;
import net.descartesresearch.librede.configuration.editor.forms.details.FileTraceDetailsPage;
import net.descartesresearch.librede.configuration.editor.util.PrettyPrinter;
import net.descartesresearch.librede.configuration.impl.FileTraceConfigurationImpl;

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

import edu.kit.ipd.descartes.librede.estimation.repository.IMetric;
import edu.kit.ipd.descartes.librede.estimation.repository.StandardMetric;
import edu.kit.ipd.descartes.librede.factory.Registry;

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

	/**
	 * Create the toolbar actions.
	 * @param managedForm
	 */
	@Override
	protected void createToolBarActions(IManagedForm managedForm) {
		// Create the toolbar actions
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
