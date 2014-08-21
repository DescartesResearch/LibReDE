package edu.kit.ipd.descartes.librede.ui.forms.master;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.descartesresearch.librede.configuration.ConfigurationFactory;
import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.TimeSeries;
import net.descartesresearch.librede.configuration.impl.TimeSeriesImpl;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.list.AbstractObservableList;
import org.eclipse.core.databinding.observable.list.ComputedList;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.databinding.observable.set.ComputedSet;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.databinding.viewers.ObservableListTreeContentProvider;
import org.eclipse.jface.databinding.viewers.TreeStructureAdvisor;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;

import edu.kit.ipd.descartes.librede.estimation.repository.StandardMetric;
import edu.kit.ipd.descartes.librede.ui.forms.MeasurementDataFormPage;
import edu.kit.ipd.descartes.librede.ui.forms.details.MeasurementDataDetailsPage;

public class MeasurementDataMasterBlock extends AbstractMasterBlock {

	private Tree tree;
	private TreeViewer treeViewer;

	/**
	 * Create the master details block.
	 */
	public MeasurementDataMasterBlock(MeasurementDataFormPage page, EditingDomain domain, LibredeConfiguration model) {
		super(page, domain, model);
	}

	/**
	 * Register the pages.
	 * @param part
	 */
	@Override
	protected void registerPages(DetailsPart part) {
		MeasurementDataDetailsPage details = new MeasurementDataDetailsPage(domain, model);
		part.registerPage(TimeSeriesImpl.class, details);
	}

	/**
	 * Create the toolbar actions.
	 * @param managedForm
	 */
	@Override
	protected void createToolBarActions(IManagedForm managedForm) {
		// Create the toolbar actions
	}
	
	private class UniqueObservableList extends ComputedList {
		private IObservableList data;
		
		public UniqueObservableList(IObservableList data) {
			this.data = data;
		}

		@Override
		protected List<?> calculate() {
			HashSet<Object> set = new HashSet<>();
			for (int i = 0; i < data.size(); i++) {
				set.add(data.get(i));
			}
			return new ArrayList<Object>(set);
		}
	}
	
	private class FilteredList extends ComputedList {
		
		private String metric;
		
		public FilteredList(String metric) {
			this.metric = metric;
		}

		@Override
		protected List<?> calculate() {
			List<TimeSeries> list = new ArrayList<TimeSeries>();
			for (TimeSeries current : model.getInput().getObservations()) {
				if (current.getMetric().equals(metric)) {
					list.add(current);
				}
			}
			return list;
		}
		
	}

	private class MeasurementTreeObservableFactory implements IObservableFactory {

		@Override
		public IObservable createObservable(Object target) {
			if (target instanceof String) {
				return new FilteredList((String) target);
			} else if (target instanceof TimeSeries) {
				return null;
			} else {
				return (IObservable) target;
			}
		}
		
	}
	
	private class MeasurementTreeStructureAdvisor extends TreeStructureAdvisor {
		
		@Override
		public Object getParent(Object element) {			
			return super.getParent(element);
		}
		
		@Override
		public Boolean hasChildren(Object element) {
			if (element instanceof TimeSeries) {
				return false;
			} else if (element instanceof String) {
				return true;
			}
			return super.hasChildren(element);
		}
		
	}
	
	private void addMeasurementTraces(Object[] results) {
		for (Object r : results) {
			TimeSeries series = ConfigurationFactory.eINSTANCE.createTimeSeries();
			series.setMetric(r.toString());
			model.getInput().getObservations().add(series);
		}
	}

	@Override
	protected String getMasterSectionTitle() {
		return "All Measurement Traces";
	}

	@Override
	protected Control createItemsList(Composite parent) {
		Composite treeViewerComposite = new Composite(parent, SWT.NONE);
		treeViewerComposite.setLayout(new TreeColumnLayout());
		toolkit.adapt(treeViewerComposite);
		toolkit.paintBordersFor(treeViewerComposite);
		
		treeViewer = new TreeViewer(treeViewerComposite, SWT.BORDER);
		tree = treeViewer.getTree();
		tree.setHeaderVisible(false);
		tree.setLinesVisible(false);
		toolkit.paintBordersFor(tree);	
		
		ObservableListTreeContentProvider cp = new ObservableListTreeContentProvider(new MeasurementTreeObservableFactory(), new MeasurementTreeStructureAdvisor());
		
		treeViewer.setContentProvider(cp);
		
		IObservableList l = EMFEditProperties.list(domain, 
				FeaturePath.fromList(ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__INPUT, ConfigurationPackage.Literals.INPUT_SPECIFICATION__OBSERVATIONS)
				).observe(model);
		IObservableList l2 = EMFEditProperties.value(domain, ConfigurationPackage.Literals.TIME_SERIES__METRIC).observeDetail(l);
		treeViewer.setInput(new UniqueObservableList(l2));
		
		treeViewer.addSelectionChangedListener(this);
		
		return treeViewerComposite;
	}

	@Override
	protected void handleAdd() {
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(page.getSite().getShell(), new LabelProvider());
		dialog.setElements(StandardMetric.values());
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
		IStructuredSelection selection = (IStructuredSelection)treeViewer.getSelection();
		Iterator<?> iterator = selection.iterator();
		while(iterator.hasNext()) {
			Object o = iterator.next();
			if (o instanceof TimeSeries) {
				model.getInput().getObservations().remove(o);
			}
		}	
	}
}
