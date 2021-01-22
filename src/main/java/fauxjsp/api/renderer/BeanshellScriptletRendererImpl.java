package fauxjsp.api.renderer;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import fauxjsp.api.logging.Logger;
import fauxjsp.api.nodes.JspScriptlet;
import fauxjsp.impl.logging.Logging;

/**
 * Renders scriptlets via Beanshell
 * 
 * @author George Georgovassilis
 *
 */

public class BeanshellScriptletRendererImpl implements JspScriptletRenderer {

	protected Logger logger = Logging.getLogger(BeanshellScriptletRendererImpl.class);
	protected Map<String, bsh.Interpreter> cachedInterpreters = new HashMap<>();
	protected final String dummyMethodName = "___bsh_method";
	
	@Override
	public void render(JspScriptlet scriptlet, RenderSession session) {
		String sourceCode = scriptlet.getSourceCode();
		try {
			PrintStream out = new PrintStream(session.response.getOutputStream());
			PrintStream err = out;
			bsh.Interpreter script = cachedInterpreters.get(sourceCode);
			if (script == null) {
				script = new bsh.Interpreter();
				script.setStrictJava(true);
				if (scriptlet.isReturnsStatement()) {
					script.eval("protected Object "+dummyMethodName+"(){\nreturn "+sourceCode+";\n}");
				} else {
					script.eval("protected void "+dummyMethodName+"(){\n"+sourceCode+";\n}");
				}
				cachedInterpreters.put(sourceCode, script);
			}
			script.setOut(out);
			script.setErr(err);

			// TODO: more implicit objects
			script.set("request", session.request);
			script.set("response", session.response);
			script.set("out", session.response.getOutputStream());
			JspPageContextImpl pageContext = new JspPageContextImpl();
			pageContext.initialize(null, session.request, session.response, null, false, 0, false);
			script.set("pageContext", pageContext);

			Object returnValue = script.eval(dummyMethodName+"()");
			if (scriptlet.isReturnsStatement() && returnValue != null) {
				session.response.write(returnValue.toString());
			}
		} catch (Exception e) {
			throw new JspRenderException(e.getMessage()+"\n\nScriptlet code was:\n "+sourceCode, scriptlet);
		}

	}

}
