package jspparser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import jspparser.utils.FileResolver;
import jspparser.utils.MockServletContext;

import org.junit.Before;

import fauxjsp.api.parser.JspParser;
import fauxjsp.api.parser.JspParserFactory;
import fauxjsp.api.parser.ResourceResolver;
import fauxjsp.api.renderer.ELEvaluation;
import fauxjsp.api.renderer.JspRenderer;
import fauxjsp.api.renderer.JspRendererFactory;
import fauxjsp.impl.parser.DefaultJspParserFactoryImpl;
import fauxjsp.impl.renderer.ELEvaluationImpl;
import fauxjsp.impl.renderer.ELFactoryServlet3Impl;
import fauxjsp.impl.renderer.JspRendererFactoryImpl;

/**
 * Base class for unit tests. Provides factories for parsers and renderers
 * @author George Georgovassilis
 *
 */
public abstract class BaseTest {

	protected JspParserFactory parserFactory;
	protected JspParser parser;
	protected JspRenderer renderer;
	protected ELEvaluation elEvaluation;
	protected JspRendererFactory rendererFactory;

	protected JspParser newParser(){
		return parser=parserFactory.create();
	}
	
	protected JspRenderer newRenderer(){
		return renderer = rendererFactory.create();
	}
	
	protected String text(ByteArrayOutputStream baos){
		try {
			return new String(baos.toByteArray(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Before
	public void setupBaseTest() {
		ResourceResolver location = new FileResolver(new File("examples"));
		parserFactory = new DefaultJspParserFactoryImpl(location);
		parser = newParser();
		rendererFactory = new JspRendererFactoryImpl();
		renderer = rendererFactory.create();
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
	}

}
