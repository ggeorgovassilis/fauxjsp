package fauxjsp.test.unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import fauxjsp.api.nodes.JspPage;

/**
 * Test the include instruction
 * @author George Georgovassilis
 *
 */

public class TestIncludeInstruction extends BaseTest{
	
	@Test
	public void test_include() throws Exception{
		JspPage page = parser.parse("WEB-INF/jsp/include.jsp");

		renderer.render(page, session);
		String text = getContent(response);
		assertEquals(text, "Part 1 Part 2 Part 3", text);
	}

}
