package fauxjsp.impl.simulatedtaglibs.core;

import javax.servlet.ServletRequest;

import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.renderer.RenderSession;
import fauxjsp.servlet.ServletRequestWrapper;

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
		ServletRequest request = session.request;
		//override scopes
		if (request instanceof ServletRequestWrapper){
			((ServletRequestWrapper) request).overwriteAttribute(varName, result);
		} else
			request.setAttribute(varName, result);
	}

	public JstlCoreTaglibSet() {
		super("set");
		declareAttribute("var", String.class.getName(), false, true);
		declareAttribute("value", String.class.getName(), true, true);
	}
}
