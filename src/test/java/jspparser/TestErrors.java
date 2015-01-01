package jspparser;

import org.junit.Test;

import fauxjsp.api.parser.JspParsingException;
import static org.junit.Assert.*;

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
			assertTrue(explanation, explanation.contains("scriptlet"));
			assertTrue(explanation, explanation.contains("Line 2"));
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
		try {
			parser.parse("WEB-INF/jsp/error_taglib_missing_argument.jsperr");
			fail("expected a JspParsingException");
		} catch (JspParsingException e) {
			String explanation = parser.explain(e);
			assertTrue(
					explanation,
					explanation
							.contains("Mandatory argument 'listOfNews' is missing"));
			assertTrue(explanation, explanation.contains("Line 14"));
		}
	}

	@Test
	public void test_taglib_undeclared_argument() {
		try {
			parser.parse("WEB-INF/jsp/error_taglib_undeclared_argument.jsperr");
			fail("expected a JspParsingException");
		} catch (JspParsingException e) {
			String explanation = parser.explain(e);
			assertTrue(explanation,
					explanation.contains("Unknown argument 'fakearg'"));
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
