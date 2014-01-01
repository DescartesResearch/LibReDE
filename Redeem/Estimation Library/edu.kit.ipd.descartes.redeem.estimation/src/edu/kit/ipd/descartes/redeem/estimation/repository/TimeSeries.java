package edu.kit.ipd.descartes.redeem.estimation.repository;

import static edu.kit.ipd.descartes.linalg.LinAlg.empty;
import static edu.kit.ipd.descartes.linalg.LinAlg.horzcat;
import static edu.kit.ipd.descartes.linalg.LinAlg.matrix;
import static edu.kit.ipd.descartes.linalg.LinAlg.sort;
import static edu.kit.ipd.descartes.linalg.LinAlg.vector;
import edu.kit.ipd.descartes.linalg.AggregateFunction;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.MatrixFunction;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorFunction;

public class TimeSeries {
	
	public enum Interpolation
	{
		NONE {

			@Override
			public double interpolate(Matrix content, double x, int idx1, int idx2) {
				return Double.NaN;
			}
			
		},
		PIECEWISE_CONSTANT {
			@Override
			public double interpolate(Matrix content, double x, int idx1, int idx2) {
				return content.get(idx2, 1);
			}
		},		
		LINEAR {
			@Override
			public double interpolate(Matrix content, double x, int idx1, int idx2) {
				if (idx1 < 0) {
					return content.get(idx2, 1);
				} else {
					return (content.get(idx2, 1) - content.get(idx1, 1)) / (content.get(idx2, 0)- content.get(idx1, 0)) * x;
				}
			}
		};		
		
		public abstract double interpolate(Matrix content, double x, int idx1, int idx2);
	}
	
	public static final TimeSeries EMPTY = new TimeSeries();
	private Matrix content;
	private Interpolation interpolation = Interpolation.NONE;
	private int offset;
	private int length;	
	private double startTime;
	private double endTime;
	
	private TimeSeries() {
		this(empty(), 0, 0);
	}

	private TimeSeries(Matrix content, int offset, int length) {
		this.content = content;
		this.offset = offset;
		this.length = length;
		if (length > 0) {
			this.startTime = content.get(offset, 0);
			this.endTime = content.get(offset + length - 1, 0);
		} else {
			this.startTime = Double.NaN;
			this.endTime = Double.NaN;
		}
	}

	private TimeSeries(Matrix content) {
		this(sort(content, 0), 0, Math.max(content.rows(), 0));
	}

	public TimeSeries(Vector time, Vector data) {
		this(horzcat(time, data));
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
		if (content.isEmpty()) {
			return empty();
		}
		return content.column(0).subset(offset, offset + length - 1);
	}

	public Vector getData() {
		if (content.isEmpty()) {
			return empty();
		}
		return content.column(1).subset(offset, offset + length - 1);
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
	
	public TimeSeries aggregate(final Vector timestamps, final AggregateFunction func) {
		int intervals = timestamps.rows();		
		Matrix results = matrix(intervals - 1, 2, new MatrixFunction() {
			@Override
			public double cell(int row, int column) {
				if (column == 0) {
					return timestamps.get(row + 1);
				} else if (column == 1) {
					TimeSeries curInterval = subset(timestamps.get(row), timestamps.get(row + 1));
					return func.aggregate(curInterval.getData());
				}
				throw new AssertionError();				
			}			
		});
		return new TimeSeries(results);	
	}
	
	public double aggregate(final AggregateFunction func) {
		return func.aggregate(getData());
	}

	public TimeSeries resample(final Vector timestamps) {
		Matrix resampled = matrix(timestamps.rows(), 2, new MatrixFunction() {			
			@Override
			public double cell(int row, int column) {
				if (column == 0) {
					return timestamps.get(row);
				} else {
					return get(timestamps.get(row));
				}
			}
		});
		
		return new TimeSeries(resampled);
	}

	public TimeSeries addSample(double time, double value) {
		double idx = interpolationSearch(time);
		Matrix temp = content;
		if (offset != 0 || length != content.rows()) {
			temp = content.subset(offset, offset + length - 1);
		}
		TimeSeries ret;
		if (idx > (offset + length - 1)) {
			ret = new TimeSeries(temp.insertRow(temp.rows(), time, value));
		} else if (idx < offset) {
			ret = new TimeSeries(temp.insertRow(0, time, value));
		} else if (Math.floor(idx) != idx) {
			ret = new TimeSeries(temp.insertRow((int)Math.floor(idx) - offset, time, value));
		} else {
			// exact match -> add behind existing sample
			ret = new TimeSeries(temp.insertRow((int)idx + 1 - offset, time, value));
		}
		ret.setInterpolationMethod(interpolation);
		return ret;
	}
	
	public double timeWeightedMean(TimeSeries userWeights) {
		return mean(true, userWeights);
	}
	
	public double timeWeightedMean() {
		return mean(true, null);
	}
	
	public double mean() {
		return mean(false, null);
	}
	
	private double mean(boolean timeWeighted, TimeSeries userWeights) {
		if (isEmpty()) {
			return Double.NaN;
		}
		
		if (!timeWeighted) {
			double total = 0.0;
			int n = 0;
			for (int i = offset; i < (offset + length); i++) {
				double value = content.get(i, 1);
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
			
			int n = Math.min(content.rows(), (offset + length + 1));
			for(int i = offset; i < n; i++) {				
				double curTs = Math.min(endTime, content.get(i, 0));
				double lastTs = Math.max(startTime, (i > 0) ? content.get(i - 1, 0) : Double.NEGATIVE_INFINITY);
				double value = get(curTs);
				if (!Double.isNaN(value)) {
					double weight = curTs - lastTs;
					if (userWeights != null) {
						weight = weight * userWeights.subset(lastTs, curTs).sum();
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
	
	public double min() {
		double min = Double.MAX_VALUE;
		int n = 0;
		for (int i = offset; i < (offset + length); i++) {
			min = Math.min(content.get(i, 1), min);
			n++;
		}
		if (n == 0) {
			return Double.NaN;
		} else {
			return min;
		}
	}
	
	public double max() {
		double max = Double.MIN_VALUE;
		int n = 0;
		for (int i = offset; i < (offset + length); i++) {
			max = Math.max(content.get(i, 1), max);
			n++;
		}
		if (n == 0) {
			return Double.NaN;
		} else {
			return max;
		}
	}
	
	public double sum() {
		double sum = 0.0;
		for (int i = offset; i < (offset + length); i++) {
			sum += content.get(i, 1);
		}
		return sum;
	}
	
	public TimeSeries subset(double startTime, double endTime) {
		if (startTime > endTime) {
			throw new IllegalArgumentException("Start time must be less or equal than end time.");
		}
		if ((startTime < this.startTime) || (endTime > this.endTime)) {
			throw new IllegalArgumentException("Requested subset is not contained by this time series.");
		}
		
		if (startTime == endTime) {
			return EMPTY;
		}
		
		double idx1 = interpolationSearch(startTime);
		double idx2 = interpolationSearch(endTime);
		
		TimeSeries ret;
		if (idx1 < 0 && idx2 < 0) {
			ret = new TimeSeries(content, 0, 0);
		} else {	
			int start = (int)Math.floor(idx1) + 1;
			int end = (int)idx2;
			ret = new TimeSeries(content, start, (end - start + 1));
		}
		ret.setInterpolationMethod(interpolation);
		ret.startTime = startTime;
		ret.endTime = endTime;
		return ret;
	}

	public double get(double timestamp) {
		double idx = interpolationSearch(timestamp);
		if (idx <= -1 || idx >= content.rows()) {
			return Double.NaN;
		}
		if (Math.floor(idx) != idx) {
			return interpolation.interpolate(content, timestamp, (int)Math.floor(idx), (int)Math.ceil(idx));
		}

		return content.get((int)idx, 1);
	}
	
	public double getAverageTimeIncrement() {
		double totalIncrement = 0.0;
		for (int i = 1; i < content.rows(); i++) {
			totalIncrement += (content.get(i - 1, 0) - content.get(i, 0));
		}		
		return totalIncrement / (content.rows() - 1);
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
			return content.rows();
		}
	
		int firstIdx = offset;
		int lastIdx = Math.max(offset + length - 1, offset);

		while (timestamp >= first && timestamp <= last) {
			double span = last - first;
			int pos = firstIdx
					+ (int) ((lastIdx - firstIdx) * (timestamp - first) / span);

			double cur = content.get(pos, 0);
			if (cur == timestamp) {
				return pos;
			} else if (cur > timestamp) {
				lastIdx = Math.max(pos - 1, 0);
			} else if (cur < timestamp) {
				firstIdx = pos + 1;
			}
			first = content.get(firstIdx, 0);
			last = content.get(lastIdx, 0);
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
		return (length == 0) && (startTime == endTime);
	}
	
	@Override
	public String toString() {
		return "Timeseries(t=" + getTime() + ";v=" + getData() + ")";
	}

}
