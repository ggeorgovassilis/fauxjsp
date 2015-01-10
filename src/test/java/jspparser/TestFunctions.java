package jspparser;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;

import jspparser.utils.MockHttpServletRequest;
import jspparser.utils.MockHttpServletResponse;

import org.junit.Test;

import fauxjsp.api.RenderSession;
import fauxjsp.api.nodes.JspPage;

/**
 * Tests fn: functions
 * @author George Georgovassilis
 *
 */

public class TestFunctions extends BaseTest{
	
	@Test
	public void test_startsWith(){
		JspPage page = parser.parse("WEB-INF/jsp/functions.jsp");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		ByteArrayOutputStream baos = response.getBaos();
		RenderSession session = new RenderSession();
		session.request = request;
		session.renderer = renderer;
		session.elEvaluation = elEvaluation;
		session.response = response;

		request.setAttribute("s1", "The rain in Spain falls on the plain");
		request.setAttribute("s2", "The");
		request.setAttribute("s3", "rain");
		
		renderer.render(page, session);
		String text = new String(baos.toByteArray());
		assertTrue(text.contains("Condition 1"));
		assertFalse(text.contains("Condition 2"));
	}

}
