package fauxjsp.impl.renderer;

import fauxjsp.api.renderer.JspRenderer;
import fauxjsp.api.renderer.JspRendererFactory;

/**
 * Simple implementation that returns {@link JspRendererImpl} instances
 * @author George Georgovassilis
 *
 */

public class JspRendererFactoryImpl implements JspRendererFactory{

	@Override
	public JspRenderer create() {
		return new JspRendererImpl();
	}

}
