package edu.kit.ipd.descartes.redeem.estimation.io;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;

public class MatrixMeasurementStream implements MeasurementStream {
	
	private Matrix samples;
	private int curPos = 0;
	
	public MatrixMeasurementStream(Matrix samples) {
		if (samples == null) {
			throw new IllegalArgumentException("observations == null");
		}
		this.samples = samples;
	}
	
	@Override
	public boolean nextSample() {
		curPos++;
		return curPos < samples.rows();
	}

	@Override
	public Vector getCurrentSample() {
		if (curPos < samples.rows()) {
			return samples.row(curPos);
		}
		return null;
	}

	@Override
	public int size() {
		return samples.columns();
	}
	
}
