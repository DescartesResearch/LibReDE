package edu.kit.ipd.descartes.linalg.backend;

import edu.kit.ipd.descartes.linalg.Vector;

public abstract class AbstractVector extends AbstractMatrix implements Vector {

	@Override
	public boolean isVector() {
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for (int i = 0; i < rows(); i++) {
			if (i > 0) {
				builder.append("; ");
			}
			builder.append(get(i));
		}
		builder.append("]");
		return builder.toString();
	}

}
