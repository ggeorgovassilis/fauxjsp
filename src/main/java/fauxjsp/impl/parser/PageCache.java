package fauxjsp.impl.parser;

import fauxjsp.api.nodes.JspPage;

/**
 * API for caching pages
 * @author george georgovassilis
 *
 */
public interface PageCache {
	JspPage get(String key);
	void put(String key, JspPage page);
	void delete(String key);
	
}
