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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IDetailsPageProvider;

import tools.descartes.librede.algorithm.IEstimationAlgorithm;
import tools.descartes.librede.configuration.ConfigurationFactory;
import tools.descartes.librede.configuration.ConfigurationPackage;
import tools.descartes.librede.configuration.EstimationAlgorithmConfiguration;
import tools.descartes.librede.configuration.EstimationSpecification;
import tools.descartes.librede.configuration.LibredeConfiguration;
import tools.descartes.librede.configuration.editor.forms.ClassesViewerFilter;
import tools.descartes.librede.configuration.editor.forms.details.ParametersDetailsPage;
import tools.descartes.librede.registry.Registry;

public class EstimationApproachesMasterBlock extends AbstractMasterBlock
		implements ISelectionChangedListener, IDetailsPageProvider {

	private Table tableAlgorithms;
	private TableViewer tableAlgorithmsViewer;

	/**
	 * Create the master details block.
	 */
	public EstimationApproachesMasterBlock(AdapterFactoryEditingDomain domain,
			LibredeConfiguration model) {
		super(domain, model);
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

	@Override
	protected String getMasterSectionTitle() {
		return "All Estimation Algorithms";
	}

	@Override
	protected Control createItemsList(Composite composite) {
		tableAlgorithmsViewer = new TableViewer(composite,
				SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		tableAlgorithms = tableAlgorithmsViewer.getTable();

		tableAlgorithmsViewer.setLabelProvider(new AdapterFactoryLabelProvider(page.getAdapterFactory()));
		tableAlgorithmsViewer
				.setContentProvider(new AdapterFactoryContentProvider(page.getAdapterFactory()));

		// We need to add the existing validators in the configuration first, so that the test for
		// equality in the checked table binding works correctly. EMF always does an equality check on
		// the object instance.
		Set<String> existingAlgorithms = new HashSet<String>();
		for (EstimationAlgorithmConfiguration v : model.getEstimation().getAlgorithms()) {
			existingAlgorithms.add(v.getType());
		}
		for (String instance : Registry.INSTANCE
				.getInstances(IEstimationAlgorithm.class)) {
			if (!existingAlgorithms.contains(instance)) {
				EstimationAlgorithmConfiguration a = ConfigurationFactory.eINSTANCE.createEstimationAlgorithmConfiguration();
				a.setType(instance);
				Command cmd = AddCommand.create(domain, model.getEstimation(), ConfigurationPackage.Literals.ESTIMATION_SPECIFICATION__ALGORITHMS, a);
				domain.getCommandStack().execute(cmd);
			}
		}
		tableAlgorithmsViewer.setInput(model.getEstimation());
		tableAlgorithmsViewer.addFilter(new ClassesViewerFilter(EstimationSpecification.class, EstimationAlgorithmConfiguration.class));
		tableAlgorithmsViewer.addSelectionChangedListener(this);
		
		registerViewer(tableAlgorithmsViewer);
		
		return tableAlgorithms;
	}

	@Override
	public Object getPageKey(Object object) {
		if (object instanceof EstimationAlgorithmConfiguration) {
			return ((EstimationAlgorithmConfiguration)object).getType();
		}
		return null;
	}

	@Override
	public IDetailsPage getPage(Object key) {
		if (key instanceof String) {
			return new ParametersDetailsPage(page, 
					domain, 
					"Estimation Algorithm Configuration", 
					ConfigurationPackage.Literals.ESTIMATION_ALGORITHM_CONFIGURATION,
					(String) key, 
					ConfigurationPackage.Literals.ESTIMATION_ALGORITHM_CONFIGURATION__PARAMETERS);
		}
		return null;
	}
}
