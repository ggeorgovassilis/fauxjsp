package fauxjsp.impl.simulatedtaglibs.core;

import fauxjsp.api.nodes.JspNode;
import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.renderer.JspRenderException;
import fauxjsp.api.renderer.RenderSession;
import fauxjsp.impl.Utils;

/**
 * Implementation of c:choose
 * 
 * @author George Georgovassilis
 *
 */

public class JstlCoreTaglibChoose extends TaglibDefinition {

	protected void runChoose(RenderSession session,
			JspTaglibInvocation invocation) {
		for (JspNode child : invocation.getChildren()) {
			if (child instanceof JspTaglibInvocation) {
				JspTaglibInvocation taglib = (JspTaglibInvocation) child;
				if (!taglib.getNamespace().equals(invocation.getNamespace())) {
					error("Unexpectedly found taglib invocation "
							+ taglib.getName() + " within "
							+ invocation.getNamespace(), child);
				}
				if (taglib.getTaglib().equals("when")) {
					String testExpression = Utils.attr("test",taglib.getAttributes());
					if (testExpression == null)
						error("Expected test argument", taglib);
					Object result = evaluate(testExpression, session);
					boolean booleanResult = toBoolean(result);
					if (booleanResult) {
						session.renderer.render(child, session);
						break;
					}
				} else if (taglib.getTaglib().equals("otherwise")) {
					render(taglib.getChildren(), session);
				} else throw new JspRenderException("Invalid child "+taglib.getName()+" of "+invocation.getName(), taglib);
			}
		}
	}
	
	@Override
	protected void checkInvocation(RenderSession session,
			JspTaglibInvocation invocation) {
		if (!invocation.getTaglib().equals("choose")&&!invocation.getTaglib().equals("otherwise"))
			error("Internal error: attempted to render an '"+invocation.getTaglib()+"' taglib with the taglib definition of "+getName(), invocation);
	}

	@Override
	protected void renderNode(RenderSession session,
			JspTaglibInvocation invocation) {
		runChoose(session, invocation);
	}

	public JstlCoreTaglibChoose() {
		super("choose");
	}

	@Override
	protected boolean isInstruction() {
		return true;
	}
}
