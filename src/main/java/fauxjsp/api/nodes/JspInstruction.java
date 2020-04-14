package fauxjsp.api.nodes;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fauxjsp.api.parser.CodeLocation;
import fauxjsp.api.renderer.JspRenderer;
import fauxjsp.api.renderer.RenderSession;
import fauxjsp.impl.Utils;

/**
 * Models a JSP instruction (the &lt;%@ ... %&gt; tags)
 * 
 * @author George Georgovassilis
 *
 */

public class JspInstruction extends JspNodeWithAttributes {
	
	protected Pattern pattern = Pattern.compile("([^;]*)[;]?.*?(charset)?.*?([^=]*)");
	
	protected boolean isInstruction;
	
	public void setInstruction(boolean isInstruction) {
		this.isInstruction = isInstruction;
	}

	public JspInstruction(String name, CodeLocation location) {
		super(name, location);
	}
	
	protected void processContentType(RenderSession session, JspRenderer renderer) {
		StringNodeAttributeValue contentTypeAttr = (StringNodeAttributeValue) getAttributes().get("contentType");
		if (contentTypeAttr == null)
			return;
		String value = contentTypeAttr.getValue();
		if (Utils.isEmpty(value))
			return;
		Matcher m = pattern.matcher(value);
		if (!m.matches())
			return;
		String contentType = m.group(1);
		String charset = m.group(3);
		if (!Utils.isEmpty(contentType))
			session.response.setContentType(contentType);
		if (!Utils.isEmpty(charset))
			session.response.setCharacterEncoding(charset);
	}

	protected void processWhitespaceTrimming(RenderSession session, JspRenderer renderer) {
		StringNodeAttributeValue tdwAttribute = (StringNodeAttributeValue) getAttributes().get("trimDirectiveWhitespaces");
		if (tdwAttribute == null)
			return;
		String value = tdwAttribute.getValue();
		if (Utils.isEmpty(value))
			return;
		session.trimDirectiveWhiteSpaces = Boolean.parseBoolean(value);
	}

	@Override
	public void renderSelf(RenderSession session, JspRenderer renderer) throws IOException {
		processContentType(session, renderer);
		processWhitespaceTrimming(session, renderer);
	}

	@Override
	public boolean isInstruction() {
		return isInstruction;
	}

}
