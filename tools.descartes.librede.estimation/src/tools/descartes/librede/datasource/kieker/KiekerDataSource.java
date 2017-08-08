package tools.descartes.librede.datasource.kieker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.AbstractMap;
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
	
	@ParameterDefinition(name = "Aggregationinterval", label = "Aggregationinterval", required = false, defaultValue = "10")
	private int aggregationinterval;
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
	//private Set<ModelEntity> services;
	/**
	 * The libredeconfiguration to identify the resources we deal with
	 */
	//private Set<ModelEntity> resources;
	/**
	 * Map that maps metric and entity name to entity and tracekey
	 */
	private Map<Metric<?>, Map<String, Entry<ModelEntity, TraceKey>>> tracekeymap;
	/**
	 * The constructor of the data source.
	 * Here the KieckerWatchThread is initialized, but not started.
	 * 
	 * @param configuration
	 * @throws IOException
	 */
	public KiekerDataSource() throws IOException {
		this.tracekeymap = new HashMap<>();
		/*this.services = new HashSet<>();
		this.resources = new HashSet<>();*/
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
		//this is not needed anymore, because we always start a reading for all the channels
		//we watch...therefore, the estimation process HAS TO BE be started
		//after all the data classes are available
		//watchThread.poll();
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

		//get the tracekey off one configuration
		List<TraceKey> keys = new LinkedList<TraceKey>();
		
		//THIS IS EXTRA LOGIC FOR GETTING ALL THE SERVICES AND RESOURCES
		//FROM THE LIBREDE FILE INTO THE KIEKER DATA SOURCE
		//WE THEREFOR ADD ALL SERVICES TO THE TRACE MAPPINGS
		//AND EXTRACT THEM HERE.
		//only do it when we have a mapping and when did not do it before.
		/*if(configuration.getMetric().equals(StandardMetrics.RESPONSE_TIME)){
			if(!configuration.getMappings().isEmpty() && services.size()==0){
				for (TraceToEntityMapping mapping : configuration.getMappings()) {
					services.add(mapping.getEntity());
				}
			}
			keys=addTraceKeys(configuration);
		} else if(configuration.getMetric().equals(StandardMetrics.VISITS)){
			if(!configuration.getMappings().isEmpty() && services.size()==0){
				for (TraceToEntityMapping mapping : configuration.getMappings()) {
					services.add(mapping.getEntity());
				}
			}
			keys=addTraceKeys(configuration);
		} else if(configuration.getMetric().equals(StandardMetrics.THROUGHPUT)){
			if(!configuration.getMappings().isEmpty() && services.size()==0){
				for (TraceToEntityMapping mapping : configuration.getMappings()) {
					services.add(mapping.getEntity());
				}
			}
			keys=addTraceKeys(configuration);
		}
		else if(configuration.getMetric().equals(StandardMetrics.UTILIZATION)){
			if(!configuration.getMappings().isEmpty() && resources.size()==0){
				for (TraceToEntityMapping mapping : configuration.getMappings()) {
					resources.add(mapping.getEntity());
				}
			}
			keys=addTraceKeys(configuration);
		}
		else if(configuration.getMetric().equals(StandardMetrics.BUSY_TIME)){
			if(!configuration.getMappings().isEmpty() && resources.size()==0){
				for (TraceToEntityMapping mapping : configuration.getMappings()) {
					resources.add(mapping.getEntity());
				}
			}
			keys=addTraceKeys(configuration);
		}*/
		keys = addTraceKeys(configuration);
		
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
		//map the metric to the kiekerId we need to observe
		KiekerId kiekerId = mapKiekerId(configuration.getMetric());
		//tell the channel to observe the metric of this tracekey and which kiekerid is necessary for that task
		channel.addTrace(configuration.getMetric(),kiekerId);
		return keys;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<TraceKey> addTraceKeys(TraceConfiguration configuration) {
		List<TraceKey> traceKeys = new ArrayList<>();
		for (TraceToEntityMapping modelEntity : configuration.getMappings()) {
			if(!tracekeymap.containsKey(configuration.getMetric())){
				tracekeymap.put(configuration.getMetric(), new HashMap<String,Entry<ModelEntity, TraceKey>>());
			}
			Map<String, Entry<ModelEntity, TraceKey>> innermap = tracekeymap.get(configuration.getMetric());
			if(!innermap.containsKey(modelEntity.getEntity().getName())){
				TraceKey newTraceKey = new TraceKey(configuration.getMetric(), configuration.getUnit(), configuration.getInterval(), modelEntity.getEntity(), configuration.getAggregation());
				Entry<ModelEntity, TraceKey> entry = new AbstractMap.SimpleEntry(modelEntity.getEntity(), newTraceKey);
				innermap.put(modelEntity.getEntity().getName(), entry);
				traceKeys.add(newTraceKey);
				//notify the listeners
				notifyListenersNewKey(newTraceKey);
			}
		}
		return traceKeys;
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
		} else if(metric.equals(StandardMetrics.DEPARTURES)){
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
	public double parseTimeStamp(String[] line) {
		return Double.valueOf(line[1]);
	}
	/**
	 * This method gets the response time timestamp out of a kieker trace row.
	 * This is the start timestamp of the method.
	 * 
	 * @param strings
	 * @return
	 */
	public double parseOperationExecutionStartTimeStamp(String[] line) {
		return Double.valueOf(line[5]);
	}
	
	/**
	 * This method gets the response time out of a kieker trace row.
	 * 
	 * @param values
	 * @param key
	 * @return
	 */
	public double parseOperationExecutionResponseTime(String[] line) {
		Double rc = -1.0;
		Double intime = Double.valueOf(line[5]);
		Double outtime = Double.valueOf(line[6]);
		rc = outtime-intime;
		return rc;
	}
	/**
	 * This method finds the resource of the configuration file for a given method name.
	 * @param methodname
	 * @return
	 */
	/*private ModelEntity findResource(String resourcename) {
		ModelEntity rc = null;
		for (ModelEntity resource : resources) {
			if(resource.getName().equals(resourcename) || resource.getName().contains(resourcename)){
				rc = resource;
				break;
			}
		}
		return rc;
		
	}*/
	/**
	 * This method finds the WC of the configuration file for a given method name.
	 * @param methodname
	 * @return
	 */
	/*private ModelEntity findService(String methodname) {
		ModelEntity rc = null;
		for (ModelEntity service : services) {
			if(service.getName().equals(methodname) || service.getName().contains(methodname)){
				rc = service;
				break;
			}
		}
		return rc;
	}*/
	/**
	 * This method creates the trace key for a given method name out of another trace key.
	 * The method name defines the Service/WC.
	 * 
	 * @param methodname
	 * @param key
	 * @return
	 */
	public TraceKey mapEntityToTraceKey(String entityname, Metric<?> metric) {
		if(!tracekeymap.containsKey(metric)){
			return null;
		}
		if(!tracekeymap.get(metric).containsKey(entityname)){
			return null;
		}
		TraceKey rc = tracekeymap.get(metric).get(entityname).getValue();
		return rc;
		//find the WC
		/*ModelEntity entity = findService(methodname);
		if(entity== null){
			log.error("The entity could not be matched. Care about your librede file");
			throw new IllegalStateException("The entity could not be matched. Care about your librede file");
		}
		//create new key
		TraceKey newKey = new TraceKey(key.getMetric(), key.getUnit(), key.getInterval(), entity, key.getAggregation());
		return newKey;*/
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
	public String getOperationExecutionMethodName(String[] line) {
		String[] tmp = line[2].split("\\(")[0].trim().split("\\s+");
		return tmp[tmp.length-1];
	}
	/**
	 * Returns the total cpu utilization, which is the second last value of a row.
	 * Converts the value to percentage
	 * @param row
	 * @return
	 */
	public Double parseCpuTotalUtilizationOfCore(String[] line) {
		return ((100.0)*Double.valueOf(line[line.length-2]));
	}
	/**
	 * Parse the core id of a CPUUtilization record.
	 * @param row
	 * @return
	 */
	public Integer parseCpuIdOfCore(String[] line) {
		return Integer.valueOf(line[4]);
	}
	/**
	 * Aggregate the CPU values of the cores to one value.
	 * 
	 * @param cpucorevalues
	 * @return
	 */
	public Double aggregateCpuCoreValues(Map<Integer, Double> cpucorevalues) {
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
	/*public Double agregateCpuTimestamp(Map<Integer, Double> cpucoretimestamps) {
		Double rc = null;
		if(cpucoretimestamps.size()==corenumber){
			rc = 0.0;
			for (Entry<Integer, Double> core : cpucoretimestamps.entrySet()) {
				rc+=core.getValue();
			}
			rc=(rc/Double.valueOf((double)corenumber));
		}
		return rc;
	}*
	/**
	 * This method adds a resource with the given name to the trace key.
	 * 
	 * @param key
	 * @param resourcename
	 * @return
	 */
	/*public TraceKey mapResourceToTraceKey(TraceKey key, String resourcename) {
		TraceKey rc = tracekeymap.get(key.getMetric()).get(resourcename).getValue();
		return rc;
		//find the resource
		/*ModelEntity entity = findResource(resourcename);
		if(entity== null){
			log.error("The resource could not be matched. Care about your librede file");
			throw new IllegalStateException("The entity could not be matched. Care about your librede file");
		}
		//add it to the key
		TraceKey newKey = new TraceKey(key.getMetric(), key.getUnit(), key.getInterval(), entity, key.getAggregation());
		return newKey;
	}*/
	/**
	 * Parse the resource name out of a CPUUtilization record.
	 * @param row
	 * @return
	 */
	public String parseCpuResourceName(String[] line) {
		return line[3];
	}
	/**
	 * Parse the timestamp out of a CPUUtilization record.
	 * @param row
	 * @return
	 */
	public Double parseCpuTimeStamp(String[] line) {
		return Double.valueOf(line[2]);
	}
	/**
	 * Parse the busy timeout of a CPUUtilization record.
	 * @param row
	 * @return
	 */
	public Double parseCpuCoreBusyTime(String[] line) {
		return ((1.0)-Double.valueOf(line[line.length-1]));
	}
	public boolean isAggregationIntervalPassed(Double visitsLastTimeStampNanos, Double timestamp) {
		boolean rc = false;
		//get nanoseconds out of seconds interval
		Double intervalnanos = aggregationinterval * 1000000000.0;
		//check if we are greater than the interval
		if((timestamp-visitsLastTimeStampNanos)>intervalnanos){
			rc = true;
		}
		return rc;
	}
	public Double increaseAggregationTimeStamp(Double visitsLastTimeStampNanos) {

		//get nanoseconds out of seconds interval
		Double intervalnanos = aggregationinterval * 1000000000.0;
		return intervalnanos+visitsLastTimeStampNanos;
	}
	public Map<String, ArrayList<Double>> initializeEntityMap(Metric<?>metric) {
		if(!tracekeymap.containsKey(metric)){
			return null;
		}
		Map<String, ArrayList<Double>> rc = new HashMap<>();
		for (Entry<String, Entry<ModelEntity, TraceKey>> entry : tracekeymap.get(metric).entrySet()) {
			rc.put(entry.getKey(), new ArrayList<Double>());
		}
		return rc;
		
		/*Map<String, ArrayList<Double>> rc = new HashMap<>();
		for (ModelEntity modelEntity : services) {
			rc.put(modelEntity.getName(), new ArrayList<>());
		}
		return rc;*/
	}
	public Double getAggregationInterval() {
		// TODO Auto-generated method stub
		return Double.valueOf(aggregationinterval);
	}
	public boolean isEntityAvailable(String entityname, Metric<?> metric) {
		if(tracekeymap.get(metric)!=null){
			if(tracekeymap.get(metric).get(entityname)!=null){
				return true;
			}
		}
		return false;
	}

}
