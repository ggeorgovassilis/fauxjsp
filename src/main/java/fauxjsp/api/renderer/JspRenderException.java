package fauxjsp.api.renderer;

import fauxjsp.api.nodes.JspNode;

/**
 * Describes a rendering error that happened while rendering a node
 * @author George Georgovassilis
 */

public class JspRenderException extends RuntimeException {
	
	private static final long serialVersionUID = -753803022059174131L;

	protected JspNode node;

	/**
	 * 
	 * @param node Node that was being rendered or node that was rendering a child node on which the error occurred
	 * @param cause Exception that occurred
	 */
	public JspRenderException(JspNode node, Exception cause) {
		super(cause);
		this.node = node;
	}
	
	public JspRenderException(String message, JspNode node, Exception cause){
		super(message, cause);
		this.node = node;
	}

	public JspNode getNode() {
		return node;
	}
}
