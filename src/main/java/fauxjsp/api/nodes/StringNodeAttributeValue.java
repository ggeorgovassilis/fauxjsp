package fauxjsp.api.nodes;

/**
 * Node attribute that is a string
 * @author george georgovassilis
 *
 */
public class StringNodeAttributeValue extends NodeAttributeValue{

	public StringNodeAttributeValue(String value){
		this.value = value;
	}
	
	protected String value;

	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
