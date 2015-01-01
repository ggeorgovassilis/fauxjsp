package fauxjsp.impl.simulatedtaglibs;

import fauxjsp.api.RenderSession;
import fauxjsp.api.nodes.JspNode;
import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TagfileDefinition;
import fauxjsp.api.nodes.TaglibDefinition;

/**
 * Implements built-in tags such as jsp:doBody. Currently implemented:
 * 
 * jsp:doBody
 * 
 * 
 * @author George Georgovassilis
 */

public class JspBuiltinTaglibDoBody extends TaglibDefinition {

	public JspBuiltinTaglibDoBody() {
		name = "doBody";
	}

	protected void runDoBody(RenderSession session,
			JspTaglibInvocation invocation) {
		JspTaglibInvocation body = (JspTaglibInvocation) session.request
				.getAttribute(TagfileDefinition.BODY_ATTRIBUTE);
		if (body != null)
			for (JspNode child : body.getChildren())
				session.renderer.render(child, session);
	}

	@Override
	public void render(RenderSession session, JspTaglibInvocation invocation) {
		try {
			if (invocation.getTaglib().equals("doBody")) {
				runDoBody(session, invocation);
			} else {
				throw new RuntimeException("Unknown taglib "
						+ invocation.getTaglib());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
