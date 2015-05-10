package swp15.link_discovery.model.metric;

/**
 * Exception if Metric is not in the right Format
 *
 */
public class MetricFormatException extends RuntimeException {

	/**
	 * serialVersion
	 */
	private static final long serialVersionUID = 7711263588635597763L;

	/**
	 * Constructor
	 */
	public MetricFormatException() {
		super();
	}

	/**
	 * Constructor with Message
	 * 
	 * @param s
	 *            Message
	 */
	public MetricFormatException(String s) {
		super(s);
	}

	/**
	 * Constructor with Message and Cause of Exception
	 * 
	 * @param s
	 *            Message
	 * @param cause
	 *            Cause of Exception
	 */
	public MetricFormatException(String s, Throwable cause) {
		super(s, cause);
	}
}