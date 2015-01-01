package fauxjsp.impl.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import fauxjsp.api.nodes.JspInstruction;
import fauxjsp.api.nodes.JspNode;
import fauxjsp.api.nodes.JspNodeWithChildren;
import fauxjsp.api.nodes.JspPage;
import fauxjsp.api.nodes.JspTaglibInvocation;
import fauxjsp.api.nodes.JspText;
import fauxjsp.api.nodes.TagfileDefinition;
import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.api.nodes.TaglibDefinition.AttributeDefinition;
import fauxjsp.api.parser.CodeLocation;
import fauxjsp.api.parser.JspParser;
import fauxjsp.api.parser.JspParserFactory;
import fauxjsp.api.parser.JspParsingException;
import fauxjsp.api.parser.ResourceResolver;
import fauxjsp.impl.Utils;
import fauxjsp.impl.tagparser.TagParser;

public class JspParserImpl implements JspParser {

	// TODO: our impl doesn't work with < or > in attributes, i.e. <when
	// test="${a<b}"> breaks. Fix that.
	protected final String OPEN_INSTRUCTION = "<%@";
	protected final String OPEN_SCRIPTLET = "<%";
	protected final String CLOSE_INSTRUCTION = "%>";

	protected String jspName;
	protected String jsp;
	protected int index;
	protected StringBuilder buffer = new StringBuilder();
	protected Map<String, String> taglibNamespaces = new HashMap<String, String>();
	protected TaglibDefinitionCache taglibDefinitions = new TaglibDefinitionCache();
	protected List<JspNodeWithChildren> nodeStack = new ArrayList<JspNodeWithChildren>();
	protected ResourceResolver location;
	protected Logger logger = Logger.getLogger(JspParserImpl.class);
	protected JspParser parentParser;
	protected JspParserFactory parserFactory;
	protected TagParser tagParser = new TagParser();
	protected int line = 0;
	protected int column = 0;

	// TODO: speed: keep a running pointer instead of recomputing the location every
	// time. this is important because this function is called every time a node
	// is constructed
	protected CodeLocation getCurrentLocation() {
		return new CodeLocation(jspName, line + 1, column + 1);
	}

	protected void parsingError(String message) throws JspParsingException {
		throw new JspParsingException(message, getCurrentLocation());
	}

	public JspParserImpl(ResourceResolver location, JspParserFactory factory) {
		this.parserFactory = factory;
		this.location = location;
	}

	// TODO: speed: construct a parsing tree and check ("remains") based on different
	// groups of the same substring length
	public JspParserImpl(JspParserFactory factory, JspParser parent) {
		this.parserFactory = factory;
		this.parentParser = parent;
		this.location = parent.getLocationResolver();
		this.taglibDefinitions = parent.getTaglibDefinitions();
	}

	protected void advanceCodeLocation(int length){
		int end = index+length;
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
	}
	
	protected void advance(int length) {
		if (length < 1)
			throw new IllegalArgumentException("Length is " + length);
		advanceCodeLocation(length);
		index += length;
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

	protected JspTaglibInvocation pullNode(String fullyQuallifiedTaglib) {
		if (nodeStack.isEmpty())
			parsingError("Found closing " + fullyQuallifiedTaglib
					+ " but don't remember opening any");
		JspNode node = getCurrentNode();
		if (!(node instanceof JspTaglibInvocation))
			parsingError("Trying to close " + fullyQuallifiedTaglib
					+ " but the current open node is not a taglib but " + node);
		JspTaglibInvocation latestOpenedTaglib = (JspTaglibInvocation) node;
		if (!latestOpenedTaglib.getName().equals(fullyQuallifiedTaglib))
			parsingError("Found closing " + fullyQuallifiedTaglib
					+ " but expected closing " + latestOpenedTaglib.getName());
		nodeStack.remove(nodeStack.size() - 1);
		return latestOpenedTaglib;
	}

	protected void advanceToNext(String what) {
		int offset = jsp.indexOf(what, index);
		if (offset == -1)
			parsingError("Expected to find '" + what + "' after offset "
					+ offset);
		advance(offset-index);
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
		if (index + what.length() >= jsp.length())
			return false;
		return what.equals(substring(what.length()));
	}

	protected boolean toBool(String s, boolean defaultValue) {
		try {
			return Boolean.parseBoolean(s);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	protected TaglibDefinition getOrLoadDefinition(
			JspTaglibInvocation invocation) {
		logger.trace("Looking for implementation of taglib "
				+ invocation.getName());
		String fullQuallifiedName = invocation.getName();
		String namespace = invocation.getNamespace();
		String path = taglibNamespaces.get(namespace);
		if (path == null)
			parsingError("Unknown taglib namespace " + namespace);
		String fullPath = null;
		if (!path.startsWith("http:")) {
			fullPath = path + "/" + invocation.getTaglib() + ".tag";
		} else
			fullPath = path+"/"+invocation.getTaglib();
		TaglibDefinition definition = taglibDefinitions.getDefinition(fullPath);
		if (definition == null) {
			if (path.startsWith("http:")) {
				parsingError("Unknown taglib "
						+ path
						+ ". Either you misspelled the uri or you need to write an emulation for this taglib.");
			} else {
				logger.debug(invocation.getName()
						+ " is a tagfile, running parser");
				JspParser tagfileParser = parserFactory.create(this);
				JspPage page = tagfileParser.parse(fullPath);
				TagfileDefinition tagfileDefinition = new TagfileDefinition();
				definition = tagfileDefinition;
				tagfileDefinition.setBody(page);
				for (JspNode child : page.getChildren())
					if (child instanceof JspInstruction) {
						JspInstruction instruction = (JspInstruction) child;
						if (instruction.getName().equals("attribute")) {
							String attributeName = instruction.getArguments()
									.get("name");
							String sAttributeRequired = instruction
									.getArguments().get("required");
							String sType = instruction.getArguments().get(
									"type");
							String sRTValue = instruction.getArguments().get(
									"rtexprvalue");

							TaglibDefinition.AttributeDefinition attributeDefinition = new TaglibDefinition.AttributeDefinition(
									attributeName, sType,
									toBool(sRTValue, true), toBool(
											sAttributeRequired, true));
							tagfileDefinition.getAttributes().put(
									attributeName, attributeDefinition);
						}
					}
			}
			definition.setName(fullQuallifiedName);
			taglibDefinitions.setDefinition(fullPath, definition);
		} else
			logger.trace("Found " + fullPath + " in cache");
		return definition;
	}

	protected JspInstruction processInstruction() {
		if (!OPEN_INSTRUCTION.equals(substring(OPEN_INSTRUCTION.length())))
			parsingError("Expected open instruction");
		int closingIndex = nextIndexOf(CLOSE_INSTRUCTION);
		if (closingIndex <= index)
			parsingError("Expected closing instruction");

		String sInstruction = substring(CLOSE_INSTRUCTION.length()
				+ closingIndex - index);
		sInstruction = sInstruction.substring(0, sInstruction.indexOf('@'))
				+ sInstruction.substring(sInstruction.indexOf('@') + 1);
		sInstruction = sInstruction.substring(0, sInstruction.indexOf('%'))
				+ sInstruction.substring(sInstruction.indexOf('%') + 1);
		sInstruction = sInstruction.substring(0, sInstruction.lastIndexOf('%'))
				+ sInstruction.substring(sInstruction.lastIndexOf('%') + 1);
		sInstruction = sInstruction.replaceFirst("< ", "<");
		sInstruction = sInstruction.replaceFirst("<", "<__:");
		TagParser.Tag tag = tagParser.parse(sInstruction, getCurrentLocation());
		if (tag == null)
			parsingError("Can't parse instruction from " + sInstruction);
		JspInstruction instruction = new JspInstruction(tag.taglib.getTaglib(),
				getCurrentLocation());
		instruction.getArguments().putAll(tag.taglib.getArguments());
		advanceAfterNext("%>");
		return instruction;
	}

	protected void flushText() {
		if (buffer.length() > 0) {
			String content = buffer.toString();
			JspText text = new JspText(content.getBytes(), getCurrentLocation());
			JspNodeWithChildren currentNode = getCurrentNode();
			currentNode.getChildren().add(text);
			buffer.setLength(0);
		}
	}

	protected void maybeRegisterTaglib(JspInstruction instruction) {
		if (instruction.getName().equals("taglib")) {
			String namespace = instruction.getArguments().get("prefix");
			if (Utils.isEmpty(namespace))
				parsingError("Missing prefix attribute on taglib");
			if (taglibNamespaces.containsKey(namespace))
				parsingError("Taglib prefix '" + namespace + "' already used.");
			String path = null;
			if (instruction.getArguments().containsKey("tagdir")) {
				path = instruction.getArguments().get("tagdir");
			} else if (instruction.getArguments().containsKey("uri")) {
				path = instruction.getArguments().get("uri");
			} else
				parsingError("Taglib declaration requires either a uri or tagdir attribute");
			taglibNamespaces.put(namespace, path);
		}
	}

	protected void processCloseTaglib(JspTaglibInvocation taglib) {
		logger.trace("Closing " + taglib.getName());
		if (nodeStack.isEmpty())
			parsingError("Tag " + taglib + " closing at offset " + index
					+ " without prior opening");
		flushText();
		JspTaglibInvocation jspTaglib = pullNode(taglib.getName());
		JspNodeWithChildren currentNode = getCurrentNode();
		currentNode.getChildren().add(jspTaglib);
		advance(taglib.getName());
		advanceAfterNext(">");
	}

	protected void processOpenCloseTaglib(JspTaglibInvocation taglib)
			throws Exception {
		flushText();
		logger.trace("Open-Closing " + taglib.getName());
		advanceAfterNext("/>");
		// we don't _really_ have to go through push/pull, but it's a more
		// general approach and might come in handy later
		taglib.setDefinition(getOrLoadDefinition(taglib));
		verifyInvocation(taglib, taglib.getDefinition());
		pushNode(taglib);
		taglib = pullNode(taglib.getName());
		if (nodeStack.isEmpty())
			throw new IllegalArgumentException("Tag " + taglib
					+ " closing at offset " + index + " without prior opening");
		flushText();
		JspNodeWithChildren currentNode = getCurrentNode();
		currentNode.getChildren().add(taglib);
	}

	protected void processOpenTaglib(JspTaglibInvocation taglib)
			throws Exception {
		flushText();
		logger.trace("Opening " + taglib.getName());
		taglib.setDefinition(getOrLoadDefinition(taglib));
		verifyInvocation(taglib, taglib.getDefinition());
		advanceAfterNext(">");
		pushNode(taglib);
	}

	protected void verifyInvocation(JspTaglibInvocation taglib,
			TaglibDefinition definition) {
		// check that all attributes required by the definition are present on
		// the invocation
		for (String attr : definition.getAttributes().keySet()) {
			AttributeDefinition def = definition.getAttributes().get(attr);
			if (def.isRequired() && !taglib.getArguments().containsKey(attr))
				parsingError("Mandatory argument '" + def.getName()
						+ "' is missing ob taglib invocation "
						+ taglib.getName());
		}

		// check that no arguments are present on the invocation that are not in
		// the definition
		for (String attr : taglib.getArguments().keySet()) {
			if (!definition.getAttributes().containsKey(attr))
				parsingError("Unknown argument '" + attr
						+ "' on taglib invocation " + taglib.getName());
		}
	}

	public JspPage parse(String path) {
		try {
			this.jspName = path;
			logger.debug("Parsing location " + path);
			this.jsp = new String(location.getContents(path));
			this.index = 0;
			JspPage page = new JspPage(path, getCurrentLocation());
			nodeStack.add(page);
			while (index < jsp.length()) {
				if (isNext(OPEN_SCRIPTLET)) {
					// we don't support scriptlets, but instructions start with
					// a similar declaration, so
					// we have to look a bit down the script to distinguish
					// instructions from scriptlets
					if (isNext(OPEN_INSTRUCTION)) {
						flushText();
						JspInstruction instruction = processInstruction();
						maybeRegisterTaglib(instruction);
						page.getChildren().add(instruction);
						continue;
					} else
						parsingError("This looks like a scriptlet. This parser doesn't support scriptlets.");
				}

				TagParser.Tag tag = tagParser.parse(remaining(),
						getCurrentLocation());
				if (tag != null) {
					if (tag.type == TagParser.Tag.TagType.opening)
						processOpenTaglib(tag.taglib);
					else if (tag.type == TagParser.Tag.TagType.openingAndClosing)
						processOpenCloseTaglib(tag.taglib);
					else if (tag.type == TagParser.Tag.TagType.closing)
						processCloseTaglib(tag.taglib);
					else
						throw new RuntimeException("Unknown tag state");
				} else {
					String nextChar = substring(1);
					buffer.append(nextChar);
					advance(nextChar);
				}
			}
			flushText();
			return page;
		} catch (JspParsingException pe) {
			throw new JspParsingException(pe, getCurrentLocation());
		} catch (Exception e) {
			throw new RuntimeException(e);
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
	public void registerTaglibDefinition(String path,
			TaglibDefinition definition) {
		taglibDefinitions.setDefinition(path, definition);
	}

	@Override
	public void registerTaglibDefinition(String namespace, String path,
			TaglibDefinition definition) {
		taglibNamespaces.put(namespace, path);
		taglibDefinitions.setDefinition(path, definition);
	}

	@Override
	public String explain(JspParsingException exception) {
		if (exception == null)
			return "";
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

}
