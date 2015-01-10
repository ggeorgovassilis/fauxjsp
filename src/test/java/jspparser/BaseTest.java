package jspparser;

import java.io.File;

import org.junit.Before;

import fauxjsp.api.parser.ELEvaluation;
import fauxjsp.api.parser.JspParser;
import fauxjsp.api.parser.JspParserFactory;
import fauxjsp.api.parser.ResourceResolver;
import fauxjsp.api.renderer.JspRenderer;
import fauxjsp.impl.parser.DefaultJspParserFactoryImpl;
import fauxjsp.impl.parser.FileResolver;
import fauxjsp.impl.renderer.ELEvaluationImpl;
import fauxjsp.impl.renderer.ELFactoryServlet3Impl;
import fauxjsp.impl.renderer.JspRendererFactory;
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
	
	@Before
	public void setup() {
		ResourceResolver location = new FileResolver(new File("examples"));
		parserFactory = new DefaultJspParserFactoryImpl(location);
		parser = newParser();
		rendererFactory = new JspRendererFactoryImpl();
		renderer = rendererFactory.create();
		elEvaluation = new ELEvaluationImpl(new ELFactoryServlet3Impl());
	}

}
