package fauxjsp.impl.logging;

import fauxjsp.api.logging.LogFactory;
import fauxjsp.api.logging.Logger;
import fauxjsp.impl.logging.java.JavaLoggingFactory;

/**
 * Get a logger instance
 * @author George Georgovassilis
 *
 */
public class Logging {

	public static LogFactory logFactory;
	
	protected static boolean classExists(String fullName){
		try {
			return null!=Class.forName(fullName);
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
	
	protected static LogFactory instantiate(String className){
		try {
			return (LogFactory)(Class.forName(className).newInstance());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected static void setupLogging(){
		if (classExists("org.apache.log4j.Logger"))
			logFactory = instantiate("fauxjsp.impl.logging.log4j.Log4jFactory");
		else
		logFactory = new JavaLoggingFactory();
	}
	
	public static Logger getLogger(String name){
		if (logFactory == null)
			setupLogging();
		return logFactory.getLogger(name);
	}

	public static Logger getLogger(Class<?> c){
		if (logFactory == null)
			setupLogging();
		return logFactory.getLogger(c);
	}

}
