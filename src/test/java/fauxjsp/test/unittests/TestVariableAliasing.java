package fauxjsp.test.unittests;

import org.junit.Test;

import fauxjsp.api.nodes.JspPage;

import static org.junit.Assert.*;

/**
 * Test that tagfile attributes overwrite variables present in the context before invocation
 * 
 * @author george georgovassilis
 *
 */
public class TestVariableAliasing extends BaseTest {

	@Test
	public void test_choose() {
		request.setAttribute("X", "1");
		JspPage page = parser.parseJsp("WEB-INF/jsp/variable_aliasing.jsp");
		renderer.render(page, session);
		String[] lines = getPrettyContent(response).split("\n");
		assertEquals("X=1", lines[0]);
		assertEquals("X=2", lines[1]);
		assertEquals("X=3", lines[2]);
		assertEquals("X=4", lines[3]);
	}

}