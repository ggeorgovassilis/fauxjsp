package fauxjsp.test.unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import fauxjsp.api.nodes.JspPage;

/**
 * Tests page directives
 * 
 * @author George Georgovassilis
 *
 */

public class TestPageDirective extends BaseTest {

	@Test
	public void test_response_type() {
		JspPage page = parser.parseJsp("WEB-INF/jsp/page_directive.jsp");
		renderer.render(page, session);
		String text = getPrettyContent(response);
		assertEquals("abc123", text);
		
		assertEquals("application/xml", session.response.getContentType());
		assertEquals("UTF-8", session.response.getCharacterEncoding());
	}

	@Test
	public void test_response_type_no_charset() {
		JspPage page = parser.parseJsp("WEB-INF/jsp/page_directive2.jsp");
		renderer.render(page, session);
		String text = getPrettyContent(response);
		assertEquals("abc123", text);
		
		assertEquals("application/xml", session.response.getContentType());
	}

}
