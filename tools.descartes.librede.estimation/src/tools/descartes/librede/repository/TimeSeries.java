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
package tools.descartes.librede.repository;

import static tools.descartes.librede.linalg.LinAlg.empty;
import static tools.descartes.librede.linalg.LinAlg.horzcat;
import static tools.descartes.librede.linalg.LinAlg.matrix;
import static tools.descartes.librede.linalg.LinAlg.range;
import static tools.descartes.librede.linalg.LinAlg.row;
import static tools.descartes.librede.linalg.LinAlg.scalar;
import static tools.descartes.librede.linalg.LinAlg.sort;
import static tools.descartes.librede.linalg.LinAlg.vector;
import static tools.descartes.librede.linalg.LinAlg.vertcat;

import tools.descartes.librede.linalg.Indices;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.MatrixBuilder;
import tools.descartes.librede.linalg.MatrixFunction;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorBuilder;
import tools.descartes.librede.linalg.VectorFunction;

public class TimeSeries implements Cloneable {
	
	public enum Interpolation
	{
		NONE {

			@Override
			public double interpolate(Vector time, Matrix content, double x, int idx1, int idx2, int column) {
				return Double.NaN;
			}
			
		},
		PIECEWISE_CONSTANT {
			@Override
			public double interpolate(Vector time, Matrix content, double x, int idx1, int idx2, int column) {
				return content.get(idx2, column);
			}
		},		
		LINEAR {
			@Override
			public double interpolate(Vector time, Matrix content, double x, int idx1, int idx2, int column) {
				if (idx1 < 0) {
					return content.get(idx2, column);
				} else {
					double x0 = time.get(idx1);
					double x1 = time.get(idx2);
					return content.get(idx1, column) * ((x1 - x) / (x1 - x0)) + content.get(idx2, column) * ((x - x0) / (x1 - x0));
				}
			}
		};		
		
		public abstract double interpolate(Vector time, Matrix content, double x, int idx1, int idx2, int column);
	}
	
	public static final TimeSeries EMPTY = new TimeSeries(empty(), empty());
	private Matrix values;
	private Vector timestamps;
	private Interpolation interpolation = Interpolation.NONE;
	private int offset;
	private int length;
	// a flag indicated whether this time series is only a view of a subset of the underlying matrix.
	private boolean view = false;
	private double startTime;
	private double endTime;
	
	private TimeSeries(Vector time, Matrix data, int offset, int length) {
		this.timestamps = time;
		this.values = data;
		this.offset = offset;
		this.length = length;
		this.view = (length != time.rows());
		if (length > 0) {
			this.startTime = time.get(offset);
			this.endTime = time.get(offset + length - 1);
		} else {
			this.startTime = Double.NaN;
			this.endTime = Double.NaN;
		}
	}

	public TimeSeries(Vector time, Matrix data) {
		if (time.isEmpty()) {
			this.timestamps = empty();
			this.values = empty();
		} else {
			Indices sorted = sort(time, 0);
			this.timestamps = time.get(sorted);
			this.values = data.rows(sorted);
		}
		this.offset = 0;
		this.length = time.rows();
		if (length > 0) {
			this.startTime = this.timestamps.get(offset);
			this.endTime = this.timestamps.get(offset + length - 1);
		} else {
			this.startTime = Double.NaN;
			this.endTime = Double.NaN;
		}
	}
	
	public int samples() {
		return length;
	}

	public double getStartTime() {
		return startTime;
	}
	
	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}

	public double getEndTime() {
		return endTime;
	}

	public Vector getTime() {
		if (view) {
			return timestamps.rows(range(offset, offset + length));
		} else {
			return timestamps;
		}
	}

	public Vector getData(int column) {
		if (view) {
			return values.column(column).rows(range(offset, offset + length));
		} else {
			return values.column(column);
		}
	}
	
	public Matrix getData() {
		if (view) {
			return values.rows(range(offset, offset + length));
		} else {
			return values;
		}
	}
	
	public Interpolation getInterpolationMethod() {
		return interpolation;
	}
	
	public void setInterpolationMethod(Interpolation method) {
		this.interpolation = method;
	}
	
	public static Vector synchronize(TimeSeries... series) {
		return union(series);
	}
	
	public TimeSeries resample(final Vector timestamps) {
		Matrix resampled = matrix(timestamps.rows(), 2, new MatrixFunction() {			
			@Override
			public double cell(int row, int column) {
				return get(timestamps.get(row), column);
			}
		});
		
		return new TimeSeries(timestamps, resampled);
	}

	public TimeSeries addSample(double time, double...values) {
		TimeSeries ret;
		
		if (isEmpty()) {
			ret = new TimeSeries(scalar(time), matrix(row(values)));
		} else {	
			double idx = interpolationSearch(time);
			Matrix tempValues = this.values;
			Vector tempTime = this.timestamps;
			if (offset != 0 || length != timestamps.rows()) {
				tempValues = tempValues.rows(range(offset, offset + length));
				tempTime = tempTime.rows(range(offset, offset + length));
			}
	
			if (idx > (offset + length - 1)) {
				ret = new TimeSeries((Vector)tempTime.insertRow(tempTime.rows(), scalar(time)), tempValues.insertRow(tempValues.rows(), vector(values)));
			} else if (idx < offset) {
				ret = new TimeSeries((Vector)tempTime.insertRow(0, scalar(time)), tempValues.insertRow(0, vector(values)));
			} else if (Math.floor(idx) != idx) {
				int newIdx = (int)Math.floor(idx) - offset;
				ret = new TimeSeries((Vector)tempTime.insertRow(newIdx, scalar(time)), tempValues.insertRow(newIdx, vector(values)));
			} else {
				// exact match -> add behind existing sample
				int newIdx = (int)idx + 1 - offset;
				ret = new TimeSeries((Vector)tempTime.insertRow(newIdx, scalar(time)), tempValues.insertRow(newIdx, vector(values)));
			}
		}
		ret.setInterpolationMethod(interpolation);
		return ret;
	}
	
	public TimeSeries append(TimeSeries series) {
		if (this.isEmpty()) {
			return (TimeSeries) series.clone();
		}
		if (series.isEmpty()) {
			return (TimeSeries) series.clone();
		}
		TimeSeries ts = new TimeSeries(vertcat(timestamps, series.timestamps), vertcat(values, series.values));
		ts.setStartTime(Math.min(getStartTime(), series.getStartTime()));
		ts.setEndTime(Math.max(getEndTime(), series.getEndTime()));
		return ts;
	}
	
	public double timeWeightedMean(int column, TimeSeries userWeights) {
		return mean(column, true, userWeights);
	}
	
	public double timeWeightedMean(int column) {
		return mean(column, true, null);
	}
	
	public double mean(int column) {
		return mean(column, false, null);
	}
	
	private double mean(int column, boolean timeWeighted, TimeSeries userWeights) {
		if (isEmpty()) {
			return Double.NaN;
		}
		
		if (!timeWeighted) {
			double total = 0.0;
			int n = 0;
			for (int i = offset; i < (offset + length); i++) {
				double value = values.get(i, column);
				if (!Double.isNaN(value)) {
					total += value;
					n++;
				}
			}
			if (n == 0) {
				return Double.NaN;
			} else {
				return total / n;
			}
		} else {
			double total = 0.0;
			double totalWeight = 0;
			
			int n = Math.min(timestamps.rows(), (offset + length + 1));
			for(int i = offset; i < n; i++) {				
				double curTs = Math.min(endTime, timestamps.get(i));
				double lastTs = Math.max(startTime, (i > 0) ? timestamps.get(i - 1) : Double.NEGATIVE_INFINITY);
				double value = get(curTs, column);
				if (!Double.isNaN(value)) {
					double weight = curTs - lastTs;
					if (userWeights != null) {
						weight = weight * userWeights.subset(lastTs, curTs).sum(0);
					}
					total += weight * value;
					totalWeight += weight;
				}
			}
			if (totalWeight == 0) {
				return Double.NaN;
			} else {
				return total / totalWeight;
			}
		}
	}
	
	public double min(int column) {
		double min = Double.MAX_VALUE;
		int n = 0;
		for (int i = offset; i < (offset + length); i++) {
			min = Math.min(values.get(i, column), min);
			n++;
		}
		if (n == 0) {
			return Double.NaN;
		} else {
			return min;
		}
	}
	
	public double max(int column) {
		double max = Double.MIN_VALUE;
		int n = 0;
		for (int i = offset; i < (offset + length); i++) {
			max = Math.max(values.get(i, column), max);
			n++;
		}
		if (n == 0) {
			return Double.NaN;
		} else {
			return max;
		}
	}
	
	public double sum(int column) {
		double sum = 0.0;
		for (int i = offset; i < (offset + length); i++) {
			sum += values.get(i, column);
		}
		return sum;
	}
	
	public TimeSeries diff() {
		if (timestamps.rows() > 1) {
			double[] buffer = new double[values.columns()];
			int entries = (offset > 0) ? length : (length - 1);
			TimeSeries diffSeries = TimeSeries.EMPTY;
			if (entries > 0) {
				MatrixBuilder diff = MatrixBuilder.create(entries, values.columns());
				VectorBuilder ts = VectorBuilder.create(entries);
				for (int i = offset; i < (offset + length); i++) {
					if (i > 0) {					
						for (int j = 0; j < buffer.length; j++) {
							buffer[j] = values.get(i, j) - values.get(i - 1, j);
						}
						ts.add(timestamps.get(i));
						diff.addRow(buffer);
					}
				}
				diffSeries = new TimeSeries(ts.toVector(), diff.toMatrix(), 0, entries);
			}			
			diffSeries.startTime = startTime;
			diffSeries.endTime = endTime;
			return diffSeries;
		}
		return EMPTY;
	}
	
	public TimeSeries subset(double startTime, double endTime) {
		if (startTime > endTime) {
			throw new IllegalArgumentException("Start time must be less or equal than end time.");
		}
		if (isEmpty() || startTime == endTime) {
			return EMPTY;
		}
		if ((startTime != startTime) && (endTime != endTime)) {
			// Both are NaN therefor skip subset
			return (TimeSeries)this.clone();
		}
		if ((startTime < this.startTime) || (endTime > this.endTime)) {
			throw new IllegalArgumentException("Requested subset [" + startTime + ", " + endTime + "] is not contained by this series time range [" + this.startTime + ", " + this.endTime + "].");
		}
		
		double idx1 = (startTime != startTime) ? offset - 1 : interpolationSearch(startTime);
		double idx2 = (endTime != endTime) ? length - 1 : interpolationSearch(endTime);
		
		TimeSeries ret;
		if (idx1 < 0 && idx2 < 0) {
			ret = new TimeSeries(timestamps, values, 0, 0);
		} else {	
			int start = (int)Math.floor(idx1) + 1;
			int end = (int)idx2;
			ret = new TimeSeries(timestamps, values, start, (end - start + 1));
		}
		ret.setInterpolationMethod(interpolation);
		ret.startTime = (startTime != startTime) ? this.startTime : startTime;
		ret.endTime = (endTime != endTime) ? this.endTime : endTime;
		return ret;
	}

	public double get(double timestamp, int column) {
		double idx = interpolationSearch(timestamp);
		if (idx <= -1 || idx >= timestamps.rows()) {
			return Double.NaN;
		}
		if (Math.floor(idx) != idx) {
			return interpolation.interpolate(timestamps, values, timestamp, (int)Math.floor(idx), (int)Math.ceil(idx), column);
		}

		return values.get((int)idx, column);
	}
	
	public double getAverageTimeIncrement() {
		double totalIncrement = 0.0;
		for (int i = 1; i < timestamps.rows(); i++) {
			totalIncrement += (timestamps.get(i - 1) - timestamps.get(i));
		}		
		return totalIncrement / (timestamps.rows() - 1);
	}

	/**
	 * This function implements an interpolation search for fast access to a
	 * sample based on a timestamp.
	 * 
	 * @param timestamp
	 *            - the timestamp of the required sample
	 * @return a double value x:
	 * 		- x <= -1: if timestamp is smaller than start time
	 * 		- -1 < x < offset: if timestamp is larger than start time but smaller than timestamp of first element
	 * 		- offset <= x <= (offset+length-1): index of element with the corresponding timestamp. If timestamp lies between two elements
	 * 							x is a number between the two indeces.
	 * 		- (offset+length-1) < x < n: if timestamp is smaller than end time but larger than timestamp of last element
	 * 		- x >= n: if timestamp is larger than end time
	 */
	private double interpolationSearch(double timestamp) {
		// execute an interpolation search
		double first = startTime;
		double last = endTime;
		
		if (timestamp < first) {
			return -1;
		} else if (timestamp > last) {
			return timestamps.rows();
		}
	
		int firstIdx = offset;
		int lastIdx = Math.max(offset + length - 1, offset);

		while (timestamp >= first && timestamp <= last) {
			double span = last - first;
			int pos = firstIdx
					+ (int) ((lastIdx - firstIdx) * (timestamp - first) / span);

			double cur = timestamps.get(pos);
			if (cur == timestamp) {
				return pos;
			} else if (cur > timestamp) {
				lastIdx = Math.max(pos - 1, 0);
			} else if (cur < timestamp) {
				firstIdx = Math.min(pos + 1, timestamps.rows() - 1);
			}
			first = timestamps.get(firstIdx);
			last = timestamps.get(lastIdx);
		}

		if (timestamp > last) {
			return lastIdx + 0.5;
		} else if (first < timestamp) {
			return firstIdx + 0.5;
		} else {
			return firstIdx - 0.5;
		}
	}
	
	private static Vector union(TimeSeries... ts) {
		final int[] rows = new int[ts.length];
		double start = Double.NEGATIVE_INFINITY;
		double end = Double.POSITIVE_INFINITY;
		for (int i = 0; i < ts.length; i++) {
			rows[i] = ts[i].samples();
			start = Math.max(start, ts[i].getStartTime());
			end = Math.min(end, ts[i].getEndTime());
		}
		
		if (start >= end) {
			return null;
		}
		
		double rate = 0;
		for (int i = 0; i < ts.length; i++) {
			double totalDiff = 0;
			int diffCount = 0;
			Vector time = ts[i].getTime();
			for (int j = 1; j < rows[i]; j++) {
				totalDiff += time.get(j) - time.get(j - 1);
				diffCount++;
			}
			rate = Math.max(rate, totalDiff / diffCount);
		}
		
		final double newRate = rate;
		final double newStart = start;
		
		int count = (int) ((end - start) / newRate);
		Vector newTimestamps = vector(count, new VectorFunction() {			
			@Override
			public double cell(int row) {
				return newStart + newRate * row;
			}
		});		
		return newTimestamps;
	}

	public boolean isEmpty() {
		return timestamps.isEmpty();
	}
	
	@Override
	public String toString() {
		return "Timeseries(t=" + getTime() + ";v=" + getData() + ")";
	}

	public void setEndTime(double endTime) {
		this.endTime = endTime;	
	}
	
	@Override
	protected Object clone() {
		try {
			return super.clone();
		} catch(CloneNotSupportedException ex) {};
		return null;
	}
}
