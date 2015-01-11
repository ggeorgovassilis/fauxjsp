package jspparser;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;

import jspparser.utils.MockHttpServletRequest;
import jspparser.utils.MockHttpServletResponse;

import org.junit.Test;

import fauxjsp.api.nodes.JspPage;
import fauxjsp.api.renderer.RenderSession;

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
		session.request = request;
		session.renderer = renderer;
		session.elEvaluation = elEvaluation;
		session.response = response;

		renderer.render(page, session);
		String text = new String(baos.toByteArray(), "UTF-8");
		assertEquals(text, "Part 1 Part 2 Part 3", text);
	}

}
