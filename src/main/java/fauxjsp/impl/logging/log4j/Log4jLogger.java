package fauxjsp.impl.logging.log4j;

import fauxjsp.api.logging.Logger;

/**
 * Facade for log4j
 * @author George Georgovassilis
 *
 */
public class Log4jLogger implements Logger{

	protected org.apache.log4j.Logger impl;
	
	public Log4jLogger(org.apache.log4j.Logger impl) {
		this.impl = impl;
	}
	
	@Override
	public void error(String message) {
		impl.error(message);
	}

	@Override
	public void error(String message, Throwable exception) {
		impl.error(message, exception);
	}

	@Override
	public void warn(String message) {
		impl.warn(message);
	}

	@Override
	public void info(String message) {
		impl.info(message);
	}

	@Override
	public void debug(String message) {
		impl.debug(message);
	}

	@Override
	public void trace(String message) {
		impl.trace(message);
	}

}
