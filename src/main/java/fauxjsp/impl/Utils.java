package fauxjsp.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.commons.lang.StringEscapeUtils;

import fauxjsp.api.RenderSession;

public class Utils {

	public static byte[] readFile(File f) {
		try (FileInputStream fis = new FileInputStream(f)) {
			byte[] b = new byte[(int) f.length()];
			fis.read(b);
			return b;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isEmpty(String s) {
		return s == null || s.isEmpty();
	}

	public static Map<String, Object> saveAttributes(ServletRequest request) {
		Map<String, Object> attributes = new HashMap<String, Object>();
		for (String attr : Collections.list(request.getAttributeNames())) {
			attributes.put(attr, request.getAttribute(attr));
		}
		return attributes;
	}

	public static void clearAttributes(ServletRequest request) {
		for (String attr : Collections.list(request.getAttributeNames())) {
			request.removeAttribute(attr);
		}
	}

	public static void restoreAttributes(ServletRequest request,
			Map<String, Object> attributes) {
		//clearAttributes(request);
		for (String attr : attributes.keySet())
			request.setAttribute(attr, attributes.get(attr));
	}

	public static Long toLong(String value, long defaultValue) {
		if (isEmpty(value))
			return defaultValue;
		return Long.parseLong(value);
	}

	public static Integer toInt(String value, int defaultValue) {
		if (isEmpty(value))
			return defaultValue;
		return Integer.parseInt(value);
	}

	public static Integer evalToInt(String expression, RenderSession session) {
		String value = expression;
		if (expression.contains("${")) {
			Object result = session.elEvaluation.evaluate(expression, session);
			value = result + "";
		}
		return Integer.parseInt(value);
	}

	public static StringBuilder replace(StringBuilder sb, String what,
			String with) {
		int index = sb.indexOf(what);
		if (index != -1) {
			sb.delete(index, index+what.length());
			sb.insert(index, with);
//			sb.replace(index, index + with.length() - 1, with);
		}
		return sb;
	}

	public static String unescapeHtml(String html) {
		return StringEscapeUtils.unescapeHtml(html);
	}

	public static String escapeHtml(String html) {
		return StringEscapeUtils.escapeHtml(html);
	}
	
	public static String escapeXml(String xml){
		return StringEscapeUtils.escapeXml(xml);
	}

	public static String string(byte[] buffer, String characterset) {
		try {
			return new String(buffer, characterset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Attempts to cast value to c. If that's not possible, then this method
	 * will try a few obvious conversions if C is Boolean, Integer, Long, Double, Float, Byte, Short
	 * 
	 * @param value
	 * @param c
	 * @return
	 */
	public static Object cast(Object value, Class<?> c){
		if (value == null)
			return null;
		if (c.isAssignableFrom(value.getClass()))
			return value;
		if (c.equals(Boolean.class))
			return Boolean.parseBoolean(value.toString());
		if (c.equals(Byte.class))
			return Byte.parseByte(value.toString());
		if (c.equals(Short.class))
			return Short.parseShort(value.toString());
		if (c.equals(Integer.class))
			return Integer.parseInt(value.toString());
		if (c.equals(Long.class))
			return Long.parseLong(value.toString());
		if (c.equals(Float.class))
			return Float.parseFloat(value.toString());
		if (c.equals(Double.class))
			return Double.parseDouble(value.toString());
		if (c.equals(String.class))
			return value.toString();
		throw new ClassCastException("Can't cast "+value.getClass()+" to "+c);
	}
}
