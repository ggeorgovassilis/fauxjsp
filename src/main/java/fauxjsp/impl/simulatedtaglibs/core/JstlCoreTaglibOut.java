package fauxjsp.impl.simulatedtaglibs.core;

import org.apache.commons.lang.StringUtils;

import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.renderer.RenderSession;

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
		String s = StringUtils.replaceEach(""+value,
		        new String[]{"&", "<", ">", "\"", "'"},
		        new String[]{"&amp;", "&lt;", "&gt;", "&quot;", "&#x27;"});
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
