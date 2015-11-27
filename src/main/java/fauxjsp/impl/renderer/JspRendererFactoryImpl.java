package fauxjsp.impl.renderer;


import fauxjsp.api.logging.Logger;
import fauxjsp.api.renderer.BeanshellScriptletRendererImpl;
import fauxjsp.api.renderer.JspRenderer;
import fauxjsp.api.renderer.JspRendererFactory;
import fauxjsp.impl.logging.Logging;

/**
 * Simple implementation that returns {@link JspRendererImpl} instances
 * @author George Georgovassilis
 *
 */

public class JspRendererFactoryImpl implements JspRendererFactory{

	protected Logger logger = Logging.getLogger(JspRendererFactoryImpl.class);
	
	@Override
	public JspRenderer create() {
		JspRendererImpl renderer = new JspRendererImpl();
		try{
			Class.forName("bsh.Interpreter");
			renderer.setScriptletRenderer(new BeanshellScriptletRendererImpl());
		}
		catch (ClassNotFoundException e){
			logger.info("Beanshell not found, disabling scriptlets");
		}
		return renderer;
	}

}
