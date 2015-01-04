package fauxjsp.impl.taglibs;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fauxjsp.api.nodes.TaglibDefinition;
import fauxjsp.impl.Utils;

public class TaglibDiscovery {
	
	public static class DiscoveredTaglibs{
		public String uri;
		public List<TaglibDefinition> taglibs;
	}

	protected Element getSingleChild(Element parent, String elementName) {
		NodeList list = parent.getElementsByTagName(elementName);
		for (int i = 0; i < list.getLength(); i++) {
			Node n = list.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE)
				return (Element) n;
		}
		return null;
	}

	protected String getTextValueOrDefault(Element element, String fallback) {
		if (element == null)
			return fallback;
		return element.getTextContent();
	}

	protected TaglibDefinition.AttributeDefinition processAttribute(
			Element eAttribute) {
		String name = getSingleChild(eAttribute, "name").getTextContent();
		String type = getTextValueOrDefault(getSingleChild(eAttribute, "type"),
				"java.lang.String");
		boolean required = Boolean.parseBoolean(getSingleChild(eAttribute,
				"required").getTextContent());
		boolean rtexprvalue = Boolean.parseBoolean(getSingleChild(eAttribute,
				"rtexprvalue").getTextContent());
		return new TaglibDefinition.AttributeDefinition(name, type,
				rtexprvalue, required);
	}

	protected TaglibDefinition processTag(Element eTag) throws Exception {
		String tagName = getSingleChild(eTag, "name").getTextContent();
		String tagClass = getSingleChild(eTag, "tag-class").getTextContent();
		TaglibDefinition def = new ClassTaglibDefinition(tagName,
				Class.forName(tagClass));

		NodeList attributeList = eTag.getElementsByTagName("attribute");
		for (int i = 0; i < attributeList.getLength(); i++) {
			Node nAttribute = attributeList.item(i);
			if (nAttribute.getNodeType() == Node.ELEMENT_NODE) {
				TaglibDefinition.AttributeDefinition attributeDefinition = processAttribute((Element) nAttribute);
				def.getAttributes().put(attributeDefinition.getName(),
						attributeDefinition);
			}
		}
		return def;

	}

	protected DiscoveredTaglibs scanTaglib(String classPathResourceName) {

		try {
			
			DiscoveredTaglibs discovery = new DiscoveredTaglibs();
			discovery.taglibs = new ArrayList<TaglibDefinition>();
			byte[] content = Utils.readClassPathResource(classPathResourceName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new ByteArrayInputStream(content));
			
			discovery.uri = getSingleChild(doc.getDocumentElement(), "uri").getTextContent();
			
			NodeList tagList = doc.getElementsByTagName("tag");
			for (int i = 0; i < tagList.getLength(); i++) {
				Node nTag = tagList.item(i);
				if (nTag.getNodeType() == Node.ELEMENT_NODE) {
					TaglibDefinition taglib = processTag((Element) nTag);
					discovery.taglibs.add(taglib);
				}
			}
			return discovery;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<DiscoveredTaglibs> scanForTaglibs() {
		return Arrays.asList(scanTaglib("META-INF/c.tld"), scanTaglib("META-INF/fmt.tld"));
	}
}
