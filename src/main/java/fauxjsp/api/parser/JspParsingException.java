package fauxjsp.api.parser;


/**
 * An exception that occurs while parsing a code fragment. These are "semantical" exceptions tied to
 * syntax errors such as unbalanced tags and not "coding" errors such as NPEs inside the parsing.
 * @author George Georgovassilis
 */

public class JspParsingException extends RuntimeException {

	private static final long serialVersionUID = -5544297335802487012L;

	public CodeLocation getCodeLocation() {
		return codeLocation;
	}

	protected final CodeLocation codeLocation;

	/**
	 * Used to create an invocation stack of what code fragment called which other, so to help the
	 * developer locate mistakes easier. Calling this constructor means that the error did not occur
	 * at the "passThrough" code location, but the "passThrough" code location called a different code
	 * fragment which caused (or, recursively some other fragment higher in the stack) the error.
	 * @param cause Parsing error that occurred
	 * @param passThrough Location in code that invoked some other code in which the error occurred
	 */
	public JspParsingException(JspParsingException cause,
			CodeLocation passThrough) {
		super(cause);
		this.codeLocation = passThrough;
	}

	/**
	 * Used to record a parsing error that occurred at the provided location
	 * @param message Error message
	 * @param codeLocation location where error happened
	 */
	public JspParsingException(String message, CodeLocation codeLocation) {
		super(codeLocation.toString() + ": " + message);
		this.codeLocation = codeLocation;
	}
}
