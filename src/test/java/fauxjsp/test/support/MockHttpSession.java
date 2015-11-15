package fauxjsp.test.support;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 * POJO implementation of an {@link HttpSession}
 * @author George Georgovassilis
 *
 */

@SuppressWarnings("deprecation")
public class MockHttpSession implements HttpSession {

	@Override
	public long getCreationTime() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getId() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public long getLastAccessedTime() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public ServletContext getServletContext() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void setMaxInactiveInterval(int interval) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public int getMaxInactiveInterval() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public HttpSessionContext getSessionContext() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Object getAttribute(String name) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Object getValue(String name) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String[] getValueNames() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void setAttribute(String name, Object value) {
		throw new RuntimeException("Not implemented");

	}

	@Override
	public void putValue(String name, Object value) {
		throw new RuntimeException("Not implemented");

	}

	@Override
	public void removeAttribute(String name) {
		throw new RuntimeException("Not implemented");

	}

	@Override
	public void removeValue(String name) {
		throw new RuntimeException("Not implemented");

	}

	@Override
	public void invalidate() {
		throw new RuntimeException("Not implemented");

	}

	@Override
	public boolean isNew() {
		throw new RuntimeException("Not implemented");
	}

}
