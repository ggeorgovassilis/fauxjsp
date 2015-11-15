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
 * Tests the JSTL var implementation
 * @author George Georgovassilis
 *
 */

public class TestJstlVar extends BaseTest{
	
	@Test
	public void test_var(){
		JspPage page = parser.parse("WEB-INF/jsp/var.jsp");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		ByteArrayOutputStream baos = response.getBaos();
		RenderSession session = new RenderSession();
		session.request = new ServletRequestWrapper(request);
		session.renderer = renderer;
		session.elEvaluation = elEvaluation;
		session.response = new ServletResponseWrapper(response, response.getBaos());
		
		request.setAttribute("x", "test");

		renderer.render(page, session);
		String text = text(baos);
		assertEquals("\n\n\ntest + string = test string", text);
	}

}