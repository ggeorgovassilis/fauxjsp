package jspparser;

import java.util.List;

import org.junit.Test;

import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.impl.taglibs.TaglibDiscovery;
import fauxjsp.impl.taglibs.TaglibDiscovery.DiscoveredTaglibs;
import static org.junit.Assert.*;

public class TestTagDiscovery {
	
	@Test
	public void test() throws Exception{
		TaglibDiscovery discovery = new TaglibDiscovery();
		List<DiscoveredTaglibs> discoveredTaglibs = discovery.scanForTaglibs();
		
		assertEquals(2, discoveredTaglibs.size());
		
		List<TaglibDefinition> taglibs = discoveredTaglibs.get(0).taglibs;
		assertEquals("http://java.sun.com/jsp/jstl/core", discoveredTaglibs.get(0).uri);
		assertEquals(14, taglibs.size());
		TaglibDefinition taglib = taglibs.get(2);
		assertEquals("if",taglib.getName());
		assertEquals(3, taglib.getAttributes().size());
		
		TaglibDefinition.AttributeDefinition attr = taglib.getAttributes().get("test");
		assertEquals("test", attr.getName());
		assertTrue(attr.isRequired());
		assertTrue(attr.isRtExpression());
		assertEquals("boolean", attr.getType());
	}

}
