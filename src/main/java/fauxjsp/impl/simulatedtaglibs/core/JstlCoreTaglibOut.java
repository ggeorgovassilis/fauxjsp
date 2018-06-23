package fauxjsp.impl.simulatedtaglibs.core;

import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.renderer.RenderSession;
import fauxjsp.impl.Utils;

/**
 * Implementation of c:out
 * 
 * @author George Georgovassilis
 *
 */
public class JstlCoreTaglibOut extends TaglibDefinition {

	@Override
	protected void renderNode(RenderSession session,
			JspTaglibInvocation invocation) {
		String valueExpression = getAttribute("value", invocation);
		Object value = evaluate(valueExpression, session);
		String s = Utils.escapeHtml("" + value);
		write(s, session);
	}

	public JstlCoreTaglibOut() {
		super("out");
		declareAttribute("value", Object.class.getName(), true, true);
	}

	@Override
	protected boolean isInstruction() {
		return true;
	}
}
