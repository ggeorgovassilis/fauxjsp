package fauxjsp.api.renderer;

import fauxjsp.api.nodes.JspNode;

/**
 * Renders a node into {@link RenderSession#response}
 * @author George Georgovassilis
 */

public interface JspRenderer {

	/**
	 * Render a node into {@link RenderSession#response} by iterating over the node's children, if any, and invoking {@link JspNode#renderSelf(RenderSession, JspRenderer)}
	 * @param node Node to render. Will ask the node to render. Nodes with children are responsible for rendering them, e.g. by invoking this
	 * method with each child node as an argument.
	 * @param session Session to use for rendering
	 * @return nothing
	 */
	Void render(JspNode node, RenderSession session);

	/**
	 * Return a human-readable description of a rendering error
	 * @param exception Error to explain
	 * @return Error explanation
	 */
	String explain(JspRenderException exception);
	
	/**
	 * Return implementation that can execute scriptlets (the &lt;%= ... %&gt; things in JSP). 
	 * @return Renderer instance
	 */
	JspScriptletRenderer getScriptletRenderer();
	
}
