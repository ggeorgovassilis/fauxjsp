package fauxjsp.impl.simulatedtaglibs.core;

import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TagfileDefinition;
import fauxjsp.api.nodes.TaglibDefinition;
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

	@Override
	protected boolean shouldUseNewVariableScope() {
		return false;
	}

	@Override
	protected void renderNode(RenderSession session,
			JspTaglibInvocation invocation) {
		session.request.setAttribute(TagfileDefinition.BODY_ATTRIBUTE,
				invocation);
	}
}
