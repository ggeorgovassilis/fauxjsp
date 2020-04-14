package fauxjsp.api.parser;

/**
 * Resolves resources (such as JSP source code)
 * @author George Georgovassilis
 */

public interface ResourceResolver {

	/**
	 * Get contents or return null, if the resource is not found.
	 * @param path Path that designates the resource. Implementation specific, e.g. a URL or path on the file system.
	 * @return Binary representation of the resource's contents.
	 */
	byte[] getContents(String path);
	
	/**
	 * Return true if the resolver can handle, in principle, that kind of path.
	 * E.g. if the resolver were to handle URLs, the path should start with a "http://" prefix.
	 * If it doesn't, then return false. Returning "true" is a prerequisite for {@link #getContents(String)} to work, which
	 * still doesn't mean that {@link #getContents(String)} will work.
	 * @param path Path the the resource.
	 * @return True if this resolver can handle resources for that path.
	 */
	boolean canHandle(String path);
}
