package fauxjsp.test.unittests;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import fauxjsp.api.nodes.JspPage;
import fauxjsp.api.renderer.JspRenderException;
import fauxjsp.api.renderer.RenderSession;
import fauxjsp.impl.simulatedtaglibs.fmt.JstlFmtMessage;
import fauxjsp.servlet.ServletRequestWrapper;
import fauxjsp.servlet.ServletResponseWrapper;
import fauxjsp.test.support.MockHttpServletRequest;
import fauxjsp.test.support.MockHttpServletResponse;

/**
 * Tests the FMT taglib implementation
 * 
 * @author George Georgovassilis
 *
 */

public class TestFmt extends BaseTest {

	@Test
	public void test_fmt_message() {
		JspPage page = parser.parse("WEB-INF/jsp/fmt_message.jsp");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		ByteArrayOutputStream baos = response.getBaos();
		RenderSession session = new RenderSession();
		session.request = new ServletRequestWrapper(request);
		session.renderer = renderer;
		session.elEvaluation = elEvaluation;
		session.response = new ServletResponseWrapper(response, response.getBaos());

		session.request.setAttribute(JstlFmtMessage.ATTR_RESOURCE_BUNDLE, "messages");

		renderer.render(page, session);
		String text = text(baos);
		assertEquals("\nThe name of the game is blame", text);
	}

	@Test
	public void test_fmt_set_bundle() {
		JspPage page = parser.parse("WEB-INF/jsp/fmt_set_bundle.jsp");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		ByteArrayOutputStream baos = response.getBaos();
		RenderSession session = new RenderSession();
		session.request = new ServletRequestWrapper(request);
		session.renderer = renderer;
		session.elEvaluation = elEvaluation;
		session.response = new ServletResponseWrapper(response, response.getBaos());

		session.request.setAttribute(JstlFmtMessage.ATTR_RESOURCE_BUNDLE, "messages");

		renderer.render(page, session);
		String text = text(baos);
		assertEquals("\n\nThe name of the game is the same", text);
	}

	@Test
	public void test_fmt_formatdate() {
		JspPage page = parser.parse("WEB-INF/jsp/fmt_format_date.jsp");
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setLocale(Locale.UK);
		MockHttpServletResponse response = new MockHttpServletResponse();
		ByteArrayOutputStream baos = response.getBaos();
		RenderSession session = new RenderSession();
		@SuppressWarnings("deprecation")
		Date date = new Date(2010 - 1900, 9 - 1, 23, 14, 27, 18);
		request.setAttribute("now", date);
		session.request = new ServletRequestWrapper(request);
		session.renderer = renderer;
		session.elEvaluation = elEvaluation;
		session.response = new ServletResponseWrapper(response, response.getBaos());
		renderer.render(page, session);
		String text = text(baos);

		String expected = "\n\n#1: 14:27:18\n" + "#2: 23-Sep-2010\n" + "#3: 23-Sep-2010 14:27:18\n"
				+ "#4: 23/09/10 14:27\n" + "#5: 23-Sep-2010 14:27:18\n" + "#6: 23 September 2010 14:27:18 CEST\n"
				+ "#7: 2010-09-23\n" + "#8: 23/09/10 14:27:18 CEST";

		assertEquals(expected, text);
	}

	@Test
	public void test_fmt_formatdate_value_is_null() {
		JspPage page = parser.parse("WEB-INF/jsp/fmt_format_date.jsp");
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setLocale(Locale.UK);
		MockHttpServletResponse response = new MockHttpServletResponse();
		RenderSession session = new RenderSession();
		Date date = null;
		request.setAttribute("now", date);
		session.request = new ServletRequestWrapper(request);
		session.renderer = renderer;
		session.elEvaluation = elEvaluation;
		session.response = new ServletResponseWrapper(response, response.getBaos());

		try {
			renderer.render(page, session);
			fail("Excpected failure");
		} catch (JspRenderException e) {
			assertEquals("'${now}' is null", e.getMessage());
		}
	}

	@Test
	public void test_fmt_formatdate_value_is_not_a_date() {
		JspPage page = parser.parse("WEB-INF/jsp/fmt_format_date.jsp");
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setLocale(Locale.UK);
		MockHttpServletResponse response = new MockHttpServletResponse();
		RenderSession session = new RenderSession();
		String date = "01-02-2013";
		request.setAttribute("now", date);
		session.request = new ServletRequestWrapper(request);
		session.renderer = renderer;
		session.elEvaluation = elEvaluation;
		session.response = new ServletResponseWrapper(response, response.getBaos());

		try {
			renderer.render(page, session);
			fail("Excpected failure");
		} catch (JspRenderException e) {

			assertEquals("'${now}' is a class java.lang.String but I need a java.util.Date", e.getMessage());
		}

	}

	@Test
	public void test_fmt_formatNumber() {
		JspPage page = parser.parse("WEB-INF/jsp/fmt_format_number.jsp");
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setLocale(Locale.UK);
		MockHttpServletResponse response = new MockHttpServletResponse();
		ByteArrayOutputStream baos = response.getBaos();
		RenderSession session = new RenderSession();
		request.setAttribute("balance", 120000.231);
		session.request = new ServletRequestWrapper(request);
		session.renderer = renderer;
		session.elEvaluation = elEvaluation;
		session.response = new ServletResponseWrapper(response, response.getBaos());
		renderer.render(page, session);
		String text = text(baos);

		String expected = "\n\n#1 £120,000.23\n" +
		"#2 000.231\n" +
		"#3 120000.231\n" +
		"#4 120000.231\n" +
		"#5 023%\n" +
		"#6 12,000,023.1000000000%\n" +
		"#7 023%\n" +
		"#8 120E3\n" +
		"#9 $120,000.23";

		assertEquals(expected, text);
	}

}
