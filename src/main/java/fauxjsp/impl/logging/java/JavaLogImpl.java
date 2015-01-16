package fauxjsp.impl.logging.java;

import java.util.logging.Level;

import fauxjsp.api.logging.Logger;

/**
 * Java logging implementation
 * @author George Georgovassilis
 *
 */
public class JavaLogImpl implements Logger{

	protected java.util.logging.Logger impl;
	
	public JavaLogImpl(java.util.logging.Logger impl) {
		this.impl = impl;
	}
	
	@Override
	public void error(String message) {
		impl.log(Level.SEVERE, message);
	}

	@Override
	public void error(String message, Throwable exception) {
		impl.log(Level.SEVERE, message, exception);
	}

	@Override
	public void warn(String message) {
		impl.log(Level.WARNING, message);
	}

	@Override
	public void info(String message) {
		impl.log(Level.INFO, message);
	}

	@Override
	public void debug(String message) {
		impl.log(Level.FINE, message);
	}

	@Override
	public void trace(String message) {
		impl.log(Level.FINEST, message);
	}

}
