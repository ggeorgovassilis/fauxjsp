package fauxjsp.impl.renderer;

import fauxjsp.api.renderer.JspRenderer;

/**
 * Create {@link JspRenderer} instnaces
 * @author George Georgovassilis
 *
 */
public interface JspRendererFactory {

	JspRenderer newInstance();
}
