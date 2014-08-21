package edu.kit.ipd.descartes.librede.ui.forms.master;

import java.util.Iterator;

import net.descartesresearch.librede.configuration.ConfigurationFactory;
import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.DataSource;
import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.TimeSeries;
import net.descartesresearch.librede.configuration.impl.DataSourceImpl;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Section;

import edu.kit.ipd.descartes.librede.estimation.repository.StandardMetric;
import edu.kit.ipd.descartes.librede.ui.forms.AbstractEstimationConfigurationFormPage;
import edu.kit.ipd.descartes.librede.ui.forms.details.DataProviderDetailsPage;

public class DataProviderMasterBlock extends AbstractMasterBlock implements ISelectionChangedListener {

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
		DataProviderDetailsPage details = new DataProviderDetailsPage();
		part.registerPage(DataSourceImpl.class, details);
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
		lstProvidersViewer = new ListViewer(composite, SWT.BORDER | SWT.V_SCROLL);
		lstProviders = lstProvidersViewer.getList();
		
		lstProvidersViewer.setContentProvider(new ObservableListContentProvider());
		IObservableList l = EMFEditProperties.list(domain, 
				FeaturePath.fromList(ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__INPUT, ConfigurationPackage.Literals.INPUT_SPECIFICATION__DATA_SOURCES)
				).observe(model);
		lstProvidersViewer.setInput(l);
		lstProvidersViewer.addSelectionChangedListener(this);
		
		return lstProviders;
	}

	@Override
	protected void handleAdd() {
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(page.getSite().getShell(), new LabelProvider());
		dialog.setElements(new String[] { "CSV Files" });
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
			DataSource source = ConfigurationFactory.eINSTANCE.createDataSource();
			source.setType((String)r);
			model.getInput().getDataSources().add(source);
		}
	}

	@Override
	protected void handleRemove() {
		IStructuredSelection selection = (IStructuredSelection)lstProvidersViewer.getSelection();
		Iterator<?> iterator = selection.iterator();
		while(iterator.hasNext()) {
			Object o = iterator.next();
			if (o instanceof DataSource) {
				model.getInput().getDataSources().remove(o);
			}
		}		
	}
}
