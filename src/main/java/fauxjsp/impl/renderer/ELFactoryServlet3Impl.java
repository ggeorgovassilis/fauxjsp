package fauxjsp.impl.renderer;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.el.ELContext;
import javax.el.ELManager;
import javax.el.ExpressionFactory;
import javax.el.StandardELContext;
import javax.servlet.ServletConfig;
import javax.servlet.descriptor.JspConfigDescriptor;
import javax.servlet.descriptor.TaglibDescriptor;

import fauxjsp.api.logging.Logger;
import fauxjsp.impl.Utils;
import fauxjsp.impl.logging.Logging;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

/**
 * Looks up expression factories and ElContexts from an EL 3.0 {@link ELManager}
 * in a typical server environment.
 * 
 * @author George Georgovassilis
 *
 */
public class ELFactoryServlet3Impl implements ELFactory {

	protected Logger log = Logging.getLogger(ELFactoryServlet3Impl.class);
	protected Pattern pMethodSignature = Pattern.compile("(.+)\\s+(.+)\\((.*)\\)");
	protected Map<String, List<Method>> prefixToFunctions = new HashMap<>();

	protected void setupFunctions(ELContext context) {
		for (String prefix : prefixToFunctions.keySet()) {
			for (Method method : prefixToFunctions.get(prefix)) {
				context.getFunctionMapper().mapFunction(prefix, method.getName(), method);
			}
		}
	}

	protected void registerTaglib(DocumentBuilder dBuilder, ServletConfig config, String uri, String location)
			throws Exception {
		log.debug("Loading taglib " + uri + " from " + location);
		InputStream in = null;
		try {
			in = config.getServletContext().getResourceAsStream(location);
		} catch (Exception e) {
		}
		if (in == null) {
			in = getClass().getClassLoader().getResourceAsStream(location);
		}
		if (in == null) {
			log.warn(
					"Taglib location '" + location + "' can't be resolved to either a server- nor classpath location.");
			return;
		}
		try {
			Document doc = dBuilder.parse(in);
			doc.getDocumentElement().normalize();
			registerFunctions(doc);
		} finally {
			Utils.close(in);
		}
	}

	public void configure(ServletConfig config) {
		try {
			log.debug("Scanning for taglibs...");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			try {
				// try to find the default function taglib
				registerTaglib(dBuilder, config, "http://java.sun.com/jsp/jstl/functions", "META-INF/fn.tld");
			} catch (Exception e) {
				log.warn(
						"Couldn't find or problem reading META-INF/fn.tld. Either a dependency (jstl.jar ?) is missing, or you should declare one with a jsp-config element in web.xml");
			}
			JspConfigDescriptor jspConfig = config.getServletContext().getJspConfigDescriptor();
			if (jspConfig != null) {
				Collection<TaglibDescriptor> taglibs = jspConfig.getTaglibs();
				if (taglibs != null)
					for (TaglibDescriptor taglib : taglibs)
						try {
							registerTaglib(dBuilder, config, taglib.getTaglibURI(), taglib.getTaglibLocation());
						} catch (Exception e) {
							log.error("Failed registering taglib " + taglib.getTaglibLocation()
									+ ", expect subsequent failures.", e);
						}
			}
		} catch (Exception e) {
			throw Utils.softenException(e);
		}
	}

	protected void registerFunctions(Document doc) throws Exception {
		String shortName = doc.getElementsByTagName("short-name").item(0).getTextContent();
		NodeList lFunction = doc.getElementsByTagName("function");
		for (int i = 0; i < lFunction.getLength(); i++) {
			Node nFunction = lFunction.item(i);
			registerFunction(shortName, nFunction);
		}
	}

	protected Class<?> getType(String type) throws Exception {
		type = type.replace('/', '.');
		Class<?> c = null;
		if ("int".equals(type))
			c = Integer.TYPE;
		else if ("long".equals(type))
			c = Long.TYPE;
		else if ("float".equals(type))
			c = Float.TYPE;
		else if ("double".equals(type))
			c = Double.TYPE;
		else if ("short".equals(type))
			c = Short.TYPE;
		else if ("byte".equals(type))
			c = Byte.TYPE;
		else if ("boolean".equals(type))
			c = Boolean.TYPE;
		else if ("char".equals(type))
			c = Character.TYPE;
		if (type.contains("[]")) {
			type = type.replaceAll("\\[", "").replaceAll("\\]", "");
			if (c == null)
				c = Utils.getClassForName(type);
			c = Array.newInstance(c, 0).getClass();
		}
		if (c == null)
			c = Utils.getClassForName(type);
		return c;
	}

	protected void registerFunction(String shortName, Node nFunction) throws Exception {
		String fnClass = ((Element) nFunction).getElementsByTagName("function-class").item(0).getTextContent();
		String fnSignature = ((Element) nFunction).getElementsByTagName("function-signature").item(0).getTextContent();
		Matcher m = pMethodSignature.matcher(fnSignature);
		if (!m.matches()) {
			log.error("Can't parse taglib function method signature " + fnSignature);
			return;
		}
		String returnType = m.group(1);
		String methodName = m.group(2);
		String arguments[] = m.group(3).split(",");
		List<Class<?>> cArgs = new ArrayList<>();
		for (String arg : arguments) {
			arg = arg.trim();
			if (!arg.isEmpty())
				cArgs.add(getType(arg));
		}

		log.debug("Registering taglib method " + returnType + " " + methodName + "(" + arguments + ")");
		Method method = Utils.getClassForName(fnClass).getMethod(methodName, cArgs.toArray(new Class[0]));
		List<Method> functions = prefixToFunctions.get(shortName);
		if (functions == null) {
			functions = new ArrayList<Method>();
			prefixToFunctions.put(shortName, functions);
		}
		functions.add(method);
	}

	@Override
	public ExpressionFactory newExpressionFactory() {
		return ELManager.getExpressionFactory();
	}

	@Override
	public ELContext newElContext() {
		ELContext context = new StandardELContext(newExpressionFactory());
		setupFunctions(context);
		return context;
	}
	
	@Override
	public ELContext newElContext(ELContext parent) {
		ELContext context = new StandardELContext(parent);
		return context;
	}

}
