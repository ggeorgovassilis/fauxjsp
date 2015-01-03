fauxjsp
=======

JSP implementation with fast page reloads that uses an interpreter rather than a compiler.

## A JSP implementation for development (not production)

For most of you cool dudes JSP is horribly out of fashion, but I like using it for prototyping and [Tagfiles](http://docs.oracle.com/javaee/1.4/tutorial/doc/JSPTags5.html). Sadly and surprisingly, not many people know about tagfiles which is a well-supported and matured technique for creating reusable web components.

Because Tomcat's JSP implementation Jasper will compile JSP and tagfiles to java source code, then to byte code and then to machine code, starting a JSP-heavy application is slow. Also, when making changes to JSP files, the entire process has to be gone through again, which slows down development. At some point you'll even get unexplainable compile errors, class loading errors, run out of memory and the likes, you'll know you've had enough and restart Tomcat.

Fauxjsp implements a JSP interpreter and a servlet which reads JSP files and interprets them on the fly, skipping compilation altogether. This brings the benefit of instant page reloads, fast application start times and robustness (no classloader fiddling!) but, obviously, the generated JSP pages are slower under load than the standard implementation.

Currently implemented features:

* Reads JSP pages
* Reads tagfiles
* Supports some core JSTL taglibs
* Is modular and extensible

Constraints and missing features:

* Cannot use taglibs out of the box. You have to provide your own implementations of taglibs other than core taglibs. This means that c:out will work but
  you can't use third party taglibs such as [displaytag](http://www.displaytag.org) (unless you re-implement it for fauxjsp).
* I didn't read up on JSP/JSTL/servlet specifications. This implementation is "steer by sight" (aka "works for me").
* Features of the core taglibs are still very limited
* No scriptlets, at all
* Your servlet container needs to provide some EL 3.0 implementation (i.e. works with Tomcat 8, not with Tomcat 7)

## Getting started

1. Check out and compile
2. Put the JAR in your webapp's WEB-INF/lib
3. Declare an instance of the JspServlet in web.xml:

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

* JSP API
* Java EL 3.0 API
* JSTL
* Servlet API
* commons-lang 2.6
* log4j 4

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

```JspServlet``` has a bunch of protected methods which construct the various factories mentioned earlier. Should you need special setups,
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


## Roadmap

Features to come in the near future:

* jsp:include, jsp:attribute, jsp:var, import
* fmt, fn
* spring mvc taglibs

Science fiction (things I have a rough idea how to implement but need to work out yet and may never come):

* wrapper for using any third-party taglib
* jsp debugger
* scriptlets
* running in more environments

## License

Fauxjsp is available under the [GPL](http://www.gnu.org/copyleft/gpl.html). Since fauxjsp is a development tool, you normally wouldn't deploy it with your
application binaries into production, so the "non-commerical" aspect of the GPL doesn't affect your application.
