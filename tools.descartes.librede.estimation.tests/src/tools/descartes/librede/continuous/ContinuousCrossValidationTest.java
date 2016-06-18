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
package tools.descartes.librede.continuous;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import tools.descartes.librede.Librede;
import tools.descartes.librede.LibredeResults;
import tools.descartes.librede.LibredeVariables;
import tools.descartes.librede.ResultTable;
import tools.descartes.librede.approach.IEstimationApproach;
import tools.descartes.librede.bayesplusplus.BayesLibrary;
import tools.descartes.librede.configuration.DataSourceConfiguration;
import tools.descartes.librede.configuration.ExporterConfiguration;
import tools.descartes.librede.configuration.FileTraceConfiguration;
import tools.descartes.librede.configuration.LibredeConfiguration;
import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Parameter;
import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.configuration.TraceConfiguration;
import tools.descartes.librede.datasource.IDataSource;
import tools.descartes.librede.ipopt.java.IpoptLibrary;
import tools.descartes.librede.linalg.LinAlg;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.MatrixBuilder;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.nnls.NNLSLibrary;
import tools.descartes.librede.registry.Instantiator;
import tools.descartes.librede.registry.Registry;
import tools.descartes.librede.testutils.LibredeTest;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.UnitsFactory;
import tools.descartes.librede.validation.IValidator;

public class ContinuousCrossValidationTest extends LibredeTest {

	private static int WRITE_RATE = 100;
	private static int LINES_TO_COPY = 5;
	private static String SOURCE_PATH = null;
	private static String DESTINATION_PATH = null;
	private static LibredeConfiguration conf;

	@BeforeClass
	public static void initLibraries() {
		File file = new File(""); // Dummy file
		SOURCE_PATH = file.getAbsolutePath() + "\\resources\\source";
		DESTINATION_PATH = file.getAbsolutePath() + "\\resources\\destination";
		IpoptLibrary.init();
		NNLSLibrary.init();
		BayesLibrary.init();
//		LogManager.getRootLogger().setLevel(Level.WARN);

		// load LibredeConf to test the execute method

		try {
			ResourceSet resourceSet = Registry.INSTANCE.createResourceSet();
			Resource resource = resourceSet.createResource(URI.createURI("estimation2.librede"));
			InputStream confStream = ContinuousCrossValidationTest.class.getResourceAsStream("estimation2.librede");
			resource.load(confStream, new HashMap<Object, Object>());
			EcoreUtil.resolveAll(resource);
			conf = (LibredeConfiguration) resource.getContents().get(0);

			// set first to source path
			for (TraceConfiguration trace : conf.getInput().getObservations()) {
				FileTraceConfiguration filetrace = (FileTraceConfiguration) trace;
				filetrace.setFile(SOURCE_PATH + "\\" + new File(filetrace.getFile()).getName());
			}
			// read data
			handleReadFromTrace();

			// now set to destination path
			for (TraceConfiguration trace : conf.getInput().getObservations()) {
				FileTraceConfiguration filetrace = (FileTraceConfiguration) trace;
				filetrace.setFile(DESTINATION_PATH + "\\" + new File(filetrace.getFile()).getName());
			}
			// output also in destination path
			for (ExporterConfiguration export : conf.getOutput().getExporters()) {
				for (Parameter param : export.getParameters()) {
					if (param.getName().equals("OutputDirectory")) {
						param.setValue(DESTINATION_PATH);
					}
				}
			}
			// configure validation
			conf.getValidation().setValidationFolds(5);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@Test
	@Ignore
	public void test() {

		LinkedList<DataReadWrite> drws = new LinkedList<DataReadWrite>();
		File sourceDirectory = new File(SOURCE_PATH);
		File destinationDirectory = new File(DESTINATION_PATH);
		// receive all files to be read for analysis
		for (File file : sourceDirectory.listFiles()) {
			File sourceFile = new File(file.getPath());
			File destinationFile = new File(destinationDirectory.getPath() + File.separator + file.getName());
			DataReadWrite drw = new DataReadWrite(sourceFile, destinationFile);
			drw.setLinesToCopy(LINES_TO_COPY);
			drws.add(drw);
		}

		// initialize ThreadPool and add all Threads
		ScheduledThreadPoolExecutor tp = new ScheduledThreadPoolExecutor(10);
		for (DataReadWrite drw : drws) {
			tp.scheduleAtFixedRate(drw, 0, WRITE_RATE, TimeUnit.MILLISECONDS);
		}
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
		}

		while (!(tp.isShutdown())) {
			boolean runningTasks = false;
			for (DataReadWrite drw : drws) {
				if (!drw.getDataReader().isFileEnd()) {
					runningTasks = true;
				}
			}
			tp.schedule(new Executor(new LibredeVariables(conf), Collections.<String, IDataSource>emptyMap()), 0,
					TimeUnit.MILLISECONDS);
			if (!runningTasks) {
				tp.shutdown();
			}

			try {
				Thread.sleep(40000);
			} catch (InterruptedException e) {
				System.err.print("Error sleeping thread.");
			}

			System.err.println("Test running");

		}

		LibredeVariables var = new LibredeVariables(conf);
		tp = new ScheduledThreadPoolExecutor(10);
		Executor ex = new Executor(var, Collections.<String, IDataSource>emptyMap());
		tp.schedule(ex, 0, TimeUnit.MILLISECONDS);
		try {
			tp.awaitTermination(100000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		checkLibredeResults(ex.getResults());

	}

	private class Executor implements Runnable {

		private LibredeResults results;

		private LibredeVariables var;

		private Map<String, IDataSource> existingDatasources;

		public Executor(LibredeVariables var, Map<String, IDataSource> existingDatasources) {
			super();
			this.var = var;
			this.existingDatasources = existingDatasources;
			Librede.initRepo(var);
		}

		/**
		 * @return the results
		 */
		public LibredeResults getResults() {
			if (results == null) {
				System.out.println("No Results yet...");
				return var.getResults();
			}
			return results;
		}

		@Override
		public void run() {
			results = Librede.executeContinuous(var, existingDatasources);
		}

	}

	private static void checkLibredeResults(LibredeResults result) {
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getApproaches());

		Assert.assertEquals(5, result.getNumberOfFolds());
		for (Class<? extends IEstimationApproach> approach : result.getApproaches()) {
			for (int fold = 0; fold < result.getNumberOfFolds(); fold++) {
				Assert.assertNotNull(result.getEstimates(approach, fold));
			}
		}

		// Aggregate results
		ResourceDemand[] variables = null;
		List<Class<? extends IEstimationApproach>> approaches = new ArrayList<>(result.getApproaches());

		Set<Class<? extends IValidator>> validators = new HashSet<Class<? extends IValidator>>();
		for (Class<? extends IEstimationApproach> approach : approaches) {
			for (int i = 0; i < result.getNumberOfFolds(); i++) {
				ResultTable curFold = result.getEstimates(approach, i);
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

		Map<Class<? extends IValidator>, MatrixBuilder> meanErrors = new HashMap<Class<? extends IValidator>, MatrixBuilder>();
		Map<Class<? extends IValidator>, MatrixBuilder> meanPredictions = new HashMap<Class<? extends IValidator>, MatrixBuilder>();
		Map<Class<? extends IValidator>, List<ModelEntity>> validatedEntities = new HashMap<Class<? extends IValidator>, List<ModelEntity>>();

		MatrixBuilder meanEstimates = MatrixBuilder.create(variables.length);
		for (Class<? extends IEstimationApproach> approach : approaches) {
			MatrixBuilder lastEstimates = MatrixBuilder.create(variables.length);
			for (int i = 0; i < result.getNumberOfFolds(); i++) {
				ResultTable curFold = result.getEstimates(approach, i);
				lastEstimates.addRow(curFold.getLastEstimates());
			}
			Matrix lastEstimatesMatrix = lastEstimates.toMatrix();
			if (lastEstimatesMatrix.isEmpty()) {
				fail("No estimates found for approach " + Registry.INSTANCE.getDisplayName(approach));
			} else {
				meanEstimates.addRow(LinAlg.mean(lastEstimatesMatrix));
			}

			for (Class<? extends IValidator> validator : validators) {
				MatrixBuilder errorsBuilder = null;
				MatrixBuilder predictionsBuilder = null;
				for (int i = 0; i < result.getNumberOfFolds(); i++) {
					ResultTable curFold = result.getEstimates(approach, i);
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

		int idx = 0;
		for (ResourceDemand var : variables) {
			if (var.getService().getName().equals("WC0")) {
				for (int i = 0; i < approaches.size(); i++) {
					if (!Double.isNaN(meanEstimates.toMatrix().get(i, idx))) {
						Assert.assertEquals(0.025, meanEstimates.toMatrix().get(i, idx), 0.08);
					} else {
						fail("Value is NaN.");
					}
				}
			} else if (var.getService().getName().equals("WC1")) {
				for (int i = 0; i < approaches.size(); i++) {
					if (!Double.isNaN(meanEstimates.toMatrix().get(i, idx))) {
						Assert.assertEquals(0.125, meanEstimates.toMatrix().get(i, idx), 0.9);
					} else {
						fail("Value is NaN.");
					}
				}
			} else if (var.getService().getName().equals("WC2")) {
				for (int i = 0; i < approaches.size(); i++) {
					if (!Double.isNaN(meanEstimates.toMatrix().get(i, idx))) {
						Assert.assertEquals(0.075, meanEstimates.toMatrix().get(i, idx), 0.1);
					} else {
						fail("Value is NaN.");
					}
				}
			} else {
				fail("Unknown Service.");
			}
			idx++;
		}
	}

	private static void handleReadFromTrace() {
		Map<String, IDataSource> dataSources = new HashMap<String, IDataSource>();

		double maxStart = Double.MIN_VALUE;
		double minEnd = Double.MAX_VALUE;

		for (TraceConfiguration trace : conf.getInput().getObservations()) {
			if (trace instanceof FileTraceConfiguration) {
				FileTraceConfiguration fileTrace = (FileTraceConfiguration) trace;
				File inputFile = new File(fileTrace.getFile());
				if (inputFile.exists()) {
					DataSourceConfiguration dataSourceConf = fileTrace.getDataSource();
					if (dataSourceConf != null) {
						IDataSource ds = dataSources.get(dataSourceConf.getType());

						if (ds == null) {
							try {
								Class<?> cl = Registry.INSTANCE.getInstanceClass(dataSourceConf.getType());
								ds = (IDataSource) Instantiator.newInstance(cl, dataSourceConf.getParameters());
								dataSources.put(dataSourceConf.getType(), ds);
							} catch (Exception e) {
								e.printStackTrace();
								return;
							}
						}
						// it is enough to load the first mapping as all will
						// have the same timestamps
						if (fileTrace.getMappings().size() >= 1) {
							try {
								maxStart = Math.max(loadFirst(inputFile, 0, ","), maxStart);
								minEnd = Math.min(loadLast(inputFile, 0, ","), minEnd);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}

		if (maxStart >= minEnd) {
			System.err.print("The time spans of the traces to no overlap.");
			return;
		}

		Quantity<Time> maxStartpoint = UnitsFactory.eINSTANCE.createQuantity();
		maxStartpoint.setValue(maxStart);
		maxStartpoint.setUnit(Time.SECONDS);
		Quantity<Time> minEndpoint = UnitsFactory.eINSTANCE.createQuantity();
		minEndpoint.setValue(minEnd);
		minEndpoint.setUnit(Time.SECONDS);

		conf.getEstimation().setStartTimestamp(maxStartpoint);
		conf.getEstimation().setEndTimestamp(minEndpoint);

		// SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// System.out.println("Starpoint set to " + date.format(maxStart *
		// 1000));
		// System.out.println("Endpoint set to " + date.format(minEnd * 1000));

	}

	/**
	 * @param in
	 * @param col
	 * @return
	 */
	private static double loadLast(File inputFile, int col, String separator) {
		double stamp = -1;
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {

			String lastLine = null;
			String line = br.readLine();
			while (line != null) {
				lastLine = line;
				line = br.readLine();
			}

			String[] split = lastLine.split(separator);
			stamp = Double.parseDouble(split[col]);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return stamp;
	}

	/**
	 * @param inputFile
	 * @param col
	 * @return
	 */
	private static double loadFirst(File inputFile, int col, String separator) {
		double stamp = -1;
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {

			String line = br.readLine();
			try {
				String[] split = line.split(separator);
				stamp = Double.parseDouble(split[col]);
			} catch (Exception e) {
				// parse next line
				line = br.readLine();
				String[] split = line.split(separator);
				stamp = Double.parseDouble(split[col]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stamp;
	}
}
