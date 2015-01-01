package fauxjsp.api.parser;

import fauxjsp.api.RenderSession;

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
	 * @return
	 */
	Object evaluate(String expression, RenderSession session);
}
