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

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import tools.descartes.librede.approach.IEstimationApproach;
import tools.descartes.librede.configuration.ExporterConfiguration;
import tools.descartes.librede.configuration.LibredeConfiguration;
import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.export.IExporter;
import tools.descartes.librede.linalg.LinAlg;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.MatrixBuilder;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.registry.Instantiator;
import tools.descartes.librede.registry.Registry;
import tools.descartes.librede.validation.IValidator;

/**
 * This class contains methods for exporting instances of
 * {@link LibredeResults}.
 * 
 * @author Johannes Grohmann (johannes.grohmann@uni-wuerzburg.de)
 *
 */
public class ResultPrinter {

	private static final Logger log = Logger.getLogger(ResultPrinter.class);

	/**
	 * Print the {@link LibredeResults} object to the given {@link PrintStream}.
	 * 
	 * @param results
	 *            The result object to print.
	 * @param outputStream
	 *            The output stream to print the results to.
	 */
	public static void printSummary(LibredeResults results, PrintStream outputStream) {
		outputStream.println("New results at " + System.currentTimeMillis() + " (time in milliseconds)");
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
		outputStream.println("Approaches");
		outputStream.println("==========");
		for (int i = 0; i < approaches.size(); i++) {
			outputStream.printf("[%d] %s\n", i + 1, Registry.INSTANCE.getDisplayName(approaches.get(i)));
		}
		outputStream.println();

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
			outputStream.println("Estimates");
			outputStream.println("=========");
			printEstimatesTable(variables, approaches, meanEstimates.toMatrix(), outputStream);
			outputStream.println();

			if (validators.size() > 0) {
				// Cross-Validation Results
				outputStream.println("Cross-Validation Results:");
				outputStream.println("=========================");

				for (Class<? extends IValidator> validator : validators) {
					String name = Registry.INSTANCE.getDisplayName(validator);
					outputStream.println(name + ":");
					if (meanErrors.containsKey(validator)) {

						Matrix errors = meanErrors.get(validator).toMatrix();
						Matrix predictions = meanPredictions.get(validator).toMatrix();
						printValidationResultsTable(validatedEntities.get(validator), approaches, predictions, errors,
								outputStream);
						outputStream.println();
					} else {
						outputStream.println("No results.");
					}
				}
			}
		}
	}

	/**
	 * Prints the {@link LibredeResults} object to the console.
	 * 
	 * @param results
	 *            The result object to print.
	 */
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
			List<Class<? extends IEstimationApproach>> approaches, Matrix predictions, Matrix errors,
			PrintStream outputStream) {
		outputStream.printf("%-80.80s | ", "Resource or service");
		for (int i = 0; i < approaches.size(); i++) {
			outputStream.printf("%-9.9s", "[" + (i + 1) + "]");
		}
		outputStream.println("|");

		for (int i = 0; i < (87 + approaches.size() * 9); i++) {
			outputStream.print("-");
		}
		outputStream.println();

		int idx = 0;
		for (ModelEntity entity : entities) {
			outputStream.printf("%-80.80s | ", limitOutput(entity.getName(), 80));
			for (int i = 0; i < approaches.size(); i++) {
				outputStream.printf("%.5e %.5f%% ", predictions.get(i, idx), errors.get(i, idx) * 100);
			}
			outputStream.println("|");
			idx++;
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
			List<Class<? extends IEstimationApproach>> approaches, Matrix values, PrintStream outputStream) {
		outputStream.printf("%-20.20s | ", "Resource");
		outputStream.printf("%-60.60s | ", "Service");
		for (int i = 0; i < approaches.size(); i++) {
			outputStream.printf("%-9.9s", "[" + (i + 1) + "]");
		}
		outputStream.println("|");

		for (int i = 0; i < (87 + approaches.size() * 9); i++) {
			outputStream.print("-");
		}
		outputStream.println();
		Resource last = null;
		int idx = 0;
		for (ResourceDemand var : variables) {
			if (var.getResource().equals(last)) {
				outputStream.printf("%-20.20s | ", "");
			} else {
				outputStream.printf("%-20.20s | ", limitOutput(var.getResource().getName(), 20));
			}
			outputStream.printf("%-60.60s | ", limitOutput(var.getService().getName(), 60));
			for (int i = 0; i < approaches.size(); i++) {
				outputStream.printf("%.5fs ", values.get(i, idx));
			}
			outputStream.println("|");
			last = var.getResource();
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

	/**
	 * Export the {@link LibredeResults} object using the specified exporter of
	 * the given {@link LibredeConfiguration}.
	 * 
	 * @param conf
	 *            The configuration object containing the
	 *            {@link ExporterConfiguration} to use.
	 * 
	 * @param results
	 *            The result object to export.
	 * 
	 */
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
}
