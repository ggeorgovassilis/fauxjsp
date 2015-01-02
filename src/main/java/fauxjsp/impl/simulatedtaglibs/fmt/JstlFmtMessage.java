package fauxjsp.impl.simulatedtaglibs.fmt;

import java.util.ResourceBundle;

import fauxjsp.api.RenderSession;
import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.renderer.JspRenderException;

/**
 * 
 * @author George Georgovasilis
 *
 */
public class JstlFmtMessage extends TaglibDefinition{

	public final static String ATTR_RESOURCE_BUNDLE = "__fauxjsp_resource_bundle";
	
	public JstlFmtMessage() {
		name = "message";
		this.attributes.put("key", new AttributeDefinition("key", String.class.getName(), true, true));
	}

	protected void runMsg(RenderSession session, JspTaglibInvocation invocation){
		String key = invocation.getArguments().get("key");
		String resourceBundleName = (String)session.request.getAttribute(ATTR_RESOURCE_BUNDLE);
		if (resourceBundleName == null)
			throw new RuntimeException("No resource bundle name found");
		ResourceBundle bundle = ResourceBundle.getBundle(resourceBundleName, session.request.getLocale());
		if (bundle == null)
			throw new RuntimeException("No resource bundle found");
		String value = bundle.getString(key);
		try {
			session.response.getOutputStream().write(("" + value).getBytes(session.response.getCharacterEncoding()));
		} catch (Exception e) {
			throw new JspRenderException(invocation, e);
		}
	}
	
	@Override
	public void render(RenderSession session, JspTaglibInvocation invocation) {
		if (!invocation.getTaglib().equals("message"))
			throw new JspRenderException(invocation, new RuntimeException("This isn't a message taglib"));
		runMsg(session, invocation);
	}

}
