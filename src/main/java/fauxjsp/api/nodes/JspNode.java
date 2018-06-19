package fauxjsp.api.nodes;

import java.io.IOException;

import fauxjsp.api.parser.CodeLocation;
import fauxjsp.api.renderer.JspRenderer;
import fauxjsp.api.renderer.RenderSession;

/**
 * Base class for JSP nodes such as text and taglibs. The sole property for the base node
 * is the location in code it appears in.
 * @author George Georgovassilis
 *
 */
public abstract class JspNode {

	protected final CodeLocation location;
	
	protected JspNode(CodeLocation location){
		this.location = location;
	}

	public CodeLocation getLocation() {
		return location;
	}
	
	public abstract String debugLabel();
	
	/**
	 * This method renders the node to session.response.outputstream
	 * @param session Session to use for rendering. Input attributes reside in {@link RenderSession#request}, response can be sent to
	 * {@link RenderSession#response}
	 * @param renderer Renderer to use. Nodes with children can/should invoke {@link JspRenderer#render(JspNode, RenderSession)} for each of their children.
	 * @throws IOException .
	 */
	public abstract void renderSelf(RenderSession session, JspRenderer renderer) throws IOException;
}
