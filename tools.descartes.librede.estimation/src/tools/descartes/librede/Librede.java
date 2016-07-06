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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import tools.descartes.librede.algorithm.EstimationAlgorithmFactory;
import tools.descartes.librede.algorithm.IEstimationAlgorithm;
import tools.descartes.librede.algorithm.SimpleApproximation;
import tools.descartes.librede.approach.IEstimationApproach;
import tools.descartes.librede.approach.LiuOptimizationApproach;
import tools.descartes.librede.approach.MenasceOptimizationApproach;
import tools.descartes.librede.approach.ResponseTimeApproximationApproach;
import tools.descartes.librede.approach.RoliaRegressionApproach;
import tools.descartes.librede.approach.ServiceDemandLawApproach;
import tools.descartes.librede.approach.WangKalmanFilterApproach;
import tools.descartes.librede.approach.ZhangKalmanFilterApproach;
import tools.descartes.librede.configuration.ConfigurationPackage;
import tools.descartes.librede.configuration.EstimationApproachConfiguration;
import tools.descartes.librede.configuration.ExporterConfiguration;
import tools.descartes.librede.configuration.LibredeConfiguration;
import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.TraceConfiguration;
import tools.descartes.librede.configuration.ValidatorConfiguration;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.datasource.DataSourceSelector;
import tools.descartes.librede.datasource.IDataSource;
import tools.descartes.librede.datasource.TraceEvent;
import tools.descartes.librede.datasource.TraceKey;
import tools.descartes.librede.datasource.csv.CsvDataSource;
import tools.descartes.librede.datasource.memory.InMemoryDataSource;
import tools.descartes.librede.exceptions.EstimationException;
import tools.descartes.librede.exceptions.InitializationException;
import tools.descartes.librede.export.IExporter;
import tools.descartes.librede.export.csv.CsvExporter;
import tools.descartes.librede.linalg.LinAlg;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.MatrixBuilder;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.registry.Instantiator;
import tools.descartes.librede.registry.Registry;
import tools.descartes.librede.repository.CachingRepositoryCursor;
import tools.descartes.librede.repository.IMonitoringRepository;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.MemoryObservationRepository;
import tools.descartes.librede.repository.TimeSeries;
import tools.descartes.librede.repository.adapters.ArrivalRateAdapter;
import tools.descartes.librede.repository.adapters.ArrivalsAdapter;
import tools.descartes.librede.repository.adapters.BusyTimeAdapter;
import tools.descartes.librede.repository.adapters.ContentionAdapter;
import tools.descartes.librede.repository.adapters.DelayAdapter;
import tools.descartes.librede.repository.adapters.DeparturesAdapter;
import tools.descartes.librede.repository.adapters.IdleTimeAdapter;
import tools.descartes.librede.repository.adapters.QueueLengthSeenOnArrivalAdapter;
import tools.descartes.librede.repository.adapters.ResidenceTimeAdapter;
import tools.descartes.librede.repository.adapters.ResourceDemandAdapter;
import tools.descartes.librede.repository.adapters.ResponseTimeAdapter;
import tools.descartes.librede.repository.adapters.StealTimeAdapter;
import tools.descartes.librede.repository.adapters.ThroughputAdapter;
import tools.descartes.librede.repository.adapters.UtilizationAdapter;
import tools.descartes.librede.repository.adapters.VisitsAdapter;
import tools.descartes.librede.repository.exceptions.MonitoringRepositoryException;
import tools.descartes.librede.units.Dimension;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Ratio;
import tools.descartes.librede.units.RequestCount;
import tools.descartes.librede.units.RequestRate;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;
import tools.descartes.librede.units.UnitsPackage;
import tools.descartes.librede.validation.ContinuousCrossValidationCursor;
import tools.descartes.librede.validation.IValidator;
import tools.descartes.librede.validation.ResponseTimeValidator;
import tools.descartes.librede.validation.UtilizationValidator;

public class Librede {

	private static final Logger log = Logger.getLogger(Librede.class);
	private final static int selectionInterval = 1;

	public static void initLogging() {
		BasicConfigurator.configure();
	}

	public static void init() {
		Registry.INSTANCE.registerDimension(Time.INSTANCE);
		Registry.INSTANCE.registerDimension(RequestCount.INSTANCE);
		Registry.INSTANCE.registerDimension(RequestRate.INSTANCE);
		Registry.INSTANCE.registerDimension(Ratio.INSTANCE);

		Registry.INSTANCE.registerMetric(StandardMetrics.ARRIVAL_RATE, new ArrivalRateAdapter());
		Registry.INSTANCE.registerMetric(StandardMetrics.ARRIVALS, new ArrivalsAdapter());
		Registry.INSTANCE.registerMetric(StandardMetrics.BUSY_TIME, new BusyTimeAdapter());
		Registry.INSTANCE.registerMetric(StandardMetrics.CONTENTION, new ContentionAdapter());
		Registry.INSTANCE.registerMetric(StandardMetrics.DELAY, new DelayAdapter());
		Registry.INSTANCE.registerMetric(StandardMetrics.DEPARTURES, new DeparturesAdapter());
		Registry.INSTANCE.registerMetric(StandardMetrics.IDLE_TIME, new IdleTimeAdapter());
		Registry.INSTANCE.registerMetric(StandardMetrics.RESIDENCE_TIME, new ResidenceTimeAdapter());
		Registry.INSTANCE.registerMetric(StandardMetrics.RESPONSE_TIME, new ResponseTimeAdapter());
		Registry.INSTANCE.registerMetric(StandardMetrics.THROUGHPUT, new ThroughputAdapter());
		Registry.INSTANCE.registerMetric(StandardMetrics.QUEUE_LENGTH_SEEN_ON_ARRIVAL,
				new QueueLengthSeenOnArrivalAdapter());
		Registry.INSTANCE.registerMetric(StandardMetrics.VISITS, new VisitsAdapter());
		Registry.INSTANCE.registerMetric(StandardMetrics.UTILIZATION, new UtilizationAdapter());
		Registry.INSTANCE.registerMetric(StandardMetrics.RESOURCE_DEMAND, new ResourceDemandAdapter());
		Registry.INSTANCE.registerMetric(StandardMetrics.STEAL_TIME, new StealTimeAdapter());

		Registry.INSTANCE.registerImplementationType(IDataSource.class, CsvDataSource.class);
		Registry.INSTANCE.registerImplementationType(IDataSource.class, InMemoryDataSource.class);

		Registry.INSTANCE.registerImplementationType(IEstimationAlgorithm.class, SimpleApproximation.class);

		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class,
				ResponseTimeApproximationApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, ServiceDemandLawApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, RoliaRegressionApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, MenasceOptimizationApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, ZhangKalmanFilterApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, WangKalmanFilterApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, LiuOptimizationApproach.class);

		Registry.INSTANCE.registerImplementationType(IValidator.class, ResponseTimeValidator.class);
		Registry.INSTANCE.registerImplementationType(IValidator.class, UtilizationValidator.class);

		Registry.INSTANCE.registerImplementationType(IExporter.class, CsvExporter.class);
	}

	public static LibredeResults execute(LibredeConfiguration conf) {
		return execute(conf, Collections.<String, IDataSource> emptyMap());
	}

	public static LibredeResults execute(LibredeConfiguration conf, Map<String, IDataSource> existingDatasources) {

		LibredeVariables var = new LibredeVariables(conf);

		return executeContinuous(var, existingDatasources);
	}

	public static LibredeResults executeContinuous(LibredeVariables var, Map<String, IDataSource> existingDatasources) {		
		Quantity<Time> endTime = loadRepository(var.getConf(), var.getRepo(), existingDatasources);
		if (var.getConf().getEstimation().getEndTimestamp().compareTo(endTime) <= 0) {
			// do not progress further than the configured end timestamp.
			var.getRepo().setCurrentTime(var.getConf().getEstimation().getEndTimestamp());
		} else {		
			var.getRepo().setCurrentTime(endTime);
		}

		try {
			runEstimation(var);
		} catch (Exception e) {
			log.error("Error running estimation.", e);
		}
		
		// Now export the results.
		exportResults(var.getConf(), var.getResults());

		return var.getResults();
//		return null;
	}

	public static void initRepo(LibredeVariables var) {
		var.getRepo().setCurrentTime(var.getConf().getEstimation().getStartTimestamp());

		Quantity<Time> endTime = loadRepository(var.getConf(), var.getRepo(), Collections.<String, IDataSource> emptyMap());
		var.getRepo().setCurrentTime(endTime);
	}

	public static Quantity<Time> loadRepository(LibredeConfiguration conf, MemoryObservationRepository repo) {
		return loadRepository(conf, repo, Collections.<String, IDataSource> emptyMap());
	}

	public static Quantity<Time> loadRepository(LibredeConfiguration conf, IMonitoringRepository repo,
			Map<String, IDataSource> existingDataSources) {
		Map<String, IDataSource> dataSources = new HashMap<String, IDataSource>();
		Quantity<Time> endTime = repo.getCurrentTime();

		try (DataSourceSelector selector = new DataSourceSelector()) {
			log.info("Start loading monitoring data");
			for (TraceConfiguration trace : conf.getInput().getObservations()) {
				String dataSourceName = trace.getDataSource().getName();
				if (!dataSources.containsKey(dataSourceName)) {
					IDataSource newSource;
					if (existingDataSources.containsKey(dataSourceName)) {
						newSource = existingDataSources.get(dataSourceName);
					} else {
						Class<?> cl = Registry.INSTANCE.getInstanceClass(trace.getDataSource().getType());
						try {
							newSource = (IDataSource) Instantiator.newInstance(cl,
									trace.getDataSource().getParameters());
						} catch (Exception e) {
							log.error("Could not instantiate data source " + trace.getDataSource().getName(), e);
							continue;
						}
					}
					newSource.setName(dataSourceName);
					selector.add(newSource);
					dataSources.put(dataSourceName, newSource);
				}
				IDataSource source = dataSources.get(dataSourceName);

				try {
					source.addTrace(trace);
				} catch (IOException ex) {
					log.error("Error loading data.", ex);
				}
			}

			for (IDataSource ds : dataSources.values()) {
				ds.load();
			}

			Set<TraceKey> loadedTraces = new HashSet<TraceKey>();
			TraceEvent curEvent = null;
			while ((curEvent = selector.poll()) != null) {
				endTime = selector.getLatestObservationTime();
				
				TraceKey key = curEvent.getKey();
				loadedTraces.add(key);

				@SuppressWarnings("unchecked")
				Metric<Dimension> metric = (Metric<Dimension>) key.getMetric();
				@SuppressWarnings("unchecked")
				Unit<Dimension> unit = (Unit<Dimension>) key.getUnit();

				TimeSeries ts = curEvent.getData();

				//end timestamp is set in ts.append()
				ts.setStartTime(conf.getEstimation().getStartTimestamp().getValue(Time.SECONDS));
//				ts.setEndTime(conf.getEstimation().getEndTimestamp().getValue(Time.SECONDS));

				if (curEvent.getKey().getAggregation() != Aggregation.NONE) {
					repo.insert(metric, unit, key.getEntity(), ts, key.getAggregation(), key.getInterval());
				} else {
					repo.insert(metric, unit, key.getEntity(), ts);
				}
			}

			for (TraceKey t : loadedTraces) {
				TimeSeries ts = repo.select((Metric<Dimension>) t.getMetric(), (Unit<Dimension>) t.getUnit(),
						t.getEntity(), t.getAggregation());
				log.info("Loaded trace: " + t.getEntity() + "/" + t.getMetric() + "/" + t.getAggregation()
						+ " <- [length=" + ts.samples() + ", mean=" + LinAlg.nanmean(ts.getData()) + ", start="
						+ ts.getStartTime() + "s, end=" + ts.getEndTime() + "s]");
			}
			log.info("Successfully loaded monitoring data.");
			((MemoryObservationRepository) repo).logContentDump();
		} catch (IOException e1) {
			log.error("Error loading monitoring data.", e1);
		} finally {
			for (IDataSource ds : dataSources.values()) {
				try {
					ds.close();
				} catch (IOException e) {
					log.error("Error closing data source.", e);
				}
			}
		}
		return endTime;
		
	}

	public static LibredeResults runEstimation(LibredeVariables var) throws Exception {
		
		if (!var.getConf().getValidation().isValidateEstimates()) {
			var.updateResults(runEstimationInternal(var));
			printSummary(var.getResults());
		} else {
			if (var.getConf().getValidation().getValidationFolds() <= 1) {
				var.updateResults(runEstimationWithValidation(var));
			} else {
				var.updateResults(runEstimationWithCrossValidation(var));
			}
			printSummary(var.getResults());
		}
		
		if (var.getConf().getEstimation().isAutomaticApproachSelection() && var.getResults().getApproaches().size() > 0 ) {
			ApproachSelector.selectApproach(var);
			if (var.getRunNr() >= selectionInterval) {
				var.resetRunNr();
			}
			var.incrementRunNr();
		}
		
		return var.getResults();
		
	}
	
	private static LibredeResults runEstimationInternal(LibredeVariables var) throws Exception {
		for (EstimationApproachConfiguration currentConf : var.getConf().getEstimation().getApproaches()) {

			IEstimationApproach currentApproach;

			try {
				Class<?> cl = Registry.INSTANCE.getInstanceClass(currentConf.getType());
				currentApproach = (IEstimationApproach) Instantiator.newInstance(cl, currentConf.getParameters());
			} catch (Exception ex) {
				log.error("Error instantiating estimation approach: " + currentConf.getType(), ex);
				continue;
			}
			String approachName = Registry.INSTANCE.getDisplayName(currentApproach.getClass());
			log.info("Run estimation approach " + approachName);

			ResultTable estimates = initAndExecuteEstimation(currentApproach, var.getRepo().getWorkload(),
					var.getConf().getEstimation().getWindow(), var.getConf().getEstimation().isRecursive(),
					var.getCursor(currentConf.getType()), var.getAlgoFactory());
			var.getResults().addEstimates(currentApproach.getClass(), 0, estimates);

		}
		return var.getResults();
	}

	public static LibredeResults runEstimationWithValidation(LibredeVariables var) throws Exception {

		for (EstimationApproachConfiguration currentConf : var.getConf().getEstimation().getApproaches()) {

			IEstimationApproach currentApproach;
			try {
				Class<?> cl = Registry.INSTANCE.getInstanceClass(currentConf.getType());
				currentApproach = (IEstimationApproach) Instantiator.newInstance(cl, currentConf.getParameters());
			} catch (Exception ex) {
				log.error("Error instantiating estimation approach: " + currentConf.getType(), ex);
				continue;
			}
			String approachName = Registry.INSTANCE.getDisplayName(currentApproach.getClass());
			log.info("Run estimation approach " + approachName);

			ResultTable estimates = initAndExecuteEstimation(currentApproach, var.getRepo().getWorkload(),
					var.getConf().getEstimation().getWindow(), var.getConf().getEstimation().isRecursive(),
					new CachingRepositoryCursor(var.getCursor(currentConf.getType()),
							var.getConf().getEstimation().getWindow()),
					var.getAlgoFactory());
			if (estimates.getEstimates().isEmpty()) {
				break;
			}
			Vector state = estimates.getLastEstimates();

			IRepositoryCursor validatingCursor = var.getRepo().getCursor(
					var.getConf().getEstimation().getStartTimestamp(), var.getConf().getEstimation().getStepSize());

			List<IValidator> validators = initValidators(var.getConf(), validatingCursor);

			runValidation(var.getConf(), validators, validatingCursor, state, estimates);

			var.getResults().addEstimates(currentApproach.getClass(), 0, estimates);

		}
		return var.getResults();
	}

	public static LibredeResults runEstimationWithCrossValidation(LibredeVariables var) throws Exception {
		for (EstimationApproachConfiguration currentConf : var.getConf().getEstimation().getApproaches()) {
			IEstimationApproach currentApproach;
			try {
				Class<?> cl = Registry.INSTANCE.getInstanceClass(currentConf.getType());
				currentApproach = (IEstimationApproach) Instantiator.newInstance(cl, currentConf.getParameters());
			} catch (Exception ex) {
				log.error("Error instantiating estimation approach: " + currentConf.getType(), ex);
				continue;
			}
			String approachName = Registry.INSTANCE.getDisplayName(currentApproach.getClass());
			log.info("Run estimation approach " + approachName);

			List<IValidator> validators = initValidators(var.getConf(),
					(ContinuousCrossValidationCursor) var.getCursor(currentConf.getType()));


			for (int i = 0; i < var.getConf().getValidation().getValidationFolds(); i++) {
				log.info("Start repetition " + (i + 1));
				((ContinuousCrossValidationCursor) var.getCursor(currentConf.getType())).startTrainingPhase(i);

				ResultTable estimates = initAndExecuteEstimation(currentApproach, var.getRepo().getWorkload(),
						var.getConf().getEstimation().getWindow(), var.getConf().getEstimation().isRecursive(),
						new CachingRepositoryCursor((ContinuousCrossValidationCursor) var.getCursor(currentConf.getType()),
								var.getConf().getEstimation().getWindow()),
						var.getAlgoFactory());
				if (estimates.getEstimates().isEmpty()) {
					break;
				}
				Vector state = estimates.getLastEstimates();

				((ContinuousCrossValidationCursor) var.getCursor(currentConf.getType())).startValidationPhase(i);

				runValidation(var.getConf(), validators, (ContinuousCrossValidationCursor) var.getCursor(currentConf.getType()),
						state, estimates);

				var.getResults().addEstimates(currentApproach.getClass(), i, estimates);

			}
		}
		return var.getResults();
	}

	private static List<IValidator> initValidators(LibredeConfiguration conf, IRepositoryCursor validatingCursor)
			throws InstantiationException, IllegalAccessException {
		List<IValidator> validators = new ArrayList<IValidator>(conf.getValidation().getValidators().size());
		for (ValidatorConfiguration validator : conf.getValidation().getValidators()) {
			Class<?> cl = Registry.INSTANCE.getInstanceClass(validator.getType());
			IValidator val = (IValidator) Instantiator.newInstance(cl, validator.getParameters());
			val.initialize(conf.getWorkloadDescription(), validatingCursor);
			validators.add(val);
		}
		return validators;
	}

	private static void runValidation(LibredeConfiguration conf, List<IValidator> validators,
			IRepositoryCursor validatingCursor, Vector demands, ResultTable estimates) {
		List<Service> sortedServices = new ArrayList<>(validatingCursor.getRepository().listServices());
		Collections.sort(sortedServices, new Comparator<Service>() {
			@Override
			public int compare(Service o1, Service o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		List<Resource> sortedResources = new ArrayList<>(validatingCursor.getRepository().listResources());
		Collections.sort(sortedResources, new Comparator<Resource>() {
			@Override
			public int compare(Resource o1, Resource o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		// TODO: Implement Exporter for this.
		// Query<Vector, Ratio> util =
		// QueryBuilder.select(StandardMetrics.UTILIZATION).in(Ratio.NONE).forResources(sortedResources).average().using(validatingCursor);
		// Query<Vector, RequestRate> tput =
		// QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forServices(sortedServices).average().using(validatingCursor);
		// Query<Vector, Time> resp =
		// QueryBuilder.select(StandardMetrics.RESPONSE_TIME).in(Time.SECONDS).forServices(sortedServices).average().using(validatingCursor);
		// File output = new
		// File("C:\\Users\\Simon\\Workspaces\\specjent-model\\specjent-model2\\data.csv");
		// try (PrintStream out = new PrintStream(output)) {
		// out.print("#");
		// for (Resource r : sortedResources) {
		// out.print(", ");
		// out.print(r.getName());
		// }
		// for (int i = 0; i < 2; i++) {
		// for (Service s : sortedServices) {
		// out.print(", ");
		// out.print(s.getName());
		// }
		// }
		// out.println();
		//
		log.info("Run validation...");
		String[] validatorNames = new String[validators.size()];
		for (int i = 0; i < validatorNames.length; i++) {
			validatorNames[i] = Registry.INSTANCE.getDisplayName(validators.get(i).getClass());
		}
		Set<IValidator> disabledValidators = new HashSet<IValidator>();
		while (validatingCursor.next()) {
			//
			// if (log.isDebugEnabled()) {
			// StringBuilder validationData = new StringBuilder();
			// validationData.append(validatingCursor.getIntervalEnd(validatingCursor.getLastInterval()).getValue(Time.SECONDS)).append(",
			// ");
			// validationData.append("U=").append(util.execute()).append(", ");
			// validationData.append("X=").append(tput.execute()).append(", ");
			// validationData.append("T=").append(resp.execute()).append(", ");
			// log.debug(validationData);
			// }
			for (int i = 0; i < validatorNames.length; i++) {
				IValidator current = validators.get(i);
				try {
					current.predict(demands);
					disabledValidators.remove(current);
				} catch (MonitoringRepositoryException ex) {
					if (!disabledValidators.contains(current)) {
						log.warn("Failed to execute validator " + validatorNames[i] + ": " + ex.getMessage());
						disabledValidators.add(current);
					}
				}
			}
		}
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }

		for (IValidator validator : validators) {
			String validatorName = Registry.INSTANCE.getDisplayName(validator.getClass());
			// if (log.isDebugEnabled()) {
			log.info("Predicted " + validatorName + ":" + validator.getPredictedValues());
			log.info("Observed " + validatorName + ":" + validator.getObservedValues());
			// }

			estimates.setValidatedEntities(validator.getClass(), validator.getModelEntities());
			estimates.addValidationResults(validator.getClass(), validator.getPredictedValues(),
					validator.getPredictionError());
		}
	}

	private static ResultTable initAndExecuteEstimation(IEstimationApproach approach, WorkloadDescription workload,
			int window, boolean iterative, IRepositoryCursor cursor, EstimationAlgorithmFactory algoFactory)
					throws InstantiationException, IllegalAccessException, InitializationException,
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

	public static void printSummary(LibredeResults results) {
		// Aggregate results
		ResourceDemand[] variables = null;
		List<Class<? extends IEstimationApproach>> approaches = new ArrayList<>(results.getApproaches());

		Set<Class<? extends IValidator>> validators = new HashSet<Class<? extends IValidator>>();
		for (Class<? extends IEstimationApproach> approach : approaches) {
			for (int i = 0; i < results.getNumberOfFolds(); i++) {
				ResultTable curFold = results.getEstimates(approach, i);
				if (variables == null) {
					variables = curFold.getStateVariables();
				} else {
					if (!Arrays.equals(variables, curFold.getStateVariables())) {
						throw new IllegalStateException();
					}
				}
				validators.addAll(curFold.getValidators());
			}
		}

		// Print approaches legend
		System.out.println("Approaches");
		System.out.println("==========");
		for (int i = 0; i < approaches.size(); i++) {
			System.out.printf("[%d] %s\n", i + 1, Registry.INSTANCE.getDisplayName(approaches.get(i)));
		}
		System.out.println();

		Map<Class<? extends IValidator>, MatrixBuilder> meanErrors = new HashMap<Class<? extends IValidator>, MatrixBuilder>();
		Map<Class<? extends IValidator>, MatrixBuilder> meanPredictions = new HashMap<Class<? extends IValidator>, MatrixBuilder>();
		Map<Class<? extends IValidator>, List<ModelEntity>> validatedEntities = new HashMap<Class<? extends IValidator>, List<ModelEntity>>();
		
		if (variables != null) {
			MatrixBuilder meanEstimates = MatrixBuilder.create(variables.length);
			for (Class<? extends IEstimationApproach> approach : approaches) {
				MatrixBuilder lastEstimates = MatrixBuilder.create(variables.length);
				for (int i = 0; i < results.getNumberOfFolds(); i++) {
					ResultTable curFold = results.getEstimates(approach, i);
					lastEstimates.addRow(curFold.getLastEstimates());
				}
				Matrix lastEstimatesMatrix = lastEstimates.toMatrix();
				if (lastEstimatesMatrix.isEmpty()) {
					log.warn("No estimates found for approach " + Registry.INSTANCE.getDisplayName(approach));
				} else {
					meanEstimates.addRow(LinAlg.mean(lastEstimatesMatrix));
				}
	
				for (Class<? extends IValidator> validator : validators) {
					MatrixBuilder errorsBuilder = null;
					MatrixBuilder predictionsBuilder = null;
					for (int i = 0; i < results.getNumberOfFolds(); i++) {
						ResultTable curFold = results.getEstimates(approach, i);
						Vector curErr = curFold.getValidationErrors(validator);
						if (errorsBuilder == null) {
							errorsBuilder = MatrixBuilder.create(curErr.rows());
							validatedEntities.put(validator, curFold.getValidatedEntities(validator));
						}
						errorsBuilder.addRow(curErr);
						Vector curPred = curFold.getValidationPredictions(validator);
						if (predictionsBuilder == null) {
							predictionsBuilder = MatrixBuilder.create(curPred.rows());
						}
						predictionsBuilder.addRow(curPred);
					}
	
					Matrix errors = errorsBuilder.toMatrix();
					Matrix predictions = predictionsBuilder.toMatrix();
					if (!errors.isEmpty() && !predictions.isEmpty()) {
						Vector curMeanErr = LinAlg.mean(errors);
						Vector curMeanPred = LinAlg.mean(predictions);
						if (!meanErrors.containsKey(validator)) {
							meanErrors.put(validator, MatrixBuilder.create(curMeanErr.rows()));
							meanPredictions.put(validator, MatrixBuilder.create(curMeanPred.rows()));
						}
						meanErrors.get(validator).addRow(curMeanErr);
						meanPredictions.get(validator).addRow(curMeanPred);
					}
				}
	
			}


			// Estimates
			System.out.println("Estimates");
			System.out.println("=========");
			printEstimatesTable(variables, approaches, meanEstimates.toMatrix());
			System.out.println();


			if (validators.size() > 0) {
				// Cross-Validation Results
				System.out.println("Cross-Validation Results:");
				System.out.println("=========================");
	
				for (Class<? extends IValidator> validator : validators) {
					String name = Registry.INSTANCE.getDisplayName(validator);
					System.out.println(name + ":");
					if (meanErrors.containsKey(validator)) {
	
						Matrix errors = meanErrors.get(validator).toMatrix();
						Matrix predictions = meanPredictions.get(validator).toMatrix();
						printValidationResultsTable(validatedEntities.get(validator), approaches, predictions, errors);
						System.out.println();
					} else {
						System.out.println("No results.");
					}
				}
			}
		}
	}

	private static void printValidationResultsTable(List<ModelEntity> entities,
			List<Class<? extends IEstimationApproach>> approaches, Matrix predictions, Matrix errors) {
		System.out.printf("%-80.80s | ", "Resource or service");
		for (int i = 0; i < approaches.size(); i++) {
			System.out.printf("%-9.9s", "[" + (i + 1) + "]");
		}
		System.out.println("|");

		for (int i = 0; i < (87 + approaches.size() * 9); i++) {
			System.out.print("-");
		}
		System.out.println();

		int idx = 0;
		for (ModelEntity entity : entities) {
			System.out.printf("%-80.80s | ", limitOutput(entity.getName(), 80));
			for (int i = 0; i < approaches.size(); i++) {
				System.out.printf("%.5e %.5f%% ", predictions.get(i, idx), errors.get(i, idx) * 100);
			}
			System.out.println("|");
			idx++;
		}
	}

	private static void printEstimatesTable(ResourceDemand[] variables,
			List<Class<? extends IEstimationApproach>> approaches, Matrix values) {
		System.out.printf("%-20.20s | ", "Resource");
		System.out.printf("%-60.60s | ", "Service");
		for (int i = 0; i < approaches.size(); i++) {
			System.out.printf("%-9.9s", "[" + (i + 1) + "]");
		}
		System.out.println("|");

		for (int i = 0; i < (87 + approaches.size() * 9); i++) {
			System.out.print("-");
		}
		System.out.println();
		Resource last = null;
		int idx = 0;
		for (ResourceDemand var : variables) {
			if (var.getResource().equals(last)) {
				System.out.printf("%-20.20s | ", "");
			} else {
				System.out.printf("%-20.20s | ", limitOutput(var.getResource().getName(), 20));
			}
			System.out.printf("%-60.60s | ", limitOutput(var.getService().getName(), 60));
			for (int i = 0; i < approaches.size(); i++) {
				System.out.printf("%.5fs ", values.get(i, idx));
			}
			System.out.println("|");
			last = var.getResource();
			idx++;
		}
	}

	private static String limitOutput(String output, int max) {
		if (output.length() > max) {
			int third = (max - 5) / 3;
			return output.substring(0, max - third - 5) + " ... " + output.substring(output.length() - third);
		} else {
			return output;
		}
	}

	public static void exportResults(LibredeConfiguration conf, LibredeResults results) {
		for (ExporterConfiguration exportConf : conf.getOutput().getExporters()) {
			IExporter exporter;
			try {
				Class<?> cl = Registry.INSTANCE.getInstanceClass(exportConf.getType());
				exporter = (IExporter) Instantiator.newInstance(cl, exportConf.getParameters());
			} catch (Exception e) {
				log.error("Could not instantiate exporter: " + exportConf.getName());
				continue;
			}
			String exporterName = Registry.INSTANCE.getDisplayName(exporter.getClass());
			log.info("Run exporter " + exporterName);
			try {
				exporter.writeResults(results);
			} catch (Exception e) {
				log.error("Error running exporter " + exporterName, e);
			}
		}
	}

	private static void checkWorkloadDescription(WorkloadDescription workload) {
		for (Service curService : workload.getServices()) {
			if (curService.getAccessedResources().isEmpty()) {
				log.warn("Service " + curService.getName()
						+ " does not access any resource. Please check that resource demands are defined for this service.");
			}
		}
	}
	
	public static LibredeConfiguration loadConfiguration(Path path) {
		ResourceSet resourceSet = Registry.INSTANCE.createResourceSet();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
				"librede", new XMIResourceFactoryImpl());
		File configFile = new File(path.toString());
		URI fileURI = URI.createFileURI(configFile.getAbsolutePath());
		ConfigurationPackage confPackage = ConfigurationPackage.eINSTANCE;
		UnitsPackage unitsPackage = UnitsPackage.eINSTANCE;
		org.eclipse.emf.ecore.resource.Resource resource = resourceSet.getResource(fileURI, true);
		EcoreUtil.resolveAll(resource);
		return (LibredeConfiguration) resource.getContents().get(0);
	}

}
