package fauxjsp.impl.logging.java;

import fauxjsp.api.logging.LogFactory;
import fauxjsp.api.logging.Logger;

/**
 * Implementation with java logging
 * @author Georgios Georgovassilis
 *
 */
public class JavaLoggingFactory implements LogFactory{

	@Override
	public Logger getLogger(String name) {
		return new JavaLogImpl(java.util.logging.Logger.getLogger(name));
	}

	@Override
	public Logger getLogger(Class<?> c) {
		return getLogger(c.getCanonicalName());
	}

}
