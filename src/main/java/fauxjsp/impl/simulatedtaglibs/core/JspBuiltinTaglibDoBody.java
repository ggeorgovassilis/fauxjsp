package fauxjsp.impl.simulatedtaglibs.core;

import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TagfileDefinition;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.renderer.JspRenderException;
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

	protected void runDoBody(RenderSession session,
			JspTaglibInvocation invocation) {
		JspTaglibInvocation body = (JspTaglibInvocation) session.request
				.getAttribute(TagfileDefinition.BODY_ATTRIBUTE);
		if (body != null){
			ServletRequestWrapper oldRequest = session.request;
			session.request = new ServletRequestWrapper(oldRequest);
			render(body.getChildren(), session);
			session.request = oldRequest;
		}
	}

	@Override
	public void render(RenderSession session, JspTaglibInvocation invocation) {
		if (invocation.getTaglib().equals("doBody")) {
			runDoBody(session, invocation);
		} else {
			throw new JspRenderException("Unknown taglib "
					+ invocation.getTaglib(), invocation);
		}
	}
}
