package fauxjsp.impl.logging.log4j;

import fauxjsp.api.logging.LogFactory;
import fauxjsp.api.logging.Logger;

/**
 * Logger implementation for log4j
 * @author George Georgovassilis
 *
 */
public class Log4jFactory implements LogFactory{

	@Override
	public Logger getLogger(String name) {
		return new Log4jLogger(org.apache.log4j.Logger.getLogger(name));
	}

	@Override
	public Logger getLogger(Class<?> c) {
		return new Log4jLogger(org.apache.log4j.Logger.getLogger(c));
	}

}
