package fauxjsp.test.unittests;

import org.junit.Test;

import fauxjsp.api.nodes.JspPage;

import static org.junit.Assert.*;

/**
 * Tests unicode in text
 * 
 * @author george georgovassilis
 *
 */
public class TestUnicode extends BaseTest {

	@Test
	public void test_utf8() {
		JspPage page = parser.parseJsp("WEB-INF/jsp/unicode.jsp");
		renderer.render(page, session);
		String text = getPrettyContent(response).trim();
		assertEquals(text, "aAéÉöÖεΕ", text);
	}

}
