package jspparser;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;

import jspparser.utils.MockHttpServletRequest;
import jspparser.utils.MockHttpServletResponse;

import org.junit.Test;

import fauxjsp.api.nodes.JspPage;
import fauxjsp.api.renderer.RenderSession;
import fauxjsp.servlet.ServletRequestWrapper;
import fauxjsp.servlet.ServletResponseWrapper;

/**
 * Tests scope isolation
 * @author George Georgovassilis
 *
 */

public class TestScopeIsolation extends BaseTest{
	
	@Test
	public void test_scope_isolation(){
		JspPage page = parser.parse("WEB-INF/jsp/scope_isolation.jsp");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		ByteArrayOutputStream baos = response.getBaos();
		RenderSession session = new RenderSession();
		session.request = new ServletRequestWrapper(request);
		session.renderer = renderer;
		session.elEvaluation = elEvaluation;
		session.response = new ServletResponseWrapper(response, response.getBaos());

		session.request.setAttribute("a", "0");
		
		renderer.render(page, session);
		String text = text(baos);
		assertEquals(text,"\n\n0=0\n\n1=1\n\n\na=2\n\n1=1\n\n\na=3\n\na=4\n4=4", text);
	}
}
