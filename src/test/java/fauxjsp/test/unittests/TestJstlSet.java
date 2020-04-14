package fauxjsp.test.unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import fauxjsp.api.nodes.JspPage;

/**
 * Tests the JSTL var implementation
 * @author George Georgovassilis
 *
 */

public class TestJstlSet extends BaseTest{
	
	@Test
	public void test_var(){
		JspPage page = parser.parseJspFragment("WEB-INF/jsp/set.jsp");
		String expected=read("/expected/set.txt");
		session.request.setAttribute("x", "test");

		renderer.render(page, session);
		String text = getPrettyContent(response);
		assertEquals(expected, text);
	}

}