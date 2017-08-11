package tools.descartes.librede.datasource.kiekeramqp;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.reader.amqp.AMQPReader;
import kieker.common.configuration.Configuration;

public class KiekerAnalysisThread extends Thread {
	/**
	 * The kieker analysis controller that handles the AMQP Readers and Filters
	 */
	private IAnalysisController analysisInstance;
	/**
	 * The kieker AMQP reader, that reads from the rabbit mq
	 */
	private KiekerAMQPReader KiekerAMQPReader;
	/**
	 * The custom kieker filter, that handles the data for librede
	 */
	private LibredeTraceFilter libredeTraceFilter;
	/**
	 * The custom kieker filter, that handles the data for librede
	 */
	private KiekerFileFilter kiekerFileFilter;
	
	private boolean writefiles;
	
	public KiekerAnalysisThread(KiekerAmqpDataSource kiekerAmqpDataSource, String uri, String queueName, int triggercount,
			int mincount, int maxtimesec, int wait, String filedirectory) {
		//check if file writing is desired
		if(filedirectory!=null && !filedirectory.equals("")){
			writefiles = true;
		}else{
			writefiles = false;
		}
		
		//Create the kieker analysis controller
		analysisInstance = new AnalysisController();
		//Create and configure the KiekerAMQPReader
		final Configuration amqpConfiguration = new Configuration();
		amqpConfiguration.setProperty(KiekerAMQPReader.CONFIG_PROPERTY_URI, uri);
		amqpConfiguration.setProperty(KiekerAMQPReader.CONFIG_PROPERTY_QUEUENAME, queueName);
		KiekerAMQPReader = new KiekerAMQPReader(amqpConfiguration, analysisInstance);
		//Create and configure the LibredeTraceFiler
		final Configuration ltfConfiguration = new Configuration();
		ltfConfiguration.setProperty(LibredeTraceFilter.CONFIG_PROPERTY_NAME_EVENT_TRIGGER_COUNT, ""+triggercount);
		ltfConfiguration.setProperty(LibredeTraceFilter.CONFIG_PROPERTY_NAME_EVENT_TRIGGER_COUNT_MINIMUM, ""+mincount);
		ltfConfiguration.setProperty(LibredeTraceFilter.CONFIG_PROPERTY_NAME_EVENT_TRIGGER_COUNT_MAX_TIME_SEC, ""+maxtimesec);
		ltfConfiguration.setProperty(LibredeTraceFilter.CONFIG_PROPERTY_NAME_EVENT_TRIGGER_COUNT_WAIT_SEC, ""+wait);
		libredeTraceFilter = new LibredeTraceFilter(ltfConfiguration, analysisInstance);
		if(writefiles){
			//Create and configure the KiekerFileFiler
			final Configuration kffConfiguration = new Configuration();
			kffConfiguration.setProperty(KiekerFileFilter.CONFIG_PROPERTY_NAME_EVENT_DIRECTORY, filedirectory);
			kiekerFileFilter = new KiekerFileFilter(kffConfiguration, analysisInstance);
		}
		try {
			libredeTraceFilter.setDataSource(kiekerAmqpDataSource);
			analysisInstance.connect(KiekerAMQPReader, KiekerAMQPReader.OUTPUT_PORT_NAME_RECORDS, libredeTraceFilter, LibredeTraceFilter.INPUT_PORT_NAME_EVENTS);
			if(writefiles){
				analysisInstance.connect(KiekerAMQPReader, KiekerAMQPReader.OUTPUT_PORT_NAME_RECORDS, kiekerFileFilter, KiekerFileFilter.INPUT_PORT_NAME_EVENTS);
			}
		} catch (final AnalysisConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			analysisInstance.run();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AnalysisConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void terminate() {
		analysisInstance.terminate();
	}
}
