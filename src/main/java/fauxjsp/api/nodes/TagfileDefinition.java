package fauxjsp.api.nodes;

import fauxjsp.api.renderer.RenderSession;
import fauxjsp.impl.Utils;
import fauxjsp.servlet.ServletRequestWrapper;

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
		for (String argument : invocation.getArguments().keySet()) {

			Object newValue = null;
			Object finalValue = null;
			String valueExpression = invocation.getArguments().get(argument);

			AttributeDefinition def = attributes.get(argument);
			if (def == null)
				throw new RuntimeException("Found unexpected attribute "
						+ argument + " on " + invocation.getName());

			if (def.isRtExpression())
				newValue = session.elEvaluation.evaluate(valueExpression,
						session);
			else
				newValue = valueExpression;
			Class<?> attributeType = getClass(def.getType());
			finalValue = Utils.cast(newValue, attributeType);
			if (finalValue == null) {
				// Value can't be cast to expected class. Ma
				throw new ClassCastException("Expected type " + def.getType()
						+ " for attribute " + argument + " on "
						+ invocation.getName() + " but got "
						+ newValue.getClass());
			}
			session.request.setAttribute(argument, finalValue);
		}

		session.request.setAttribute(BODY_ATTRIBUTE, invocation);
		session.renderer.render(body, session);
	}
}
