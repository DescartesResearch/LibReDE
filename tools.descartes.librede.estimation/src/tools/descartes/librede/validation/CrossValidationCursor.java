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
package tools.descartes.librede.validation;

import java.util.Arrays;
import java.util.List;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.repository.IMonitoringRepository;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.TimeSeries;
import tools.descartes.librede.units.Dimension;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;
import cern.jet.random.sampling.RandomSampler;

public class CrossValidationCursor implements IRepositoryCursor {
	
	private int kfold;
	
	private int[] partitions;
	
	private int validationPartition = 0;
	
	private int curIdx = -1;
	
	private IRepositoryCursor delegate;
	
	private boolean validationMode;
	
	public CrossValidationCursor(IRepositoryCursor cursor, int kfold) {
		this.kfold = kfold;
		this.delegate = cursor;
	}
	
	public void startTrainingPhase(int validationPartition) {
		this.validationMode = false;
		this.validationPartition = validationPartition;
		delegate.seek(0);
		curIdx = -1;
	}
	
	public void startValidationPhase(int validationPartition) {
		this.validationMode = true;
		this.validationPartition = validationPartition;
		delegate.seek(0);
		curIdx = -1;
	}
	
	@Override
	public boolean next() {
		while(delegate.next()) {
			curIdx++;
			if ((partitions[curIdx] == validationPartition) == validationMode) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Quantity<Time> getCurrentIntervalStart() {
		return delegate.getCurrentIntervalStart();
	}

	@Override
	public Quantity<Time> getCurrentIntervalLength() {
		return delegate.getCurrentIntervalLength();
	}

	@Override
	public Quantity<Time> getCurrentIntervalEnd() {
		return delegate.getCurrentIntervalEnd();
	}

	@Override
	public <D extends Dimension> TimeSeries getValues(Metric<D> metric, Unit<D> unit, ModelEntity entity) {
		return delegate.getValues(metric, unit, entity);
	}

	@Override
	public <D extends Dimension> double getAggregatedValue(Metric<D> metric, Unit<D> unit, ModelEntity entity,
			Aggregation func) {
		return delegate.getAggregatedValue(metric, unit, entity, func);
	}

	@Override
	public IMonitoringRepository getRepository() {
		return delegate.getRepository();
	}

	@Override
	public <D extends Dimension> boolean hasData(Metric<D> metric, List<ModelEntity> entities,
			Aggregation aggregation) {
		return delegate.hasData(metric, entities, aggregation);
	}
	
	public void initPartitions() {
		int intervals = delegate.getAvailableIntervals();
		partitions = new int[intervals];
		int partitionSize = (int)Math.ceil(intervals / (double)kfold);
		Arrays.fill(partitions, -1);
		
		for (int i = 0; i < kfold - 1; i++) {
			int[] subset = new int[intervals - partitionSize * i];
			for (int p = 0, s = 0; p < intervals; p++) {
				if (partitions[p] < 0) {
					subset[s] = p;
					s++;
				}
			}
			long[] samples = new long[partitionSize];
			RandomSampler.sample(partitionSize, subset.length, partitionSize, 0, samples, 0, null);
			for (int s = 0; s < samples.length; s++) {
				partitions[subset[(int)samples[s]]] = i;
			}
		}
		
		for (int i = 0; i < intervals; i++) {
			if (partitions[i] < 0) {
				partitions[i] = kfold - 1;
			}
		}
	}

	@Override
	public boolean seek(int interval) {
		return delegate.seek(interval);
	}
	
	@Override
	public boolean seek(Quantity<Time> newTime) {
		return delegate.seek(newTime);
	}
	
	@Override
	public int getAvailableIntervals() {
		throw new UnsupportedOperationException();
	}
}
