package fauxjsp.impl.simulatedtaglibs.core;

import java.io.ByteArrayOutputStream;

import javax.servlet.ServletResponse;

import fauxjsp.api.RenderSession;
import fauxjsp.api.nodes.JspNode;
import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.impl.Utils;
import fauxjsp.impl.renderer.ServletResponseWrapper;

/**
 * Implements built-in tag jsp:attribute
 * 
 * 
 * 
 * @author George Georgovassilis
 */

public class JspBuiltinTaglibAttribute extends TaglibDefinition {

	public JspBuiltinTaglibAttribute() {
		name = "name";
		this.attributes.put("name", new AttributeDefinition("name",
				String.class.getName(), false, true));
	}

	// TODO: this doesn't work yet. result needs to be rendered into a buffer
	// instead of session.response
	protected void runAttribute(RenderSession session,
			JspTaglibInvocation invocation) {
		String attributeName = invocation.getArguments().get("name");
		if (Utils.isEmpty(attributeName))
			throw new RuntimeException("Missing attribute name");
		ServletResponse oldResponse = session.response;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		session.response = new ServletResponseWrapper(oldResponse, buffer);
		for (JspNode node:invocation.getChildren())
			session.renderer.render(node, session);
		session.response = oldResponse;
		String renderedValue = Utils.string(buffer.toByteArray(), session.response.getCharacterEncoding());
		session.request.setAttribute(attributeName, renderedValue);
	}

	@Override
	public void render(RenderSession session, JspTaglibInvocation invocation) {
		try {
			if (invocation.getTaglib().equals("attribute")) {
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
