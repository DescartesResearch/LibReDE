package tools.descartes.librede.units;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Unit for a proportion (e.g., utilization)
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 */
public final class Proportion implements IDimension {
	
	public static final Proportion INSTANCE = new Proportion();
	
	public static final Unit<Proportion> NONE = new Unit<Proportion>("none", "", 1);
	public static final Unit<Proportion> PERCENTAGE = new Unit<Proportion>("precentage", "%", 100);
	
	private static final List<Unit<? extends IDimension>> UNITS = Collections.unmodifiableList(Arrays.asList(NONE, PERCENTAGE));
	
	private Proportion() {}
	
	@Override
	public Unit<? extends IDimension> getBaseUnit() {
		return NONE;
	}
	@Override
	public List<Unit<? extends IDimension>> getUnits() {
		return UNITS;
	}
	

}
