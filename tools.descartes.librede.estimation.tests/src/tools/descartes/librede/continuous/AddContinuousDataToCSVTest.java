package tools.descartes.librede.continuous;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import tools.descartes.librede.Librede;
import tools.descartes.librede.LibredeResults;
import tools.descartes.librede.LibredeVariables;
import tools.descartes.librede.ResultTable;
import tools.descartes.librede.approach.IEstimationApproach;
import tools.descartes.librede.bayesplusplus.BayesLibrary;
import tools.descartes.librede.configuration.LibredeConfiguration;
import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.datasource.IDataSource;
import tools.descartes.librede.ipopt.java.IpoptLibrary;
import tools.descartes.librede.linalg.LinAlg;
import tools.descartes.librede.linalg.MatrixBuilder;
import tools.descartes.librede.nnls.NNLSLibrary;
import tools.descartes.librede.registry.Registry;
import tools.descartes.librede.testutils.LibredeTest;

public class AddContinuousDataToCSVTest extends LibredeTest {

	private static int WRITE_RATE = 1;
	private static int LINES_TO_COPY = 5;
	private static String SOURCE_PATH = "C:/Users/Veronika/Desktop/example1/sourcefiles/";
	private static String DESTINATION_PATH = "C:/Users/Veronika/Desktop/example1/";
	private static LibredeConfiguration conf;

	@BeforeClass
	public static void initLibraries() {
		IpoptLibrary.init();
		NNLSLibrary.init();
		BayesLibrary.init();

		// load LibredeConf to test the execute method

		try {
			ResourceSet resourceSet = Registry.INSTANCE.createResourceSet();
			Resource resource = resourceSet.createResource(URI.createURI("estimation2.librede"));
			InputStream confStream = AddContinuousDataToCSVTest.class.getResourceAsStream("estimation2.librede");
			resource.load(confStream, new HashMap<Object, Object>());
			EcoreUtil.resolveAll(resource);
			conf = (LibredeConfiguration) resource.getContents().get(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}

	@Test
	public void test() {
		LibredeVariables var = new LibredeVariables(conf);
		Librede.initRepo(var);

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

		while (!(tp.isShutdown())) {
			System.out.println("Test running");
			var.updateResults(Librede.executeContinuous(var, Collections.<String, IDataSource> emptyMap()));
			checkLibredeResults(var.getResults());
			boolean runningTasks = false;
			for (DataReadWrite drw : drws) {
				if (!drw.getDataReader().isFileEnd()) {
					runningTasks = true;
				}
			}
			if (!runningTasks) {
				tp.shutdown();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}

		

	}

	private static void checkLibredeResults(LibredeResults result) {
		// check structure of libredeResult
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getApproaches());

		Assert.assertEquals(5, result.getNumberOfFolds());
		ResourceDemand[] variables = null;
		for (Class<? extends IEstimationApproach> approach : result.getApproaches()) {
			for (int fold = 0; fold < 5; fold++) {
				Assert.assertNotNull(result.getEstimates(approach, fold));
				ResultTable curFold = result.getEstimates(approach, fold);
				if (variables == null) {
					variables = curFold.getStateVariables();
				}
			}
		}

		List<Class<? extends IEstimationApproach>> approaches = new ArrayList<>(result.getApproaches());
		MatrixBuilder meanEstimates = MatrixBuilder.create(variables.length);
		for (Class<? extends IEstimationApproach> approach : approaches) {
			MatrixBuilder lastEstimates = MatrixBuilder.create(variables.length);
			for (int i = 0; i < result.getNumberOfFolds(); i++) {
				ResultTable curFold = result.getEstimates(approach, i);
				lastEstimates.addRow(curFold.getLastEstimates());
			}
			meanEstimates.addRow(LinAlg.mean(lastEstimates.toMatrix()));
		}

		int idx = 0;
		for (ResourceDemand var : variables) {
			if (var.getService().getName().equals("WC0")) {
				for (int i = 0; i < approaches.size(); i++) {
					if (!Double.isNaN(meanEstimates.toMatrix().get(i, idx))) {
						Assert.assertEquals(0.025, meanEstimates.toMatrix().get(i, idx), 0.08);
					}
				}
			} else if (var.getService().getName().equals("WC1")) {
				for (int i = 0; i < approaches.size(); i++) {
					if (!Double.isNaN(meanEstimates.toMatrix().get(i, idx))) {
						Assert.assertEquals(0.125, meanEstimates.toMatrix().get(i, idx), 0.9);
					}
				}
			} else if (var.getService().getName().equals("WC2")) {
				for (int i = 0; i < approaches.size(); i++) {
					if (!Double.isNaN(meanEstimates.toMatrix().get(i, idx))) {
						Assert.assertEquals(0.075, meanEstimates.toMatrix().get(i, idx), 0.1);
					}
				}
			}
			idx++;
		}
	}

}
