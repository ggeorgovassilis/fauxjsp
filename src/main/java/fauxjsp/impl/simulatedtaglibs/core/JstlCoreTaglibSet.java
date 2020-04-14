package fauxjsp.impl.simulatedtaglibs.core;

import java.io.ByteArrayOutputStream;

import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.renderer.RenderSession;
import fauxjsp.impl.Utils;
import fauxjsp.servlet.ServletResponseWrapper;

/**
 * Implementation of c:set
 * 
 * @author George Georgovassilis
 *
 */

public class JstlCoreTaglibSet extends TaglibDefinition {
	
	protected Object getValueFromAttribute(RenderSession session, JspTaglibInvocation invocation) {
		String valueExpression = getAttribute("value", invocation);
		if (!Utils.isEmpty(valueExpression)) {
			return session.elEvaluation.evaluate(valueExpression, session);
		}
		return null;
	}

	protected Object getValueFromChildren(RenderSession session, JspTaglibInvocation invocation) {
		ServletResponseWrapper oldResponse = session.response;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		session.response = new ServletResponseWrapper(oldResponse, buffer);
		render(invocation.getChildren(), session);
		session.response.commit();
		session.response = oldResponse;
		String renderedValue = Utils.string(buffer.toByteArray(), session.response.getCharacterEncoding());
		return renderedValue;
	}

	@Override
	protected void renderNode(RenderSession session, JspTaglibInvocation invocation) {
		String varName = getAttribute("var", invocation);
		Object value = getValueFromAttribute(session, invocation);
		if (value == null) {
			value = getValueFromChildren(session, invocation);
		}
		session.request.overwriteAttribute(varName, value);

	}

	public JstlCoreTaglibSet() {
		super("set");
		declareAttribute("var", String.class.getName(), false, true);
		declareAttribute("value", String.class.getName(), true, false);
	}

	@Override
	protected boolean isInstruction() {
		return true;
	}
}
