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
	
	private final int kfold;
	
	private final int intervals;
	
	// this 2d array contains for each partition a list of indices
	private int[][] partitions;
	
	private int[] curFold;
	
	private int curIdx = -1;
	
	private IRepositoryCursor delegate;
	
	public CrossValidationCursor(IRepositoryCursor cursor, int kfold, int intervals) {
		this.kfold = kfold;
		this.intervals = intervals;
		this.delegate = cursor;
	}
	
	public void startTrainingPhase(int validationPartition) {
		int count = intervals - partitions[validationPartition].length;
		curFold = new int[count];
		for (int i = 0, pos = 0; i < kfold; i++) {			
			if (i != validationPartition) {
				System.arraycopy(partitions[i], 0, curFold, pos, partitions[i].length);
				pos += partitions[i].length;
			}
		}
		Arrays.sort(curFold);
		delegate.reset();
		curIdx = -1;
	}
	
	public void startValidationPhase(int validationPartition) {
		curFold = partitions[validationPartition];
		delegate.reset();
		curIdx = -1;
	}
	
	@Override
	public boolean next() {
		if (curIdx < (curFold.length - 1)) {
			curIdx++;
			return true;
		}
		return false;
	}

	@Override
	public Quantity<Time> getIntervalStart(int interval) {
		return delegate.getIntervalStart(curFold[interval]);
	}

	@Override
	public Quantity<Time> getIntervalEnd(int interval) {
		return delegate.getIntervalEnd(curFold[interval]);
	}

	@Override
	public <D extends Dimension> TimeSeries getValues(int interval, Metric<D> metric, Unit<D> unit, ModelEntity entity) {
		return delegate.getValues(curFold[interval], metric, unit, entity);
	}

	@Override
	public <D extends Dimension> double getAggregatedValue(int interval, Metric<D> metric, Unit<D> unit, ModelEntity entity,
			Aggregation func) {
		return delegate.getAggregatedValue(curFold[interval], metric, unit, entity, func);
	}

	@Override
	public IMonitoringRepository getRepository() {
		return delegate.getRepository();
	}

	@Override
	public <D extends Dimension> boolean hasData(int interval, Metric<D> metric, List<ModelEntity> entities,
			Aggregation aggregation) {
		return delegate.hasData(curFold[interval], metric, entities, aggregation);
	}
	
	public void initPartitions() {
		partitions = new int[kfold][];
		int partitionSize = (int)Math.ceil(intervals / (double)kfold);
		
		int unassigned = intervals;
		boolean[] notSelected = new boolean[intervals];
		for (int i = 0; i < kfold - 1; i++) {
			int[] subset = new int[intervals - partitionSize * i];
			for (int p = 0, s = 0; p < intervals; p++) {
				if (notSelected[p]) {
					subset[s] = p;
					s++;
				}
			}
			long[] samples = new long[partitionSize];
			RandomSampler.sample(partitionSize, subset.length, partitionSize, 0, samples, 0, null);
			partitions[i] = new int[samples.length];
			for (int s = 0; s < samples.length; s++) {
				partitions[i][s] = (int) samples[s];
				notSelected[subset[(int)samples[s]]] = false;
				unassigned--;
			}
		}
		
		partitions[kfold - 1] = new int[unassigned];
		for (int i = 0, p = 0; i < intervals; i++) {
			if (notSelected[i]) {
				partitions[kfold - 1][p] = i;
				p++;
			}
		}
	}

	@Override
	public void reset() {
		delegate.reset();
	}

	@Override
	public int getLastInterval() {
		return curIdx;
	}
}
