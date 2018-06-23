package fauxjsp.impl.simulatedtaglibs.core;

import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.renderer.RenderSession;

/**
 * Implementation of c:set
 * 
 * @author George Georgovassilis
 *
 */

public class JstlCoreTaglibSet extends TaglibDefinition {

	@Override
	protected void renderNode(RenderSession session, JspTaglibInvocation invocation) {
		String varName = getAttribute("var", invocation);
		String valueExpression = getAttribute("value", invocation);
		Object result = session.elEvaluation.evaluate(valueExpression, session);
		session.request.overwriteAttribute(varName, result);
	}

	public JstlCoreTaglibSet() {
		super("set");
		declareAttribute("var", String.class.getName(), false, true);
		declareAttribute("value", String.class.getName(), true, true);
	}

	@Override
	protected boolean isInstruction() {
		return true;
	}
}
