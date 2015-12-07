package fauxjsp.api.nodes;

import java.io.IOException;

import fauxjsp.api.parser.CodeLocation;
import fauxjsp.api.renderer.JspRenderer;
import fauxjsp.api.renderer.RenderSession;

/**
 * Models the invocation of a taglib or tagfile with optional parameters and a body (nested content). 
 * @author George Georgovassilis
 *
 */

public class JspTaglibInvocation extends JspNodeWithChildren {

	protected TaglibDefinition definition;

	public TaglibDefinition getDefinition() {
		return definition;
	}

	public void setDefinition(TaglibDefinition definition) {
		this.definition = definition;
	}

	public JspTaglibInvocation(String namespace, String taglib, CodeLocation location) {
		super(namespace+":"+taglib, location);
		if (taglib==null)
			throw new IllegalArgumentException("taglib name can't be null");
	}

	/**
	 * Convenience getter which returns the namespace part of the fully qualified name
	 * @return
	 */
	public String getNamespace() {
		return getName().split(":")[0];
	}

	/**
	 * Convenience getter which returns the taglib part of the fully qualified name
	 * @return
	 */
	public String getTaglib() {
		return getName().split(":")[1];
	}

	@Override
	public void renderSelf(RenderSession session, JspRenderer renderer) throws IOException {
		getDefinition().render(session, this);
	}

}
