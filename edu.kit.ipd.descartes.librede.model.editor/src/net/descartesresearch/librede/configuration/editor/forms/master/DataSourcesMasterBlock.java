package net.descartesresearch.librede.configuration.editor.forms.master;

import java.util.Iterator;

import net.descartesresearch.librede.configuration.ConfigurationFactory;
import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.DataSourceConfiguration;
import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.editor.forms.ClassesViewerFilter;
import net.descartesresearch.librede.configuration.editor.forms.details.ParametersDetailsPage;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
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
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IDetailsPageProvider;
import org.eclipse.ui.forms.IManagedForm;

import edu.kit.ipd.descartes.librede.datasource.IDataSource;
import edu.kit.ipd.descartes.librede.factory.Registry;

public class DataSourcesMasterBlock extends AbstractMasterBlockWithButtons implements ISelectionChangedListener, IDetailsPageProvider {
	
	private Table tableSources;
	private TableViewer tableSourcesViewer;

	/**
	 * Create the master details block.
	 */
	public DataSourcesMasterBlock(AdapterFactoryEditingDomain domain, LibredeConfiguration model) {
		super(domain, model);
	}

	/**
	 * Register the pages.
	 * @param part
	 */
	@Override
	protected void registerPages(DetailsPart part) {
		part.setPageProvider(this);
	}

	@Override
	protected String getMasterSectionTitle() {
		return "All Data Sources";
	}

	@Override
	protected Control createItemsList(Composite composite) {
		tableSourcesViewer = new TableViewer(composite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		tableSources = tableSourcesViewer.getTable();
		
		tableSourcesViewer.setContentProvider(new AdapterFactoryContentProvider(page.getAdapterFactory()));
		tableSourcesViewer.setLabelProvider(new AdapterFactoryLabelProvider(page.getAdapterFactory()));
		
		tableSourcesViewer.setInput(model.getInput());
		tableSourcesViewer.addFilter(new ClassesViewerFilter(DataSourceConfiguration.class));
		tableSourcesViewer.addSelectionChangedListener(this);
		
		return tableSources;
	}

	@Override
	protected void handleAdd() {
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(page.getSite().getShell(), new LabelProvider() {
			@Override
			public String getText(Object element) {
				return Registry.INSTANCE.getDisplayName((Class<?>)element);
			}
		});
		dialog.setElements(Registry.INSTANCE.getImplementationClasses(IDataSource.class).toArray());
		dialog.setAllowDuplicates(false);
		dialog.setMultipleSelection(false);
		dialog.setTitle("Data Source Types");
		dialog.setMessage("Select a type for the new data source:");
		dialog.create();
		if (dialog.open() == Window.OK) {
			addDataProviders(dialog.getResult());
		}		
	}

	private void addDataProviders(Object[] results) {
		for (Object r : results) {
			DataSourceConfiguration source = ConfigurationFactory.eINSTANCE.createDataSourceConfiguration();
			source.setName("New Data Source");
			source.setType((Class<?>)r);
			
			Command cmd = AddCommand.create(domain, model.getInput(), ConfigurationPackage.Literals.INPUT_SPECIFICATION__DATA_SOURCES, source);
			domain.getCommandStack().execute(cmd);
		}
	}

	@Override
	protected void handleRemove() {
		IStructuredSelection selection = (IStructuredSelection)tableSourcesViewer.getSelection();
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
			return new ParametersDetailsPage(page, 
					domain, 
					"Data Source Configuration", 
					ConfigurationPackage.Literals.DATA_SOURCE_CONFIGURATION,
					((Class<?>)key), 
					ConfigurationPackage.Literals.DATA_SOURCE_CONFIGURATION__PARAMETERS);
		}
		return null;
	}
}
