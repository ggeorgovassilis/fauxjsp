package fauxjsp.impl.simulatedtaglibs.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.jsp.jstl.core.LoopTagStatus;

import fauxjsp.api.RenderSession;
import fauxjsp.api.nodes.JspNode;
import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.renderer.JspRenderException;
import fauxjsp.impl.Utils;
import fauxjsp.impl.renderer.ForEachIndex;

/**
 * Implementation of c:foreach
 * @author George Georgovassilis
 *
 */

public class JstlCoreTaglibForEach extends TaglibDefinition{

	protected void runForEachLoop(RenderSession session,
			JspTaglibInvocation invocation) {
		String itemsExpression = getAttribute("items", invocation);
		String varName = getAttribute("var", invocation);
		String sBegin = getAttribute("begin", invocation);
		String sEnd = getAttribute("end", invocation);
		String sStep = getAttribute("step", invocation);
		String varStatus = getAttribute("varStatus", invocation);
		int begin = 0;
		int end = 0;
		int step = 1;
		
		//TODO: I really should figure out how variable scoping works and whether variables need to be saved and restored
		//Map<String, Object> oldAttributeValues = Utils.saveAttributes(session.request);

		Object rawItems = evaluate(itemsExpression,
				session);
		if (rawItems == null)
			throw new JspRenderException(itemsExpression + " is null", invocation);
		if (!(rawItems instanceof Collection))
			throw new JspRenderException(
					"items attribute is not a collection but " + rawItems, invocation);
		@SuppressWarnings({ "rawtypes", "unchecked" })
		List<?> items = new ArrayList((Collection) rawItems);
		
		
		if (!Utils.isEmpty(sBegin))
			begin = Utils.evalToInt(sBegin, session);
		if (!Utils.isEmpty(sEnd))
			end = Utils.evalToInt(sEnd, session)+1;
		else end = items.size();
		if (!Utils.isEmpty(sStep))
			step = Utils.evalToInt(sStep, session);
		
		for (int i=begin,count=1;i<end;i=i+step,count++) {
			Object item = items.get(i);
			session.request.setAttribute(varName, item);
			if (!Utils.isEmpty(varStatus)){
				Integer _begin = Utils.isEmpty(sBegin)?null:begin;
				Integer _end = Utils.isEmpty(sEnd)?null:end;
				Integer _step = Utils.isEmpty(sStep)?null:step;
				boolean isFirst = count == 1;
				boolean isLast = i+step>=end;
				LoopTagStatus lts = new ForEachIndex(item, i, count, _begin, _end, _step, isFirst, isLast);
				session.request.setAttribute(varStatus, lts);
			}
			for (JspNode child : invocation.getChildren())
				session.renderer.render(child, session);
		}
//		Utils.restoreAttributes(session.request, oldAttributeValues);
	}

	@Override
	public void render(RenderSession session, JspTaglibInvocation invocation) {
		if (invocation.getTaglib().equals("forEach")) {
			runForEachLoop(session, invocation);
		} else 
			throw new JspRenderException(invocation, new RuntimeException("This isn't a forEach taglib"));
	}
	
	public JstlCoreTaglibForEach(){
		super("forEach");
		declareAttribute("items", List.class.getName(), true, true);
		declareAttribute("var", Object.class.getName(), true, false);
		declareAttribute("begin", Number.class.getName(), true, false);
		declareAttribute("end", Number.class.getName(), true, false);
		declareAttribute("step", Number.class.getName(), true, false);
		declareAttribute("varStatus", String.class.getName(), false, false);
	}

}
