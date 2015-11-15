package fauxjsp.test.unittests;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import fauxjsp.api.nodes.JspPage;
import fauxjsp.api.renderer.RenderSession;
import fauxjsp.servlet.ServletRequestWrapper;
import fauxjsp.servlet.ServletResponseWrapper;
import fauxjsp.test.support.MockHttpServletRequest;
import fauxjsp.test.support.MockHttpServletResponse;

/**
 * Tests fn: functions
 * 
 * @author George Georgovassilis
 *
 */

public class TestFunctions extends BaseTest {

	MockHttpServletRequest request;
	JspPage page;
	RenderSession session;
	ByteArrayOutputStream baos;

	@Before
	public void setup() {
		page = parser.parse("WEB-INF/jsp/functions.jsp");
		request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		baos = response.getBaos();
		session = new RenderSession();
		session.request = new ServletRequestWrapper(request);
		session.renderer = renderer;
		session.elEvaluation = elEvaluation;
		session.response = new ServletResponseWrapper(response, response.getBaos());
		request.setAttribute("s1", "The rain in Spain falls on the plain");
		request.setAttribute("s2", "The");
		request.setAttribute("s3", "rain");
		request.setAttribute("arr",
				new String[] { "The", "rain", "in", "Spain" });
		request.setAttribute("list",
				Arrays.asList(new String[] { "The", "rain", "in", "Spain" }));
	}

	@Test
	public void test_startsWith() {
		renderer.render(page, session);
		String text = text(baos);
		assertTrue(text, text.contains("Condition 1"));
		assertFalse(text, text.contains("Condition 2"));
	}

	@Test
	public void test_contains() {
		renderer.render(page, session);
		String text = text(baos);
		assertTrue(text, text.contains("Condition 3"));
		assertFalse(text, text.contains("Condition 4"));
	}

	@Test
	public void test_containsIgnoreCase() {
		renderer.render(page, session);
		String text = text(baos);
		assertTrue(text, text.contains("Condition 5"));
		assertFalse(text, text.contains("Condition 6"));
	}

	@Test
	public void test_endsWith() {
		renderer.render(page, session);
		String text = text(baos);
		assertTrue(text, text.contains("Condition 7"));
		assertFalse(text, text.contains("Condition 8"));
	}

	@Test
	public void test_escapeXml() {
		// TODO: escapeXml(...) will work as advertised. however, if I <c:out
		// value="fn:escapeXml(...)"/> this will double-encode
		// the result. verify how "standard" JSP does it.
		request.setAttribute("xml", "<howdy/>");
		renderer.render(page, session);
		String text = text(baos);
		assertTrue(text, text.contains("escapeXml &lt;howdy/&gt;"));
	}

	@Test
	public void test_indexOf() {
		renderer.render(page, session);
		String text = text(baos);
		assertTrue(text, text.contains("indexOf 4"));
	}

	@Test
	public void test_join() {
		renderer.render(page, session);
		String text = text(baos);
		assertTrue(text.contains("join The_rain_in_Spain"));
	}

	@Test
	public void test_length() {
		renderer.render(page, session);
		String text = text(baos);
		assertTrue(text.contains("length 4, 4, 36"));
	}

	@Test
	public void test_replace() {
		renderer.render(page, session);
		String text = text(baos);
		assertTrue(text.contains("replace Life is good"));
	}

	@Test
	public void test_split() {
		renderer.render(page, session);
		String text = text(baos);
		assertTrue(text.contains("split The,rain,in,Spain"));
	}

	@Test
	public void test_substring() {
		renderer.render(page, session);
		String text = text(baos);
		assertTrue(text.contains("substring rain."));
	}

	@Test
	public void test_toLowerCase() {
		renderer.render(page, session);
		String text = text(baos);
		assertTrue(text.contains("toLowerCase some word"));
	}

	@Test
	public void test_toUpperCase() {
		renderer.render(page, session);
		String text = text(baos);
		assertTrue(text.contains("toUpperCase SOME WORD"));
	}

	@Test
	public void test_trim() {
		renderer.render(page, session);
		String text = text(baos);
		assertTrue(text.contains("trim (this    is  some text)"));
	}

}
