package fauxjsp.api.logging;

/**
 * Facade for logger
 * @author George Georgovassilis
 *
 */
public interface Logger {

	void error(String message);
	void error(String message, Throwable exception);
	void warn(String message);
	void info(String message);
	void debug(String message);
	void trace(String message);
}
