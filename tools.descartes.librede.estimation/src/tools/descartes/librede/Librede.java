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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
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
import tools.descartes.librede.approach.KumarKalmanFilterApproach;
import tools.descartes.librede.approach.LiuOptimizationApproach;
import tools.descartes.librede.approach.MenasceOptimizationApproach;
import tools.descartes.librede.approach.ResponseTimeApproximationApproach;
import tools.descartes.librede.approach.ResponseTimeRegressionApproach;
import tools.descartes.librede.approach.ServiceDemandLawApproach;
import tools.descartes.librede.approach.UtilizationRegressionApproach;
import tools.descartes.librede.approach.WangKalmanFilterApproach;
import tools.descartes.librede.configuration.EstimationApproachConfiguration;
import tools.descartes.librede.configuration.LibredeConfiguration;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.TraceConfiguration;
import tools.descartes.librede.configuration.ValidatorConfiguration;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.datasource.DataSourceSelector;
import tools.descartes.librede.datasource.IDataSource;
import tools.descartes.librede.datasource.TraceEvent;
import tools.descartes.librede.datasource.TraceKey;
import tools.descartes.librede.datasource.csv.CsvDataSource;
import tools.descartes.librede.datasource.csv.CsvDataSourceOffline;
import tools.descartes.librede.datasource.kieker.KiekerDataSource;
import tools.descartes.librede.datasource.kiekeramqp.KiekerAmqpDataSource;
import tools.descartes.librede.datasource.kiekeramqp.KiekerDataSourceOffline;
import tools.descartes.librede.datasource.memory.InMemoryDataSource;
import tools.descartes.librede.exceptions.EstimationException;
import tools.descartes.librede.exceptions.InitializationException;
import tools.descartes.librede.export.IExporter;
import tools.descartes.librede.export.csv.CsvExporter;
import tools.descartes.librede.linalg.LinAlg;
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
import tools.descartes.librede.units.UnitsFactory;
import tools.descartes.librede.validation.AbsoluteUtilizationValidator;
import tools.descartes.librede.validation.ContinuousCrossValidationCursor;
import tools.descartes.librede.validation.IValidator;
import tools.descartes.librede.validation.ResponseTimeValidator;
import tools.descartes.librede.validation.UtilizationValidator;
import tools.descartes.librede.validation.WeightedResponseTimeValidator;

/**
 * This is the main entry point of Librede. Used to start various kinds of
 * estimations.
 * 
 * @author Simon Spinner, Johannes Grohmann, Torsten Krauss
 *
 */
public class Librede {

	private static final Logger log = Logger.getLogger(Librede.class);
	private final static int selectionInterval = 1;
	private static final Quantity<Time> ONE_FOLD_STEPSIZE = UnitsFactory.eINSTANCE.createQuantity();

	/**
	 * Initialize logging.
	 */
	public static void initLogging() {
		BasicConfigurator.configure();
	}

	/**
	 * Registers all options at the registry.
	 */
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
		Registry.INSTANCE.registerImplementationType(IDataSource.class, CsvDataSourceOffline.class);
		Registry.INSTANCE.registerImplementationType(IDataSource.class, KiekerDataSource.class);
		Registry.INSTANCE.registerImplementationType(IDataSource.class, KiekerAmqpDataSource.class);
		Registry.INSTANCE.registerImplementationType(IDataSource.class, KiekerDataSourceOffline.class);

		Registry.INSTANCE.registerImplementationType(IDataSource.class, InMemoryDataSource.class);

		Registry.INSTANCE.registerImplementationType(IEstimationAlgorithm.class, SimpleApproximation.class);

		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class,
				ResponseTimeApproximationApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, ServiceDemandLawApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, UtilizationRegressionApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, MenasceOptimizationApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, KumarKalmanFilterApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, WangKalmanFilterApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, LiuOptimizationApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, ResponseTimeRegressionApproach.class);

		Registry.INSTANCE.registerImplementationType(IValidator.class, ResponseTimeValidator.class);
		Registry.INSTANCE.registerImplementationType(IValidator.class, UtilizationValidator.class);
		Registry.INSTANCE.registerImplementationType(IValidator.class, AbsoluteUtilizationValidator.class);
		Registry.INSTANCE.registerImplementationType(IValidator.class, WeightedResponseTimeValidator.class);

		Registry.INSTANCE.registerImplementationType(IExporter.class, CsvExporter.class);

		ONE_FOLD_STEPSIZE.setValue(60);
		ONE_FOLD_STEPSIZE.setUnit(Time.SECONDS);
	}

	/**
	 * Executes the given {@link LibredeConfiguration}. This is the main entry
	 * point for execution.
	 * 
	 * @param conf
	 *            The configuration to execute.
	 * @return A {@link LibredeResults} objects containing the results of the
	 *         estimation.
	 */
	public static LibredeResults execute(LibredeConfiguration conf) {
		return execute(conf, Collections.<String, IDataSource>emptyMap());
	}

	/**
	 * Executes the given {@link LibredeConfiguration}.
	 * 
	 * @param conf
	 *            The configuration to execute.
	 * @return A {@link LibredeResults} objects containing the results of the
	 *         estimation.
	 */
	public static LibredeResults execute(LibredeConfiguration conf, Map<String, IDataSource> existingDatasources) {

		LibredeVariables var = new LibredeVariables(conf);

		return executeContinuous(var, existingDatasources);
	}

	/**
	 * Initializes and starts a continuous execution using continuous online
	 * data sources. It will terminate after the end timestamp in the configured
	 * {@link LibredeVariables} is reached.
	 * 
	 * @param var
	 *            The Librede variables.
	 * @param existingDatasources
	 *            The datasources to use.
	 * @return A {@link LibredeResults} objects containing the results of the
	 *         estimation.
	 */
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
		ResultPrinter.exportResults(var.getConf(), var.getResults());

		return var.getResults();
		// return null;
	}

	/**
	 * Initializes the repository of the given {@link LibredeVariables} to use
	 * it later on.
	 * 
	 * @param var
	 *            The variables to initialize.
	 */
	public static void initRepo(LibredeVariables var) {
		var.getRepo().setCurrentTime(var.getConf().getEstimation().getStartTimestamp());

		Quantity<Time> endTime = loadRepository(var.getConf(), var.getRepo(),
				Collections.<String, IDataSource>emptyMap());
		var.getRepo().setCurrentTime(endTime);
	}

	/**
	 * Loads the repository data.
	 * 
	 * @param conf
	 *            The Librede Configuration to load.
	 * @param repo
	 *            The monitoring repository to load the data into.
	 * @param existingDataSources
	 *            The datasources to load from.
	 * @return The latest observation time found in the data sources.
	 */
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

				// end timestamp is set in ts.append()
				ts.setStartTime(conf.getEstimation().getStartTimestamp().getValue(Time.SECONDS));
				// ts.setEndTime(conf.getEstimation().getEndTimestamp().getValue(Time.SECONDS));

				if (curEvent.getKey().getAggregation() != Aggregation.NONE) {
					repo.insert(metric, unit, key.getEntity(), ts, key.getAggregation(), key.getInterval());
				} else {
					repo.insert(metric, unit, key.getEntity(), ts);
				}
			}

			for (TraceKey t : loadedTraces) {
				@SuppressWarnings("unchecked")
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

	/**
	 * Execute one estimation run using the given {@link LibredeVariables}. The
	 * results are additionally printed to the console. If the validation folds
	 * ar less or equal than one,
	 * 
	 * @param var
	 *            The variables to execute.
	 * @return A results object containing all estimation data.
	 * @throws Exception
	 *             If an exception occurred during estimation.
	 */
	public static LibredeResults runEstimation(LibredeVariables var) throws Exception {

		if (!var.getConf().getValidation().isValidateEstimates()) {
			var.updateResults(runEstimationInternal(var));
			ResultPrinter.printSummary(var.getResults());
		} else {
			if (var.getConf().getValidation().getValidationFolds() <= 1) {
				var.updateResults(runEstimationWithValidation(var));
			} else {
				var.updateResults(runEstimationWithCrossValidation(var));
			}
			ResultPrinter.printSummary(var.getResults());
		}

		if (var.getConf().getEstimation().isAutomaticApproachSelection()
				&& var.getResults().getApproaches().size() > 0) {
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

	/**
	 * This executes an estimation and validates it on the estimation data
	 * itself.
	 * 
	 * @param var
	 *            The variables to execute and estimate.
	 * @return A results object containing all estimation data.
	 * @throws Exception
	 *             If an exception occurred during estimation.
	 */
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

			// Attention: one-fold estimation always gets fixed step size
			IRepositoryCursor validatingCursor = var.getRepo()
					.getCursor(var.getConf().getEstimation().getStartTimestamp(), ONE_FOLD_STEPSIZE);
			// uncomment this for normal behavior
			// IRepositoryCursor validatingCursor = var.getRepo().getCursor(
			// var.getConf().getEstimation().getStartTimestamp(),
			// var.getConf().getEstimation().getStepSize());

			List<IValidator> validators = initValidators(var.getConf(), validatingCursor);

			runValidation(var.getConf(), validators, validatingCursor, state, estimates);

			var.getResults().addEstimates(currentApproach.getClass(), 0, estimates);

		}
		return var.getResults();
	}

	/**
	 * Executes the estimation validating with cross-validation.
	 * 
	 * @param var
	 *            The variables to execute and estimate.
	 * @return A results object containing all estimation data.
	 * @throws Exception
	 *             If an exception occurred during estimation.
	 */
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
						new CachingRepositoryCursor(
								(ContinuousCrossValidationCursor) var.getCursor(currentConf.getType()),
								var.getConf().getEstimation().getWindow()),
						var.getAlgoFactory());
				if (estimates.getEstimates().isEmpty()) {
					break;
				}
				Vector state = estimates.getLastEstimates();

				((ContinuousCrossValidationCursor) var.getCursor(currentConf.getType())).startValidationPhase(i);

				runValidation(var.getConf(), validators,
						(ContinuousCrossValidationCursor) var.getCursor(currentConf.getType()), state, estimates);

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
						ex.printStackTrace();
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
			throws InstantiationException, IllegalAccessException, InitializationException, EstimationException {

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

	/**
	 * Loads a Librede-file from the given {@link Path}.
	 * 
	 * @param path
	 *            The path to the file to load. (File-ending should be
	 *            ".librede")
	 * @return The loaded {@link LibredeConfiguration}.
	 */
	public static LibredeConfiguration loadConfiguration(Path path) {
		ResourceSet resourceSet = Registry.INSTANCE.createResourceSet();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("librede",
				new XMIResourceFactoryImpl());
		File configFile = new File(path.toString());
		URI fileURI = URI.createFileURI(configFile.getAbsolutePath());
		org.eclipse.emf.ecore.resource.Resource resource = resourceSet.getResource(fileURI, true);
		EcoreUtil.resolveAll(resource);
		return (LibredeConfiguration) resource.getContents().get(0);
	}

}
