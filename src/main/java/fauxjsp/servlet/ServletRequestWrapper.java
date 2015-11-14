package fauxjsp.servlet;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
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

	public final static String OVERRIDEN_LOCALE="__fauxjsp_locale";
	
	protected Map<String, Object> attributes;
	protected Set<String> attributeNamesCache;
	
	public ServletRequestWrapper(ServletRequest request) {
		super(request);
		attributes = Utils.getCopyOfAttributes(request);
	}

	@Override
	public void setAttribute(String name, Object o) {
		attributeNamesCache = null;
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
		attributeNamesCache = null;
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
		if (attributeNamesCache==null){
			attributeNamesCache = new HashSet<String>(attributes.keySet());
			attributeNamesCache.addAll(Collections.list(super.getAttributeNames()));
		}
		return Collections.enumeration(attributeNamesCache);
	}
	
	@Override
	public Locale getLocale() {
		Locale locale = (Locale)getAttribute(OVERRIDEN_LOCALE);
		if (locale!=null)
			return locale;
		return super.getLocale();
	}
	
	public void setLocale(Locale locale){
		overwriteAttribute(OVERRIDEN_LOCALE, locale);
	}
	
}
