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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import tools.descartes.librede.algorithm.EstimationAlgorithmFactory;
import tools.descartes.librede.algorithm.IEstimationAlgorithm;
import tools.descartes.librede.algorithm.SimpleApproximation;
import tools.descartes.librede.approach.IEstimationApproach;
import tools.descartes.librede.approach.MenasceOptimizationApproach;
import tools.descartes.librede.approach.ResponseTimeApproximationApproach;
import tools.descartes.librede.approach.RoliaRegressionApproach;
import tools.descartes.librede.approach.ServiceDemandLawApproach;
import tools.descartes.librede.approach.WangKalmanFilterApproach;
import tools.descartes.librede.approach.ZhangKalmanFilterApproach;
import tools.descartes.librede.configuration.EstimationApproachConfiguration;
import tools.descartes.librede.configuration.ExporterConfiguration;
import tools.descartes.librede.configuration.LibredeConfiguration;
import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.TraceConfiguration;
import tools.descartes.librede.configuration.ValidatorConfiguration;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.datasource.DataSourceSelector;
import tools.descartes.librede.datasource.IDataSource;
import tools.descartes.librede.datasource.TraceEvent;
import tools.descartes.librede.datasource.TraceKey;
import tools.descartes.librede.datasource.csv.CsvDataSource;
import tools.descartes.librede.exceptions.EstimationException;
import tools.descartes.librede.exceptions.InitializationException;
import tools.descartes.librede.export.IExporter;
import tools.descartes.librede.export.csv.CsvExporter;
import tools.descartes.librede.linalg.LinAlg;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.MatrixBuilder;
import tools.descartes.librede.linalg.Scalar;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.state.StateVariable;
import tools.descartes.librede.registry.Instantiator;
import tools.descartes.librede.registry.Registry;
import tools.descartes.librede.repository.IMonitoringRepository;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.MemoryObservationRepository;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.repository.StandardMetricHelpers;
import tools.descartes.librede.repository.TimeSeries;
import tools.descartes.librede.units.Ratio;
import tools.descartes.librede.units.RequestCount;
import tools.descartes.librede.units.RequestRate;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.validation.CrossValidationCursor;
import tools.descartes.librede.validation.IValidator;
import tools.descartes.librede.validation.ResponseTimeValidator;
import tools.descartes.librede.validation.UtilizationValidator;

public class Librede {
	
	private static final Logger log = Logger.getLogger(Librede.class);	
	
	public static void init() {
		Registry.INSTANCE.registerDimension(Time.INSTANCE);
		Registry.INSTANCE.registerDimension(RequestCount.INSTANCE);
		Registry.INSTANCE.registerDimension(RequestRate.INSTANCE);
		Registry.INSTANCE.registerDimension(Ratio.INSTANCE);
		
		Registry.INSTANCE.registerMetric(StandardMetrics.ARRIVAL_RATE, StandardMetricHelpers.createHandler(StandardMetrics.ARRIVAL_RATE));
		Registry.INSTANCE.registerMetric(StandardMetrics.ARRIVALS, StandardMetricHelpers.createHandler(StandardMetrics.ARRIVALS));
		Registry.INSTANCE.registerMetric(StandardMetrics.BUSY_TIME, StandardMetricHelpers.createHandler(StandardMetrics.BUSY_TIME));
		Registry.INSTANCE.registerMetric(StandardMetrics.DEPARTURES, StandardMetricHelpers.createHandler(StandardMetrics.DEPARTURES));
		Registry.INSTANCE.registerMetric(StandardMetrics.IDLE_TIME, StandardMetricHelpers.createHandler(StandardMetrics.IDLE_TIME));
		Registry.INSTANCE.registerMetric(StandardMetrics.RESPONSE_TIME, StandardMetricHelpers.createHandler(StandardMetrics.RESPONSE_TIME));
		Registry.INSTANCE.registerMetric(StandardMetrics.THROUGHPUT, StandardMetricHelpers.createHandler(StandardMetrics.THROUGHPUT));
		Registry.INSTANCE.registerMetric(StandardMetrics.QUEUE_LENGTH_SEEN_ON_ARRIVAL, StandardMetricHelpers.createHandler(StandardMetrics.QUEUE_LENGTH_SEEN_ON_ARRIVAL));
		Registry.INSTANCE.registerMetric(StandardMetrics.UTILIZATION, StandardMetricHelpers.createHandler(StandardMetrics.UTILIZATION));
		
		Registry.INSTANCE.registerImplementationType(IDataSource.class, CsvDataSource.class);
		
		Registry.INSTANCE.registerImplementationType(IEstimationAlgorithm.class, SimpleApproximation.class);
		
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, ResponseTimeApproximationApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, ServiceDemandLawApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, RoliaRegressionApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, MenasceOptimizationApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, ZhangKalmanFilterApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, WangKalmanFilterApproach.class);
		
		Registry.INSTANCE.registerImplementationType(IValidator.class, ResponseTimeValidator.class);
		Registry.INSTANCE.registerImplementationType(IValidator.class, UtilizationValidator.class);
		
		Registry.INSTANCE.registerImplementationType(IExporter.class, CsvExporter.class);
	}
	
	public static void execute(LibredeConfiguration conf) {
		MemoryObservationRepository repo = new MemoryObservationRepository(conf.getWorkloadDescription());
		repo.setCurrentTime(conf.getEstimation().getEndTimestamp());

		loadRepository(conf, repo);
		
		if (!conf.getValidation().isValidateEstimates()) {
			
			try {
				List<ResultTable[]> results = runEstimation(conf, repo);
				printSummary(results);
				exportResults(conf, results);
			} catch (Exception e) {
				log.error("Error running estimation.", e);
			}			
		} else {
			try {
				List<ResultTable[]> results;
				if (conf.getValidation().getValidationFolds() <= 1) {
					results = runEstimationWithValidation(conf, repo);
				} else {
					results = runEstimationWithCrossValidation(conf, repo);
				}				
				printSummary(results);
				exportResults(conf, results);
			} catch (Exception e) {
				log.error("Error running estimation.", e);
			}			
		}
	}
	
	public static void loadRepository(LibredeConfiguration conf, MemoryObservationRepository repo) {
		Map<String, IDataSource> dataSources = new HashMap<String, IDataSource>();
		
		try (DataSourceSelector selector = new DataSourceSelector()) {		
			for (TraceConfiguration trace : conf.getInput().getObservations()) {
				String dataSourceName = trace.getDataSource().getName();
				if (!dataSources.containsKey(dataSourceName)) {
					Class<?> cl = Registry.INSTANCE.getInstanceClass(trace.getDataSource().getType());
					try {
						IDataSource newSource = (IDataSource) Instantiator.newInstance(cl, trace.getDataSource().getParameters());
						newSource.open();
						selector.add(newSource);
						dataSources.put(dataSourceName, newSource);
					} catch (Exception e) {
						log.error("Could not instantiate data source " + trace.getDataSource().getName(), e);
						continue;
					}
				}
				IDataSource source = dataSources.get(dataSourceName);
				try {
					source.addTrace(trace);
				} catch(IOException ex) {
					log.error("Error loading data.", ex);
				}
			}
			
			for (IDataSource ds : dataSources.values()) {
				ds.load();
			}
			
			TraceEvent curEvent = null;
			while ((curEvent = selector.poll()) != null) {
				TraceKey key = curEvent.getKey();
				TimeSeries ts = repo.select(key.getMetric(),key.getUnit(), key.getEntity());
				if (ts == null) {
					ts = curEvent.getData();
				} else {
					ts = ts.append(curEvent.getData());
				}
				ts.setStartTime(conf.getEstimation().getStartTimestamp().getValue(Time.SECONDS));
				ts.setEndTime(conf.getEstimation().getEndTimestamp().getValue(Time.SECONDS));
				
				if (curEvent.getKey().getInterval().getValue() > 0) {
					repo.insert(key.getMetric(), key.getUnit(), key.getEntity(), ts, key.getInterval());
				} else {
					repo.insert(key.getMetric(), key.getUnit(), key.getEntity(), ts);
				}
			}
		} catch (IOException e1) {
			log.error("Error closing selector");
		} finally {			
			for (IDataSource ds : dataSources.values()) {
				try {
					ds.close();
				} catch (IOException e) {
					log.error("Error closing data source.", e);
				}
			}
		}
	}
	
	public static List<ResultTable[]> runEstimation(LibredeConfiguration conf, IMonitoringRepository repository) throws Exception {
		EstimationAlgorithmFactory algoFactory = new EstimationAlgorithmFactory(conf);
		
		List<ResultTable[]> results = new ArrayList<ResultTable[]>();
		for (EstimationApproachConfiguration currentConf : conf.getEstimation().getApproaches()) {
			IRepositoryCursor cursor = repository.getCursor(conf.getEstimation().getStartTimestamp(), 
					conf.getEstimation().getStepSize());
			
			IEstimationApproach currentApproach;
			try {
				Class<?> cl = Registry.INSTANCE.getInstanceClass(currentConf.getType());
				currentApproach = (IEstimationApproach) Instantiator.newInstance(cl, currentConf.getParameters());
			} catch(Exception ex) {
				log.error("Error instantiating estimation approach: " + currentConf.getType(), ex);
				continue;
			}
			
			ResultTable estimates = initAndExecuteEstimation(currentApproach, 
					repository.getWorkload(), 
					conf.getEstimation().getWindow(), 
					conf.getEstimation().isRecursive(),
					cursor, algoFactory);
			results.add(new ResultTable[] {estimates});
		}
		return results;
	}
	
	public static List<ResultTable[]> runEstimationWithValidation(LibredeConfiguration conf, IMonitoringRepository repository) throws Exception {
		EstimationAlgorithmFactory algoFactory = new EstimationAlgorithmFactory(conf);
		
		List<ResultTable[]> results = new ArrayList<ResultTable[]>();
		for (EstimationApproachConfiguration currentConf : conf.getEstimation().getApproaches()) {
			IRepositoryCursor cursor = repository.getCursor(conf.getEstimation().getStartTimestamp(), 
					conf.getEstimation().getStepSize());
			
			IEstimationApproach currentApproach;
			try {
				Class<?> cl = Registry.INSTANCE.getInstanceClass(currentConf.getType());
				currentApproach = (IEstimationApproach) Instantiator.newInstance(cl, currentConf.getParameters());
			} catch(Exception ex) {
				log.error("Error instantiating estimation approach: " + currentConf.getType(), ex);
				continue;
			}
			
			ResultTable estimates = initAndExecuteEstimation(currentApproach, 
					repository.getWorkload(), 
					conf.getEstimation().getWindow(), 
					conf.getEstimation().isRecursive(), 
					cursor, algoFactory);
			if (estimates.getEstimates().isEmpty()) {
				break;
			}
			Vector state = estimates.getLastEstimates();
			
			
			
			IRepositoryCursor validatingCursor = repository.getCursor(conf.getEstimation().getStartTimestamp(), 
					conf.getEstimation().getStepSize());
			
			List<IValidator> validators = initValidators(conf, validatingCursor);
			
			runValidation(validators, validatingCursor, state, estimates);
				
			results.add(new ResultTable[] { estimates });
		}
		return results;	
	}
	
	public static List<ResultTable[]> runEstimationWithCrossValidation(LibredeConfiguration conf, IMonitoringRepository repository) throws Exception {
		EstimationAlgorithmFactory algoFactory = new EstimationAlgorithmFactory(conf);
		
		List<ResultTable[]> results = new ArrayList<ResultTable[]>();
		for (EstimationApproachConfiguration currentConf : conf.getEstimation().getApproaches()) {
			ResultTable[] folds = new ResultTable[conf.getValidation().getValidationFolds()];
			CrossValidationCursor cursor = new CrossValidationCursor(repository.getCursor(
					conf.getEstimation().getStartTimestamp(), 
					conf.getEstimation().getStepSize()), 
					conf.getValidation().getValidationFolds());
			cursor.initPartitions();
			
			IEstimationApproach currentApproach;
			try {
				Class<?> cl = Registry.INSTANCE.getInstanceClass(currentConf.getType());
				currentApproach = (IEstimationApproach) Instantiator.newInstance(cl, currentConf.getParameters());
			} catch(Exception ex) {
				log.error("Error instantiating estimation approach: " + currentConf.getType(), ex);
				continue;
			}
			
			List<IValidator> validators = initValidators(conf, cursor);
			
			for (int i = 0; i < conf.getValidation().getValidationFolds(); i++) {
				log.info("Running repetition " + (i + 1) + " of estimation approach " + currentConf.getType());
				cursor.startTrainingPhase(i);			
				
				ResultTable estimates = initAndExecuteEstimation(currentApproach, 
						repository.getWorkload(), 
						conf.getEstimation().getWindow(), 
						conf.getEstimation().isRecursive(), 
						cursor, algoFactory);
				if (estimates.getEstimates().isEmpty()) {
					break;
				}
				Vector state = estimates.getLastEstimates();
				
				cursor.startValidationPhase(i);
				
				runValidation(validators, cursor, state, estimates);
				
				folds[i] = estimates;
			}
			results.add(folds);
		}
		return results;
	}
	
	private static List<IValidator> initValidators(LibredeConfiguration conf, IRepositoryCursor validatingCursor) throws InstantiationException, IllegalAccessException {
		List<IValidator> validators = new ArrayList<IValidator>(conf.getValidation().getValidators().size());
		for (ValidatorConfiguration validator : conf.getValidation().getValidators()) {
			Class<?> cl = Registry.INSTANCE.getInstanceClass(validator.getType());
			IValidator val = (IValidator) Instantiator.newInstance(cl, validator.getParameters());
			val.initialize(conf.getWorkloadDescription(), validatingCursor);
			validators.add(val);
		}
		return validators;
	}
	
	private static void runValidation(List<IValidator> validators, IRepositoryCursor validatingCursor, Vector demands, ResultTable estimates){		
		Query<Vector, Ratio> util = QueryBuilder.select(StandardMetrics.UTILIZATION).in(Ratio.NONE).forResources(validatingCursor.getRepository().listResources()).average().using(validatingCursor);
		Query<Vector, RequestRate> tput = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forServices(validatingCursor.getRepository().listServices()).average().using(validatingCursor);
		Query<Vector, Time> resp = QueryBuilder.select(StandardMetrics.RESPONSE_TIME).in(Time.SECONDS).forServices(validatingCursor.getRepository().listServices()).average().using(validatingCursor);
		while(validatingCursor.next()) {
			if (log.isDebugEnabled()) {
				StringBuilder validationData = new StringBuilder();
				validationData.append(validatingCursor.getCurrentIntervalEnd().getValue(Time.SECONDS)).append(", ");
				validationData.append("U=").append(util.execute()).append(", ");
				validationData.append("X=").append(tput.execute()).append(", ");
				validationData.append("T=").append(resp.execute()).append(", ");
				log.debug(validationData);
			}
			for (IValidator validator : validators) {
				validator.predict(demands);
			}
		}
		
		for (IValidator validator : validators) {
			if (log.isDebugEnabled()) {
				log.debug("Predicted " + validator.getClass().getName() + ":" + validator.getPredictedValues());
				log.debug("Observed " + validator.getClass().getName() + ":" + validator.getObservedValues());
			}
			estimates.setValidatedEntities(validator.getClass(), validator.getModelEntities());
			estimates.addValidationResults(validator.getClass(), validator.getPredictionError());
		}				
	}
	
	private static ResultTable initAndExecuteEstimation(IEstimationApproach approach,
			WorkloadDescription workload, int window, boolean iterative,
			IRepositoryCursor cursor, EstimationAlgorithmFactory algoFactory) throws InstantiationException,
			IllegalAccessException, InitializationException,
			EstimationException {
		
		if (approach != null) {
			
			approach.initialize(workload, cursor, algoFactory, window, iterative);
			approach.constructEstimationDefinitions();			
			approach.pruneEstimationDefinitions();
			return approach.executeEstimation();
		} else {
			log.error("Unkown estimation approach: " + approach);
			throw new IllegalArgumentException();
		}
	}
	
	public static void printSummary(List<ResultTable[]> results) {
		// Aggregate results
		StateVariable[] variables = null;
		List<String> approaches = new ArrayList<String>(results.size());
		Set<Class<? extends IValidator>> validators = new HashSet<Class<? extends IValidator>>();
		for (ResultTable[] folds : results) {
			boolean first = true;
			for (ResultTable curFold : folds) {
				if (first) {
					approaches.add(Registry.INSTANCE.getDisplayName(curFold.getApproach()));
					first = false;
				}
				if (variables == null) {
					variables = curFold.getStateVariables();
				} else {
					if(!Arrays.equals(variables, curFold.getStateVariables())) {
						throw new IllegalStateException();
					}
				}
				validators.addAll(curFold.getValidators());
			}
		}

		Map<Class<? extends IValidator>, MatrixBuilder> meanErrors = new HashMap<Class<? extends IValidator>, MatrixBuilder>();
		Map<Class<? extends IValidator>, List<ModelEntity>> validatedEntities = new HashMap<Class<? extends IValidator>, List<ModelEntity>>();
	
		MatrixBuilder meanEstimates = new MatrixBuilder(variables.length);
		for (ResultTable[] folds : results) {
			MatrixBuilder lastEstimates = new MatrixBuilder(variables.length);
			for (ResultTable curFold : folds) {
				lastEstimates.addRow(curFold.getLastEstimates());
			}
			meanEstimates.addRow(LinAlg.mean(lastEstimates.toMatrix(), 0));

			for (Class<? extends IValidator> validator : validators) {
				MatrixBuilder errors = null; 
				for (ResultTable curFold : folds) {
					Vector vec = curFold.getValidationErrors(validator);
					if (errors == null) {
						errors = new MatrixBuilder(vec.rows());
						validatedEntities.put(validator, curFold.getValidatedEntities(validator));
					}
					errors.addRow(vec);
				}
				
				Vector mean = LinAlg.mean(errors.toMatrix(), 0);
				if (!meanErrors.containsKey(validator)) {
					meanErrors.put(validator, new MatrixBuilder(mean.rows()));
				}
				meanErrors.get(validator).addRow(mean);
			}
			
		}
		
		// Estimates
		System.out.println("Estimates");
		printEstimatesTable(variables, approaches, meanEstimates.toMatrix());
		System.out.println();
		
		if (validators.size() > 0) {
			// Cross-Validation Results
			System.out.println("Cross-Validation Results:");
			
			for (Class<? extends IValidator> validator : validators) {
				String name = Registry.INSTANCE.getDisplayName(validator);
				System.out.println("\n" + name + ":");				
				printValidationResultsTable(validatedEntities.get(validator), approaches, meanErrors.get(validator).toMatrix());
			}
		}
	}
	
	private static void printValidationResultsTable(List<ModelEntity> entities, List<String> approaches, Matrix values) {
		System.out.printf("%-60.60s | ", "Approach");
		
		for (ModelEntity var : entities) {
			System.out.printf("%-8.8s", var.getName());
		}
		
		System.out.println("|");

		for (int i = 0; i < (64 + entities.size() * 8); i++) {
			System.out.print("-");
		}
		System.out.println();

		for (int r = 0; r < values.rows(); r++) {
			System.out.printf("%-60.60s |", approaches.get(r));
			Vector row = values.row(r);
			for (int i = 0; i < row.rows(); i++) {
				System.out.printf(" %.5f", row.get(i));
			}
			System.out.println(" |");
		}
	}
	
	private static void printEstimatesTable(StateVariable[] variables, List<String> approaches, Matrix values) {
		System.out.printf("%-60.60s | ", "Approach");
		Resource last = null;
		for (StateVariable var : variables) {
			if (var.getResource().equals(last)) {
				System.out.printf("%-8.8s", "");
			} else {
				System.out.printf("%-8.8s", var.getResource().getName());
			}
		}
		System.out.println();

		System.out.printf("%60.60s | ", "");
		for (StateVariable var : variables) {
			System.out.printf("%-8.8s", var.getService().getName());
		}
		System.out.println("|");

		for (int i = 0; i < (64 + variables.length * 8); i++) {
			System.out.print("-");
		}
		System.out.println();

		for (int r = 0; r < values.rows(); r++) {
			System.out.printf("%-60.60s |", approaches.get(r));
			Vector row = values.row(r);
			for (int i = 0; i < row.rows(); i++) {
				System.out.printf(" %.5f", row.get(i));
			}
			System.out.println(" |");
		}
	}
	
	public static void exportResults(LibredeConfiguration conf, List<ResultTable[]> results) {
		for (ExporterConfiguration exportConf :conf.getOutput().getExporters()) {
			IExporter exporter;
			try {
				Class<?> cl = Registry.INSTANCE.getInstanceClass(exportConf.getType());
				exporter = (IExporter) Instantiator.newInstance(cl, exportConf.getParameters());
			} catch (Exception e) {
				log.error("Could not instantiate exporter: " + exportConf.getName());
				continue;
			}
			for (ResultTable[] folds : results) {
				int i = 0;
				for (ResultTable curFold : folds) {
					try {
						exporter.writeResults(curFold.getApproach().getSimpleName(), i, curFold.getEstimates());
					} catch (Exception e) {
						log.error("Could not export results.", e);
					}
					i++;
				}
			}
		}
	}

}
