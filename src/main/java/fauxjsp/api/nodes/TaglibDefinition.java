package fauxjsp.api.nodes;

import java.util.HashMap;
import java.util.Map;

import fauxjsp.api.RenderSession;

/**
 * Definition of a tag library or tagfile
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

		public AttributeDefinition(String name, String type,
				boolean rtExpression, boolean required) {
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

	protected String name;
	protected Map<String, AttributeDefinition> attributes = new HashMap<String, TaglibDefinition.AttributeDefinition>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, AttributeDefinition> getAttributes() {
		return attributes;
	}

	public void render(RenderSession session, JspTaglibInvocation invocation) {
		throw new RuntimeException("Not implemented");
	}
}
