package fauxjsp.api.renderer;

import java.io.PrintStream;
import java.io.Writer;
import java.util.Enumeration;

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
	
	@Override
	public void render(JspScriptlet scriptlet, RenderSession session) {
		try {
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


			//TODO: should populate only attributes that were declared in the parent scope as <@ attribute...>
			Enumeration<String> attributes = session.request.getAttributeNames();
			while (attributes.hasMoreElements()) {
				String attr = attributes.nextElement();
				if (attr.contains("."))
					continue;
				Object value = session.request.getAttribute(attr);
				script.set(attr, value);
			}

			Object returnValue = script.eval(scriptlet.getSourceCode());
			if (scriptlet.isReturnsStatement() && returnValue != null) {
				Writer writer = session.response.getWriter();
				writer.write(returnValue.toString());
				writer.flush();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new JspRenderException(e.getMessage()+"\n\nScriptlet code was:\n "+scriptlet.getSourceCode(), scriptlet);
		}

	}

}
