package fauxjsp.api.nodes;

import fauxjsp.api.parser.CodeLocation;

/**
 * Base class for JSP nodes such as text and taglibs. The sole property for the base node
 * is the location in code it appears in.
 * @author George Georgovassilis
 *
 */
public abstract class JspNode {

	protected CodeLocation location;
	
	protected JspNode(CodeLocation location){
		this.location = location;
	}

	public CodeLocation getLocation() {
		return location;
	}
}
