package fauxjsp.servlet;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

import fauxjsp.impl.Utils;

/**
 * Wrapper around a servlet request; mostly used to isolate attribute scopes
 * @author George Georgovassilis
 *
 */
public class ServletRequestWrapper extends javax.servlet.ServletRequestWrapper{

	protected Map<String, Object> attributes;
	
	public ServletRequestWrapper(ServletRequest request) {
		super(request);
		attributes = Utils.getCopyOfAttributes(request);
	}

	@Override
	public void setAttribute(String name, Object o) {
		attributes.put(name, o);
	}

	public void overwriteAttribute(String name, Object o){
		setAttribute(name, o);
		super.setAttribute(name, o);
		ServletRequest innerRequest = getRequest();
		if (innerRequest instanceof ServletRequestWrapper){
			((ServletRequestWrapper)innerRequest).overwriteAttribute(name, o);
		}
	}
	
	@Override
	public void removeAttribute(String name) {
		attributes.remove(name);
	}
	
	@Override
	public Object getAttribute(String name) {
		Object o = attributes.get(name);
		if (o==null)
			o = super.getAttribute(name);
		return o;
	}
	
	@Override
	public Enumeration<String> getAttributeNames() {
		Set<String> names = new HashSet<String>(attributes.keySet());
		names.addAll(Collections.list(super.getAttributeNames()));
		return Collections.enumeration(names);
	}
	
}
