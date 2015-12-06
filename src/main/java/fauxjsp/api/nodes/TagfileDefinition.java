package fauxjsp.api.nodes;

import fauxjsp.api.renderer.JspRenderException;
import fauxjsp.api.renderer.RenderSession;
import fauxjsp.impl.Utils;

/**
 * Definition of a tagfile
 * 
 * @author George Georgovassilis
 */

public class TagfileDefinition extends TaglibDefinition {

	public static String BODY_ATTRIBUTE = "__fauxjsp_body";
	protected JspPage body;

	public TagfileDefinition(String name) {
		super(name);
	}

	public JspPage getBody() {
		return body;
	}

	public void setBody(JspPage body) {
		this.body = body;
	}

	protected Class<?> getClass(String type) {
		try {
			return Class.forName(type);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void renderNode(RenderSession session, JspTaglibInvocation invocation) {
		for (String argument : invocation.getAttributes().keySet()) {

			Object newValue = null;
			Object finalValue = null;
			String valueExpression = null;
			NodeAttributeValue attributeValue = invocation.getAttributes().get(argument);
			AttributeDefinition def = attributes.get(argument);
			if (def == null)
				throw new JspRenderException("Found unexpected attribute '" + argument + "' on " + invocation.getName(),
						invocation);

			if (attributeValue instanceof StringNodeAttributeValue){
				valueExpression = ((StringNodeAttributeValue)attributeValue).getValue();
				if (def.isRtExpression())
					newValue = session.elEvaluation.evaluate(valueExpression, session);
				else
					newValue = valueExpression;
			} else if (attributeValue instanceof BodyNodeAttributeValue){
				BodyNodeAttributeValue bodyNode = ((BodyNodeAttributeValue)attributeValue);
				JspTaglibInvocation attributeInvocation = bodyNode.getJspAttribute();
				attributeInvocation.getDefinition().render(session, attributeInvocation);
				newValue = session.request.getAttribute(argument);
			}


			Class<?> attributeType = getClass(def.getType());
			boolean castingError = false;
			try {
				finalValue = Utils.cast(newValue, attributeType);
			} catch (ClassCastException e) {
				castingError = true;
			}
			// TODO: it looks like this check is never performed; there is a
			// unit test that
			// tries to provoke this error but it's handled earlier in
			// checkInvocation.
			if (castingError || (finalValue == null && newValue != null)) {
				// Value can't be cast to expected class.
				error("Expected type " + def.getType() + " for attribute " + argument + " on " + invocation.getName()
						+ " but supplied argument was " + Utils.getClassOf(newValue), invocation);
			}
			session.request.setAttribute(argument, finalValue);
		}

		// run over attributes
		JspNode bodyNode = invocation;
		for (JspNode node : invocation.getChildren())
			if (node instanceof JspNodeWithChildren) {
				JspNodeWithChildren attribute = (JspNodeWithChildren) node;
				if ("jsp:attribute".equals(attribute.getName())) {
					// JspBuiltinTagAttribute renders attributes
					session.renderer.render(attribute, session);
				} else if ("jsp:body".equals(attribute.getName())) {
					bodyNode = attribute;
				}
			}
		session.request.setAttribute(BODY_ATTRIBUTE, bodyNode);
		session.renderer.render(body, session);
	}
}
