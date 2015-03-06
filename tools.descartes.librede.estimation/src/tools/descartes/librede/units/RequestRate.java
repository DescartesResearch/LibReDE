package tools.descartes.librede.units;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Unit for request rates (e.g., throughput).
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 */
public final class RequestRate implements IDimension {
	
	public static final RequestRate INSTANCE = new RequestRate();
	
	public static final Unit<RequestRate> REQ_PER_NANOSECOND = new Unit<RequestRate>("requests_per_nanosecond", "req/ns", 1e9);
	public static final Unit<RequestRate> REQ_PER_MICROSECOND = new Unit<RequestRate>("requests_per_microsecond", "req/\u00B5s", 1e6);
	public static final Unit<RequestRate> REQ_PER_MILLISECOND = new Unit<RequestRate>("requests_per_millisecond", "req/ms", 1e3);
	public static final Unit<RequestRate> REQ_PER_SECOND = new Unit<RequestRate>("requests_per_second", "req/s", 1);
	public static final Unit<RequestRate> REQ_PER_MINUTE = new Unit<RequestRate>("requests_per_minute", "req/min", 1.0 / 60);
	public static final Unit<RequestRate> REQ_PER_HOUR = new Unit<RequestRate>("requests_per_hour", "req/h", 1.0 / (60 * 60));
	public static final Unit<RequestRate> REQ_PER_DAY = new Unit<RequestRate>("requests_per_day", "req/d", 1.0 / (60 * 60 * 24));
	
	private static final List<Unit<? extends IDimension>> UNITS = Collections.unmodifiableList(Arrays.asList(REQ_PER_NANOSECOND, REQ_PER_MICROSECOND, 
			REQ_PER_MILLISECOND, REQ_PER_SECOND, REQ_PER_MINUTE, REQ_PER_HOUR, REQ_PER_DAY));
	
	private RequestRate() {}

	@Override
	public Unit<? extends IDimension> getBaseUnit() {
		return REQ_PER_SECOND;
	}

	@Override
	public List<Unit<? extends IDimension>> getUnits() {
		return UNITS;
	}

}
