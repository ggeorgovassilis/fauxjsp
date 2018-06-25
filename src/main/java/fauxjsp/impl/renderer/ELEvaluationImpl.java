package fauxjsp.impl.renderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fauxjsp.api.renderer.ELEvaluation;
import fauxjsp.api.renderer.JspPageContextImpl;
import fauxjsp.api.renderer.RenderSession;
import fauxjsp.impl.Utils;
import fauxjsp.servlet.ServletRequestWrapper;

/**
 * Default implementation that evaluates java EL expressions
 * 
 * @author George Georgovassilis
 *
 */
public class ELEvaluationImpl implements ELEvaluation {

	protected ELFactory elFactory;

	public ELEvaluationImpl(ELFactory elFactory) {
		this.elFactory = elFactory;
	}

	protected void populateVariables(ELContext context, ExpressionFactory expressionFactory, RenderSession session) {
		VariableMapper variables = context.getVariableMapper();
		variables.setVariable("request",
				expressionFactory.createValueExpression(session.request, HttpServletRequest.class));
		variables.setVariable("response",
				expressionFactory.createValueExpression(session.response, HttpServletResponse.class));
		HttpSession httpSession = session.request.getSession(false);
		if (httpSession != null)
			variables.setVariable("session", expressionFactory.createValueExpression(httpSession, HttpSession.class));

		variables.setVariable("application",
				expressionFactory.createValueExpression(session.request.getServletContext(), ServletContext.class));

		JspPageContextImpl pageContext = new JspPageContextImpl();
		try {
			pageContext.initialize(session.servlet, session.request, session.response, null, false, 0, false);
			variables.setVariable("pageContext",
					expressionFactory.createValueExpression(pageContext, JspPageContextImpl.class));
		} catch (Exception e) {
			throw Utils.softenException(e);
		}
		// TODO: check that servletConfig is spelled correctly
		variables.setVariable("servletConfig",
				expressionFactory.createValueExpression(session.servlet.getServletConfig(), ServletConfig.class));
		// TODO: set page page attribute
		// TODO: set exception page attribute
		ServletContext sc = session.request.getServletContext();
		populateAttributes("contextScope", sc.getAttributeNames(), expressionFactory,
				variables, (attr) -> sc.getAttribute(attr));
		if (session.request.getSession() != null) {
			populateAttributes("sessionScope", httpSession.getAttributeNames(), expressionFactory,
					variables, (attr) -> httpSession.getAttribute(attr));
		}
		ServletRequestWrapper srw = session.request;
		populateAttributes("requestScope", srw.getAttributeNames(), expressionFactory, variables,
				(attr) -> srw.getAttribute(attr));
		populateAttributes("applicationScope", Collections.emptyEnumeration(), expressionFactory, variables,
				null);
	}

	protected void populateAttributes(String scopeObjectName, Enumeration<String> attributes,
			ExpressionFactory expressionFactory, VariableMapper variables, Function<String, Object> l) {
		Map<String, Object> scopeObject = new HashMap<>();
		while (attributes.hasMoreElements()) {
			String attribute = attributes.nextElement();
			Object value = l.apply(attribute);
			if (value != null) {
				variables.setVariable(attribute, expressionFactory.createValueExpression(value, value.getClass()));
				scopeObject.put(attribute, value);
			}
		}
		variables.setVariable(scopeObjectName, expressionFactory.createValueExpression(scopeObject, scopeObject.getClass()));
	}

	protected Object evaluateSimpleExpression(String expression, RenderSession session) {
		if (!expression.startsWith("${"))
			throw new RuntimeException(expression + " is not a simple EL expression, expected to start with ${");
		if (!expression.endsWith("}"))
			throw new RuntimeException(expression + " is not a simple EL expression, expected to end with }");
		ExpressionFactory expressionFactory = elFactory.newExpressionFactory();
		ELContext context = new FauxELContext(elFactory.newElContext(session.fauxELContext), expressionFactory);
		populateVariables(context, expressionFactory, session);

		expression = Utils.unescapeHtml(expression);
		ValueExpression expr = expressionFactory.createValueExpression(context, expression, Object.class);
		try {
			Object result = expr.getValue(context);
			return result;
		} catch (Exception e) {
			throw new RuntimeException("Error when evaluating expression '" + expression + "'", e);
		}

	}

	protected String getEL(String text) {
		int bracketCount = 1;
		int i = 2;
		for (; i < text.length() && bracketCount > 0; i++) {
			char c = text.charAt(i);
			if (c == '{')
				bracketCount++;
			else if (c == '}')
				bracketCount--;
		}
		return text.substring(0, i);
	}

	@Override
	public Object evaluate(String expression, RenderSession session) {
		if (!expression.contains("${"))
			return expression;
		List<Object> list = new ArrayList<>();
		String remainingExpression = expression;
		while (!remainingExpression.isEmpty()) {
			int nextELStart = remainingExpression.indexOf("${");
			if (nextELStart == 0) {
				String el = getEL(remainingExpression);
				Object o = evaluateSimpleExpression(el, session);
				if (o != null)
					list.add(o);
				remainingExpression = remainingExpression.substring(el.length());
				continue;
			} else if (nextELStart == -1) {
				nextELStart = remainingExpression.length();
			}
			String s = remainingExpression.substring(0, nextELStart);
			list.add(s);
			remainingExpression = remainingExpression.substring(s.length());
		}
		if (list.isEmpty())
			return null;
		if (list.size() == 1)
			return list.get(0);
		StringBuffer sb = new StringBuffer();
		for (Object o : list)
			if (o != null)
				sb.append(o);
		return sb.toString();
	}
}
