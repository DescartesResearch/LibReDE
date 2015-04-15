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
package tools.descartes.librede.datasource;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import tools.descartes.librede.configuration.TraceConfiguration;

/**
 * This interface provides common methods supported by all data source
 * implementations.
 * 
 * A data source provides functionality to read measurement data from a source
 * of a specific type and format (e.g., file, database, or interface provided by
 * a monitoring tool). A data source is used to continuously load the newly
 * available measurement data points into an instance of a
 * {@link tools.descartes.librede.repository.IMonitoringRepository}.
 * 
 * The data source provides an asynchronous interface for reading the
 * measurement data similar to the asynchronous file operations from the NIO
 * packages in the java standard library.
 * 
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 */
public interface IDataSource extends Closeable {

	/**
	 * Adds the specified trace into this data source. The data source will
	 * monitor for new data until this data source is closed.
	 * 
	 * @param configuration
	 *            A TraceConfiguration containing the information where the data
	 *            source can find the data for this trace.
	 * @throws IOException
	 *             thrown if the data source cannot read this trace.
	 */
	public List<TraceKey> addTrace(TraceConfiguration configuration) throws IOException;

	/**
	 * Starts loading the existing data in the data source and watches for new
	 * data to become available until the data source is closed.
	 * 
	 * @throws IOException
	 */
	public void load() throws IOException;

	/**
	 * Add a data source listener.
	 * 
	 * @param listener
	 */
	public void addListener(IDataSourceListener listener);

	/**
	 * Remove a data source listener
	 * 
	 * @param listener
	 */
	public void removeListener(IDataSourceListener listener);

}
