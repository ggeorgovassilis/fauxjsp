package fauxjsp.impl.renderer;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Enumeration;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fauxjsp.api.RenderSession;
import fauxjsp.api.parser.ELEvaluation;
import fauxjsp.impl.Utils;
import fauxjsp.impl.simulatedtaglibs.core.Functions;

public class ELEvaluationImpl implements ELEvaluation {

	protected ELFactory elFactory;

	public ELEvaluationImpl(ELFactory elFactory) {
		this.elFactory = elFactory;
	}
	
	protected void populateVariables(ELContext context, ExpressionFactory expressionFactory, RenderSession session){
		VariableMapper variables = context.getVariableMapper();
		variables.setVariable("request", expressionFactory
				.createValueExpression(session.request,
						HttpServletRequest.class));
		variables.setVariable("response", expressionFactory
				.createValueExpression(session.response,
						HttpServletResponse.class));
		// TODO: set output stream and writer
		// variables.setVariable(
		// "out",
		// expressionFactory.createValueExpression(
		// session.response.getWriter(), PrintWriter.class));
		if (session.request instanceof HttpServletRequest) {
			variables.setVariable("session", expressionFactory
					.createValueExpression(
							((HttpServletRequest) session.request)
									.getSession(false), HttpSession.class));
		}
		variables.setVariable("application", expressionFactory
				.createValueExpression(session.request.getServletContext(),
						ServletContext.class));
		// TODO: set servlet config page attribute
		// TODO: set pagecontext page attribute
		// TODO: set page page attribute
		// TODO: set exception page attribute

		Enumeration<String> attributes = session.request.getAttributeNames();
		while (attributes.hasMoreElements()) {
			String attribute = attributes.nextElement();
			Object value = session.request.getAttribute(attribute);
			if (value != null)
				variables.setVariable(
						attribute,
						expressionFactory.createValueExpression(value,
								value.getClass()));

		}
	}

	protected void declareFunctions(ELContext context, ExpressionFactory expressionFactory, RenderSession session){
		try {
			// iterate over Functions methods and register static functions that start with an _
			for (Method m:Functions.class.getMethods())
				if (m.getName().startsWith("_") && Modifier.isStatic(m.getModifiers())){
					context.getFunctionMapper().mapFunction("fn", m.getName().substring(1), m);
				}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object evaluate(String expression, RenderSession session) {
		ExpressionFactory expressionFactory = elFactory.newExpressionFactory();
		ELContext context = new FauxELContext(elFactory.newElContext(), expressionFactory);
		populateVariables(context, expressionFactory, session);
		declareFunctions(context, expressionFactory, session);

		expression = Utils.unescapeHtml(expression);
		ValueExpression expr = expressionFactory.createValueExpression(context,
				expression, Object.class);
		try {
			Object result = expr.getValue(context);
			return result;
		} catch (Exception e) {
			throw new RuntimeException("Error when evaluating expression '"+expression+"'", e);
		}
	}
}
