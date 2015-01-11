package fauxjsp.impl.simulatedtaglibs.fmt;

import fauxjsp.api.RenderSession;
import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.renderer.JspRenderException;

/**
 * 
 * @author George Georgovasilis
 *
 */
public class JstlFmtSetBundle extends TaglibDefinition{

	public JstlFmtSetBundle() {
		super("setBundle");
		declareAttribute("basename", String.class.getName(), true, true);
	}

	protected void runSetBundle(RenderSession session, JspTaglibInvocation invocation){
		String basename = getAttribute("basename", invocation);
		String bundleName = (String)session.elEvaluation.evaluate(basename, session);
		session.request.setAttribute(JstlFmtMessage.ATTR_RESOURCE_BUNDLE, bundleName);
	}
	
	@Override
	public void render(RenderSession session, JspTaglibInvocation invocation) {
		if (!invocation.getTaglib().equals("setBundle"))
			throw new JspRenderException("This isn't a message taglib", invocation);
		runSetBundle(session, invocation);
	}

}
