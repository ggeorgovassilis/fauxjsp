package fauxjsp.test.unittests;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import org.junit.Assert;
import org.junit.Test;

import fauxjsp.api.logging.Logger;
import fauxjsp.impl.logging.java.JavaLoggingFactory;

public class TestJavaLogging {

	@Test
	public void testLogging() {
		JavaLoggingFactory factory = new JavaLoggingFactory();
		java.util.logging.Logger loggerImpl = java.util.logging.Logger.getLogger("testlogger");
		final StringBuffer sb = new StringBuffer();
		loggerImpl.addHandler(new Handler() {

			@Override
			public void publish(LogRecord record) {
				sb.append(record.getLevel().getName() + " " + record.getMessage() + "\n");
			}

			@Override
			public void flush() {
			}

			@Override
			public void close() throws SecurityException {
			}
		});
		Logger logger = factory.getLogger("testlogger");
		logger.trace("trace message");
		logger.debug("debug message");
		logger.info("info message");
		logger.warn("warn message");
		logger.error("error message");
		logger.error("error exception", new RuntimeException("Testing error logger, please ignore this exception"));

		String expected = "INFO info message\n" + "WARNING warn message\n" + "SEVERE error message\n"
				+ "SEVERE error exception\n";
		Assert.assertEquals(expected, sb.toString());
	}
}
