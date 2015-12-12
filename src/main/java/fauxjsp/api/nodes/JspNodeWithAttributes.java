package fauxjsp.api.nodes;

import java.util.HashMap;
import java.util.Map;

import fauxjsp.api.parser.CodeLocation;

/**
 * Base class for nodes with attributes
 * @author George Georgovassilis
 *
 */

public abstract class JspNodeWithAttributes extends JspNode{

	protected final Map<String, NodeAttributeValue> attributes = new HashMap<String, NodeAttributeValue>();
	protected final String name;
	
	protected JspNodeWithAttributes(String name, CodeLocation location) {
		super(location);
		this.name = name;
	}

	public Map<String, NodeAttributeValue> getAttributes() {
		return attributes;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String debugLabel() {
		return getName();
	}


}
