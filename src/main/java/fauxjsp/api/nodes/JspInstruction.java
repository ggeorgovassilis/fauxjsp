package fauxjsp.api.nodes;

import fauxjsp.api.parser.CodeLocation;

/**
 * Models a JSP instruction (the &lt;%@ ... %&gt; tags)
 * @author George Georgovassilis
 *
 */

public class JspInstruction extends JspNodeWithChildren {

	public JspInstruction(String name, CodeLocation location){
		super(name, location);
	}

}
