package fauxjsp.test.unittests;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.junit.Before;

import fauxjsp.api.parser.JspParser;
import fauxjsp.api.parser.JspParserFactory;
import fauxjsp.api.parser.ResourceResolver;
import fauxjsp.api.renderer.ELEvaluation;
import fauxjsp.api.renderer.JspRenderer;
import fauxjsp.api.renderer.JspRendererFactory;
import fauxjsp.api.renderer.RenderSession;
import fauxjsp.impl.Utils;
import fauxjsp.impl.parser.DefaultJspParserFactoryImpl;
import fauxjsp.impl.renderer.ELEvaluationImpl;
import fauxjsp.impl.renderer.ELFactoryServlet3Impl;
import fauxjsp.impl.renderer.JspRendererFactoryImpl;
import fauxjsp.servlet.JspServlet;
import fauxjsp.servlet.ServletRequestWrapper;
import fauxjsp.servlet.ServletResponseWrapper;
import fauxjsp.test.support.FileResolver;
import fauxjsp.test.support.MockHttpServletRequest;
import fauxjsp.test.support.MockHttpServletResponse;
import fauxjsp.test.support.MockServletContext;

/**
 * Base class for unit tests. Provides factories for parsers and renderers
 * 
 * @author George Georgovassilis
 *
 */
public abstract class BaseTest {

	protected JspParserFactory parserFactory;
	protected JspParser parser;
	protected JspRenderer renderer;
	protected ELEvaluation elEvaluation;
	protected JspRendererFactory rendererFactory;
	protected MockHttpServletRequest request;
	protected MockHttpServletResponse response;
	protected RenderSession session;


	protected String sanitize(String s) {
		String old = "";
		while (!old.equals(s)) {
			old = s;
			while (s.startsWith("\n"))
				s = s.substring(1);
			while (s.contains("\n\n"))
				s = s.replaceAll("\n\n", "\n");
			while (s.endsWith("\n"))
				s = s.substring(0, s.length() - 1);
			s = s.replaceAll("\r", "");
			s = s.replaceAll(" \n", "\n");
		}
		return s;
	}

	protected JspParser newParser() {
		return parser = parserFactory.create();
	}

	protected String text(ByteArrayOutputStream baos) {
		try {
			return new String(baos.toByteArray(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected String getContent(MockHttpServletResponse response){
		return text(response.getBaos());
	}
	
	protected String getPrettyContent(MockHttpServletResponse response){
		return sanitize(getContent(response));
	}

	protected String read(String resource) {
		try {
			InputStream in = getClass().getResourceAsStream(resource);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Utils.copy(in, baos);
			in.close();
			return new String(baos.toByteArray(), "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Before
	public void setupBaseTest() {
		ResourceResolver location = new FileResolver(new File("src/test/resources/webapp"));
		parserFactory = new DefaultJspParserFactoryImpl(location);
		parser = newParser();
		rendererFactory = new JspRendererFactoryImpl();
		renderer = rendererFactory.create();
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();

		ELFactoryServlet3Impl elFactory = new ELFactoryServlet3Impl();
		elFactory.configure(new ServletConfig() {

			@Override
			public String getServletName() {
				return null;
			}

			@Override
			public ServletContext getServletContext() {
				return new MockServletContext();
			}

			@Override
			public Enumeration<String> getInitParameterNames() {
				return null;
			}

			@Override
			public String getInitParameter(String name) {
				return null;
			}
		});
		elEvaluation = new ELEvaluationImpl(elFactory);
		session = new RenderSession();
		session.request = new ServletRequestWrapper(request);
		session.renderer = renderer;
		session.elEvaluation = elEvaluation;
		session.response = new ServletResponseWrapper(response, response.getBaos());
		session.servlet = new JspServlet();
	}

}
