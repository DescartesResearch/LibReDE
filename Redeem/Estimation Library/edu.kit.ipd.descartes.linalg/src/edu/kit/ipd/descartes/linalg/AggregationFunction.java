package edu.kit.ipd.descartes.linalg;

public enum AggregationFunction {
	
	COUNT {
		@Override
		public double apply(double x, double y) {
			if (Double.isNaN(y)) {
				return x;
			}
			if (Double.isNaN(x)) {
				return 1;
			}
			return x + 1;
		}
	},
	MINIMUM {

		@Override
		public double apply(double x, double y) {
			if (Double.isNaN(x)) {
				return y;
			}
			if (Double.isNaN(y)) {
				return x;
			}
			return (y < x) ? y : x;
		}
		
	},
	MAXIMUM {

		@Override
		public double apply(double x, double y) {
			if (Double.isNaN(x)) {
				return y;
			}
			if (Double.isNaN(y)) {
				return x;
			}
			return (y > x) ? y : x;
		}
		
	},
	SUM {

		@Override
		public double apply(double x, double y) {
			if (Double.isNaN(y)) {
				return x;
			}
			if (Double.isNaN(x)) {
				return y;
			}
			return x + y;
		}
		
	};
	
	
	public abstract double apply(double x, double y);
	
}
