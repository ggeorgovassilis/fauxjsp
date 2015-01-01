package jspparser;

import java.io.File;

import org.junit.Before;

import fauxjsp.api.parser.ELEvaluation;
import fauxjsp.api.parser.JspParser;
import fauxjsp.api.parser.JspParserFactory;
import fauxjsp.api.parser.ResourceResolver;
import fauxjsp.api.renderer.JspRenderer;
import fauxjsp.impl.parser.DefaultJspParserFactoryImpl;
import fauxjsp.impl.parser.FileLocation;
import fauxjsp.impl.renderer.ELEvaluationImpl;
import fauxjsp.impl.renderer.ELFactoryServlet3Impl;
import fauxjsp.impl.renderer.JspRendererFactory;
import fauxjsp.impl.renderer.JspRendererFactoryImpl;

public abstract class BaseTest {

	protected JspParserFactory parserFactory;
	protected JspParser parser;
	protected JspRenderer renderer;
	protected ELEvaluation elEvaluation;
	protected JspRendererFactory jspRendererFactory;

	@Before
	public void setup() {
		ResourceResolver location = new FileLocation(new File("examples"));
		parserFactory = new DefaultJspParserFactoryImpl(location);
		parser = parserFactory.create();
		jspRendererFactory = new JspRendererFactoryImpl();
		renderer = jspRendererFactory.newInstance();
		elEvaluation = new ELEvaluationImpl(new ELFactoryServlet3Impl());
	}

}
