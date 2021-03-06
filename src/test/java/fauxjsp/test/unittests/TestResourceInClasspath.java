package fauxjsp.test.unittests;

import org.junit.Test;

import fauxjsp.api.nodes.JspPage;
import fauxjsp.api.parser.ResourceResolver;
import fauxjsp.servlet.ClasspathResourceResolver;
import fauxjsp.servlet.CompositeResourceResolver;

import static org.junit.Assert.*;

/**
 * Tests that tagfiles residing in the classpath are resolved
 * 
 * @author george georgovassilis
 *
 */
public class TestResourceInClasspath extends BaseTest {
	
	@Override
	protected ResourceResolver getResourceResolver() {
		CompositeResourceResolver crr = new CompositeResourceResolver();
		crr.register(new ClasspathResourceResolver());
		crr.register(super.getResourceResolver());
		return crr;
	}

	@Test
	public void test_tagfile_in_classpath() {
		request.setAttribute("value", "testvalue");
		JspPage page = parser.parseJsp("WEB-INF/jsp/tagfile_in_classpath.jsp");
		renderer.render(page, session);
		String text = getPrettyContent(response);
		assertEquals("<c>testvalue</c>",text);
	}
	
	@Test
	public void test_jsp_in_classpath() {
		request.setAttribute("value", "testvalue");
		JspPage page = parser.parseJsp("classpath:/webapp/WEB-INF/jsp/tagfile_in_classpath.jsp");
		renderer.render(page, session);
		String text = getPrettyContent(response);
		assertEquals("<c>testvalue</c>",text);
		
	}

}
