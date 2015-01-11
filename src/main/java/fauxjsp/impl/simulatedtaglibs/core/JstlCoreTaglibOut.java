package fauxjsp.impl.simulatedtaglibs.core;

import fauxjsp.api.RenderSession;
import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.renderer.JspRenderException;
import fauxjsp.impl.Utils;

/**
 * Implementation of c:out
 * 
 * @author George Georgovassilis
 *
 */
public class JstlCoreTaglibOut extends TaglibDefinition {

	protected void runOut(RenderSession session, JspTaglibInvocation invocation) {
		String valueExpression = getAttribute("value", invocation);
		Object value = evaluate(valueExpression, session);
		String s = Utils.escapeHtml("" + value);
		write(s, session);
	}

	@Override
	public void render(RenderSession session, JspTaglibInvocation invocation) {
		if (!invocation.getTaglib().equals("out"))
			throw new JspRenderException(invocation, new RuntimeException(
					"This isn't an out taglib"));
		runOut(session, invocation);
	}

	public JstlCoreTaglibOut() {
		super("out");
		declareAttribute("value", Object.class.getName(), true, true);
	}
}
