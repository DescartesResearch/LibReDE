package tools.descartes.librede.datasource.kiekeramqp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import tools.descartes.librede.configuration.FileTraceConfiguration;
import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.TraceConfiguration;
import tools.descartes.librede.configuration.TraceToEntityMapping;
import tools.descartes.librede.datasource.AbstractDataSource;
import tools.descartes.librede.datasource.Loggers;
import tools.descartes.librede.datasource.Stream;
import tools.descartes.librede.datasource.TraceEvent;
import tools.descartes.librede.datasource.TraceKey;
import tools.descartes.librede.datasource.kieker.KiekerChannel;
import tools.descartes.librede.datasource.kieker.KiekerId;
import tools.descartes.librede.datasource.kieker.KiekerWatchThread;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.registry.ParameterDefinition;
import tools.descartes.librede.units.Dimension;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.UnitsFactory;

@Component(displayName = "Kieker Data Source Offline Traces")
public class KiekerDataSourceOffline extends AbstractDataSource{

	@ParameterDefinition(name = "TriggerCount", label = "Count of entries when we trigger trace events", required = false, defaultValue = "512")
	private int triggercount;
	
	@ParameterDefinition(name = "Corenumber", label = "Corenumber", required = false, defaultValue = "4")
	private int corenumber;
	
	@ParameterDefinition(name = "Aggregationinterval", label = "Aggregationinterval", required = false, defaultValue = "10")
	private int aggregationinterval;
	
	@ParameterDefinition(name = "Maximaltimestamp", label = "Max stamp, that the datasource uses", required = false, defaultValue = "-1")
	private long maxTimeStamp;
	
	@ParameterDefinition(name = "KiekerDirectory", label = "Filedirectory", required = false, defaultValue = "/home/torsten/Schreibtisch/jettytests/local/1l_600s_500t_ubuntu_visits/")
	private String filedirectory;
	/**
	 * The logging instance
	 */
	private final Logger log = Loggers.DATASOURCE_LOG;


	/**
	 * Map that maps metric and entity name to entity and tracekey
	 */
	private Map<Metric<?>, Map<String, Entry<ModelEntity, TraceKey>>> tracekeymap;
	/**
	 * the new channel that deals with our directory
	 */
	private KiekerChannelOffline channel;
	private File channeldirecotry;
	/**
	 * The constructor of the data source.
	 * 
	 * @param configuration
	 * @throws IOException
	 */
	public KiekerDataSourceOffline() throws IOException {
		this.tracekeymap = new HashMap<>();
	}
	
	/**
	 * Overridden Method of the IDataSource Interface.
	 * Here we read all the data that are currently available.
	 */
	@Override
	public void load() throws IOException {
		//read the data
		readFromChannel();
	}
	/**
	 * Add a configuration to the Trace List.
	 * In Kieker datasources we add a specific directory and a specific metric we want to observe.
	 */
	@Override
	public List<TraceKey> addTrace(TraceConfiguration configuration) throws IOException {

		//get the tracekey off one configuration
		List<TraceKey> keys = new LinkedList<TraceKey>();
		
		
		keys = addTraceKeys(configuration);
		//check if if is the first trace
		if(channeldirecotry==null){
			File inputDirectory = new File(filedirectory+"kieker/");
			//check if it is a directory
			if (inputDirectory.exists() && inputDirectory.isDirectory()) {
				channeldirecotry=inputDirectory;
				//Get the channel for this directory
				//Open new channel if necessary		
				if (this.channel == null) {
					channel = new KiekerChannelOffline(channeldirecotry, this, maxTimeStamp);
				}else{
					
				}
			} else {
				throw new FileNotFoundException(inputDirectory.toString() + "does not exist!");
			}
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
	 * Read from the given channel. 
	 * (This will publish data, if the buffer is full, or the file is at the end)
	 * @param channel
	 */
	private void readFromChannel() {
		if (!channel.read()) {
			// Channel was closed unexpectedly -> remove it from list
			try {
				channel.close();
			} catch (IOException e) {
				log.error("Error closing channel.");
			}
		}
	}
	/**
	 * This method closes the watch thread and therefore the datasource.
	 */
	public void close() throws IOException {
		if(channel!=null){
			channel.close();
		}
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
