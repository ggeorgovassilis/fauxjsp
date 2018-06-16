package fauxjsp.impl.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import fauxjsp.api.logging.Logger;
import fauxjsp.api.nodes.BodyNodeAttributeValue;
import fauxjsp.api.nodes.JspInstruction;
import fauxjsp.api.nodes.JspNode;
import fauxjsp.api.nodes.JspNodeWithChildren;
import fauxjsp.api.nodes.JspPage;
import fauxjsp.api.nodes.JspScriptlet;
import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.JspText;
import fauxjsp.api.nodes.NodeAttributeValue;
import fauxjsp.api.nodes.StringNodeAttributeValue;
import fauxjsp.api.nodes.TagfileDefinition;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.parser.CodeLocation;
import fauxjsp.api.parser.JspParser;
import fauxjsp.api.parser.JspParserFactory;
import fauxjsp.api.parser.JspParsingException;
import fauxjsp.api.parser.JspResourceNotFoundException;
import fauxjsp.api.parser.ResourceResolver;
import fauxjsp.impl.Utils;
import fauxjsp.impl.logging.Logging;
import fauxjsp.impl.tagparser.TagParser;

/**
 * Implementation of {@link JspParser}
 * 
 * @author George Georgovassilis
 *
 */
public class JspParserImpl implements JspParser {

	// TODO: our impl doesn't work with < or > in attributes, e.g. <when
	// test="${a<b}"> breaks. Fix that.
	protected final String OPEN_INSTRUCTION = "<%@";
	protected final String OPEN_COMMENT = "<%--";
	protected final String CLOSE_COMMENT = "--%>";
	protected final String OPEN_SCRIPTLET = "<%";
	protected final String OPEN_SCRIPTLET_RETURN_VALUE = "<%=";
	protected final String CLOSE_INSTRUCTION = "%>";

	protected String pagePath;
	protected String jsp;
	protected int index;
	protected StringBuilder buffer = new StringBuilder();
	protected Map<String, String> taglibNamespaces = new HashMap<String, String>();
	protected TaglibDefinitionCache taglibDefinitions = new TaglibDefinitionCache();
	protected List<JspNodeWithChildren> nodeStack = new ArrayList<JspNodeWithChildren>();
	protected ResourceResolver location;
	protected Logger logger = Logging.getLogger(JspParserImpl.class);
	protected JspParser parentParser;
	protected JspParserFactory parserFactory;
	protected TagParser tagParser = new TagParser();
	protected int line = 0;
	protected int column = 0;

	// TODO: speed: keep a running pointer instead of recomputing the location
	// every
	// time. this is important because this function is called every time a node
	// is constructed
	protected CodeLocation getCurrentLocation() {
		return new CodeLocation(pagePath, line + 1, column + 1);
	}

	protected void parsingError(String message) throws JspParsingException {
		throw new JspParsingException(message, getCurrentLocation());
	}

	public JspParserImpl(ResourceResolver location, JspParserFactory factory) {
		this.parserFactory = factory;
		this.location = location;
	}

	// TODO: speed: construct a parsing tree and check ("remains") based on
	// different
	// groups of the same substring length
	public JspParserImpl(JspParserFactory factory, JspParser parent) {
		this.parserFactory = factory;
		this.parentParser = parent;
		this.location = parent.getLocationResolver();
		this.taglibDefinitions = parent.getTaglibDefinitions();
	}

	protected void advanceCodeLocation(int length) {
		int end = index + length;
		for (int i = index; i < end; i++) {
			char c = jsp.charAt(i);
			switch (c) {
			case '\n':
				line++;
				column = 0;
				break;
			default:
				column++;
			}
		}
		index += length;
	}

	protected void advance(int length) {
		if (length >= 0)
			advanceCodeLocation(length);
		else
			throw new IllegalArgumentException("Length is " + length);
	}

	protected void advance(String str) {
		advance(str.length());
	}

	protected String remaining() {
		return jsp.substring(index);
	}

	protected void pushNode(JspNodeWithChildren node) {
		nodeStack.add(node);
	}

	protected JspNodeWithChildren getCurrentNode() {
		if (nodeStack.isEmpty())
			return null;
		return nodeStack.get(nodeStack.size() - 1);
	}

	protected JspTaglibInvocation pullNode(String fullyQualifiedTaglib) {
		if (nodeStack.isEmpty())
			parsingError("Found closing " + fullyQualifiedTaglib + " but don't remember opening any");
		JspNode node = getCurrentNode();
		if (!(node instanceof JspTaglibInvocation))
			parsingError("Trying to close " + fullyQualifiedTaglib + " but the current open node is not a taglib but "
					+ node);
		JspTaglibInvocation latestOpenedTaglib = (JspTaglibInvocation) node;
		if (!latestOpenedTaglib.getName().equals(fullyQualifiedTaglib))
			parsingError(
					"Found closing " + fullyQualifiedTaglib + " but expected closing " + latestOpenedTaglib.getName());
		nodeStack.remove(nodeStack.size() - 1);
		return latestOpenedTaglib;
	}

	protected void advanceToNext(String what) {
		int offset = jsp.indexOf(what, index);
		if (offset == -1)
			parsingError("Expected to find '" + what + "' after offset " + offset);
		advance(offset - index);
	}

	protected void advanceAfterNext(String what) {
		advanceToNext(what);
		advance(what);
	}

	protected String substring(int length) {
		return jsp.substring(index, index + length);
	}

	protected int nextIndexOf(String what) {
		return jsp.indexOf(what, index);
	}

	protected boolean isNext(String what) {
		return jsp.regionMatches(index, what, 0, what.length());
	}

	protected boolean toBool(String s, boolean defaultValue) {
		try {
			return Boolean.parseBoolean(s);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	protected TagfileDefinition loadTagfileDefinition(String fullQualifiedName, String path, String fullPath) {
		JspParser tagfileParser = parserFactory.create(this);
		TagfileDefinition tagfileDefinition = new TagfileDefinition(fullQualifiedName);
		// need to add a marker into cache that the tagfile is known,
		// otherwise a recursive call
		// will lead to a stack overflow
		taglibDefinitions.setDefinition(fullPath, tagfileDefinition);

		JspPage page = tagfileParser.parseJspFragment(fullPath);
		tagfileDefinition.setBody(page);
		for (JspNode child : page.getChildren())
			if (child instanceof JspInstruction) {
				JspInstruction instruction = (JspInstruction) child;
				if (instruction.getName().equals("attribute")) {
					String attributeName = Utils.attr("name", instruction.getAttributes());
					String sAttributeRequired = Utils.attr("required", instruction.getAttributes());
					String sType = Utils.attr("type", instruction.getAttributes());
					if (Utils.isEmpty(sType))
						sType = String.class.getCanonicalName();
					String sRTValue = Utils.attr("rtexprvalue", instruction.getAttributes());

					TaglibDefinition.AttributeDefinition attributeDefinition = new TaglibDefinition.AttributeDefinition(
							attributeName, sType, toBool(sRTValue, true), toBool(sAttributeRequired, true));
					tagfileDefinition.getAttributes().put(attributeName, attributeDefinition);
				}
			}
		return tagfileDefinition;
	}

	protected TaglibDefinition getOrLoadDefinition(JspTaglibInvocation invocation) {
		String fullQualifiedName = invocation.getName();
		String namespace = invocation.getNamespace();
		String path = taglibNamespaces.get(namespace);
		if (path == null)
			parsingError("Unknown taglib namespace '" + namespace + "'");
		String fullPath = null;
		if (!path.startsWith("http:")) {
			fullPath = path + "/" + invocation.getTaglib() + ".tag";
		} else
			fullPath = path + "/" + invocation.getTaglib();
		TaglibDefinition definition = taglibDefinitions.getDefinition(fullPath);
		if (definition == null) {
			if (path.startsWith("http:"))
				parsingError("Unknown taglib '" + fullPath
						+ "'. Either you misspelled the uri or you need to write an emulation for this taglib.");
			definition = loadTagfileDefinition(fullQualifiedName, path, fullPath);
		}
		return definition;
	}

	protected JspInstruction processInstruction() {
		if (!isNext(OPEN_INSTRUCTION))
			parsingError("Expected open instruction " + OPEN_INSTRUCTION);
		int closingIndex = nextIndexOf(CLOSE_INSTRUCTION);
		if (closingIndex <= index)
			parsingError("Expected closing instruction " + CLOSE_INSTRUCTION);

		StringBuilder sInstruction = new StringBuilder(substring(CLOSE_INSTRUCTION.length() + closingIndex - index));

		int indexOfAt = sInstruction.indexOf("@");
		sInstruction.deleteCharAt(indexOfAt);

		int indexOfPercent = sInstruction.indexOf("%");
		sInstruction.deleteCharAt(indexOfPercent);

		int lastIndexOfPercent = sInstruction.lastIndexOf("%");
		sInstruction.deleteCharAt(lastIndexOfPercent);

		/*
		 * we're reusing the tagparser for parsing instructions. But the
		 * tagparser requires tags to be in the form <namespace:tagname ...>, so
		 * we'll "invent" the namespace "__" to keep it happy.
		 */
		sInstruction = Utils.replace(sInstruction, "< ", "<");
		sInstruction = Utils.replace(sInstruction, "<", "<__:");
		TagParser.Tag tag = tagParser.parse(sInstruction.toString(), getCurrentLocation());
		if (tag == null)
			parsingError("Can't parse instruction from " + sInstruction);
		JspInstruction instruction = new JspInstruction(tag.taglib.getTaglib(), getCurrentLocation());
		instruction.getAttributes().putAll(tag.taglib.getAttributes());
		advanceAfterNext(CLOSE_INSTRUCTION);
		return instruction;
	}

	protected JspScriptlet processScriptlet() {
		if (!isNext(OPEN_SCRIPTLET))
			parsingError("Expected scriptlet open tag <% or <%=");
		int closingIndex = nextIndexOf(CLOSE_INSTRUCTION);
		if (closingIndex <= index)
			parsingError("Expected closing instruction " + CLOSE_INSTRUCTION);
		String sourceCode = jsp.substring(index + OPEN_INSTRUCTION.length(), closingIndex);
		boolean returnsStatement = isNext(OPEN_SCRIPTLET_RETURN_VALUE);
		JspScriptlet node = new JspScriptlet(getCurrentLocation(), sourceCode, returnsStatement);
		advanceAfterNext(CLOSE_INSTRUCTION);
		return node;
	}

	protected void flushText() {
		if (buffer.length() > 0) {
			String content = buffer.toString();
			JspText text = new JspText(content, getCurrentLocation());
			JspNodeWithChildren currentNode = getCurrentNode();
			currentNode.getChildren().add(text);
			buffer.setLength(0);
		}
	}

	protected void maybeRegisterTaglib(JspInstruction instruction) {
		if (instruction.getName().equals("taglib")) {
			String namespace = Utils.attr("prefix", instruction.getAttributes());
			if (Utils.isEmpty(namespace))
				parsingError("Missing prefix attribute on taglib");
			if (taglibNamespaces.containsKey(namespace))
				parsingError("Taglib prefix '" + namespace + "' already used.");
			String path = null;
			if (instruction.getAttributes().containsKey("tagdir")) {
				path = Utils.attr("tagdir", instruction.getAttributes());
			} else if (instruction.getAttributes().containsKey("uri")) {
				path = Utils.attr("uri", instruction.getAttributes());
			} else
				parsingError("Taglib declaration requires either a uri or tagdir attribute");
			taglibNamespaces.put(namespace, path);
		}
	}

	protected void convertJspAttributeTagToAttributeOnParent(JspTaglibInvocation jspAttribute,
			JspTaglibInvocation parent) {
		String attributeName = Utils.attr("name", jspAttribute.getAttributes());
		BodyNodeAttributeValue body = new BodyNodeAttributeValue(jspAttribute);
		parent.getAttributes().put(attributeName, body);
		parent.getChildren().remove(jspAttribute);
	}

	protected void processCloseTaglib(JspTaglibInvocation taglib) {
		if (nodeStack.isEmpty())
			parsingError("Tag " + taglib + " closing at offset " + index + " without prior opening");
		flushText();
		JspTaglibInvocation jspTaglib = pullNode(taglib.getName());
		JspNodeWithChildren currentNode = getCurrentNode();
		currentNode.getChildren().add(jspTaglib);
		if (jspTaglib.getName().equals("jsp:attribute")) {
			JspNodeWithChildren parent = nodeStack.get(nodeStack.size() - 1);
			if (!(parent instanceof JspTaglibInvocation))
				throw new JspParsingException(taglib.getName() + " must be direct child of a taglib invocation, but "
						+ parent.getName() + " isn't one.", getCurrentLocation());
			convertJspAttributeTagToAttributeOnParent(jspTaglib, (JspTaglibInvocation) parent);
		}
	}

	protected void processCloseTaglibAndAdvance(JspTaglibInvocation taglib) {
		processCloseTaglib(taglib);
		advance(taglib.getName());
		advanceAfterNext(">");
	}

	protected void processOpenCloseTaglib(JspTaglibInvocation invocation) {
		processOpenTaglib(invocation);
		invocation = pullNode(invocation.getName());
		if (nodeStack.isEmpty())
			throw new IllegalArgumentException(
					"Tag " + invocation + " closing at offset " + index + " without prior opening");
		flushText();
		JspNodeWithChildren currentNode = getCurrentNode();
		currentNode.getChildren().add(invocation);
	}

	protected void processOpenTaglib(JspTaglibInvocation invocation) {
		flushText();
		invocation.setDefinition(getOrLoadDefinition(invocation));
		advanceAfterNext(">");
		// if attributes contain a > then we need to advance even further
		for (NodeAttributeValue attr : invocation.getAttributes().values())
			if (attr instanceof StringNodeAttributeValue) {
				StringNodeAttributeValue snav = (StringNodeAttributeValue) attr;
				int matchCount = StringUtils.countMatches(snav.getValue(), ">");
				for (; matchCount > 0; matchCount--)
					advanceAfterNext(">");
			}
		pushNode(invocation);
	}

	protected void maybeIncludeContent(JspInstruction instruction, JspNodeWithChildren parent) {
		if ("include".equals(instruction.getName())) {
			String path = Utils.attr("file", instruction.getAttributes());
			String resolvedPath = path;
			if (Utils.isEmpty(path))
				parsingError("Missing 'file' attribute");

			if (!path.startsWith("/")) {
				// relative path needs translation
				File currentJsp = new File(pagePath);
				File includedFile = new File(currentJsp.getParentFile(), path);
				resolvedPath = includedFile.getPath();
			}
			byte[] content = location.getContents(resolvedPath);
			if (content == null)
				parsingError("Content at '" + resolvedPath + "' not found");
			// TODO: configurable encoding
			JspText text = new JspText(Utils.string(content, "UTF-8"), getCurrentLocation());
			parent.getChildren().add(text);
		}
	}

	protected void processOpenScriptlet(JspPage page) {
		if (isNext(OPEN_COMMENT)) {
			flushText();
			advanceAfterNext(CLOSE_COMMENT);
		} else if (isNext(OPEN_INSTRUCTION)) {
			flushText();
			JspInstruction instruction = processInstruction();
			maybeRegisterTaglib(instruction);
			maybeIncludeContent(instruction, getCurrentNode());
			page.getChildren().add(instruction);
		} else if (isNext(OPEN_SCRIPTLET_RETURN_VALUE)) {
			flushText();
			JspScriptlet scriptlet = processScriptlet();
			getCurrentNode().getChildren().add(scriptlet);
		} else {
			flushText();
			JspScriptlet scriptlet = processScriptlet();
			getCurrentNode().getChildren().add(scriptlet);
		}
	}

	protected boolean processTag(JspPage page, int index) {
		// calling remaining() is slow, so we run a fast plausibility
		// check first
		TagParser.Tag tag = tagParser.mightContainDeclaration(jsp, index)
				? tagParser.parse(remaining(), getCurrentLocation()) : null;
		if (tag != null) {
			switch (tag.type) {
			case opening:
				processOpenTaglib(tag.taglib);
				break;
			case openingAndClosing:
				processOpenCloseTaglib(tag.taglib);
				break;
			case closing:
				processCloseTaglibAndAdvance(tag.taglib);
				break;
			default:
				throw new RuntimeException("Unknown tag state");
			}
			return true;
		}
		return false;
	}

	/**
	 * General contract of {@link JspParser#parseJspFragment(String)} holds.
	 * This method, in addition, parses also tagfiles.
	 */
	@Override
	public JspPage parseJspFragment(String path) {
		try {
			this.pagePath = path;
			// TODO: configurable encoding
			byte[] fileContent = location.getContents(path);
			if (fileContent == null)
				throw new JspResourceNotFoundException("Location '" + path + "' not found.", getCurrentLocation());
			this.jsp = Utils.string(fileContent, "UTF-8");
			this.index = 0;
			JspPage page = new JspPage(path, getCurrentLocation());
			nodeStack.add(page);
			while (index < jsp.length()) {
				if (isNext(OPEN_SCRIPTLET)) {
					processOpenScriptlet(page);
					continue;
				}
				if (!processTag(page, index)) {
					buffer.append(jsp.charAt(index));
					advance(1);
				}
			}
			flushText();
			return page;
		} catch (JspParsingException pe) {
			throw new JspParsingException(pe, getCurrentLocation());
		}
	}

	@Override
	public ResourceResolver getLocationResolver() {
		return location;
	}

	@Override
	public TaglibDefinitionCache getTaglibDefinitions() {
		return taglibDefinitions;
	}

	@Override
	public void registerTaglibDefinition(String path, TaglibDefinition definition) {
		taglibDefinitions.setDefinition(path, definition);
	}

	@Override
	public void registerTaglibDefinition(String namespace, String path, TaglibDefinition definition) {
		taglibNamespaces.put(namespace, path);
		taglibDefinitions.setDefinition(path, definition);
	}

	@Override
	public String explain(JspParsingException exception) {
		Throwable cause = exception.getCause();
		String causeExplanation = "";
		if (cause != null && cause != exception) {
			if (cause instanceof JspParsingException)
				causeExplanation = explain((JspParsingException) cause);
			else
				causeExplanation = cause.getMessage();
		} else
			causeExplanation = exception.getMessage();
		return causeExplanation + "\n" + exception.getCodeLocation();
	}

	/**
	 * Removes instructions from a node and children since the parser already
	 * constructed all definitions. Removing instruction nodes slims down the
	 * page.
	 * 
	 * @param node
	 */
	// TODO: nodes should probably be removed during parsing already / not
	// constructed at all?
	protected void stripInstructions(JspNodeWithChildren node) {
		for (Iterator<JspNode> ite = node.getChildren().iterator(); ite.hasNext();) {
			JspNode child = ite.next();
			if (child instanceof JspNodeWithChildren)
				stripInstructions((JspNodeWithChildren) child);
			else if (child instanceof JspInstruction)
				ite.remove();
		}
	}

	@Override
	public JspPage parseJsp(String path) {
		JspPage page = parseJspFragment(path);
		stripInstructions(page);
		return page;
	}

}
