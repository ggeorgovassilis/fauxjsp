package fauxjsp.api.renderer;

import fauxjsp.api.RenderSession;
import fauxjsp.api.nodes.JspNode;

/**
 * Renders a node into {@link RenderSession#out}
 * @author George Georgovassilis
 */

public interface JspRenderer {

	void render(JspNode page, RenderSession session);

	String explain(JspRenderException exception);
}
