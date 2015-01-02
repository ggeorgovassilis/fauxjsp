package fauxjsp.impl.simulatedtaglibs.core;

import fauxjsp.api.RenderSession;
import fauxjsp.api.nodes.JspNode;
import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.renderer.JspRenderException;

/**
 * Implementation of c:choose
 * @author George Georgovassilis
 *
 */

public class JstlCoreTaglibChoose extends TaglibDefinition{

	protected void runChoose(RenderSession session,
			JspTaglibInvocation invocation) {
		for (JspNode child : invocation.getChildren()) {
			if (child instanceof JspTaglibInvocation) {
				JspTaglibInvocation taglib = (JspTaglibInvocation) child;
				if (!taglib.getNamespace().equals(invocation.getNamespace())) {
					throw new RuntimeException(
							"Unexpectedly found taglib invocation "+taglib.getName()+" within "+invocation.getNamespace());
				}
				if (taglib.getTaglib().equals("when")) {
					String testExpression = taglib.getArguments().get("test");
					if (testExpression == null)
						throw new RuntimeException("Expected test argument");
					Object result = session.elEvaluation.evaluate(
							testExpression, session);
					boolean booleanResult = (result instanceof Boolean) ? (((Boolean) result)
							.booleanValue()) : result != null;
					if (booleanResult) {
						session.renderer.render(child, session);
						break;
					}
				} else if (taglib.getTaglib().equals("otherwise")) {
					session.renderer.render(child, session);
				}
			}
		}
	}

	@Override
	public void render(RenderSession session, JspTaglibInvocation invocation) {
		if (!invocation.getTaglib().equals("choose")&&!invocation.getTaglib().equals("otherwise"))
			throw new JspRenderException(invocation, new RuntimeException("This isn't a choose or otherwise taglib"));
		runChoose(session, invocation);
	}
}
