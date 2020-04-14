package fauxjsp.api.nodes;

/**
 * Node attribute that is a string
 * @author george georgovassilis
 *
 */
public class StringNodeAttributeValue implements NodeAttributeValue{
	protected final String value;

	public StringNodeAttributeValue(String value){
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
