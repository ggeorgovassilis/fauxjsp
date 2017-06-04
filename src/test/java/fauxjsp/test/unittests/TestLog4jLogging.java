package fauxjsp.test.unittests;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Assert;
import org.junit.Test;

import fauxjsp.api.logging.Logger;
import fauxjsp.impl.logging.log4j.Log4jFactory;

public class TestLog4jLogging {

	@Test
	public void testLogging() {
		Log4jFactory factory = new Log4jFactory();
		org.apache.log4j.Logger loggerImpl= org.apache.log4j.Logger.getLogger("testlogger");
		
		final StringBuffer sb = new StringBuffer();
		loggerImpl.addAppender(new Appender() {
			
			@Override
			public void setName(String name) {
			}
			
			@Override
			public void setLayout(Layout layout) {
			}
			
			@Override
			public void setErrorHandler(ErrorHandler errorHandler) {
			}
			
			@Override
			public boolean requiresLayout() {
				return false;
			}
			
			@Override
			public String getName() {
				return null;
			}
			
			@Override
			public Layout getLayout() {
				return null;
			}
			
			@Override
			public Filter getFilter() {
				return null;
			}
			
			@Override
			public ErrorHandler getErrorHandler() {
				return null;
			}
			
			@Override
			public void doAppend(LoggingEvent event) {
				sb.append(event.getLevel()+" "+event.getMessage()+"\n");
			}
			
			@Override
			public void close() {
			}
			
			@Override
			public void clearFilters() {
			}
			
			@Override
			public void addFilter(Filter newFilter) {
			}
		});
		Logger logger = factory.getLogger("testlogger");
		logger.trace("trace message");
		logger.debug("debug message");
		logger.info("info message");
		logger.warn("warn message");
		logger.error("error message");
		logger.error("error exception", new RuntimeException("Testing error logger, please ignore this exception"));

		String expected = "INFO info message\n" + "WARN warn message\n" + "ERROR error message\n"
				+ "ERROR error exception\n";
		Assert.assertEquals(expected, sb.toString());
	}
}
