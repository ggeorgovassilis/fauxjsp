package fauxjsp.api.parser;

import fauxjsp.api.nodes.JspPage;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.impl.parser.TaglibDefinitionCache;

/**
 * Parses JSPs. Is not multi-thread safe.
 * @author George Georgovassilis
 */
public interface JspParser {

	/**
	 * Get the location resolver this parser is using
	 * @return
	 */
	ResourceResolver getLocationResolver();
	
	/**
	 * Get taglib definitions this parser has encountered
	 * @return
	 */
	TaglibDefinitionCache getTaglibDefinitions();
	
	/**
	 * Register a taglib definition
	 * @param path
	 * @param definition
	 */
	void registerTaglibDefinition(String path, TaglibDefinition definition);

	/**
	 * Register a taglib definition under a specific namespace
	 * @param namespace
	 * @param path
	 * @param definition
	 */
	void registerTaglibDefinition(String namespace, String path, TaglibDefinition definition);
	
	/**
	 * Parse the JSP at the given path. The parser's location resolver (see {@link #getLocationResolver()} will be asked to
	 * return the JSP file for that path
	 * @param path
	 * @return
	 */
	JspPage parse(String path);
	
	/**
	 * Explain an exception in understandable terms
	 * @param exception
	 * @return
	 */
	String explain(JspParsingException exception);

}
