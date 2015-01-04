package fauxjsp.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.el.ELContext;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;

public class PageContextImpl extends PageContext{
	
	protected Servlet servlet;
	protected ServletRequest request;
	protected ServletResponse response;
	boolean needsSession;
	int bufferSize;
	boolean autoFlush;
	JspWriterImpl jspWriter;

	@Override
	public void initialize(Servlet servlet, ServletRequest request,
			ServletResponse response, String errorPageURL,
			boolean needsSession, int bufferSize, boolean autoFlush)
			throws IOException, IllegalStateException, IllegalArgumentException {
		this.servlet = servlet;
		this.request = request;
		this.response = response;
		this.needsSession = needsSession;
		this.bufferSize = bufferSize;
		this.autoFlush = autoFlush;
		jspWriter = new JspWriterImpl(new PrintWriter(response.getOutputStream()), bufferSize, autoFlush);
	}

	@Override
	public void release() {
	}

	@Override
	public HttpSession getSession() {
		return ((HttpServletRequest)request).getSession();
	}

	@Override
	public Object getPage() {
		return null;
	}

	@Override
	public ServletRequest getRequest() {
		return request;
	}

	@Override
	public ServletResponse getResponse() {
		return response;
	}

	@Override
	public Exception getException() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public ServletConfig getServletConfig() {
		return servlet.getServletConfig();
	}

	@Override
	public ServletContext getServletContext() {
		return getServletConfig().getServletContext();
	}

	@Override
	public void forward(String relativeUrlPath) throws ServletException,
			IOException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void include(String relativeUrlPath) throws ServletException,
			IOException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void include(String relativeUrlPath, boolean flush)
			throws ServletException, IOException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void handlePageException(Exception e) throws ServletException,
			IOException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void handlePageException(Throwable t) throws ServletException,
			IOException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void setAttribute(String name, Object value) {
		request.setAttribute(name, value);
	}

	@Override
	public void setAttribute(String name, Object value, int scope) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Object getAttribute(String name) {
		return request.getAttribute(name);
	}

	@Override
	public Object getAttribute(String name, int scope) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Object findAttribute(String name) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void removeAttribute(String name) {
		request.removeAttribute(name);
	}

	@Override
	public void removeAttribute(String name, int scope) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public int getAttributesScope(String name) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Enumeration<String> getAttributeNamesInScope(int scope) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public JspWriter getOut() {
		return jspWriter;
	}

	@Override
	public ExpressionEvaluator getExpressionEvaluator() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public VariableResolver getVariableResolver() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public ELContext getELContext() {
		throw new RuntimeException("Not implemented");
	}

}
