fauxjsp
=======

JSP replacement implementation aimed at speeding up developing JSP-heavy web pages. It features fast page reloads by relying on an interpreter rather than a compiler.

## Another JSP implementation?

In short:
a. there aren't that many (I only know of one)
b. it's much faster for development and reduces application start times compared to the standard implementation
c. no memory leaks, more robust, more precise and helpful error messages for syntax- and runtime errors
d. extensible

For most of you cool dudes JSP is horribly out of fashion, but I like using it for prototyping and [tagfiles](http://docs.oracle.com/javaee/1.4/tutorial/doc/JSPTags5.html). Sadly and surprisingly, not many people know about tagfiles despite them being a well-supported and mature technique for creating reusable web UI components.

Starting a JSP-heavy application is slow because the widely used JSP implementation Jasper will first compile JSP and tagfiles to java source code, then to byte code and then to machine code. Also, when making changes to JSP files, the entire process has to be repeated which slows down development. At some point you'll even get unexplainable compile errors, class loading errors, run out of memory and the likes, you'll know you've had enough and you'll restart the servlet container.

Fauxjsp implements a JSP interpreter and a servlet which reads JSP files and interprets them on the fly, skipping compilation altogether. This brings the benefit of instant page reloads, fast application start times and robustness (no classloader fiddling!) but, obviously, the generated JSP pages are slower under load than the standard implementation and thus fauxjsp shouldn't be used for production.

Currently implemented features:

* Reads JSP pages
* Reads tagfiles
* Supports some core JSTL taglibs
* Is modular and extensible

Constraints and missing features:

* Cannot use third-party taglibs. You have to provide your own implementations of taglibs other than core taglibs. This means that c:out will work but
  you can't use third party taglibs such as [displaytag](http://www.displaytag.org) (unless you re-implement it for fauxjsp).
* Not all core taglibs are supported and not all features of the supported ones are implemented.
* I didn't read up on JSP/JSTL/servlet specifications. This implementation is "steer by sight" (aka "works for me").
* No scriptlets, at all
* Your servlet container needs to provide some EL 3.0 implementation (i.e. works with Tomcat 8, not with Tomcat 7)
* Variable scoping is arbitrary

## Getting started

1) download sources and compile
```sh
git clone https://github.com/ggeorgovassilis/fauxjsp.git
cd fauxjsp
mvn install
```

2) add the dependency to your project

```xml
<dependency>
	<groupId>com.github.ggeorgovassilis</groupId>
	<artifactId>fauxjsp</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>
```

3) Declare an instance of the JspServlet in web.xml:

```xml
<servlet>
        <servlet-name>FauxJsp</servlet-name>
        <servlet-class>fauxjsp.servlet.JspServlet</servlet-class>
</servlet>

<servlet-mapping>
	<servlet-name>FauxJsp</servlet-name>
	<url-pattern>*.jsp</url-pattern>
</servlet-mapping>
```

## Run-time Dependencies

* JSP API 2.0
* Java EL 3.0 API
* JSTL 1.2
* Servlet API 3.0
* commons-lang 2.6
* log4j 4

For up-to-date details, [please see the pom](pom.xml).

## Extending fauxjsp's functionality and working around bugs

Please consider submit a bug report when you find a bug or require a feature implemented. Now, in real (project) life, 
you are probably on a tight schedule and don't want to wait for an official fix. Fauxjsp is modular and easily
extensible. So have a look at the next chapter about fauxjsp's architecture which will help you understand the various,
rather simple components, how to modify them and implement new functionality.

## Architecture overview

### JspServlet

```JspServlet``` accepts an HTTP request and renders the requested JSP. The entire sequence looks like this:

1. Gets the requested JSP file's name from the ```ServletRequest```
2. Looks at ```jspbase``` (see javadocs for ```JspServlet```) for the JSP file
3. Asks the ```JspParserFactory``` for a new ```JspParser```
4. Gives the JSP file to the ```JspParser``` who parses it, recursively resolves dependencies and returns a ```JspPage```
5. Creates a new ```RenderSession```
6. Asks the ```JspRendererFactory``` for a new ```JspRenderer```
7. Gives the ```JspPage``` and ```RenderSession``` to the ```JspRenderer``` who renders it
8. Streams the results to the browser

```JspServlet``` has a bunch of protected methods which construct the various factories mentioned earlier. Should you need special setups
then overriding these constructor methods allows you to specify your own factories which can return modified or completely new implementations
of parsers and renderers.

### JspParser

The ```JspParser``` and more specifically it's only implementation ```JspParserImpl``` is given a JSP file location, renders it and returns
the parsed results. In detail:

1. ```JspParser.parse(path)``` is given the path of the JSP file to render
2. The parser gives the path to a ```ResourceResolver``` instance (the ```JspParserFactory``` sets that one up) which returns
   the JSP file as a string.
3. The parser reads through the JSP file creating a tree of ```JspNode```s.
4. When the parser finds taglib (or tagfile) declarations, it puts them into the ```TagLibDefinitionCache``` which makes sure that
   taglibs are read only once during each request.
5. When the parser meets tagfile declarations, it asks the ```JspParserFactory``` for a new parser who recursively parses the tagfile.

Unfortunately, currently it is not possible to load taglibs other than tagfiles. However it is possible to fake missing taglibs by providing a special
implementation for fauxjsp. For some examples, have a look at the ```JstlCoreTaglib*``` classes. The ```DefaultJspParserFactoryImpl``` factory sets those
up under a special namespace, one for each taglib method.

## Supported taglibs

### Directives

```xml
<%@ taglib prefix uri tagdir%>

<@ include file %>

<%@ attribute name required rtexprvalue type %>

<jsp:attribute name="...">...</jsp:attribute>

<jsp:body>...</jsp:body>

<jsp:doBody/>
```

### Core taglib

```xml
<c:out value="..."/>

<c:choose test="...">...</c:choose>

<c:when test="...">...</c:when>

<c:otherwise>...</c:otherwise>

<c:forEach var="..." items="..." varStatus="..." begin="..." end="...">...</c:forEach>

<c:if test="...">...</c:if>

<c:set var="..." value="..."/>

```

### Formatting and internationalization

```xml
<fmt:message key="..."/>

<fmt:setBundle basename="..."/>

<fmt:formatNumber value="..." .../>

<fmt:formatDate value="..." .../>

<fmt:setLocale value="..."/>

```

### Functions

Fauxjsp delegates to the standard JSTL function implementation used by the application server, so everything should work out of the box.

If you need more function taglibs, don't forget to declare them in web.xml:
```xml
<jsp-config> 
	<taglib>
		<taglib-uri>http://java.sun.com/jstl/core-rt<taglib-uri>
		<taglib-location>META-INF/c-1_0-rt.tld</taglib-location>
	</taglib>
</jsp-config>
```
taglib-location can be a server- or classpath resource path. 

## How do I ...

### ... add a missing tag library?

As written before, fauxjsp can't use taglibs and has to emulate them instead, which means that someone has to program that emulation.

*Step 1*: create the taglib implementation. Just find one of the already simulated taglibs like [JstlCoreTaglibOut](src/main/java/fauxjsp/impl/simulatedtaglibs/core/JstlCoreTaglibOut.java) and copy & paste it

```java

public class TaglibAdd extends TaglibDefinition{

	protected void runAdd(RenderSession session, JspTaglibInvocation invocation) {
		String xExpression = invocation.getArguments().get("x");
		if (xExpression == null)
			throw new RuntimeException("Missing x argument");
		Object x = session.elEvaluation.evaluate(xExpression, session);

		String yExpression = invocation.getArguments().get("y");
		if (yExpression == null)
			throw new RuntimeException("Missing y argument");
		Object y = session.elEvaluation.evaluate(yExpression, session);
		
		try {
		    int ix = (Number)x;
		    int iy = (Number)y;
		    String s = ""+(ix+iy);
			session.response.getOutputStream().write((s).getBytes(session.response.getCharacterEncoding()));
		} catch (Exception e) {
			throw new JspRenderException(invocation, e);
		}
	}

	@Override
	public void render(RenderSession session, JspTaglibInvocation invocation) {
		runAdd(session, invocation);
	}
	
	public TaglibAdd() {
		this.name="add";
		this.attributes.put("x", new AttributeDefinition("x", Integer.class.getName(), true, true));
		this.attributes.put("y", new AttributeDefinition("y", Integer.class.getName(), true, true));
	}
}


```

*Step 2*: extend [DefaultJspParserFactoryImpl](src/main/java/fauxjsp/impl/parser/DefaultJspParserFactoryImpl.java) and override the ```setup``` method. Here you can register the new taglib you wrote

```java
	public class MyJspParserFactory extends DefaultJspParserFactoryImpl{
		
	protected void setup(JspParserImpl parser) {
	  super.setup(parser);
		parser.registerTaglibDefinition(
				"http://mytaglibs/add",
				new TaglibAdd());
	}		
	}

```

*Step 3*: extend [JspServlet](src/main/java/fauxjsp/servlet/JspServlet.java) and override ```getJspParserFactory``` so that it returns your new factory

```java
	public class MyJspServlet extends JspServlet{

	protected JspParserFactory getJspParserFactory(ServletConfig config){
		ResourceResolver location = new ServletResourceResolver(jspBase, getServletContext());
		DefaultJspParserFactoryImpl factory = new MyJspParserFactory(location);
		return factory;
	}
		
	}

```


*Step 4*: use your new JspServlet implementation in web.xml instead of the old JspServlet 

```xml
<servlet>
        <servlet-name>FauxJsp</servlet-name>
        <servlet-class>MyJspServlet</servlet-class>
</servlet>

<servlet-mapping>
	<servlet-name>FauxJsp</servlet-name>
	<url-pattern>*.jsp</url-pattern>
</servlet-mapping>
```

### ... enable logging to see what's going on?

```JspServlet``` logs through log4j, if found on the classpath, otherwise through the standard JDK logging mechanism.
A possible logging setup in ```log4j.properties``` could look like this:

```
log4j.rootLogger=INFO, stdout
log4j.logger.fauxjsp=INFO, stdout
log4j.additivity.fauxjsp.impl.parser.JspParserImpl=false

log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target=System.out
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n
```

## Roadmap

Features to come in the near future:

* jsp:include, ~~jsp:attribute~~, jsp:var, import
* fmt, ~~fn~~
* spring mvc taglibs

Science fiction (things I have a rough idea how to implement but need to work out yet and may never come):

* wrapper for using any third-party taglib
* jsp debugger
* scriptlets
* running in more environments

## License

Fauxjsp is available under the [GPL](http://www.gnu.org/copyleft/gpl.html). Since fauxjsp is a development tool, you normally wouldn't deploy it with your
application binaries into production, so the "non-commerical" aspect of the GPL doesn't affect your application.
