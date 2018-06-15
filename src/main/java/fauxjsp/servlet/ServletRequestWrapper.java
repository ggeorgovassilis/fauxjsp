package fauxjsp.servlet;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 * Wrapper around a servlet request; mostly used to isolate attribute scopes
 * 
 * @author George Georgovassilis
 *
 */
public class ServletRequestWrapper extends HttpServletRequestWrapper implements HttpServletRequest{

	public final static String OVERRIDEN_LOCALE = "__fauxjsp_locale";

	protected final Map<String, Object> attributes;

	public ServletRequestWrapper(HttpServletRequest request) {
		super(request);
		attributes = new HashMap<>(10);
		for (Enumeration<String> e = request.getAttributeNames(); e.hasMoreElements();) {
			String attribute = e.nextElement();
			attributes.put(attribute, request.getAttribute(attribute));
		}
	}

	public ServletRequestWrapper(ServletRequestWrapper request) {
		super(request);
		attributes=new HashMap<String,Object>(request.getAttributes());
	}

	protected Map<String, Object> getAttributes(){
		return attributes;
	}

	@Override
	public void setAttribute(String name, Object o) {
		attributes.put(name, o);
	}

	public void overwriteAttribute(String name, Object o) {
		setAttribute(name, o);
		if (super.getRequest() instanceof ServletRequestWrapper)
			((ServletRequestWrapper) super.getRequest()).overwriteAttribute(name, o);
		else
			super.setAttribute(name, o);
	}

	@Override
	public void removeAttribute(String name) {
		attributes.remove(name);
	}

	@Override
	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return Collections.enumeration(attributes.keySet());
	}

	@Override
	public Locale getLocale() {
		Locale locale = (Locale) getAttribute(OVERRIDEN_LOCALE);
		if (locale != null)
			return locale;
		return super.getLocale();
	}

	public void setLocale(Locale locale) {
		overwriteAttribute(OVERRIDEN_LOCALE, locale);
	}

	@Override
	public HttpSession getSession(boolean create) {
		return ((HttpServletRequest)getRequest()).getSession(create);
	}

}
