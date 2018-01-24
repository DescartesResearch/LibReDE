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
package tools.descartes.librede.datasource.kiekeramqp;

import java.awt.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
 * This filter will wirte the events as like the AsyncFsWriter
 * 
 * @author Torsten Krauß
 * 
 * @since 1.2
 */
@Plugin(description = "A filter to print kieker files",
outputPorts = @OutputPort(name = KiekerFileFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, description = "Provides each incoming object", eventTypes = { Object.class }),
configuration = {
		@Property(name = KiekerFileFilter.CONFIG_PROPERTY_NAME_EVENT_DIRECTORY,
				defaultValue = KiekerFileFilter.CONFIG_PROPERTY_VALUE_EVENT_DIRECTORY,
				description = "Sets the directory for the files.")
})
public class KiekerFileFilter extends AbstractFilterPlugin{

	/** The name of the input port for incoming events. */
	public static final String INPUT_PORT_NAME_EVENTS = "receivedEvents";
	/** The name of the output port delivering the incoming events. */
	public static final String OUTPUT_PORT_NAME_RELAYED_EVENTS = "relayedEvents";
	/** The name of the property determining the number how many events should be collected
	 * before we trigger some traceevents*/
	public static final String CONFIG_PROPERTY_NAME_EVENT_DIRECTORY = "directory";
	/** The default value of the number how many events should be collected
	 * before we trigger some traceevents*/
	public static final String CONFIG_PROPERTY_VALUE_EVENT_DIRECTORY = "/tmp/";
	
	private String directory;
	
	private File dir;
	
	private File actualfile;
	
	private int counter;
	
	private BufferedWriter writer;
	
	private Map<Class<?>, Integer> mappinglist;
	
	public KiekerFileFilter(Configuration configuration, IProjectContext projectContext) {
		super(configuration, projectContext);
		this.directory = configuration.getStringProperty(CONFIG_PROPERTY_NAME_EVENT_DIRECTORY);
	}
	
	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_EVENT_DIRECTORY, directory);
		return configuration;
	}

	@Override
	public boolean init() {
		actualfile=null;
		mappinglist = new HashMap<>();
		counter = 0;
		dir=new File(directory);
		if(!(dir.exists() && dir.isDirectory())){
			return false;
		}
		return true;
	}
	
	@Override
	public void terminate(boolean error) {
		if(actualfile!=null){
			try {
				writer.flush();
			    writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

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
			printData(monitoringRecord);
		}
		//print out as string
		//String objectString = '('+object.getClass().getSimpleName()+") "+object.toString();
		//log.info("We received from AMQPReader: "+objectString);
		//forward the message to the outputport.
		super.deliver(OUTPUT_PORT_NAME_RELAYED_EVENTS, object);
	}

	private synchronized void printData(IMonitoringRecord monitoringRecord) {
		try {
			//check if we need to write to a new file
			if(counter>=25000 || actualfile==null){
				//reset the counter
				counter = 0;
				//close actual filestream
				if(actualfile!=null){
					writer.flush();
					writer.close();
				}
				//create new filename
				long actualtime = System.currentTimeMillis();
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmssSSS");
				//create new file
				actualfile = new File(directory+"kieker-"+format.format(new Date(actualtime))+".dat");
				//open stream
				writer = new BufferedWriter(new FileWriter(actualfile, true));
			}
			//write data
			Class<?> instance = monitoringRecord.getClass();
			if(!mappinglist.containsKey(instance)){
				mappinglist.put(instance, mappinglist.size()+1);
				//write to map file
				BufferedWriter mapfileWriter = new BufferedWriter(new FileWriter(new File(directory+"kieker.map"), true));
				mapfileWriter.write("$"+mappinglist.get(instance)+"="+instance.getName()+"\n");
				mapfileWriter.flush();
				mapfileWriter.close();
			}
			Integer key = mappinglist.get(instance);
			final StringBuilder sb = new StringBuilder(256);
			sb.append('$');
			sb.append(""+key);
			sb.append(';');
			sb.append(monitoringRecord.getLoggingTimestamp());
			for (final Object recordField : monitoringRecord.toArray()) {
				sb.append(';');
				sb.append(String.valueOf(recordField));
			}
			sb.append("\n");
			writer.append(sb.toString());
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    //increase the counter
	    counter++;
	}
	
}
