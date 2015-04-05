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

import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.log4j.Logger;

import tools.descartes.librede.datasource.AbstractFileDataSource;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.registry.ParameterDefinition;

@Component(displayName = "CSV Data Source")
public class CsvDataSource extends AbstractFileDataSource {
	
	private static final Logger log = Logger.getLogger(CsvDataSource.class);

	@ParameterDefinition(name = "Separators", label = "Separators", required = false, defaultValue = ",")
	private String separators;

	@ParameterDefinition(name = "SkipFirstLine", label = "Skip First Line", required = false, defaultValue = "false")
	private boolean skipFirstLine;
	
	@ParameterDefinition(name = "TimestampFormat", label = "Timestamp Format", required = false, defaultValue = "")
	private String timestampFormatPattern;
	
	@ParameterDefinition(name = "NumberLocale", label = "Number Locale", required = false, defaultValue = "en_US")
	private String numberLocale;
	
	private int readLines = 0;
	private SimpleDateFormat timestampFormat;
	private NumberFormat numberFormat;
	
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
	protected boolean skipLine(File file, String line) {
		readLines++;
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
	protected double parse(File file, String line, double[] values) throws ParseException {
		String[] fields = line.split(separators);
		if (fields.length >= 1) {
			double timestamp = getTimestamp(file, fields[0]);
			if (Double.isNaN(timestamp)) {
				throw new ParseException("Timestamp is invalid", readLines);
			}
			// We only consider columns < values.length,
			// Possible additional columns are not required and are
			// discarded.
			for (int i = 0; i < values.length; i++) {
				if ((i + 1) < fields.length) {
					values[i] = getNumber(file, fields[i + 1]);
				} else {					
					values[i] = Double.NaN;
				}
			}
			return timestamp;
		}
		throw new AssertionError(); // We should never be here if programmed correctly.
	}

	private double getTimestamp(File file, String timestamp) {
		if (timestampFormatPattern != null && !timestampFormatPattern.isEmpty()) {
			timestampFormat = new SimpleDateFormat(timestampFormatPattern);
		}
		if (timestampFormat == null) {
			return getNumber(file, timestamp);
		} else {
			try {
				return timestampFormat.parse(timestamp.trim()).getTime() / 1000.0;
			} catch(ParseException ex) {
				logDiagnosis(file, "Skipping line due to invalid timestamp: " + timestamp);
			}
			return Double.NaN;
		}
	}

	private double getNumber(File file, String number) {
		if (numberFormat == null) {
			numberFormat = NumberFormat.getNumberInstance(Locale.forLanguageTag(numberLocale));
		}
		try {
			return numberFormat.parse(number.trim()).doubleValue();
		} catch(ParseException ex) {
			logDiagnosis(file, "Error parsing number: " + number);
		}	
		return Double.NaN;
	}
	
	private void logDiagnosis(File file, String message) {
		StringBuilder diagnosis = new StringBuilder(message);
		diagnosis.append(" (");
		diagnosis.append(file);
		diagnosis.append(":");
		diagnosis.append(readLines);
		diagnosis.append(")");
		log.warn(diagnosis.toString());
	}
	
}
