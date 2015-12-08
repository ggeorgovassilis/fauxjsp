package fauxjsp.impl.logging;

import fauxjsp.api.logging.LogFactory;
import fauxjsp.api.logging.Logger;
import fauxjsp.impl.Utils;
import fauxjsp.impl.logging.java.JavaLoggingFactory;

/**
 * Logging. JspServlet will instantiate it by calling setupLogging which sets up
 * a log4j logger if found in the classpath, otherwise java.util.logging is
 * used. Other logging implementations can be set with
 * {@link #setLogFactory(LogFactory)} at any time.
 * 
 * @author George Georgovassilis
 *
 */
public class Logging {

	public static LogFactory logFactory;

	protected static boolean classExists(String fullName) {
		try {
			return null != Utils.getClassForName(fullName);
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	protected static LogFactory instantiate(String className) {
		try {
			return (LogFactory) (Utils.getClassForName(className).newInstance());
		} catch (Exception e) {
			throw Utils.translate(e);
		}
	}

	public static void setLogFactory(LogFactory logFactory) {
		Logging.logFactory = logFactory;
	}

	public static void setupLogging() {
		LogFactory factory;
		if (classExists("org.apache.log4j.Logger"))
			factory = instantiate("fauxjsp.impl.logging.log4j.Log4jFactory");
		else
			factory = new JavaLoggingFactory();
		setLogFactory(factory);
	}

	public static Logger getLogger(Class<?> c) {
		if (logFactory == null)
			setupLogging();
		return logFactory.getLogger(c);
	}

}
