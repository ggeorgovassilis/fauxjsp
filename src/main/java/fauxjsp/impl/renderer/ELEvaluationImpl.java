package fauxjsp.impl.renderer;

import java.util.Enumeration;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;

import fauxjsp.api.RenderSession;
import fauxjsp.api.parser.ELEvaluation;

public class ELEvaluationImpl implements ELEvaluation {

	protected ELFactory elFactory;
	
	public ELEvaluationImpl(ELFactory elFactory) {
		this.elFactory = elFactory;
	}

	@Override
	public Object evaluate(String expression, RenderSession session) {
		ExpressionFactory expressionFactory = elFactory.newExpressionFactory();
		ELContext context = elFactory.newElContext();
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

		expression = StringEscapeUtils.unescapeHtml(expression);
		ValueExpression expr = expressionFactory.createValueExpression(context,
				expression, Object.class);
		Object result = expr.getValue(context);
		return result;
	}
}
