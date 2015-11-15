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
 * Tests jsp:attribute
 * @author George Georgovassilis
 *
 */

public class TestJspAttribute extends BaseTest{
	
	@Test
	public void jsp_attibute(){
		JspPage page = parser.parse("WEB-INF/jsp/jsp_attribute.jsp");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		ByteArrayOutputStream baos = response.getBaos();
		RenderSession session = new RenderSession();
		session.request = new ServletRequestWrapper(request);
		session.renderer = renderer;
		session.elEvaluation = elEvaluation;
		session.response = new ServletResponseWrapper(response, response.getBaos());
		
		renderer.render(page, session);
		String text = sanitize(text(baos));
		assertEquals(text,"before\n<frame>\n<a>value1</a>\n<body>middle</body>\n<b>value2</b>\n</frame>\nafter", text);
	}

}