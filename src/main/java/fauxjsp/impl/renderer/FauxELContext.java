package fauxjsp.impl.renderer;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.PropertyNotFoundException;
import javax.el.StandardELContext;
import javax.el.ValueExpression;
import javax.el.VariableMapper;

/**
 * {@link ELContext} that returns nulls for variables not found instead of throwing {@link PropertyNotFoundException}s
 * @author George Georgovassilis
 *
 */
public class FauxELContext extends StandardELContext {

	public FauxELContext(ELContext context) {
		super(context);
	}

}
