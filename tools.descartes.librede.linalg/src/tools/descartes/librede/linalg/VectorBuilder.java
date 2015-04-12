package tools.descartes.librede.linalg;

public class VectorBuilder {
	
	private double[] values;
	
	public VectorBuilder(int rows) {
		values = new double[rows];
	}

	public void set(int row, double value) {
		this.values[row] = value;
	}
	
	public Vector toVector() {
		return LinAlg.vector(values);
	}
}
