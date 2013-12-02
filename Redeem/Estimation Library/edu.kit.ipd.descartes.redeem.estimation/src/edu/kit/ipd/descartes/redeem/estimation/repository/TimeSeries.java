package edu.kit.ipd.descartes.redeem.estimation.repository;

import static edu.kit.ipd.descartes.linalg.LinAlg.horzcat;
import static edu.kit.ipd.descartes.linalg.LinAlg.matrix;
import static edu.kit.ipd.descartes.linalg.LinAlg.row;
import static edu.kit.ipd.descartes.linalg.LinAlg.sort;
import static edu.kit.ipd.descartes.linalg.LinAlg.vector;
import static edu.kit.ipd.descartes.linalg.LinAlg.vertcat;
import edu.kit.ipd.descartes.linalg.AggregateFunction;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.MatrixFunction;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorFunction;

public class TimeSeries {
	
	private Matrix content;
	private int[] offset;
	private double startTime;
	private double endTime;

	private TimeSeries(Matrix content, int[] offset) {
		this.content = content;
		this.offset = offset;
		this.startTime = content.get(offset[0], 0);
		this.endTime = content.get(offset[offset.length - 1], 0);
	}

	private TimeSeries(Matrix content) {
		this(content, sort(content, 0));
	}

	public TimeSeries(Vector time, Vector data) {
		this(horzcat(time, data));
	}
	
	public int samples() {
		return offset.length;
	}

	public double getStartTime() {
		return startTime;
	}

	public double getEndTime() {
		return endTime;
	}

	public Vector getTime() {
		return content.column(0).subset(offset);
	}

	public Vector getData() {
		return content.column(1).subset(offset);
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
		int[] newOffset = new int[offset.length + 1];
		// Assume that most times the new sample is added to the back of the
		// list.
		// Therefore, start insertion from back to reduce number of timestamp
		// comparisons.
		int i = offset.length - 1;
		while (i >= 0 && (content.get(offset[i], 0) < time)) {
			newOffset[i + 1] = offset[i];
			i--;
		}
		newOffset[i + 1] = content.rows();
		while (i >= 0) {
			newOffset[i] = offset[i];
			i--;
		}
		return new TimeSeries(vertcat(content, matrix(row(time, value))),
				newOffset);
	}

	public TimeSeries subset(double startTime, double endTime) {
		if (startTime > endTime) {
			throw new IllegalArgumentException("Start time must be less or equal than end time.");
		}
		
		int idx1 = interpolationSearch(startTime);
		int idx2 = interpolationSearch(endTime);
		
		int[] subsetOffset = new int[idx2 - idx1 + 1];	
		
		return new TimeSeries(content, subsetOffset);
	}

	public double get(double timestamp) {
		int idx = interpolationSearch(timestamp);
		if (idx == Integer.MIN_VALUE || idx == Integer.MAX_VALUE) {
			return Double.NaN;
		}
		if (idx < 0) {
			return interpolate(idx, timestamp);
		}

		return content.get(offset[idx], 1);
	}

	private double interpolate(int idx, double timestamp) {
		int p1 = offset[-idx - 1];
		int p2 = offset[-idx];

		return (content.get(p2, 1) - content.get(p1, 1))
				/ (content.get(p2, 0) - content.get(p1, 0)) * timestamp;
	}

	/**
	 * This function implements an interpolation search for fast access to a
	 * sample based on a timestamp.
	 * 
	 * @param timestamp
	 *            - the timestamp of the required sample
	 * @return Integer.MIN_VALUE if the timestamp is before all samples in this
	 *         time series, Integer.MAX_VALUE if the timestamp is after all
	 *         samples in this time series, a non-negative integer if there is a
	 *         perfect match, a negative integer if the timestamp lies between
	 *         two samples (returns the index of the right sample)
	 */
	private int interpolationSearch(double timestamp) {
		// execute an interpolation search
		double first = startTime;
		double last = endTime;
		int firstIdx = 0;
		int lastIdx = offset.length - 1;

		if (timestamp < first) {
			return Integer.MIN_VALUE;
		} else if (timestamp > last) {
			return Integer.MAX_VALUE;
		}

		while (timestamp >= first && timestamp <= last) {
			double span = last - first;
			int pos = firstIdx
					+ (int) ((lastIdx - firstIdx) * (timestamp - first) / span);

			double cur = content.get(offset[pos], 0);
			if (cur == timestamp) {
				return pos;
			} else if (cur > timestamp) {
				lastIdx = pos - 1;
			} else if (cur < timestamp) {
				firstIdx = pos + 1;
			}

			first = content.get(offset[firstIdx], 0);
			last = content.get(offset[lastIdx], 0);
		}

		if (first < timestamp) {
			return -(firstIdx + 1);
		} else {
			return -(firstIdx);
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

}
