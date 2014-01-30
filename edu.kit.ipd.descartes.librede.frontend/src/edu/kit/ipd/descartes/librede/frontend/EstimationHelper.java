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
package edu.kit.ipd.descartes.librede.frontend;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.kit.ipd.descartes.librede.approaches.EstimationApproachFactory;
import edu.kit.ipd.descartes.librede.approaches.IEstimationApproach;
import edu.kit.ipd.descartes.librede.estimation.exceptions.EstimationException;
import edu.kit.ipd.descartes.librede.estimation.exceptions.InitializationException;
import edu.kit.ipd.descartes.librede.estimation.repository.IMonitoringRepository;
import edu.kit.ipd.descartes.librede.estimation.repository.IRepositoryCursor;
import edu.kit.ipd.descartes.librede.estimation.repository.MemoryObservationRepository;
import edu.kit.ipd.descartes.librede.estimation.repository.TimeSeries;
import edu.kit.ipd.descartes.librede.estimation.validation.CrossValidationCursor;
import edu.kit.ipd.descartes.librede.estimation.validation.ResponseTimeValidator;
import edu.kit.ipd.descartes.librede.estimation.validation.UtilizationValidator;
import edu.kit.ipd.descartes.librede.estimation.workload.Resource;
import edu.kit.ipd.descartes.librede.estimation.workload.Service;
import edu.kit.ipd.descartes.librede.estimation.workload.WorkloadDescription;
import edu.kit.ipd.descartes.linalg.LinAlg;
import edu.kit.ipd.descartes.linalg.Vector;

public class EstimationHelper {
	
	public static class EstimationResult {
		
		public final TimeSeries estimates;
		public final Vector relativeUtilizationError;
		public final Vector relativeResponseTimeError;
		
		public EstimationResult(TimeSeries estimates) {
			this(estimates, LinAlg.empty(), LinAlg.empty());
		}
		
		public EstimationResult(TimeSeries estimates,
				Vector relativeUtilizationError,
				Vector relativeResponseTimeError) {
			super();
			this.estimates = estimates;
			this.relativeUtilizationError = relativeUtilizationError;
			this.relativeResponseTimeError = relativeResponseTimeError;
		}		
	}
	
	private static final Logger log = Logger.getLogger(EstimationHelper.class);
	
	public static WorkloadDescription createWorkloadDescription(String[] services, String[] resources) {
		List<Service> srv = new ArrayList<Service>(services.length);
		List<Resource> res = new ArrayList<Resource>(resources.length);
		
		StringBuilder serviceNames = new StringBuilder("Services: ");
		for (String s : services) {
			srv.add(new Service(s));
			serviceNames.append(s).append(" ");
		}
		log.info(serviceNames);
		
		StringBuilder resourceNames = new StringBuilder("Resources: ");
		for (String r : resources) {
			res.add(new Resource(r));
			resourceNames.append(r).append(" ");
		}
		log.info(resourceNames);
		return new WorkloadDescription(res, srv);
	}
	
	public static IMonitoringRepository createRepository(WorkloadDescription workload, double endTime) {
		IMonitoringRepository repo = new MemoryObservationRepository(workload);
		repo.setCurrentTime(endTime);
		return repo;
	}
	
	public static EstimationResult runEstimation(String approach, WorkloadDescription workload, IMonitoringRepository repository, double startTime, double interval, int window, boolean iterative) throws Exception {
		IRepositoryCursor cursor = repository.getCursor(startTime, interval);
		return new EstimationResult(initAndExecuteEstimation(approach, workload, window, iterative,
				cursor));
	}

	private static TimeSeries initAndExecuteEstimation(String approach,
			WorkloadDescription workload, int window, boolean iterative,
			IRepositoryCursor cursor) throws InstantiationException,
			IllegalAccessException, InitializationException,
			EstimationException {
		IEstimationApproach estimator = EstimationApproachFactory.newEstimationApproach(approach);
		
		if (estimator != null) {
			estimator.initialize(workload, cursor, window, iterative);
			TimeSeries estimates = estimator.execute();
			return estimates;
		} else {
			log.error("Unkown estimation approach: " + approach);
			throw new IllegalArgumentException();
		}
	}
	
	public static List<EstimationResult> runEstimationWithCrossValidation(String approach, WorkloadDescription workload, IMonitoringRepository repository, double startTime, double interval, int window, boolean iterative) throws Exception {
		int kfold = 4;
		List<EstimationResult> results = new ArrayList<EstimationResult>();
		CrossValidationCursor cursor = new CrossValidationCursor(repository.getCursor(startTime, interval), kfold);
		cursor.initPartitions();
		for (int i = 0; i < kfold; i++) {
			cursor.startTrainingPhase(i);
			TimeSeries estimates = initAndExecuteEstimation(approach, workload, window, iterative, cursor);
			Vector state = estimates.getData().row(estimates.samples() - 1);
			
			cursor.startValidationPhase(i);
			
			UtilizationValidator utilValidator = new UtilizationValidator(workload, cursor);
			ResponseTimeValidator respValidator = new ResponseTimeValidator(workload, cursor);
			while(cursor.next()) {
				utilValidator.predict(state);
				respValidator.predict(state);
			}
			
			Vector relErrUtil = utilValidator.getPredictionError();
			Vector relErrResp = respValidator.getPredictionError();
			
			results.add(new EstimationResult(estimates, relErrUtil, relErrResp));
		}
		return results;
	}

}
