package edu.kit.ipd.descartes.librede.approaches;

import java.util.HashMap;
import java.util.Map;

public class EstimationApproachFactory {
	
	private static Map<String, Class<?>> approaches = new HashMap<String, Class<?>>();
	
	static {
		approaches.put(ResponseTimeApproximationApproach.NAME, ResponseTimeApproximationApproach.class);
		approaches.put(ServiceDemandLawApproach.NAME, ServiceDemandLawApproach.class);
		approaches.put(RoliaRegressionApproach.NAME, RoliaRegressionApproach.class);
		approaches.put(ZhangKalmanFilterApproach.NAME, ZhangKalmanFilterApproach.class);
		approaches.put(WangKalmanFilterApproach.NAME, WangKalmanFilterApproach.class);
		approaches.put(MenasceOptimizationApproach.NAME, MenasceOptimizationApproach.class);
	}
	
	public static IEstimationApproach newEstimationApproach(String name) throws InstantiationException, IllegalAccessException {
		Class<?> cls = approaches.get(name);
		if (cls != null) {
			return (IEstimationApproach)cls.newInstance();
		}
		return null;
	}

}
