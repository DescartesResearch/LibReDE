package tools.descartes.librede.datasource.kiekeramqp;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.log4j.Logger;

import tools.descartes.librede.configuration.TraceFilter;
import tools.descartes.librede.datasource.FileStream;
import tools.descartes.librede.datasource.Loggers;
import tools.descartes.librede.datasource.Stream;
import tools.descartes.librede.datasource.TraceEvent;
import tools.descartes.librede.datasource.TraceKey;
import tools.descartes.librede.datasource.kieker.KiekerDataSource;
import tools.descartes.librede.datasource.kieker.KiekerId;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorBuilder;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.repository.TimeSeries;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;
import tools.descartes.librede.units.UnitsFactory;

public class KiekerChannelOffline implements Closeable{
	/**
	 * The logging instance
	 */
	private final Logger log = Loggers.DATASOURCE_LOG;
	/**
	 * Zero instance
	 */
	private final Quantity<Time> ZERO = UnitsFactory.eINSTANCE.createQuantity(0, Time.SECONDS);
	/**
	 * The default maximum number of lines to read before a TraceEvent is triggered.
	 */
	private final int DEFAULT_MAX_BUFFERED_LINES = 512;
	/**
	 * The default maximum number of bytes that are read into a buffer at once.
	 */
	private final int DEFAULT_BUFFER_SIZE = 4096;
	/**
	 * The default maximum number of columns in the kieker file
	 */
	private final int DEFAULT_MAX_COLUMN_SIZE = 1;
	/**
	 * The maximum number of lines to read before a TraceEvent is triggered.
	 */
	private int maxBufferedLines;
	/**
	 * The maximum number of bytes that are read into a buffer at once.
	 */
	private int bufferSize;
	/**
	 * The maximum number of columns in the kieker file
	 */
	private int maxColumnSize;
	/**
	 * The newest time that the channel found data.
	 */
	private Quantity<Time> channelCurrentTime = ZERO;
	/**
	 * The values buffer of this channel.
	 */
	private String[][] valuesBuffer;
	/**
	 * The bytes buffer of this channel.
	 */
	private byte[] buffer;
	/**
	 * The DataSource we work with.
	 */
	private KiekerDataSourceOffline kiekerDataSource;
	/**
	 * A mapping from Trace Key to the Kieker Id, that is necessary for it.
	 */
	private Map<Metric<?>, KiekerId> traces = new HashMap<Metric<?>, KiekerId>();

	/**
	 * A mapping from Kieker Ids to their numbers in the map file for this directory/channel.
	 */
	private Map<KiekerId, String> kiekerids = new HashMap<>();
	/**
	 * Contains any incomplete line at the end of a buffer
	 */
	private String linePart = "";
	/**
	 * Counter for read lines.
	 */
	private int readLines = 0;
	/**
	 * The directory this channel observes.
	 */
	private File inputDirectory;
	/**
	 * The file we are actually dealing with in our input Stream.
	 */
	private String actualFile = "a";
	/**
	 * The actual input stream of this channel.
	 */
	private Stream input;
	/**
	 * A flag that indicates, if the map file has already been read.
	 */
	private boolean isMapFileRead = false;

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
	 * defines the maximum timestamp we want to read
	 */
	private long maxTimeStamp;
	/**
	 * The Constructor with default buffer sizes.
	 * 
	 * @param input - The Stream
	 * @param abstractFolderDataSource -  The datasource.
	 * @throws IOException
	 */
	public KiekerChannelOffline(File inputDirectory, KiekerDataSourceOffline kiekerDataSource, long maxTimeStamp){
		this.kiekerDataSource = kiekerDataSource;
		this.maxBufferedLines = DEFAULT_MAX_BUFFERED_LINES;
		this.bufferSize = DEFAULT_BUFFER_SIZE;
		this.maxColumnSize = DEFAULT_MAX_COLUMN_SIZE;
		valuesBuffer = new String[maxBufferedLines][maxColumnSize];
		buffer = new byte[bufferSize];
		this.inputDirectory = inputDirectory;
		this.maxTimeStamp=maxTimeStamp;
	}
	
	/**
	 * The constructor with custom buffer sizes.
	 * 
	 * @param input - The Stream.
	 * @param abstractFolderDataSource - The datasource.
	 * @param maxBufferedLines - The buffer lines.
	 * @param bufferSize - The buffer size.
	 * @throws IOException
	 */
	public KiekerChannelOffline(File inputDirectory, KiekerDataSourceOffline kiekerDataSource, long maxTimeStamp, int maxBufferedLines, int bufferSize, int maxColumnSize){
		this.kiekerDataSource = kiekerDataSource;
		this.maxBufferedLines = maxBufferedLines;
		this.bufferSize = bufferSize;
		this.maxColumnSize = maxColumnSize;
		valuesBuffer = new String[maxBufferedLines][maxColumnSize];
		buffer = new byte[bufferSize];
		this.inputDirectory = inputDirectory;
		this.maxTimeStamp = maxTimeStamp;
	}
	
	public void setMaxTimeStamp(long maxTimeStamp) {
		this.maxTimeStamp = maxTimeStamp;
	}
	
	/**
	 * Get the actual Stream (which is mostly a FileStream).
	 * @return - The Stream.
	 */
	public Stream getStream() {
		return input;
	}
	/**
	 * Sets the actual input stream and opens it with a RandomAccessFile Pointer.
	 * 
	 * @param input
	 * @throws IOException
	 */
	public void setStream(File file) throws IOException {
		//close the actual stream if one is open
		if(this.input!=null){
			this.input.close();
			this.input = null;
		}
		System.out.println("reading file "+file.getName());
		//create a new stream
		FileStream fileStream = new FileStream(file);
		//set it to the member
		this.input = fileStream;
		//remember the actual file
		actualFile = file.getName();
		//open the RandomAccessFile Pointer
		input.open();
	}
	/**
	 * Get the observed directory of this channel
	 * @return
	 */
	public File getInputDirectory() {
		return inputDirectory;
	}
	/**
	 * Adds the trace with the corresponding kieker id.
	 * @param key
	 * @param column
	 */
	public void addTrace(Metric<?> key, KiekerId id) {
		synchronized(traces) {
			if(!traces.containsKey(key)){
				traces.put(key, id);
				if(key.equals(StandardMetrics.UTILIZATION)){
					this.utilizationcorevaluesperresource = new HashMap<>();
					this.utilizationcoretimestampsperresource = new HashMap<>();
				} else if(key.equals(StandardMetrics.BUSY_TIME)){
					this.busytimecorevaluesperresource = new HashMap<>();
					this.busytimecoretimestampsperresource = new HashMap<>();
				} else if(key.equals(StandardMetrics.VISITS)){
					this.visitsMethodToTimeStampList = kiekerDataSource.initializeEntityMap(StandardMetrics.VISITS);
					this.visitsLastTimeStampNanos = null;
				} else if(key.equals(StandardMetrics.THROUGHPUT)){
					this.throughputMethodToTimeStampList = kiekerDataSource.initializeEntityMap(StandardMetrics.THROUGHPUT);
					this.throughputLastTimeStampNanos = null;
				} else if(key.equals(StandardMetrics.DEPARTURES)){
					this.departuresMethodToTimeStampList = kiekerDataSource.initializeEntityMap(StandardMetrics.DEPARTURES);
					this.departuresLastTimeStampNanos = null;
				}
			}
		}
	}

	/**
	 * Call this method to read all data from the current position in the
	 * actual file to the end of the last file in the directory.
	 * 
	 * @return <code>false</code> if a file stream was closed
	 *         unexpectedly.
	 */
	public boolean read() {
		boolean rc = true;
		//first ask if we already read the .map file
		if(!isMapFileRead){
			//this is a initial read process
			rc = readMapFile();
			if(rc){
				//mark that we read the map file
				isMapFileRead = true;
				rc = read();
			}
		}else{
			//this is a read process triggeres by file change
			if(!isMapFileRead){
				throw new IllegalStateException("We want to read data, but have not read the kieker.map file yet.");
			}
			//continue reading
			rc = continueReading();
		}
		return rc;
		
	}
	
	/**
	 * This method reads the data from the kieker files. 
	 * Therefore, it reads the actual file until it ends and then searches 
	 * for a newer file to continue.
	 * @return
	 */
	private boolean continueReading() {
		try {
			if(input == null){
				//we do not have an actual input stream which means we 
				//are in the first iteration
				File file = getNextFile();
				//create a new Filestream and open it
				setStream(file);
			}
			
			//now we have an open stream in the input member variable
		
			int lineCnt = 0; //the actual line count
			while (true) {
				// We avoid RandomAccessFile#readLine() as it is
				// performance-wise slow (many system calls) and we
				// cannot say whether a line a the end of the input is
				// complete or not.
				
				//read bufferSize bytes into the buffer
				int len = input.read(buffer, 0, bufferSize);
				//check if we have been at the actual end of the file
				if (len > 0) {
					//we got some data
					// Search for line terminators
					boolean eol = false;
					int s, i; // last line end (s), current index (i)
					//for every byte
					for (s = 0, i = 0; i < len; i++) {
						//we search line endings
						eol = (buffer[i] == '\n' || buffer[i] == '\r');
						//check if we found one
						if (eol) {
							//we found a line ending
							//create a line string out of the buffer
							String line = new String(buffer, s, (i - s));
							//check if we have buffer rests from last call
							if (!linePart.isEmpty()) {
								// Prepend the partial line from the
								// previous buffer
								line = linePart + line;
								//and clear the partial line
								linePart = "";
							}
							// New line parsed, increase global counter
							readLines++;
							//Parse the line
							//first ask the datasource if we should skip the line
							boolean skip = kiekerDataSource.skipLine(input, line, readLines);
							//then check if we need to process it due to kiekerids.
							if(!skip){
								skip = skipLine(line);
							}
							if (!skip) {
									synchronized(traces) {
										//get the values out of the line here - split and save to valuesBuffer
										valuesBuffer[lineCnt] = kiekerDataSource.parse(line);
										//increase the actual line count
										lineCnt++;
										// If MAX_BUFFERED_LINES is reached
										if (lineCnt >= maxBufferedLines) {
											// we notify listeners of new data
											notifySelector(maxBufferedLines);
											//and reset the actual count
											lineCnt = 0;
										}
									}
							}//end of the SKIP
							
							//we have processed the actual line 
							s = ++i;
							// skip windows line ending
							if ((i < len) && (buffer[i - 1] == '\r') && (buffer[i] == '\n')) {
								i++;
								s++;
							}
						}//this is the end of the EOL found 
						//here we go to next byte of buffer
					}//this is the end of the actual buffer
					if (i != s) {
						// there is an incomplete line at the end of
						// the buffer, therefore save that for next iteration
						linePart = new String(buffer, s, len - s);
					}
					//now we continue to read the next buffer bytes
				} else {
					//we are at the actual end of this file -
					//the buffer found no bytes to read
					break;
				}
			}
			//we get here, if we were at the actual end of this file
			
			//process the rest traces we have in the buffer
			if (lineCnt > 0) {
				// Notify listeners of the remaining data
				synchronized(traces) {
					notifySelector(lineCnt);
					lineCnt = 0;
				}
			}
			//check for a newer file to continue
			File file = getNextFile();
			//if there is a newer file
			if(file != null){
				//close the actual file and open the newer file
				setStream(file);
				//reset the line ending for savety
				linePart = "";
				//continue reading
				return read();
			}
			//if there is no newer file stop
			return true;
		} catch (IOException e) {
			log.error("Error reading from input " + input + ". Close input.", e);
			try {
				close();
			} catch (IOException e2) {
				log.error("Error that can be ignored"); /* Ignore */
			}
			return false;
		}
	}

	/**
	 * Method that tells us if a line has to be processed
	 * due to its kieker Id
	 * 
	 * @param line
	 * @return
	 */
	private boolean skipLine(String line) {
		boolean rc = true;
		for (Entry<Metric<?>, KiekerId> trace : traces.entrySet()) {
			String id = kiekerids.get(trace.getValue());
			if(line.startsWith(id)){
				rc = false;
				break;
			}
		}
		return rc;
	}

	/**
	 * This method returns the next bigger kieker data file
	 * @return - the next file or null if there is no file
	 */
	private File getNextFile() {
		File[] files = inputDirectory.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				//look at all kieker data files
				if(name.startsWith("kieker-")){
					//that are newer than the actual file
					if(name.compareTo(actualFile)>0){
						//where the timestamp is smaller than the maxTimeStamp
						String[] tmp = name.split("-");
						String timestamp = tmp[1]+"-"+tmp[2];
						SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmssSSS");
						long time;
						try {
							time = format.parse(timestamp).getTime();
						} catch (ParseException e) {
							e.printStackTrace();
							return false;
						}
						if(time<maxTimeStamp){
							return true;
						}else{
							return false;
						}
					}else{
						return false;
					}
				}else{
					return false;
				}
			}
		});
		//if nothing was found return null
		if(files == null || files.length <= 1){
			return null;
		}
		//sort the new files
		Arrays.sort(files, NameFileComparator.NAME_COMPARATOR);
		//return the first file, which is the next bigger file
		return files[0];
	}

	/**
	 * Reads the kieker.map file in the directory and stores the 
	 * mapping ini the kiekerids member map
	 * 
	 * @return
	 */
	private boolean readMapFile() {
		boolean rc = true;
		try {
			//Read all the lines of the kieker.map file
			File mapFile = new File(inputDirectory.getAbsolutePath()+"/kieker.map");
			List<String> lines = Files.readAllLines(mapFile.toPath(),
                    Charset.defaultCharset());
			//for all the lines
            for (String line : lines) {
                System.out.println(line);
                //save the mapping in the kiekerids member map
                mapLineToId(line);
            }
		} catch (IOException e) {
			log.error("No kieker.map file discovered.");
			rc = false;
		}
		return rc;
	}

	/**
	 * Maps the kieker.map entries to the enum
	 * @param line
	 */
	private void mapLineToId(String line) {
		String tmp[] = line.split("=");
		String id = tmp[0];
		String tmp2[] = tmp[1].split("\\.");
		String name = tmp2[tmp2.length-1];
		KiekerId kiekerId = KiekerId.Unknown;
		switch (name) {
		case "KiekerMetadataRecord":
			kiekerId = KiekerId.KiekerMetadataRecord;
			break;
		case "CPUUtilizationRecord":
			kiekerId = KiekerId.CPUUtilizationRecord;
			break;
		case "MemSwapUsageRecord":
			kiekerId = KiekerId.MemSwapUsageRecord;
			break;
		case "DiskUsageRecord":
			kiekerId = KiekerId.DiskUsageRecord;
			break;
		case "LoadAverageRecord":
			kiekerId = KiekerId.LoadAverageRecord;
			break;
		case "GCRecord":
			kiekerId = KiekerId.GCRecord;
			break;
		case "ThreadsStatusRecord":
			kiekerId = KiekerId.ThreadsStatusRecord;
			break;
		case "ClassLoadingRecord":
			kiekerId = KiekerId.ClassLoadingRecord;
			break;
		case "CompilationRecord":
			kiekerId = KiekerId.CompilationRecord;
			break;
		case "MemoryRecord":
			kiekerId = KiekerId.MemoryRecord;
			break;
		case "NetworkUtilizationRecord":
			kiekerId = KiekerId.NetworkUtilizationRecord;
			break;
		case "OperationExecutionRecord":
			kiekerId = KiekerId.OperationExecutionRecord;
			break;
		default:
			kiekerId = KiekerId.Unknown;
			break;
		}
		if(!kiekerids.containsKey(kiekerId)){
			kiekerids.put(kiekerId, id);
		}
	}

	/**
	 * This method publishs length lines from the values buffer to the listeners.
	 * 
	 * @param bufferlinecount
	 */
	private void notifySelector(int bufferlinecount) {
		//for all traces means in this case for all metrics we want to collect data for
		for (Entry<Metric<?>, KiekerId> trace : traces.entrySet()) {
			//create a map, in which we will find the data to publish
			Map<TraceKey, TimeSeries> finalSeries;
			//get the kiekerid we need for the actual metric
			String kiekerid = kiekerids.get(trace.getValue());
			//get trace event data we want to publish
			finalSeries = getTraceEventData(kiekerid, trace.getKey(),bufferlinecount);
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
					kiekerDataSource.notifyListeners(event);
				}
			}
		}
	}

	/**
	 * This mehtod creates all the trace events from the data in the values buffer.
	 * @param kiekerid
	 * @param key
	 * @return
	 */
	private Map<TraceKey, TimeSeries> getTraceEventData(String kiekerid, Metric<?> metric, int bufferlinecount) {
		//IF WE HAVE A RESPONSE TIME TO CALCUALTE
		if(metric.equals(StandardMetrics.RESPONSE_TIME)){
			return getResponseTimeSeries(kiekerid, metric, bufferlinecount);
		} 
		//IF WE HAVE VISITS TO CALCUALTE
		else if(metric.equals(StandardMetrics.VISITS)){
			return getVisitsTimeSeries(kiekerid, metric, bufferlinecount);
		} 
		//IF WE HAVE DEPARTURES TO CALCUALTE
		else if(metric.equals(StandardMetrics.DEPARTURES)){
			return getDeparturesTimeSeries(kiekerid, metric, bufferlinecount);
		} 
		//IF WE HAVE THROUGHTPUT TO CALCUALTE
		else if(metric.equals(StandardMetrics.THROUGHPUT)){
			return getThroughputTimeSeries(kiekerid, metric, bufferlinecount);
		} 
		//IF WE HAVE THE BUSY_TIME TO CALCULATE
		else if(metric.equals(StandardMetrics.BUSY_TIME)){
			return getBusyTimeTimeSeries(kiekerid, metric, bufferlinecount);
		} 
		//IF WE HAVE THE UTILIZATION TO CALCULATE
		else if(metric.equals(StandardMetrics.UTILIZATION)){
			return getUtilizationTimeSeries(kiekerid, metric, bufferlinecount);
		} 
		//IF WE WANT TO CALCULATE OTHER METRICS DO IT HERE
		else{
			//other metrics
			return new HashMap<>();
		}
	}

	private Map<TraceKey, TimeSeries> getUtilizationTimeSeries(String kiekerid, Metric<?> metric, int bufferlinecount) {
		Map<TraceKey, TimeSeries> finalSeries = new HashMap<>();
		//map for the utilization for each resource in local tmp maps
		Map<String, ArrayList<Double>> values = new HashMap<>();
		//map for the timestamps for each resource in local tmp maps
		Map<String, ArrayList<Double>> timestamps = new HashMap<>();
		//foreach line in the buffer
		for (int i=0; i<bufferlinecount; ++i) {
			//get the actual row
			String line[] = valuesBuffer[i];
			//check if we have to deal with this row due to id
			if(line[0].equals(kiekerid)){
				//get the cpu core utilization value in percentage
				Double coreutilization = kiekerDataSource.parseCpuTotalUtilizationOfCore(line);
				//get the cpu core id value
				Integer coreid = kiekerDataSource.parseCpuIdOfCore(line);
				//get the timestamp
				Double coretimestamp = kiekerDataSource.parseCpuTimeStamp(line);
				//get the resourcename
				String coreresource = kiekerDataSource.parseCpuResourceName(line);
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
				Double utilization = kiekerDataSource.aggregateCpuCoreValues(cpucorevalues);
				//if we have all cores and got a value
				if(utilization != null){
					//do the same for the timestamps
					Double timestamp = kiekerDataSource.aggregateCpuCoreValues(cpucoretimestamps);
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
			TraceKey traceKey = kiekerDataSource.mapEntityToTraceKey(resource.getKey(),metric);
			//create the time series
			TimeSeries timeSeries = createTimeSeries(resource.getValue(), timestamps.get(resource.getKey()));
			//add it to the final set
			finalSeries.put(traceKey, timeSeries);
		}
		//return the finalseries.
		return finalSeries;
	}

	private Map<TraceKey, TimeSeries> getBusyTimeTimeSeries(String kiekerid, Metric<?> metric, int bufferlinecount) {
		Map<TraceKey, TimeSeries> finalSeries = new HashMap<>();
		//map for the data for each resource
		Map<String, ArrayList<Double>> values = new HashMap<String, ArrayList<Double>>();
		//map for the timestamps for each resource
		Map<String, ArrayList<Double>> timestamps = new HashMap<String, ArrayList<Double>>();
		//foreach line in the buffer
		for (int i=0; i<bufferlinecount; ++i) {
			String line[] = valuesBuffer[i];
			//check if we have to deal with this row due to id
			if(line[0].equals(kiekerid)){
				//get the cpu core utilization value
				Double corebusytime = kiekerDataSource.parseCpuCoreBusyTime(line);
				//get the cpu core id value
				Integer coreid = kiekerDataSource.parseCpuIdOfCore(line);
				//get the timestamp
				Double coretimestamp = kiekerDataSource.parseCpuTimeStamp(line);
				//get the timestamp
				String coreresource = kiekerDataSource.parseCpuResourceName(line);
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
				Double busytime = kiekerDataSource.aggregateCpuCoreValues(busytimecorevalues);
				//if we have all cores and got a value
				if(busytime != null){
					//do the same for the timestamps
					Double timestamp = kiekerDataSource.aggregateCpuCoreValues(busytimecoretimestamps);
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
			TraceKey traceKey = kiekerDataSource.mapEntityToTraceKey(resource.getKey(), metric);
			//create the time series
			TimeSeries timeSeries = createTimeSeries(resource.getValue(), timestamps.get(resource.getKey()));
			//add it to the final set
			finalSeries.put(traceKey, timeSeries);
		}
		return finalSeries;
	}

	private Map<TraceKey, TimeSeries> getThroughputTimeSeries(String kiekerid, Metric<?> metric, int bufferlinecount) {
		Map<TraceKey, TimeSeries> finalSeries = new HashMap<>();
		//Create a map that holds the different methods and the troughput for this method
		Map<String, ArrayList<Double>> methodToValuesList = new HashMap<>();
		//Create a map that holds the different methods and the timestamp for this method
		Map<String, ArrayList<Double>> methodToTimeStampList = new HashMap<>();
		//foreach line in the buffer
		for (int i=0; i<bufferlinecount; ++i) {
			//get the actual row
			String line[] = valuesBuffer[i];
			//check if we have to deal with this row due to id
			if(line[0].equals(kiekerid)){
				//get the method name which is the WC
				String methodname = kiekerDataSource.getOperationExecutionMethodName(line);
				//check if we have to deal with this WC
				if(kiekerDataSource.isEntityAvailable(methodname, metric)){
					//get the response time and the timestamp
					Double timestamp = kiekerDataSource.parseOperationExecutionStartTimeStamp(line);
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
					if(kiekerDataSource.isAggregationIntervalPassed(throughputLastTimeStampNanos, timestamp)){
						//get the visits values out of visitsMethodToTimeStampList
						//for all the intervals, that have passed - perhaps two intervals have been passed.
						while(timestamp>throughputLastTimeStampNanos){
							//increase the last interval timestamp of the class value
							throughputLastTimeStampNanos = kiekerDataSource.increaseAggregationTimeStamp(throughputLastTimeStampNanos);
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
								methodToValuesList.get(wc.getKey()).add((Double.valueOf((double)visits))/(kiekerDataSource.getAggregationInterval()));
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
			TraceKey traceKey = kiekerDataSource.mapEntityToTraceKey(wc.getKey(), metric);
			//create the time series
			TimeSeries timeSeries = createTimeSeries(wc.getValue(), methodToTimeStampList.get(wc.getKey()));
			//add it to the final set
			finalSeries.put(traceKey, timeSeries);
			//System.out.println("We have data for "+wc.getKey());
		}
		//return this series.
		return finalSeries;
	}

	private Map<TraceKey, TimeSeries> getDeparturesTimeSeries(String kiekerid, Metric<?> metric, int bufferlinecount) {
		Map<TraceKey, TimeSeries> finalSeries = new HashMap<>();
		//Create a map that holds the different methods and the departures WHICH WILL BE THE VISITS IN THIS CASE for this method
		Map<String, ArrayList<Double>> methodToValuesList = new HashMap<>();
		//Create a map that holds the different methods and the timestamps for this method
		Map<String, ArrayList<Double>> methodToTimeStampList = new HashMap<>();
		//foreach line in the buffer
		for (int i=0; i<bufferlinecount; ++i) {
			//get the actual row
			String line[] = valuesBuffer[i];
			//check if we have to deal with this row due to id
			if(line[0].equals(kiekerid)){
				//get the method name which is the WC
				String methodname = kiekerDataSource.getOperationExecutionMethodName(line);
				//check if we have to deal with this WC
				if(kiekerDataSource.isEntityAvailable(methodname, metric)){
					//get the response time and the timestamp
					Double timestamp = kiekerDataSource.parseOperationExecutionStartTimeStamp(line);
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
					if(kiekerDataSource.isAggregationIntervalPassed(departuresLastTimeStampNanos, timestamp)){
						//get the visits values out of visitsMethodToTimeStampList
						//for all the intervals, that have passed - perhaps two intervals have been passed.
						while(timestamp>departuresLastTimeStampNanos){
							//increase the last interval timestamp of the class value
							departuresLastTimeStampNanos = kiekerDataSource.increaseAggregationTimeStamp(departuresLastTimeStampNanos);
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
			TraceKey traceKey = kiekerDataSource.mapEntityToTraceKey(wc.getKey(), metric);
			//create the time series
			TimeSeries timeSeries = createTimeSeries(wc.getValue(), methodToTimeStampList.get(wc.getKey()));
			//add it to the final set
			finalSeries.put(traceKey, timeSeries);
		}
		//return this series.
		return finalSeries;
	}

	private Map<TraceKey, TimeSeries> getVisitsTimeSeries(String kiekerid, Metric<?> metric, int bufferlinecount) {
		Map<TraceKey, TimeSeries> finalSeries = new HashMap<>();
		//Create a map that holds the different methods and the visits for this method
		Map<String, ArrayList<Double>> methodToValuesList = new HashMap<>();
		//Create a map that holds the different methods and the timestamps for this method
		Map<String, ArrayList<Double>> methodToTimeStampList = new HashMap<>();
		//foreach line in the buffer
		for (int i=0; i<bufferlinecount; ++i) {
			//get the actual row
			String line[] = valuesBuffer[i];
			//check if we have to deal with this row due to id
			if(line[0].equals(kiekerid)){
				//get the method name which is the WC
				String methodname = kiekerDataSource.getOperationExecutionMethodName(line);
				//check if we have to deal with this WC
				if(kiekerDataSource.isEntityAvailable(methodname, metric)){
					//get the response time and the timestamp
					Double timestamp = kiekerDataSource.parseOperationExecutionStartTimeStamp(line);
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
					if(kiekerDataSource.isAggregationIntervalPassed(visitsLastTimeStampNanos, timestamp)){
						//get the visits values out of visitsMethodToTimeStampList
						//for all the intervals, that have passed - perhaps two intervals have been passed.
						while(timestamp>visitsLastTimeStampNanos){
							//increase the last interval timestamp of the class value
							visitsLastTimeStampNanos = kiekerDataSource.increaseAggregationTimeStamp(visitsLastTimeStampNanos);
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
			TraceKey traceKey = kiekerDataSource.mapEntityToTraceKey(wc.getKey(), metric);
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

	private Map<TraceKey, TimeSeries> getResponseTimeSeries(String kiekerid, Metric<?> metric, int bufferlinecount) {
		Map<TraceKey, TimeSeries> finalSeries = new HashMap<>();
		//Create a map that holds the different method names and the response time for this method
		Map<String, ArrayList<Double>> methodToValuesList = new HashMap<>();
		//Create a map that holds the different method names and the time stamps for this method
		Map<String, ArrayList<Double>> methodToTimeStampList = new HashMap<>();
		//foreach line in the buffer
		for (int i=0; i<bufferlinecount; ++i) {
			//get the actual row
			String line[] = valuesBuffer[i];
			//check if we have to deal with this row due to id
			if(line[0].equals(kiekerid)){
				//get the method name which is the WC
				String methodname = kiekerDataSource.getOperationExecutionMethodName(line);
				//check if we have to deal with this WC
				if(kiekerDataSource.isEntityAvailable(methodname, metric)){
					//get the response time and the timestampp
					Double timestamp = kiekerDataSource.parseOperationExecutionStartTimeStamp(line);
					Double responsetime = kiekerDataSource.parseOperationExecutionResponseTime(line);
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
			TraceKey traceKey = kiekerDataSource.mapEntityToTraceKey(wc.getKey(), metric);
			//create the time series
			TimeSeries timeSeries = createTimeSeries(wc.getValue(), methodToTimeStampList.get(wc.getKey()));
			//add it to the final set
			finalSeries.put(traceKey, timeSeries);
		}
		//return this set.
		return finalSeries;
	}

	private void saveToCsv(Metric<?> metric, String entityname, ArrayList<Double> values, ArrayList<Double> timestamps) {
		if(true){
			return;
		}
		String foldername = "/home/torsten/Schreibtisch/jettytests/10minsubuntu200t/csv/";
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

	/**
	 * TODO: is this method necessary?
	 * @param filters
	 * @param line
	 * @return
	 */
	private boolean applyFilters(List<TraceFilter> filters, String[] line) {
		for (TraceFilter f : filters) {
			if (!f.getValue().equals(line[f.getTraceColumn()])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Close the input Stream.
	 */
	@Override
	public void close() throws IOException {
		if (input != null) {
			input.close();
			input = null;
		}
	}
}
