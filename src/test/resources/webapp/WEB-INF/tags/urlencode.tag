<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="url" required="true" rtexprvalue="true" type="java.lang.String"%>
<%= java.net.URLEncoder.encode(pageContext.getAttribute("url"),"UTF-8") %>