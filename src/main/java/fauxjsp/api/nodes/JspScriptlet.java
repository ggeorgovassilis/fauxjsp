package fauxjsp.api.nodes;

import java.io.PrintStream;
import java.io.Writer;
import java.util.Enumeration;

import fauxjsp.api.parser.CodeLocation;
import fauxjsp.api.renderer.JspPageContextImpl;
import fauxjsp.api.renderer.RenderSession;

/**
 * {@link JspScriptlet} models a jsp scriptlet.
 * 
 * @author George Georgovassilis
 *
 */

public class JspScriptlet extends JspNode {

	protected String sourceCode;
	protected boolean returnsStatement;

	public boolean isReturnsStatement() {
		return returnsStatement;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public JspScriptlet(CodeLocation location, String sourceCode, boolean returnsStatement) {
		super(location);
		this.returnsStatement=returnsStatement;
		this.sourceCode=sourceCode;
	}

	@Override
	public String debugLabel() {
		return ":scriptlet";
	}

}
