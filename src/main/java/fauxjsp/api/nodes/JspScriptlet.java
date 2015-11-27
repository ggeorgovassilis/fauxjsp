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

	public void render(RenderSession session) {
		try {
			//TODO: create scriptlet renderer class and move rendering to that renderer
			PrintStream out = new PrintStream(session.response.getOut());
			PrintStream err = out;
			bsh.Interpreter script = new bsh.Interpreter();
			script.setOut(out);
			script.setErr(err);

			// TODO: more implicit objects
			script.set("request", session.request);
			script.set("response", session.response);
			script.set("out", session.response.getOutputStream());
			JspPageContextImpl pageContext = new JspPageContextImpl();
			pageContext.initialize(null, session.request, session.response, null, false, 0, false);
			script.set("pageContext", pageContext);
			
			Enumeration<String> attributes = session.request.getAttributeNames();
			while (attributes.hasMoreElements()){
				String attr = attributes.nextElement();
				Object value = session.request.getAttribute(attr);
				script.set(attr, value);
			}
				

			Object returnValue = script.eval(sourceCode);
			if (isReturnsStatement() && returnValue != null){
				Writer writer = session.response.getWriter();
				writer.write(returnValue.toString());
				writer.flush();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
