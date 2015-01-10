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

	protected final String nullProperty = "__fauxjsp_null";
	protected VariableMapper variableMapper;
	
	public FauxELContext(final ELContext context, ExpressionFactory expressionFactory) {
		super(context);
		variableMapper = new VariableMapper() {

			@Override
			public ValueExpression setVariable(String variable,
					ValueExpression expression) {
				return context.getVariableMapper().setVariable(variable, expression);
			}

			@Override
			public ValueExpression resolveVariable(String variable) {
				ValueExpression ve = context.getVariableMapper().resolveVariable(variable);
				if (ve == null) {
					return context.getVariableMapper().resolveVariable(nullProperty);
				}
				return ve;
			}
		};

		getVariableMapper().setVariable(nullProperty,
				expressionFactory.createValueExpression(null, Object.class));
		
	}

	@Override
	public VariableMapper getVariableMapper() {
		return variableMapper;
	}

}
