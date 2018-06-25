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
 * Servlet that will open the requested file and render it as JSP. This
 * implementation has been tested only with Tomcat 8. Other servlet containers
 * which do not operate a standard {@link ELManager} will likely require custom
 * implementations of this servlet which lookup and provide {@link ELContext}
 * and {@link ExpressionFactory} implementations. There are some methods which
 * can be overridden in custom implementations:
 * 
 * {@link #getJspBase(ServletConfig)} which determines the base location (as in
 * servlet context location) of JSP files (etc) to look for
 * {@link #getJspParserFactory(ServletConfig)} which creates an
 * {@link JspParserFactory} instance {@link #getElFactory(ServletConfig)} which
 * creates an {@link ELFactory} instance
 * {@link #getJspRendererFactory(ServletConfig)} which creates an
 * {@link JspRendererFactory} instance
 * 
 * The following init parameters are recognized: "base-path" specifies the root
 * path where resources (JSPs, tag files etc) will be looked for. Any resource
 * locations will be relative to that path. Defaults to webapp root.
 * 
 * @author George Georgovassilis
 *
 */

public class JspServlet extends HttpServlet {

	private static final long serialVersionUID = 2902779187837649396L;

	protected String jspBase;
	protected JspParserFactory jspParserFactory;
	protected ELFactory elFactory;
	protected JspRendererFactory jspRendererFactory;
	protected boolean trimDirectiveWhiteSpaces = false;

	/**
	 * Finds the jsp base location from the "base-path" init parameter. This field
	 * tells the servlet at which server location to look for JSP files.
	 * 
	 * @param config
	 *            Servlet configuration to use
	 * @return server location to look for JSP files
	 */
	protected String getJspBase(ServletConfig config) {
		String jspBase = config.getInitParameter("base-path");
		if (jspBase == null)
			jspBase = "/";
		return jspBase;
	}

	/**
	 * Initializes a thread-safe {@link JspParserFactory}
	 * 
	 * @param config
	 *            Servlet configuration to use
	 * @return a factory for {@link JspParser} instances
	 */
	protected JspParserFactory getJspParserFactory(ServletConfig config) {
		ResourceResolver servletResourceResolver = new ServletResourceResolver(jspBase, getServletContext());
		ClasspathResourceResolver classpathResourceResolver = new ClasspathResourceResolver();
		CompositeResourceResolver crr = new CompositeResourceResolver();
		crr.register(classpathResourceResolver);
		crr.register(servletResourceResolver);
		DefaultJspParserFactoryImpl factory = new DefaultJspParserFactoryImpl(crr);
		return factory;
	}

	/**
	 * Creates an {@link ELFactory} instance
	 * 
	 * @param config
	 *            Configuration to use with {@link ELFactoryServlet3Impl}
	 * @return an expression language factory
	 */
	protected ELFactory getElFactory(ServletConfig config) {
		ELFactoryServlet3Impl factory = new ELFactoryServlet3Impl();
		factory.configure(config);
		return factory;
	}

	/**
	 * Creates an {@link JspRendererFactory} instance
	 * 
	 * @param config
	 *            servlet configuration to use
	 * @return a new {@link JspRendererFactoryImpl} instance
	 */
	protected JspRendererFactory getJspRendererFactory(ServletConfig config) {
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
		trimDirectiveWhiteSpaces = Boolean.parseBoolean(config.getInitParameter("trimDirectiveWhiteSpaces"));
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String servletPath = req.getServletPath();
		JspParser parser = jspParserFactory.create();
		JspRenderer renderer = jspRendererFactory.create();
		try {
			if (resp.getContentType() == null)
				resp.setContentType("text/html;charset=UTF-8");
			if (resp.getCharacterEncoding() == null)
				resp.setCharacterEncoding("UTF-8");
			RenderSession session = new RenderSession();
			JspPage page = parser.parseJsp(servletPath);
			session.renderer = renderer;
			session.elEvaluation = new ELEvaluationImpl(elFactory);
			session.elContext = elFactory.newElContext();
			session.request = new ServletRequestWrapper(req);
			session.response = new ServletResponseWrapper(resp);
			session.servlet = this;
			session.trimDirectiveWhiteSpaces = trimDirectiveWhiteSpaces;
			renderer.render(page, session);
			session.response.flushBuffer();
		} catch (JspParsingException pe) {
			String explanation = parser.explain(pe);
			throw new ServletException("Error while parsing " + servletPath + "\n" + explanation, pe);
		} catch (JspRenderException re) {
			String explanation = renderer.explain(re);
			throw new ServletException("Error while rendering " + servletPath + "\n" + explanation, re);
		} catch (IOException ioe) {
			throw ioe;
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
