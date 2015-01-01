package fauxjsp.impl.parser;

import fauxjsp.api.parser.JspParser;
import fauxjsp.api.parser.JspParserFactory;
import fauxjsp.api.parser.ResourceResolver;
import fauxjsp.impl.simulatedtaglibs.JspBuiltinTaglibDoBody;
import fauxjsp.impl.simulatedtaglibs.JstlCoreTaglibChoose;
import fauxjsp.impl.simulatedtaglibs.JstlCoreTaglibForEach;
import fauxjsp.impl.simulatedtaglibs.JstlCoreTaglibIf;
import fauxjsp.impl.simulatedtaglibs.JstlCoreTaglibOut;
import fauxjsp.impl.simulatedtaglibs.JstlCoreTaglibWhen;

/**
 * 
 * @author George Georgovassilis
 */

public class DefaultJspParserFactoryImpl implements JspParserFactory {

	protected ResourceResolver location;

	protected void setup(JspParser parser) {
		parser.registerTaglibDefinition(
				"http://java.sun.com/jsp/jstl/core/forEach",
				new JstlCoreTaglibForEach());
		parser.registerTaglibDefinition(
				"http://java.sun.com/jsp/jstl/core/out",
				new JstlCoreTaglibOut());
		parser.registerTaglibDefinition(
				"http://java.sun.com/jsp/jstl/core/choose",
				new JstlCoreTaglibChoose());
		parser.registerTaglibDefinition(
				"http://java.sun.com/jsp/jstl/core/when",
				new JstlCoreTaglibWhen());
		parser.registerTaglibDefinition(
				"http://java.sun.com/jsp/jstl/core/otherwise",
				new JstlCoreTaglibChoose());
		parser.registerTaglibDefinition(
				"http://java.sun.com/jsp/jstl/core/if",
				new JstlCoreTaglibIf());
		parser.registerTaglibDefinition("jsp", "http://java.sun.com/jsp/doBody",
				new JspBuiltinTaglibDoBody());
		parser.registerTaglibDefinition("jsp", "http://java.sun.com/jsp",
				new JspBuiltinTaglibDoBody());
	}

	public DefaultJspParserFactoryImpl(ResourceResolver location) {
		this.location = location;
	}

	@Override
	public JspParser create() {
		JspParserImpl parser = new JspParserImpl(location, this);
		setup(parser);
		return parser;
	}

	@Override
	public JspParser create(JspParser parent) {
		JspParserImpl parser = new JspParserImpl(this, parent);
		setup(parser);
		return parser;
	}

}
