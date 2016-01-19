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
package tools.descartes.librede.approach;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import tools.descartes.librede.ResultTable;
import tools.descartes.librede.algorithm.EstimationAlgorithmFactory;
import tools.descartes.librede.algorithm.IEstimationAlgorithm;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.exceptions.EstimationException;
import tools.descartes.librede.exceptions.InitializationException;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.observation.IObservationModel;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.exceptions.OutOfMonitoredRangeException;
import tools.descartes.librede.units.Time;

public abstract class AbstractEstimationApproach implements IEstimationApproach {
	
	private static final Logger log = Logger.getLogger(AbstractEstimationApproach.class);

	private boolean iterative;
	private int estimationWindow;
	private EstimationAlgorithmFactory algorithmFactory;
	private WorkloadDescription workload;
	private List<IEstimationAlgorithm> algorithms;
	private IRepositoryCursor cursor;

	protected abstract List<IStateModel<?>> deriveStateModels(
			WorkloadDescription workload, IRepositoryCursor cursor);

	protected abstract IObservationModel<?, ?> deriveObservationModel(
			IStateModel<?> stateModel, IRepositoryCursor cursor);

	protected abstract IEstimationAlgorithm getEstimationAlgorithm(
			EstimationAlgorithmFactory factory);

	@Override
	public void initialize(WorkloadDescription workload,
			IRepositoryCursor cursor,
			EstimationAlgorithmFactory algorithmFactory, int estimationWindow,
			boolean iterative) {
		this.workload = workload;
		this.cursor = cursor;
		this.algorithmFactory = algorithmFactory;
		this.iterative = iterative;
		this.estimationWindow = estimationWindow;
	}

	public void constructEstimationDefinitions() throws InitializationException{
		if (workload == null || cursor == null) {
			throw new IllegalStateException();
		}
		List<IStateModel<?>> stateModels = deriveStateModels(workload, cursor);
		for(Iterator<IStateModel<?>> it = stateModels.iterator(); it.hasNext();) {
			if(it.next().getStateSize() == 0) {
				log.info("Prune empty state model.");
				it.remove();
			}
		}
		
		algorithms = new ArrayList<IEstimationAlgorithm>(
				stateModels.size());		
		
		for (IStateModel<?> sm : stateModels) {
			IObservationModel<?, ?> om = deriveObservationModel(sm, cursor);
			IEstimationAlgorithm algo = getEstimationAlgorithm(algorithmFactory);
			algo.initialize(sm, om, cursor, estimationWindow);
			algorithms.add(algo);
		}
	}
	
	@Override
	public void pruneEstimationDefinitions() {
		// TODO Remove this function from interface!		
	}
	
	@Override
	public ResultTable executeEstimation() throws EstimationException {
		if (algorithms == null) {
			throw new IllegalStateException();
		}

		try {
			Set<IEstimationAlgorithm> inactiveAlgorithms = new HashSet<IEstimationAlgorithm>();
			ResultTable.Builder builder = ResultTable.builder(
					this.getClass(), workload);
			int iterations = 0;
			if (iterative) {
				while (cursor.next()) {					
					builder.next(cursor.getIntervalEnd(cursor.getLastInterval()).getValue(Time.MILLISECONDS));
					
					for (int i = 0; i < algorithms.size(); i++) {
						IEstimationAlgorithm a = algorithms.get(i);
						if (a.isApplicable()) {
							try {
								a.update();
								Vector curEstimates = a.estimate();
								for (int j = 0; j < curEstimates.rows(); j++) {
									builder.set(a.getStateModel().getResourceDemand(j), curEstimates.get(j));
								}
								if (inactiveAlgorithms.remove(a)) {
									log.info("Estimation algorithm [" + i + "]: Reactived (new data available).");
								}
							} catch(OutOfMonitoredRangeException ex) {
								if (!inactiveAlgorithms.contains(a)) {
									log.warn("Estimation algorithm [" + i + "]: " + ex.getMessage());
									log.warn("Estimation algorithm [" + i + "]: Disabled due to missing data.", ex);
									inactiveAlgorithms.add(a);
								}
							}
						}
					}
					iterations++;
					builder.save();
				}
			} else {
				while(cursor.next()) {
					for (int i = 0; i < algorithms.size(); i++) {
						IEstimationAlgorithm a = algorithms.get(i);
						if (a.isApplicable()) {
							try {
								a.update();
								if (inactiveAlgorithms.remove(a)) {
									log.info("Estimation algorithm [" + i + "]: Reactived (new data available).");
								}
							} catch(OutOfMonitoredRangeException ex) {
								if (!inactiveAlgorithms.contains(a)) {
									log.warn("Estimation algorithm [" + i + "]: " + ex.getMessage());
									log.warn("Estimation algorithm [" + i + "]: Disabled due to missing data.", ex);
									inactiveAlgorithms.add(a);
								}
							}
						}
					}
					iterations++;
				}
				
				builder.next(cursor.getIntervalEnd(cursor.getLastInterval()).getValue(Time.MILLISECONDS));
				for (IEstimationAlgorithm a : algorithms) {
					Vector curEstimates = a.estimate();
					for (int i = 0; i < curEstimates.rows(); i++) {
						builder.set(a.getStateModel().getResourceDemand(i), curEstimates.get(i));
					}
				}
				builder.save();
			}
			
			
			if (iterations > 0) {
				log.info("Number of iterations for estimation approach: " + iterations);
			} else {
				log.warn("Estimation was skipped (zero iterations): start time=" + cursor.getIntervalStart(0) + ", current time=" + cursor.getRepository().getCurrentTime() + ", step size=" + cursor.getIntervalEnd(0).minus(cursor.getIntervalStart(0)));
			}
			
			return builder.build();
		} finally {
			for (IEstimationAlgorithm a: algorithms) {
				a.destroy();
			}
		}
	}
	
	protected int getEstimationWindow() {
		return estimationWindow;
	}

}
