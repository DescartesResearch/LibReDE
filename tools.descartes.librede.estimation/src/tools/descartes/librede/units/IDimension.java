package tools.descartes.librede.units;

import java.util.List;

/**
 * Each physical unit has a dimension (e.g., time). Different units of the same
 * dimension can be converted to each other.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 */
public interface IDimension {

	/**
	 * @return the default unit of this dimension.
	 */
	public Unit<? extends IDimension> getBaseUnit();

	/**
	 * @return a list of all defined units of this dimension.
	 */
	public List<Unit<? extends IDimension>> getUnits();

}
