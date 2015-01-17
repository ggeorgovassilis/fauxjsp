package fauxjsp.impl.simulatedtaglibs.core;

import fauxjsp.api.nodes.JspNode;
import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.renderer.JspRenderException;
import fauxjsp.api.renderer.RenderSession;

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
					throw new JspRenderException(
							"Unexpectedly found taglib invocation "
									+ taglib.getName() + " within "
									+ invocation.getNamespace(), child);
				}
				if (taglib.getTaglib().equals("when")) {
					String testExpression = taglib.getArguments().get("test");
					if (testExpression == null)
						throw new JspRenderException("Expected test argument",
								taglib);
					Object result = evaluate(testExpression, session);
					boolean booleanResult = toBoolean(result);
					if (booleanResult) {
						session.renderer.render(child, session);
						break;
					}
				} else if (taglib.getTaglib().equals("otherwise")) {
					render(taglib.getChildren(), session);
				}
			}
		}
	}

	@Override
	protected void renderNode(RenderSession session, JspTaglibInvocation invocation) {
		if (!invocation.getTaglib().equals("choose")
				&& !invocation.getTaglib().equals("otherwise"))
			throw new JspRenderException(invocation, new RuntimeException(
					"This isn't a choose or otherwise taglib"));
		runChoose(session, invocation);
	}

	public JstlCoreTaglibChoose() {
		super("choose");
	}
}
