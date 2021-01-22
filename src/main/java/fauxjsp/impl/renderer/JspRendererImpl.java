package fauxjsp.impl.renderer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.TimeZone;

import fauxjsp.api.logging.Logger;
import fauxjsp.api.nodes.JspNode;
import fauxjsp.api.renderer.JspRenderException;
import fauxjsp.api.renderer.JspRenderer;
import fauxjsp.api.renderer.JspScriptletRenderer;
import fauxjsp.api.renderer.NOPScriptletRendererImpl;
import fauxjsp.api.renderer.RenderSession;
import fauxjsp.impl.Utils;
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
	protected TimeZone defaultTimeZone;
	
	public JspRendererImpl() {
		defaultTimeZone = TimeZone.getDefault();
	}
	
	protected boolean isInstructionOrTaglib(JspNode node) {
		return node.isInstruction();
	}
	
	public void setScriptletRenderer(JspScriptletRenderer scriptletRenderer) {
		this.scriptletRenderer = scriptletRenderer;
	}

	@Override
	public Void render(JspNode page, RenderSession session) {
		if (session.request.getAttribute(RenderSession.ATTR_TIMEZONE) == null)
			session.request.setAttribute(RenderSession.ATTR_TIMEZONE, defaultTimeZone);
		renderNode(page, session);
		return null;
	}

	protected void write(OutputStream out, byte[] content) {
		try {
			out.write(content);
		} catch (IOException e) {
			throw Utils.softenException(e);
		}
	}

	protected void renderNode(JspNode node, RenderSession session) {
		// need this try/catch for exception translation
		try {
			node.renderSelf(session, this);
			session.previousElementWasInstructionOrTaglib = isInstructionOrTaglib(node);
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

	@Override
	public JspScriptletRenderer getScriptletRenderer() {
		return scriptletRenderer;
	}
}
