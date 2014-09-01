package net.descartesresearch.librede.configuration.editor.forms.master;

import java.util.Iterator;

import net.descartesresearch.librede.configuration.ConfigurationFactory;
import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.DataSourceConfiguration;
import net.descartesresearch.librede.configuration.ExporterConfiguration;
import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.editor.forms.AbstractEstimationConfigurationFormPage;
import net.descartesresearch.librede.configuration.editor.forms.details.OutputDetailsPage;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IDetailsPageProvider;
import org.eclipse.ui.forms.IManagedForm;

import edu.kit.ipd.descartes.librede.export.IExporter;
import edu.kit.ipd.descartes.librede.factory.ComponentRegistry;

public class OutputMasterBlock extends AbstractMasterBlockWithButtons implements IDetailsPageProvider {
	
	private List lstExporters;
	private ListViewer lstExportersViewer;

	public OutputMasterBlock(AbstractEstimationConfigurationFormPage page,
			EditingDomain domain, LibredeConfiguration model) {
		super(page, domain, model);
	}

	@Override
	protected void handleAdd() {
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(page.getSite().getShell(), new LabelProvider());
		dialog.setElements(ComponentRegistry.INSTANCE.getImplementationClasses(IExporter.class).toArray());
		dialog.setAllowDuplicates(false);
		dialog.setMultipleSelection(false);
		dialog.setTitle("Exporter Types");
		dialog.setMessage("Select a type for the new exporter:");
		dialog.create();
		if (dialog.open() == Window.OK) {
			addExporters(dialog.getResult());
		}	
	}
	
	private void addExporters(Object[] results) {
		for (Object r : results) {
			ExporterConfiguration source = ConfigurationFactory.eINSTANCE.createExporterConfiguration();
			source.setType((Class<?>)r);
			
			Command cmd = AddCommand.create(domain, model.getOutput(), ConfigurationPackage.Literals.OUTPUT_SPECIFICATION__EXPORTERS, source);
			domain.getCommandStack().execute(cmd);
		}
	}

	@Override
	protected void handleRemove() {
		IStructuredSelection selection = (IStructuredSelection)lstExportersViewer.getSelection();
		Iterator<?> iterator = selection.iterator();
		while(iterator.hasNext()) {
			Object o = iterator.next();
			if (o instanceof DataSourceConfiguration) {
				domain.getCommandStack().execute(RemoveCommand.create(domain, o));
			}
		}		
	}

	@Override
	protected String getMasterSectionTitle() {
		return "All Exporters";
	}

	@Override
	protected Control createItemsList(Composite parent) {
		lstExportersViewer = new ListViewer(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		lstExporters = lstExportersViewer.getList();
		
		lstExportersViewer.setContentProvider(new ObservableListContentProvider());
		lstExportersViewer.setLabelProvider(new AdapterFactoryLabelProvider(page.getAdapterFactory()));
		IObservableList l = EMFEditProperties.list(domain, 
				FeaturePath.fromList(ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__OUTPUT, ConfigurationPackage.Literals.OUTPUT_SPECIFICATION__EXPORTERS)
				).observe(model);
		lstExportersViewer.setInput(l);
		lstExportersViewer.addSelectionChangedListener(this);
		
		return lstExporters;
	}

	@Override
	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.setPageProvider(this);
	}

	@Override
	protected void createToolBarActions(IManagedForm managedForm) {
		// TODO Auto-generated method stub		
	}

	@Override
	public Object getPageKey(Object object) {
		if (object instanceof ExporterConfiguration) {
			return ((ExporterConfiguration)object).getType();
		}
		return null;
	}

	@Override
	public IDetailsPage getPage(Object key) {
		return new OutputDetailsPage(page, domain, model, (Class<?>)key);
	}

}
