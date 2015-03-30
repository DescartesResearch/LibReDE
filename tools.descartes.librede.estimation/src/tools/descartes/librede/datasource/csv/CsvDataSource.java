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
package tools.descartes.librede.datasource.csv;

import static tools.descartes.librede.linalg.LinAlg.vector;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import tools.descartes.librede.datasource.IDataSource;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorFunction;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.registry.ParameterDefinition;
import tools.descartes.librede.repository.TimeSeries;

@Component(displayName = "CSV Data Source")
public class CsvDataSource implements IDataSource {
	
	private static final Logger log = Logger.getLogger(CsvDataSource.class);

	@ParameterDefinition(name = "Separators", label = "Separators", required = false, defaultValue = ",")
	private String separators;

	@ParameterDefinition(name = "SkipFirstLine", label = "Skip First Line", required = false, defaultValue = "false")
	private boolean skipFirstLine;
	
	@ParameterDefinition(name = "TimestampFormat", label = "Timestamp Format", required = false, defaultValue = "")
	private String timestampFormat;
	
	@ParameterDefinition(name = "NumberLocale", label = "Number Locale", required = false, defaultValue = "en_US")
	private String numberLocale;
	
	public TimeSeries load(InputStream in, int column) throws Exception {
		SimpleDateFormat format = null;
		if (timestampFormat != null && !timestampFormat.isEmpty()) {
			format = new SimpleDateFormat(timestampFormat);
		}
		
		NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.forLanguageTag(numberLocale));
		
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
						if (fields.length >= 2) {
							try {
								if (format == null) {
									timestamps.add(numberFormat.parse(fields[0]).doubleValue());									
								} else {
									try {
										timestamps.add(format.parse(fields[0]).getTime() / 1000.0);
									} catch(ParseException ex) {
										log.error("Error parsing date: " + fields[0]);
									}
								}
								if (column >= fields.length) {
									log.error("Error parsing line " + lineNumber
											+ ": too few columns.");
									values.add(Double.NaN);
								} else {
									values.add(numberFormat.parse(fields[column].trim()).doubleValue());
								}
							} catch (ParseException ex) {
								log.error("Error parsing value in line " + lineNumber
										+ ": " + fields[column].trim());
								values.add(Double.NaN);
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
