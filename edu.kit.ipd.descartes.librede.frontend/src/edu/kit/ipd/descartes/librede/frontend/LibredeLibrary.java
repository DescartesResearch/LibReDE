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
import edu.kit.ipd.descartes.librede.export.IExporter;
import edu.kit.ipd.descartes.librede.export.csv.CsvExporter;
import edu.kit.ipd.descartes.librede.registry.Registry;
import edu.kit.ipd.descartes.librede.repository.StandardMetric;
import edu.kit.ipd.descartes.librede.validation.ResponseTimeValidator;
import edu.kit.ipd.descartes.librede.validation.UtilizationValidator;
import edu.kit.ipd.descartes.librede.validation.Validator;

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
