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

	public JspInstruction(String name, CodeLocation location) {
		super(name, location);
	}

	@Override
	public void renderSelf(RenderSession session, JspRenderer renderer) throws IOException {
		StringNodeAttributeValue contentTypeAttr = (StringNodeAttributeValue) getAttributes().get("contentType");
		if (contentTypeAttr == null)
			return;
		String declaration = contentTypeAttr.getValue();
		if (Utils.isEmpty(declaration))
			return;
		Matcher m = pattern.matcher(declaration);
		if (!m.matches())
			return;
		String contentType = m.group(1);
		String charset = m.group(3);
		if (!Utils.isEmpty(contentType))
			session.response.setContentType(contentType);
		if (!Utils.isEmpty(charset))
			session.response.setCharacterEncoding(charset);
	}

}
