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
