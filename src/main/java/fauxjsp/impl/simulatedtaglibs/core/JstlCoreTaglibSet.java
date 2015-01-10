package fauxjsp.impl.simulatedtaglibs.core;

import fauxjsp.api.RenderSession;
import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.renderer.JspRenderException;
import fauxjsp.impl.Utils;

/**
 * Implementation of c:set
 * 
 * @author George Georgovassilis
 *
 */

public class JstlCoreTaglibSet extends TaglibDefinition {

	protected void runSet(RenderSession session, JspTaglibInvocation invocation) {
		String varName = invocation.getArguments().get("var");
		String valueExpression = invocation.getArguments().get("value");
		if (Utils.isEmpty(varName))
			throw new RuntimeException("Expected var argument");
		if (valueExpression == null)
			throw new RuntimeException("Expected value argument");
		Object result = session.elEvaluation.evaluate(valueExpression, session);
		session.request.setAttribute(varName, result);
	}

	@Override
	public void render(RenderSession session, JspTaglibInvocation invocation) {
		if (!invocation.getTaglib().equals("set"))
			throw new JspRenderException(invocation, new RuntimeException(
					"This isn't a set taglib"));
		runSet(session, invocation);
	}

	public JstlCoreTaglibSet() {
		this.name = "set";
		this.attributes.put("var", new AttributeDefinition("var", String.class.getName(), false, true));
		this.attributes.put("value", new AttributeDefinition("value", String.class.getName(), true, true));
	}
}
