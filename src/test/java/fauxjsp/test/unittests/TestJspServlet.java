package fauxjsp.test.unittests;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.spi.RendererSupport;
import org.junit.Test;

import fauxjsp.api.nodes.JspNode;
import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.parser.CodeLocation;
import fauxjsp.api.parser.JspParsingException;
import fauxjsp.api.renderer.JspRenderException;
import fauxjsp.api.renderer.JspRenderer;
import fauxjsp.api.renderer.RenderSession;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests a few things other tests didn't catch (mostly error handling)
 * 
 * @author george georgovassilis
 *
 */
public class TestJspServlet extends BaseTest {

	@Test
	public void testMissingContentType() throws Exception {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		ServletConfig config = mock(ServletConfig.class);
		ServletContext context = mock(ServletContext.class);
		when(response.getContentType()).thenReturn(null);
		when(response.getCharacterEncoding()).thenReturn(null);
		when(config.getServletContext()).thenReturn(context);
		session.servlet.init(config);
		try {
			session.servlet.service(request, response);
		} catch (ServletException e) {
			assertTrue(e.getMessage(), e.getMessage().contains("Error while parsing"));
		}
		verify(response).setContentType("text/html;charset=UTF-8");
		verify(response).setCharacterEncoding("UTF-8");
	}

}
