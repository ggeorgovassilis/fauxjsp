package fauxjsp.impl.simulatedtaglibs.core;

import fauxjsp.api.RenderSession;
import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.renderer.JspRenderException;

/**
 * Implements built-in tag jsp:body
 * 
 * 
 * 
 * @author George Georgovassilis
 */

public class JspBuiltinTaglibBody extends TaglibDefinition {

	public JspBuiltinTaglibBody() {
		super("body");
	}

	// TODO: this doesn't work yet. result needs to be rendered into a buffer
	// instead of session.response
	protected void runAttribute(RenderSession session,
			JspTaglibInvocation invocation) {
		render(invocation.getChildren(), session);
	}

	@Override
	public void render(RenderSession session, JspTaglibInvocation invocation) {
		if (invocation.getTaglib().equals("body")) {
			runAttribute(session, invocation);
		} else {
			throw new JspRenderException("Unknown taglib "
					+ invocation.getTaglib(), invocation);
		}
	}
}
