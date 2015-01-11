package fauxjsp.impl.simulatedtaglibs.core;

import fauxjsp.api.RenderSession;
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
		String testExpression = getAttribute("test", invocation);
		Object result = evaluate(testExpression, session);
		boolean booleanResult = toBoolean(result);
		if (booleanResult)
			render(invocation.getChildren(), session);
	}

	@Override
	public void render(RenderSession session, JspTaglibInvocation invocation) {
		if (!invocation.getTaglib().equals("if"))
			throw new JspRenderException("This isn't an if taglib", invocation);
		runIf(session, invocation);
	}

	public JstlCoreTaglibIf() {
		super("if");
		declareAttribute("test", Boolean.class.getName(), true, true);
	}
}
