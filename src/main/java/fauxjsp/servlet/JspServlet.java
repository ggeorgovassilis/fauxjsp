package fauxjsp.servlet;

import java.io.IOException;

import javax.el.ELContext;
import javax.el.ELManager;
import javax.el.ExpressionFactory;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fauxjsp.api.nodes.JspPage;
import fauxjsp.api.parser.JspParser;
import fauxjsp.api.parser.JspParserFactory;
import fauxjsp.api.parser.JspParsingException;
import fauxjsp.api.parser.ResourceResolver;
import fauxjsp.api.renderer.JspRenderException;
import fauxjsp.api.renderer.JspRenderer;
import fauxjsp.api.renderer.JspRendererFactory;
import fauxjsp.api.renderer.RenderSession;
import fauxjsp.impl.parser.DefaultJspParserFactoryImpl;
import fauxjsp.impl.renderer.ELEvaluationImpl;
import fauxjsp.impl.renderer.ELFactory;
import fauxjsp.impl.renderer.ELFactoryServlet3Impl;
import fauxjsp.impl.renderer.JspRendererFactoryImpl;

/**
 * Servlet that will open the requested file and render it as JSP. This implementation has been tested only with Tomcat 8.
 * Other servlet containers which do not operate a standard {@link ELManager} will likely require custom implementations 
 * of this servlet which lookup and provide {@link ELContext} and {@link ExpressionFactory}
 * implementations.
 * There are some methods which can be overridden in custom implementations:
 * 
 * {@link #getJspBase(ServletConfig)} which determines the base location (as in servlet context location) of JSP files (etc) to look for
 * {@link #getJspParserFactory(ServletConfig)} which creates an {@link JspParserFactory} instance
 * {@link #getElFactory(ServletConfig)} which creates an {@link ELFactory} instance
 * {@link #getJspRendererFactory(ServletConfig)} which creates an {@link JspRendererFactory} instance
 * 
 * The following init parameters are recognized:
 * 
 * <code>errorOnScriptlet</code> when set to "true" will throw an exception when scriptlets are used, otherwise just log a warning.
 * @author George Georgovassilis
 *
 */

public class JspServlet extends HttpServlet {

	private static final long serialVersionUID = 2902779187837649396L;

	protected String jspBase;
	protected JspParserFactory jspParserFactory;
	protected ELFactory elFactory;
	protected JspRendererFactory jspRendererFactory;
	
	/**
	 * Finds the jsp base location from the "base-path" init parameter.
	 * This field tells the servlet at which server location to look for JSP files.
	 * @param config
	 * @return server location to look for JSP files
	 */
	protected String getJspBase(ServletConfig config){
		String jspBase = config.getInitParameter("base-path");
		if (jspBase == null)
			jspBase = "/";
		return jspBase;
	}

	/**
	 * Initializes a thread-safe {@link JspParserFactory}
	 * @param config
	 * @return 
	 */
	protected JspParserFactory getJspParserFactory(ServletConfig config){
		ResourceResolver location = new ServletResourceResolver(jspBase, getServletContext());
		DefaultJspParserFactoryImpl factory = new DefaultJspParserFactoryImpl(location);
		factory.setFailOnScriptletUsage("true".equalsIgnoreCase(config.getInitParameter("errorOnScriptlet")));
		return factory;
	}
	
	/**
	 * Creates an {@link ELFactory} instance
	 * @param config
	 * @return
	 */
	protected ELFactory getElFactory(ServletConfig config){
		ELFactoryServlet3Impl factory = new ELFactoryServlet3Impl();
		factory.configure(config);
		return factory;
	}
	
	/**
	 * Creates an {@link JspRendererFactory} instance
	 * @param config
	 * @return
	 */
	protected JspRendererFactory getJspRendererFactory(ServletConfig config){
		return new JspRendererFactoryImpl();
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		config.getServletContext().log("Configuring JspServlet");
		jspBase = getJspBase(config);
		jspParserFactory = getJspParserFactory(config);
		elFactory = getElFactory(config);
		jspRendererFactory = getJspRendererFactory(config);
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String servletPath = req.getServletPath();
		JspParser parser = jspParserFactory.create();
		JspRenderer renderer = jspRendererFactory.create();
		try {
			if (resp.getContentType()==null)
				resp.setContentType("text/html;charset=UTF-8");
			if (resp.getCharacterEncoding()==null)
				resp.setCharacterEncoding("UTF-8");
			RenderSession session = new RenderSession();
			JspPage page = parser.parse(servletPath);
			session.renderer = renderer;
			session.elEvaluation = new ELEvaluationImpl(elFactory);
			session.request = new ServletRequestWrapper(req);
			session.response = new ServletResponseWrapper(resp, resp.getOutputStream());
			session.servlet = this;
			renderer.render(page, session);
		} catch (JspParsingException pe) {
			String explanation = parser.explain(pe);
			throw new ServletException("Error while parsing "+servletPath+"\n"+explanation, pe);
		} catch (JspRenderException re){
			String explanation = renderer.explain(re);
			throw new ServletException("Error while rendering "+servletPath+"\n"+explanation, re);
		} catch (Exception e){
			throw new ServletException(e);
		}
	}
}
