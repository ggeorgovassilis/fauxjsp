package fauxjsp.impl.renderer;

import javax.el.ELContext;
import javax.el.ExpressionFactory;

/**
 * Factory that provides {@link ELContext} and {@link ExpressionFactory} implementations. Unfortunately 
 * this varies widely over implementations and a special factory for each servlet container is required.
 * @author George Georgovassilis
 *
 */
public interface ELFactory {

	ExpressionFactory newExpressionFactory();
	ELContext newElContext();
}
