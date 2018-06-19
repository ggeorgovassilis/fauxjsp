package fauxjsp.api.nodes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fauxjsp.api.renderer.JspRenderException;
import fauxjsp.api.renderer.RenderSession;
import fauxjsp.impl.Utils;
import fauxjsp.servlet.ServletRequestWrapper;

/**
 * Definition of a tag library or tagfile
 * 
 * @author George Georgovassilis
 */
public abstract class TaglibDefinition {

	/**
	 * Definition of an attribute to a tag library
	 *
	 */
	public static class AttributeDefinition {
		protected final String name;
		protected final String type;
		protected final boolean rtExpression;
		protected final boolean required;

		public AttributeDefinition(String name, String type, boolean rtExpression, boolean required) {
			this.name = name;
			this.type = type;
			this.rtExpression = rtExpression;
			this.required = required;
		}

		public boolean isRequired() {
			return required;
		}

		public String getName() {
			return name;
		}

		public String getType() {
			return type;
		}

		public boolean isRtExpression() {
			return rtExpression;
		}
	}

	protected final String name;
	protected final Map<String, AttributeDefinition> attributes = new HashMap<String, TaglibDefinition.AttributeDefinition>();

	protected TaglibDefinition(String name) {
		this.name = name;
	}

	protected void error(String message, JspNode invocation) throws JspRenderException {
		throw new JspRenderException(message, invocation);
	}

	protected void error(Exception cause, JspNode invocation) throws JspRenderException {
		throw new JspRenderException(invocation, cause);
	}

	protected String getAttribute(String name, JspTaglibInvocation invocation) {
		AttributeDefinition def = attributes.get(name);
		if (def == null)
			throw new JspRenderException("Accessing undeclared attibute '" + name + "'", invocation);
		String value = Utils.attr(name,invocation.getAttributes());
		return value;
	}

	protected Object evaluate(String expression, RenderSession session) {
		Object result = session.elEvaluation.evaluate(expression, session);
		return result;
	}

	protected void write(String content, RenderSession session) {
		try {
			session.response.write(content);
		} catch (Exception e) {
			throw Utils.translate(e);
		}
	}

	protected void declareAttribute(String name, String classType, boolean rtExpression, boolean required) {
		attributes.put(name, new AttributeDefinition(name, classType, rtExpression, required));
	}

	protected void render(List<JspNode> nodes, RenderSession session) {
		for (JspNode node : nodes)
			session.renderer.render(node, session);
	}

	public String getName() {
		return name;
	}

	public Map<String, AttributeDefinition> getAttributes() {
		return attributes;
	}

	protected boolean toBoolean(Object result) {
		return (result instanceof Boolean) ? (((Boolean) result).booleanValue()) : result != null;
	}

	/**
	 * Should render(...) create a new variable scope? Implementations extending
	 * this class can decide on their own by overriding this method
	 * 
	 * @return true
	 */
	protected boolean shouldUseNewVariableScope() {
		return true;
	}

	// TODO: cache this result
	protected void checkInvocation(RenderSession session, JspTaglibInvocation invocation) {
		// check that required attributes are there
		TaglibDefinition definition = invocation.getDefinition();
		for (String attribute : definition.getAttributes().keySet()) {
			boolean isRequired = definition.getAttributes().get(attribute).isRequired();
			boolean isUsedInInvocation = invocation.getAttributes().containsKey(attribute);
			if (isRequired && !isUsedInInvocation)
				error("Attribute " + attribute + " is mandatory for taglib " + invocation.getDefinition().getName()
						+ " but wasn't specified", invocation);
		}
	}

	public void render(RenderSession session, JspTaglibInvocation invocation) {
		// open a new variable scope
		ServletRequestWrapper oldRequest = session.request;
		try {
			if (shouldUseNewVariableScope())
				session.request = new ServletRequestWrapper(session.request);
			checkInvocation(session, invocation);
			renderNode(session, invocation);
		} finally {
			// restore old variable scope
			session.request = oldRequest;
		}
	}

	protected abstract void renderNode(RenderSession session, JspTaglibInvocation invocation);

}
