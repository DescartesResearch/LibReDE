package tools.descartes.librede.datasource.kieker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import tools.descartes.librede.configuration.FileTraceConfiguration;
import tools.descartes.librede.configuration.LibredeConfiguration;
import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.TraceConfiguration;
import tools.descartes.librede.configuration.TraceToEntityMapping;
import tools.descartes.librede.datasource.AbstractDataSource;
import tools.descartes.librede.datasource.Loggers;
import tools.descartes.librede.datasource.Stream;
import tools.descartes.librede.datasource.TraceEvent;
import tools.descartes.librede.datasource.TraceKey;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.registry.ParameterDefinition;
import tools.descartes.librede.units.Dimension;

/**
 * This class is a datasource for kieker data files
 * Therefore, it has a KieckerWatchThread instance, that listens to a directory.
 * 
 * @author torsten
 *
 */
@Component(displayName = "Kieker Data Source")
public class KiekerDataSource extends AbstractDataSource {

	@ParameterDefinition(name = "Corenumber", label = "Corenumber", required = false, defaultValue = "4")
	private int corenumber;
	
	@ParameterDefinition(name = "Visitsinterval", label = "Visitsinterval", required = false, defaultValue = "10")
	private int visitsinterval;
	/**
	 * The logging instance
	 */
	private final Logger log = Loggers.DATASOURCE_LOG;
	/**
	 * The thread that watches the kieker directories
	 */
	private KiekerWatchThread watchThread;
	/**
	 * The libredeconfiguration to identify the services we deal with
	 */
	private Set<ModelEntity> services;
	/**
	 * The libredeconfiguration to identify the resources we deal with
	 */
	private Set<ModelEntity> resources;
	/**
	 * The constructor of the data source.
	 * Here the KieckerWatchThread is initialized, but not started.
	 * 
	 * @param configuration
	 * @throws IOException
	 */
	public KiekerDataSource() throws IOException {
		this.services = new HashSet<>();
		this.resources = new HashSet<>();
		this.watchThread = new KiekerWatchThread();
		this.watchThread.setDaemon(true);
	}
	/**
	 * Overridden Method of the IDataSource Interface.
	 * Here we first read all the data that are currently available.
	 * Then we start the thread to notify, when new data came in.
	 */
	@Override
	public void load() throws IOException {
		//Read all the data that are currently available.
		watchThread.poll();
		//Start the thread to notify, when new data came in.
		watchThread.start();
	}
	/**
	 * Add a configuration to the Trace List.
	 * In Kieker datasources we add a specific directory and a specific metric we want to observe.
	 */
	@Override
	public List<TraceKey> addTrace(TraceConfiguration configuration) throws IOException {
		//check for savety
		if (watchThread == null) {
			throw new IllegalStateException();
		}
		//THIS IS EXTRA LOGIC FOR GETTING ALL THE SERVICES AND RESOURCES
		//FROM THE LIBREDE FILE INTO THE KIEKER DATA SOURCE
		//WE THEREFOR ADD ALL SERVICES TO THE TRACE MAPPINGS
		//AND EXTRACT THEM HERE.
		//only do it when we have a mapping and when did not do it before.
		if(configuration.getMetric().equals(StandardMetrics.RESPONSE_TIME)){
			if(!configuration.getMappings().isEmpty() && services.size()==0){
				for (TraceToEntityMapping mapping : configuration.getMappings()) {
					services.add(mapping.getEntity());
				}
			}
		} else if(configuration.getMetric().equals(StandardMetrics.VISITS)){
			if(!configuration.getMappings().isEmpty() && services.size()==0){
				for (TraceToEntityMapping mapping : configuration.getMappings()) {
					services.add(mapping.getEntity());
				}
			}
		} else if(configuration.getMetric().equals(StandardMetrics.THROUGHPUT)){
			if(!configuration.getMappings().isEmpty() && services.size()==0){
				for (TraceToEntityMapping mapping : configuration.getMappings()) {
					services.add(mapping.getEntity());
				}
			}
		}
		else if(configuration.getMetric().equals(StandardMetrics.UTILIZATION)){
			if(!configuration.getMappings().isEmpty() && resources.size()==0){
				for (TraceToEntityMapping mapping : configuration.getMappings()) {
					resources.add(mapping.getEntity());
				}
			}
		}
		else if(configuration.getMetric().equals(StandardMetrics.BUSY_TIME)){
			if(!configuration.getMappings().isEmpty() && resources.size()==0){
				for (TraceToEntityMapping mapping : configuration.getMappings()) {
					resources.add(mapping.getEntity());
				}
			}
		}
		
		
		//the new channel that deals with our directory
		KiekerChannel channel;
		//check if we gave a filename
		if (configuration instanceof FileTraceConfiguration) {
			//get the directory 
			FileTraceConfiguration fileTrace = (FileTraceConfiguration) configuration;
			File inputDirectory = new File(fileTrace.getFile());
			//check if it is a directory
			if (inputDirectory.exists() && inputDirectory.isDirectory()) {
				//Get the channel for this directory
				//Open new channel if necessary		
				channel = watchThread.getChannel(inputDirectory);
				if (channel == null) {
					channel = new KiekerChannel(inputDirectory, this);
					watchThread.registerChannel(channel);
				}
			} else {
				throw new FileNotFoundException(inputDirectory.toString() + "does not exist!");
			}
		}else{
			throw new UnsupportedOperationException("You need a FileTraceConfiguration!");
		}
		//get the tracekey off this configuration
		List<TraceKey> keys = new LinkedList<TraceKey>();
		TraceKey k = new TraceKey(configuration.getMetric(), configuration.getUnit(), configuration.getInterval(),
				configuration.getMappings().get(0).getEntity(), configuration.getAggregation(), configuration.getMappings().get(0).getFilters());
		//notify the listeners
		notifyListenersNewKey(k);
		//map the metric to the kiekerId we need to observe
		KiekerId kiekerId = mapKiekerId(configuration.getMetric());
		//tell the channel to observe the metric of this tracekey and which kiekerid is necessary for that task
		channel.addTrace(k,kiekerId);
		//return the key
		keys.add(k);
		return keys;
	}
	/**
	 * Maps the metric of the libede traceconfiguration to a kieker id enum.
	 * Here you can find all the metrics, that are currently supported.
	 * 
	 * @param metric
	 * @return
	 */
	private KiekerId mapKiekerId(Metric<? extends Dimension> metric) {
		KiekerId rc = KiekerId.Unknown;
		if(metric.equals(StandardMetrics.UTILIZATION)){
			rc = KiekerId.CPUUtilizationRecord;
		} else if(metric.equals(StandardMetrics.RESPONSE_TIME)){
			rc = KiekerId.OperationExecutionRecord;
		} else if(metric.equals(StandardMetrics.VISITS)){
			rc = KiekerId.OperationExecutionRecord;
		} else if(metric.equals(StandardMetrics.THROUGHPUT)){
			rc = KiekerId.OperationExecutionRecord;
		} else if(metric.equals(StandardMetrics.BUSY_TIME)){
			rc = KiekerId.CPUUtilizationRecord;
		}
		return rc;
	}
	/**
	 * This method closes the watch thread and therefore the datasource.
	 */
	public void close() throws IOException {
		if (this.watchThread != null) {
			this.watchThread.close();
			try {
				this.watchThread.join();
			} catch (InterruptedException e) {
				log.error("Error joining fileWatcher thread.", e);
			}
		}
		this.watchThread = null;
	}
	/**
	 * This method can declare some rules every kieker file has. 
	 * The rules should show which lines to ignore.
	 * 
	 * @param stream - the stream we come from
	 * @param line - the line we want to check
	 * @param readLines - the number of the line in the file
	 * @return
	 */
	public boolean skipLine(Stream stream, String line, int readLines) {
		boolean rc = false;
		//create your rules here
		if(line.contains("kieker.monitoring.probe.servlet.SessionAndTraceRegistrationFilter.doFilter")){
			//this is the kieker monitoring probe method
			rc = true;
		}
		return rc;
	}
	/**
	 * This method splits the kieker trace file line into all its parts.
	 * The seperator is specified here.
	 * 
	 * @param line
	 * @return
	 */
	public String[] parse(String line){
		//get the values out of the line
		return line.split(";");
	}
	/**
	 * This method gets the timestamp out of a kieker trace row.
	 * The timestamp is always the second entry after the id.
	 * 
	 * @param strings
	 * @return
	 */
	public double parseTimeStamp(String[] strings) {
		return Double.valueOf(strings[1]);
	}
	/**
	 * This method gets the response time timestamp out of a kieker trace row.
	 * This is the start timestamp of the method.
	 * 
	 * @param strings
	 * @return
	 */
	public double parseOperationExecutionStartTimeStamp(String[] values) {
		return Double.valueOf(values[5]);
	}
	
	/**
	 * This method gets the response time out of a kieker trace row.
	 * 
	 * @param values
	 * @param key
	 * @return
	 */
	public double parseResponseTime(String[] values) {
		Double rc = -1.0;
		Double intime = Double.valueOf(values[5]);
		Double outtime = Double.valueOf(values[6]);
		rc = outtime-intime;
		return rc;
	}
	/**
	 * This method finds the resource of the configuration file for a given method name.
	 * @param methodname
	 * @return
	 */
	private ModelEntity findResource(String resourcename) {
		ModelEntity rc = null;
		for (ModelEntity resource : resources) {
			if(resource.getName().equals(resourcename) || resource.getName().contains(resourcename)){
				rc = resource;
				break;
			}
		}
		return rc;
	}
	/**
	 * This method finds the WC of the configuration file for a given method name.
	 * @param methodname
	 * @return
	 */
	private ModelEntity findService(String methodname) {
		ModelEntity rc = null;
		for (ModelEntity service : services) {
			if(service.getName().equals(methodname) || service.getName().contains(methodname)){
				rc = service;
				break;
			}
		}
		return rc;
	}
	/**
	 * This method creates the trace key for a given method name out of another trace key.
	 * The method name defines the Service/WC.
	 * 
	 * @param methodname
	 * @param key
	 * @return
	 */
	public TraceKey mapServiceToTraceKey(String methodname, TraceKey key) {
		//find the WC
		ModelEntity entity = findService(methodname);
		if(entity== null){
			log.error("The entity could not be matched. Care about your librede file");
			throw new IllegalStateException("The entity could not be matched. Care about your librede file");
		}
		//create new key
		TraceKey newKey = new TraceKey(key.getMetric(), key.getUnit(), key.getInterval(), entity, key.getAggregation());
		return newKey;
	}
	/**
	 * This method forwards the listener notification
	 * @param event
	 */
	public void notifyListeners(TraceEvent event){
		super.notifyListeners(event);
	}
	/**
	 * This method gets the method name out of a operationexecution kieker trace file row.
	 * @param row
	 * @return
	 */
	public String getOperationExecutionMethodName(String[] row) {
		String[] tmp = row[2].split("\\(")[0].trim().split("\\s+");
		return tmp[tmp.length-1];
	}
	/**
	 * Returns the total cpu utilization, which is the second last value of a row.
	 * Converts the value to percentage
	 * @param row
	 * @return
	 */
	public Double parseTotalUtilizationOfCore(String[] row) {
		return ((100.0)*Double.valueOf(row[row.length-2]));
	}
	/**
	 * Parse the core id of a CPUUtilization record.
	 * @param row
	 * @return
	 */
	public Integer parseIdOfCore(String[] row) {
		return Integer.valueOf(row[4]);
	}
	/**
	 * Aggregate the CPU values of the cores to one value.
	 * 
	 * @param cpucorevalues
	 * @return
	 */
	public Double aggregateTotalCpuValue(Map<Integer, Double> cpucorevalues) {
		Double rc = null;
		if(cpucorevalues.size()==corenumber){
			rc = 0.0;
			for (Entry<Integer, Double> core : cpucorevalues.entrySet()) {
				rc+=core.getValue();
			}
			rc=(rc/Double.valueOf((double)corenumber));
		}
		return rc;
	}
	/**
	 * Aggregate the CPU timestamps of the cores to one value.
	 * @param cpucoretimestamps
	 * @return
	 */
	public Double agregateCpuTimestamp(Map<Integer, Double> cpucoretimestamps) {
		Double rc = null;
		if(cpucoretimestamps.size()==corenumber){
			rc = 0.0;
			for (Entry<Integer, Double> core : cpucoretimestamps.entrySet()) {
				rc+=core.getValue();
			}
			rc=(rc/Double.valueOf((double)corenumber));
		}
		return rc;
	}
	/**
	 * This method adds a resource with the given name to the trace key.
	 * 
	 * @param key
	 * @param resourcename
	 * @return
	 */
	public TraceKey mapResourceToTraceKey(TraceKey key, String resourcename) {
		//find the resource
		ModelEntity entity = findResource(resourcename);
		if(entity== null){
			log.error("The resource could not be matched. Care about your librede file");
			throw new IllegalStateException("The entity could not be matched. Care about your librede file");
		}
		//add it to the key
		TraceKey newKey = new TraceKey(key.getMetric(), key.getUnit(), key.getInterval(), entity, key.getAggregation());
		return newKey;
	}
	/**
	 * Parse the resource name out of a CPUUtilization record.
	 * @param row
	 * @return
	 */
	public String parseResource(String[] row) {
		return row[3];
	}
	/**
	 * Parse the timestamp out of a CPUUtilization record.
	 * @param row
	 * @return
	 */
	public Double parseCpuTimeStamp(String[] row) {
		return Double.valueOf(row[2]);
	}
	/**
	 * Parse the busy timeout of a CPUUtilization record.
	 * @param row
	 * @return
	 */
	public Double parseBusyTimeOfCore(String[] row) {
		return ((1.0)-Double.valueOf(row[row.length-1]));
	}
	public boolean isVisitsIntervalPassed(Double visitsLastTimeStampNanos, Double timestamp) {
		boolean rc = false;
		//get nanoseconds out of seconds interval
		Double intervalnanos = visitsinterval * 1000000000.0;
		//check if we are greater than the interval
		if((timestamp-visitsLastTimeStampNanos)>visitsinterval){
			rc = true;
		}
		return rc;
	}
	public Double increaseVisitsTimeStamp(Double visitsLastTimeStampNanos) {

		//get nanoseconds out of seconds interval
		Double intervalnanos = visitsinterval * 1000000000.0;
		return intervalnanos+visitsLastTimeStampNanos;
	}
	public Map<String, ArrayList<Double>> initializeVisitsMap() {
		Map<String, ArrayList<Double>> rc = new HashMap<>();
		for (ModelEntity modelEntity : services) {
			rc.put(modelEntity.getName(), new ArrayList<>());
		}
		return rc;
	}
	public Double getInterval() {
		// TODO Auto-generated method stub
		return Double.valueOf(visitsinterval);
	}

}
