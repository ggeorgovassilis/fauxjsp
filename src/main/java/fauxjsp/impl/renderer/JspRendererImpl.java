package fauxjsp.impl.renderer;

import java.io.IOException;
import java.io.OutputStream;

import fauxjsp.api.logging.Logger;
import fauxjsp.api.nodes.JspNode;
import fauxjsp.api.nodes.JspNodeWithChildren;
import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.JspText;
import fauxjsp.api.renderer.JspRenderException;
import fauxjsp.api.renderer.JspRenderer;
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

	@Override
	public void render(JspNode page, RenderSession session) {
		renderNode(page, session);
	}

	protected void write(OutputStream out, byte[] content) {
		try {
			out.write(content);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected void renderNode(JspNode node, RenderSession session) {
		logger.trace("Rendering " + node.debugLabel());
		try {
			if (node instanceof JspText) {
				JspText textNode = (JspText) node;
				String content = textNode.getContentAsString();
				content = (String) session.elEvaluation.evaluate(content,
						session);
				write(session.response.getOutputStream(),
						content.getBytes(session.response
								.getCharacterEncoding()));
			} else if (node instanceof JspTaglibInvocation) {
				JspTaglibInvocation taglibInvocation = (JspTaglibInvocation) node;
				taglibInvocation.getDefinition().render(session,
						taglibInvocation);
			} else if (node instanceof JspNodeWithChildren) {
				JspNodeWithChildren nodeWithChildren = (JspNodeWithChildren) node;
				for (JspNode childNode : nodeWithChildren.getChildren())
					renderNode(childNode, session);
			}
		} catch (JspRenderException re) {
			throw re;
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
