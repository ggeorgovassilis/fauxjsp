package fauxjsp.api.parser;

/**
 * Points at a place in the code
 * 
 * @author George Georgovassilis
 *
 */
public class CodeLocation {

	protected final String file;
	protected final int line;
	protected final int column;

	/**
	 * 
	 * @param file
	 *            Name of the code fragment. Something the user/developer can
	 *            understand, like the name of the jsp or tag file.
	 * @param line
	 *            Line number in the code.
	 * @param column
	 *            Colum in the line
	 */
	public CodeLocation(String file, int line, int column) {
		this.file = file;
		this.line = line;
		this.column = column;
	}

	@Override
	public String toString() {
		return getFile() + " Line " + line + " Column " + column;
	}

	public String getFile() {
		return file;
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}
}
