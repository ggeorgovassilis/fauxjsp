package fauxjsp.impl;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

import fauxjsp.api.nodes.NodeAttributeValue;
import fauxjsp.api.nodes.StringNodeAttributeValue;
import fauxjsp.api.renderer.RenderSession;

/**
 * Tools
 * 
 * @author George Georgovassilis
 *
 */
public class Utils {

	protected static Map<String, WeakReference<Class<?>>> cachedClasses = new HashMap<>();

	/**
	 * Get class of an object or return null for a null argument
	 * 
	 * @param o Object to get class of
	 * @return null-safe o.getClass()
	 */
	public static Class<?> getClassOf(Object o) {
		return o == null ? null : o.getClass();
	}

	/**
	 * Get file contents
	 * 
	 * @param f File to read
	 * @return file contents
	 */
	public static byte[] readFile(File f) {
		try (FileInputStream fis = new FileInputStream(f)) {
			byte[] b = new byte[(int) f.length()];
			fis.read(b);
			return b;
		} catch (Exception e) {
			throw softenException(e);
		}
	}

	/**
	 * Silently close a closeable, no matter what
	 * 
	 * @param c Object to close
	 */
	public static void close(Closeable c) {
		try {
			c.close();
		} catch (Exception e) {
		}
	}

	/**
	 * True if s is null or empty
	 * 
	 * @param s String to test
	 * @return true if empty or null
	 */
	public static boolean isEmpty(String s) {
		return s == null || s.isEmpty();
	}

	/**
	 * Evalute "expression" as an EL expression and return its result as an integer
	 * 
	 * @param expression Expression to evaluate
	 * @param session Session to pick variable values from
	 * @return evaluation result
	 */
	public static Integer evalToInt(String expression, RenderSession session) {
		String value = expression;
		if (expression.contains("${")) {
			Object result = session.elEvaluation.evaluate(expression, session);
			value = result + "";
		}
		return Integer.parseInt(value);
	}

	/**
	 * Replace all ocurrences of "what" in "sb" with "with". Modifies "sb" and
	 * returns the "sb" parameter
	 * 
	 * @param sb String builder to modify
	 * @param what Text to be replaced
	 * @param with Replacement
	 * @return sb
	 */
	public static StringBuilder replace(StringBuilder sb, String what, String with) {
		int index = sb.indexOf(what);
		if (index != -1) {
			sb.delete(index, index + what.length());
			sb.insert(index, with);
			// sb.replace(index, index + with.length() - 1, with);
		}
		return sb;
	}

	public static String unescapeHtml(String html) {
		return StringEscapeUtils.unescapeHtml(html);
	}

	public static String escapeHtml(String html) {
		return StringEscapeUtils.escapeHtml(html);
	}

	/**
	 * Makes a string out of a byte buffer. Convenience method that wraps the
	 * standard API {@link UnsupportedEncodingException}
	 * 
	 * @param buffer bytes to convert
	 * @param characterset character set for conversion
	 * @return converted string
	 */
	public static String string(byte[] buffer, String characterset) {
		try {
			return new String(buffer, characterset);
		} catch (UnsupportedEncodingException e) {
			throw softenException(e);
		}
	}

	/**
	 * Attempts to cast expression to c. If that's not possible, then this method
	 * will try a few obvious conversions if C is Boolean, Integer, Long, Double,
	 * Float, Byte, Short
	 * 
	 * @param value object to cast to c 
	 * @param c Class to cast to
	 * @return result
	 */
	public static Object cast(Object value, Class<?> c) {
		if (value == null)
			return null;
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
		if (c.isAssignableFrom(value.getClass()))
			return value;
		throw new ClassCastException("Can't cast " + value.getClass() + " to " + c);
	}

	/**
	 * Copy input stream to output stream. Doesn't do anything if either is null.
	 * 
	 * @param in Stream to copy
	 * @param out Stream to copy to
	 */
	public static void copy(InputStream in, OutputStream out) {
		if (in == null || out == null)
			return;
		byte[] buffer = new byte[1024];
		try {
			int length = 0;
			while (-1 != (length = in.read(buffer))) {
				out.write(buffer, 0, length);
			}
		} catch (IOException e) {
			throw softenException(e);
		}
	}

	/**
	 * Get attribute from map
	 * @param name Attribute name
	 * @param attributes Map of attributes
	 * @return Attribute value or null if not present
	 */
	public static String attr(String name, Map<String, NodeAttributeValue> attributes) {
		NodeAttributeValue attr = attributes.get(name);
		if (attr == null)
			return null;
		if (!(attr instanceof StringNodeAttributeValue))
			throw new RuntimeException("Attribute " + name + " isn't a string but a" + attr);
		return ((StringNodeAttributeValue) attr).getValue();
	}

	/**
	 * (Cached) lookup of classes. Implementation relies on weak references
	 * @param className Fully qualified (package.class) name of class
	 * @return Class or null, if not found
	 * @throws ClassNotFoundException If class not found in either the cache or the default classloader
	 */
	public static Class<?> getClassForName(String className) throws ClassNotFoundException {
		synchronized (cachedClasses) {
			WeakReference<Class<?>> ref = cachedClasses.get(className);
			if (ref == null) {
				Class<?> c = Class.forName(className);
				ref = new WeakReference<Class<?>>(c);
				cachedClasses.put(className, ref);
			}
			return ref.get();
		}
	}

	/**
	 * Returns e if its an instance of a {@link RuntimeException}, otherwise wraps e in one.
	 * @param e Exception
	 * @return a {@link RuntimeException}
	 */
	public static RuntimeException translate(Exception e) {
		if (e instanceof RuntimeException)
			return (RuntimeException) e;
		return new RuntimeException(e);
	}

	/**
	 * (Ab)uses type erasure to be able to throw checked exceptions without declaring them
	 * @param e Exception
	 * @param <T> Type after erasure is applied
	 * @return Nothing. Required for type erasure.
	 * @throws T The same exception without a type
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Exception> T checkednessRemover(Exception e) throws T {
		throw (T) e;
	}

	/**
	 * Removes type check on exception
	 * @param e Exception
	 * @return e
	 */
	public static RuntimeException softenException(Exception e) {
		return checkednessRemover(e);
	}

}
