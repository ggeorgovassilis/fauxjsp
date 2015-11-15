package fauxjsp.test.unittests;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;

import org.junit.Test;

import fauxjsp.api.nodes.JspPage;
import fauxjsp.api.renderer.RenderSession;
import fauxjsp.servlet.ServletRequestWrapper;
import fauxjsp.servlet.ServletResponseWrapper;
import fauxjsp.test.support.MockHttpServletRequest;
import fauxjsp.test.support.MockHttpServletResponse;

/**
 * Test the include instruction
 * @author George Georgovassilis
 *
 */

public class TestIncludeInstruction extends BaseTest{
	
	@Test
	public void test_include() throws Exception{
		JspPage page = parser.parse("WEB-INF/jsp/include.jsp");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		ByteArrayOutputStream baos = response.getBaos();
		RenderSession session = new RenderSession();
		session.request = new ServletRequestWrapper(request);
		session.renderer = renderer;
		session.elEvaluation = elEvaluation;
		session.response = new ServletResponseWrapper(response, response.getBaos());

		renderer.render(page, session);
		String text = text(baos);
		assertEquals(text, "Part 1 Part 2 Part 3", text);
	}

}
