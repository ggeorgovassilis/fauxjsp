package fauxjsp.impl.simulatedtaglibs.core;

import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.renderer.JspRenderException;
import fauxjsp.api.renderer.RenderSession;

/**
 * Implements built-in tag jsp:body
 * 
 * @author George Georgovassilis
 */

public class JspBuiltinTaglibBody extends TaglibDefinition {

	public JspBuiltinTaglibBody() {
		super("body");
	}

	protected void runBody(RenderSession session,
			JspTaglibInvocation invocation) {
//		render(invocation.getChildren(), session);
	}

	@Override
	protected void renderNode(RenderSession session, JspTaglibInvocation invocation) {
		if (invocation.getTaglib().equals("body")) {
			runBody(session, invocation);
		} else {
			throw new JspRenderException("Unknown taglib "
					+ invocation.getTaglib(), invocation);
		}
	}
}
