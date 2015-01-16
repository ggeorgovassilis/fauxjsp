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
//		ServletResponseWrapper oldResponse = session.response;
//		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//		session.response = new ServletResponseWrapper(oldResponse, buffer);
		render(invocation.getChildren(), session);
//		session.response = oldResponse;
//		String renderedValue = Utils.string(buffer.toByteArray(),
//				session.response.getCharacterEncoding());
//		session.request.setAttribute(TagfileDefinition.BODY_ATTRIBUTE, renderedValue);
	}

	@Override
	public void render(RenderSession session, JspTaglibInvocation invocation) {
		if (invocation.getTaglib().equals("body")) {
			runBody(session, invocation);
		} else {
			throw new JspRenderException("Unknown taglib "
					+ invocation.getTaglib(), invocation);
		}
	}
}
