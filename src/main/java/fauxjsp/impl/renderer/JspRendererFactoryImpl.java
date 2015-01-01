package fauxjsp.impl.renderer;

import fauxjsp.api.renderer.JspRenderer;

/**
 * Simple implementation that returns {@link JspRendererImpl} instances
 * @author George Georgovassilis
 *
 */

public class JspRendererFactoryImpl implements JspRendererFactory{

	@Override
	public JspRenderer newInstance() {
		return new JspRendererImpl();
	}

}
