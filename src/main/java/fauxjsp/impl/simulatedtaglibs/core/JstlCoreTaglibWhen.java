package fauxjsp.impl.simulatedtaglibs.core;

import fauxjsp.api.nodes.JspNode;
import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.renderer.RenderSession;

/**
 * Implementation of c:when
 * 
 * @author George Georgovassilis
 *
 */

public class JstlCoreTaglibWhen extends TaglibDefinition {

	@Override
	protected void renderNode(RenderSession session,
			JspTaglibInvocation invocation) {
		String testExpression = getAttribute("test", invocation);
		Object result = evaluate(testExpression, session);
		boolean booleanResult = toBoolean(result);
		if (booleanResult) {
			for (JspNode child : invocation.getChildren())
				session.renderer.render(child, session);
		}
	}

	public JstlCoreTaglibWhen() {
		super("when");
		declareAttribute("test", Boolean.class.getName(), true, true);
	}

	@Override
	protected boolean isInstruction() {
		return true;
	}
}
