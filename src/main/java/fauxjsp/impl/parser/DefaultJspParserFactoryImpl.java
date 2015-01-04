package fauxjsp.impl.parser;

import java.util.List;

import org.apache.log4j.Logger;

import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.parser.JspParser;
import fauxjsp.api.parser.JspParserFactory;
import fauxjsp.api.parser.ResourceResolver;
import fauxjsp.impl.simulatedtaglibs.core.JspBuiltinTaglibDoBody;
import fauxjsp.impl.simulatedtaglibs.core.JstlCoreTaglibChoose;
import fauxjsp.impl.simulatedtaglibs.core.JstlCoreTaglibForEach;
import fauxjsp.impl.simulatedtaglibs.core.JstlCoreTaglibIf;
import fauxjsp.impl.simulatedtaglibs.core.JstlCoreTaglibOut;
import fauxjsp.impl.simulatedtaglibs.core.JstlCoreTaglibWhen;
import fauxjsp.impl.simulatedtaglibs.fmt.JstlFmtMessage;
import fauxjsp.impl.simulatedtaglibs.fmt.JstlFmtSetBundle;
import fauxjsp.impl.taglibs.TaglibDiscovery;
import fauxjsp.impl.taglibs.TaglibDiscovery.DiscoveredTaglibs;

/**
 * 
 * @author George Georgovassilis
 */

public class DefaultJspParserFactoryImpl implements JspParserFactory {

	protected ResourceResolver location;
	protected Logger logger = Logger.getLogger(DefaultJspParserFactoryImpl.class);

	protected void setupFakeTaglibs(JspParser parser){
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
		
		parser.registerTaglibDefinition("http://java.sun.com/jsp/jstl/fmt/message", new JstlFmtMessage());
		parser.registerTaglibDefinition("http://java.sun.com/jsp/jstl/fmt/setBundle", new JstlFmtSetBundle());
	}
	
	protected void setup(JspParser parser) {
		TaglibDiscovery discovery = new TaglibDiscovery();
		List<DiscoveredTaglibs> taglibs = discovery.scanForTaglibs();
		for (DiscoveredTaglibs taglibAndUri:taglibs){
			for (TaglibDefinition def:taglibAndUri.taglibs){
				String uri = taglibAndUri.uri+"/"+def.getName();
				logger.debug("Registering taglib "+uri);
				parser.registerTaglibDefinition(uri, def);
			}
		}
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
