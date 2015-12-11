package fauxjsp.api.nodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public abstract class JspNodeWithChildren extends JspNode{

	protected final List<JspNode> children = new ArrayList<JspNode>();
	protected final Map<String, NodeAttributeValue> attributes = new HashMap<String, NodeAttributeValue>();
	protected final String fullQualifiedName;

	public JspNodeWithChildren(String fullQualifiedName, CodeLocation location){
		super(location);
		this.fullQualifiedName = fullQualifiedName;
	}
	
	@Override
	public String debugLabel() {
		return getName();
	}

	public String getName() {
		return fullQualifiedName;
	}

	public List<JspNode> getChildren() {
		return children;
	}

	public Map<String, NodeAttributeValue> getAttributes() {
		return attributes;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("<").append(getName());
		for (String key:attributes.keySet()){
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
