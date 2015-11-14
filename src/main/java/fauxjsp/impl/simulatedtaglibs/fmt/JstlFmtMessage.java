package fauxjsp.impl.simulatedtaglibs.fmt;

import java.util.ResourceBundle;

import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.renderer.JspRenderException;
import fauxjsp.api.renderer.RenderSession;

/**
 * 
 * @author George Georgovassilis
 *
 */
public class JstlFmtMessage extends TaglibDefinition{

	public final static String ATTR_RESOURCE_BUNDLE = "__fauxjsp_resource_bundle";
	
	public JstlFmtMessage() {
		super("message");
		declareAttribute("key", String.class.getName(), true, true);
	}

	protected void runMsg(RenderSession session, JspTaglibInvocation invocation){
		String key = getAttribute("key", invocation);
		String resourceBundleName = (String)session.request.getAttribute(ATTR_RESOURCE_BUNDLE);
		if (resourceBundleName == null)
			throw new JspRenderException("No resource bundle name found", invocation);
		ResourceBundle bundle = ResourceBundle.getBundle(resourceBundleName, session.request.getLocale());
		if (bundle == null)
			throw new JspRenderException("No resource bundle found", invocation);
		String value = bundle.getString(key);
		write(value, session);
	}
	
	@Override
	protected void renderNode(RenderSession session, JspTaglibInvocation invocation) {
		if (!invocation.getTaglib().equals("message"))
			throw new JspRenderException(invocation, new RuntimeException("This isn't a message taglib"));
		runMsg(session, invocation);
	}

}
