package fauxjsp.test.unittests;

import fauxjsp.api.nodes.JspNodeWithChildren;
import fauxjsp.api.nodes.JspPage;
import static org.junit.Assert.*;

import org.junit.Test;
/**
 * Tests data model
 * 
 * @author George Georgovassilis
 *
 */

public class TestNodes extends BaseTest{

	@Test
	public void test_JspNodeWithChildren_toString(){
		String expected="<c:forEach end=${end} begin=1 items=${listOfNews} var=news><c:out value=${news.id}></c:out></c:forEach>";
		JspPage page = parser.parse("WEB-INF/jsp/foreach.jsp");
		JspNodeWithChildren n = (JspNodeWithChildren)page.getChildren().get(4);
		assertEquals("c:forEach", n.getName());
		assertEquals(expected, n.toString());
	}
}
