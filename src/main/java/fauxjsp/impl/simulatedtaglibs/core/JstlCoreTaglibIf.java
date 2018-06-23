package fauxjsp.impl.simulatedtaglibs.core;

import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.renderer.RenderSession;

/**
 * Implementation of c:choose
 * 
 * @author George Georgovassilis
 *
 */

public class JstlCoreTaglibIf extends TaglibDefinition {

	@Override
	protected void renderNode(RenderSession session, JspTaglibInvocation invocation) {
		String testExpression = getAttribute("test", invocation);
		Object result = evaluate(testExpression, session);
		boolean booleanResult = toBoolean(result);
		if (booleanResult)
			render(invocation.getChildren(), session);
	}

	public JstlCoreTaglibIf() {
		super("if");
		declareAttribute("test", Boolean.class.getName(), true, true);
	}

	@Override
	protected boolean isInstruction() {
		return true;
	}
}
