package fauxjsp.impl.simulatedtaglibs.fmt;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.commons.lang.StringUtils;

import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.renderer.RenderSession;
import fauxjsp.servlet.ServletRequestWrapper;

/**
 * fmt:formatNumber simulation
 * 
 * @author George Georgovassilis
 *
 */
public class JstlFmtFormatNumber extends TaglibDefinition {

	public JstlFmtFormatNumber() {
		super("formatNumber");
		declareAttribute("value", String.class.getName(), true, true);
		declareAttribute("type", String.class.getName(), true, false);
		declareAttribute("pattern", String.class.getName(), true, false);
		declareAttribute("currencyCode", String.class.getName(), true, false);
		declareAttribute("groupingUsed", String.class.getName(), true, false);
		declareAttribute("maxIntegerDigits", Integer.class.getName(), true, false);
		declareAttribute("minIntegerDigits", Integer.class.getName(), true, false);
		declareAttribute("maxFractionDigits", Integer.class.getName(), true, false);
		declareAttribute("minFractionDigits", Integer.class.getName(), true, false);
		declareAttribute("var", String.class.getName(), true, false);
		declareAttribute("scope", String.class.getName(), true, false);
	}

	protected Number getNumber(RenderSession session, JspTaglibInvocation invocation) {
		String valueExpression = getAttribute("value", invocation);
		Object value = evaluate(valueExpression, session);
		if (value == null)
			error("'" + valueExpression + "' is null", invocation);
		if (!(value instanceof Number))
			error("'" + valueExpression + "' is a " + value.getClass() + " but I need a number",
					invocation);
		Number number = (Number) value;
		return number;
	}

	protected String getType(RenderSession session, JspTaglibInvocation invocation) {
		String type = getAttribute("type", invocation);
		return type;
	}

	protected void applyMinIntegerDigits(NumberFormat format, RenderSession session, JspTaglibInvocation invocation) {
		String digits = getAttribute("minIntegerDigits", invocation);
		if (!StringUtils.isEmpty(digits)) {
			format.setMinimumIntegerDigits(Integer.parseInt(digits));
		}
	}

	protected void applyMaxIntegerDigits(NumberFormat format, RenderSession session, JspTaglibInvocation invocation) {
		String digits = getAttribute("maxIntegerDigits", invocation);
		if (!StringUtils.isEmpty(digits)) {
			format.setMaximumIntegerDigits(Integer.parseInt(digits));
		}
	}

	protected void applyMinFractionalDigits(NumberFormat format, RenderSession session,
			JspTaglibInvocation invocation) {
		String digits = getAttribute("minFractionDigits", invocation);
		if (!StringUtils.isEmpty(digits)) {
			format.setMinimumFractionDigits(Integer.parseInt(digits));
		}
	}

	protected void applyMaxFractionalDigits(NumberFormat format, RenderSession session,
			JspTaglibInvocation invocation) {
		String digits = getAttribute("maxFractionDigits", invocation);
		if (!StringUtils.isEmpty(digits)) {
			format.setMaximumFractionDigits(Integer.parseInt(digits));
		}
	}

	protected void applyGrouping(NumberFormat format, RenderSession session, JspTaglibInvocation invocation) {
		String grouping = getAttribute("groupingUsed", invocation);
		if (!StringUtils.isEmpty(grouping)) {
			format.setGroupingUsed(Boolean.parseBoolean(grouping));
		}
	}
	
	protected String getPattern(JspTaglibInvocation invocation){
		return getAttribute("pattern", invocation);
	}

	protected void run(RenderSession session, JspTaglibInvocation invocation) {
		Number number = getNumber(session, invocation);
		String type = getType(session, invocation);
		String pattern = getPattern(invocation);
		ServletRequestWrapper request = session.request;
		NumberFormat format = null;
		if ("currency".equals(type)) {
			format = NumberFormat.getCurrencyInstance(request.getLocale());
		} else if ("percent".equals(type)) {
			format = NumberFormat.getPercentInstance(request.getLocale());
		} else
			format = NumberFormat.getInstance(request.getLocale());
		applyMinIntegerDigits(format, session, invocation);
		applyMaxIntegerDigits(format, session, invocation);
		applyMinFractionalDigits(format, session, invocation);
		applyMaxFractionalDigits(format, session, invocation);
		applyGrouping(format, session, invocation);
		if (!StringUtils.isEmpty(pattern)){
			format = new DecimalFormat(pattern);
		}
		
		String formattedNumber = format.format(number);
		write(formattedNumber, session);
	}

	@Override
	protected void renderNode(RenderSession session, JspTaglibInvocation invocation) {
		if (!invocation.getTaglib().equals("formatNumber"))
			error("This isn't a formatNumber taglib", invocation);
		run(session, invocation);
	}

	@Override
	protected boolean isInstruction() {
		return true;
	}

}
