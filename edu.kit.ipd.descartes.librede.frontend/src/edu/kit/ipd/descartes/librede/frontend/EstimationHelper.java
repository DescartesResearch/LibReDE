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

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.descartesresearch.librede.configuration.EstimationApproachConfiguration;
import net.descartesresearch.librede.configuration.ExporterConfiguration;
import net.descartesresearch.librede.configuration.FileTraceConfiguration;
import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.Resource;
import net.descartesresearch.librede.configuration.Service;
import net.descartesresearch.librede.configuration.TraceConfiguration;
import net.descartesresearch.librede.configuration.TraceToEntityMapping;

import org.apache.log4j.Logger;

import edu.kit.ipd.descartes.librede.approaches.IEstimationApproach;
import edu.kit.ipd.descartes.librede.datasource.IDataSource;
import edu.kit.ipd.descartes.librede.exceptions.EstimationException;
import edu.kit.ipd.descartes.librede.exceptions.InitializationException;
import edu.kit.ipd.descartes.librede.export.IExporter;
import edu.kit.ipd.descartes.librede.registry.Instantiator;
import edu.kit.ipd.descartes.librede.registry.Registry;
import edu.kit.ipd.descartes.librede.repository.IMetric;
import edu.kit.ipd.descartes.librede.repository.IMonitoringRepository;
import edu.kit.ipd.descartes.librede.repository.IRepositoryCursor;
import edu.kit.ipd.descartes.librede.repository.MemoryObservationRepository;
import edu.kit.ipd.descartes.librede.repository.TimeSeries;
import edu.kit.ipd.descartes.librede.validation.CrossValidationCursor;
import edu.kit.ipd.descartes.librede.validation.ResponseTimeValidator;
import edu.kit.ipd.descartes.librede.validation.UtilizationValidator;
import edu.kit.ipd.descartes.librede.workload.WorkloadDescription;
import edu.kit.ipd.descartes.linalg.LinAlg;
import edu.kit.ipd.descartes.linalg.MatrixBuilder;
import edu.kit.ipd.descartes.linalg.Vector;

public class EstimationHelper {
	
	public static class EstimationResult {
		
		public final List<TimeSeries> estimates;
		public final MatrixBuilder lastEstimates;
		public final MatrixBuilder relativeUtilizationError;
		public final MatrixBuilder relativeResponseTimeError;
		
		public EstimationResult(WorkloadDescription workload) {
			estimates = new ArrayList<TimeSeries>();
			relativeUtilizationError = new MatrixBuilder(workload.getResources().size());
			relativeResponseTimeError = new MatrixBuilder(workload.getServices().size());
			lastEstimates =  new MatrixBuilder(workload.getResources().size() * workload.getServices().size());
		}
		
		public void addEstimates(TimeSeries curEstimates) {
			estimates.add(curEstimates);
			lastEstimates.addRow(curEstimates.getData().row(curEstimates.samples() - 1));
		}
		
		public void addFold(TimeSeries curEstimates, Vector curUtilError, Vector curRespTimeError) {
			addEstimates(curEstimates);
			relativeUtilizationError.addRow(curUtilError);
			relativeResponseTimeError.addRow(curRespTimeError);
		}
		
		public Vector getMeanRelativeUtilizationError() {
			return LinAlg.mean(relativeUtilizationError.toMatrix(), 0);
		}
		
		public Vector getMeanRelativeResponseTimeError() {
			return LinAlg.mean(relativeResponseTimeError.toMatrix(), 0);
		}
		
		public Vector getMeanEstimates() {
			return LinAlg.mean(lastEstimates.toMatrix(), 0);
		}
	
	}
	
	private static final Logger log = Logger.getLogger(EstimationHelper.class);
	
//	public static WorkloadDescription createWorkloadDescription(String[] services, String[] resources) {
//		List<Service> srv = new ArrayList<Service>(services.length);
//		List<Resource> res = new ArrayList<Resource>(resources.length);
//		
//		StringBuilder serviceNames = new StringBuilder("Services: ");
//		for (String s : services) {
//			srv.add(new Service(s));
//			serviceNames.append(s).append(" ");
//		}
//		log.info(serviceNames);
//		
//		StringBuilder resourceNames = new StringBuilder("Resources: ");
//		for (String r : resources) {
//			res.add(new Resource(r));
//			resourceNames.append(r).append(" ");
//		}
//		log.info(resourceNames);
//		return new WorkloadDescription(res, srv);
//	}
	
	public static IMonitoringRepository createRepository(WorkloadDescription workload, double endTime) {
		IMonitoringRepository repo = new MemoryObservationRepository(workload);
		repo.setCurrentTime(endTime / 1000.0);
		return repo;
	}
	
	public static void loadRepository(LibredeConfiguration conf, IMonitoringRepository repo) {
		Map<Class<?>, IDataSource> dataSources = new HashMap<Class<?>, IDataSource>();
		
		for (TraceConfiguration trace : conf.getInput().getObservations()) {
			if (trace instanceof FileTraceConfiguration) {
				FileTraceConfiguration fileTrace = (FileTraceConfiguration)trace;
				File file = new File(fileTrace.getFile());
				if (!file.exists()) {
					log.error("Measurement trace " + fileTrace.getFile() + " does not exist.");
					continue;
				}
				
				Class<?> dataSourceType = fileTrace.getProvider().getType();
				if (!dataSources.containsKey(dataSourceType)) {
					try {
						IDataSource newSource = (IDataSource) Instantiator.newInstance(dataSourceType, fileTrace.getProvider().getParameters());
						dataSources.put(dataSourceType, newSource);
					} catch (Exception e) {
						log.error("Could not instantiate data source " + fileTrace.getProvider().getName(), e);
						continue;
					}
				}
				IDataSource source = dataSources.get(dataSourceType);
				
				IMetric metric = Registry.INSTANCE.getMetric(fileTrace.getMetric());
				if (metric == null) {
					log.error("Unknown metric type: " + fileTrace.getMetric());
					continue;
				}
				
				for (TraceToEntityMapping mapping : fileTrace.getMappings()) {
					try {
						FileInputStream in = null;
						try {						
							in = new FileInputStream(file);
							TimeSeries data = source.load(in, mapping.getTraceColumn());
							data.setStartTime(conf.getEstimation().getStartTimestamp() / 1000.0);
							data.setEndTime(conf.getEstimation().getEndTimestamp() / 1000.0);
							
							if (fileTrace.getInterval() > 0) {
								repo.setAggregatedData(metric, mapping.getEntity(), data, fileTrace.getInterval() / 1000.0);
							} else {
								repo.setData(metric, mapping.getEntity(), data);
							}
						} finally {
							if (in != null) in.close();
						}
					} catch (Exception e) {
						log.error("Error reading measurement trace " + fileTrace.getFile() + ".", e);
					}					
				}
			}		
		}
	}
	
	public static Map<String, EstimationResult> runEstimation(LibredeConfiguration conf, IMonitoringRepository repository) throws Exception {
		Map<String, EstimationResult> allResults = new HashMap<String, EstimationResult>();
		if (conf.getEstimation().getApproaches().size() <= 0) {
			return Collections.emptyMap();
		}
		for (EstimationApproachConfiguration currentConf : conf.getEstimation().getApproaches()) {
			EstimationResult result = new EstimationResult(repository.getWorkload());
			IRepositoryCursor cursor = repository.getCursor(conf.getEstimation().getStartTimestamp() / 1000.0, 
					conf.getEstimation().getStepSize() / 1000.0);
			
			IEstimationApproach currentApproach;
			try {
				currentApproach = (IEstimationApproach) Instantiator.newInstance(currentConf.getType(), currentConf.getParameters());
			} catch(Exception ex) {
				log.error("Error instantiating estimation approach: " + currentConf.getType().getSimpleName(), ex);
				continue;
			}
			
			TimeSeries estimates = initAndExecuteEstimation(currentApproach, 
					repository.getWorkload(), 
					conf.getEstimation().getWindow(), 
					conf.getEstimation().isRecursive(),
					cursor);
			result.addEstimates(estimates);
			allResults.put(currentConf.getType().getSimpleName(), result);
		}
		return allResults;
	}
	
	private static TimeSeries initAndExecuteEstimation(IEstimationApproach approach,
			WorkloadDescription workload, int window, boolean iterative,
			IRepositoryCursor cursor) throws InstantiationException,
			IllegalAccessException, InitializationException,
			EstimationException {
		
		if (approach != null) {
			approach.initialize(workload, cursor, window, iterative);
			List<String> messages = new LinkedList<String>();
			if (approach.checkPreconditions(messages)) {
				TimeSeries estimates = approach.execute();
				return estimates;
			} else {
				log.warn("Preconditions of approach " + approach + " are not fulfilled. Skip estimation approach.");
				for (String msg : messages) {
					log.info(msg);
				}
				return TimeSeries.EMPTY;
			}
		} else {
			log.error("Unkown estimation approach: " + approach);
			throw new IllegalArgumentException();
		}
	}
	
	public static Map<String, EstimationResult> runEstimationWithCrossValidation(LibredeConfiguration conf, IMonitoringRepository repository) throws Exception {
		Map<String, EstimationResult> allResults = new HashMap<String, EstimationResult>();
		if (conf.getEstimation().getApproaches().size() <= 0) {
			return Collections.emptyMap();
		}
		for (EstimationApproachConfiguration currentConf : conf.getEstimation().getApproaches()) {
			EstimationResult result = new EstimationResult(repository.getWorkload());
			CrossValidationCursor cursor = new CrossValidationCursor(repository.getCursor(
					conf.getEstimation().getStartTimestamp() / 1000.0, 
					conf.getEstimation().getStepSize() / 1000.0), 
					conf.getValidation().getValidationFolds());
			cursor.initPartitions();
			
			IEstimationApproach currentApproach;
			try {
				currentApproach = (IEstimationApproach) Instantiator.newInstance(currentConf.getType(), currentConf.getParameters());
			} catch(Exception ex) {
				log.error("Error instantiating estimation approach: " + currentConf.getType(), ex);
				continue;
			}	
			
			for (int i = 0; i < conf.getValidation().getValidationFolds(); i++) {
				log.info("Running repetition " + (i + 1) + " of estimation approach " + currentConf.getType().getSimpleName());
				cursor.startTrainingPhase(i);			
				
				TimeSeries estimates = initAndExecuteEstimation(currentApproach, 
						repository.getWorkload(), 
						conf.getEstimation().getWindow(), 
						conf.getEstimation().isRecursive(), 
						cursor);
				if (estimates.isEmpty()) {
					break;
				}
				Vector state = estimates.getData().row(estimates.samples() - 1);
				
				cursor.startValidationPhase(i);
				
				UtilizationValidator utilValidator = new UtilizationValidator(repository.getWorkload(), cursor);
				ResponseTimeValidator respValidator = new ResponseTimeValidator(repository.getWorkload(), cursor);
				while(cursor.next()) {
					utilValidator.predict(state);
					respValidator.predict(state);
				}
				
				Vector relErrUtil = utilValidator.getPredictionError();
				Vector relErrResp = respValidator.getPredictionError();
				
				result.addFold(estimates, relErrUtil, relErrResp);
			}
			allResults.put(currentConf.getType().getSimpleName(), result);			
		}
		return allResults;
	}
	
	public static void exportResults(LibredeConfiguration conf, Map<String, EstimationResult> results) {
		for (ExporterConfiguration exportConf :conf.getOutput().getExporters()) {
			IExporter exporter;
			try {
				exporter = (IExporter) Instantiator.newInstance(exportConf.getType(), exportConf.getParameters());
			} catch (Exception e) {
				log.error("Could not instantiate exporter: " + exportConf.getName());
				continue;
			}
			for (String approach : results.keySet()) {
				EstimationResult curResult = results.get(approach);
				for (int i = 0; i < curResult.estimates.size(); i++) {
					try {
						exporter.writeResults(approach, i, curResult.estimates.get(i));
					} catch (Exception e) {
						log.error("Could not export results.", e);
					}
				}
			}
		}
	}
	
	public static void printSummary(WorkloadDescription workload,
			Map<String, EstimationResult> results) {
		// Estimates
		System.out.println("Estimates");
		int contentSize = workload.getServices().size() * 8;

		System.out.printf("%-20.20s | ", "Approach");
		for (Resource r : workload.getResources()) {
			System.out.printf("%-8.8s", r.getName());
		}
		System.out.println();

		System.out.printf("%20.20s | ", "");
		for (Service s : workload.getServices()) {
			System.out.printf("%-8.8s", s.getName());
		}
		System.out.println("|");

		for (int i = 0; i < (24 + contentSize); i++) {
			System.out.print("-");
		}
		System.out.println();

		for (String currentApproach : results.keySet()) {
			EstimationResult currentResult = results.get(currentApproach);
			System.out.printf("%-20.20s |", currentApproach);
			Vector estimates = currentResult.getMeanEstimates();
			if (estimates.isEmpty()) {
				System.out.printf("%-6.6s", "N/A");
			} else {
				for (int i = 0; i < estimates.rows(); i++) {
					System.out.printf(" %.5f", estimates.get(i));
				}
			}
			System.out.println(" |");
		}

		System.out.println();
		System.out.println("Cross-Validation Results:");
		int utilColumnSize = workload.getResources().size() * 8;
		int respColumnSize = workload.getServices().size() * 8;

		System.out.printf("%-20.20s | ", "Approach");
		System.out.printf("%-" + utilColumnSize + "." + utilColumnSize + "s| ",
				"U");
		System.out.printf("%-" + respColumnSize + "." + respColumnSize + "s|",
				"Rt");
		System.out.println();

		System.out.printf("%20.20s | ", "");
		for (Resource r : workload.getResources()) {
			System.out.printf("%-8.8s", r.getName());
		}
		System.out.print("| ");
		for (Service s : workload.getServices()) {
			System.out.printf("%-8.8s", s.getName());
		}
		System.out.println("|");

		for (int i = 0; i < (26 + utilColumnSize + respColumnSize); i++) {
			System.out.print("-");
		}
		System.out.println();

		for (String currentApproach : results.keySet()) {
			EstimationResult currentResult = results.get(currentApproach);
			System.out.printf("%-20.20s |", currentApproach);
			Vector utilErr = currentResult.getMeanRelativeUtilizationError();
			if (utilErr.isEmpty()) {
				System.out.printf("%-" + utilColumnSize + "." + utilColumnSize
						+ "s", "N/A");
			} else {
				for (int i = 0; i < utilErr.rows(); i++) {
					System.out.printf(" %6.2f%%", utilErr.get(i) * 100);
				}
			}
			System.out.print(" |");
			Vector respErr = currentResult.getMeanRelativeResponseTimeError();
			if (respErr.isEmpty()) {
				System.out.printf("%-" + respColumnSize + "." + respColumnSize
						+ "s", "N/A");
			} else {
				for (int i = 0; i < respErr.rows(); i++) {
					System.out.printf(" %6.2f%%", respErr.get(i) * 100);
				}
			}
			System.out.println(" |");
		}
	}

}
