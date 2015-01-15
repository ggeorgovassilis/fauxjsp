package jspparser;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.GregorianCalendar;

import jspparser.dto.NavigationItem;
import jspparser.dto.News;
import jspparser.dto.Stock;
import jspparser.utils.MockHttpServletRequest;
import jspparser.utils.MockHttpServletResponse;
import jspparser.utils.TestUtils;

import org.junit.Test;

import fauxjsp.api.nodes.JspInstruction;
import fauxjsp.api.nodes.JspPage;
import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.JspText;
import fauxjsp.api.renderer.RenderSession;
import fauxjsp.impl.parser.JspParserImpl;
import fauxjsp.servlet.ServletRequestWrapper;
import fauxjsp.servlet.ServletResponseWrapper;
import static org.junit.Assert.*;

/**
 * Test of {@link JspParserImpl}
 * 
 * @author George Georgovassilis
 *
 */

public class TestJspParserAndRenderer extends BaseTest{

	@Test
	public void testParser() throws Exception {
		JspPage page = parser.parse("WEB-INF/jsp/homepage.jsp");
		assertEquals(5, page.getChildren().size());

		JspInstruction j1 = (JspInstruction) page.getChildren().get(0);
		assertEquals("taglib", j1.getName());
		assertEquals("c", j1.getArguments().get("prefix"));
		assertEquals("http://java.sun.com/jsp/jstl/core", j1.getArguments()
				.get("uri"));

		JspText j2 = (JspText) page.getChildren().get(1);
		assertEquals("\r\n", j2.getContentAsString());

		JspInstruction j3 = (JspInstruction) page.getChildren().get(2);
		assertEquals("taglib", j3.getName());
		assertEquals("t", j3.getArguments().get("prefix"));
		assertEquals("/WEB-INF/tags", j3.getArguments().get("tagdir"));

		JspText j4 = (JspText) page.getChildren().get(3);
		assertTrue(j4.getContentAsString().equals("\r\n"));

		JspTaglibInvocation j5 = (JspTaglibInvocation) page.getChildren()
				.get(4);
		assertEquals("t:structure", j5.getName());

		assertEquals(3, j5.getChildren().size());

		JspText j5_1 = (JspText) j5.getChildren().get(0);
		assertTrue(j5_1.getContentAsString().startsWith("\r\n<p>This"));
		assertTrue(j5_1.getContentAsString().endsWith("</p>\r\n"));

		JspTaglibInvocation j5_2 = (JspTaglibInvocation) j5.getChildren()
				.get(1);
		assertEquals("t:news", j5_2.getName());
		assertTrue(j5_2.getChildren().isEmpty());
		assertEquals("${listOfNews}", j5_2.getArguments().get("listOfNews"));

	}

	@Test
	public void testJspRenderer() throws Exception{
		JspPage page = parser.parse("WEB-INF/jsp/homepage.jsp");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		ByteArrayOutputStream baos = response.getBaos();
		RenderSession session = new RenderSession();
		session.request = new ServletRequestWrapper(request);
		session.renderer = renderer;
		session.elEvaluation = elEvaluation;
		session.response = new ServletResponseWrapper(response, response.getBaos());

		session.request.setAttribute("navigation", Arrays.asList(
				new NavigationItem("path 1", "label 1"), new NavigationItem(
						"path 2", "label 2")));
		session.request
				.setAttribute("listOfStocks", Arrays.asList(new Stock("S1",
						"Stock one", 10, 20),
						new Stock("S2", "Stock 2", -9, 88)));
		session.request.setAttribute("listOfNews", Arrays.asList(new News("1",
				"headline 1", "description 1", "full text of news 1", false),
				new News("2", "headline 2", "description 2",
						"full text of news 2", false)));

		session.request.setAttribute("date", new GregorianCalendar(2000, 2, 2).getTime());
		renderer.render(page, session);
		String text = text(baos);
		assertTrue(text.contains("Thu Mar 02"));
		assertTrue(text
				.contains("<a href=\"news?id=2\" class=headline>headline 2</a>"));
		assertTrue(text, text.contains("<span class=price>0.2 €</span>"));
		
		//lazy man's arse-covering insurance that we didn't change something in the implementation without knowing about it
		assertEquals("c55ae99e22e0d3fb23a262328c57bcea",TestUtils.MD5(text));
	}

	@Test
	public void testJspParserNews() {
		JspPage page = parser.parse("WEB-INF/jsp/news.jsp");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		ByteArrayOutputStream baos = response.getBaos();
		RenderSession session = new RenderSession();
		session.request = new ServletRequestWrapper(request);
		session.renderer = renderer;
		session.elEvaluation = elEvaluation;
		session.response = new ServletResponseWrapper(response, response.getBaos());

		session.request.setAttribute("navigation", Arrays.asList(
				new NavigationItem("path 1", "label 1"), new NavigationItem(
						"path 2", "label 2")));
		session.request.setAttribute("listOfStocks", Arrays.asList(new Stock(
				"S1", "Stock one", 25000, 1), new Stock("S2", "Stock two",
				10000, -2), new Stock("S3", "Stock3", 9999, 12)));
		session.request.setAttribute("listOfNews", Arrays.asList(new News("1",
				"headline 1", "description 1", "full text of news 1", true),
				new News("2", "headline 2", "description 2",
						"full text of news 2", false)));
		session.request.setAttribute("news", new News("1", "headline 1",
				"description 1", "full text of news 3", true));

		renderer.render(page, session);
		String text = text(baos);
		assertTrue(text.contains("+++headline 1"));
	}

	@Test
	public void testHtmlEscape() {
		JspPage page = parser.parse("WEB-INF/jsp/news.jsp");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		ByteArrayOutputStream baos = response.getBaos();
		RenderSession session = new RenderSession();
		session.request = new ServletRequestWrapper(request);
		session.renderer = renderer;
		session.elEvaluation = elEvaluation;
		session.response = new ServletResponseWrapper(response, response.getBaos());

		session.request.setAttribute("navigation", Arrays.asList(
				new NavigationItem("path 1", "label 1"), new NavigationItem(
						"path 2", "label 2")));
		session.request.setAttribute("listOfStocks", Arrays.asList(new Stock(
				"S1", "Stock one", 25000, 1), new Stock("S2", "Stock two",
				10000, -2), new Stock("S3", "Stock3", 9999, 12)));
		session.request.setAttribute("listOfNews", Arrays.asList(new News("1",
				"headline 1", "description 1", "full text of news 1", true),
				new News("2", "headline 2", "description 2",
						"full text of news 2", false)));
		session.request.setAttribute("news", new News("1", "Action & news 1",
				"description 1", "full text of news 3", true));

		renderer.render(page, session);
		String text = text(baos);
		assertTrue(text.contains("+++Action &amp; news 1"));
	}

	@Test
	public void testRendererForEach() {
		JspPage page = parser.parse("WEB-INF/jsp/foreach.jsp");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		ByteArrayOutputStream baos = response.getBaos();
		RenderSession session = new RenderSession();
		session.request = new ServletRequestWrapper(request);
		session.renderer = renderer;
		session.elEvaluation = elEvaluation;
		session.response = new ServletResponseWrapper(response, response.getBaos());

		session.request.setAttribute("listOfNews", Arrays.asList(new News("0",
				"headline 0", "description 0", "full text of news 0", false),
				new News("1", "headline 1", "description 1",
						"full text of news 1", false), new News("2", "headline 2",
						"description 2", "full text of news 2", false)));
		renderer.render(page, session);
		String text = text(baos);
		assertEquals("\nNEWS SECTION 1: 012\nNEWS SECTION 2: 12\nNEWS SECTION 3: 0\nVARSTATUS: 1,0,true,false\n2,1,false,false\n3,2,false,true\n", text);
	}

	//The jsp parser is using regular expressions. It's known that regular expressions can't parse hierarchical syntaxes like HTML (or JSP),
	//so the implementation has some workarounds for regex limitations. This test verifies that the regex doesn't get fooled by alternating,
	//nested tags
	@Test
	public void testParserMultipleNesting() {
		JspPage page = parser.parse("WEB-INF/jsp/nesting.jsp");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		ByteArrayOutputStream baos = response.getBaos();
		RenderSession session = new RenderSession();
		session.request = new ServletRequestWrapper(request);
		session.renderer = renderer;
		session.elEvaluation = elEvaluation;
		session.response = new ServletResponseWrapper(response, response.getBaos());

		renderer.render(page, session);
		String text = text(baos);
		assertEquals(
				"\nlevel0-a-opens\n <a value=\"level1\">\nlevel1-b-opens\n <b value=\"level2\">\nlevel2-a-opens\n <a value=\"level3\">\nlevel3-b-opens\n <b value=\"level4\">\nlevel4-inner\n</b>\nlevel3-b-closed\n</a>\nlevel2-a-closed\n</b>\nlevel1-b-closed\n</a>\nlevel0-a-closed",
				text);
	}
	
	/**
	 * In a previous implementation, the {@link JspParserImpl} had a regular expression that would look for tags in the form of <namespace:tagname ...>
	 * However this picks up HTML/XML that has colons in attribute values such as <a href="http://example.com">
	 */
	@Test
	public void testTrickyColonInAttribute(){
		JspPage page = parser.parse("WEB-INF/jsp/link.jsp");
		assertEquals(1, page.getChildren().size());
		JspText node = (JspText)page.getChildren().get(0);
		assertEquals("<a class=nav href=\"http://www.example.com/q?a:bbb:cccc\" id=\"some/id\"><span class=\"some class\">[...]</span>Link</a>\n", node.getContentAsString());
	}

}
