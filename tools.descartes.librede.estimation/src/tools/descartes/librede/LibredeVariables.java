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
package tools.descartes.librede;

import java.util.HashMap;

import tools.descartes.librede.algorithm.EstimationAlgorithmFactory;
import tools.descartes.librede.configuration.EstimationApproachConfiguration;
import tools.descartes.librede.configuration.LibredeConfiguration;
import tools.descartes.librede.repository.CachingRepositoryCursor;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.MemoryObservationRepository;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.validation.CrossValidationCursor;

public class LibredeVariables {
	private LibredeConfiguration conf;
	private MemoryObservationRepository repo;
	private EstimationAlgorithmFactory algoFactory;
	private LibredeResults results;
	private HashMap<String, IRepositoryCursor> cursors;

	public LibredeVariables(LibredeConfiguration conf) {
		this.conf = conf;
		this.repo = new MemoryObservationRepository(conf.getWorkloadDescription());
		this.algoFactory = new EstimationAlgorithmFactory();
		this.cursors = new HashMap<String, IRepositoryCursor>();
		// case cross validation
		if (conf.getValidation().isValidateEstimates() && conf.getValidation().getValidationFolds() > 1) {
			this.results = new LibredeResults(conf.getEstimation().getApproaches().size(),
					conf.getValidation().getValidationFolds());
			for (EstimationApproachConfiguration currentConf : conf.getEstimation().getApproaches()) {
				CrossValidationCursor cursor = new CrossValidationCursor(
						repo.getCursor(conf.getEstimation().getStartTimestamp(), conf.getEstimation().getStepSize()),
						conf.getValidation().getValidationFolds(),
						(int) (conf.getEstimation().getEndTimestamp().minus(conf.getEstimation().getStartTimestamp())
								.getValue(Time.SECONDS) / conf.getEstimation().getStepSize().getValue(Time.SECONDS)));
				cursor.initPartitions();
				this.cursors.put(currentConf.getType(), (IRepositoryCursor) cursor);
			}
			// case no or one validation
		} else {
			this.results = new LibredeResults(conf.getEstimation().getApproaches().size(), 1);

			for (EstimationApproachConfiguration currentConf : conf.getEstimation().getApproaches()) {
				IRepositoryCursor cursor = new CachingRepositoryCursor(
						repo.getCursor(conf.getEstimation().getStartTimestamp(), conf.getEstimation().getStepSize()),
						conf.getEstimation().getWindow());
				this.cursors.put(currentConf.getType(), cursor);
			}
		}
	}

	public LibredeConfiguration getConf() {
		return conf;
	}

	public MemoryObservationRepository getRepo() {
		return repo;
	}

	public EstimationAlgorithmFactory getAlgoFactory() {
		return algoFactory;
	}

	public LibredeResults getResults() {
		return results;
	}

	public HashMap<String, IRepositoryCursor> getCursors() {
		return cursors;
	}

	public IRepositoryCursor getCursor(String cursorOfApproach) {
		return this.cursors.get(cursorOfApproach);
	}

	public void updateResults(LibredeResults newResults) {
		this.results = newResults;
	}
}
