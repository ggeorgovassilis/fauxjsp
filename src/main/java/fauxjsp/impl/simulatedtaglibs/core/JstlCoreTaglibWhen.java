package fauxjsp.impl.simulatedtaglibs.core;

import fauxjsp.api.RenderSession;
import fauxjsp.api.nodes.JspNode;
import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.renderer.JspRenderException;

/**
 * Implementation of c:when
 * @author George Georgovassilis
 *
 */

public class JstlCoreTaglibWhen extends TaglibDefinition{

	protected void runWhen(RenderSession session, JspTaglibInvocation invocation) {
		String testExpression = getAttribute("test", invocation);
		Object result = evaluate(testExpression, session);
		boolean booleanResult = toBoolean(result);
		if (booleanResult) {
			for (JspNode child : invocation.getChildren())
				session.renderer.render(child, session);
		}
	}
	@Override
	public void render(RenderSession session, JspTaglibInvocation invocation) {
		if (!invocation.getTaglib().equals("when"))
			throw new JspRenderException("This isn't a when taglib", invocation);
		runWhen(session, invocation);
	}
	
	public JstlCoreTaglibWhen() {
		super("when");
		declareAttribute("test", Boolean.class.getName(), true, true);
	}
}
