package jspparser;

import org.junit.Test;

import fauxjsp.impl.tagparser.TagParser;
import static org.junit.Assert.*;

public class TestTagParser{

	String s(String s){
		return s;
	}
	
	@Test
	public void test1(){
		TagParser parser = new TagParser();
		
		TagParser.Tag tag = parser.parse(s("<c:out value=\"abc\"/>"), null);
		assertEquals("c", tag.taglib.getNamespace());
		assertEquals("out", tag.taglib.getTaglib());

		assertEquals("abc", tag.taglib.getArguments().get("value"));
		assertEquals(TagParser.Tag.TagType.openingAndClosing, tag.type);
	}

	@Test
	public void test2(){
		TagParser parser = new TagParser();
		
		TagParser.Tag tag = parser.parse(s("<c:out value=\"abc\">\n this is something else</c:out>"), null);
		assertEquals("c", tag.taglib.getNamespace());
		assertEquals("out", tag.taglib.getTaglib());

		assertEquals("abc", tag.taglib.getArguments().get("value"));
		assertEquals(TagParser.Tag.TagType.opening, tag.type);
	}

	@Test
	public void test3(){
		TagParser parser = new TagParser();
		
		TagParser.Tag tag = parser.parse(s("<c:out value=\"abc\" encoding=\"UTF-8\">\n this is something else</c:out>"), null);
		assertEquals("c", tag.taglib.getNamespace());
		assertEquals("out", tag.taglib.getTaglib());

		assertEquals("abc", tag.taglib.getArguments().get("value"));
		assertEquals("UTF-8", tag.taglib.getArguments().get("encoding"));
		assertEquals(TagParser.Tag.TagType.opening, tag.type);
	}

}
