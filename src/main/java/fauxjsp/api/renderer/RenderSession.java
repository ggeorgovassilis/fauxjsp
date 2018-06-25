package fauxjsp.api.renderer;

import javax.el.ELContext;
import javax.servlet.Servlet;

import fauxjsp.api.nodes.JspPage;
import fauxjsp.servlet.ServletRequestWrapper;
import fauxjsp.servlet.ServletResponseWrapper;

/**
 * A {@link RenderSession} conveniently groups objects a {@link JspRenderer} needs to render a {@link JspPage}
 * @author George Georgovassilis
 *
 */
public class RenderSession {
	
	public RenderSession() {
	}

	public final static String ATTR_TIMEZONE = "__fauxjsp_timezone";
	public JspRenderer renderer;
	public ServletRequestWrapper request;
	public ServletResponseWrapper response;
	public ELEvaluation elEvaluation;
	public ELContext elContext;
	public Servlet servlet;
	public boolean previousElementWasInstructionOrTaglib;
	public boolean trimDirectiveWhiteSpaces;

}
