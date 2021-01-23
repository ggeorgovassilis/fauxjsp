package fauxjsp.api.renderer;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import bsh.Interpreter;
import fauxjsp.api.logging.Logger;
import fauxjsp.api.nodes.JspScriptlet;
import fauxjsp.impl.logging.Logging;
import fauxjsp.impl.stream.OutputStreamWrapper;

/**
 * Renders scriptlets via Beanshell. This implementation assumes that scriptlets don't invoke other scriptlets.
 * 
 * @author George Georgovassilis
 *
 */

public class BeanshellScriptletRendererImpl implements JspScriptletRenderer {

	protected Logger logger = Logging.getLogger(BeanshellScriptletRendererImpl.class);
	protected Map<String, bsh.Interpreter> cachedInterpreters = new HashMap<>();
	protected final String dummyMethodName = "___bsh_method";
	protected OutputStreamWrapper outputStreamWrapper = new OutputStreamWrapper();
	protected PrintStream printStream = new PrintStream(outputStreamWrapper);
	protected int invocationCounter = 0;
	
	protected Interpreter initInterpreter(JspScriptlet scriptlet, RenderSession session, PrintStream out, PrintStream err) throws Exception{
		String sourceCode = scriptlet.getSourceCode();
		Interpreter script = cachedInterpreters.get(sourceCode);
		if (script == null) {
			script = new bsh.Interpreter();
			script.setStrictJava(true);
			if (scriptlet.isReturnsStatement()) {
				script.eval("protected Object " + dummyMethodName + "() throws Throwable {\nreturn " + sourceCode + ";\n}");
			} else {
				script.eval("protected void " + dummyMethodName + "() throws Throwable {\n" + sourceCode + ";\n}");
			}
			cachedInterpreters.put(sourceCode, script);
		}
		script.set("request", session.request);
		script.set("response", session.response);
		script.set("out", session.response.getOutputStream());
		
		JspPageContextImpl pageContext = new JspPageContextImpl();
		pageContext.initialize(null, session.request, session.response, null, false, 0, false);

		script.set("pageContext", pageContext);

		script.setOut(out);
		script.setErr(err);
		return script;
	}

	@Override
	public void render(JspScriptlet scriptlet, RenderSession session) {
		if (invocationCounter>0)
			throw new RuntimeException("render() already being invoked. Is a scriptlet trying to execute another scriptlet?");
		invocationCounter++;
		String sourceCode = scriptlet.getSourceCode();
		try {
			outputStreamWrapper.setWrappedOutputStream(session.response.getOutputStream());
			PrintStream out =  printStream, err = printStream;

			Interpreter script = initInterpreter(scriptlet, session, out, err);
			Object returnValue = script.eval(dummyMethodName + "()");
			if (scriptlet.isReturnsStatement() && returnValue != null) {
				session.response.write(returnValue.toString());
			}
		} catch (Exception e) {
			throw new JspRenderException(e.getMessage() + "\n\nScriptlet code was:\n " + sourceCode, scriptlet);
		} finally {
			outputStreamWrapper.setWrappedOutputStream(null);
			invocationCounter--;
		}

	}

}
