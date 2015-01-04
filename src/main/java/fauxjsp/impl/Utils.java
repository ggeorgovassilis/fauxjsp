package fauxjsp.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;

import fauxjsp.api.RenderSession;

public class Utils {

	protected static byte[] readContents(InputStream in) throws IOException{
		if (in == null)
			return null;
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int length;
		while (-1!=(length = in.read(buffer))){
			baos.write(buffer, 0 , length);
		}
		return baos.toByteArray();
	}
	
	public static byte[] readFile(File f) {
		try (FileInputStream fis = new FileInputStream(f)) {
			return readContents(fis);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] readClassPathResource(String name) {
		InputStream in = Utils.class.getResourceAsStream(name);
		if (in == null)
			in = ClassLoader.getSystemResourceAsStream(name);
		if (in == null)
			return null;
		try (InputStream in2=in) {
			return readContents(in2);
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

}
