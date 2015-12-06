package fauxjsp.test.unittests;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import fauxjsp.api.nodes.JspPage;

/**
 * Tests fn: functions
 * 
 * @author George Georgovassilis
 *
 */

public class TestFunctions extends BaseTest {

	JspPage page;
	String text;

	@Before
	public void setup() {
		page = parser.parse("WEB-INF/jsp/functions.jsp");
		request.setAttribute("s1", "The rain in Spain falls on the plain");
		request.setAttribute("s2", "The");
		request.setAttribute("s3", "rain");
		request.setAttribute("arr",
				new String[] { "The", "rain", "in", "Spain" });
		request.setAttribute("list",
				Arrays.asList(new String[] { "The", "rain", "in", "Spain" }));
		renderer.render(page, session);
		text = getPrettyContent(response);
	}

	@Test
	public void test_startsWith() {
		assertTrue(text, text.contains("Condition 1"));
		assertFalse(text, text.contains("Condition 2"));
	}

	@Test
	public void test_contains() {
		assertTrue(text, text.contains("Condition 3"));
		assertFalse(text, text.contains("Condition 4"));
	}

	@Test
	public void test_containsIgnoreCase() {
		assertTrue(text, text.contains("Condition 5"));
		assertFalse(text, text.contains("Condition 6"));
	}

	@Test
	public void test_endsWith() {
		assertTrue(text, text.contains("Condition 7"));
		assertFalse(text, text.contains("Condition 8"));
	}

	@Test
	public void test_escapeXml() {
		// TODO: escapeXml(...) will work as advertised. however, if I <c:out
		// expression="fn:escapeXml(...)"/> this will double-encode
		// the result. verify how "standard" JSP does it.
		session.request.setAttribute("xml", "<howdy/>");
		renderer.render(page, session);
		String text = getPrettyContent(response);
		assertTrue(text, text.contains("escapeXml &lt;howdy/&gt;"));
	}

	@Test
	public void test_indexOf() {
		assertTrue(text, text.contains("indexOf 4"));
	}

	@Test
	public void test_join() {
		assertTrue(text, text.contains("join The_rain_in_Spain"));
	}

	@Test
	public void test_length() {
		assertTrue(text, text.contains("length 4, 4, 36"));
	}

	@Test
	public void test_replace() {
		assertTrue(text.contains("replace Life is good"));
	}

	@Test
	public void test_split() {
		assertTrue(text.contains("split The,rain,in,Spain"));
	}

	@Test
	public void test_substring() {
		assertTrue(text.contains("substring rain."));
	}

	@Test
	public void test_toLowerCase() {
		assertTrue(text.contains("toLowerCase some word"));
	}

	@Test
	public void test_toUpperCase() {
		assertTrue(text.contains("toUpperCase SOME WORD"));
	}

	@Test
	public void test_trim() {
		assertTrue(text.contains("trim (this    is  some text)"));
	}

}
