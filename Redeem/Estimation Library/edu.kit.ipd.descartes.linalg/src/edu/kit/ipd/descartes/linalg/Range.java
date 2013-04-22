package edu.kit.ipd.descartes.linalg;

public class Range {
	
	private final int start;
	private final int end;	
	
	public Range(int start, int end) {
		super();
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
}
