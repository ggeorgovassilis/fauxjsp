package fauxjsp.api.logging;

/**
 * Factory for loggers
 * @author George Georgovassilis
 *
 */
public interface LogFactory {

	Logger getLogger(String name);
	Logger getLogger(Class<?> c);
}
