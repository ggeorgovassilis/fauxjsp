package fauxjsp.test.unittests;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import fauxjsp.api.nodes.JspPage;
import fauxjsp.api.parser.JspParsingException;
import fauxjsp.api.renderer.JspRenderException;
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
	public void test_taglib_missing_argument() throws Exception{
		JspPage page = parser.parse("WEB-INF/jsp/error_taglib_missing_argument.jsperr");
		try {
			renderer.render(page, session);
			fail("expected a JspParsingException");
		} catch (JspRenderException e) {
			String explanation = renderer.explain(e);
			assertTrue(
					explanation,
					explanation
							.contains("Attribute listOfStocks is mandatory for taglib t:stocks"));
			assertTrue(explanation, explanation.contains("Line 3"));
		}
	}

	@Test
	public void test_taglib_wrong_argument_type() throws Exception{
		JspPage page = parser.parse("WEB-INF/jsp/error_taglib_wrong_argument_type.jsperr");
		try {
			request.setAttribute("listOfStocks", new Date());
			renderer.render(page, session);
			fail("expected a JspParsingException");
		} catch (JspRenderException e) {
			String explanation = renderer.explain(e);
			assertTrue(
					explanation,
					explanation
							.contains("Expected type java.util.List"));
			assertTrue(
					explanation,
					explanation
							.contains("for attribute listOfStocks"));
			assertTrue(
					explanation,
					explanation
							.contains("t:stocks"));
			assertTrue(
					explanation,
					explanation
							.contains("argument was class java.util.Date"));
			assertTrue(explanation, explanation.contains("Line 3"));
		}
	}

	@Test
	public void test_taglib_undeclared_argument() throws Exception{
		try {
			JspPage page = parser.parse("WEB-INF/jsp/error_taglib_undeclared_argument.jsperr");
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

	@Test
	public void test_taglib_declaration_missing_namespace() {
		try {
			parser.parse("WEB-INF/jsp/error_taglib_missing_namespace.jsperr");
			fail("expected a JspParsingException");
		} catch (JspParsingException e) {
			String explanation = parser.explain(e);
			assertTrue(
					explanation,
					explanation
							.contains("Missing prefix attribute"));
			assertTrue(explanation, explanation.contains("Line 1"));
		}
	}

	@Test
	public void test_taglib_declaration_duplicate_namespace() {
		try {
			parser.parse("WEB-INF/jsp/error_taglib_duplicate_namespace.jsperr");
			fail("expected a JspParsingException");
		} catch (JspParsingException e) {
			String explanation = parser.explain(e);
			assertTrue(
					explanation,
					explanation
							.contains("Taglib prefix 't' already used"));
			assertTrue(explanation, explanation.contains("Line 2"));
		}
	}

}
