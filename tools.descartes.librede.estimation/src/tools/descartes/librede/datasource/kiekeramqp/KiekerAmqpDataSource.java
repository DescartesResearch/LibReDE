package tools.descartes.librede.datasource.kiekeramqp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.reader.amqp.AMQPReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.system.CPUUtilizationRecord;
import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.TraceConfiguration;
import tools.descartes.librede.configuration.TraceToEntityMapping;
import tools.descartes.librede.datasource.AbstractDataSource;
import tools.descartes.librede.datasource.Loggers;
import tools.descartes.librede.datasource.TraceEvent;
import tools.descartes.librede.datasource.TraceKey;
import tools.descartes.librede.datasource.kieker.KiekerId;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorBuilder;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.registry.ParameterDefinition;
import tools.descartes.librede.repository.TimeSeries;
import tools.descartes.librede.units.Dimension;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;
import tools.descartes.librede.units.UnitsFactory;


@Component(displayName = "Kieker AMQP Data Source")
public class KiekerAmqpDataSource extends AbstractDataSource {

	@ParameterDefinition(name = "Corenumber", label = "Corenumber", required = false, defaultValue = "16")
	private int corenumber;
	
	@ParameterDefinition(name = "Aggregationinterval", label = "Aggregationinterval", required = false, defaultValue = "10")
	private int aggregationinterval;

	@ParameterDefinition(name = "AMQPserveruri", label = "AMQPserveruri", required = false, defaultValue = "amqp://kieker:descartes@10.0.1.148:5672/kieker")
	private String uri;
	
	@ParameterDefinition(name = "AMQP queuename", label = "AMQPqueuename", required = false, defaultValue = "kiekerqueue")
	private String queueName;
	
	@ParameterDefinition(name = "Min Count", label = "Minimum entry count for triggering trace event.", required = false, defaultValue = "100")
	private int mincount;
	
	@ParameterDefinition(name = "Max Time (Sec)", label = "Maximum elapsed time before we try to trigger events.", required = false, defaultValue = "20")
	private int maxtimesec;
	
	@ParameterDefinition(name = "Trigger Count", label = "Count of entries when we trigger trace events", required = false, defaultValue = "512")
	private int triggercount;
	
	@ParameterDefinition(name = "Wait time (Sec)", label = "Time we wait if we did not have enough entries yet", required = false, defaultValue = "2")
	private int wait;

	/**
	 * The logging instance
	 */
	private final Logger log = Loggers.DATASOURCE_LOG;
	/**
	 * Zero instance
	 */
	private final Quantity<Time> ZERO = UnitsFactory.eINSTANCE.createQuantity(0, Time.SECONDS);
	/**
	 * Map that maps metric and entity name to entity and tracekey
	 */
	private Map<Metric<?>, Map<String, Entry<ModelEntity, TraceKey>>> tracekeymap;
	/**
	 * A mapping from Trace Key to the Kieker Recordtype, that is necessary for it.
	 */
	private Map<Metric<?>, Class<?>> traces = new HashMap<Metric<?>, Class<?>>();
	/**
	 * The map that is used to aggregate the cpu utilization.
	 * The first value is the resource name the second one the core id the third one the utilization
	 */
	private Map<String, Map<Integer, Double>> utilizationcorevaluesperresource;
	/**
	 * The map that is used to aggregate the timestamps of the cpu utilization.
	 * The first value is the resource name the second one the core id the third one the timestamp
	 */
	private Map<String,Map<Integer, Double>> utilizationcoretimestampsperresource;
	/**
	 * The map that is used to aggregate the cpu busytime.
	 * The first value is the resource name the second one the core id the third one the busytime
	 */
	private Map<String, Map<Integer, Double>> busytimecorevaluesperresource;
	/**
	 * The map that is used to aggregate the timestamps of the cpu busytime.
	 * The first value is the resource name the second one the core id the third one the timestamp
	 */
	private Map<String,Map<Integer, Double>> busytimecoretimestampsperresource;
	/**
	 * Create a map that holds the different methods and the entry timestamp for visits
	 */
	private Map<String, ArrayList<Double>> visitsMethodToTimeStampList;
	/**
	 * The last timestamp, where we pushed the visits
	 */
	private Double visitsLastTimeStampNanos;
	/**
	 * Create a map that holds the different methods and the entry timestamp for visits
	 */
	private Map<String, ArrayList<Double>> departuresMethodToTimeStampList;
	/**
	 * The last timestamp, where we pushed the visits
	 */
	private Double departuresLastTimeStampNanos;
	/**
	 * Create a map that holds the different methods and the entry timestamp for throughput calculations
	 */
	private Map<String, ArrayList<Double>> throughputMethodToTimeStampList;
	/**
	 * The last timestamp, where we pushed the throughput
	 */
	private Double throughputLastTimeStampNanos;

	/**
	 * The newest time that the channel found data.
	 */
	private Quantity<Time> channelCurrentTime = ZERO;
	
	private KiekerAnalysisThread analysisThread;
	
	
	public KiekerAmqpDataSource() throws IOException{
		tracekeymap = new HashMap<>();
	}
	
	@Override
	public List<TraceKey> addTrace(TraceConfiguration configuration) throws IOException {
		//get the tracekey off one configuration
		List<TraceKey> keys = new LinkedList<TraceKey>();
		//add all the tracekeys, that this configuration brings up to local map
		keys = addTraceKeys(configuration);
		//get the metric 
		Metric<?> metric = configuration.getMetric();
		//map the metric to the kiekerId we need to observe
		Class<?> recordclass = mapMetricToMonitoringRecord(metric);
		//remember to observe the metric of this configuration and which kiekerid is necessary for that task
		synchronized(traces) {
			//fill the list
			if(!traces.containsKey(metric)){
				traces.put(metric, recordclass);
				//initialize all the necessary maps
				if(metric.equals(StandardMetrics.UTILIZATION)){
					this.utilizationcorevaluesperresource = new HashMap<>();
					this.utilizationcoretimestampsperresource = new HashMap<>();
				} else if(metric.equals(StandardMetrics.BUSY_TIME)){
					this.busytimecorevaluesperresource = new HashMap<>();
					this.busytimecoretimestampsperresource = new HashMap<>();
				} else if(metric.equals(StandardMetrics.VISITS)){
					this.visitsMethodToTimeStampList = initializeEntityMap(StandardMetrics.VISITS);
					this.visitsLastTimeStampNanos = null;
				} else if(metric.equals(StandardMetrics.THROUGHPUT)){
					this.throughputMethodToTimeStampList = initializeEntityMap(StandardMetrics.THROUGHPUT);
					this.throughputLastTimeStampNanos = null;
				} else if(metric.equals(StandardMetrics.DEPARTURES)){
					this.departuresMethodToTimeStampList = initializeEntityMap(StandardMetrics.DEPARTURES);
					this.departuresLastTimeStampNanos = null;
				}
			}
		}
		return keys;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<TraceKey> addTraceKeys(TraceConfiguration configuration) {
		List<TraceKey> traceKeys = new ArrayList<>();
		for (TraceToEntityMapping modelEntity : configuration.getMappings()) {
			if(!tracekeymap.containsKey(configuration.getMetric())){
				tracekeymap.put(configuration.getMetric(), new HashMap<String, Entry<ModelEntity,TraceKey>>());
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

	private Map<String, ArrayList<Double>> initializeEntityMap(Metric<?>metric) {
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
	/**
	 * Maps the metric of the libede traceconfiguration to a kieker id enum.
	 * Here you can find all the metrics, that are currently supported.
	 * 
	 * @param metric
	 * @return
	 */
	private Class<?> mapMetricToMonitoringRecord(Metric<? extends Dimension> metric) {
		Class<?> rc = null;
		if(metric.equals(StandardMetrics.UTILIZATION)){
			rc = CPUUtilizationRecord.class;
		} else if(metric.equals(StandardMetrics.RESPONSE_TIME)){
			rc = OperationExecutionRecord.class;
		} else if(metric.equals(StandardMetrics.VISITS)){
			rc = OperationExecutionRecord.class;
		} else if(metric.equals(StandardMetrics.DEPARTURES)){
			rc = OperationExecutionRecord.class;
		} else if(metric.equals(StandardMetrics.THROUGHPUT)){
			rc = OperationExecutionRecord.class;
		} else if(metric.equals(StandardMetrics.BUSY_TIME)){
			rc = CPUUtilizationRecord.class;
		}
		return rc;
	}
	@Override
	public void load() {
		this.analysisThread = new KiekerAnalysisThread(this, 
				uri, queueName, triggercount, mincount, maxtimesec, wait);
		this.analysisThread.start();
	}

	public void notifySelector(ArrayList<IMonitoringRecord> records){
		//for all traces means in this case for all metrics we want to collect data for
		for (Entry<Metric<?>, Class<?>> trace : traces.entrySet()) {
			//create a map, in which we will find the data to publish
			Map<TraceKey, TimeSeries> finalSeries;
			//get trace event data we want to publish
			finalSeries = getTraceEventData(trace.getValue(), trace.getKey(), records);
			//for all the traces in the final set
			for (Entry<TraceKey, TimeSeries> serie : finalSeries.entrySet()) {
				if (!serie.getValue().isEmpty()) {
					// update current time to the maximum observation timestamp
					if (channelCurrentTime == null || serie.getValue().getEndTime() > channelCurrentTime.getValue(Time.SECONDS)) {
						channelCurrentTime = UnitsFactory.eINSTANCE.createQuantity(serie.getValue().getEndTime(), Time.SECONDS);
					}
					//create the trace event
					TraceEvent event = new TraceEvent(serie.getKey(), serie.getValue(), channelCurrentTime);
					//notify the listeners and therefor publish it
					this.notifyListeners(event);
				}
			}
		}
	}
	
	private Map<TraceKey, TimeSeries> getTraceEventData(Class<?> recordtype, Metric<?> metric,
			ArrayList<IMonitoringRecord> records) {
		//IF WE HAVE A RESPONSE TIME TO CALCUALTE
				if(metric.equals(StandardMetrics.RESPONSE_TIME)){
					return getResponseTimeSeries(recordtype, metric, records);
				} 
				//IF WE HAVE VISITS TO CALCUALTE
				else if(metric.equals(StandardMetrics.VISITS)){
					return getVisitsTimeSeries(recordtype, metric, records);
				} 
				//IF WE HAVE DEPARTURES TO CALCUALTE
				else if(metric.equals(StandardMetrics.DEPARTURES)){
					return getDeparturesTimeSeries(recordtype, metric, records);
				} 
				//IF WE HAVE THROUGHTPUT TO CALCUALTE
				else if(metric.equals(StandardMetrics.THROUGHPUT)){
					return getThroughputTimeSeries(recordtype, metric, records);
				} 
				//IF WE HAVE THE BUSY_TIME TO CALCULATE
				else if(metric.equals(StandardMetrics.BUSY_TIME)){
					return getBusyTimeTimeSeries(recordtype, metric, records);
				} 
				//IF WE HAVE THE UTILIZATION TO CALCULATE
				else if(metric.equals(StandardMetrics.UTILIZATION)){
					return getUtilizationTimeSeries(recordtype, metric, records);
				} 
				//IF WE WANT TO CALCULATE OTHER METRICS DO IT HERE
				else{
					//other metrics
					return new HashMap<>();
				}
	}

	private Map<TraceKey, TimeSeries> getUtilizationTimeSeries(Class<?> recordtype, Metric<?> metric,
			ArrayList<IMonitoringRecord> records) {
		Map<TraceKey, TimeSeries> finalSeries = new HashMap<>();
		//map for the utilization for each resource in local tmp maps
		Map<String, ArrayList<Double>> values = new HashMap<>();
		//map for the timestamps for each resource in local tmp maps
		Map<String, ArrayList<Double>> timestamps = new HashMap<>();
		//foreach line in the buffer
		for (int i=0; i<records.size(); ++i) {
			//check if we have to deal with this row due to id
			if(records.get(i).getClass() == recordtype){
				//get the actual row
				CPUUtilizationRecord cpuUtilizationRecord = (CPUUtilizationRecord)records.get(i);
				//get the cpu core utilization value in percentage
				Double coreutilization = cpuUtilizationRecord.getTotalUtilization();
				//get the cpu core id value
				Integer coreid = Integer.valueOf(cpuUtilizationRecord.getCpuID());
				//get the timestamp
				Double coretimestamp = Double.valueOf((double)cpuUtilizationRecord.getTimestamp());
				//get the timestamp
				String coreresource = cpuUtilizationRecord.getHostname();
				//search for the resource name in the class map
				if(!utilizationcorevaluesperresource.containsKey(coreresource)){
					//if the resource is new then add a new entry
					utilizationcorevaluesperresource.put(coreresource, new HashMap<Integer,Double>());
					utilizationcoretimestampsperresource.put(coreresource, new HashMap<Integer,Double>());
				}
				//add the values to the cpu array and add a trace to final series if we have all the data of all cores
				//check if we are in a new iteration of the cpu data due to the coreid
				if(utilizationcorevaluesperresource.get(coreresource).containsKey(coreid)){
					//clean out the arrays because we have a new iteration
					utilizationcorevaluesperresource.put(coreresource, new HashMap<Integer,Double>());
					utilizationcoretimestampsperresource.put(coreresource, new HashMap<Integer, Double>());
				}
				//get the right map for the actual resource name
				Map<Integer, Double> cpucorevalues = utilizationcorevaluesperresource.get(coreresource);
				Map<Integer, Double> cpucoretimestamps = utilizationcoretimestampsperresource.get(coreresource);
				//add the values for the core id
				cpucorevalues.put(coreid, coreutilization);
				cpucoretimestamps.put(coreid, coretimestamp);
				//aggregate all the cores to one value
				Double utilization = aggregateCpuCoreValues(cpucorevalues);
				//if we have all cores and got a value
				if(utilization != null){
					//do the same for the timestamps
					Double timestamp = aggregateCpuCoreValues(cpucoretimestamps);
					if(timestamp != null){
						//add it to the tmp list values we want to push later
						//check if we already have data for this resource
						if(!values.containsKey(coreresource)){
							//add the resource
							values.put(coreresource, new ArrayList<Double>());
							timestamps.put(coreresource, new ArrayList<Double>());
						}
						//add the values
						values.get(coreresource).add(utilization);
						timestamps.get(coreresource).add(timestamp);
					}//for safty
				}//we do not have all the coreinfos
			}//it is not the right kiekerid...skip the line
		}//get the next row
		//publish the data
		//foreach resource
		for (Entry<String, ArrayList<Double>> resource : values.entrySet()) {
			//for debugging use save all the data to a csv file
			saveToCsv(metric, resource.getKey(), resource.getValue(),timestamps.get(resource.getKey()));
			//create the key with the right entity
			TraceKey traceKey = mapEntityToTraceKey(resource.getKey(),metric);
			//create the time series
			TimeSeries timeSeries = createTimeSeries(resource.getValue(), timestamps.get(resource.getKey()));
			//add it to the final set
			finalSeries.put(traceKey, timeSeries);
		}
		//return the finalseries.
		return finalSeries;
	}

	private Map<TraceKey, TimeSeries> getBusyTimeTimeSeries(Class<?> recordtype, Metric<?> metric,
			ArrayList<IMonitoringRecord> records) {
		Map<TraceKey, TimeSeries> finalSeries = new HashMap<>();
		//map for the data for each resource
		Map<String, ArrayList<Double>> values = new HashMap<String, ArrayList<Double>>();
		//map for the timestamps for each resource
		Map<String, ArrayList<Double>> timestamps = new HashMap<String, ArrayList<Double>>();
		//foreach line in the buffer
		for (int i=0; i<records.size(); ++i) {
			//check if we have to deal with this row due to id
			if(records.get(i).getClass() == recordtype){
				//get the actual row
				CPUUtilizationRecord cpuUtilizationRecord = (CPUUtilizationRecord)records.get(i);
				//get the cpu core utilization value
				Double corebusytime = 1.0-cpuUtilizationRecord.getIdle();
				//get the cpu core id value
				Integer coreid = Integer.valueOf(cpuUtilizationRecord.getCpuID());
				//get the timestamp
				Double coretimestamp = Double.valueOf((double)cpuUtilizationRecord.getTimestamp());
				//get the timestamp
				String coreresource = cpuUtilizationRecord.getHostname();
				if(!busytimecorevaluesperresource.containsKey(coreresource)){
					busytimecorevaluesperresource.put(coreresource, new HashMap<Integer,Double>());
					busytimecoretimestampsperresource.put(coreresource, new HashMap<Integer,Double>());
				}
				//add it to the cpu array and add a trace to final series if we have all the data of all cores
				if(busytimecorevaluesperresource.get(coreresource).containsKey(coreid)){
					//clean out the arrays because we have a new iteration
					busytimecorevaluesperresource.put(coreresource, new HashMap<Integer,Double>());
					busytimecoretimestampsperresource.put(coreresource, new HashMap<Integer,Double>());
				}

				Map<Integer, Double> busytimecorevalues = busytimecorevaluesperresource.get(coreresource);
				Map<Integer, Double> busytimecoretimestamps = busytimecoretimestampsperresource.get(coreresource);
				busytimecorevalues.put(coreid, corebusytime);
				busytimecoretimestamps.put(coreid, coretimestamp);
				//aggregate all the cores to one value
				Double busytime = aggregateCpuCoreValues(busytimecorevalues);
				//if we have all cores and got a value
				if(busytime != null){
					//do the same for the timestamps
					Double timestamp = aggregateCpuCoreValues(busytimecoretimestamps);
					if(timestamp != null){
						//add it to the values we want to push later
						if(!values.containsKey(coreresource)){
							values.put(coreresource, new ArrayList<Double>());
							timestamps.put(coreresource, new ArrayList<Double>());
						}
						values.get(coreresource).add(busytime);
						timestamps.get(coreresource).add(timestamp);
					}
				}
			}
		}
		//publish the data
		//foreach resource
		for (Entry<String, ArrayList<Double>> resource : values.entrySet()) {
			//for debugging use save all the data to a csv file
			saveToCsv(metric, resource.getKey(), resource.getValue(),timestamps.get(resource.getKey()));
			//create the key with the right entity
			TraceKey traceKey = mapEntityToTraceKey(resource.getKey(), metric);
			//create the time series
			TimeSeries timeSeries = createTimeSeries(resource.getValue(), timestamps.get(resource.getKey()));
			//add it to the final set
			finalSeries.put(traceKey, timeSeries);
		}
		return finalSeries;
	}

	private Map<TraceKey, TimeSeries> getThroughputTimeSeries(Class<?> recordtype, Metric<?> metric,
			ArrayList<IMonitoringRecord> records) {
		Map<TraceKey, TimeSeries> finalSeries = new HashMap<>();
		//Create a map that holds the different methods and the troughput for this method
		Map<String, ArrayList<Double>> methodToValuesList = new HashMap<>();
		//Create a map that holds the different methods and the timestamp for this method
		Map<String, ArrayList<Double>> methodToTimeStampList = new HashMap<>();
		//foreach line in the buffer
		for (int i=0; i<records.size(); ++i) {
			//check if we have to deal with this row due to id
			if(records.get(i).getClass() == recordtype){
				//get the actual row
				OperationExecutionRecord operationExecutionRecord = (OperationExecutionRecord)records.get(i);
				//get the method name which is the WC
				String methodname = parseOperationExecutionMethodName(operationExecutionRecord.getOperationSignature());
				//check if we have to deal with this WC
				if(isEntityAvailable(methodname, metric)){
					//get the response time and the timestamp
					Double timestamp = Double.valueOf((double)operationExecutionRecord.getTin());
					//check map entry is there for safety
					if(!throughputMethodToTimeStampList.containsKey(methodname)){
						//this map is initialized in the constructor to always have all the 
						//WCs there to be able to report 0 visits.
						throw new IllegalStateException("The WC does not exist, but has to exist");
					}
					//get the WC entry and add the visit timestamp from the class map
					throughputMethodToTimeStampList.get(methodname).add(timestamp);
					//set the first timestamp as start timestamp
					if(throughputLastTimeStampNanos==null){
						throughputLastTimeStampNanos = timestamp;
					}
					//check if we have a interval finished due to the class timestamp and the new timestamp
					if(isAggregationIntervalPassed(throughputLastTimeStampNanos, timestamp)){
						//get the visits values out of visitsMethodToTimeStampList
						//for all the intervals, that have passed - perhaps two intervals have been passed.
						while(timestamp>throughputLastTimeStampNanos){
							//increase the last interval timestamp of the class value
							throughputLastTimeStampNanos = increaseAggregationTimeStamp(throughputLastTimeStampNanos);
							//foreach WC in the class map
							for (Entry<String, ArrayList<Double>> wc : throughputMethodToTimeStampList.entrySet()) {
								//get the visits of this wc until the new visitsLastTimeStampNanos timestamp
								int visits = 0;
								for (Double ts : wc.getValue()) {
									//count
									if(ts<throughputLastTimeStampNanos){
										visits++;
									}else{
										//stop counting, because we are in the next interval here
										break;
									}
								}
								//remove the counted entries from list, because we don't want to count them twice
								for (int j=0; j<visits; ++j) {
									//remove visits times the first element, which is the smalest.
									wc.getValue().remove(0);
								}
								//add the results to the local tmp lists
								if(!methodToValuesList.containsKey(wc.getKey())){
									methodToValuesList.put(wc.getKey(), new ArrayList<Double>());
									methodToTimeStampList.put(wc.getKey(), new ArrayList<Double>());
								}
								methodToValuesList.get(wc.getKey()).add((Double.valueOf((double)visits))/(aggregationinterval));
								methodToTimeStampList.get(wc.getKey()).add(throughputLastTimeStampNanos);
								//check the next WC
							}
							//check if we have processed all data up to the actual time stamp of this new line
						}
					}//interval has not been passed, therefore do nothing
				}//we do not have this entity
			}//it was a wrong kiekerid goto next line
		}//all lines passed
		//for each WC that was handled within this buffer rows. traverse the local tmp lists
		for (Entry<String, ArrayList<Double>> wc : methodToValuesList.entrySet()) {
			//for debugging use save all the data to a csv file
			saveToCsv(metric, wc.getKey(), wc.getValue(),methodToTimeStampList.get(wc.getKey()));
			//find the corresponding service and create the key with the right entity
			TraceKey traceKey = mapEntityToTraceKey(wc.getKey(), metric);
			//create the time series
			TimeSeries timeSeries = createTimeSeries(wc.getValue(), methodToTimeStampList.get(wc.getKey()));
			//add it to the final set
			finalSeries.put(traceKey, timeSeries);
			//System.out.println("We have data for "+wc.getKey());
		}
		//return this series.
		return finalSeries;
	}

	private Map<TraceKey, TimeSeries> getDeparturesTimeSeries(Class<?> recordtype, Metric<?> metric,
			ArrayList<IMonitoringRecord> records) {
		Map<TraceKey, TimeSeries> finalSeries = new HashMap<>();
		//Create a map that holds the different methods and the departures WHICH WILL BE THE VISITS IN THIS CASE for this method
		Map<String, ArrayList<Double>> methodToValuesList = new HashMap<>();
		//Create a map that holds the different methods and the timestamps for this method
		Map<String, ArrayList<Double>> methodToTimeStampList = new HashMap<>();
		//foreach line in the buffer
		for (int i=0; i<records.size(); ++i) {
			//check if we have to deal with this row due to id
			if(records.get(i).getClass() == recordtype){
				//get the actual row
				OperationExecutionRecord operationExecutionRecord = (OperationExecutionRecord)records.get(i);
				//get the method name which is the WC
				String methodname = parseOperationExecutionMethodName(operationExecutionRecord.getOperationSignature());
				//check if we have to deal with this WC
				if(isEntityAvailable(methodname, metric)){
					//get the response time and the timestamp
					Double timestamp = Double.valueOf((double)operationExecutionRecord.getTin());
					//check map entry is there for safety
					if(!departuresMethodToTimeStampList.containsKey(methodname)){
						//this map is initialized in the constructor to always have all the 
						//WCs there to be able to report 0 visits.
						throw new IllegalStateException("The WC does not exist, but has to exist");
					}
					//get the WC entry and add the visit timestamp from the class map
					departuresMethodToTimeStampList.get(methodname).add(timestamp);
					//set the first timestamp as start timestamp
					if(departuresLastTimeStampNanos==null){
						departuresLastTimeStampNanos = timestamp;
					}
					//check if we have a interval finished due to the class timestamp and the new timestamp
					if(isAggregationIntervalPassed(departuresLastTimeStampNanos, timestamp)){
						//get the visits values out of visitsMethodToTimeStampList
						//for all the intervals, that have passed - perhaps two intervals have been passed.
						while(timestamp>departuresLastTimeStampNanos){
							//increase the last interval timestamp of the class value
							departuresLastTimeStampNanos = increaseAggregationTimeStamp(departuresLastTimeStampNanos);
							//foreach WC in the class map
							for (Entry<String, ArrayList<Double>> wc : departuresMethodToTimeStampList.entrySet()) {
								//get the visits of this wc until the new visitsLastTimeStampNanos timestamp
								int visits = 0;
								for (Double ts : wc.getValue()) {
									//count
									if(ts<departuresLastTimeStampNanos){
										visits++;
									}else{
										//stop counting, because we are in the next interval here
										break;
									}
								}
								//leave out the 0 values
								if(visits!=0){
									//remove the counted entries from list, because we don't want to count them twice
									for (int j=0; j<visits; ++j) {
										//remove visits times the first element, which is the smalest.
										wc.getValue().remove(0);
									}
									//add the results to the local tmp lists
									if(!methodToValuesList.containsKey(wc.getKey())){
										methodToValuesList.put(wc.getKey(), new ArrayList<Double>());
										methodToTimeStampList.put(wc.getKey(), new ArrayList<Double>());
									}
									methodToValuesList.get(wc.getKey()).add(Double.valueOf((double)visits));
									methodToTimeStampList.get(wc.getKey()).add(departuresLastTimeStampNanos);
									//check the next WC
								}
							}
							//check if we have processed all data up to the actual time stamp of this new line
						}
					}//interval has not been passed, therefore do nothing
				}//we do not have this entity
			}//it was a wrong kiekerid goto next line
		}//all lines passed
		//for each WC that was handled within this buffer rows. traverse the local tmp lists
		for (Entry<String, ArrayList<Double>> wc : methodToValuesList.entrySet()) {
			//for debugging use save all the data to a csv file
			saveToCsv(metric, wc.getKey(), wc.getValue(),methodToTimeStampList.get(wc.getKey()));
			//find the corresponding service and create the key with the right entity
			TraceKey traceKey = mapEntityToTraceKey(wc.getKey(), metric);
			//create the time series
			TimeSeries timeSeries = createTimeSeries(wc.getValue(), methodToTimeStampList.get(wc.getKey()));
			//add it to the final set
			finalSeries.put(traceKey, timeSeries);
		}
		//return this series.
		return finalSeries;
	}

	private Map<TraceKey, TimeSeries> getVisitsTimeSeries(Class<?> recordtype, Metric<?> metric,
			ArrayList<IMonitoringRecord> records) {
		Map<TraceKey, TimeSeries> finalSeries = new HashMap<>();
		//Create a map that holds the different methods and the visits for this method
		Map<String, ArrayList<Double>> methodToValuesList = new HashMap<>();
		//Create a map that holds the different methods and the timestamps for this method
		Map<String, ArrayList<Double>> methodToTimeStampList = new HashMap<>();
		//foreach line in the buffer
		for (int i=0; i<records.size(); ++i) {
			//check if we have to deal with this row due to id
			if(records.get(i).getClass() == recordtype){
				//get the actual row
				OperationExecutionRecord operationExecutionRecord = (OperationExecutionRecord)records.get(i);
				//get the method name which is the WC
				String methodname = parseOperationExecutionMethodName(operationExecutionRecord.getOperationSignature());
				//check if we have to deal with this WC
				if(isEntityAvailable(methodname, metric)){
					//get the response time and the timestamp
					Double timestamp = Double.valueOf((double)operationExecutionRecord.getTin());
					//check map entry is there for safety
					if(!visitsMethodToTimeStampList.containsKey(methodname)){
						//this map is initialized in the constructor to always have all the 
						//WCs there to be able to report 0 visits.
						throw new IllegalStateException("The WC does not exist, but has to exist");
					}
					//get the WC entry and add the visit timestamp from the class map
					visitsMethodToTimeStampList.get(methodname).add(timestamp);
					//set the first timestamp as start timestamp
					if(visitsLastTimeStampNanos==null){
						visitsLastTimeStampNanos = timestamp;
					}
					//check if we have a interval finished due to the class timestamp and the new timestamp
					if(isAggregationIntervalPassed(visitsLastTimeStampNanos, timestamp)){
						//get the visits values out of visitsMethodToTimeStampList
						//for all the intervals, that have passed - perhaps two intervals have been passed.
						while(timestamp>visitsLastTimeStampNanos){
							//increase the last interval timestamp of the class value
							visitsLastTimeStampNanos = increaseAggregationTimeStamp(visitsLastTimeStampNanos);
							//foreach WC in the class map
							for (Entry<String, ArrayList<Double>> wc : visitsMethodToTimeStampList.entrySet()) {
								//get the visits of this wc until the new visitsLastTimeStampNanos timestamp
								int visits = 0;
								for (Double ts : wc.getValue()) {
									//count
									if(ts<visitsLastTimeStampNanos){
										visits++;
									}else{
										//stop counting, because we are in the next interval here
										break;
									}
								}
								//remove the counted entries from list, because we don't want to count them twice
								for (int j=0; j<visits; ++j) {
									//remove visits times the first element, which is the smalest.
									wc.getValue().remove(0);
								}
								//add the results to the local tmp lists
								if(!methodToValuesList.containsKey(wc.getKey())){
									methodToValuesList.put(wc.getKey(), new ArrayList<Double>());
									methodToTimeStampList.put(wc.getKey(), new ArrayList<Double>());
								}
								methodToValuesList.get(wc.getKey()).add(Double.valueOf((double)visits));
								methodToTimeStampList.get(wc.getKey()).add(visitsLastTimeStampNanos);
								//check the next WC
							}
							//check if we have processed all data up to the actual time stamp of this new line
						}
					}//interval has not been passed, therefore do nothing
				}//we do not have this entity
			}//it was a wrong kiekerid goto next line
		}//all lines passed
		//for each WC that was handled within this buffer rows. traverse the local tmp lists
		for (Entry<String, ArrayList<Double>> wc : methodToValuesList.entrySet()) {
			//for debugging use save all the data to a csv file
			saveToCsv(metric, wc.getKey(), wc.getValue(),methodToTimeStampList.get(wc.getKey()));
			//find the corresponding service and create the key with the right entity
			TraceKey traceKey = mapEntityToTraceKey(wc.getKey(), metric);
			//check if the trace key exists, which means if the WC is important for us
			if(traceKey!=null){
				//create the time series
				TimeSeries timeSeries = createTimeSeries(wc.getValue(), methodToTimeStampList.get(wc.getKey()));
				//add it to the final set
				finalSeries.put(traceKey, timeSeries);
			}
		}
		//return this series.
		return finalSeries;
	}

	private Map<TraceKey, TimeSeries> getResponseTimeSeries(Class<?> recordtype, Metric<?> metric,
			ArrayList<IMonitoringRecord> records) {
		Map<TraceKey, TimeSeries> finalSeries = new HashMap<>();
		//Create a map that holds the different method names and the response time for this method
		Map<String, ArrayList<Double>> methodToValuesList = new HashMap<>();
		//Create a map that holds the different method names and the time stamps for this method
		Map<String, ArrayList<Double>> methodToTimeStampList = new HashMap<>();
		//foreach line in the buffer
		for (int i=0; i<records.size(); ++i) {
			//check if we have to deal with this row due to id
			if(records.get(i).getClass()==recordtype){
				//get the actual record
				OperationExecutionRecord operationExecutionRecord = (OperationExecutionRecord) records.get(i);
				//get the method name which is the WC
				String methodname = parseOperationExecutionMethodName(operationExecutionRecord.getOperationSignature());
				//check if we have to deal with this WC
				if(isEntityAvailable(methodname, metric)){
					//get the response time and the timestampp
					Double timestamp = Double.valueOf((double)operationExecutionRecord.getTin());
					Double outtime = Double.valueOf((double)operationExecutionRecord.getTout());
					Double responsetime = outtime-timestamp;
					//create a map entry if the WC is new in the local tmp lists
					if(!methodToValuesList.containsKey(methodname)){
						methodToValuesList.put(methodname, new ArrayList<Double>());
						methodToTimeStampList.put(methodname, new ArrayList<Double>());
					}
					//get the WC entry and add the responsetime in the local tmp lists
					methodToValuesList.get(methodname).add(responsetime);
					//get the WC entry and add the timestamp in the local tmp lists
					methodToTimeStampList.get(methodname).add(timestamp);
				}
			}//we have not to deal with it due to kiekerid
		}//we finished all rows in the buffer
		//for each WC in the local tmp lists
		for (Entry<String, ArrayList<Double>> wc : methodToValuesList.entrySet()) {
			//for debugging use save all the data to a csv file
			saveToCsv(metric, wc.getKey(), wc.getValue(),methodToTimeStampList.get(wc.getKey()));
			//find the corresponding service and create the key with the right entity
			TraceKey traceKey = mapEntityToTraceKey(wc.getKey(), metric);
			//create the time series
			TimeSeries timeSeries = createTimeSeries(wc.getValue(), methodToTimeStampList.get(wc.getKey()));
			//add it to the final set
			finalSeries.put(traceKey, timeSeries);
		}
		//return this set.
		return finalSeries;
	}

	private String parseOperationExecutionMethodName(String operationSignature) {
		String[] tmp = operationSignature.split("\\(")[0].trim().split("\\s+");
		return tmp[tmp.length-1];
	}


	private boolean isEntityAvailable(String entityname, Metric<?> metric) {
		if(tracekeymap.get(metric)!=null){
			if(tracekeymap.get(metric).get(entityname)!=null){
				return true;
			}
		}
		return false;
	}
	/**
	 * This method creates the trace key for a given method name out of another trace key.
	 * The method name defines the Service/WC.
	 * 
	 * @param methodname
	 * @param key
	 * @return
	 */
	private TraceKey mapEntityToTraceKey(String entityname, Metric<?> metric) {
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

	private boolean isAggregationIntervalPassed(Double visitsLastTimeStampNanos, Double timestamp) {
		boolean rc = false;
		//get nanoseconds out of seconds interval
		Double intervalnanos = aggregationinterval * 1000000000.0;
		//check if we are greater than the interval
		if((timestamp-visitsLastTimeStampNanos)>intervalnanos){
			rc = true;
		}
		return rc;
	}

	private Double increaseAggregationTimeStamp(Double visitsLastTimeStampNanos) {

		//get nanoseconds out of seconds interval
		Double intervalnanos = aggregationinterval * 1000000000.0;
		return intervalnanos+visitsLastTimeStampNanos;
	}
	/**
	 * Aggregate the CPU values of the cores to one value.
	 * 
	 * @param cpucorevalues
	 * @return
	 */
	private Double aggregateCpuCoreValues(Map<Integer, Double> cpucorevalues) {
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
	 * Create the Time Series instance out of Double values.
	 * 
	 * @param values
	 * @param timestamps
	 * @return
	 */
	private TimeSeries createTimeSeries(ArrayList<Double> values, ArrayList<Double> timestamps) {
		VectorBuilder vbTimestamps = VectorBuilder.create(timestamps.size());
		for (Double time : timestamps) {
			Unit<Time> dateUnit = Time.NANOSECONDS;
			double value = time = dateUnit.convertTo(time.doubleValue(), Time.SECONDS);
			vbTimestamps.add(value);
		}
		Vector ts = vbTimestamps.toVector();
		if (ts.isEmpty()) {
			return TimeSeries.EMPTY;
		}
		VectorBuilder vbValues = VectorBuilder.create(values.size());
		for (Double value : values) {
			vbValues.add(value.doubleValue());
		}
		Vector vs = vbValues.toVector();
		if (vs.isEmpty()) {
			return TimeSeries.EMPTY;
		}
		return new TimeSeries(ts, vs);
	}
	@Override
	public void close() throws IOException {
		analysisThread.terminate();
		try {
			analysisThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void saveToCsv(Metric<?> metric, String entityname, ArrayList<Double> values, ArrayList<Double> timestamps) {
		if(false){
			return;
		}
		String foldername = "/home/torsten/Schreibtisch/jettytests/remote/";
		//append the data to the file called metric_entityname.csv
		String filename ="";
		if(metric.equals(StandardMetrics.RESPONSE_TIME)){
			filename = "RESPONSETIME";
		} 
		//IF WE HAVE VISITS TO CALCUALTE
		else if(metric.equals(StandardMetrics.VISITS)){
			filename = "VISITS";
		} 
		//IF WE HAVE DEPARTURES TO CALCUALTE
		else if(metric.equals(StandardMetrics.DEPARTURES)){
			filename = "DEPARTURES";
		} 
		//IF WE HAVE THROUGHTPUT TO CALCUALTE
		else if(metric.equals(StandardMetrics.THROUGHPUT)){
			filename = "THROUGHPUT";
		} 
		//IF WE HAVE THE BUSY_TIME TO CALCULATE
		else if(metric.equals(StandardMetrics.BUSY_TIME)){
			filename = "BUSYTIME";
		} 
		//IF WE HAVE THE UTILIZATION TO CALCULATE
		else if(metric.equals(StandardMetrics.UTILIZATION)){
			filename = "UTILIZATION";
		} 
		//IF WE WANT TO CALCULATE OTHER METRICS DO IT HERE
		else{
			//other metrics
			filename = "UNKNOWN";
		}
		filename = filename+"_"+entityname+".csv";
		filename = foldername+filename;
		
		try{
		    FileOutputStream writer = new FileOutputStream(filename, true);
		    for (int i=0; i<values.size(); ++i) {
				Double value = values.get(i);
				Double timestamp = timestamps.get(i);
				Unit<Time> dateUnit = Time.NANOSECONDS;
				double ts  = dateUnit.convertTo(timestamp.doubleValue(), Time.SECONDS);
				BigDecimal bDecimal = new BigDecimal(ts);
				String line = bDecimal.toPlainString()+";"+value.doubleValue()+"\n";
				writer.write(line.getBytes());
				writer.flush();
			}
		    writer.close();
		} catch (IOException e) {
		   // do something
		}
		//throw new UnsupportedOperationException("Not yet implemented");
	}
}
