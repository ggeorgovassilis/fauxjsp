package fauxjsp.test.unittests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import fauxjsp.api.nodes.JspPage;

/**
 * Ensure that parser leaves xml elements with namespaces (eg. <crm:address>) alone
 * if it's not a declared taglib namespace
 * @author george georgovassilis
 *
 */
public class TestProperNamespaceTolerance extends BaseTest{

	@Test
	public void test_namespace() {
		request.setAttribute("thumbnail_loc", "testlocation");
		request.setAttribute("title", "testtitle");
		JspPage page = parser.parseJsp("WEB-INF/jsp/xml_namespace.jsp");
		renderer.render(page, session);
		String text = getContent(response);
		String expected = read("/expected/namespace.xml");
		assertEquals(expected, text);
	}
}
