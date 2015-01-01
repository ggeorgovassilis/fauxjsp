package fauxjsp.api.parser;

/**
 * Resolves resources (such as JSP source code)
 * @author George Georgovassilis
 */

public interface ResourceResolver {

	byte[] getContents(String path);
}
