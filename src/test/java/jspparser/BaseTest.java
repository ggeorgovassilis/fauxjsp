package jspparser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;

import jspparser.utils.FileResolver;

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
		elEvaluation = new ELEvaluationImpl(new ELFactoryServlet3Impl());
	}

}
