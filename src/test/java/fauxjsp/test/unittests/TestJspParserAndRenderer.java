package fauxjsp.test.unittests;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

import fauxjsp.api.nodes.JspInstruction;
import fauxjsp.api.nodes.JspPage;
import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.JspText;
import fauxjsp.api.renderer.RenderSession;
import fauxjsp.impl.parser.JspParserImpl;
import fauxjsp.servlet.ServletRequestWrapper;
import fauxjsp.servlet.ServletResponseWrapper;
import fauxjsp.test.support.MockHttpServletRequest;
import fauxjsp.test.support.MockHttpServletResponse;
import fauxjsp.test.webapp.dto.NavigationItem;
import fauxjsp.test.webapp.dto.News;
import fauxjsp.test.webapp.dto.Stock;

import static org.junit.Assert.*;

/**
 * Test of {@link JspParserImpl}
 * 
 * @author George Georgovassilis
 *
 */

public class TestJspParserAndRenderer extends BaseTest {

	@Test
	public void testParser() throws Exception {
		JspPage page = parser.parse("WEB-INF/jsp/homepage.jsp");
		assertEquals(5, page.getChildren().size());

		JspInstruction j1 = (JspInstruction) page.getChildren().get(0);
		assertEquals("taglib", j1.getName());
		assertEquals("c", j1.getAttributes().get("prefix"));
		assertEquals("http://java.sun.com/jsp/jstl/core", j1.getAttributes().get("uri"));

		JspText j2 = (JspText) page.getChildren().get(1);
		assertEquals("\r\n", j2.getContentAsString());

		JspInstruction j3 = (JspInstruction) page.getChildren().get(2);
		assertEquals("taglib", j3.getName());
		assertEquals("t", j3.getAttributes().get("prefix"));
		assertEquals("/WEB-INF/tags", j3.getAttributes().get("tagdir"));

		JspText j4 = (JspText) page.getChildren().get(3);
		assertTrue(j4.getContentAsString().equals("\r\n"));

		JspTaglibInvocation j5 = (JspTaglibInvocation) page.getChildren().get(4);
		assertEquals("t:structure", j5.getName());

		assertEquals(5, j5.getChildren().size());

		JspText j5_1 = (JspText) j5.getChildren().get(0);
		assertTrue(j5_1.getContentAsString().startsWith("\r\n<p>This"));
		assertTrue(j5_1.getContentAsString().endsWith("</p>\r\n"));

		JspTaglibInvocation j5_2 = (JspTaglibInvocation) j5.getChildren().get(1);
		assertEquals("t:news", j5_2.getName());
		assertTrue(j5_2.getChildren().isEmpty());
		assertEquals("${listOfNews}", j5_2.getAttributes().get("listOfNews"));

	}

	@Test
	public void testJspRenderer() throws Exception {
		JspPage page = parser.parse("WEB-INF/jsp/homepage.jsp");

		session.request.setAttribute(RenderSession.ATTR_TIMEZONE, TimeZone.getTimeZone("UTC"));

		request.setAttribute("navigation",
				Arrays.asList(new NavigationItem("path 1", "label 1"), new NavigationItem("path 2", "label 2")));
		request.setAttribute("listOfStocks",
				Arrays.asList(new Stock("S1", "Stock one", 10, 20), new Stock("S2", "Stock 2", -9, 88)));
		request.setAttribute("listOfNews",
				Arrays.asList(new News("1", "headline 1", "description 1", "full text of news 1", false),
						new News("2", "headline 2", "description 2", "full text of news 2", false)));

		request.setAttribute("date", new GregorianCalendar(2000, 2, 2, 18, 23, 45).getTime());
		renderer.render(page, session);
		String text = getPrettyContent(response);

		String expected = sanitize(read("/expected/newspage.html"));
		assertEquals(expected, text);
	}

	@Test
	public void testJspParserNews() throws Exception {
		String expected = sanitize(read("/expected/newspage2.html"));
		JspPage page = parser.parse("news.jsp");

		SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = isoFormat.parse("2010-09-23T14:27:18");

		request.setAttribute("date", date);

		request.setAttribute("navigation",
				Arrays.asList(new NavigationItem("path 1", "label 1"), new NavigationItem("path 2", "label 2")));
		request.setAttribute("listOfStocks", Arrays.asList(new Stock("S1", "Stock one", 25000, 1),
				new Stock("S2", "Stock two", 10000, -2), new Stock("S3", "Stock3", 9999, 12)));
		request.setAttribute("listOfNews",
				Arrays.asList(new News("1", "headline 1", "description 1", "full text of news 1", true),
						new News("2", "headline 2", "description 2", "full text of news 2", false)));
		request.setAttribute("news", new News("1", "headline 1", "description 1", "full text of news 3", true));

		renderer.render(page, session);
		String text = getPrettyContent(response);
		assertEquals(expected, text);
	}

	@Test
	public void testHtmlEscape() throws Exception {
		JspPage page = parser.parse("news.jsp");
		request.setAttribute("navigation",
				Arrays.asList(new NavigationItem("path 1", "label 1"), new NavigationItem("path 2", "label 2")));
		request.setAttribute("listOfStocks", Arrays.asList(new Stock("S1", "Stock one", 25000, 1),
				new Stock("S2", "Stock two", 10000, -2), new Stock("S3", "Stock3", 9999, 12)));
		request.setAttribute("listOfNews",
				Arrays.asList(new News("1", "Action & news 1", "description 1", "full text of news 1", true),
						new News("2", "headline 2", "description 2", "full text of news 2", false)));
		SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = isoFormat.parse("2010-09-23T14:27:18");
		request.setAttribute("date", date);

		renderer.render(page, session);
		String text = getPrettyContent(response);
		assertTrue(text, text.contains("+++Action &amp; news 1"));
	}

	@Test
	public void testRendererForEach() {
		JspPage page = parser.parse("WEB-INF/jsp/foreach.jsp");

		request.setAttribute("listOfNews",
				Arrays.asList(new News("0", "headline 0", "description 0", "full text of news 0", false),
						new News("1", "headline 1", "description 1", "full text of news 1", false),
						new News("2", "headline 2", "description 2", "full text of news 2", false)));
		
		request.setAttribute("end", 2);
		renderer.render(page, session);
		String text = getContent(response);
		assertEquals(
				"\nNEWS SECTION 1: 012\nNEWS SECTION 2: 12\nNEWS SECTION 3: 0\nVARSTATUS: 1,0,true,false\n2,1,false,false\n3,2,false,true\n",
				text);
	}

	// The jsp parser is using regular expressions. It's known that regular
	// expressions can't parse hierarchical syntaxes like HTML (or JSP),
	// so the implementation has some workarounds for regex limitations. This
	// test verifies that the regex doesn't get fooled by alternating,
	// nested tags
	@Test
	public void testParserMultipleNesting() {
		JspPage page = parser.parse("WEB-INF/jsp/nesting.jsp");

		renderer.render(page, session);
		String text = getPrettyContent(response);
		assertEquals(text,
				"level0-a-opens\n <a value=\"level1\">\nlevel1-b-opens\n <b value=\"level2\">\nlevel2-a-opens\n <a value=\"level3\">\nlevel3-b-opens\n <b value=\"level4\">\nlevel4-inner\n</b>\nlevel3-b-closed\n</a>\nlevel2-a-closed\n</b>\nlevel1-b-closed\n</a>\nlevel0-a-closed",
				text);
	}

	/**
	 * In a previous implementation, the {@link JspParserImpl} had a regular
	 * expression that would look for tags in the form of <namespace:tagname
	 * ...> However this picks up HTML/XML that has colons in attribute values
	 * such as <a href="http://example.com">
	 */
	@Test
	public void testTrickyColonInAttribute() {
		JspPage page = parser.parse("WEB-INF/jsp/link.jsp");
		assertEquals(1, page.getChildren().size());
		JspText node = (JspText) page.getChildren().get(0);
		assertEquals(
				"<a class=nav href=\"http://www.example.com/q?a:bbb:cccc\" id=\"some/id\"><span class=\"some class\">[...]</span>Link</a>\n",
				node.getContentAsString());
	}

}
