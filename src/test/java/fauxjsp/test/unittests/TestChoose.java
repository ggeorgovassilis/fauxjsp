package fauxjsp.test.unittests;

import org.junit.Test;

import fauxjsp.api.nodes.JspPage;
import fauxjsp.api.renderer.JspRenderException;
import fauxjsp.impl.simulatedtaglibs.fmt.JstlFmtMessage;

import static org.junit.Assert.*;

/**
 * Tests choose/otherwise
 * 
 * @author george georgovassilis
 *
 */
public class TestChoose extends BaseTest {

	@Test
	public void test_choose() {
		request.setAttribute("change", -12);
		JspPage page = parser.parse("WEB-INF/jsp/c_choose.jsp");
		renderer.render(page, session);
		String text = getPrettyContent(response);
		assertTrue(text, text.contains("change negative"));
		assertTrue(text, text.contains("-12"));
	}

	@Test
	public void test_choose_bad_element() {
		request.setAttribute("change", -12);
		JspPage page = parser.parse("WEB-INF/jsp/error_c_choose_bad_element.jsperr");
		try {
			renderer.render(page, session);
			fail("Expected to fail");
		} catch (JspRenderException e) {
			String explanation = renderer.explain(e);
			assertTrue(explanation, explanation.contains("Invalid child c:out"));
			assertTrue(explanation, explanation.contains("Line 6"));
		}
	}
}
