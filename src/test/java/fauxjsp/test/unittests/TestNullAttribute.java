package fauxjsp.test.unittests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fauxjsp.api.nodes.JspPage;

/**
 * Tests that optional, null attributes are handled as ampty
 * 
 * @author george georgovassilis
 *
 */
public class TestNullAttribute extends BaseTest{

	@Test
	public void test_choose() {
		request.setAttribute("arg1", "value1");
		JspPage page = parser.parseJsp("WEB-INF/jsp/null_attribute.jsp");
		renderer.render(page, session);
		String excepted = read("/expected/nullable.txt");
		String text = getPrettyContent(response);
		assertEquals(excepted, text);
	}

}
