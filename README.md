fauxjsp
=======

JSP implementation with fast page reloads that uses an interpreter rather than a compiler. Documentation will follow :-)

## A JSP implementation for development (not production)

For most of you cool dudes JSP is horribly out of fashion, but I like using it for prototyping and [Tagfiles](http://docs.oracle.com/javaee/1.4/tutorial/doc/JSPTags5.html). Sadly and suprisingly, not many people know about tagfiles which is a well-supported and matured technique for creating reusable web components.

Because Tomcat's JSP implementation Jasper will compile JSP and tagfiles to java source code, then to byte code and then to machine code, starting a JSP-heavy application is slow. Also, when making changes to JSP files, the entire process has to be gone through again, which slow down development. At some point you'll even get unexplainable compile errors, class loading errors, run out of memory and the likes and you'll have to restart Tomcat.

Fauxjsp is a servlet which reads JSP files and interprets them on the fly. This brings the benefit of instant page reloads, fast application start times and robustness (no classloader fiddling!) but, obviously, the generated JSP pages are slower under load than the standard implementation.

Currently implemented features:

* Reads JSP pages
* Reads tagfiles
* Supports some core JSTL taglibs
* Is modular and extensible

Constraints and missing features:

* Cannot use taglibs out of the box. You have to provide your own implementations of taglibs other than core taglibs. This means that c:out will work but
  you can't use third party taglibs such as displaytag (unless you re-implement it for fauxjsp).
* I didn't read up on JSP/JSTL/servlet specifications. This implementation is "steer by sight" (aka "works for me").
* Features of the core taglibs are still very limited
* No scriptlets, at all
* You'll need some EL 3.0 implementation (i.e. works with Tomcat 8, not with Tomcat 7)

## Getting started

1. Check out and compile
2. Put the JAR in your webapp's WEB-INF/lib
3. Declare an instance of the JspServlet in web.xml:

<servlet>
        <servlet-name>FauxJsp</servlet-name>
        <servlet-class>fauxjsp.servlet.JspServlet</servlet-class>
</servlet>

<servlet-mapping>
	<servlet-name>FauxJsp</servlet-name>
	<url-pattern>*.jsp</url-pattern>
</servlet-mapping>

## Roadmap

Features to come in the near future:

* jsp:include, jsp:attribute, jsp:var
* fmt
* spring mvc taglibs

Science fiction (things I have a rough idea how to implement but need to work out yet):

* wrapper for using any third-party taglib
* jsp debugger
