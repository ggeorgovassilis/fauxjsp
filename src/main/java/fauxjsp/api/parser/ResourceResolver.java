package fauxjsp.api.parser;

/**
 * Resolves resources (such as JSP source code)
 * @author George Georgovassilis
 */

public interface ResourceResolver {

	/**
	 * Get contents or return null, if the resource is not found.
	 * @param path
	 * @return
	 */
	byte[] getContents(String path);
	
	/**
	 * Return true if the resolver can handle, in principle, that kind of path.
	 * E.g. if the resolver were to handle URLs, the path should start with a "http://" prefix.
	 * If it doesn't, then return false.
	 * @param path
	 * @return
	 */
	boolean canHandle(String path);
}
