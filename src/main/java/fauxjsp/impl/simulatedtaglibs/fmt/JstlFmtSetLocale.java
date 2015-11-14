package fauxjsp.impl.simulatedtaglibs.fmt;

import org.apache.commons.lang.LocaleUtils;

import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.renderer.JspRenderException;
import fauxjsp.api.renderer.RenderSession;

/**
 * fmt:setLocale simulation
 * @author George Georgovasilis
 *
 */
public class JstlFmtSetLocale extends TaglibDefinition{

	public JstlFmtSetLocale() {
		super("setLocale");
		declareAttribute("value", String.class.getName(), true, true);
	}

	protected void run(RenderSession session, JspTaglibInvocation invocation){
		String value = getAttribute("value", invocation);
		session.request.setLocale(LocaleUtils.toLocale(value));
	}
	
	@Override
	protected void renderNode(RenderSession session, JspTaglibInvocation invocation) {
		if (!invocation.getTaglib().equals("setLocale"))
			throw new JspRenderException("This isn't a setLocale taglib", invocation);
		run(session, invocation);
	}

}
