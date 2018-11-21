/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2018, by Simon Spinner, Johannes Grohmann
 *  and Contributors.
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
package tools.descartes.librede.datasource.kiekeramqp;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

/**
 * This filter has exactly one input port and one output port.
 * 
 * This filter will queue events from a AMQPReader and trigger events to the KiekerAmqpDataSoruce
 * 
 * @author Torsten Krauß
 * 
 * @since 1.2
 */
@Plugin(description = "A filter to generate trace data for librede.",
		outputPorts = @OutputPort(name = LibredeTraceFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, description = "Provides each incoming object", eventTypes = { Object.class }),
		configuration = {
				@Property(name = LibredeTraceFilter.CONFIG_PROPERTY_NAME_EVENT_TRIGGER_COUNT,
						defaultValue = LibredeTraceFilter.CONFIG_PROPERTY_VALUE_EVENT_TRIGGER_COUNT,
						description = "Sets the maximum number of stored values."),
				@Property(name = LibredeTraceFilter.CONFIG_PROPERTY_NAME_EVENT_TRIGGER_COUNT_MAX_TIME_SEC,
				defaultValue = LibredeTraceFilter.CONFIG_PROPERTY_VALUE_EVENT_TRIGGER_COUNT_MAX_TIME_SEC,
				description = "Sets the time after we definitly try to trigger something."),
				@Property(name = LibredeTraceFilter.CONFIG_PROPERTY_NAME_EVENT_TRIGGER_COUNT_MINIMUM,
				defaultValue = LibredeTraceFilter.CONFIG_PROPERTY_VALUE_EVENT_TRIGGER_COUNT_MINIMUM,
				description = "Sets the minimum number of events we need to trigger something")
		})
public class LibredeTraceFilter extends AbstractFilterPlugin{

	/** The name of the input port for incoming events. */
	public static final String INPUT_PORT_NAME_EVENTS = "receivedEvents";
	/** The name of the output port delivering the incoming events. */
	public static final String OUTPUT_PORT_NAME_RELAYED_EVENTS = "relayedEvents";
	/** The name of the property determining the number how many events should be collected
	 * before we trigger some traceevents*/
	public static final String CONFIG_PROPERTY_NAME_EVENT_TRIGGER_COUNT = "triggercount";
	/** The default value of the number how many events should be collected
	 * before we trigger some traceevents*/
	public static final String CONFIG_PROPERTY_VALUE_EVENT_TRIGGER_COUNT = "512";
	/** The name of the property determining the number how many events have to be collected
	 * before we trigger some traceevents*/
	public static final String CONFIG_PROPERTY_NAME_EVENT_TRIGGER_COUNT_MINIMUM = "triggercountmin";
	/** The default value of the number how many events have to be collected
	 * before we trigger some traceevents*/
	public static final String CONFIG_PROPERTY_VALUE_EVENT_TRIGGER_COUNT_MINIMUM = "100";
	/** The name of the property determining the time that should elapse before we definitly try to
	 * trigger some traceevents*/
	public static final String CONFIG_PROPERTY_NAME_EVENT_TRIGGER_COUNT_MAX_TIME_SEC = "triggercountmaxtimesec";
	/** The default value of the property determining the time that should elaps before we definitly try to
	 * trigger some traceevents*/
	public static final String CONFIG_PROPERTY_VALUE_EVENT_TRIGGER_COUNT_MAX_TIME_SEC = "20";
	/** The name of the property determining the time that should elapse before we try again to trigger 
	 * some events after we previously found not enough events*/
	public static final String CONFIG_PROPERTY_NAME_EVENT_TRIGGER_COUNT_WAIT_SEC = "triggercountwaitsec";
	/** The default value of the property determining the time that should elapse before we try again to trigger 
	 * some events after we previously found not enough events*/
	public static final String CONFIG_PROPERTY_VALUE_EVENT_TRIGGER_COUNT_WAIT_SEC = "2";
	/**
	 * The thread with the data processing logic.
	 */
	private DataHandler dataHandler;
	
	public LibredeTraceFilter(Configuration configuration, IProjectContext projectContext) {
		super(configuration, projectContext);
		// Read the configuration
		int triggercount = configuration.getIntProperty(CONFIG_PROPERTY_NAME_EVENT_TRIGGER_COUNT);
		int maxtimesec = configuration.getIntProperty(CONFIG_PROPERTY_NAME_EVENT_TRIGGER_COUNT_MAX_TIME_SEC);
		int mincount = configuration.getIntProperty(CONFIG_PROPERTY_NAME_EVENT_TRIGGER_COUNT_MINIMUM);
		int waitsec =configuration.getIntProperty(CONFIG_PROPERTY_NAME_EVENT_TRIGGER_COUNT_WAIT_SEC);
		dataHandler = new DataHandler(triggercount, maxtimesec, mincount, waitsec);
	}

	public void setDataSource(KiekerAmqpDataSource kiekerAmqpDataSource){
		dataHandler.setDataSource(kiekerAmqpDataSource);
	}
	
	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_EVENT_TRIGGER_COUNT, String.valueOf(this.dataHandler.getTriggercount()));
		configuration.setProperty(CONFIG_PROPERTY_NAME_EVENT_TRIGGER_COUNT_MAX_TIME_SEC, String.valueOf(this.dataHandler.getMaxtimesec()));
		configuration.setProperty(CONFIG_PROPERTY_NAME_EVENT_TRIGGER_COUNT_MINIMUM, String.valueOf(this.dataHandler.getMincount()));
		configuration.setProperty(CONFIG_PROPERTY_NAME_EVENT_TRIGGER_COUNT_WAIT_SEC, String.valueOf(this.dataHandler.getWaitsec()));
		return configuration;
	}

	@Override
	public boolean init() {
		dataHandler.start();
		return true;
	}
	
	@Override
	public void terminate(boolean error) {
		dataHandler.close();
	}
	
	/**
	 * This method is the input port of the filter receiving incoming objects. Every object will be printed into a stream (based on the configuration) before the
	 * filter sends it to the output port.
	 * 
	 * @param object
	 *            The new object.
	 */
	@InputPort(name = INPUT_PORT_NAME_EVENTS, description = "Receives incoming objects to be processed and forwarded", eventTypes = { Object.class })
	public final void inputEvent(final Object object) {
		//do something with the object
		if(object instanceof IMonitoringRecord){
			IMonitoringRecord monitoringRecord = (IMonitoringRecord)object;
			dataHandler.push(monitoringRecord);
		}
		//print out as string
		//String objectString = '('+object.getClass().getSimpleName()+") "+object.toString();
		//log.info("We received from AMQPReader: "+objectString);
		//forward the message to the outputport.
		super.deliver(OUTPUT_PORT_NAME_RELAYED_EVENTS, object);
	}
	
	private class DataHandler extends Thread{
		/**
		 * The kieker datasource we use.
		 */
		private KiekerAmqpDataSource dataSource;
		/**
		 * The internal queue for the kieker data
		 */
		private BlockingQueue<IMonitoringRecord> queue;
		/**
		 * The timestamp when the filter pushed Traces to the datasourcelistener the last time
		 */
		private long lastPushTimestamp;
		/** The value of the number how many events should be collected
		 * before we trigger some traceevents*/
		private int triggercount;
		/** The value of the property determining the time that should elapse before we definitly try to
		 * trigger some traceevents*/
		private int maxtimesec;
		/** The value of the number how many events have to be collected
		 * before we trigger some traceevents*/
		private int mincount;
		/** The value of the property determining the time that should elapse before we try again to trigger 
		 * some events after we previously found not enough events*/
		private int waitsec;
		
		private boolean stop = false;
		
		public DataHandler(int triggercount, int maxtimesec, int mincount, int waitsec){
			this.waitsec = waitsec;
			this.maxtimesec = maxtimesec;
			this.mincount = mincount;
			this.triggercount = triggercount;
			this.dataSource = null;
			this.queue = new LinkedBlockingQueue<>();
			this.lastPushTimestamp = System.currentTimeMillis();
		}
		
		public void setDataSource(KiekerAmqpDataSource kiekerAmqpDataSource) {
			this.dataSource = kiekerAmqpDataSource;
		}

		public void push(IMonitoringRecord monitoringRecord) {
			//try to insert to the queue.
			boolean worked = queue.offer(monitoringRecord);
			if(!worked){
				log.error("We could not insert the data to this queue with offer(...) method.");
			}
		}

		@Override
		public void run() {
			while(!stop){
				//if we reach the trigger threshould
				//or we have minimum mincount events in the queue and the maximum time is elapsed
				//since the last push
				int size = queue.size();
				//log.info("Actual queue size - "+size);
				//System.out.println("actual queue size:" + size);
				long now = System.currentTimeMillis();
				if((size>=triggercount)||((size>=mincount)&&(now-lastPushTimestamp>(maxtimesec*1000)))){
					if(size>triggercount){
						//set the size to the triggercount value to only handle this much entries at once
						size = triggercount;
					}
					//process these data
					processEvents(size);
					//update the last trigger time
					lastPushTimestamp = now;
				} else{
					try {
						Thread.sleep(waitsec*1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		private void processEvents(int size) {
			ArrayList<IMonitoringRecord> records = new ArrayList<>();
			for(int i = 0; i<size; ++i){
				IMonitoringRecord record = queue.poll();
				if(record==null){
					throw new IllegalStateException("This should normally never happen. No data in queue");
				}
				records.add(record);
			}
			//log.info("We will process "+size+" events");
			//only process the data to traces, is we have a datassource
			if(dataSource!=null){
				//here we can use the datasource to get the data out of the record,
				//to create the traces and to notify the listeners.
				pushTraces(records);
			} else{
				log.error("We have no datasource configured therefor the traces will not be processed.");
			}
			//do other stuff here like writing to a file or something else
		}

		private void pushTraces(ArrayList<IMonitoringRecord> records) {
			dataSource.notifySelector(records);
		}

		public void close() {
			stop = true;
			try {
				this.join();
			} catch (InterruptedException e) {
				this.interrupt();
				try {
					this.join();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		public int getTriggercount() {
			return triggercount;
		}
		public int getMaxtimesec() {
			return maxtimesec;
		}
		public int getMincount() {
			return mincount;
		}
		public int getWaitsec() {
			return waitsec;
		}
		
	}
}
