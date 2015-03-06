package tools.descartes.librede.units;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Units for time quantities (e.g., response time).
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 */
public final class Time implements IDimension {
	
	public static final Time INSTANCE = new Time();
	
	public static final Unit<Time> NANOSECONDS = new Unit<Time>("nanoseconds", "ns", 1e-9);
	public static final Unit<Time> MICROSECONDS = new Unit<Time>("microseconds", "\u00B5s", 1e-6);
	public static final Unit<Time> MILLISECONDS = new Unit<Time>("milliseconds", "ms", 1e-3);
	public static final Unit<Time> SECONDS = new Unit<Time>("seconds", "s", 1);
	public static final Unit<Time> MINUTES = new Unit<Time>("minutes", "min", 60);
	public static final Unit<Time> HOURS = new Unit<Time>("hours", "h", 60 * 60);
	public static final Unit<Time> DAYS = new Unit<Time>("days", "d", 60 * 60 * 24);
	
	private static final List<Unit<? extends IDimension>> UNITS = Collections.unmodifiableList(Arrays.asList(NANOSECONDS, MICROSECONDS, 
			MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS));
	
	private Time() {}

	@Override
	public Unit<? extends IDimension> getBaseUnit() {
		return SECONDS;
	}
	@Override
	public List<Unit<? extends IDimension>> getUnits() {
		return UNITS;
	}
}
