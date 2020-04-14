package fauxjsp.test.unittests;

import org.junit.Test;

import fauxjsp.api.nodes.JspPage;

import static org.junit.Assert.*;

/**
 * Tests comments
 * 
 * @author george georgovassilis
 *
 */
public class TestComment extends BaseTest {

	@Test
	public void test_choose() {
		JspPage page = parser.parseJsp("WEB-INF/jsp/comment.jsp");
		renderer.render(page, session);
		String text = getPrettyContent(response).trim();
		assertEquals(text, "Life goes on.", text);
	}

}
