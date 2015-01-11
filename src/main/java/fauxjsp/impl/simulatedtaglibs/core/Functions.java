package fauxjsp.impl.simulatedtaglibs.core;

import java.util.Collection;

import fauxjsp.impl.Utils;

/**
 * Implements fn: functions
 * @author George Georgovassilis
 *
 */
public class Functions {

	public static boolean _startsWith(String what, String with){
		return what.startsWith(with);
	}
	
	public static boolean _contains(String text, String substring){
		return text.contains(substring);
	}

	public static boolean _containsIgnoreCase(String text, String substring){
		return text.toLowerCase().contains(substring.toLowerCase());
	}

	public static boolean _endsWith(String text, String substring){
		return text.endsWith(substring);
	}

	public static String _escapeXml(String text){
		return Utils.escapeXml(text);
	}

	public static int _indexOf(String text, String substring){
		return text.indexOf(substring);
	}
	
	public static String _join(String[] arr, String delimiter){
		StringBuilder sb = new StringBuilder();
		String prefix="";
		if (arr!=null)
		for (String s:arr){
			sb.append(prefix).append(s);
			prefix = delimiter;
		}
		return sb.toString();
	}
	
	public static int _length(Object object){
		if (object == null)
			return 0;
		if (object instanceof Collection){
			return ((Collection<?>)object).size();
		}
		if (object.getClass().isArray()){
			return ((Object[])object).length;
		}
		return object.toString().length();
	}
	
	public static String _replace(String text, String substring, String replaceWith){
		return text.replace(substring, replaceWith);
	}

	public static String[] _split(String text, String delimiter){
		return text.split(delimiter);
	}

	public static String _substring(String text, int start, int end){
		return text.substring(start, end);
	}
	
	public static String _toLowerCase(String text){
		return text.toLowerCase();
	}

	public static String _toUpperCase(String text){
		return text.toUpperCase();
	}
	
	public static String _trim(String text){
		return text.trim();
	}

}
