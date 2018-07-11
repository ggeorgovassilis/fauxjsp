package fauxjsp.impl.renderer;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Function;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.PropertyNotFoundException;
import javax.el.ValueExpression;
import javax.servlet.ServletContext;
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
		ELResolver elResolver = context.getELResolver();
		elResolver.setValue(context, null, "request", session.request);
		elResolver.setValue(context, null, "response", session.response);
		HttpSession httpSession = session.request.getSession(false);
		elResolver.setValue(context, null, "session", httpSession);
		elResolver.setValue(context, null, "application", session.request.getServletContext());
		//TODO: reuse pageContext?
		JspPageContextImpl pageContext = new JspPageContextImpl();
		try {
			pageContext.initialize(session.servlet, session.request, session.response, null, false, 0, false);
			elResolver.setValue(context, null, "pageContext", pageContext);
		} catch (Exception e) {
			throw Utils.softenException(e);
		}
		elResolver.setValue(context, null, "config", session.servlet.getServletConfig());
		// TODO: set page page attribute
		// TODO: set exception page attribute
		ServletContext sc = session.request.getServletContext();
		populateAttributes(context, sc.getAttributeNames(), (attr) -> sc.getAttribute(attr));
		if (httpSession != null) {
			populateAttributes(context, httpSession.getAttributeNames(), (attr) -> httpSession.getAttribute(attr));
		}
		ServletRequestWrapper srw = session.request;
		populateAttributes(context, srw.getAttributeNames(), (attr) -> srw.getAttribute(attr));
	}

	protected void populateAttributes(ELContext elContext, Enumeration<String> attributes,
			Function<String, Object> valueResolver) {
		ELResolver elResolver = elContext.getELResolver();
		while (attributes.hasMoreElements()) {
			String attribute = attributes.nextElement();
			Object value = valueResolver.apply(attribute);
			elResolver.setValue(elContext, null, attribute, value);
		}
	}
	
	protected Object evaluateSimpleExpression(String expression, RenderSession session) {
		if (!expression.startsWith("${"))
			throw new RuntimeException(expression + " is not a simple EL expression, expected to start with ${");
		if (!expression.endsWith("}"))
			throw new RuntimeException(expression + " is not a simple EL expression, expected to end with }");
		ExpressionFactory expressionFactory = elFactory.newExpressionFactory();
		ELContext context = elFactory.newElContext(session.elContext);
		populateVariables(context, expressionFactory, session);

		expression = Utils.unescapeHtml(expression);
		ValueExpression expr = expressionFactory.createValueExpression(context, expression, Object.class);
		try {
			Object result = expr.getValue(context);
			return result;
		} 
		catch (PropertyNotFoundException pnfe) {
			return null;
		}
		catch (Exception e) {
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
