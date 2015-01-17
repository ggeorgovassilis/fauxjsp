package fauxjsp.impl.simulatedtaglibs.core;

import java.io.ByteArrayOutputStream;

import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.renderer.JspRenderException;
import fauxjsp.api.renderer.RenderSession;
import fauxjsp.impl.Utils;
import fauxjsp.servlet.ServletResponseWrapper;

/**
 * Implements built-in tag jsp:attribute
 * 
 * @author George Georgovassilis
 */

public class JspBuiltinTaglibAttribute extends TaglibDefinition {

	public JspBuiltinTaglibAttribute() {
		super("attribute");
		declareAttribute("name", String.class.getName(), false, true);
	}

	protected void runAttribute(RenderSession session,
			JspTaglibInvocation invocation) {
		String attributeName = getAttribute("name", invocation);
		ServletResponseWrapper oldResponse = session.response;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		session.response = new ServletResponseWrapper(oldResponse, buffer);
		render(invocation.getChildren(), session);
		session.response = oldResponse;
		String renderedValue = Utils.string(buffer.toByteArray(),
				session.response.getCharacterEncoding());
		session.request.setAttribute(attributeName, renderedValue);
	}

	@Override
	protected void renderNode(RenderSession session, JspTaglibInvocation invocation) {
		if (invocation.getTaglib().equals("attribute")) {
			runAttribute(session, invocation);
		} else
			throw new JspRenderException("Unknown taglib "
					+ invocation.getTaglib(), invocation);
	}
}
