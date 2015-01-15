package fauxjsp.api.renderer;

import fauxjsp.api.nodes.JspPage;
import fauxjsp.servlet.ServletRequestWrapper;
import fauxjsp.servlet.ServletResponseWrapper;

/**
 * A {@link RenderSession} conveniently groups objects a {@link JspRenderer} needs to render a {@link JspPage}
 * @author George Georgovassilis
 *
 */
public class RenderSession {

	public JspRenderer renderer;
	public ServletRequestWrapper request;
	public ServletResponseWrapper response;
	public ELEvaluation elEvaluation;

}
