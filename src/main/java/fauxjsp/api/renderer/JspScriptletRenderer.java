package fauxjsp.api.renderer;

import fauxjsp.api.nodes.JspScriptlet;

/**
 * Renders a scriptlet
 * 
 * @author George Georgovassilis
 *
 */

public interface JspScriptletRenderer {

	void render(JspScriptlet scriptlet, RenderSession session);
}
