package net.descartesresearch.librede.configuration.editor.forms.master;

import net.descartesresearch.librede.configuration.ConfigurationFactory;
import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.EstimationApproachConfiguration;
import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.editor.forms.AbstractEstimationConfigurationFormPage;
import net.descartesresearch.librede.configuration.editor.forms.details.EstimationApproachesDetailsPage;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IDetailsPageProvider;
import org.eclipse.ui.forms.IManagedForm;

import edu.kit.ipd.descartes.librede.approaches.IEstimationApproach;
import edu.kit.ipd.descartes.librede.factory.ComponentRegistry;

public class EstimationApproachesMasterBlock extends AbstractMasterBlock
		implements ISelectionChangedListener, IDetailsPageProvider {

	private Table tableApproaches;
	private CheckboxTableViewer tableApproachesViewer;

	private EMFDataBindingContext masterBindingContext = new EMFDataBindingContext();

	/**
	 * Create the master details block.
	 */
	public EstimationApproachesMasterBlock(
			AbstractEstimationConfigurationFormPage page, EditingDomain domain,
			LibredeConfiguration model) {
		super(page, domain, model);
	}

	/**
	 * Register the pages.
	 * 
	 * @param part
	 */
	@Override
	protected void registerPages(DetailsPart part) {
		part.setPageProvider(this);
	}

	/**
	 * Create the toolbar actions.
	 * 
	 * @param managedForm
	 */
	@Override
	protected void createToolBarActions(IManagedForm managedForm) {
		// Create the toolbar actions
	}

	@Override
	protected String getMasterSectionTitle() {
		return "All Estimation Approaches";
	}

	@Override
	protected Control createItemsList(Composite composite) {
		tableApproachesViewer = CheckboxTableViewer.newCheckList(composite,
				SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		tableApproaches = tableApproachesViewer.getTable();
		tableApproaches.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		toolkit.paintBordersFor(tableApproaches);

		tableApproachesViewer.setLabelProvider(new AdapterFactoryLabelProvider(page.getAdapterFactory()));
		tableApproachesViewer
				.setContentProvider(new ObservableListContentProvider());

		IObservableList approaches = new WritableList();
		for (Class<?> cl : ComponentRegistry.INSTANCE
				.getImplementationClasses(IEstimationApproach.class)) {
			EstimationApproachConfiguration a = ConfigurationFactory.eINSTANCE
					.createEstimationApproachConfiguration();
			a.setType(cl);
			approaches.add(a);
		}
		tableApproachesViewer.setInput(approaches);
		
		// Binding: All checked approaches are added to the model instance
		masterBindingContext
				.bindSet(
						ViewerProperties.checkedElements(
								EstimationApproachConfiguration.class).observe(
								tableApproachesViewer),
						EMFEditProperties
								.set(domain,
										FeaturePath
												.fromList(
														ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__ESTIMATION,
														ConfigurationPackage.Literals.ESTIMATION_SPECIFICATION__APPROACHES))
								.observe(model));

		return tableApproaches;
	}

	@Override
	public Object getPageKey(Object object) {
		if (object instanceof EstimationApproachConfiguration) {
			return ((EstimationApproachConfiguration)object).getType();
		}
		return null;
	}

	@Override
	public IDetailsPage getPage(Object key) {
		if (key instanceof Class<?>) {
			return new EstimationApproachesDetailsPage(page, domain, model, (Class<?>) key);
		}
		return null;
	}
}
