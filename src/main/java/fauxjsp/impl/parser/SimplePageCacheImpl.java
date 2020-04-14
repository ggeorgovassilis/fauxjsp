package fauxjsp.impl.parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import fauxjsp.api.nodes.JspPage;

/**
 * Thread-safe implementation backed by a {@link HashMap}
 * @author george georgovassilis
 *
 */
public class SimplePageCacheImpl implements PageCache{

	Map<String, JspPage> cache = Collections.synchronizedMap(new HashMap<>());
	
	@Override
	public JspPage get(String key) {
		return cache.get(key);
	}

	@Override
	public void put(String key, JspPage page) {
		cache.put(key,  page);
	}

	@Override
	public void delete(String key) {
		cache.remove(key);
	}

}
