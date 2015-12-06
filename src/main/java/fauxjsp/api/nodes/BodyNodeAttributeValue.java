package fauxjsp.api.nodes;

/**
 * Node attribute that refers a jsp:attribute child
 * @author george georgovassilis
 *
 */
public class BodyNodeAttributeValue extends NodeAttributeValue{
	
	protected JspTaglibInvocation jspAttribute;

	public BodyNodeAttributeValue(JspTaglibInvocation jspAttribute){
		this.jspAttribute = jspAttribute;
	}

	public JspTaglibInvocation getJspAttribute() {
		return jspAttribute;
	}

}