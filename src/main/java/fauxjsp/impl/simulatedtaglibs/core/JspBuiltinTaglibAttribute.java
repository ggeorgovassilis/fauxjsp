package fauxjsp.impl.simulatedtaglibs.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TaglibDefinition;
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

	@Override
	protected boolean shouldUseNewVariableScope() {
		return false;
	}

	@Override
	protected void renderNode(RenderSession session, JspTaglibInvocation invocation) {
		String attributeName = getAttribute("name", invocation);
		ServletResponseWrapper oldResponse = session.response;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		session.response = new ServletResponseWrapper(oldResponse, buffer);
		render(invocation.getChildren(), session);
		session.response.commit();
		session.response = oldResponse;
		String renderedValue = Utils.string(buffer.toByteArray(),
				session.response.getCharacterEncoding());
		session.request.setAttribute(attributeName, renderedValue);
	}
}
