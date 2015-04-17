package tools.descartes.librede.linalg;

public abstract class Indices {
	
	protected Indices() {
	}
	
	public abstract int length();
	
	public abstract int get(int idx);
	
	public abstract boolean isContinuous();

}
