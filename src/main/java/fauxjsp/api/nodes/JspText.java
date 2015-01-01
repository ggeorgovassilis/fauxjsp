package fauxjsp.api.nodes;

import fauxjsp.api.parser.CodeLocation;
import fauxjsp.api.parser.ELEvaluation;

/**
 * Models binary content which may include EL (see {@link ELEvaluation}).
 * @author George Georgovassilis
 *
 */
public class JspText extends JspNode {
	protected final byte[] content;

	public JspText(byte[] content, CodeLocation location) {
		super(location);
		this.content = content;
	}

	public byte[] getContent() {
		return content;
	}

	public String getContentAsString() {
		return new String(content);
	}

	@Override
	public String toString() {
		return getContentAsString();
	}
}
