package tools.descartes.librede.repository;

import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.units.Unit;

public class UnitConverter {
	
	public static TimeSeries convertTo(TimeSeries series, Unit sourceUnit, Unit targetUnit) {
		if (!sourceUnit.getDimension().equals(targetUnit.getDimension())) {
			throw new IllegalArgumentException("Incompabtible dimensions");
		}
		if (sourceUnit.equals(targetUnit)) {
			return series;
		}
		Matrix convertedData = series.getData().times(sourceUnit.getBaseFactor() / targetUnit.getBaseFactor());
		return new TimeSeries(series.getTime(), convertedData);
	}

}
