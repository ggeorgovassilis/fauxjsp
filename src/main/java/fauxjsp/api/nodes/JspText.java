package fauxjsp.api.nodes;

import java.io.IOException;

import fauxjsp.api.parser.CodeLocation;
import fauxjsp.api.renderer.ELEvaluation;
import fauxjsp.api.renderer.JspRenderer;
import fauxjsp.api.renderer.RenderSession;

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
	
	protected String trim(String content) {
		while (content.startsWith(" ")||content.startsWith("\n")||content.startsWith("\r")||content.startsWith("\t"))
			content = content.substring(1);
		return content;
	}

	@Override
	public void renderSelf(RenderSession session, JspRenderer renderer) throws IOException{
		String content = getContentAsString();
		content = (String) session.elEvaluation.evaluate(content, session);
		if (session.trimDirectiveWhiteSpaces && session.previousElementWasInstructionOrTaglib)
			content = trim(content);
		session.response.write(content);
	}

	@Override
	public boolean isInstruction() {
		return false;
	}
}