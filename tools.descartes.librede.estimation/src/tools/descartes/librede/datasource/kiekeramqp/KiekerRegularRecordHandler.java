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

/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/


import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.ILookup;

/**
 * Runnable to handle incoming regular records.
 *
 * @author Holger Knoche
 *
 * @since 1.12
 */
public class KiekerRegularRecordHandler implements Runnable {

	/** Default queue size for the regular record queue */
	private static final int DEFAULT_QUEUE_SIZE = 4096;

	private static final Log LOG = LogFactory.getLog(KiekerRegularRecordHandler.class);

	private final ILookup<String> stringRegistry;
	private final CachedRecordFactoryCatalog cachedRecordFactoryCatalog = CachedRecordFactoryCatalog.getInstance();
	private final KiekerAMQPReader reader;

	private final BlockingQueue<ByteBuffer> queue = new ArrayBlockingQueue<ByteBuffer>(DEFAULT_QUEUE_SIZE);

	/**
	 * Creates a new regular record handler.
	 *
	 * @param reader
	 *            The reader to send the instantiated records to
	 * @param stringRegistry
	 *            The string registry to use
	 */
	public KiekerRegularRecordHandler(final KiekerAMQPReader reader, final ILookup<String> stringRegistry) {
		this.reader = reader;
		this.stringRegistry = stringRegistry;
	}

	@Override
	public void run() {
		while (true) {
			try {
				final ByteBuffer nextRecord = this.queue.take();

				this.readRegularRecord(nextRecord);
			} catch (final InterruptedException e) {
				LOG.error("Regular record handler was interrupted", e);
			}
		}
	}

	/**
	 * Enqueues an unparsed regular record for processing.
	 *
	 * @param buffer
	 *            The unparsed data in an appropriately positioned byte buffer
	 */
	public void enqueueRegularRecord(final ByteBuffer buffer) {
		try {
			this.queue.put(buffer);
		} catch (final InterruptedException e) {
			LOG.error("Record queue was interrupted", e);
		}
	}

	private void readRegularRecord(final ByteBuffer buffer) {
		final int classId = buffer.getInt();
		final long loggingTimestamp = buffer.getLong();

		try {
			final String recordClassName = this.stringRegistry.get(classId);
			final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.cachedRecordFactoryCatalog.get(recordClassName);
			final IMonitoringRecord record = recordFactory.create(buffer, this.stringRegistry);
			record.setLoggingTimestamp(loggingTimestamp);

			this.reader.deliverRecord(record);
		} catch (final RecordInstantiationException e) {
			LOG.error("Error instantiating record", e);
		}
	}

}
