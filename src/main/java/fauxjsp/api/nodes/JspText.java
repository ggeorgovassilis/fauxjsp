package fauxjsp.api.nodes;

import java.io.UnsupportedEncodingException;

import fauxjsp.api.parser.CodeLocation;
import fauxjsp.api.renderer.ELEvaluation;

/**
 * Models binary content which may include EL (see {@link ELEvaluation}).
 * @author George Georgovassilis
 *
 */
public class JspText extends JspNode {
	protected final String content;

	public JspText(String content, CodeLocation location) {
		super(location);
		this.content = content;
	}

	public byte[] getContentAsBytes(String charset) {
		try {
			return content.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public String getContentAsString() {
		return new String(content);
	}

	@Override
	public String toString() {
		return getContentAsString();
	}
}
