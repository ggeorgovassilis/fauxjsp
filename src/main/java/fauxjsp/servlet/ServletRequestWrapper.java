package fauxjsp.servlet;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

/**
 * Wrapper around a servlet request; mostly used to isolate attribute scopes
 * @author George Georgovassilis
 *
 */
public class ServletRequestWrapper extends javax.servlet.ServletRequestWrapper{

	public final static String OVERRIDEN_LOCALE="__fauxjsp_locale";
	
	protected Map<String, Object> attributes;
	
	public ServletRequestWrapper(ServletRequest request) {
		super(request);
	}

	@Override
	public void setAttribute(String name, Object o) {
		if (attributes==null)
			attributes=new HashMap<>();
		attributes.put(name, o);
	}

	public void overwriteAttribute(String name, Object o){
		removeAttribute(name);
		ServletRequest innerRequest = getRequest();
		if (innerRequest instanceof ServletRequestWrapper){
			((ServletRequestWrapper)innerRequest).overwriteAttribute(name, o);
		} else
		super.setAttribute(name, o);
	}
	
	@Override
	public void removeAttribute(String name) {
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
	
	//TODO: I know from experiments with "TestPerformance" that caching attribute names greatly speeds up the benchmark (x2)
	@Override
	public Enumeration<String> getAttributeNames() {
		if (attributes==null)
			return super.getAttributeNames();
		Enumeration<String> attributeNamesFromInnerRequest = getRequest().getAttributeNames();
		Set<String> attributeNames = new HashSet<>(attributes.keySet());
		while (attributeNamesFromInnerRequest.hasMoreElements())
			attributeNames.add(attributeNamesFromInnerRequest.nextElement());
		return Collections.enumeration(attributeNames);
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
