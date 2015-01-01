package fauxjsp.impl.renderer;

import javax.el.ELContext;
import javax.el.ELManager;
import javax.el.ExpressionFactory;
import javax.el.StandardELContext;

/**
 * Looks up expression factories and ElContexts from an EL 3.0 {@link ELManager}
 * @author George Georgovassilis
 *
 */
public class ELFactoryServlet3Impl implements ELFactory{

	@Override
	public ExpressionFactory newExpressionFactory() {
		return ELManager.getExpressionFactory();
	}

	@Override
	public ELContext newElContext() {
		return new StandardELContext(newExpressionFactory());
	}

	
	

}
