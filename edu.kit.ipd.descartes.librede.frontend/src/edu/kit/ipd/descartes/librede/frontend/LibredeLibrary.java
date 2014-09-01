package edu.kit.ipd.descartes.librede.frontend;

import edu.kit.ipd.descartes.librede.approaches.IEstimationApproach;
import edu.kit.ipd.descartes.librede.approaches.MenasceOptimizationApproach;
import edu.kit.ipd.descartes.librede.approaches.ResponseTimeApproximationApproach;
import edu.kit.ipd.descartes.librede.approaches.RoliaRegressionApproach;
import edu.kit.ipd.descartes.librede.approaches.ServiceDemandLawApproach;
import edu.kit.ipd.descartes.librede.approaches.WangKalmanFilterApproach;
import edu.kit.ipd.descartes.librede.approaches.ZhangKalmanFilterApproach;
import edu.kit.ipd.descartes.librede.datasource.IDataItem;
import edu.kit.ipd.descartes.librede.datasource.IDataSource;
import edu.kit.ipd.descartes.librede.datasource.csv.CsvDataSource;
import edu.kit.ipd.descartes.librede.datasource.csv.CsvFile;
import edu.kit.ipd.descartes.librede.estimation.validation.ResponseTimeValidator;
import edu.kit.ipd.descartes.librede.estimation.validation.UtilizationValidator;
import edu.kit.ipd.descartes.librede.estimation.validation.Validator;
import edu.kit.ipd.descartes.librede.export.IExporter;
import edu.kit.ipd.descartes.librede.export.csv.CsvExporter;
import edu.kit.ipd.descartes.librede.factory.ComponentRegistry;

public class LibredeLibrary {
	
	public static void init() {
		ComponentRegistry.INSTANCE.registerImplementationType(IDataSource.class, CsvDataSource.class);		
		ComponentRegistry.INSTANCE.registerImplementationType(IDataItem.class, CsvFile.class);
		
		ComponentRegistry.INSTANCE.registerItemType(CsvDataSource.class, CsvFile.class);
		
		ComponentRegistry.INSTANCE.registerImplementationType(IEstimationApproach.class, ResponseTimeApproximationApproach.class);
		ComponentRegistry.INSTANCE.registerImplementationType(IEstimationApproach.class, ServiceDemandLawApproach.class);
		ComponentRegistry.INSTANCE.registerImplementationType(IEstimationApproach.class, RoliaRegressionApproach.class);
		ComponentRegistry.INSTANCE.registerImplementationType(IEstimationApproach.class, MenasceOptimizationApproach.class);
		ComponentRegistry.INSTANCE.registerImplementationType(IEstimationApproach.class, ZhangKalmanFilterApproach.class);
		ComponentRegistry.INSTANCE.registerImplementationType(IEstimationApproach.class, WangKalmanFilterApproach.class);
		
		ComponentRegistry.INSTANCE.registerImplementationType(Validator.class, ResponseTimeValidator.class);
		ComponentRegistry.INSTANCE.registerImplementationType(Validator.class, UtilizationValidator.class);
		
		ComponentRegistry.INSTANCE.registerImplementationType(IExporter.class, CsvExporter.class);
	}

}
