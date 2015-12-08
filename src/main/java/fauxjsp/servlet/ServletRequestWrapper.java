package fauxjsp.servlet;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Wrapper around a servlet request; mostly used to isolate attribute scopes
 * @author George Georgovassilis
 *
 */
public class ServletRequestWrapper extends javax.servlet.ServletRequestWrapper{

	public final static String OVERRIDEN_LOCALE="__fauxjsp_locale";
	
	protected Map<String, Object> attributes;
	protected Set<String> attributeNamesCache=null;
	
	public ServletRequestWrapper(ServletRequest request) {
		super(request);
	}

	@Override
	public void setAttribute(String name, Object o) {
		if (attributeNamesCache!=null)
			attributeNamesCache.add(name);
		if (attributes==null)
			attributes=new HashMap<>();
		attributes.put(name, o);
	}

	public void overwriteAttribute(String name, Object o){
		removeAttribute(name);
		if (attributeNamesCache!=null)
			attributeNamesCache.add(name);
		ServletRequest innerRequest = getRequest();
		if (innerRequest instanceof ServletRequestWrapper){
			((ServletRequestWrapper)innerRequest).overwriteAttribute(name, o);
		} else
		super.setAttribute(name, o);
	}
	
	@Override
	public void removeAttribute(String name) {
		if (attributeNamesCache!=null)
			attributeNamesCache.remove(name);
		if (attributes!=null)
			attributes.remove(name);
	}
	
	@Override
	public Object getAttribute(String name) {
		Object o = null;
		if (attributes!=null)
			o=attributes.get(name);
		if (o==null)
			o = super.getAttribute(name);
		return o;
	}
	
	@Override
	public Enumeration<String> getAttributeNames() {
		if (attributes==null||attributes.isEmpty())
			return super.getAttributeNames();
		if (attributeNamesCache!=null)
			return Collections.enumeration(attributeNamesCache);
		Enumeration<String> attributeNamesFromInnerRequest = getRequest().getAttributeNames();
		attributeNamesCache = new HashSet<>(attributes.keySet());
		while (attributeNamesFromInnerRequest.hasMoreElements())
			attributeNamesCache.add(attributeNamesFromInnerRequest.nextElement());
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
	
	public HttpSession getSession(boolean create){
		if (getRequest() instanceof HttpServletRequest){
			return ((HttpServletRequest)getRequest()).getSession(create);
		}
		if (getRequest() instanceof ServletRequestWrapper){
			return ((ServletRequestWrapper)getRequest()).getSession(create);
		}
		throw new RuntimeException("This type of request doesn't have an http session");
	}
	
}
