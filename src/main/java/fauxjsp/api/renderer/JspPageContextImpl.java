package fauxjsp.api.renderer;

import java.io.IOException;
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

import fauxjsp.impl.Utils;

/**
 * (Incomplete) implementation of a {@link PageContext}
 * @author George Georgovassilis
 *
 */

@SuppressWarnings("deprecation")
public class JspPageContextImpl extends PageContext{
	
	protected ServletRequest request;
	protected ServletResponse response;

	@Override
	public void initialize(Servlet servlet, ServletRequest request, ServletResponse response, String errorPageURL,
			boolean needsSession, int bufferSize, boolean autoFlush)
					throws IOException, IllegalStateException, IllegalArgumentException {
		this.request = request;
		this.response = response;
	}

	@Override
	public void release() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public HttpSession getSession() {
		return ((HttpServletRequest)request).getSession();
	}

	@Override
	public Object getPage() {
		throw new RuntimeException("Not implemented");
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
		try {
			return getServletContext().getServlet("JspServlet").getServletConfig();
		} catch (ServletException e) {
			throw Utils.softenException(e);
		}
	}

	@Override
	public ServletContext getServletContext() {
		return request.getServletContext();
	}

	@Override
	public void forward(String relativeUrlPath) throws ServletException, IOException {
		getServletContext().getRequestDispatcher(relativeUrlPath).forward(request, response);
	}

	@Override
	public void include(String relativeUrlPath) throws ServletException, IOException {
		getServletContext().getRequestDispatcher(relativeUrlPath).forward(request, response);
	}

	@Override
	public void include(String relativeUrlPath, boolean flush) throws ServletException, IOException {
		getServletContext().getRequestDispatcher(relativeUrlPath).include(request, response);
	}

	@Override
	public void handlePageException(Exception e) throws ServletException, IOException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void handlePageException(Throwable t) throws ServletException, IOException {
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
		return getAttribute(name);
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
		throw new RuntimeException("Not implemented");
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
