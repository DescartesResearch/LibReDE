package tools.descartes.librede.units;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Unit for number of requests (e.g., arrival or departure count).
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
public class RequestCount implements IDimension {
	
	public static final RequestCount INSTANCE = new RequestCount();
	
	public static final Unit<RequestCount> REQUESTS = new Unit<RequestCount>("requests", "req", 1);
	
	private static final List<Unit<? extends IDimension>> UNITS = Collections.unmodifiableList(Arrays.asList(REQUESTS));

	private RequestCount() {}
	
	@Override
	public Unit<? extends IDimension> getBaseUnit() {
		return REQUESTS;
	}

	@Override
	public List<Unit<? extends IDimension>> getUnits() {
		return UNITS;
	}

}
