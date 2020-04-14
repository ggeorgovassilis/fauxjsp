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
	 * @return resource resolver
	 */
	ResourceResolver getLocationResolver();
	
	/**
	 * Get taglib definitions this parser has encountered
	 * @return Processes taglib definitions
	 */
	TaglibDefinitionCache getTaglibDefinitions();
	
	/**
	 * Register a taglib definition
	 * @param path Path for the taglib definition
	 * @param definition Parsed taglib definition
	 */
	void registerTaglibDefinition(String path, TaglibDefinition definition);

	/**
	 * Register a taglib definition under a specific namespace
	 * @param namespace Namespace under which the taglib was declared
	 * @param path Path where taglib was loaded from
	 * @param definition Parsed taglib
	 */
	void registerTaglibDefinition(String namespace, String path, TaglibDefinition definition);
	
	/**
	 * Parse the JSP (or fragment) at the given path. The parser's location resolver (see {@link #getLocationResolver()} will be asked to
	 * return the JSP file for that path.
	 * @param path Path of JSP fragment
	 * @return Parsed JSP fragment
	 */
	JspPage parseJspFragment(String path);

	/**
	 * Parse the JSP at the given path.
	 * @param path Path of JSP page
	 * @return Parsed JSP page
	 */
	JspPage parseJsp(String path);

	/**
	 * Explain an exception in understandable terms
	 * @param exception Exception to explain
	 * @return Explanation
	 */
	String explain(JspParsingException exception);

}
