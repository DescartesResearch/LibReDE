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
package edu.kit.ipd.descartes.librede.datasource.csv;

import static edu.kit.ipd.descartes.linalg.LinAlg.vector;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.kit.ipd.descartes.librede.datasource.IDataSource;
import edu.kit.ipd.descartes.librede.estimation.repository.TimeSeries;
import edu.kit.ipd.descartes.librede.registry.Component;
import edu.kit.ipd.descartes.librede.registry.ParameterDefinition;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorFunction;

@Component(displayName = "CSV Data Source")
public class CsvDataSource implements IDataSource {
	
	private static final Logger log = Logger.getLogger(CsvDataSource.class);

	@ParameterDefinition(name = "Separators", label = "Separators", required = false, defaultValue = ",")
	private String separators;

	@ParameterDefinition(name = "SkipFirstLine", label = "Skip First Line", required = false, defaultValue = "false")
	private boolean skipFirstLine;
	
	public TimeSeries load(InputStream in, int column) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		try {
			final List<Double> timestamps = new ArrayList<Double>();
			final List<Double> values = new ArrayList<Double>();
			String line;
			int lineNumber = 0;
			while ((line = reader.readLine()) != null) {
				lineNumber++;
				if (!(skipFirstLine && lineNumber == 1)) {
					if (!line.startsWith("#")) {
						String[] fields = line.split(separators);
						if (fields.length == 2) {
							try {
								timestamps.add(Double.parseDouble(fields[0]));
								if (column >= fields.length) {
									log.error("Error parsing line " + lineNumber
											+ ": too few columns.");
									values.add(Double.NaN);
								} else {
									values.add(Double.parseDouble(fields[column]));
								}
							} catch (NumberFormatException ex) {
								log.error("Error parsing line " + lineNumber
										+ ": could not parse number.");
							}
						} else {
							log.error("Error parsing line " + lineNumber
									+ ": could not find seperator char.");
						}
					}
				}
			}

			Vector v1 = vector(timestamps.size(), new VectorFunction() {
				@Override
				public double cell(int row) {
					return timestamps.get(row);
				}
			});

			Vector v2 = vector(values.size(), new VectorFunction() {
				@Override
				public double cell(int row) {
					return values.get(row);
				}
			});
			return new TimeSeries(v1, v2);
		} finally {
			reader.close();
		}
	}

}
