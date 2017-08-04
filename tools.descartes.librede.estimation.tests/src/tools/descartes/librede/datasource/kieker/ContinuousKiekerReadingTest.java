package tools.descartes.librede.datasource.kieker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.BeforeClass;
import org.junit.Test;

import tools.descartes.librede.Librede;
import tools.descartes.librede.LibredeResults;
import tools.descartes.librede.LibredeVariables;
import tools.descartes.librede.bayesplusplus.BayesLibrary;
import tools.descartes.librede.configuration.LibredeConfiguration;
import tools.descartes.librede.continuous.AddContinuousDataToCSVTest;
import tools.descartes.librede.continuous.DataReadWrite;
import tools.descartes.librede.datasource.DataSourceSelector;
import tools.descartes.librede.datasource.IDataSource;
import tools.descartes.librede.datasource.IDataSourceListener;
import tools.descartes.librede.ipopt.java.IpoptLibrary;
import tools.descartes.librede.nnls.NNLSLibrary;
import tools.descartes.librede.registry.Registry;
import tools.descartes.librede.testutils.LibredeTest;

public class ContinuousKiekerReadingTest extends LibredeTest{

	private static final Logger log = Logger.getLogger(Librede.class);
	private static LibredeConfiguration conf;
	@BeforeClass
	public static void initLibraries() {
		System.out.println("Ich starte zumindest");
		//Librede.init();
		IpoptLibrary.init();
		NNLSLibrary.init();
		BayesLibrary.init();

		// load LibredeConf to test the execute method

		try {
			ResourceSet resourceSet = Registry.INSTANCE.createResourceSet();
			Resource resource = resourceSet.createResource(URI.createURI("kieker.librede"));
			InputStream confStream = ContinuousKiekerReadingTest.class.getResourceAsStream("kieker.librede");
			resource.load(confStream, new HashMap<Object, Object>());
			EcoreUtil.resolveAll(resource);
			conf = (LibredeConfiguration) resource.getContents().get(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
	
	@Test
	public void test() {
		int counter = 1;
		System.out.println("Ich starte zumindest2");
		boolean stop = false;
		Map<String, IDataSource> existingDatasources = new HashMap<>();
		IDataSourceListener dataSourceListener = new DataSourceSelector();
		LibredeVariables var = new LibredeVariables(conf);
		Librede.initRepoOnline(var, existingDatasources, dataSourceListener);
		while(!stop){
			LibredeResults results = Librede.executeContinuousOnline(var, existingDatasources, dataSourceListener);
	
			try {
				System.out.println("Type anything within the next 10 seconds to stop.");
				//Scanner scanner = new Scanner(System.in);
				Thread.sleep(10000);
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
