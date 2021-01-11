package fauxjsp.test.unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import fauxjsp.api.nodes.JspPage;

/**
 * Tests that double attributes on generated html/xml are preserved
 * @author George Georgovassilis
 *
 */

public class TestDoubleAttribute extends BaseTest{
	
	@Test
	public void jsp_double_attibute(){
		JspPage page = parser.parseJsp("WEB-INF/jsp/double_attribute.jsp");
		session.request.setAttribute("tagName", "someelement");
		session.request.setAttribute("condition", true);
		renderer.render(page, session);
		
		String text = getPrettyContent(response).replaceAll("\n", " ");
		assertEquals(text,"<someelement someattribute=\"somevalue1\" someattribute=\"somevalue2\" >sometext</someelement>", text);
	}

}