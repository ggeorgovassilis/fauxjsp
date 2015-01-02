package fauxjsp.impl.simulatedtaglibs.core;

import fauxjsp.api.RenderSession;
import fauxjsp.api.nodes.JspNode;
import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.renderer.JspRenderException;

/**
 * Implementation of c:choose
 * 
 * @author George Georgovassilis
 *
 */

public class JstlCoreTaglibIf extends TaglibDefinition {

	protected void runIf(RenderSession session, JspTaglibInvocation invocation) {
		String testExpression = invocation.getArguments().get("test");
		if (testExpression == null)
			throw new RuntimeException("Expected test argument");
		Object result = session.elEvaluation.evaluate(testExpression, session);
		boolean booleanResult = (result instanceof Boolean) ? (((Boolean) result)
				.booleanValue()) : result != null;
		if (booleanResult) {
			for (JspNode child : invocation.getChildren())
				session.renderer.render(child, session);
		}
	}

	@Override
	public void render(RenderSession session, JspTaglibInvocation invocation) {
		if (!invocation.getTaglib().equals("if"))
			throw new JspRenderException(invocation, new RuntimeException(
					"This isn't an if taglib"));
		runIf(session, invocation);
	}

	public JstlCoreTaglibIf() {
		this.name = "if";
		this.attributes.put("test", new AttributeDefinition("test", Boolean.class.getName(), true, true));
	}
}
