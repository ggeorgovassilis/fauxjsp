package fauxjsp.test.unittests;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import fauxjsp.api.nodes.JspPage;
import fauxjsp.api.renderer.JspRenderException;
import fauxjsp.api.renderer.RenderSession;
import fauxjsp.impl.simulatedtaglibs.fmt.JstlFmtMessage;

/**
 * Tests the FMT taglib implementation
 * 
 * @author George Georgovassilis
 *
 */

public class TestFmt extends BaseTest {

	@Test
	public void test_fmt_message() {
		JspPage page = parser.parseJsp("WEB-INF/jsp/fmt_message.jsp");

		session.request.setAttribute(JstlFmtMessage.ATTR_RESOURCE_BUNDLE, "messages");

		renderer.render(page, session);
		String text = getPrettyContent(response);
		assertEquals("The name of the game is blame", text);
	}

	@Test
	public void test_fmt_set_bundle() {
		JspPage page = parser.parseJsp("WEB-INF/jsp/fmt_set_bundle.jsp");

		session.request.setAttribute(JstlFmtMessage.ATTR_RESOURCE_BUNDLE, "messages");

		renderer.render(page, session);
		String text = getPrettyContent(response);
		assertEquals("The name of the game is the same", text);
	}

	@Test
	public void test_fmt_bundle_not_found() {
		JspPage page = parser.parseJsp("WEB-INF/jsp/fmt_no_bundle.jsp");

		session.request.setAttribute(JstlFmtMessage.ATTR_RESOURCE_BUNDLE, null);

		try {
			renderer.render(page, session);
		} catch (JspRenderException e) {
			assertTrue(e.getMessage(), e.getMessage().contains("No resource bundle name found"));
		}
	}

	@Test
	public void test_fmt_formatdate() throws Exception {
		JspPage page = parser.parseJsp("WEB-INF/jsp/fmt_format_date.jsp");
		request.setLocale(Locale.UK);

		SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = isoFormat.parse("2010-09-23T14:27:18");
		request.setAttribute("now", date);
		request.setAttribute(RenderSession.ATTR_TIMEZONE, TimeZone.getTimeZone("UTC"));

		renderer.render(page, session);
		String text = getPrettyContent(response);

		String expected = "#1: 14:27:18\n" + "#2: 23-Sep-2010\n" + "#3: 23-Sep-2010 14:27:18\n" + "#4: 23/09/10 14:27\n"
				+ "#5: 23-Sep-2010 14:27:18\n" + "#6: 23 September 2010 14:27:18 UTC\n" + "#7: 2010-09-23\n"
				+ "#8: 23/09/10 14:27:18 UTC";

		assertEquals(expected, text);
	}

	@Test
	public void test_fmt_formatdate_value_is_null() {
		JspPage page = parser.parseJsp("WEB-INF/jsp/fmt_format_date.jsp");
		request.setLocale(Locale.UK);
		Date date = null;
		request.setAttribute("now", date);

		try {
			renderer.render(page, session);
			fail("Excpected failure");
		} catch (JspRenderException e) {
			assertTrue(e.getMessage(), e.getMessage().contains("'${now}' is null"));
		}
	}

	@Test
	public void test_fmt_formatdate_value_is_not_a_date() {
		JspPage page = parser.parseJsp("WEB-INF/jsp/fmt_format_date.jsp");
		request.setLocale(Locale.UK);
		String date = "01-02-2013";
		request.setAttribute("now", date);

		try {
			renderer.render(page, session);
			fail("Excpected failure");
		} catch (JspRenderException e) {
			assertTrue(e.getMessage(),
					e.getMessage().contains("'${now}' is a class java.lang.String but I need a java.util.Date"));
		}

	}

	@Test
	public void test_fmt_formatNumber() {
		JspPage page = parser.parseJsp("WEB-INF/jsp/fmt_format_number.jsp");
		request.setLocale(Locale.UK);
		request.setAttribute("balance", 120000.231);
		renderer.render(page, session);
		String text = getPrettyContent(response);

		String expected = "#1 £120,000.23\n" + "#2 000.231\n" + "#3 120000.231\n" + "#4 120000.231\n" + "#5 023%\n"
				+ "#6 12,000,023.1000000000%\n" + "#7 023%\n" + "#8 120E3\n" + "#9 $120,000.23";

		assertEquals(expected, text);
	}

}
