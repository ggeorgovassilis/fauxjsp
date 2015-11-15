package fauxjsp.test.unittests;

import javax.el.ExpressionFactory;
import javax.el.StandardELContext;
import javax.el.ValueExpression;

import org.junit.Assert;
import org.junit.Test;

import fauxjsp.api.nodes.JspPage;
import fauxjsp.api.parser.CodeLocation;

/**
 * A few test for java EL
 * @author George Georgovassilis
 *
 */

public class TestEL {
	
	@Test
	public void testBasicEL(){

		JspPage page = new JspPage("testpath", new CodeLocation("testpath", 1, 1));
		ExpressionFactory expressionFactory = ExpressionFactory.newInstance();
		StandardELContext elContext = new StandardELContext(expressionFactory);
		elContext.getVariableMapper().setVariable("page", expressionFactory.createValueExpression(page, JspPage.class));
		
		ValueExpression expr = expressionFactory.createValueExpression(elContext, "${page.name}", String.class);
		Object result = expr.getValue(elContext);
		Assert.assertEquals("testpath", result	);
		
	}

	@Test
	public void testELWithString(){

		JspPage page = new JspPage("testpath", new CodeLocation("testpath", 1, 1));
		ExpressionFactory expressionFactory = ExpressionFactory.newInstance();
		StandardELContext elContext = new StandardELContext(expressionFactory);
		elContext.getVariableMapper().setVariable("page", expressionFactory.createValueExpression(page, JspPage.class));
		
		ValueExpression expr = expressionFactory.createValueExpression(elContext, "The page name is ${page.name}", String.class);
		Object result = expr.getValue(elContext);
		Assert.assertEquals("The page name is testpath", result	);
		
	}

}
