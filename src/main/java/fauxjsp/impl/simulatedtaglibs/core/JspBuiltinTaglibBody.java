package fauxjsp.impl.simulatedtaglibs.core;

import fauxjsp.api.RenderSession;
import fauxjsp.api.nodes.JspNode;
import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TaglibDefinition;

/**
 * Implements built-in tag jsp:body
 * 
 * 
 * 
 * @author George Georgovassilis
 */

public class JspBuiltinTaglibBody extends TaglibDefinition {

	public JspBuiltinTaglibBody() {
		name = "body";
	}

	// TODO: this doesn't work yet. result needs to be rendered into a buffer
	// instead of session.response
	protected void runAttribute(RenderSession session,
			JspTaglibInvocation invocation) {
		for (JspNode child : invocation.getChildren())
			session.renderer.render(child, session);
	}

	@Override
	public void render(RenderSession session, JspTaglibInvocation invocation) {
		try {
			if (invocation.getTaglib().equals("body")) {
				runAttribute(session, invocation);
			} else {
				throw new RuntimeException("Unknown taglib "
						+ invocation.getTaglib());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
