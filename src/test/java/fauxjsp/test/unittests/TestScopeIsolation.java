package fauxjsp.test.unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import fauxjsp.api.nodes.JspPage;

/**
 * Tests scope isolation
 * @author George Georgovassilis
 *
 */

public class TestScopeIsolation extends BaseTest{
	
	@Test
	public void test_scope_isolation(){
		JspPage page = parser.parseJspFragment("WEB-INF/jsp/scope_isolation.jsp");

		session.request.setAttribute("a", "0");
		
		renderer.render(page, session);
		String text = getPrettyContent(response);
		assertEquals(text,"0=0\n1=1\na=2\n1=1\na=3\na=4\n4=4", text);
	}
}
