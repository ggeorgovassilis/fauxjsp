package fauxjsp.test.unittests;

import java.util.ArrayList;

import org.junit.Test;

import fauxjsp.api.nodes.JspPage;
import fauxjsp.api.parser.JspParsingException;
import fauxjsp.api.renderer.JspRenderException;
import fauxjsp.api.renderer.RenderSession;
import fauxjsp.servlet.ServletRequestWrapper;
import fauxjsp.servlet.ServletResponseWrapper;
import fauxjsp.test.support.MockHttpServletRequest;
import fauxjsp.test.support.MockHttpServletResponse;

import static org.junit.Assert.*;

/**
 * Tests for error handling
 * @author George Georgovassilis
 *
 */

public class TestErrors extends BaseTest{

	@Test
	public void test_taglib_declaration_missing_prefix() {
		try {
			parser.parse("WEB-INF/jsp/error_taglib_missing_prefix.jsperr");
			fail("expected a JspParsingException");
		} catch (JspParsingException e) {
			String explanation = parser.explain(e);
			assertTrue(explanation, explanation.contains("Missing prefix"));
			assertTrue(explanation, explanation.contains("Line 1"));
		}
	}

	@Test
	public void test_malformed_instruction() {
		try {
			parser.parse("WEB-INF/jsp/error_malformed_instruction_1.jsperr");
			fail("expected a JspParsingException");
		} catch (JspParsingException e) {
			String explanation = parser.explain(e);
			assertTrue(explanation, explanation.contains("Unknown taglib namespace 't'"));
			assertTrue(explanation, explanation.contains("Line 3"));
		}
	}

	@Test
	public void test_taglib_declaration_missing_uri() {
		try {
			parser.parse("WEB-INF/jsp/error_taglib_missing_uri.jsperr");
			fail("expected a JspParsingException");
		} catch (JspParsingException e) {
			String explanation = parser.explain(e);
			assertTrue(
					explanation,
					explanation
							.contains("declaration requires either a uri or tagdir"));
			assertTrue(explanation, explanation.contains("Line 2"));
		}
	}

	@Test
	public void test_taglib_missing_argument() {
		JspPage page = parser.parse("WEB-INF/jsp/error_taglib_missing_argument.jsperr");
		try {
			MockHttpServletRequest request = new MockHttpServletRequest();
			MockHttpServletResponse response = new MockHttpServletResponse();
			RenderSession session = new RenderSession();
			session.request = new ServletRequestWrapper(request);
			session.renderer = renderer;
			session.elEvaluation = elEvaluation;
			session.response = new ServletResponseWrapper(response, response.getBaos());
			renderer.render(page, session);
			fail("expected a JspParsingException");
		} catch (JspRenderException e) {
			String explanation = renderer.explain(e);
			assertTrue(
					explanation,
					explanation
							.contains("for attribute navigation on t:navigation but got null"));
			assertTrue(explanation, explanation.contains("Line 8"));
		}
	}

	@Test
	public void test_taglib_undeclared_argument() throws Exception{
		try {
			JspPage page = parser.parse("WEB-INF/jsp/error_taglib_undeclared_argument.jsperr");
			MockHttpServletRequest request = new MockHttpServletRequest();
			MockHttpServletResponse response = new MockHttpServletResponse();
			RenderSession session = new RenderSession();
			session.request = new ServletRequestWrapper(request);
			session.renderer = renderer;
			session.elEvaluation = elEvaluation;
			session.response = new ServletResponseWrapper(response, response.getBaos());
			session.request.setAttribute("navigation", new ArrayList<Object>());
			session.request.setAttribute("listOfStocks", new ArrayList<Object>());
			session.request.setAttribute("listOfNews", new ArrayList<Object>());
			renderer.render(page, session);
			fail("expected a JspRenderException");
		} catch (JspRenderException e) {
			String explanation = renderer.explain(e);
			assertTrue(explanation,
					explanation.contains("unexpected attribute 'fakearg'"));
			assertTrue(explanation, explanation.contains("Line 14"));
		}
	}

	@Test
	public void test_taglib_unbalanced() {
		try {
			parser.parse("WEB-INF/jsp/error_taglib_unbalanced.jsperr");
			fail("expected a JspParsingException");
		} catch (JspParsingException e) {
			String explanation = parser.explain(e);
			assertTrue(
					explanation,
					explanation
							.contains("Found closing c:choose but expected closing c:otherwise"));
			assertTrue(explanation, explanation.contains("Line 8"));
		}
	}

	@Test(expected = Exception.class)
	public void test_missing_tagfile() {
		parser.parse("WEB-INF/jsp/error_missing_tagfile.jsperr");
	}

}
