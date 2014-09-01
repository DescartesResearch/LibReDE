package net.descartesresearch.librede.configuration.editor.forms.master;

import java.util.Iterator;

import net.descartesresearch.librede.configuration.ConfigurationFactory;
import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.DataProviderConfiguration;
import net.descartesresearch.librede.configuration.DataSourceConfiguration;
import net.descartesresearch.librede.configuration.InputSpecification;
import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.editor.forms.AbstractEstimationConfigurationFormPage;
import net.descartesresearch.librede.configuration.editor.forms.details.DataProviderDetailsPage;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
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

import edu.kit.ipd.descartes.librede.datasource.IDataSource;
import edu.kit.ipd.descartes.librede.factory.ComponentRegistry;

public class DataProviderMasterBlock extends AbstractMasterBlockWithButtons implements ISelectionChangedListener, IDetailsPageProvider {

	private List lstProviders;
	private ListViewer lstProvidersViewer;

	/**
	 * Create the master details block.
	 */
	public DataProviderMasterBlock(AbstractEstimationConfigurationFormPage page, EditingDomain domain, LibredeConfiguration model) {
		super(page, domain, model);
	}

	/**
	 * Register the pages.
	 * @param part
	 */
	@Override
	protected void registerPages(DetailsPart part) {
		part.setPageProvider(this);
	}

	/**
	 * Create the toolbar actions.
	 * @param managedForm
	 */
	@Override
	protected void createToolBarActions(IManagedForm managedForm) {
		// Create the toolbar actions
	}

	@Override
	protected String getMasterSectionTitle() {
		return "All Data Providers";
	}

	@Override
	protected Control createItemsList(Composite composite) {
		lstProvidersViewer = new ListViewer(composite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		lstProviders = lstProvidersViewer.getList();
		
		lstProvidersViewer.setContentProvider(new ObservableListContentProvider());
		IObservableList l = EMFEditProperties.list(domain, 
				FeaturePath.fromList(ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__INPUT, ConfigurationPackage.Literals.INPUT_SPECIFICATION__DATA_PROVIDERS)
				).observe(model);
		lstProvidersViewer.setInput(l);
		lstProvidersViewer.addSelectionChangedListener(this);
		
		return lstProviders;
	}

	@Override
	protected void handleAdd() {
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(page.getSite().getShell(), new LabelProvider());
		dialog.setElements(ComponentRegistry.INSTANCE.getImplementationClasses(IDataSource.class).toArray());
		dialog.setAllowDuplicates(false);
		dialog.setMultipleSelection(false);
		dialog.setTitle("Data Provider Types");
		dialog.setMessage("Select a type for the new data provider:");
		dialog.create();
		if (dialog.open() == Window.OK) {
			addDataProviders(dialog.getResult());
		}		
	}

	private void addDataProviders(Object[] results) {
		for (Object r : results) {
			DataProviderConfiguration source = ConfigurationFactory.eINSTANCE.createDataProviderConfiguration();
			source.setType((Class<?>)r);
			
			Command cmd;
			if (model.getInput() == null) {
				InputSpecification input = ConfigurationFactory.eINSTANCE.createInputSpecification();
				input.getDataProviders().add(source);
				cmd = SetCommand.create(domain, model, ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__INPUT, input);
			} else {
				cmd = AddCommand.create(domain, model.getInput(), ConfigurationPackage.Literals.INPUT_SPECIFICATION__DATA_PROVIDERS, source);
			}	
			domain.getCommandStack().execute(cmd);
		}
	}

	@Override
	protected void handleRemove() {
		IStructuredSelection selection = (IStructuredSelection)lstProvidersViewer.getSelection();
		Iterator<?> iterator = selection.iterator();
		while(iterator.hasNext()) {
			Object o = iterator.next();
			if (o instanceof DataSourceConfiguration) {
				domain.getCommandStack().execute(RemoveCommand.create(domain, o));
			}
		}		
	}

	@Override
	public Object getPageKey(Object object) {
		if (object instanceof DataSourceConfiguration) {
			return ((DataSourceConfiguration)object).getType();
		}
		return null;
	}

	@Override
	public IDetailsPage getPage(Object key) {
		if (key instanceof Class<?>) {
			return new DataProviderDetailsPage(page, domain, model, ((Class<?>)key));
		}
		return null;
	}
}
