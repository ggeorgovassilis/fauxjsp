package fauxjsp.api.nodes;

import fauxjsp.api.parser.CodeLocation;

/**
 * {@link JspPage} models an entire JSP page, a jsp fragment or a tagfile.
 * @author George Georgovassilis
 *
 */

public class JspPage extends JspNodeWithChildren{

	public JspPage(String path, CodeLocation location){
		super(path, location);
	}
	
	@Override
	public String toString() {
		return "FRAGMENT "+super.toString();
	}

	@Override
	public boolean isInstruction() {
		return true;
	}
}
