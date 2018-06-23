package fauxjsp.test.unittests;

import org.junit.Test;

import fauxjsp.api.nodes.JspPage;

import static org.junit.Assert.*;

/**
 * Tests choose/otherwise
 * 
 * @author george georgovassilis
 *
 */
public class TestTrimDirectiveWhitespaces extends BaseTest {

	@Test
	public void test_preserve_whitespaces() {
		String expected = read("/expected/whitespace-preserved.txt");
		JspPage page = parser.parseJsp("WEB-INF/jsp/whitespaces.jsp");
		renderer.render(page, session);
		String text = getContent(response);
		assertEquals(expected, text);
	}

	@Test
	public void test_trim_whitespaces() {
		String expected = read("/expected/whitespace-trimmed.txt");
		JspPage page = parser.parseJsp("WEB-INF/jsp/whitespaces.jsp");
		session.trimDirectiveWhiteSpaces = true;
		renderer.render(page, session);
		String text = getContent(response);
		assertEquals(expected, text);
	}

}
