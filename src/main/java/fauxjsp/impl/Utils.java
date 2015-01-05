package fauxjsp.impl;

import java.io.File;
import java.io.FileInputStream;
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
		clearAttributes(request);
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
	
	public static StringBuilder replace(StringBuilder sb, String what, String with){
		int index = sb.indexOf(what);
		if (index!=-1){
			sb.replace(index, index+with.length()-1, with);
		}
		return sb;
	}
	
	public static String unescapeHtml(String html){
		return StringEscapeUtils.unescapeHtml(html);
	}

}
