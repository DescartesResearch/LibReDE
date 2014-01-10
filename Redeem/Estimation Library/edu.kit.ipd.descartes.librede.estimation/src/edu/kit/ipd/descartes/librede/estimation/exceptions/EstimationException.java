package edu.kit.ipd.descartes.librede.estimation.exceptions;

public class EstimationException extends Exception {

	private static final long serialVersionUID = 388659551537319011L;

	public EstimationException() {
		super();
	}

	public EstimationException(String message, Throwable cause) {
		super(message, cause);
	}

	public EstimationException(String message) {
		super(message);
	}

	public EstimationException(Throwable cause) {
		super(cause);
	}
}
