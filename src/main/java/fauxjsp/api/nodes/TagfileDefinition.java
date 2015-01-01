package fauxjsp.api.nodes;

import java.util.Map;

import fauxjsp.api.RenderSession;
import fauxjsp.impl.Utils;

/**
 * Definition of a tagfile
 * 
 * @author George Georgovassilis
 */

public class TagfileDefinition extends TaglibDefinition {

	public static String BODY_ATTRIBUTE = "__fauxjsp_body";
	protected JspPage body;

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
	public void render(RenderSession session, JspTaglibInvocation invocation) {
		Map<String, Object> oldAttributes = Utils.saveAttributes(session.request);
		for (String argument : invocation.getArguments().keySet()) {

			Object newValue = null;
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
			if (newValue != null
					&& !attributeType.isAssignableFrom(newValue.getClass()))
				throw new ClassCastException("Expected type " + def.getType()
						+ " for attribute " + argument + " on "
						+ invocation.getName() + " but got "
						+ newValue.getClass());
			session.request.setAttribute(argument, newValue);
		}

		session.request.setAttribute(BODY_ATTRIBUTE, invocation);
		session.renderer.render(body, session);
		Utils.restoreAttributes(session.request, oldAttributes);
	}
}
