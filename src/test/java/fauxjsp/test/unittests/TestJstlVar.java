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
		request.setAttribute("x", "test");

		renderer.render(page, session);
		String text = getPrettyContent(response);
		assertEquals("test + string = test string", text);
	}

}