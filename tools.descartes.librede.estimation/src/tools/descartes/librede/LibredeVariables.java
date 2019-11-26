/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2018, by Simon Spinner, Johannes Grohmann
 *  and Contributors.
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
import java.util.Map;

import tools.descartes.librede.algorithm.EstimationAlgorithmFactory;
import tools.descartes.librede.configuration.ConstantDataPoint;
import tools.descartes.librede.configuration.EstimationApproachConfiguration;
import tools.descartes.librede.configuration.LibredeConfiguration;
import tools.descartes.librede.configuration.ObservationToEntityMapping;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.repository.CachingRepositoryCursor;
import tools.descartes.librede.repository.IMonitoringRepository;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.MemoryObservationRepository;
import tools.descartes.librede.repository.handlers.ConstantHandler;
import tools.descartes.librede.repository.rules.DerivationRule;
import tools.descartes.librede.units.Dimension;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.validation.ContinuousCrossValidationCursor;

/**
 * Object of this class contain all initialized Librede variables required for
 * running an estimation.
 * 
 * @author Simon Spinner
 *
 */
public class LibredeVariables {
	private LibredeConfiguration conf;
	private IMonitoringRepository repo;
	private EstimationAlgorithmFactory algoFactory;
	private LibredeResults results;
	private Map<String, IRepositoryCursor> cursors;
	private int runNr;

	public LibredeVariables(LibredeConfiguration conf) {
		this.conf = conf;
		this.repo = createRepository(conf);
		this.algoFactory = new EstimationAlgorithmFactory(conf.getEstimation().getAlgorithms());
		this.cursors = new HashMap<String, IRepositoryCursor>();
		this.runNr = 1;
		setup();

	}

	public LibredeVariables(LibredeConfiguration conf, IMonitoringRepository repo) {
		this.conf = conf;
		this.repo = repo;
		this.algoFactory = new EstimationAlgorithmFactory(conf.getEstimation().getAlgorithms());
		this.cursors = new HashMap<String, IRepositoryCursor>();
		this.runNr = 1;
		setup();
	}

	private void setup() {
		// case cross validation
		if (conf.getValidation().isValidateEstimates() && conf.getValidation().getValidationFolds() > 1) {
			this.results = new LibredeResults(conf.getEstimation().getApproaches().size(),
					conf.getValidation().getValidationFolds());
			for (EstimationApproachConfiguration currentConf : conf.getEstimation().getApproaches()) {
				ContinuousCrossValidationCursor cursor = new ContinuousCrossValidationCursor(
						repo.getCursor(conf.getEstimation().getStartTimestamp(), conf.getEstimation().getStepSize()),
						conf.getValidation().getValidationFolds(), conf.getEstimation().getWindow(), true);
				this.cursors.put(currentConf.getType(), cursor);
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

	public IMonitoringRepository getRepo() {
		return repo;
	}

	public void setRepo(IMonitoringRepository repo) {
		this.repo = repo;
	}

	public EstimationAlgorithmFactory getAlgoFactory() {
		return algoFactory;
	}

	public LibredeResults getResults() {
		return results;
	}

	public Map<String, IRepositoryCursor> getCursors() {
		return cursors;
	}

	public IRepositoryCursor getCursor(String cursorOfApproach) {
		return this.cursors.get(cursorOfApproach);
	}

	public int getRunNr() {
		return runNr;
	}

	public void incrementRunNr() {
		this.runNr++;
	}

	public void resetRunNr() {
		this.runNr = 1;
	}

	public void updateResults(LibredeResults newResults) {
		this.results = newResults;
	}

	@SuppressWarnings("unchecked")
	private static IMonitoringRepository createRepository(LibredeConfiguration conf) {
		IMonitoringRepository repo = new MemoryObservationRepository(conf.getWorkloadDescription());
		// Load constant data points into repository
		for (ConstantDataPoint curValue : conf.getInput().getConstantDataPoints()) {
			DerivationRule<?> rule = DerivationRule
					.rule((Metric<Dimension>) curValue.getMetric(), curValue.getAggregation())
					.build(new ConstantHandler<Dimension>((Quantity<Dimension>) curValue.getValue()));
			for (ObservationToEntityMapping curMapping : curValue.getMappings()) {
				repo.insertDerivation(rule, curMapping.getEntity());
			}
		}
		return repo;
	}

	public void reset() {
		this.resetRunNr();
		this.algoFactory = new EstimationAlgorithmFactory(conf.getEstimation().getAlgorithms());
		this.cursors = new HashMap<String, IRepositoryCursor>();
		setup();
	}

}
