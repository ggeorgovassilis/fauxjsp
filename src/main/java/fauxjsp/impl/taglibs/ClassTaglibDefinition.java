package fauxjsp.impl.taglibs;

import javax.servlet.jsp.tagext.Tag;

import fauxjsp.api.RenderSession;
import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.impl.PageContextImpl;

public class ClassTaglibDefinition extends TaglibDefinition {

	protected Class taglibClass;

	public ClassTaglibDefinition(String name, Class taglibClass) {
		this.name = name;
		this.taglibClass = taglibClass;
	}

	public Class getTaglibClass() {
		return taglibClass;
	}

	@Override
	public void render(RenderSession session, JspTaglibInvocation invocation) {
		try {
			Tag tagInstance = (Tag) taglibClass.newInstance();
			PageContextImpl pc = new PageContextImpl();
			pc.initialize(null, session.request, session.response, null, false, 1024, true);
			tagInstance.setPageContext(pc);
			//TODO: there is soooo much missing here...
			tagInstance.doStartTag();
			tagInstance.doEndTag();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
