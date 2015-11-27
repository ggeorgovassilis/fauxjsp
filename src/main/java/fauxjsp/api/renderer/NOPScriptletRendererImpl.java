package fauxjsp.api.renderer;

import fauxjsp.api.nodes.JspScriptlet;

/**
 * This renderer fails when invoking a scriptlet 
 * @author George Georgovassilis
 *
 */

public class NOPScriptletRendererImpl implements JspScriptletRenderer{

	@Override
	public void render(JspScriptlet scriptlet, RenderSession session) {
		throw new JspRenderException("Scriptlets are deactivated.", scriptlet);
	}

}
