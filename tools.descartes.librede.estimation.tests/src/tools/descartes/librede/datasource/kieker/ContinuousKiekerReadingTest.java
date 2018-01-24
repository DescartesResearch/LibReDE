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
package tools.descartes.librede.datasource.kieker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import tools.descartes.librede.Librede;
import tools.descartes.librede.LibredeResults;
import tools.descartes.librede.LibredeVariables;
import tools.descartes.librede.configuration.DataSourceConfiguration;
import tools.descartes.librede.configuration.FileTraceConfiguration;
import tools.descartes.librede.configuration.LibredeConfiguration;
import tools.descartes.librede.configuration.Parameter;
import tools.descartes.librede.configuration.TraceConfiguration;
import tools.descartes.librede.datasource.DataSourceSelector;
import tools.descartes.librede.datasource.IDataSource;
import tools.descartes.librede.datasource.csv.CsvDataSource;
import tools.descartes.librede.ipopt.java.IpoptLibrary;
import tools.descartes.librede.nnls.NNLSLibrary;
import tools.descartes.librede.registry.Instantiator;
import tools.descartes.librede.registry.Registry;
import tools.descartes.librede.testutils.LibredeTest;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;
import tools.descartes.librede.units.UnitsFactory;

public class ContinuousKiekerReadingTest extends LibredeTest{

	private static final Logger log = Logger.getLogger(Librede.class);
	private static LibredeConfiguration conf;	
	@BeforeClass
	public static void initLibraries() {
		System.out.println("Ich starte zumindest");
		//Librede.init();
		IpoptLibrary.init();
		NNLSLibrary.init();
//		BayesLibrary.init();
	}
	

	@Test
	@Ignore
	public void rabbitmqtest(){
		//create the runner class
		RunnerThread runner = new RunnerThread();
		//start it
		runner.start();
		System.out.println("Type something to stop");
		Scanner scanner = new Scanner(System.in);
		scanner.next();
		System.out.println("Terminating the calculation...");
		runner.terminate();
		try {
			System.out.println("Wait for the calculations to finish...");
			runner.join();
			System.out.println("Terminated!");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * The class that runs the logic
	 * 
	 * @author torsten
	 *
	 */
	private class RunnerThread extends Thread{
		private volatile boolean stop = false;
		private boolean isWaiting = false;
		public void terminate() {
			this.stop = true;
			if(isWaiting){
				this.interrupt();
			}
		}
		@Override
		public void run() {
			System.out.println("Load configuration...");
			loadConf("kieker_amqp.librede");
			System.out.println("Create DataSourceListener / Selector ...");
			Map<String, IDataSource> existingDatasources = new HashMap<>();
			DataSourceSelector dataSourceListener = new DataSourceSelector();
			System.out.println("Setting configuration values ...");
			conf.getEstimation().setWindow(1);
			Quantity<Time> onesecond = UnitsFactory.eINSTANCE.createQuantity(60, Time.SECONDS);
			conf.getEstimation().setStepSize(onesecond);
			//IN ONLINE MODE SET THE START TIME HERE!!!
			//Quantity<Time> defaulttime = conf.getEstimation().getStartTimestamp();
			//Unit<Time> unit = defaulttime.getUnit();
			double actualtime = System.currentTimeMillis();
			//actualtime = ((int)(actualtime/1000))*1000.0;
			
			//Unit<Time> nanotime = Time.NANOSECONDS;
			//double actualtimeinunit = nanotime.convertTo(actualtime, unit);
			
			//defaulttime.setValue(actualtime);
			//conf.getEstimation().setStartTimestamp(defaulttime);
			System.out.println("Loading Librede Variables...");
			LibredeVariables var = new LibredeVariables(conf);
			System.out.println("Initialize DataSources...");
			Librede.initDataSources(var, existingDatasources, dataSourceListener);
			//the next time stamp when librede should calculate resource demands
			long offsettime = 300000; //5mins
			long nextexecutiontimestamp = (long)actualtime+offsettime;
			long calculationinterval = 60000;
			long pollinginteral = 20000;
			while(!stop){
				System.out.println("Wait until the next calcualtion...");
				//sleep some time to give the repo time to initialize
				//more precise: give the watchthread time to push the initil data to the dataSourceListener
				
				//the time between two librede calculations
				try {
					isWaiting = true;
					Thread.sleep(pollinginteral);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
				isWaiting = false;
				if(!stop){
					System.out.println("Updating repository for some time...");
					Librede.updateRepositoryOnline(5000, var, existingDatasources, dataSourceListener);
					System.out.println("Stopped updating repository.");
				}
				if(!stop && System.currentTimeMillis() >= nextexecutiontimestamp){
					System.out.println("Start the next calcualtion...");
					//update the repo and calcualte the results
					LibredeResults results = Librede.executeOnline(var, existingDatasources, dataSourceListener);
					/*for (Class<? extends IEstimationApproach> approach : results.getApproaches()) {
						double responsetimeerror = results.getApproachResponseTimeError(approach);
						double utilizationerror = results.getApproachUtilizationError(approach);
					}*/
					try {
						System.out.println("Writing results...");
						File outputfile = new File("/home/torsten/Schreibtisch/jettytests/local/1l_600s_500t_ubuntu_visits/info/results.txt");
						PrintStream outputStream = new PrintStream(new FileOutputStream(outputfile, true));
						Librede.printSummary(results, outputStream);
						outputStream.flush();
						outputStream.close();
						System.out.println("Results written!");
					} catch (FileNotFoundException e) {
						System.out.println("WARN: Cannot write output!");
					}
					System.out.println("Calcualtion done!");
					nextexecutiontimestamp = nextexecutiontimestamp+calculationinterval;
				}
			}
			System.out.println("Librede Process stopped!");
			System.out.println("Start closing datasources...");
			//close the databases
			for (Entry<String, IDataSource> entry : existingDatasources.entrySet()) {
				try {
					System.out.println("Closing datasource: "+entry.getKey());
					entry.getValue().close();
				} catch (IOException e) {
					log.error("Error closing data source.", e);
				}
			}
			System.out.println("Finished closing datasources!");
			System.out.println("Finish!");
		}
		
	}
	
	
	@Test
	@Ignore
	public void runEstimation(){
		LibredeResults results = null;
		conf = null;
		conf = Librede.loadConfiguration(new File("/home/torsten/git/librede/tools.descartes.librede.estimation.tests/src/tools/descartes/librede/datasource/kieker/kiekercsv.librede").toPath());
	    conf.getEstimation().setWindow(1);
		fixTimeStamps(conf);
	    try {
			LibredeVariables var = new LibredeVariables(conf);
			Librede.initRepo(var);
			if (var.getConf().getValidation().getValidationFolds() <= 1) {
				results = Librede.runEstimationWithValidation(var);
			} else {
				results = Librede.runEstimationWithCrossValidation(var);
			}
		} catch (Exception e) {
			log.error("Error running estimation.", e);
			results = null;
		}
	    System.out.println(results.toString());
	    System.out.println("ende");
	}
	public static boolean fixTimeStamps(LibredeConfiguration conf) {
		Map<String, IDataSource> dataSources = new HashMap<String, IDataSource>();

		double maxStart = Double.MIN_VALUE;
		double minEnd = Double.MAX_VALUE;

		Unit<Time> unit = Time.MILLISECONDS;

		for (TraceConfiguration trace : conf.getInput().getObservations()) {
			if (trace instanceof FileTraceConfiguration) {
				FileTraceConfiguration fileTrace = (FileTraceConfiguration) trace;
				File inputFile = new File(fileTrace.getFile());
				if (inputFile.exists()) {
					DataSourceConfiguration dataSourceConf = fileTrace
							.getDataSource();
					if (dataSourceConf != null) {
						IDataSource ds = dataSources.get(dataSourceConf
								.getType());

						if (ds == null) {
							try {
								Class<?> cl = Registry.INSTANCE
										.getInstanceClass(dataSourceConf
												.getType());
								ds = (IDataSource) Instantiator.newInstance(cl,
										dataSourceConf.getParameters());
								dataSources.put(dataSourceConf.getType(), ds);
							} catch (Exception e) {
								e.printStackTrace();
								return false;
							}
						}

						// retrieve all important parameters
						if (ds instanceof CsvDataSource) {

							unit = parseTimeUnit(fileTrace.getDataSource());
							if (fileTrace.getMappings().size() >= 1) {
								try {
									// assume that the timestamp is always in
									// column
									// 0, this should be changed?
									maxStart = Math.max(
											loadFirst(inputFile, 0,
													getSeparators(fileTrace
															.getDataSource())),
											maxStart);
									minEnd = Math.min(
											loadLast(inputFile, 0,
													getSeparators(fileTrace
															.getDataSource())),
											minEnd);
								} catch (Exception e) {
									log.error("Error occurred", e);
								}
							}
						} else {
							log.error("Other Datasources than csv are not supported yet...");
						}
					}
				}
			}
		}

		if (maxStart >= minEnd) {
			log.error("The time spans of the traces do not overlap.");
			return false;
		}

		Quantity<Time> maxStartpoint = UnitsFactory.eINSTANCE.createQuantity();
		maxStartpoint.setValue(maxStart);
		maxStartpoint.setUnit(unit);
		Quantity<Time> minEndpoint = UnitsFactory.eINSTANCE.createQuantity();
		minEndpoint.setValue(minEnd);
		minEndpoint.setUnit(unit);

		conf.getEstimation().setStartTimestamp(maxStartpoint);
		conf.getEstimation().setEndTimestamp(minEndpoint);

		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		log.trace("Startpoint of config " + conf.toString() + " set to "
				+ date.format(maxStartpoint.getValue(Time.MILLISECONDS)));
		log.trace("Endpoint of config " + conf.toString() + " set to "
				+ date.format(minEndpoint.getValue(Time.MILLISECONDS)));
		return true;
	}
	private static Unit<Time> parseTimeUnit(DataSourceConfiguration datasource) {
		String simpleDateFormat = null;
		for (Parameter p : datasource.getParameters()) {
			if (p.getName().equals("TimestampFormat")) {
				simpleDateFormat = p.getValue();
			}
		}

		if (simpleDateFormat == null) {
			// default
			return Time.MILLISECONDS;
		}

		// default
		Unit<Time> dateUnit = Time.MILLISECONDS;
		if (simpleDateFormat != null && !simpleDateFormat.isEmpty()) {
			if (simpleDateFormat.startsWith("[")
					&& simpleDateFormat.endsWith("]")) {
				String unit = simpleDateFormat.substring(1,
						simpleDateFormat.length() - 1);
				for (Unit<?> u : Time.INSTANCE.getUnits()) {
					if (u.getSymbol().equalsIgnoreCase(unit)) {
						dateUnit = (Unit<Time>) u;
						break;
					}
				}
			} else {
				// default
				dateUnit = Time.MILLISECONDS;
			}
		}
		return dateUnit;
	}
	private static String getSeparators(DataSourceConfiguration dataSource) {
		for (Parameter p : dataSource.getParameters()) {
			if (p.getName().equals("Separators")) {
				return p.getValue();
			}
		}
		// default
		return ",";
	}
	private static double loadFirst(File inputFile, int col, String separator) {
		double stamp = -1;
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {

			String line = br.readLine();
			try {
				if (line != null && !line.isEmpty()) {
					String[] split = line.split(separator);
					stamp = Double.parseDouble(split[col]);
				}
			} catch (NumberFormatException e) {
				// parse next line since first was obviously header
				line = br.readLine();
				if (line != null && !line.isEmpty()) {
					String[] split = line.split(separator);
					stamp = Double.parseDouble(split[col]);
				}
			}
		} catch (IOException e) {
			log.error("IOError occurred.", e);
		}
		if (stamp == -1) {
			log.warn("No readable Timestamp found.");
		}
		return stamp;
	}
	private static double loadLast(File inputFile, int col, String separator) {
		double stamp = -1;
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {

			String lastLine = null;
			String line = br.readLine();
			while (line != null) {
				lastLine = line;
				line = br.readLine();
			}

			try {
				if (lastLine != null && !lastLine.isEmpty()) {
					String[] split = lastLine.split(separator);
					stamp = Double.parseDouble(split[col]);
				}
			} catch (NumberFormatException e) {
				// line was probably an empty file with just an header
			}

		} catch (IOException e) {
			log.error("IOError occurred.", e);
		}
		if (stamp == -1) {
			log.warn("No readable Timestamp found.");
		}
		return stamp;
	}

	
	private void loadConf(String confstring){
		// load LibredeConf to test the execute method

				try {
					ResourceSet resourceSet = Registry.INSTANCE.createResourceSet();
					Resource resource = resourceSet.createResource(URI.createURI(confstring));
					InputStream confStream = ContinuousKiekerReadingTest.class.getResourceAsStream(confstring);
					resource.load(confStream, new HashMap<Object, Object>());
					EcoreUtil.resolveAll(resource);
					conf = (LibredeConfiguration) resource.getContents().get(0);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
	}
	@Test
	@Ignore
	public void test() {
		loadConf("kieker.librede");
		int counter = 0;
		System.out.println("Ich starte zumindest2");
		boolean stop = false;
		Map<String, IDataSource> existingDatasources = new HashMap<>();
		DataSourceSelector dataSourceListener = new DataSourceSelector();
		conf.getEstimation().setWindow(60);
		Quantity<Time> onesecond = UnitsFactory.eINSTANCE.createQuantity(60, Time.SECONDS);
		conf.getEstimation().setStepSize(onesecond);
		//IN ONLINE MODE SET THE START TIME HERE!!!
		/*Quantity<Time> defaulttime = conf.getEstimation().getStartTimestamp();
		Unit<Time> unit = defaulttime.getUnit();
		double actualtime = System.nanoTime();
		Unit<Time> nanotime = Time.NANOSECONDS;
		nanotime.convertTo(actualtime, unit);
		defaulttime.setValue(actualtime);
		conf.getEstimation().setStartTimestamp(defaulttime);*/
		LibredeVariables var = new LibredeVariables(conf);
		Librede.initDataSources(var, existingDatasources, dataSourceListener);
		//sleep some time to give the repo time to initialize
		//more precise: give the watchthread time to push the initil data to the dataSourceListener
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(!stop){
			LibredeResults results = Librede.executeContinuousOnline(5000, var, existingDatasources, dataSourceListener);
	
			try {
				System.out.println("Type anything within the next 5 seconds to stop.");
				//Scanner scanner = new Scanner(System.in);
				Thread.sleep(5000);
				if(counter==0){
					System.out.println("You gave a input, therefore we stop now");
					stop = true;
				}else{
					counter--;
					System.out.println("You gave no input, therefore we continue");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (Entry<String, IDataSource> entry : existingDatasources.entrySet()) {
			try {
				entry.getValue().close();
			} catch (IOException e) {
				log.error("Error closing data source.", e);
			}
		}
//		System.out.println("Saving Data to CSV");
//		File outputFile = new File("");
//		var.exportResultTimelineCSV(outputFile);

		//checkLibredeResults(var.getResults());

	}
}
