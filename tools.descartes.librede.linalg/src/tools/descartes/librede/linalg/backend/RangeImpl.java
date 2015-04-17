package tools.descartes.librede.linalg.backend;

import tools.descartes.librede.linalg.Indices;

public class RangeImpl extends Indices {
	
	private final int start;
	private final int end;
	
	public RangeImpl(int start, int end) {
		this.start = start;
		this.end = end;
	}
	
	public int getStart() {
		return start;
	}
	public int getEnd() {
		return end;
	}
	
	@Override
	public String toString() {
		return "Range: [" + start + ", " + end + ")";
	}

	public int getLength() {
		return end - start;
	}

	@Override
	public int length() {
		return end - start;
	}

	@Override
	public int get(int idx) {
		int res = start + idx;
		if (res >= end) {
			throw new ArrayIndexOutOfBoundsException(idx);
		}
		return res;
	}

	@Override
	public boolean isContinuous() {
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + end;
		result = prime * result + start;
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
		RangeImpl other = (RangeImpl) obj;
		if (end != other.end)
			return false;
		if (start != other.start)
			return false;
		return true;
	}
	
	

}
