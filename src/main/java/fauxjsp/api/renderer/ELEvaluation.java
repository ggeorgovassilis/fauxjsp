package fauxjsp.api.renderer;


/**
 * @author George Georgovassilis
 *
 */
public interface ELEvaluation {

	/**
	 * Evaluates an expression and returns the results
	 * @param expression A standard java EL expression
	 * @param session The render session to use. {@link RenderSession#request} attributes are populated as 
	 * values into the expression context
	 * @return Evaluation result
	 */
	Object evaluate(String expression, RenderSession session);
}
