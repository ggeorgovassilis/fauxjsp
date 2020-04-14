package fauxjsp.api.nodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fauxjsp.api.parser.CodeLocation;
import fauxjsp.api.renderer.JspRenderer;
import fauxjsp.api.renderer.RenderSession;

/**
 * Base class for nodes which child nodes. These type of nodes, such as taglibs,
 * have children nodes and also a fully qualified name (of the form namespace:tagname such as c:out).
 * They have also an optional list of arguments.
 * @author George Georgovassilis
 *
 */
public abstract class JspNodeWithChildren extends JspNodeWithAttributes{

	protected final List<JspNode> children = new ArrayList<JspNode>();

	public JspNodeWithChildren(String fullQualifiedName, CodeLocation location){
		super(fullQualifiedName, location);
	}
	
	public List<JspNode> getChildren() {
		return children;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("<").append(getName());
		List<String> sortedAttributes = new ArrayList<>(attributes.keySet());
		// not important for production, but unit tests expect a fixed order
		Collections.sort(sortedAttributes);
		for (String key:sortedAttributes){
			sb.append(" ");
			sb.append(key);
			sb.append("=");
			sb.append(attributes.get(key));
		}
		sb.append(">");
		for (JspNode n:children){
			sb.append(n.toString());
		}
		sb.append("</").append(getName()).append(">");
		return sb.toString();
	}
	
	@Override
	public void renderSelf(RenderSession session, JspRenderer renderer) throws IOException {
		for (JspNode childNode : getChildren())
			renderer.render(childNode, session);
	}

}
