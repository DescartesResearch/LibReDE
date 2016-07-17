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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.repository.IMonitoringRepository;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.TimeSeries;
import tools.descartes.librede.repository.exceptions.NoMonitoringDataException;
import tools.descartes.librede.repository.exceptions.OutOfMonitoredRangeException;
import tools.descartes.librede.units.Dimension;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;

/**
 * A cursor for cross-validation supporting continuous input streams.
 * 
 * @author JS
 *
 */
public class ContinuousCrossValidationCursor implements IRepositoryCursor {

	/**
	 * The number of folds in the cross-validation
	 */
	private final int kfold;

	/**
	 * A pointer to the current index
	 */
	private int lastIndex = -1;

	/**
	 * A delegate cursor that traverses data in the original order
	 */
	private IRepositoryCursor cursor;

	/**
	 * The maximum history that needs to be stored, i.e. the maximum length of
	 * the arrays, usually defined by the window size
	 */
	private final int maxlength;

	/**
	 * Indicates, if we are currently in validation or training phase
	 */
	private boolean validation = false;

	/**
	 * Stores the current allocation of indices to folds
	 */
	private MaxSizeHashMap<Integer, Integer> kmapping;

	/**
	 * Maps the interval indices of the current fold to the real one of the
	 * underlying delegate
	 */
	private MaxSizeHashMap<Integer, Integer> translation;

	/**
	 * The current k used for validation
	 */
	private int validationk;

	/**
	 * Creates a new instance
	 * 
	 * @param cursor
	 *            A delegate cursor to be used
	 * @param kfold
	 *            The number of folds to initialize
	 * @param maxlength
	 *            The maximum history that needs to be stored, i.e. the maximum
	 *            length of the arrays, usually defined by the window size
	 */
	public ContinuousCrossValidationCursor(IRepositoryCursor cursor, int kfold, int maxlength) {
		super();
		this.kfold = kfold;
		this.cursor = cursor;
		this.maxlength = kfold * maxlength;
		this.validationk = 1;
		this.validation = false;
		kmapping = new MaxSizeHashMap<Integer, Integer>(this.maxlength);
		resetPointer();
	}

	/**
	 * Shifts to training phase and makes training data available. Resets the
	 * mapping.
	 * 
	 * @param validationPartition
	 *            The partition to now be used as validation (exclude in this
	 *            stream)
	 */
	public void startTrainingPhase(int validationPartition) {
		if (kfold < 2)
			throw new IllegalStateException(
					"The number of folds is smaller than 2. Training and validation phases are not supported.");
		validation = false;
		validationk = validationPartition + 1;
		resetPointer();
	}

	/**
	 * Shifts to validation phase and makes only validation stream data
	 * available. Resets the mapping.
	 * 
	 * @param validationPartition
	 *            The partition to now be used as validation (exclusively use in
	 *            this stream)
	 */
	public void startValidationPhase(int validationPartition) {
		if (kfold < 2)
			throw new IllegalStateException(
					"The number of folds is smaller than 2. Training and validation phases are not supported.");
		validation = true;
		validationk = validationPartition + 1;
		resetPointer();
	}

	@Override
	public boolean next() {
		if (!cursor.next()) {
			return false;
		} else {
			// if not validation and not validation fold or validation and
			// validation fold then jump to next
			while ((validation && getMapping(cursor.getLastInterval()) != validationk)
					|| (!validation && getMapping(cursor.getLastInterval()) == validationk)) {
				// jump to next
				if (!cursor.next()) {
					return false;
				}
			}
		}
		// found interval that fits for current fold config
		lastIndex = lastIndex + 1;
		translation.put(lastIndex, cursor.getLastInterval());
		return true;
	}

	@Override
	public Quantity<Time> getIntervalStart(int interval) {
		return cursor.getIntervalStart(translateToDelegate(interval));
	}

	@Override
	public Quantity<Time> getIntervalEnd(int interval) {
		return cursor.getIntervalEnd(translateToDelegate(interval));
	}

	@Override
	public <D extends Dimension> TimeSeries getValues(int interval, Metric<D> metric, Unit<D> unit,
			ModelEntity entity) {
		return cursor.getValues(translateToDelegate(interval), metric, unit, entity);
	}

	@Override
	public <D extends Dimension> double getAggregatedValue(int interval, Metric<D> metric, Unit<D> unit,
			ModelEntity entity, Aggregation func) {
		// System.out.println("Interval " + interval + " translated to
		// "+translateToDelegate(interval));
		return cursor.getAggregatedValue(translateToDelegate(interval), metric, unit, entity, func);
	}

	@Override
	public IMonitoringRepository getRepository() {
		return cursor.getRepository();
	}

	@Override
	public <D extends Dimension> boolean hasData(int interval, Metric<D> metric, ModelEntity entity,
			Aggregation aggregation) {
		return cursor.hasData(translateToDelegate(interval), metric, entity, aggregation);
	}

	/**
	 * Resets the whole pointer, i.e. also the mapping therefore if not
	 * deterministic, the mapping will be different after this call
	 */
	@Override
	public void reset() {
		resetPointer();
		translation.clear();
	}

	/**
	 * Resets the last index with the delegate pointer and the translation
	 * mapping. Useful when a repetition with the same mapping of folds is
	 * wanted, since the mapping stays untouched.
	 */
	public void resetPointer() {
		translation = new MaxSizeHashMap<Integer, Integer>(maxlength);
		cursor.reset();
		lastIndex = -1;
	}

	@Override
	public int getLastInterval() {
		return lastIndex;
	}

	/**
	 * Returns the delegate interval index
	 * 
	 * @param interval
	 *            the masked interval
	 * @return the interval index of the delegate cursor
	 */
	private int translateToDelegate(int interval) {
		if (interval == -1) {
			// an interval of -1 is also -1 since its a flag for unknown
			// interval
			return -1;
		}
		Integer real = translation.get(interval);
		if (real == null) {
			// to catch calls with 0 without initialization
			if (interval == 0)
				return 0;
			throw new IndexOutOfBoundsException("Interval index " + interval
					+ " is either not yet initialized or already deleted. Try calling next().");
		}
		return real.intValue();
	}

	/**
	 * Returns the mapped fold between 1 and k and assigns the next k folds if
	 * index has not been assigned yet. For invalid indices, they behavior is
	 * not specified.
	 * 
	 * @param index
	 *            The (delegate)-index of the interval
	 * @return The fold the interval is assigned to
	 */
	private int getMapping(int index) {
		// index has not been assigned yet
		if (kmapping.get(index) == null) {
			int run = index;
			// assign next indices randomly
			LinkedList<Integer> list = new LinkedList<Integer>();
			for (int i = 1; i <= kfold; i++) {
				list.add(i);
			}
			Collections.shuffle(list);
			while (list.peek() != null) {
				kmapping.put(run++, list.remove());
			}
		}
		return kmapping.get(index).intValue();
	}

	/**
	 * Helper class with a maximum number of elements held at once.
	 * 
	 * @author JS
	 *
	 * @param <K>
	 * @param <V>
	 */
	public class MaxSizeHashMap<K, V> extends LinkedHashMap<K, V> {
		private static final long serialVersionUID = 4143398628870932712L;
		private final int maxSize;

		public MaxSizeHashMap(int maxSize) {
			this.maxSize = maxSize;
		}

		@Override
		protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
			return size() > maxSize;
		}
	}
}
