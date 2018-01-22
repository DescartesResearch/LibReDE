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

import java.io.IOException;

/**
 * Provides an interface to handle stream reading into an array which can be used as buffer.
 * @author torsten
 *
 */
public interface Stream {
	/**
	 * Open the stream.
	 * @throws IOException
	 */
	public abstract void open() throws IOException;
	/**
	 * Read data to the given array from the stream
	 * @param data - the output array
	 * @param offset - the offset of the actual read 
	 * @param length - the length that should be read
	 * @return - the number of bytes we read or -1 if there is nothing to read.
	 * @throws IOException
	 */
	public abstract int read(byte[] data, int offset, int length) throws IOException;
	/**
	 * Close the stream.
	 * @throws IOException
	 */
	public abstract void close() throws IOException;
}
