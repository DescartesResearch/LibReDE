package tools.descartes.librede.linalg.backend;

import java.util.Arrays;

import tools.descartes.librede.linalg.Indices;
import tools.descartes.librede.linalg.VectorFunction;

public class IndicesImpl extends Indices {
	
	protected final int[] indices;
	
	public IndicesImpl(int... indices) {
		this.indices = indices;
	}
	
	public IndicesImpl(int length, VectorFunction init) {
		indices = new int[length];
		for (int i = 0; i < length; i++) {
			indices[i] = (int) init.cell(i);
		}
	}
	
	public int[] getIndices() {
		return indices;
	}
	
	@Override
	public int length() {
		return indices.length;
	}

	@Override
	public int get(int idx) {
		return indices[idx];
	}
	
	@Override
	public boolean isContinuous() {
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(indices);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IndicesImpl other = (IndicesImpl) obj;
		if (!Arrays.equals(indices, other.indices))
			return false;
		return true;
	}



}
