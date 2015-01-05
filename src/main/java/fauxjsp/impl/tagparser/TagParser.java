package fauxjsp.impl.tagparser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.parser.CodeLocation;
import fauxjsp.impl.tagparser.TagParser.Tag.TagType;

/**
 * Utility which can parse tag invocations and jsp instructions. Note that
 * {@link TagParser.Tag#taglib} is not a fully parsed instance but a stub
 * which merely contains the namespace, tagname and attributes but lacks, i.e.
 * nested nodes.
 * @author George Georgovassilis
 */

public class TagParser {

	public static class Tag {
		public enum TagType {
			opening, openingAndClosing, closing
		};

		public TagType type;
		public JspTaglibInvocation taglib;
	}

	protected final static Pattern namespaceAndTagPattern = Pattern.compile(
			"<\\s*([_a-zA-Z0-9\\-]+):([a-zA-Z0-9\\-]+)\\s*(.*)\\/{0,1}>",
			Pattern.DOTALL | Pattern.MULTILINE);
	protected final static Pattern tagPattern = Pattern.compile(
			"<\\s*([_a-zA-Z0-9\\-]+)\\s*(.*)\\/{0,1}>", Pattern.DOTALL
					| Pattern.MULTILINE);
	
	protected final static Pattern colonPattern = Pattern.compile("\\:");

	protected String getTagSegment(String text) {
		boolean inQuotes = false;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c == '\"')
				inQuotes = !inQuotes;
			else if (c == '>' && !inQuotes)
				return text.substring(0, i + 1);
		}
		return null;
	}

	protected Map<String, String> getAttributes(String tagSegment) {
		Map<String, String> attributes = new HashMap<String, String>();
		boolean inQuotes = false;
		String name = "";
		String value = "";
		StringBuilder text = new StringBuilder();
		for (int i = 0; i < tagSegment.length(); i++) {
			char c = tagSegment.charAt(i);
			switch (c) {
			case '\r':
			case '\n':
			case '\t':
				continue;
			case '"':
				inQuotes = !inQuotes;
				if (!inQuotes) {
					value = text.toString();
					text.setLength(0);
					attributes.put(name, value);
					name = "";
					value = "";
				}
				continue;
			case '=':
				if (!inQuotes) {
					name = text.toString().trim();
					text.setLength(0);
					continue;
				}
			default:
				text.append(c);
			}
		}
		return attributes;
	}

	protected Tag parseOpeningTag(String text, CodeLocation location) {
		String segment = getTagSegment(text);
		if (segment == null)
			return null;
		Matcher m = namespaceAndTagPattern.matcher(segment);
		String namespace = "";
		String tag = "";
		String attributesSegment = "";
		if (m.matches()) {
			namespace = m.group(1);
			tag = m.group(2);
			attributesSegment = m.group(3);
		} else {
			m = tagPattern.matcher(segment);
			if (!m.matches())
				return null;
			tag = m.group(1);
			attributesSegment = m.group(2);
		}
		JspTaglibInvocation taglib = new JspTaglibInvocation(namespace, tag,
				location);

		Tag result = new Tag();
		result.taglib = taglib;
		result.type = Tag.TagType.opening;
		if (segment.endsWith("/>"))
			result.type = Tag.TagType.openingAndClosing;
		taglib.getArguments().putAll(getAttributes(attributesSegment));
		return result;
	}

	protected Tag parseClosingTag(String text, CodeLocation location) {
		int end = text.indexOf(">");
		if (end == -1)
			return null;
		String name = text.substring(2, end).trim();
		String[] parts = colonPattern.split(name);
		if (parts.length != 2)
			return null;
		JspTaglibInvocation inv = new JspTaglibInvocation(parts[0], parts[1],
				location);
		Tag tag = new Tag();
		tag.taglib = inv;
		tag.type = TagType.closing;
		return tag;
	}
	
	/**
	 * Fast plausibility check on a large string. If this returns true, there might be a tag declaration (but doesn't have to be).
	 * If it returns false, there definitely isn't a tag declaration.
	 * @param text Big string containing (somewhere) the tag.
	 * @param offset Look for tag only after "offset"
	 * @return
	 */
	public boolean mightContainDeclaration(String text, int offset){
		/* In an earlier version I replicated the entire logic of parse(text, location), hoping it would
		 * yield fewer false positives (which it did), but the overall performance dropped.
		 */
		return text.charAt(offset)=='<';
	}

	public Tag parse(String text, CodeLocation location) {
		if (text.length()<2)
			return null;
		char c1 = text.charAt(0);
		char c2 = text.charAt(1);
		if (c1=='<' && c2=='/')
			return parseClosingTag(text, location);
		int indexOfColon = text.indexOf(":");
		int indexOfAttributeOpening = text.indexOf('\"');
		if (indexOfAttributeOpening==-1)
			indexOfAttributeOpening = text.indexOf('\'');
		if (indexOfAttributeOpening==-1)
			indexOfAttributeOpening = text.length();
		if (c1=='<' &&  indexOfColon> 0
				&& indexOfColon < text.indexOf(">") && indexOfColon<indexOfAttributeOpening)
			return parseOpeningTag(text, location);
		return null;
	}

}
