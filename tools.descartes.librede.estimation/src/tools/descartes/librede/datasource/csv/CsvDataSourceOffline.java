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

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.log4j.Logger;

import tools.descartes.librede.datasource.AbstractFileDataSourceOffline;
import tools.descartes.librede.datasource.Loggers;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.registry.ParameterDefinition;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;

@Component(displayName = "CSV Data Source")
public class CsvDataSourceOffline extends AbstractFileDataSourceOffline {
	
	private static final Logger log = Loggers.DATASOURCE_LOG;

	@ParameterDefinition(name = "Separators", label = "Separators", required = false, defaultValue = ",")
	private String separators;

	@ParameterDefinition(name = "SkipFirstLine", label = "Skip First Line", required = false, defaultValue = "false")
	private boolean skipFirstLine;
	
	/*
	 * The timestamp format can be either a pattern as expected by java.util.SimpleDataFormat or if it
	 * is a numerical timestamp a specifier of the form [xx] where xx specifies the time unit, e.g., [ms]
	 */
	@ParameterDefinition(name = "TimestampFormat", label = "Timestamp Format", required = false, defaultValue = "")
	private String timestampFormatPattern;
	
	@ParameterDefinition(name = "NumberLocale", label = "Number Locale", required = false, defaultValue = "en_US")
	private String numberLocale;
	
	private SimpleDateFormat timestampFormat;
	private NumberFormat numberFormat;
	private Unit<Time> dateUnit = Time.SECONDS;
	private boolean initialized = false;
	
	public CsvDataSourceOffline() throws IOException {
		super();
	}
	
	public String getSeparators() {
		return separators;
	}

	public void setSeparators(String separators) {
		this.separators = separators;
	}

	public boolean isSkipFirstLine() {
		return skipFirstLine;
	}

	public void setSkipFirstLine(boolean skipFirstLine) {
		this.skipFirstLine = skipFirstLine;
	}

	public String getNumberLocale() {
		return numberLocale;
	}

	public void setNumberLocale(String numberLocale) {
		this.numberLocale = numberLocale;
	}

	public SimpleDateFormat getTimestampFormat() {
		return timestampFormat;
	}

	public void setTimestampFormat(SimpleDateFormat timestampFormat) {
		this.timestampFormat = timestampFormat;
	}

	@Override
	protected boolean skipLine(Stream stream, String line, int readLines) {
		if (skipFirstLine && readLines == 1) {
			return true;			
		}
		if (line.isEmpty()) {
			return true;
		}
		if (line.startsWith("#")) {
			return true;
		}
		return false;
	}

	@Override
	protected double parse(Stream stream, String line, String[] values, int readLines) throws ParseException {
		if (!initialized) {
			if (timestampFormatPattern != null && !timestampFormatPattern.isEmpty()) {
				if (timestampFormatPattern.startsWith("[") && timestampFormatPattern.endsWith("]")) {
					String unit = timestampFormatPattern.substring(1, timestampFormatPattern.length() - 1);
					for (Unit<?> u : Time.INSTANCE.getUnits()) {
						if (u.getSymbol().equalsIgnoreCase(unit)) {
							dateUnit = (Unit<Time>)u;
							break;
						}
					}
				} else {
					timestampFormat = new SimpleDateFormat(timestampFormatPattern);
					dateUnit = Time.MILLISECONDS;
				}
			}
			numberFormat = NumberFormat.getNumberInstance(Locale.forLanguageTag(numberLocale));
			initialized = true;
		}
		String[] fields = line.split(separators);
		if (fields.length >= 1) {
			double timestamp = getTimestamp(stream, fields[0], readLines);
			if (Double.isNaN(timestamp)) {
				throw new ParseException("Timestamp is invalid", readLines);
			}
			// We only consider columns < values.length,
			// Possible additional columns are not required and are
			// discarded.
			for (int i = 0; i < values.length; i++) {
				if ((i + 1) < fields.length) {
					values[i] = fields[i + 1].trim();
				} else {					
					values[i] = "";
				}
			}
			return timestamp;
		}
		throw new AssertionError(); // We should never be here if programmed correctly.
	}
	
	@Override
	protected double parseNumber(Stream stream, String value, int readLines) throws ParseException {
		return getNumber(stream, value, readLines);
	}

	private double getTimestamp(Stream stream, String timestamp, int readLines) {
		double time;
		if (timestampFormat == null) {
			time = getNumber(stream, timestamp, readLines);
		} else {
			try {
				time = timestampFormat.parse(timestamp.trim()).getTime();
			} catch(ParseException ex) {
				logDiagnosis(stream, "Skipping line due to invalid timestamp: " + timestamp, readLines);
			}
			return Double.NaN;
		}
		return dateUnit.convertTo(time, Time.SECONDS);
	}

	private double getNumber(Stream stream, String number, int readLines) {
		if (!number.isEmpty()) {
			try {
				return numberFormat.parse(number.trim()).doubleValue();
			} catch(ParseException ex) {
				logDiagnosis(stream, "Error parsing number: " + number, readLines);
			}
		}
		return Double.NaN;
	}
	
	private void logDiagnosis(Stream stream, String message, int readLines) {
		StringBuilder diagnosis = new StringBuilder(message);
		diagnosis.append(" (");
		diagnosis.append(stream);
		diagnosis.append(":");
		diagnosis.append(readLines);
		diagnosis.append(")");
		log.warn(diagnosis.toString());
	}
	
}
