package tools.descartes.librede.units;

/**
 * This class describes a physical unit of measurement data.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 * @param <D> the dimension this unit belongs to
 */
public final class Unit<D extends IDimension> implements Comparable<Unit<D>> {
	
	private final String name;
	private final String symbol;
	private final double baseFactor;
	
	protected Unit(String name, String symbol, double baseFactor) {
		this.name = name;
		this.symbol = symbol;
		this.baseFactor = baseFactor;
	}
	
	/**
	 * A human-readable name of the unit.
	 * @return String
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * The physical symbol of the symbol.
	 * @return String
	 */
	public String getSymbol() {
		return symbol;
	}
	
	/**
	 * Converts the given value to another unit.
	 * @param value the input value of the source unit.
	 * @param targetUnit the target unit must have the same dimension.
	 * @return double
	 */
	public double convertTo(double value, Unit<D> targetUnit) {
		return value / targetUnit.baseFactor * baseFactor;
	}
	
	/**
	 * Converts the given value from another unit.
	 * @param value the input value of the source unit.
	 * @param sourceUnit the source unit must have the same dimension. 
	 * @return
	 */
	public double convertFrom(double value, Unit<D> sourceUnit) {
		return sourceUnit.convertTo(value, this);
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Unit<D> o) {
		return Double.compare(baseFactor, o.baseFactor);
	}	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Unit<?> other = (Unit<?>) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name + "(" + symbol + ")";
	}
}
