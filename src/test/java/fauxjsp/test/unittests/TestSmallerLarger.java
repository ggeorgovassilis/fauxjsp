package fauxjsp.test.unittests;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import fauxjsp.api.nodes.JspPage;

public class TestSmallerLarger extends BaseTest {

	JspPage page;
	
	@Before
	public void setup(){
		page = parser.parseJsp("WEB-INF/jsp/smaller-larger.jsp");
	}
	
	@Test
	public void test_larger() {
		session.request.setAttribute("value", 6);
		renderer.render(page, session);
		String text = getPrettyContent(response);
		assertEquals(text, "larger", text);
	}

	@Test
	public void test_larger_equals() {
		session.request.setAttribute("value", 4.5);
		renderer.render(page, session);
		String text = getPrettyContent(response);
		assertEquals(text, "larger-equals", text);
	}

	@Test
	public void test_equals() {
		session.request.setAttribute("value", 3);
		renderer.render(page, session);
		String text = getPrettyContent(response);
		assertEquals(text, "equals", text);
	}

	@Test
	public void test_smaller_equals() {
		session.request.setAttribute("value", 0);
		renderer.render(page, session);
		String text = getPrettyContent(response);
		assertEquals(text, "smaller-equals", text);
	}

	@Test
	public void test_smaller() {
		session.request.setAttribute("value", 1);
		renderer.render(page, session);
		String text = getPrettyContent(response);
		assertEquals(text, "smaller", text);
	}

}