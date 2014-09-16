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

import java.util.Map;

import org.apache.log4j.Logger;

import edu.kit.ipd.descartes.librede.estimation.repository.IMonitoringRepository;
import edu.kit.ipd.descartes.librede.estimation.workload.WorkloadDescription;
import edu.kit.ipd.descartes.librede.frontend.EstimationHelper.EstimationResult;
import net.descartesresearch.librede.configuration.LibredeConfiguration;

public class LibredeEstimation {
	
	private static final Logger log = Logger.getLogger(LibredeEstimation.class);
	
	public static void execute(LibredeConfiguration conf) {
		WorkloadDescription workload = new WorkloadDescription(conf.getWorkloadDescription().getResources(), conf.getWorkloadDescription().getServices());
		IMonitoringRepository repo = EstimationHelper.createRepository(
				workload, conf.getEstimation().getEndTimestamp());

		EstimationHelper.loadRepository(conf, repo);
		
		if (!conf.getValidation().isValidateEstimates()) {
			Map<String, EstimationResult> results;
			try {
				results = EstimationHelper
						.runEstimation(conf, repo);
				EstimationHelper.printSummary(workload, results);
				EstimationHelper.exportResults(conf, results);
			} catch (Exception e) {
				log.error("Error running estimation.", e);
			}			
		} else {
			Map<String, EstimationResult> results;
			try {
				results = EstimationHelper
						.runEstimationWithCrossValidation(conf, repo);
				EstimationHelper.printSummary(workload, results);
				EstimationHelper.exportResults(conf, results);
			} catch (Exception e) {
				log.error("Error running estimation.", e);
			}			
		}
	}
}
