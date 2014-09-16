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

import static edu.kit.ipd.descartes.linalg.LinAlg.vector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.descartesresearch.librede.configuration.ModelEntity;
import net.descartesresearch.librede.configuration.Resource;
import net.descartesresearch.librede.configuration.Service;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.MapOptionHandler;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;

import edu.kit.ipd.descartes.librede.estimation.repository.IMetric;
import edu.kit.ipd.descartes.librede.estimation.repository.IMonitoringRepository;
import edu.kit.ipd.descartes.librede.estimation.repository.StandardMetric;
import edu.kit.ipd.descartes.librede.estimation.repository.TimeSeries;
import edu.kit.ipd.descartes.librede.estimation.workload.WorkloadDescription;
import edu.kit.ipd.descartes.librede.frontend.EstimationHelper.EstimationResult;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorFunction;

public class Console {

	private static final Logger log = Logger.getLogger(Console.class);

	@Option(name = "-r", aliases = { "--resources" }, required = true, handler = StringArrayOptionHandler.class)
	private String[] resources;

	@Option(name = "-s", aliases = { "--services" }, required = true, handler = StringArrayOptionHandler.class)
	private String[] services;

	@Option(name = "-a", aliases = { "--approaches" }, required = false, handler = StringArrayOptionHandler.class)
	private String[] approaches;

	@Option(name = "-UTIL", handler = MapOptionHandler.class)
	private Map<String, String> utilization;

	@Option(name = "-RT", handler = MapOptionHandler.class)
	private Map<String, String> responsetime;

	@Option(name = "-TPUT", handler = MapOptionHandler.class)
	private Map<String, String> throughput;

	@Option(name = "-M", handler = MapOptionHandler.class)
	private Map<String, String> metrics;

	@Option(name = "-i", aliases = { "--step" })
	private double step = 60;

	@Option(name = "-b", aliases = { "--start-time" })
	private double startTime = -1;

	@Option(name = "-e", aliases = { "--end-time" })
	private double endTime = -1;

	@Option(name = "-o", aliases = { "--output-dir" }, required = false)
	private File outputDirectory = null;

	@Option(name = "-w", aliases = { "--window" }, required = false)
	private int window = 15;

	@Option(name = "-n", aliases = { "--iterative" })
	private boolean iterative = false;

	@Option(name = "-f", aliases = { "--folds" }, required = false)
	private int folds = 0;

	private Path currentPath;

	public static void main(String[] args) {
		Console instance = new Console();
		instance.run(args);
	}

	public void run(String[] args) {
		BasicConfigurator.configure();

		CmdLineParser parser = new CmdLineParser(this);
		try {
			currentPath = Paths.get("").toAbsolutePath();

			parser.parseArgument(args);

			if (outputDirectory == null) {
				outputDirectory = currentPath.toFile();
			} else {
				outputDirectory = new File(currentPath.toFile(), outputDirectory.toString());
				outputDirectory.mkdirs();
			}
			log.info("Base directory: " + outputDirectory);

			execute();
		} catch (CmdLineException ex) {
			System.err.println(ex.getMessage());
			parser.printUsage(System.err);
		} catch (Exception ex) {
			log.error("Error executing resource demand estimation.", ex);
		}
	}

	private void execute() throws Exception {
//		WorkloadDescription workload = EstimationHelper
//				.createWorkloadDescription(services, resources);
//		IMonitoringRepository repo = EstimationHelper.createRepository(
//				workload, endTime);
//		loadRepository(repo, workload);
//		if (folds <= 0) {
//			Map<String, EstimationResult> results = EstimationHelper
//					.runEstimation(approaches, repo, startTime, step, window,
//							iterative);
//			printSummary(workload, results);
//			writeResults(results);
//		} else {
//			Map<String, EstimationResult> results = EstimationHelper
//					.runEstimationWithCrossValidation(approaches, repo,
//							startTime, step, window, iterative, folds);
//			printSummary(workload, results);
//			writeResults(results);
//		}
	}

	private void printSummary(WorkloadDescription workload,
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

	private void writeResults(Map<String, EstimationResult> results) throws FileNotFoundException {
		for (String approach : results.keySet()) {
			EstimationResult curResult = results.get(approach);
			for (int i = 0; i < curResult.estimates.size(); i++) {
				File outputFile = new File(outputDirectory, "estimates_" + approach + "_" + "fold_" + (i + 1) + ".csv");
				
				Vector time = curResult.estimates.get(i).getTime();
				Matrix estimates = curResult.estimates.get(i).getData();
		
				PrintWriter out = new PrintWriter(outputFile);
				try {
					for (int j = 0; j < time.rows(); j++) {
						out.print(time.get(j));
						for (int k = 0; k < estimates.columns(); k++) {
							out.print(",");
							out.print(estimates.get(j, k));
						}
						out.println();
					}
				} finally {
					out.close();
				}
			}
		}
	}
}
