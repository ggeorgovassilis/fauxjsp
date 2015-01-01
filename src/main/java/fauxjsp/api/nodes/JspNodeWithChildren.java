package fauxjsp.api.nodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fauxjsp.api.parser.CodeLocation;

/**
 * Base class for nodes which child nodes. These type of nodes, such as taglibs,
 * have children nodes and also a fully qualified name (of the form namespace:tagname such as c:out).
 * They have also an optional list of arguments.
 * @author George Georgovassilis
 *
 */
public abstract class JspNodeWithChildren extends JspNode{

	protected List<JspNode> children = new ArrayList<JspNode>();
	protected final Map<String, String> arguments = new HashMap<String, String>();
	protected String fullQualifiedName;

	public JspNodeWithChildren(String fullQualifiedName, CodeLocation location){
		super(location);
		this.fullQualifiedName = fullQualifiedName;
	}

	public String getName() {
		return fullQualifiedName;
	}

	public List<JspNode> getChildren() {
		return children;
	}

	public Map<String, String> getArguments() {
		return arguments;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("<").append(getName());
		for (String key:arguments.keySet()){
			sb.append(" ");
			sb.append(key);
			sb.append("=");
			sb.append(arguments.get(key));
		}
		sb.append(">");
		for (JspNode n:children){
			sb.append(n.toString());
		}
		sb.append("</").append(getName()).append(">");
		return sb.toString();
	}

}
