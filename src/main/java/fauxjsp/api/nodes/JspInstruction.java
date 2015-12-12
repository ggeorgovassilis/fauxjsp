package fauxjsp.api.nodes;

import java.io.IOException;

import fauxjsp.api.parser.CodeLocation;
import fauxjsp.api.renderer.JspRenderer;
import fauxjsp.api.renderer.RenderSession;

/**
 * Models a JSP instruction (the &lt;%@ ... %&gt; tags)
 * @author George Georgovassilis
 *
 */

public class JspInstruction extends JspNodeWithAttributes {

	public JspInstruction(String name, CodeLocation location){
		super(name, location);
	}
	
	@Override
	public void renderSelf(RenderSession session, JspRenderer renderer) throws IOException {
	}

}
