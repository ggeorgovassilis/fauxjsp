package fauxjsp.test.unittests;

import org.junit.Test;

import fauxjsp.impl.Utils;
import fauxjsp.impl.tagparser.TagParser;
import static org.junit.Assert.*;

public class TestTagParser{

	String s(String s){
		return s;
	}
	
	@Test
	public void test_tag_open_close_with_attribute(){
		TagParser parser = new TagParser();
		
		TagParser.Tag tag = parser.parse(s("<c:out value=\"abc\"/>"), null);
		assertEquals("c", tag.taglib.getNamespace());
		assertEquals("out", tag.taglib.getTaglib());

		assertEquals("abc", Utils.attr("value",tag.taglib.getAttributes()));
		assertEquals(TagParser.Tag.TagType.openingAndClosing, tag.type);
	}

	@Test
	public void test_tag_with_attribute_and_content(){
		TagParser parser = new TagParser();
		
		TagParser.Tag tag = parser.parse(s("<c:out value=\"abc\">\n this is something else</c:out>"), null);
		assertEquals("c", tag.taglib.getNamespace());
		assertEquals("out", tag.taglib.getTaglib());

		assertEquals("abc", Utils.attr("value",tag.taglib.getAttributes()));
		assertEquals(TagParser.Tag.TagType.opening, tag.type);
	}

	@Test
	public void test_tag_with_multiple_attributes(){
		TagParser parser = new TagParser();
		
		TagParser.Tag tag = parser.parse(s("<c:out value=\"abc\" encoding=\"UTF-8\">\n this is something else</c:out>"), null);
		assertEquals("c", tag.taglib.getNamespace());
		assertEquals("out", tag.taglib.getTaglib());

		assertEquals("abc", Utils.attr("value",tag.taglib.getAttributes()));
		assertEquals("UTF-8", Utils.attr("encoding",tag.taglib.getAttributes()));
		assertEquals(TagParser.Tag.TagType.opening, tag.type);
	}

	@Test
	public void test_tag_with_single_quotes(){
		TagParser parser = new TagParser();
		
		TagParser.Tag tag = parser.parse(s("<c:out value='abc' encoding=\"UTF-8\">\n this is something else</c:out>"), null);
		assertEquals("c", tag.taglib.getNamespace());
		assertEquals("out", tag.taglib.getTaglib());

		assertEquals("abc", Utils.attr("value",tag.taglib.getAttributes()));
		assertEquals("UTF-8", Utils.attr("encoding",tag.taglib.getAttributes()));
		assertEquals(TagParser.Tag.TagType.opening, tag.type);
	}
	
	@Test
	public void test_tag_with_single_quotes_in_string(){
		TagParser parser = new TagParser();
		TagParser.Tag tag = parser.parse(s("<c:if test=\"${fn:containsIgnoreCase('alice had a little lamb','Alice')}\">Condition 5</c:if>"), null);
		assertEquals("c", tag.taglib.getNamespace());
		assertEquals("if", tag.taglib.getTaglib());
		assertEquals("${fn:containsIgnoreCase('alice had a little lamb','Alice')}", Utils.attr("test",tag.taglib.getAttributes()));

	}

}
