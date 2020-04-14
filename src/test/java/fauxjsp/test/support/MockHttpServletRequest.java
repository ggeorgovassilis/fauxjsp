package fauxjsp.test.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

/**
 * POJO implementation of an {@link HttpServletRequest}
 * @author George Georgovassilis
 *
 */

public class MockHttpServletRequest implements HttpServletRequest{

	protected Map<String, Object> attributes = new HashMap<String, Object>();
	protected HttpSession session = new MockHttpSession();
	protected ServletContext servletContext = new MockServletContext();
	protected Locale locale = Locale.ENGLISH;
		
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	@Override
	public Object getAttribute(String name) {
		return attributes.get(name);
	}
	
	@Override
	public Enumeration<String> getAttributeNames() {
		return Collections.enumeration(attributes.keySet());
	}

	@Override
	public String getCharacterEncoding() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void setCharacterEncoding(String env)
			throws UnsupportedEncodingException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public int getContentLength() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public long getContentLengthLong() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getContentType() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getParameter(String name) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Enumeration<String> getParameterNames() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String[] getParameterValues(String name) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getProtocol() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getScheme() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getServerName() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public int getServerPort() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public BufferedReader getReader() throws IOException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getRemoteAddr() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getRemoteHost() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void setAttribute(String name, Object o) {
		attributes.put(name, o);
	}

	@Override
	public void removeAttribute(String name) {
		attributes.remove(name);
	}

	@Override
	public Locale getLocale() {
		return locale;
	}

	@Override
	public Enumeration<Locale> getLocales() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public boolean isSecure() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getRealPath(String path) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public int getRemotePort() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getLocalName() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getLocalAddr() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public int getLocalPort() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public ServletContext getServletContext() {
		return servletContext;
	}

	@Override
	public AsyncContext startAsync() throws IllegalStateException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public AsyncContext startAsync(ServletRequest servletRequest,
			ServletResponse servletResponse) throws IllegalStateException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public boolean isAsyncStarted() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public boolean isAsyncSupported() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public AsyncContext getAsyncContext() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public DispatcherType getDispatcherType() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getAuthType() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Cookie[] getCookies() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public long getDateHeader(String name) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getHeader(String name) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public int getIntHeader(String name) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getMethod() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getPathInfo() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getPathTranslated() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getContextPath() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getQueryString() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getRemoteUser() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public boolean isUserInRole(String role) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Principal getUserPrincipal() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getRequestedSessionId() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getRequestURI() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public StringBuffer getRequestURL() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getServletPath() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public HttpSession getSession(boolean create) {
		return session;
	}

	@Override
	public HttpSession getSession() {
		return getSession(true);
	}

	@Override
	public String changeSessionId() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public boolean authenticate(HttpServletResponse response)
			throws IOException, ServletException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void login(String username, String password) throws ServletException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void logout() throws ServletException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Collection<Part> getParts() throws IOException, ServletException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Part getPart(String name) throws IOException, ServletException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass)
			throws IOException, ServletException {
		throw new RuntimeException("Not implemented");
	}

}
