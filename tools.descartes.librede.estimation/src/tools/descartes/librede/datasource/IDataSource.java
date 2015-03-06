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

import java.io.InputStream;

import tools.descartes.librede.repository.TimeSeries;

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
 * TODO: Make this interface more general: no direct dependency on InputStream
 * and support iterativ reading from data source (currently always the all available
 * data is loaded)
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 */
public interface IDataSource {

	public TimeSeries load(InputStream in, int column) throws Exception;

}
