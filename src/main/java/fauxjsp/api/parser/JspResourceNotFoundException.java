package fauxjsp.api.parser;

public class JspResourceNotFoundException extends JspParsingException{

	public JspResourceNotFoundException(String message, CodeLocation passThrough) {
		super(message, passThrough);
	}

}
