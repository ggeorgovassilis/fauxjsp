package jspparser;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;

import jspparser.utils.MockHttpServletRequest;
import jspparser.utils.MockHttpServletResponse;

import org.junit.Test;

import fauxjsp.api.nodes.JspPage;
import fauxjsp.api.renderer.RenderSession;

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
		session.request = request;
		session.renderer = renderer;
		session.elEvaluation = elEvaluation;
		session.response = response;
		
		request.setAttribute("x", "test");

		renderer.render(page, session);
		String text = new String(baos.toByteArray());
		assertEquals("\n\n\ntest + string = test string", text);
	}

}