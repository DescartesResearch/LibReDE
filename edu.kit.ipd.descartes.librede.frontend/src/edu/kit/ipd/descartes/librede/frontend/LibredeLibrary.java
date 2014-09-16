package edu.kit.ipd.descartes.librede.frontend;

import edu.kit.ipd.descartes.librede.approaches.IEstimationApproach;
import edu.kit.ipd.descartes.librede.approaches.MenasceOptimizationApproach;
import edu.kit.ipd.descartes.librede.approaches.ResponseTimeApproximationApproach;
import edu.kit.ipd.descartes.librede.approaches.RoliaRegressionApproach;
import edu.kit.ipd.descartes.librede.approaches.ServiceDemandLawApproach;
import edu.kit.ipd.descartes.librede.approaches.WangKalmanFilterApproach;
import edu.kit.ipd.descartes.librede.approaches.ZhangKalmanFilterApproach;
import edu.kit.ipd.descartes.librede.datasource.IDataSource;
import edu.kit.ipd.descartes.librede.datasource.csv.CsvDataSource;
import edu.kit.ipd.descartes.librede.estimation.repository.StandardMetric;
import edu.kit.ipd.descartes.librede.estimation.validation.ResponseTimeValidator;
import edu.kit.ipd.descartes.librede.estimation.validation.UtilizationValidator;
import edu.kit.ipd.descartes.librede.estimation.validation.Validator;
import edu.kit.ipd.descartes.librede.export.IExporter;
import edu.kit.ipd.descartes.librede.export.csv.CsvExporter;
import edu.kit.ipd.descartes.librede.factory.Registry;

public class LibredeLibrary {
	
	public static void init() {
		
		for (StandardMetric metric : StandardMetric.class.getEnumConstants()) {
			Registry.INSTANCE.registerMetric(metric.toString(), metric);
		}
		
		Registry.INSTANCE.registerImplementationType(IDataSource.class, CsvDataSource.class);		
		
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, ResponseTimeApproximationApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, ServiceDemandLawApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, RoliaRegressionApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, MenasceOptimizationApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, ZhangKalmanFilterApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, WangKalmanFilterApproach.class);
		
		Registry.INSTANCE.registerImplementationType(Validator.class, ResponseTimeValidator.class);
		Registry.INSTANCE.registerImplementationType(Validator.class, UtilizationValidator.class);
		
		Registry.INSTANCE.registerImplementationType(IExporter.class, CsvExporter.class);
	}

}
