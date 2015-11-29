package fauxjsp.test.unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import fauxjsp.api.nodes.JspPage;

/**
 * Tests the JSTL var implementation
 * @author George Georgovassilis
 *
 */

public class TestJstlVar extends BaseTest{
	
	@Test
	public void test_var(){
		JspPage page = parser.parse("WEB-INF/jsp/var.jsp");
		request.setAttribute("x", "test");

		renderer.render(page, session);
		String text = getPrettyContent(response);
		assertEquals("test + string = test string", text);
	}

}