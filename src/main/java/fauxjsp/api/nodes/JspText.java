package fauxjsp.api.nodes;

import fauxjsp.api.parser.CodeLocation;
import fauxjsp.api.renderer.ELEvaluation;

/**
 * Models binary content which may include EL (see {@link ELEvaluation}).
 * @author George Georgovassilis
 *
 */
public class JspText extends JspNode {
	protected final String content;
	
	@Override
	public String debugLabel() {
		return ":text";
	}

	public JspText(String content, CodeLocation location) {
		super(location);
		this.content = content;
	}

	public String getContentAsString() {
		return content;
	}

	@Override
	public String toString() {
		return getContentAsString();
	}
}
