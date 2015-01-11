package jspparser;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;

import jspparser.utils.MockHttpServletRequest;
import jspparser.utils.MockHttpServletResponse;

import org.junit.Test;

import fauxjsp.api.nodes.JspPage;
import fauxjsp.api.renderer.RenderSession;
import fauxjsp.impl.simulatedtaglibs.fmt.JstlFmtMessage;

/**
 * Tests the FMT taglib implementation
 * @author George Georgovassilis
 *
 */

public class TestFmt extends BaseTest{
	
	@Test
	public void test_fmt_message(){
		JspPage page = parser.parse("WEB-INF/jsp/fmt_message.jsp");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		ByteArrayOutputStream baos = response.getBaos();
		RenderSession session = new RenderSession();
		session.request = request;
		session.renderer = renderer;
		session.elEvaluation = elEvaluation;
		session.response = response;

		session.request.setAttribute(JstlFmtMessage.ATTR_RESOURCE_BUNDLE, "messages");
		
		renderer.render(page, session);
		String text = new String(baos.toByteArray());
		assertEquals("\nThe name of the game is blame", text);
	}

	@Test
	public void test_fmt_set_bundle(){
		JspPage page = parser.parse("WEB-INF/jsp/fmt_set_bundle.jsp");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		ByteArrayOutputStream baos = response.getBaos();
		RenderSession session = new RenderSession();
		session.request = request;
		session.renderer = renderer;
		session.elEvaluation = elEvaluation;
		session.response = response;

		session.request.setAttribute(JstlFmtMessage.ATTR_RESOURCE_BUNDLE, "messages");
		
		renderer.render(page, session);
		String text = new String(baos.toByteArray());
		assertEquals("\n\nThe name of the game is the same", text);
	}
}
