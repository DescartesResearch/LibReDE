package tools.descartes.librede.exceptions;

public class NonOverlappingRangeException extends Exception {

	private static final long serialVersionUID = 9076352020064611701L;

	public NonOverlappingRangeException() {
		super();
	}

	public NonOverlappingRangeException(String message, Throwable cause) {
		super(message, cause);
	}

	public NonOverlappingRangeException(String message) {
		super(message);
	}

	public NonOverlappingRangeException(Throwable cause) {
		super(cause);
	}
}
