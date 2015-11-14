package fauxjsp.impl.parser;

import fauxjsp.api.parser.JspParser;
import fauxjsp.api.parser.JspParserFactory;
import fauxjsp.api.parser.ResourceResolver;
import fauxjsp.impl.simulatedtaglibs.core.JspBuiltinTaglibAttribute;
import fauxjsp.impl.simulatedtaglibs.core.JspBuiltinTaglibBody;
import fauxjsp.impl.simulatedtaglibs.core.JspBuiltinTaglibDoBody;
import fauxjsp.impl.simulatedtaglibs.core.JstlCoreTaglibChoose;
import fauxjsp.impl.simulatedtaglibs.core.JstlCoreTaglibForEach;
import fauxjsp.impl.simulatedtaglibs.core.JstlCoreTaglibIf;
import fauxjsp.impl.simulatedtaglibs.core.JstlCoreTaglibOut;
import fauxjsp.impl.simulatedtaglibs.core.JstlCoreTaglibSet;
import fauxjsp.impl.simulatedtaglibs.core.JstlCoreTaglibWhen;
import fauxjsp.impl.simulatedtaglibs.fmt.JstlFmtFormatDate;
import fauxjsp.impl.simulatedtaglibs.fmt.JstlFmtMessage;
import fauxjsp.impl.simulatedtaglibs.fmt.JstlFmtSetBundle;

/**
 * Sets up {@link JspParserImpl} instances with emulated taglibs and functions.
 * @author George Georgovassilis
 */

public class DefaultJspParserFactoryImpl implements JspParserFactory {

	protected ResourceResolver location;
	protected boolean errorOnScriptlets = true;

	protected void setup(JspParserImpl parser) {
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
		parser.registerTaglibDefinition(
				"http://java.sun.com/jsp/jstl/core/set",
				new JstlCoreTaglibSet());
		parser.registerTaglibDefinition("jsp", "http://java.sun.com/jsp/doBody",
				new JspBuiltinTaglibDoBody());
		parser.registerTaglibDefinition("jsp", "http://java.sun.com/jsp/body",
				new JspBuiltinTaglibBody());
		parser.registerTaglibDefinition("jsp", "http://java.sun.com/jsp/attribute",
				new JspBuiltinTaglibAttribute());
		parser.registerTaglibDefinition("jsp", "http://java.sun.com/jsp",
				new JspBuiltinTaglibDoBody());
		
		parser.registerTaglibDefinition("http://java.sun.com/jsp/jstl/fmt/message", new JstlFmtMessage());
		parser.registerTaglibDefinition("http://java.sun.com/jsp/jstl/fmt/setBundle", new JstlFmtSetBundle());
		parser.registerTaglibDefinition("http://java.sun.com/jsp/jstl/fmt/formatDate", new JstlFmtFormatDate());
		parser.setErrorOnScriptlets(errorOnScriptlets);
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

	@Override
	public void setFailOnScriptletUsage(boolean value) {
		this.errorOnScriptlets = value;
	}

}
