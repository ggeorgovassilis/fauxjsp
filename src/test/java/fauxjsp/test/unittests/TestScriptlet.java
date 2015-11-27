package fauxjsp.test.unittests;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import fauxjsp.api.nodes.JspPage;
import fauxjsp.api.renderer.RenderSession;
import fauxjsp.servlet.ServletRequestWrapper;
import fauxjsp.servlet.ServletResponseWrapper;
import fauxjsp.test.support.MockHttpServletRequest;
import fauxjsp.test.support.MockHttpServletResponse;
import fauxjsp.test.webapp.dto.News;

/**
 * Test scriptlets
 * @author George Georgovassilis
 *
 */
public class TestScriptlet extends BaseTest{

	@Test
	public void testScriptlets(){
		final String expected = "N0,Headline 0,DESCRIPTION 0,true\nN1,Headline 1,DESCRIPTION 1,false\nN2,Headline 2,DESCRIPTION 2,true";
		JspPage page = parser.parse("WEB-INF/jsp/scriptlet.jsp");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		ByteArrayOutputStream baos = response.getBaos();
		RenderSession session = new RenderSession();
		session.request = new ServletRequestWrapper(request);
		List<News> news = new ArrayList<>();
		for (int i=0;i<3;i++)
			news.add(new News("N"+i, "Headline "+i, "Description "+i, "Fulltext "+i, i%2==0));
		session.request.setAttribute("listOfNews", news);
		
		session.renderer = renderer;
		session.elEvaluation = elEvaluation;
		session.response = new ServletResponseWrapper(response, response.getBaos());


		renderer.render(page, session);
		String text = sanitize(text(baos));
		assertEquals(expected, text);

	}
}
