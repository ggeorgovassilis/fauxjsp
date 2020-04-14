package fauxjsp.impl.simulatedtaglibs.core;

import fauxjsp.api.nodes.JspNode;
import fauxjsp.api.nodes.JspNodeWithChildren;
import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TagfileDefinition;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.renderer.RenderSession;
import fauxjsp.servlet.ServletRequestWrapper;

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
		super("doBody");
	}

	@Override
	protected void renderNode(RenderSession session,
			JspTaglibInvocation invocation) {
		JspNode body = (JspNode) session.request
				.getAttribute(TagfileDefinition.BODY_ATTRIBUTE);
		if (body != null) {
			ServletRequestWrapper oldRequest = session.request;
			session.request = new ServletRequestWrapper(oldRequest);
			if (body instanceof JspNodeWithChildren)
				render(((JspNodeWithChildren) body).getChildren(), session);
			session.request = oldRequest;
		}
	}

	@Override
	protected boolean isInstruction() {
		return true;
	}
}
