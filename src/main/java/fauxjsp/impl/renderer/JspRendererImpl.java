package fauxjsp.impl.renderer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.TimeZone;

import fauxjsp.api.logging.Logger;
import fauxjsp.api.nodes.JspNode;
import fauxjsp.api.nodes.JspNodeWithChildren;
import fauxjsp.api.nodes.JspScriptlet;
import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.JspText;
import fauxjsp.api.renderer.JspRenderException;
import fauxjsp.api.renderer.JspRenderer;
import fauxjsp.api.renderer.JspScriptletRenderer;
import fauxjsp.api.renderer.NOPScriptletRendererImpl;
import fauxjsp.api.renderer.RenderSession;
import fauxjsp.impl.logging.Logging;

/**
 * JSP renderer implementation
 * 
 * @author George Georgovassilis
 *
 */

public class JspRendererImpl implements JspRenderer {

	protected Logger logger = Logging.getLogger(JspRendererImpl.class);
	protected JspScriptletRenderer scriptletRenderer = new NOPScriptletRendererImpl();

	public void setScriptletRenderer(JspScriptletRenderer scriptletRenderer) {
		this.scriptletRenderer = scriptletRenderer;
	}

	@Override
	public void render(JspNode page, RenderSession session) {
		if (session.request.getAttribute(RenderSession.ATTR_TIMEZONE) == null)
			session.request.setAttribute(RenderSession.ATTR_TIMEZONE, TimeZone.getDefault());
		renderNode(page, session);
	}

	protected void write(OutputStream out, byte[] content) {
		try {
			out.write(content);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected void renderTextNode(JspText node, RenderSession session) throws IOException {
		String content = node.getContentAsString();
		content = (String) session.elEvaluation.evaluate(content, session);
		write(session.response.getOutputStream(), content.getBytes(session.response.getCharacterEncoding()));
	}

	protected void renderTaglib(JspTaglibInvocation taglibInvocation, RenderSession session) {
		taglibInvocation.getDefinition().render(session, taglibInvocation);
	}

	protected void renderScriptlet(JspScriptlet scriptlet, RenderSession session) {
		scriptletRenderer.render(scriptlet, session);
	}

	protected void renderNodeWithChildren(JspNodeWithChildren nodeWithChildren, RenderSession session) {
		for (JspNode childNode : nodeWithChildren.getChildren())
			renderNode(childNode, session);
	}

	protected void renderNode(JspNode node, RenderSession session) {
		// need this try/catch for exception translation
		try {
			// order is important here: eg. a taglib is also a node with
			// children, but requires a different handling.
			if (node instanceof JspText) {
				renderTextNode((JspText) node, session);
			} else if (node instanceof JspTaglibInvocation) {
				renderTaglib((JspTaglibInvocation) node, session);
			} else if (node instanceof JspScriptlet) {
				renderScriptlet((JspScriptlet) node, session);
			} else if (node instanceof JspNodeWithChildren) {
				renderNodeWithChildren((JspNodeWithChildren) node, session);
			} else
				logger.warn("Encountered an unknown node " + node.getClass() + " at " + node.getLocation());
		} catch (Exception e) {
			throw new JspRenderException(node, e);
		}
	}

	@Override
	public String explain(JspRenderException exception) {
		if (exception == null)
			return "";
		Throwable cause = exception.getCause();
		String causeExplanation = exception.getMessage();
		if (cause != null && cause != exception) {
			if (cause instanceof JspRenderException)
				causeExplanation = explain((JspRenderException) cause);
			else
				causeExplanation = cause.getMessage();
		}
		return causeExplanation + "\n" + exception.getNode().getLocation();
	}
}
