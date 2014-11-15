/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2014, by Simon Spinner and Contributors.
 *
 * Project Info:   http://www.descartes-research.net/
 *
 * All rights reserved. This software is made available under the terms of the
 * Eclipse Public License (EPL) v1.0 as published by the Eclipse Foundation
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Eclipse Public License (EPL)
 * for more details.
 *
 * You should have received a copy of the Eclipse Public License (EPL)
 * along with this software; if not visit http://www.eclipse.org or write to
 * Eclipse Foundation, Inc., 308 SW First Avenue, Suite 110, Portland, 97204 USA
 * Email: license (at) eclipse.org
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 */
package tools.descartes.librede;

import tools.descartes.librede.algorithm.IEstimationAlgorithm;
import tools.descartes.librede.algorithm.SimpleApproximation;
import tools.descartes.librede.approach.IEstimationApproach;
import tools.descartes.librede.approach.MenasceOptimizationApproach;
import tools.descartes.librede.approach.ResponseTimeApproximationApproach;
import tools.descartes.librede.approach.RoliaRegressionApproach;
import tools.descartes.librede.approach.ServiceDemandLawApproach;
import tools.descartes.librede.approach.WangKalmanFilterApproach;
import tools.descartes.librede.approach.ZhangKalmanFilterApproach;
import tools.descartes.librede.datasource.IDataSource;
import tools.descartes.librede.datasource.csv.CsvDataSource;
import tools.descartes.librede.export.IExporter;
import tools.descartes.librede.export.csv.CsvExporter;
import tools.descartes.librede.registry.Registry;
import tools.descartes.librede.repository.StandardMetric;
import tools.descartes.librede.validation.IValidator;
import tools.descartes.librede.validation.ResponseTimeValidator;
import tools.descartes.librede.validation.UtilizationValidator;

public class LibredeLibrary {
	
	public static void init() {
		
		for (StandardMetric metric : StandardMetric.class.getEnumConstants()) {
			Registry.INSTANCE.registerMetric(metric.toString(), metric);
		}
		
		Registry.INSTANCE.registerImplementationType(IDataSource.class, CsvDataSource.class);
		
		Registry.INSTANCE.registerImplementationType(IEstimationAlgorithm.class, SimpleApproximation.class);
		
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, ResponseTimeApproximationApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, ServiceDemandLawApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, RoliaRegressionApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, MenasceOptimizationApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, ZhangKalmanFilterApproach.class);
		Registry.INSTANCE.registerImplementationType(IEstimationApproach.class, WangKalmanFilterApproach.class);
		
		Registry.INSTANCE.registerImplementationType(IValidator.class, ResponseTimeValidator.class);
		Registry.INSTANCE.registerImplementationType(IValidator.class, UtilizationValidator.class);
		
		Registry.INSTANCE.registerImplementationType(IExporter.class, CsvExporter.class);
	}

}
